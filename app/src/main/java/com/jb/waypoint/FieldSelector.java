package com.jb.waypoint;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jb.waypoint.adapter.OrganizationsAdapter;
import com.jb.waypoint.interfaces.GetOrganizationsInterface;
import com.jb.waypoint.model.Organizations;
import com.jb.waypoint.model.OrganizationsList;
import com.jb.waypoint.retrofit_instance.RetrofitInstance;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationService;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Route;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FieldSelector extends AppCompatActivity {

    private static final String TAG = "ChooseFarm";

    private AuthState authState;
    private AuthorizationService authorizationService;
    private OrganizationsAdapter organizationsAdapter;
    private RecyclerView recyclerView;
//    private String baseUrl = getString(R.string.jd_base_url);
//    private JDClient client = new JDClient(baseUrl);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_selector);
//        Toolbar toolbar = findViewById(R.id.toolbar_field_selector);
//        setSupportActionBar(toolbar);



        authState.performActionWithFreshTokens(authorizationService, (String accessToken,
                                                                      String idToken,
                                                                      AuthorizationException ex) -> {
            GetOrganizationsInterface organizationsInterface = RetrofitInstance
                    .getRetrofitInstance()
                    .create(GetOrganizationsInterface.class);

            Call<OrganizationsList> organizationsListCall = organizationsInterface
                    .getOrganizationData("Bearer " + accessToken);

            Log.wtf("Called Organizations GET", organizationsListCall.request().url() + "");

            organizationsListCall.enqueue(new Callback<OrganizationsList>() {
                @Override
                public void onResponse(Call<OrganizationsList> call, Response<OrganizationsList> response) {
                    generateOrganizationsList(response.body().getOrganizationsArrayList());
                }

                @Override
                public void onFailure(Call<OrganizationsList> call, Throwable t) {
                    Toast.makeText(FieldSelector.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void generateOrganizationsList(ArrayList<Organizations> organizationsArrayList) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_content_field_selector);
        OrganizationsAdapter adapter = new OrganizationsAdapter(organizationsArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(FieldSelector.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

//    class AppAuthAuthenticator implements Authenticator{
//        private AuthorizationService authService;
//        private AuthState authState;
//
//
//        @Override
//        public Request authenticate(Route route, okhttp3.Response response) throws IOException {
//            CompletableFuture<Request> future;
//            authState.performActionWithFreshTokens(authService);
//        }
//    }

}
