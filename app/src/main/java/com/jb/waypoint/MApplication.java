package com.jb.waypoint;

import android.app.Application;
import android.content.Context;

import com.secneo.sdk.Helper;

public class MApplication extends Application {
    private Setup fpvApp;

    @Override
    protected void attachBaseContext(Context paramContext) {
        super.attachBaseContext(paramContext);
        Helper.install(MApplication.this);
        if (fpvApp == null) {
            fpvApp = new Setup();
            fpvApp.setContext(this);
        }
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        fpvApp.onCreate();
    }
}
