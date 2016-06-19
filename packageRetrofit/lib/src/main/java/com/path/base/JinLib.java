package com.path.base;

import android.app.Application;
import android.content.Context;
import android.util.Log;



/**
 * Created by Jin on 2016/5/9.
 */
public class JinLib extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        initialize(this);
    }
    public static  void initialize(final Context context){
        sContext=context;
    }
    public static Context getContext(){
        return sContext;
    }

    public JinLib(){
        sContext=this;
        if (sContext==null){
            Log.e("JinLib","JinLib is not initialize");
            return;
        }
        init();
    }

    private void init() {

    }
}
