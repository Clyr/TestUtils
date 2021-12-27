package com.clyr.test.bean;

/**
 * Created by 11635 of clyr on 2021/12/27.
 */
public class NumberInt {
    String name;
    int count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "NumberInt{" +
                "name='" + name + '\'' +
                ", count=" + count +
                '}';
    }
}
