package com.jb.waypoint.selectors;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jb.waypoint.AuthStateManager;
import com.jb.waypoint.LoadConfig;
import com.jb.waypoint.adapter.FarmsAdapter;
import com.jb.waypoint.R;
import com.jb.waypoint.interfaces.GetFarmsInterface;
import com.jb.waypoint.model.Farms;
import com.jb.waypoint.model.FarmsList;
import com.jb.waypoint.retrofit_instance.RetrofitInstance;

import net.openid.appauth.AppAuthConfiguration;
import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FarmSelect extends AppCompatActivity {
    private static final String TAG = "Choose Farm";

    private AuthState authState;
    private AuthorizationService authorizationService;
    private FarmsAdapter farmsAdapter;
    private RecyclerView recyclerView;
    private AuthStateManager authStateManager;
    String orgId, clientId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.org_selector);

        Intent intentFarmSelect = getIntent();
        orgId = intentFarmSelect.getStringExtra("org_id");
        clientId = intentFarmSelect.getStringExtra("client_id");

        LoadConfig config = LoadConfig.getInstance(this);
        authStateManager = AuthStateManager.getInstance(this);
        authState = authStateManager.getCurrent();

        authorizationService = new AuthorizationService(this,
                new AppAuthConfiguration
                        .Builder()
                        .setConnectionBuilder(config.getConnectionBuilder())
                        .build());
        authState.performActionWithFreshTokens(authorizationService,
                (String accessToken,
                 String idToken,
                 AuthorizationException ex) -> {

                    GetFarmsInterface farmsInterface = RetrofitInstance
                            .getRetrofitInstance()
                            .create(GetFarmsInterface.class);
                    Call<FarmsList> farmsListCall = farmsInterface
                            .getOrgAndClientData(
                                    orgId + "",
                                    clientId + "",
                                    "Bearer " + accessToken);

                    Log.wtf("Called Farms GET", farmsListCall.request().url() + "");

                    farmsListCall.enqueue(new Callback<FarmsList>() {
                        @Override
                        public void onResponse(Call<FarmsList> call, Response<FarmsList> response) {
                            generateFarmsList(response.body().getFarmsArrayList());
                        }

                        @Override
                        public void onFailure(Call<FarmsList> call, Throwable t) {
                            Toast.makeText(FarmSelect.this,
                                    "Error: " + t.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                });
    }

    private void generateFarmsList(ArrayList<Farms> getFarmsArrayList) {
        recyclerView = findViewById(R.id.recycler_view_content_field_selector);
        farmsAdapter = new FarmsAdapter(getFarmsArrayList, orgId, FarmSelect.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(FarmSelect.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(farmsAdapter);
    }
}
