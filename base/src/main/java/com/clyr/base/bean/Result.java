/**
  * Copyright 2021 bejson.com 
  */
package com.clyr.base.bean;
import java.util.List;

/**
 * Auto-generated: 2021-11-29 17:18:9
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Result {

    private Pm pm;
    private int ret_code;
    private List<SiteList> siteList;
    public void setPm(Pm pm) {
         this.pm = pm;
     }
     public Pm getPm() {
         return pm;
     }

    public void setRet_code(int ret_code) {
         this.ret_code = ret_code;
     }
     public int getRet_code() {
         return ret_code;
     }

    public void setSiteList(List<SiteList> siteList) {
         this.siteList = siteList;
     }
     public List<SiteList> getSiteList() {
         return siteList;
     }

}