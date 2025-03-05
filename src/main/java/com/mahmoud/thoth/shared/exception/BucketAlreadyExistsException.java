package com.mahmoud.thoth.shared.exception;

public class BucketAlreadyExistsException extends RuntimeException {
    public BucketAlreadyExistsException(String message) {
        super(message);
    }
}
