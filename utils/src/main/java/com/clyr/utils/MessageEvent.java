package com.clyr.utils;

/**
 * Created by 11635 of clyr on 2021/9/22.
 */

public class MessageEvent {
    public final static int NOLOGIN = 0;
    public final static int LOGIN = 1;
    public final static int OTHER = 2;
    public final static int CHATRED = 3;
    public final static int INVITE = 4;
    public final static int PUSH = 5;
    public final static int CHATRELOAD = 6;
    public String message;
    public int Type;
    public int count = 0;

    public static MessageEvent getInstance(String message, int type) {
        return new MessageEvent(message, type);
    }

    public MessageEvent(String message, int type) {
        this.message = message;
        this.Type = type;
    }

    public static MessageEvent getInstance(String message, int type, int count) {
        return new MessageEvent(message, type, count);
    }

    public MessageEvent(String message, int type, int count) {
        this.message = message;
        this.Type = type;
        this.count = count;
    }

    @Override
    public String toString() {

        return "MessageEvent{ " +
                "message = '" + message + '\'' +
                ", Type = " + Type +
                ", count = " + count +
                ", TypeName = " + getTypeName(Type) +
                " }";
    }

    private String getTypeName(int type) {
        switch (type) {
            case NOLOGIN:
                return "NOLOGIN";
            case LOGIN:
                return "LOGIN";
            case OTHER:
                return "OTHER";
            case CHATRED:
                return "CHATRED";
            case INVITE:
                return "INVITE";
            case PUSH:
                return "PUSH";
            case CHATRELOAD:
                return "CHATRELOAD";
        }
        return "undefined";
    }
}
