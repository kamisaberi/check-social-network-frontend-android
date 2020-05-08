package com.narij.checkv2.model;

public enum ConversationType {

    COMMENT(1), LOG(2), LEFT_MESSAGE(3), RIGHT_MESSAGE(4), FILE(5);

    ConversationType(int value) {
        this.value = value;
    }

    int value;

    public int getValue() {
        return value;
    }
}
