package com.path.packageretrofit;

import android.app.Application;

import com.path.base.JinLib;

/**
 * Created by win10 on 2016/6/19.
 */
public class myApplicatioon extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JinLib.initialize(this);//初始化lib
    }
}
