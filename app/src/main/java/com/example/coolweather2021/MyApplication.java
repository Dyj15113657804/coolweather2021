package com.example.coolweather2021;

import android.app.Application;
import android.content.Context;
import android.net.http.HttpResponseCache;

import com.didichuxing.doraemonkit.DoraemonKit;

import org.litepal.LitePalApplication;
import org.litepal.util.Const;

public class MyApplication extends Application {
    private static Context context;


    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePalApplication.initialize(context);
//        DoraemonKit.install(this);
        DoraemonKit.install(this,
                "a0b8197f2d04a9abed22cd598b951a21");
//        DoraemonKit.install(this,null,"a0b8197f2d04a9abed22cd598b951a21");


    }

}
