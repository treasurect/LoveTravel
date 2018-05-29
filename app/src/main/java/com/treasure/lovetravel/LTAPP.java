package com.treasure.lovetravel;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.mob.MobSDK;
import com.treasure.lovetravel.utils.SharedPreferencesUtil;

import cn.bmob.v3.Bmob;

/**
 * ============================================================
 * Copyright：treasure和他的朋友们有限公司版权所有 (c) 2018
 * Author：   treasure
 * time：2018/5/4
 * name:
 * ============================================================
 */

public class LTAPP extends Application {
    public static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        //初始化SP
        initPrefs();
        //初始化短信SDK
        MobSDK.init(this);
        //初始化BMob云
        Bmob.initialize(this, "4431c630a007dff562abbb5a0a5493da");
//        //百度地图
        SDKInitializer.initialize(this);
    }

    public static Context getContext() {
        return context;
    }

    /**
     * 初始化SharedPreference
     */
    protected void initPrefs() {
        SharedPreferencesUtil.init(getApplicationContext(), getPackageName() + "_preference", Context.MODE_PRIVATE);
    }
}
