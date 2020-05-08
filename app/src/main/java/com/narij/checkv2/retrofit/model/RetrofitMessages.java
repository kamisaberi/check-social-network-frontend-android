package com.narij.checkv2.retrofit.model;

import com.google.gson.annotations.SerializedName;
import com.narij.checkv2.model.Message;

import java.util.ArrayList;

/**
 * Created by kami on 8/15/2017.
 */

public class RetrofitMessages {

    @SerializedName("id")
    private int id;
    @SerializedName("error")
    private boolean error;
    @SerializedName("messages")
    private ArrayList<Message> messages= new ArrayList<>();

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    @SerializedName("message")
    private String message;

    public RetrofitMessages() {
    }

    public RetrofitMessages(int id, boolean error, String content) {
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
