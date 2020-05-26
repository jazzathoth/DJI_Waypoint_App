package com.jb.waypoint;

import android.util.Log;

import net.openid.appauth.AuthorizationException;

import org.json.JSONObject;


public class JDClient {

    private static String hostUrl;

    public JDClient(String host) {
        hostUrl = host;
    }


    private JSONObject callApi(
            String accessToken,
            String idToken,
            AuthorizationException ex) {


        return null;
    }
}
