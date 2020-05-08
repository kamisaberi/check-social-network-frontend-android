package com.narij.checkv2.retrofit.model;

import com.narij.checkv2.model.Duty;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by kami on 8/15/2017.
 */

public class RetrofitDuties {

    @SerializedName("id")
    private int id;
    @SerializedName("error")
    private boolean error;
    @SerializedName("duties")
    private ArrayList<Duty> duties = new ArrayList<>();


    public ArrayList<Duty> getDuties() {
        return duties;
    }

    public void setDuties(ArrayList<Duty> duties) {
        this.duties = duties;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    @SerializedName("message")
    private String message;

    public RetrofitDuties() {
    }

    public RetrofitDuties(int id, boolean error, String content) {
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
