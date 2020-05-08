package com.narij.checkv2.retrofit.model;

import com.google.gson.annotations.SerializedName;
import com.narij.checkv2.model.Record;

import java.util.ArrayList;

/**
 * Created by kami on 8/15/2017.
 */

public class RetrofitRecords {

    @SerializedName("id")
    private int id;
    @SerializedName("error")
    private boolean error;
    @SerializedName("records")
    private ArrayList<Record> records = new ArrayList<>();
    @SerializedName("message")
    private String message;

    public RetrofitRecords() {
    }

    public RetrofitRecords(int id, boolean error, String content) {
        this.id = id;
        this.error = error;
        this.message = content;
    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public void setRecords(ArrayList<Record> records) {
        this.records = records;
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
