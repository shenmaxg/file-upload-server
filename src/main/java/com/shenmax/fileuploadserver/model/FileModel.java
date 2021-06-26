package com.shenmax.fileuploadserver.model;

public class FileModel {

    public FileModel() {
    }

    public FileModel(String fileName, String fullPath, String contentHash, Long fileSize, String status) {
        this.fileName = fileName;
        this.fullPath = fullPath;
        this.contentHash = contentHash;
        this.fileSize = fileSize;
        this.status = status;
    }

    private Integer id;

    // 文件名
    private String fileName;

    // 全路径
    private String fullPath;

    // 全量 Hash
    private String contentHash;

    // 文件大小
    private Long fileSize;

    // 文件当前状态
    // todo 考虑多用户上传同一个文件问题
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getContentHash() {
        return contentHash;
    }

    public void setContentHash(String contentHash) {
        this.contentHash = contentHash;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
