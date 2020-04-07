package com.jb.waypoint;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.secneo.sdk.Helper;

public class MApplication extends Application {

    @Override
    protected void attachBaseContext(Context paramContext) {
        super.attachBaseContext(paramContext);
        MultiDex.install(this);
        Helper.install(MApplication.this);
    }
}
