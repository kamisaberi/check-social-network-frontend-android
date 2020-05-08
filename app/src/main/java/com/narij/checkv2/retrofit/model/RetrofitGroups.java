package com.narij.checkv2.retrofit.model;

import com.narij.checkv2.model.Group;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by kami on 8/15/2017.
 */

public class RetrofitGroups {

    @SerializedName("id")
    private int id;
    @SerializedName("error")
    private boolean error;
    @SerializedName("groups")
    private ArrayList<Group> groups = new ArrayList<>();

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    @SerializedName("message")
    private String message;

    public RetrofitGroups() {
    }

    public RetrofitGroups(int id, boolean error, String content) {
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
