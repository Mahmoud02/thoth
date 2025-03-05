package com.mahmoud.thoth.service;

import java.io.IOException;
import java.io.InputStream;

public interface StorageService {

    void uploadObject(String bucketName, String objectName, InputStream inputStream) throws IOException;
    byte[] downloadObject(String bucketName, String objectName) throws IOException;
}
