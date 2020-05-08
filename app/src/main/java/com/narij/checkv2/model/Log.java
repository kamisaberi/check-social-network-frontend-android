package com.narij.checkv2.model;

import com.google.gson.annotations.SerializedName;

public class Log extends Conversation {

    @SerializedName("log")
    private String log;

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Log() {
        this.setType(Conversation.LOG);
    }
}
