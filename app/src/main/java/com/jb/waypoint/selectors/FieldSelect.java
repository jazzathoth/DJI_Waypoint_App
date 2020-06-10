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
    String orgId, farmId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.org_selector);

        Intent intentFieldSelect = getIntent();
        orgId = intentFieldSelect.getStringExtra("org_id");
        farmId = intentFieldSelect.getStringExtra("farm_id");

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
}
