package com.mahmoud.thoth.model;

import org.springframework.web.multipart.MultipartFile;

public class DocumentAnalysisRequest {
    private MultipartFile file;
    private String message;

    // Getters and Setters
    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
