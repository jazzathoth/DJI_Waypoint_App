package com.jb.waypoint;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jb.waypoint.adapter.ClientsAdapter;
import com.jb.waypoint.adapter.OrganizationsAdapter;
import com.jb.waypoint.interfaces.GetClientsInterface;
import com.jb.waypoint.model.Clients;
import com.jb.waypoint.model.ClientsList;
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
    private OrganizationsAdapter organizationsAdapter;
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
                new AppAuthConfiguration.Builder()
                .setConnectionBuilder(config.getConnectionBuilder())
                .build());

        authState.performActionWithFreshTokens(authorizationService, (String accessToken,
                                                                      String idToken,
                                                                      AuthorizationException ex) -> {
            GetClientsInterface clientsInterface = RetrofitInstance
                    .getRetrofitInstance()
                    .create(GetClientsInterface.class);

            Call<ClientsList> clientsListCall = clientsInterface
                    .getOrganizationData(orgId, "Bearer " + accessToken);

            Log.wtf("Called Clients GET", clientsListCall.request().url() + "");

            clientsListCall.enqueue(new Callback<ClientsList>() {
                @Override
                public void onResponse(Call<ClientsList> call, Response<ClientsList> response) {
                    generateClientsList(response.body().getClientsArrayList());
                }

                @Override
                public void onFailure(Call<ClientsList> call, Throwable t) {
                    Toast.makeText(ClientSelect.this,
                            "Error: " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });

        });
    }

    private void generateClientsList(ArrayList<Clients> clientsArrayList) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_content_field_selector);
        ClientsAdapter adapter = new ClientsAdapter(clientsArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ClientSelect.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
