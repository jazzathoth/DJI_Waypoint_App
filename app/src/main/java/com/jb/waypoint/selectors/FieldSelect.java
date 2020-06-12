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
import com.jb.waypoint.adapter.FieldsAdapter;
import com.jb.waypoint.interfaces.GetFieldsInterface;
import com.jb.waypoint.model.Fields;
import com.jb.waypoint.model.FieldsList;
import com.jb.waypoint.retrofit_instance.RetrofitInstance;

import net.openid.appauth.AppAuthConfiguration;
import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FieldSelect extends AppCompatActivity {

    private static final String TAG = "Choose Field";

    private AuthState authState;
    private AuthorizationService authorizationService;
    private FieldsAdapter fieldsAdapter;
    private RecyclerView recyclerView;
    private AuthStateManager authStateManager;
    String orgId, clientId, farmId;
    String orgName, clientName, farmName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.org_selector);

        SelectorState selectorState = SelectorState.getInstance();

        orgId = selectorState.getOrgId();
        clientId = selectorState.getClientId();
        farmId = selectorState.getFarmId();

        orgName = selectorState.getOrgName();
        clientName = selectorState.getClientName();
        farmName = selectorState.getFarmName();

        Toolbar selectorToolbar = findViewById(R.id.selector_toolbar);
        setSupportActionBar(selectorToolbar);
        getSupportActionBar().setTitle("Choose Field");

        setOrgText(orgName, true);
        setClientText(clientName, true);
        setFarmText(farmName, true);
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
                    GetFieldsInterface fieldsInterface = RetrofitInstance
                            .getRetrofitInstance()
                            .create(GetFieldsInterface.class);
                    Call<FieldsList> fieldsListCall = fieldsInterface.getOrgAndFarmData(
                            orgId + "",
                            farmId + "",
                            "Bearer " + accessToken);

                    Log.wtf("Called Fields GET", fieldsListCall.request().url() + "");

                    fieldsListCall.enqueue(new Callback<FieldsList>() {
                        @Override
                        public void onResponse(Call<FieldsList> call,
                                               Response<FieldsList> response) {
                            generateFieldsList(response.body().getFieldsArrayList());
                        }

                        @Override
                        public void onFailure(Call<FieldsList> call, Throwable t) {
                            Toast.makeText(FieldSelect.this,
                                    "Error: " + t.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                });
    }

    private void generateFieldsList(ArrayList<Fields> getFieldsArrayList) {
        recyclerView = findViewById(R.id.recycler_view_content_field_selector);
        fieldsAdapter = new FieldsAdapter(
                getFieldsArrayList,
                orgId + "",
                farmId + "",
                FieldSelect.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                FieldSelect.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(fieldsAdapter);
    }

    private void setOrgText(String txtOrg, boolean visible) {
        TextView topText = findViewById(R.id.selector_header_text_org);
        topText.setText(txtOrg);
        if (!visible) {
            topText.setVisibility(View.INVISIBLE);
        }
    }

    private void setClientText(String txtClient, boolean visible) {


        TextView topText = findViewById(R.id.selector_header_text_client);
        if (!visible) {
            topText.setText(txtClient);}
        else {
            topText.setVisibility(View.INVISIBLE);
        }
    }

    private void setFarmText(String txtFarm, boolean visible) {
        TextView topText = findViewById(R.id.selector_header_text_farm);
        topText.setText(txtFarm);
        if (!visible) {
            topText.setVisibility(View.INVISIBLE);
        }
    }

    private void setFieldText(String txtField, boolean visible) {
        TextView topText = findViewById(R.id.selector_header_text_field);
        topText.setText(txtField);
        if (!visible) {
            topText.setVisibility(View.INVISIBLE);
        }
    }
}
