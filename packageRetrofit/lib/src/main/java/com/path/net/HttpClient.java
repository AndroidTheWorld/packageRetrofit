package com.path.net;


import com.path.cache.ACache;
import com.path.utils.AppUtil;
import com.path.utils.StringUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Jin on 2016/5/19.
 */
public class HttpClient {
    private Call<ResponseBody> mCall;
    private Builder mBuilder;
    private String cacheKey;

    public HttpClient(Builder builder) {
        this.mBuilder = builder;
        this.cacheKey = StringUtil.buffer(builder.url, builder.params.toString());

    }

    /**
     * post请求
     * @param onResultListener
     */
    public void post(final OnResultListener onResultListener) {
        if (onResultListener.onCache(ACache.get().getAsString(cacheKey))) return;

        if (!AppUtil.isNetworkAvailable()) {
            onResultListener.onFailure("无法连接网络");
            return;
        }
        mCall = new Retrofit.Builder()
                .baseUrl(mBuilder.baseUrl)
                .build()
                .create(Params.class)
                .params(mBuilder.url, mBuilder.params);
        request(onResultListener);
    }

    /**
     * get请求
     * @param onResultListener
     */
    public void get(final OnResultListener onResultListener){
        if (onResultListener.onCache(ACache.get().getAsString(cacheKey))) return;

        if (!AppUtil.isNetworkAvailable()) {
            onResultListener.onFailure("无法连接网络");
            return;
        }
        if (!mBuilder.params.isEmpty()){
            String value="";
            String span="";
            Iterator iter = mBuilder.params.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();
                if (!value.equals(""))span="&";
                String par=StringUtil.buffer(span,key,"=",val);
                value=StringUtil.buffer(value,par);
            }
            mBuilder.url(StringUtil.buffer(mBuilder.url,"?",value));
        }
        mCall = new Retrofit.Builder()
                .baseUrl(mBuilder.baseUrl)
                .build()
                .create(Params.class)
                .params(mBuilder.url);
       request(onResultListener);
    }
    private void request(final OnResultListener onResultListener){
        mCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.print("成功==》" + response.code());
                if (response.code() == 200) {
                    try {
                        onResultListener.onSuccess(response.body().string());
                        ACache.get().put(cacheKey, response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (response.code() > 400) onResultListener.onFailure("请求数据失败！");
                    else if (response.code() > 500) onResultListener.onFailure("服务器繁忙，请稍后重试");

                }
                onResultListener.onFinish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                onResultListener.onFailure("网络繁忙，请稍后重试！");
                onResultListener.onFinish();
            }

        });
    }

    /**
     * 取消网络请求
     */
    public void cancel() {
        if (mCall != null) {
            mCall.cancel();
            mCall = null;
        }
    }

    public static final class Builder {
        private String baseUrl = NetConfig.BASE_URL;//给设定默认值，
        private String url;
        private Map<String, String> params = new HashMap<>();

        public Builder() {
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder param(String key, String value) {
            params.put(key, value);
            return this;
        }

        public HttpClient build() {
            return new HttpClient(this);
        }
    }

}
