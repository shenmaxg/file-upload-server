package com.shenmax.fileuploadserver.service.Impl;

import com.shenmax.fileuploadserver.model.FileChunk;
import com.shenmax.fileuploadserver.model.FileModel;
import com.shenmax.fileuploadserver.service.UploadService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class UploadServiceImpl implements UploadService {

    // 模拟数据库操作 FileChunk
    private static Map<Integer, FileChunk> chunkMap;
    private static Integer chunkGlobalId = 1;

    // 模拟数据库操作 FileModel
    private static Map<Integer, FileModel> fileMap;
    private static Integer fileGlobalId = 1;

    static {
        chunkMap = new ConcurrentHashMap<>();
        fileMap = new ConcurrentHashMap<>();
    }

    @Override
    public Integer addChunk(FileChunk fileChunk) {
        fileChunk.setId(chunkGlobalId++);
        chunkMap.put(fileChunk.getId(), fileChunk);

        return chunkGlobalId;
    }

    @Override
    public Integer getChunkNumByContentHash(String contentHash) {
        long count = chunkMap.values().stream()
                .filter((chunk) -> chunk.getFileHash().equals(contentHash))
                .count();

        return Math.toIntExact(count);
    }

    @Override
    public Boolean removeChunkRecord(String chunkHash) {
        return chunkMap.values().removeIf(chunk -> chunk.getFileHash().equals(chunkHash));
    }

    @Override
    public Boolean verifyContentHash(Long fileSize, String contentHash) {
        return fileMap.values().stream()
                .anyMatch(file -> file.getContentHash().equals(contentHash)
                        && file.getFileSize().equals(fileSize));
    }

    @Override
    public List<FileChunk> getChunkListByContentHash(String contentHash) {
        return chunkMap.values().stream()
                .filter(chunk -> chunk.getFileHash().equals(contentHash))
                .collect(Collectors.toList());
    }

    @Override
    public Integer addFile(FileModel fileModel) {
        fileModel.setId(fileGlobalId++);
        fileMap.put(fileModel.getId(), fileModel);

        return fileGlobalId;
    }
}
