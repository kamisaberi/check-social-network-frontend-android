package com.narij.checkv2.retrofit.model;

import com.narij.checkv2.model.User;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RetrofitFriends {

    @SerializedName("id")
    private int id;

    @SerializedName("error")
    private boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("friends")
    private ArrayList<User> friends = new ArrayList<>();

    public ArrayList<User> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<User> friends) {
        this.friends = friends;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
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
