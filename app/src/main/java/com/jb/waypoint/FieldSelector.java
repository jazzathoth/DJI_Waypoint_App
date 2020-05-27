package com.jb.waypoint;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jb.waypoint.interfaces.GetOrganizationsInterface;
import com.jb.waypoint.retrofit_instance.RetrofitInstance;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationService;

import org.json.JSONObject;


public class FieldSelector extends AppCompatActivity {

    private static final String TAG = "ChooseFarm";

    private AuthState authState;
    private AuthorizationService authorizationService;
    private String baseUrl = getString(R.string.jd_base_url);
    private JDClient client = new JDClient(baseUrl);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_selector);
        Toolbar toolbar = findViewById(R.id.toolbar_field_selector);
        setSupportActionBar(toolbar);

        GetOrganizationsInterface organizationsInterface = RetrofitInstance.getRetrofitInstance().create()

        authState.performActionWithFreshTokens(authorizationService, new AuthState.AuthStateAction() {

            @Override
            public void execute(@Nullable String accessToken, @Nullable String idToken, @Nullable AuthorizationException ex) {

            }
        });
    }

}
