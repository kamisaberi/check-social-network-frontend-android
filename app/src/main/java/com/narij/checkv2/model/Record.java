package com.narij.checkv2.model;

import com.google.gson.annotations.SerializedName;

public class Record extends Conversation {

    @SerializedName("content")
    private String content;

    @SerializedName("record_type")
    private String recordType;

    public Record() {
        this.setType(Conversation.LOG);
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
