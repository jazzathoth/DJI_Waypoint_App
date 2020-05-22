package com.jb.waypoint;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.Nullable;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import net.openid.appauth.AuthorizationException;

import org.json.JSONObject;

public class JDClient {

//    public void execute(@Nullable String accessToken,
//                        @Nullable String idToken,
//                        @Nullable AuthorizationException ex) {

    private class requestHandler extends AsyncTask<String, Void, JSONObject> {
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

}
