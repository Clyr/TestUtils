package com.clyr.utils.utilshelper;

/**
 * Created by zhoufazhan on 2017/7/27.
 */

public class TestData<T> {
    String state;
    private T user;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public T getUser() {
        return user;
    }

    public void setUser(T user) {
        this.user = user;
    }
}
