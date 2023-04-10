package com.example.telproject.bucket;

public enum BucketName {
    PROFILE_IMAGE("bank-app-123");

    private final String bucketName;

    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
