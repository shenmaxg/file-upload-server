package com.shenmax.fileuploadserver.service;

import com.shenmax.fileuploadserver.model.FileChunk;
import com.shenmax.fileuploadserver.model.FileModel;

import java.util.List;

public interface UploadService {

    Integer addChunk(FileChunk fileChunk);

    Integer getChunkNumByContentHash(String contentHash);

    Boolean removeChunkRecord(String contentHash);

    Boolean verifyContentHash(Long fileSize, String contentHash);

    List<FileChunk> getChunkListByContentHash(String contentHash);

    Integer addFile(FileModel fileModel);
}
