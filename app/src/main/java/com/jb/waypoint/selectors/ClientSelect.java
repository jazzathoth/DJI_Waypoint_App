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
import com.jb.waypoint.R;
import com.jb.waypoint.adapter.ClientsAdapter;
import com.jb.waypoint.interfaces.GetClientsInterface;
import com.jb.waypoint.model.ClientsValues;
import com.jb.waypoint.model.ClientsBase;
import com.jb.waypoint.retrofit_instance.RetrofitInstance;

import net.openid.appauth.AppAuthConfiguration;
import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientSelect extends AppCompatActivity {
    private static final String TAG = "ChooseClient";

    private AuthState authState;
    private AuthorizationService authorizationService;
    private ClientsAdapter clientsAdapter;
    private RecyclerView recyclerView;
    private AuthStateManager authStateManager;
    String orgId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.org_selector);

        Intent intentClientSelect = getIntent();
        orgId = intentClientSelect.getStringExtra("org_id");

        LoadConfig config = LoadConfig.getInstance(this);
        authStateManager = AuthStateManager.getInstance(this);
        authState = authStateManager.getCurrent();

        authorizationService = new AuthorizationService(
                this,
                new AppAuthConfiguration
                        .Builder()
                        .setConnectionBuilder(config.getConnectionBuilder())
                        .build());

        authState.performActionWithFreshTokens(authorizationService,
                (String accessToken,
                 String idToken,
                 AuthorizationException ex) -> {
                    GetClientsInterface clientsInterface = RetrofitInstance
                            .getRetrofitInstance()
                            .create(GetClientsInterface.class);

                    Call<ClientsBase> clientsListCall = clientsInterface
                            .getOrganizationData(orgId + "", "Bearer " + accessToken);

                    Log.wtf("Called Clients GET", clientsListCall.request().url() + "");

                    clientsListCall.enqueue(new Callback<ClientsBase>() {
                        @Override
                        public void onResponse(Call<ClientsBase> call, Response<ClientsBase> response) {
                            if(response.body() != null) {
                                generateClientsList(response.body().getClientsValuesArrayList());
                            } else { Toast.makeText(ClientSelect.this,
                                     "response: " + response.code(),
                                     Toast.LENGTH_LONG).show();}
                        }

                        @Override
                        public void onFailure(Call<ClientsBase> call, Throwable t) {
                            Toast.makeText(ClientSelect.this,
                                    "Error: " + t.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                });
    }

    private void generateClientsList(ArrayList<ClientsValues> getClientsValuesArrayList) {
        recyclerView = findViewById(R.id.recycler_view_content_field_selector);
        clientsAdapter = new ClientsAdapter(getClientsValuesArrayList, orgId,ClientSelect.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ClientSelect.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(clientsAdapter);
    }
}
