package com.clyr.testutils;

import android.os.Build.VERSION_CODES;

/**
 * Created by clyr on 2018/3/13 0013.
 * 保存固定设置和固有数据以及标志
 * 多种可用枚举
 */

public class Config {
    public static final String murl = "http://192.168.32.22:8080/TRAMS";//地址 https
//    public static final String mUrl = "https://192.168.32.22:8443/TRAMS";//地址 https

    public static final String mRefreash = "REFREASH"; //EventBus：刷新
    public static final String mLoginName = "LOGINNAME";//用户名
    public static final String mPassword = "PASSWORD";//用户密码
    public static final String mUserInfo = "SERINFO";//用户信息

    public static final String mPush = "Push";//Intent 标记
    public static final String mTag = "MTAG";//Intent 标记
    public static final String mTagOne = "MTAGONE";//Intent 标记
    public static final String mTagTwo = "MTAGTWO";//Intent 标记
    public static final String mTagThree = "MTAGTHREE";//Intent 标记
    public static final String mId = "ID";//Intent 标记
    public static final String mName = "NAME";
    public static final String mIp = "IP";
    public static final String mItemdisplay = "ITEMDISPLAY";


    public static final String mCMS = "LOGVALUE_CMS";//Intent 标记
    public static final String mFILE = "LOGVALUE_FILE";//Intent 标记
    public static final String mTWOLIST = "normaltwolist";//Intent 标记
    public static final String mONELIST = "dz_normalonelist";//Intent 标记

    public static final String mPandectListTitle = "PANDECTLISTTITLE";//用户信息


    //系统版本号 目前是 1.0 - 8.0
    public static final int[] mVersion = {
            VERSION_CODES.BASE, // 1
            VERSION_CODES.BASE_1_1, //1.1
            VERSION_CODES.CUPCAKE,// 1.5
            VERSION_CODES.DONUT,// 1.6
            VERSION_CODES.ECLAIR,// 2.0
            VERSION_CODES.ECLAIR_0_1, // 2.0.1
            VERSION_CODES.ECLAIR_MR1,//2.1
            VERSION_CODES.FROYO, // 2.2
            VERSION_CODES.GINGERBREAD,//2.3
            VERSION_CODES.GINGERBREAD_MR1,// 2.3.3
            VERSION_CODES.HONEYCOMB,//3.0
            VERSION_CODES.HONEYCOMB_MR1, // 3.1
            VERSION_CODES.HONEYCOMB_MR2, //3.2
            VERSION_CODES.ICE_CREAM_SANDWICH,//4.0
            VERSION_CODES.ICE_CREAM_SANDWICH_MR1,// 4.0.3
            VERSION_CODES.JELLY_BEAN,//4.1
            VERSION_CODES.JELLY_BEAN_MR1, // 4.2
            VERSION_CODES.JELLY_BEAN_MR2,//4.3
            VERSION_CODES.KITKAT,//4.4
            VERSION_CODES.KITKAT_WATCH,  //4.4W
            VERSION_CODES.LOLLIPOP,//5.0
            VERSION_CODES.LOLLIPOP_MR1,//5.1
            VERSION_CODES.M,//6.0
            VERSION_CODES.N, //7.0
            VERSION_CODES.N_MR1,  //7.1
            VERSION_CODES.O//8.0
    };
    public static final String APP_SECRET_KEY = "2Wu7EwW4le1MUC41g7NdOQ==";
    public static final String MY_PACKAGE_NAME = "com.matrix.myapplication";


}
