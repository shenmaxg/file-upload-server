package com.shenmax.fileuploadserver.controller;

import com.shenmax.fileuploadserver.model.FileChunk;
import com.shenmax.fileuploadserver.model.FileModel;
import com.shenmax.fileuploadserver.service.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/file")
public class UploadController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    UploadService uploadService;

    @Value("${file.target.dir}")
    private String filePath;

    private static final ConcurrentHashMap<String, Integer> times = new ConcurrentHashMap<>();

    /**
     * 上传完整的文件
     * @param file 文件
     * @return true | false
     */
    @PostMapping("/uploadSingle")
    @ResponseBody
    public Boolean upload(@RequestParam("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        File dest = new File(filePath + fileName);

        try  {
            file.transferTo(dest);
            return true;
        } catch (IOException e) {
            LOGGER.error(e.toString(), e);
        }

        return false;
    }

    /**
     * 上传文件前置任务
     * 1. 通过文件大小以及内容Hash判断是否已经存在该文件
     * 2. 如果不存在，返回已经上传的切片
     * @param fileSize      文件大小
     * @param contentHash  文件内容的全量 Hash
     * @return {uploaded: false, uploadedChunkList: [FileChunk, FileChunk]}
     */
    @PostMapping("/prepare")
    public Object prepare(@RequestParam("fileSize") Long fileSize, @RequestParam("contentHash") String contentHash) {
        Boolean uploaded = uploadService.verifyContentHash(fileSize, contentHash);
        List<FileChunk> fileChunkList = new ArrayList<>();

        if (uploaded.equals(Boolean.FALSE)) {
            fileChunkList = uploadService.getChunkListByContentHash(contentHash);
        }

        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("uploaded", uploaded);
        returnMap.put("uploadedChunkList", fileChunkList);

        return returnMap;
    }

    /**
     * 上传切片文件
     * @param chunk      切片文件
     * @param fileChunk  切片文件的源数据
     * @return true | false
     */
    @PostMapping("/uploadChunk")
    @ResponseBody
    public Boolean upload(@RequestParam("chunk") MultipartFile chunk,
                         FileChunk fileChunk) {
        String fullPath = filePath + fileChunk.getFileName();

        // 模拟上传出错
//        Integer uploadTimes = times.get(fileChunk.getChunkName());
//        if (uploadTimes == null) {
//            times.put(fileChunk.getChunkName(), 1);
//
//            return false;
//        }

        // 模块写入对应的位置
        try(RandomAccessFile rf = new RandomAccessFile(fullPath,
                "rw")) {
            rf.seek(fileChunk.getStart());
            rf.write(chunk.getBytes());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }

        // chunk 记录到数据库
        fileChunk.setFullPath(fullPath);
        uploadService.addChunk(fileChunk);

        // 文件全部上传完成
        Integer chunkSize = uploadService.getChunkNumByContentHash(fileChunk.getFileHash());
        if (chunkSize.equals(fileChunk.getChunkNum())) {
            // 删除 chunk 记录
            uploadService.removeChunkRecord(fileChunk.getFileHash());

            // 增加 file 记录
            FileModel fileModel = new FileModel(fileChunk.getFileName(),
                    fileChunk.getFullPath(),
                    fileChunk.getFileHash(),
                    fileChunk.getTotal(),
                    "SUCCESS");
            uploadService.addFile(fileModel);
        }

        return true;
    }
}
