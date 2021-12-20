package com.clyr.base.bean;

/**
 * Created by 11635 of clyr on 2021/12/14.
 */

public class NotificationType {
    int notifiId = 0;
    NotifiType notifiType = NotifiType.normal;

    public enum NotifiType {
        normal, process, important, other, noCancle
    }

    public NotificationType() {
    }

    public NotificationType(int notifiId, NotifiType notifiType) {
        this.notifiId = notifiId;
        this.notifiType = notifiType;
    }

    public int getNotifiId() {
        return notifiId;
    }

    public void setNotifiId(int notifiId) {
        this.notifiId = notifiId;
    }

    public NotifiType getNotifiType() {
        return notifiType;
    }

    public void setNotifiType(NotifiType notifiType) {
        this.notifiType = notifiType;
    }
}
