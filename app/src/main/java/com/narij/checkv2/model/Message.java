package com.narij.checkv2.model;

import com.google.gson.annotations.SerializedName;

public class Message extends Conversation {

    @SerializedName("id")
    private int id;
    @SerializedName("content")
    private String content;
    @SerializedName("user")
    private User user;
    @SerializedName("duty")
    private Duty duty;
    @SerializedName("date")
    private long date;


    public Message() {
        this.user = new User();
        this.duty = new Duty();
    }

    public Message(int id, String content, User user, Duty duty, long date) {
        this.id = id;
        this.content = content;
        this.user = user;
        this.duty = duty;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Duty getDuty() {
        return duty;
    }

    public void setDuty(Duty duty) {
        this.duty = duty;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
