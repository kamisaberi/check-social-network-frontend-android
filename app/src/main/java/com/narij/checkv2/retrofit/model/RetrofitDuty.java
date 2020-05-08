package com.narij.checkv2.retrofit.model;

import com.google.gson.annotations.SerializedName;
import com.narij.checkv2.model.Duty;

/**
 * Created by kami on 8/15/2017.
 */

public class RetrofitDuty {

    @SerializedName("id")
    private int id;
    @SerializedName("error")
    private boolean error;
    @SerializedName("duty")
    private Duty duty = new Duty();


    public Duty getDuty() {
        return duty;
    }

    public void setDuty(Duty duty) {
        this.duty = duty;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    @SerializedName("message")
    private String message;

    public RetrofitDuty() {
    }

    public RetrofitDuty(int id, boolean error, String content) {
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
