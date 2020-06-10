package com.jb.waypoint.selectors;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
//    private String baseUrl = getString(R.string.jd_base_url);
//    private JDClient client = new JDClient(baseUrl);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.org_selector);
//        Toolbar toolbar = findViewById(R.id.toolbar_field_selector);
//        setSupportActionBar(toolbar);

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
        RecyclerView recyclerView = findViewById(R.id.recycler_view_content_field_selector);
        OrganizationsAdapter adapter = new OrganizationsAdapter(organizationsArrayList, OrgSelect.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(OrgSelect.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }



}
