package com.path.net;




/**
 * Created by Jin on 2016/5/6.
 */
public abstract class OnResultListener<T> {
    public boolean onCache(String cache) {//返回true,表示不再请求网络
        return false;
    }
    public boolean onCache(T cache) {//返回true,表示不再请求网络
        return false;
    }

    public  void onSuccess(String result){}
    public  void onSuccess(T result){}

    public void onFailure(String message) {//根据需求实现
    }

    public void onFailure(String cache, String message) {//根据需求实现，当请求异常时，需要使用缓存时，调用此方法
    }

    public void onFailure(T cache, String message) {//根据需求实现，当请求异常时，需要使用缓存时，调用此方法
    }

    @Deprecated
    public void onFinish() {//已经废弃
    }
}
