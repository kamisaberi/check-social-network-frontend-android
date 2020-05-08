package com.narij.checkv2.retrofit.model;

import com.google.gson.annotations.SerializedName;
import com.narij.checkv2.model.Expert;
import com.narij.checkv2.model.User;

import java.util.ArrayList;

public class RetrofitUser {

    @SerializedName("id")
    private int id;

    @SerializedName("error")
    private boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("user")
    private User user = new User();


    @SerializedName("experts")
    private ArrayList<Expert> experts = new ArrayList<>();

    public ArrayList<Expert> getExperts() {
        return experts;
    }

    public void setExperts(ArrayList<Expert> experts) {
        this.experts = experts;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
