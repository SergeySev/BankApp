package com.example.telproject.entity.enums;

public enum TransactionType {
    NEW("NEW"),
    PENDING("PENDING"),
    APPROVED("APPROVED");

    private final String value;

    TransactionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}