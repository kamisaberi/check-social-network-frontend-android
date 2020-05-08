package com.narij.checkv2.retrofit.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kami on 8/15/2017.
 */

public class WebServiceMessage {

    @SerializedName("id")
    private int id;

    @SerializedName("error")
    private boolean error;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    @SerializedName("message")
    private String message;

    public WebServiceMessage() {
    }

    public WebServiceMessage(int id, boolean error, String content) {
        this.id = id;
        this.error = error;
        this.message = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
