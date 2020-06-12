package com.jb.waypoint.selectors;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.jb.waypoint.AuthStateManager;
import com.jb.waypoint.LoadConfig;
import com.jb.waypoint.R;
import com.jb.waypoint.adapter.OrganizationsAdapter;
import com.jb.waypoint.interfaces.GetOrganizationsInterface;
import com.jb.waypoint.model.Organizations;
import com.jb.waypoint.model.OrganizationsList;
import com.jb.waypoint.retrofit_instance.RetrofitInstance;

import net.openid.appauth.AppAuthConfiguration;
import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrgSelect extends AppCompatActivity {

    private static final String TAG = "ChooseOrg";

    private AuthState authState;
    private AuthorizationService authorizationService;
    private OrganizationsAdapter organizationsAdapter;
    private RecyclerView recyclerView;
    private AuthStateManager authStateManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.org_selector);

        Toolbar selectorToolbar = findViewById(R.id.selector_toolbar);
        setSupportActionBar(selectorToolbar);
        getSupportActionBar().setTitle("Choose Organization");

        setOrgText("", false);
        setClientText("", false);
        setFarmText("", false);
        setFieldText("", false);

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
            GetOrganizationsInterface organizationsInterface = RetrofitInstance
                    .getRetrofitInstance()
                    .create(GetOrganizationsInterface.class);

            Call<OrganizationsList> organizationsListCall = organizationsInterface
                    .getOrganizationData("Bearer " + accessToken);

            Log.wtf("Called Organizations GET", organizationsListCall.request().url() + "");

            organizationsListCall.enqueue(new Callback<OrganizationsList>() {
                @Override
                public void onResponse(Call<OrganizationsList> call, Response<OrganizationsList> response) {
                    if(response.body() != null) {
                        generateOrganizationsList(response.body().getOrganizationsArrayList());
                    } else { Toast.makeText(OrgSelect.this,
                                     "response: " + response.code(),
                                     Toast.LENGTH_LONG).show();}
                }

                @Override
                public void onFailure(Call<OrganizationsList> call, Throwable t) {
                    Toast.makeText(OrgSelect.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }


    private void generateOrganizationsList(ArrayList<Organizations> organizationsArrayList) {

        recyclerView = findViewById(R.id.recycler_view_content_field_selector);
        organizationsAdapter = new OrganizationsAdapter(organizationsArrayList, OrgSelect.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(OrgSelect.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(organizationsAdapter);

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
