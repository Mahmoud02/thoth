package com.mahmoud.thoth.shared;

public class BucketAlreadyExistsException extends RuntimeException {
    public BucketAlreadyExistsException(String message) {
        super(message);
    }
}
