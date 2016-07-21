package com.path.net;


import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by dell on 2016/5/6.
 */
public interface Params {

    @FormUrlEncoded
    @POST("{filePath}")
    Call<ResponseBody> params(@Path("filePath") String filePath, @FieldMap Map<String, String> param);


    @GET("{filePath}")
    Call<ResponseBody> params(@Path("filePath") String filePath);

    Call<ResponseBody> params(@QueryMap Map<String,String> options,
                              @PartMap Map<String, RequestBody> files);


}
