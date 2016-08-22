package com.path.net;



import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.path.cache.ACache;
import com.path.utils.AppUtil;
import com.path.utils.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Jin on 2016/5/19.
 */
public class HttpClient {
    public static String BASE_URL="";
    private static HttpClient mInstance;
    private Call<ResponseBody> mCall;
    private Builder mBuilder;
    private String cacheKey;
    private String mCache;
    public static final int OBJECT=1;//返回数据为json对象
    public static final int ARRAY=2;//返回数据为数组

    private static Retrofit retrofit;

    public static HttpClient getInstance() {
        if (mInstance==null){
            synchronized (HttpClient.class){
                if (mInstance==null){
                    mInstance=new HttpClient();
                }
            }
        }
        return mInstance;
    }

    public HttpClient() {
        OkHttpClient client = new OkHttpClient();
        client.newBuilder().connectTimeout(7000, TimeUnit.MILLISECONDS);
        retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .build();
    }

    public Builder getBuilder() {
        return mBuilder;
    }

    public void setBuilder(Builder builder) {
        this.mBuilder = builder;
        this.cacheKey = StringUtil.buffer(builder.url, builder.params.toString());
        mCache=ACache.get().getAsString(cacheKey);
    }

    /**
     * post请求
     * @param onResultListener
     */
    public void post(final OnResultListener onResultListener) {

        if (onResultListener.onCache(mCache)) return;
        if (parseCache(mCache,onResultListener)) return;
        if (!AppUtil.isNetworkAvailable()) {
            handlerError("无法连接网络",onResultListener);
            return;
        }
        mCall =retrofit.create(Params.class)
                .params(mBuilder.url, mBuilder.params);

        request(onResultListener);
    }

    /**
     * get请求
     * @param onResultListener  不可为空
     */
    public void get(final OnResultListener onResultListener){
        if (onResultListener.onCache(mCache)) return;

        if (parseCache(mCache,onResultListener)) return;

        if (!AppUtil.isNetworkAvailable()) {
            handlerError("无法连接网络",onResultListener);
            return;
        }
        if (!mBuilder.params.isEmpty()){
            String value="";
            String span="";
            for (Map.Entry<String, String> entry :
                    mBuilder.params.entrySet()) {
                String key = entry.getKey();
                String val = entry.getValue();
                if (!value.equals(""))span="&";
                String par=StringUtil.buffer(span,key,"=",val);
                value=StringUtil.buffer(value,par);
            }
            mBuilder.url(StringUtil.buffer(mBuilder.url,"?",value));
        }
        mCall = retrofit.create(Params.class)
                .params(mBuilder.url);
       request(onResultListener);
    }

    /**
     * 文件上传  有bug,未解决
     * @param onProgressListener
     */
    @Deprecated
    public void upload(OnProgressListener onProgressListener){
        Map<String,RequestBody> requestBody=new ArrayMap<>();

        for(Map.Entry<String,File> entry : mBuilder.files.entrySet()){
            UploadRequestBody uploadRequestBody=new UploadRequestBody(entry.getValue(),onProgressListener);
            requestBody.put(entry.getKey(),uploadRequestBody);
        }

        mCall=retrofit.create(Params.class)
                .params(mBuilder.url,mBuilder.params,requestBody);
        mCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i("上传成功","code="+response.code());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }

    private void request(final OnResultListener onResultListener){
        mCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.print("成功==》" + response.code());
                if (response.code() == 200) {
                    try {
                        String result=response.body().string();
                        ACache.get().put(cacheKey, result);
                        onResultListener.onSuccess(result);
                        parseJson(result,onResultListener);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {

                    if (response.code() > 400) handlerError("请求数据失败！",onResultListener);
                    else if (response.code() > 500) handlerError("服务器繁忙，请稍后重试",onResultListener);
                }
                onResultListener.onFinish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                handlerError("网络繁忙，请稍后重试！",onResultListener);
                onResultListener.onFinish();
            }

        });
    }

    private void handlerError(String message,OnResultListener onResultListener){
        onResultListener.onFailure(message);
        onResultListener.onFailure(mCache,message);
        parseFails(mCache,message,onResultListener);
    }

    @SuppressWarnings("unchecked")
    private boolean parseCache(String json,OnResultListener onResultListener){
        switch (mBuilder.bodyType){
            case OBJECT:
               return onResultListener.onCache(JSONObject.parseObject(mCache,mBuilder.clazz));
            case ARRAY:
                return onResultListener.onCache(JSONObject.parseArray(json,mBuilder.clazz));
            default:
                return false;
        }
    }

    @SuppressWarnings("unchecked")
    private void parseFails(String json,String error,OnResultListener onResultListener){
        switch (mBuilder.bodyType){
            case OBJECT:
                onResultListener.onFailure(JSONObject.parseObject(mCache,mBuilder.clazz),error);
                break;
            case ARRAY:
                onResultListener.onFailure(JSONObject.parseArray(json,mBuilder.clazz),error);
                break;
        }
    }

    @SuppressWarnings("unchecked")
    private void parseJson(String json,OnResultListener onResultListener){
        switch (mBuilder.bodyType){
            case OBJECT:
                onResultListener.onSuccess(JSONObject.parseObject(json,mBuilder.clazz));
                break;
            case ARRAY:
                onResultListener.onSuccess(JSONObject.parseArray(json,mBuilder.clazz));
                break;
        }
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
        private Map<String, String> params = new ArrayMap<>();
        private Map<String, File> files=new ArrayMap<>();
        private int bodyType=0;//返回数据的类型,因为前期返回是字符串，为了避免修改
        private boolean hasShowLoadding=false;
        private Class clazz;
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
        public Builder file(String key, File file){
           files.put(key,file);
            return this;
        }

        public <T> Builder bodyType(int bodyType,Class<T> clazz) {
            this.bodyType = bodyType;
            this.clazz = clazz;
            return this;
        }

        public Builder hasShowLoadding(boolean hasShowLoadding) {
            this.hasShowLoadding = hasShowLoadding;
            return this;
        }

        public HttpClient build() {
            BASE_URL=baseUrl;
            HttpClient httpClient= HttpClient.getInstance();
            httpClient.setBuilder(this);
            return httpClient;
        }
    }

}
