package com.path.net;




/**
 * Created by Jin on 2016/5/6.
 */
public interface OnResultListener {
    boolean onCache(String cache);//返回true,表示不再请求网络
    void onSuccess(String result);
    void onFailure(String message);
    void onFinish();
}
