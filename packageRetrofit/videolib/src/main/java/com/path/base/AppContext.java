package com.path.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

/**
 * Created by 邱锦洋 on 2016/8/20.
 * 邮箱 549024741@qq.com
 * 博客：http://www.jianshu.com/notebooks/4683942/latest
 */
public class AppContext extends Application{
    private static Context sContext;
    private static Resources sResource;//直接获取Resources 放在application，可以减少调用getResources的次数

    @Override
    public void onCreate() {
        super.onCreate();
        initialize(this);
    }
    public static  void initialize(final Context context){
        sContext=context;
        sResource=sContext.getResources();
    }


    public static Context getContext(){
        return sContext;
    }

    public static Resources resources(){
        return sResource;
    }

    public AppContext(){
        sContext=this;
        if (sContext==null){
            Log.e("JinLib","JinLib is not initialize");
            return;
        }
        init();
    }

    private void init() {

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        //  Glide.get(this).trimMemory(level);
    }
}
