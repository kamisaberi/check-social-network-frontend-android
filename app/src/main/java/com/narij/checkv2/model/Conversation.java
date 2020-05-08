package com.narij.checkv2.model;

import com.google.gson.annotations.SerializedName;

public class Conversation {

    public static final int COMMENT = 1;
    public static final int LOG = 2;
    public static final int LEFT_MESSAGE = 3;
    public static final int RIGHT_MESSAGE = 4;
    public static final int FILE = 5;


    @SerializedName("id")
    private int id;
    @SerializedName("type")
    private int type;
    @SerializedName("date")
    private long date;
    @SerializedName("user")
    private User user = new User();


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
