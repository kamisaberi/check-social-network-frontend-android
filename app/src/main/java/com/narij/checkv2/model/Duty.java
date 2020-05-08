package com.narij.checkv2.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Duty implements Parcelable {

    public static final Parcelable.Creator<Duty> CREATOR = new Parcelable.Creator<Duty>() {
        public Duty createFromParcel(Parcel in) {
            return new Duty(in);
        }

        public Duty[] newArray(int size) {
            return new Duty[size];
        }
    };
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("start_date")
    private long startDate;
    @SerializedName("duration")
    private long duration;
    @SerializedName("parent")
    private int parent;
    @SerializedName("creator")
    private User creator = new User();
    @SerializedName("logs")
    private ArrayList<Log> logs = new ArrayList<>();
    @SerializedName("records")
    private ArrayList<Record> records = new ArrayList<>();
    @SerializedName("experts")
    private ArrayList<Expert> experts = new ArrayList<>();
    @SerializedName("priority")
    private Priority priority = new Priority();
    @SerializedName("exact_time")
    private boolean exactTime;
    @SerializedName("can_continue_after_timeout")
    private boolean canContinueAfterTimeout;
    @SerializedName("finish_type")
    private int finishType;
    @SerializedName("finish_time")
    private long finishTime;
    private String groupids;
    @SerializedName("users")
    private ArrayList<User> users = new ArrayList<>();

    public Duty(int id, String title, long startDate, long duration) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.duration = duration;
    }

    public Duty(int id) {
        this.id = id;
    }

    public Duty() {

    }

    public Duty(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.startDate = in.readLong();
        this.duration = in.readLong();
        this.parent = in.readInt();
    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public void setRecords(ArrayList<Record> records) {
        this.records = records;
    }

    public ArrayList<Expert> getExperts() {
        return experts;
    }

    public void setExperts(ArrayList<Expert> experts) {
        this.experts = experts;
    }

    public int getFinishType() {
        return finishType;
    }

    public void setFinishType(int finishType) {
        this.finishType = finishType;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    public boolean isCanContinueAfterTimeout() {
        return canContinueAfterTimeout;
    }

    public void setCanContinueAfterTimeout(boolean canContinueAfterTimeout) {
        this.canContinueAfterTimeout = canContinueAfterTimeout;
    }

    public boolean isExactTime() {
        return exactTime;
    }

    public void setExactTime(boolean exactTime) {
        this.exactTime = exactTime;
    }

    public String getGroupids() {
        return groupids;
    }

    public void setGroupids(String groupids) {
        this.groupids = groupids;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public ArrayList<Log> getLogs() {
        return logs;
    }

    public void setLogs(ArrayList<Log> logs) {
        this.logs = logs;
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

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.title);
        parcel.writeString(this.description);
        parcel.writeLong(this.startDate);
        parcel.writeLong(this.duration);
        parcel.writeInt(this.parent);
    }

    @Override
    public String toString() {
        return "Duty{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }


}
