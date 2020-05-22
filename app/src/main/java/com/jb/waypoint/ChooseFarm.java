package com.jb.waypoint;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationService;

import org.json.JSONObject;

import java.net.URI;

public class ChooseFarm extends AppCompatActivity {

    private static final String TAG = "ChooseFarm";

    private AuthState authState;
    private AuthorizationService authorizationService;
    private String baseUrl = getString(R.string.jd_base_url);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authState.performActionWithFreshTokens(authorizationService, new AuthState.AuthStateAction() {
            @Override
            public void execute(@Nullable String accessToken,
                                @Nullable String idToken,
                                @Nullable AuthorizationException ex) {
                new AsyncTask<String, Void, JSONObject>() {
                    @Override
                    protected JSONObject doInBackground(String... tokens){
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request()
                                .Builder().url(baseUrl + "platform/organizations/")
                                .addHeader("Authorization", String.format("Bearer %s", tokens[0]))
                                .build();

                        try {
                            Response response = client.newCall((request)).execute();
                            String jsonBody = response.body().string();
                            Log.i(TAG, String.format("User Info Response %s", jsonBody));
                            return new JSONObject(jsonBody);
                        } catch (Exception ex) {
                            Log.w(TAG, ex);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(JSONObject jdOrg) {
                        if (jdOrg != null) {

                            JSONObject jdOrgValues = jdOrg.optJSONObject("values");
                            JSONObject jdOrgLinks = jdOrgValues.optJSONObject("links");
                            String orgName = jdOrgValues.optString("name");
                            String orgID = jdOrgValues.optString("id");
                        }
                    }
                }.execute(accessToken);


            }
        });
    }

}
