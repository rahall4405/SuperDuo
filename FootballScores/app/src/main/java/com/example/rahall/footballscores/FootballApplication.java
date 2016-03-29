package com.example.rahall.footballscores;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.example.rahall.footballscores.service.myFetchService;
import com.facebook.stetho.Stetho;

/**
 * Created by rahall4405 on 3/15/16.
 */
public class FootballApplication  extends Application {
    private static FootballApplication mInstance;
    private static Context mContext;
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mInstance = this;

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());


    }
    public static synchronized FootballApplication getInstance() {
        return mInstance;
    }

    public static Context getAppContext() {
        return mContext;
    }


}
