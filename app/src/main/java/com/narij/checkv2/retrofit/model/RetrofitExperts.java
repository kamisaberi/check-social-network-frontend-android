package com.narij.checkv2.retrofit.model;

import com.google.gson.annotations.SerializedName;
import com.narij.checkv2.model.Expert;
import com.narij.checkv2.model.Group;

import java.util.ArrayList;

/**
 * Created by kami on 8/15/2017.
 */

public class RetrofitExperts {

    @SerializedName("id")
    private int id;
    @SerializedName("error")
    private boolean error;
    @SerializedName("experts")
    private ArrayList<Expert> experts= new ArrayList<>();

    public ArrayList<Expert> getExperts() {
        return experts;
    }

    public void setExperts(ArrayList<Expert> experts) {
        this.experts = experts;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    @SerializedName("message")
    private String message;

    public RetrofitExperts() {
    }

    public RetrofitExperts(int id, boolean error, String content) {
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
