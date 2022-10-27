package com.clyr.utils;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 异步数据合并
 *
 * @author lzy of clyr
 * @date 2022/9/19
 */

public class AsynDataMerge {
    private final Map<String, Object> mObjMap = new HashMap<>();
    private final AsynDataMergeCallback mergeCallback;
    private final int asynNum;

    public AsynDataMerge(AsynDataMergeCallback dataMergeCallback, int asynNum) {
        this.mergeCallback = dataMergeCallback;
        this.asynNum = asynNum;
    }

    /**
     * 添加合并数据
     *
     * @param tag 标签
     * @param obj obj
     */
    public void addMergeData(String tag, Object obj) {
        synchronized (AsynDataMerge.class) {
            if (TextUtils.isEmpty(tag)) {
                tag = "TAG_" + mObjMap.size();
            }

            mObjMap.put(tag, obj);
            if (mergeCallback != null) {
                if (mObjMap.size() >= asynNum) {
                    mergeCallback.callback(mObjMap);
                }
            }
        }
    }

    /**
     * 清理合并数据
     */
    public void clearMergeData() {
        mObjMap.clear();
    }

    public interface AsynDataMergeCallback {
        void callback(Map<String, Object> mObjMap);
    }
}
