package com.clyr.base.bean;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by clyr on 2018/4/9 0009.
 */

public class JsonModle {
    private List<TreeMap<String,String[]>> datalist;

    public List<TreeMap<String, String[]>> getDatalist() {
        return datalist;
    }

    public void setDatalist(List<TreeMap<String, String[]>> datalist) {
        this.datalist = datalist;
    }
}
