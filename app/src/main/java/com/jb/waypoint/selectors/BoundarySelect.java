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
import com.jb.waypoint.adapter.BoundariesAdapter;
import com.jb.waypoint.interfaces.GetBoundariesInterface;
import com.jb.waypoint.model.Boundaries;
import com.jb.waypoint.model.BoundariesList;
import com.jb.waypoint.retrofit_instance.RetrofitInstance;

import net.openid.appauth.AppAuthConfiguration;
import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BoundarySelect extends AppCompatActivity {
    private static final String TAG = "Choose Boundary";

    private AuthState authState;
    private AuthorizationService authorizationService;
    private BoundariesAdapter boundariesAdapter;
    private RecyclerView recyclerView;
    private AuthStateManager authStateManager;
    String orgId, clientId, farmId, fieldId;
    String orgName, clientName, farmName, fieldName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.org_selector);

        SelectorState selectorState = SelectorState.getInstance();

        orgId = selectorState.getOrgId();
        clientId = selectorState.getClientId();
        farmId = selectorState.getFarmId();
        fieldId = selectorState.getFieldId();

        orgName = selectorState.getOrgName();
        clientName = selectorState.getClientName();
        farmName = selectorState.getFarmName();
        fieldName = selectorState.getFieldName();

        Toolbar selectorToolbar = findViewById(R.id.selector_toolbar);
        setSupportActionBar(selectorToolbar);
        getSupportActionBar().setTitle("Choose Organization");

        setOrgText(orgName, true);
        setClientText(clientName, true);
        setFarmText(farmName, true);
        setFieldText(fieldName, true);

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
                     GetBoundariesInterface boundariesInterface = RetrofitInstance
                             .getRetrofitInstance()
                             .create(GetBoundariesInterface.class);
                     Call<BoundariesList> boundariesListCall = boundariesInterface
                             .getOrgAndFieldData(
                                     orgId + "",
                                     fieldId + "",
                                     "Bearer " + accessToken);

                     Log.wtf("Called Boundaries GET", boundariesListCall.request().url() + "");

                     boundariesListCall.enqueue(new Callback<BoundariesList>() {
                         @Override
                         public void onResponse(Call<BoundariesList> call, Response<BoundariesList> response) {
                             generateBoundariesList(response.body().getBoundariesArrayList());
                         }

                         @Override
                         public void onFailure(Call<BoundariesList> call, Throwable t) {
                             Toast.makeText(BoundarySelect.this,
                                     "Error: " + t.getMessage(),
                                     Toast.LENGTH_LONG).show();
                         }
                     });
                 });
    }

    private void  generateBoundariesList(ArrayList<Boundaries> getBoundariesArrayList) {
        recyclerView = findViewById(R.id.recycler_view_content_field_selector);

        boundariesAdapter = new BoundariesAdapter(
                getBoundariesArrayList,
                orgId + "",
                fieldId + "",
                BoundarySelect.this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                BoundarySelect.this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(boundariesAdapter);
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
