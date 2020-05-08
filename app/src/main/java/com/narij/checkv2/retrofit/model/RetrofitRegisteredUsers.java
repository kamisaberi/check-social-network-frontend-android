package com.narij.checkv2.retrofit.model;

import com.narij.checkv2.model.User;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RetrofitRegisteredUsers {

    @SerializedName("id")
    private int id;

    @SerializedName("error")
    private boolean error;

    @SerializedName("message")
    private String message;


    @SerializedName("users")
    private ArrayList<User> users = new ArrayList<>();


    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
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
