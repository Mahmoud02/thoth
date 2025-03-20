package com.mahmoud.thoth.infrastructure.store.impl;

import com.mahmoud.thoth.domain.model.BucketMetadata;
import com.mahmoud.thoth.shared.JsonUtil;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class BucketPersistenceService {

    private static final String DATA_DIR = "data/buckets/";
    private static final String INDEX_FILE = DATA_DIR + "index.json";
    private Map<String, Long> index = new HashMap<>();

    public BucketPersistenceService() {
        loadIndex();
    }

    private void loadIndex() {
        try {
            File indexFile = new File(INDEX_FILE);
            if (indexFile.exists()) {
                index = JsonUtil.readFromFile(INDEX_FILE, Map.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveIndex() {
        try {
            JsonUtil.writeToFile(INDEX_FILE, index);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveBucketToFile(String bucketName, BucketMetadata metadata) {
        try (RandomAccessFile file = new RandomAccessFile(DATA_DIR + "buckets.json", "rw")) {
            long position = file.length();
            file.seek(position);
            file.writeBytes(JsonUtil.writeValueAsString(metadata) + "\n");
            index.put(bucketName, position);
            saveIndex();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BucketMetadata loadBucketFromFile(String bucketName) {
        Long position = index.get(bucketName);
        if (position == null) {
            return null;
        }
        try (RandomAccessFile file = new RandomAccessFile(DATA_DIR + "buckets.json", "r")) {
            file.seek(position);
            String json = file.readLine();
            return JsonUtil.readValue(json, BucketMetadata.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean bucketFileExists(String bucketName) {
        return index.containsKey(bucketName);
    }

    public void deleteBucketFile(String bucketName) {
        index.remove(bucketName);
        saveIndex();
    }
}