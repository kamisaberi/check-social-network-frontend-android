package com.narij.checkv2.retrofit.model;

import com.google.gson.annotations.SerializedName;
import com.narij.checkv2.model.Log;

import java.util.ArrayList;

/**
 * Created by kami on 8/15/2017.
 */

public class RetrofitLogs {

    @SerializedName("id")
    private int id;
    @SerializedName("error")
    private boolean error;
    @SerializedName("logs")
    private ArrayList<Log> logs= new ArrayList<>();

    public ArrayList<Log> getLogs() {
        return logs;
    }

    public void setLogs(ArrayList<Log> logs) {
        this.logs = logs;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    @SerializedName("message")
    private String message;

    public RetrofitLogs() {
    }

    public RetrofitLogs(int id, boolean error, String content) {
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
