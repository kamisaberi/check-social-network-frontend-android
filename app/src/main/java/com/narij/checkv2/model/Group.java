package com.narij.checkv2.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Group implements Parcelable {



    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;

    public Group(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static Creator<Group> getCREATOR() {
        return CREATOR;
    }

    @SerializedName("duties")
    private ArrayList<Duty> duties = new ArrayList<>();

    public Group() {
    }

    public Group(int id, String title) {
        this.id = id;
        this.title = title;
    }

    protected Group(Parcel in) {
        id = in.readInt();
        title = in.readString();
        duties = in.createTypedArrayList(Duty.CREATOR);
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    public ArrayList<Duty> getDuties() {
        return duties;
    }

    public void setDuties(ArrayList<Duty> duties) {
        this.duties = duties;
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


    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeTypedList(duties);
    }



}
