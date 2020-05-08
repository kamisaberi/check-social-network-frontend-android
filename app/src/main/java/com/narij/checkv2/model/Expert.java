package com.narij.checkv2.model;

import com.google.gson.annotations.SerializedName;

public class Expert {

    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    public Expert() {
    }

    public Expert(int id) {
        this.id = id;
    }

    public Expert(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
