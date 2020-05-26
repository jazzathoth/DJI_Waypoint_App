package com.jb.waypoint;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationService;

import org.json.JSONObject;


public class ChooseFarm extends AppCompatActivity {

    private static final String TAG = "ChooseFarm";

    private AuthState authState;
    private AuthorizationService authorizationService;
    private String baseUrl = getString(R.string.jd_base_url);
    private JDClient client = new JDClient(baseUrl);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authState.performActionWithFreshTokens(authorizationService, new AuthState.AuthStateAction() {

            @Override
            public void execute(@Nullable String accessToken, @Nullable String idToken, @Nullable AuthorizationException ex) {

            }
        });
    }

}
