package com.clyr.testutils.push.jpush;

import android.content.Context;
import android.util.Log;

import cn.jpush.android.api.JPushMessage;

/**
 * 处理tagalias相关的逻辑
 * */
public class TagAliasOperatorHelper {
    private static final String TAG = "JIGUANG-TagAliasHelper";

    private Context context;

    private static TagAliasOperatorHelper mInstance;
    private TagAliasOperatorHelper(){
    }
    public static TagAliasOperatorHelper getInstance(){
        if(mInstance == null){
            synchronized (TagAliasOperatorHelper.class){
                if(mInstance == null){
                    mInstance = new TagAliasOperatorHelper();
                }
            }
        }
        return mInstance;
    }
    public void init(Context context){
        if(context != null) {
            this.context = context.getApplicationContext();
        }
    }


    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        int sequence = jPushMessage.getSequence();
        Log.i(TAG,"action - onTagOperatorResult, sequence:"+sequence+",tags:"+jPushMessage.getTags());
        Log.i(TAG,"tags size:"+jPushMessage.getTags().size());
        init(context);

        if(jPushMessage.getErrorCode() == 0){
            Log.i(TAG,"action - modify tag Success,sequence:"+sequence);
        }else{
            String logs = "Failed to modify tags";
            if(jPushMessage.getErrorCode() == 6018){
                //tag数量超过限制,需要先清除一部分再add
                logs += ", tags is exceed limit need to clean";
            }
            logs += ", errorCode:" + jPushMessage.getErrorCode();
            Log.e(TAG, logs);
        }
    }
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage){
        int sequence = jPushMessage.getSequence();
        Log.i(TAG,"action - onCheckTagOperatorResult, sequence:"+sequence+",checktag:"+jPushMessage.getCheckTag());
        init(context);
        if(jPushMessage.getErrorCode() == 0){
            String logs = "modify tag "+jPushMessage.getCheckTag() + " bind state success,state:"+jPushMessage.getTagCheckStateResult();
            Log.i(TAG,logs);
        }else{
            String logs = "Failed to modify tags, errorCode:" + jPushMessage.getErrorCode();
            Log.e(TAG, logs);
        }
    }
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        int sequence = jPushMessage.getSequence();
        Log.i(TAG,"action - onAliasOperatorResult, sequence:"+sequence+",alias:"+jPushMessage.getAlias());
        init(context);

        if(jPushMessage.getErrorCode() == 0){
            Log.i(TAG,"action - modify alias Success,sequence:"+sequence);
        }else{
            String logs = "Failed to modify alias, errorCode:" + jPushMessage.getErrorCode();
            Log.e(TAG, logs);
        }
    }
    //设置手机号码回调
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        int sequence = jPushMessage.getSequence();
        Log.i(TAG,"action - onMobileNumberOperatorResult, sequence:"+sequence+",mobileNumber:"+jPushMessage.getMobileNumber());
        init(context);
        if(jPushMessage.getErrorCode() == 0){
            Log.i(TAG,"action - set mobile number Success,sequence:"+sequence);
        }else{
            String logs = "Failed to set mobile number, errorCode:" + jPushMessage.getErrorCode();
            Log.e(TAG, logs);
        }
    }



}
