package com.clyr.base.bean;

/**
 * Created by kelin on 16-11-18.
 */

public class OrderInfo {
    private long id;
    private String guestName;
    private Status status;
    private boolean isBegin;
    public static final String mNormal = "NORMAL";
    public static final String mWarn = "WARN";
    public static final String mAlarm = "ALARM";
    public static final String mDanger = "DANGER";

    public enum Status {
        NORMAL, WARN, ALARM, DANGER;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isBegin() {
        return isBegin;
    }

    public void setBegin(boolean begin) {
        isBegin = begin;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
