package com.example.multiplechoiceexam.dto.fileAttach;

public class FileAttachDTO {

    Long id;

    String fileName;

    String filePath;

    String fileExt;

    public FileAttachDTO() {
    }

    public FileAttachDTO(Long id, String fileName, String filePath, String fileExt) {
        this.id = id;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileExt = fileExt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileExt() {
        return fileExt;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }
}
