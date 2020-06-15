package com.jb.waypoint.selectors;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
    String orgId, orgName;

    SelectorState selectorState = SelectorState.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.org_selector);

        orgId = selectorState.getOrgId();
        orgName = selectorState.getOrgName();

        Toolbar selectorToolbar = findViewById(R.id.selector_toolbar);
        setSupportActionBar(selectorToolbar);
        getSupportActionBar().setTitle("Choose Client");

        setOrgText(orgName, true);
        setClientText("", false);
        setFarmText("", false);
        setFieldText("", false);

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
        clientsAdapter = new ClientsAdapter(getClientsValuesArrayList,
                orgId,
                orgName,
                ClientSelect.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                ClientSelect.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(clientsAdapter);
    }

    private void setOrgText(String txtOrg, boolean visible) {
        TextView topText = findViewById(R.id.selector_header_text_org);
        if (visible) {
            topText.setText(txtOrg + " > ");
        }
        else {
            topText.setVisibility(View.INVISIBLE);
        }
    }

    private void setClientText(String txtClient, boolean visible) {


        TextView topText = findViewById(R.id.selector_header_text_client);
        if (visible) {
            topText.setText(txtClient + " > ");}
        else {
            topText.setVisibility(View.INVISIBLE);
        }
    }

    private void setFarmText(String txtFarm, boolean visible) {
        TextView topText = findViewById(R.id.selector_header_text_farm);
        if (visible) {
            topText.setText(txtFarm + " > ");
        }
        else {
            topText.setVisibility(View.INVISIBLE);
        }
    }

    private void setFieldText(String txtField, boolean visible) {
        TextView topText = findViewById(R.id.selector_header_text_field);

        if (visible) {
            topText.setText(txtField + " > ");
        }
        else {
            topText.setVisibility(View.INVISIBLE);
        }
    }
}
