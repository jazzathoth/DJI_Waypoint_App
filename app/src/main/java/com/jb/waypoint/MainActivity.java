package com.jb.waypoint;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import dji.common.error.DJIError;
import dji.common.error.DJISDKError;
import dji.sdk.base.BaseComponent;
import dji.sdk.base.BaseProduct;
import dji.sdk.sdkmanager.DJISDKInitEvent;
import dji.sdk.sdkmanager.DJISDKManager;

public class MainActivity extends FragmentActivity implements View.OnClickListener, GoogleMap.OnMapClickListener, OnMapReadyCallback {

    private static final String TAG = MainActivity.class.getName();
    public static final String FLAG_CONNECTION_CHANGE = "dji_sdk_connection_change";
    private static BaseProduct mProduct;
    private Handler mHandler;
    private GoogleMap gMap;
    private Button locate, add, clear;
    private Button config, upload, start, stop;

    private static final String[] REQUIRED_PERMISSION_LIST = new String[]{
            Manifest.permission.VIBRATE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
    };

    private List<String> missingPermission = new ArrayList<>();
    private AtomicBoolean isRegistrationInProgress = new AtomicBoolean(false);
    private static final int REQUEST_PERMISSION_CODE = 12345;

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onReturn(View view){
        Log.d(TAG, "onReturn");
        this.finish();
    }

    private void initUI() {
        locate = (Button) findViewById(R.id.locate);
        add = (Button) findViewById(R.id.add);
        clear = (Button) findViewById(R.id.clear);
        config = (Button) findViewById(R.id.config);
        upload = (Button) findViewById(R.id.upload);
        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);

        locate.setOnClickListener(this);
        add.setOnClickListener(this);
        clear.setOnClickListener(this);
        config.setOnClickListener(this);
        upload.setOnClickListener(this);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // When the compile and target version is higher than 22, please request the following
        // permission at runtime to ensure the SDK works well.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndRequestPermissions();
        }

        setContentView(R.layout.activity_main);
        initUI();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mHandler = new Handler(Looper.getMainLooper());
    }

    private void showSettingsDialog() {
        LinearLayout wayPointSettings = (LinearLayout)getLayoutInflater()
                .inflate(R.layout.dialog_waypointsetting, null);
        final TextView wpAltitude_TV = (TextView) wayPointSettings.findViewById(R.id.altitude);
        RadioGroup speed_RG = (RadioGroup) wayPointSettings.findViewById(R.id.speed);
        RadioGroup actionAfterFinished_RG = (RadioGroup) wayPointSettings.findViewById(R.id.actionAfterFinished);
        RadioGroup heading_RG = (RadioGroup) wayPointSettings.findViewById(R.id.heading);

        speed_RG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d(TAG, "Selected Speed");

            }
        });

        actionAfterFinished_RG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d(TAG, "Selected action");
            }
        });

        heading_RG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                Log.d(TAG, "Select heading finish");
            }
        });

        new AlertDialog.Builder(this)
                .setTitle("")
                .setView(wayPointSettings)
                .setPositiveButton("Finish",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {}
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .create()
                .show();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.config:{
                showSettingsDialog();
                break;
            }
            default:
                break;
        }
    }

    public void onMapReady(GoogleMap googleMap) {
        if (gMap == null) {
            gMap = googleMap;
            setUpMap();
        }
        LatLng hburg = new LatLng(38.482934, -78.870652);
        gMap.addMarker(new MarkerOptions().position(hburg).title("Harrisonburg Marker"));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(hburg));

    }

    private void setUpMap() {
        gMap.setOnMapClickListener(this);
    }

    private void onMapClick(LatLng point) {
    }

    private void checkAndRequestPermissions() {
        // Checking for permissions
        for (String eachPermission : REQUIRED_PERMISSION_LIST) {
            if (ContextCompat.checkSelfPermission(this, eachPermission) !=
                    PackageManager.PERMISSION_GRANTED) {
                missingPermission.add(eachPermission);
            }
        }

        // Request missing permissions
        if (missingPermission.isEmpty()) {
            startSDKRegistration();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            showToast("Permissions needed to continue!");
            ActivityCompat.requestPermissions(this,
                    missingPermission.toArray(new String[missingPermission.size()]),
                    REQUEST_PERMISSION_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Check for granted permissions and remove from list of missing
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = grantResults.length - 1; i >= 0; i--) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    missingPermission.remove(permissions[i]);
                }
            }
        }

        // Start registration if all required permissions are granted
        if (missingPermission.isEmpty()) {
            startSDKRegistration();
        } else {
            showToast("Permissions still missing!");
        }
    }

    private void startSDKRegistration() {
        if (isRegistrationInProgress.compareAndSet(false, true)) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    showToast("Registering, Please wait...");
                    DJISDKManager.getInstance().registerApp(
                            MainActivity.this.getApplicationContext(),
                            new DJISDKManager.SDKManagerCallback() {
                                @Override
                                public void onRegister(DJIError djiError) {
                                    if (djiError == DJISDKError.REGISTRATION_SUCCESS) {
                                        showToast("Register Success");
                                        DJISDKManager.getInstance().startConnectionToProduct();
                                    } else {
                                        showToast("Register sdk fails, please check the " +
                                                "bundle id and network connection!");
                                    }
                                    Log.v(TAG, djiError.getDescription());
                                }

                                @Override
                                public void onProductDisconnect() {
                                    Log.d(TAG, "onProductDisconnect");
                                    showToast("Disconnected aircraft");
                                    notifyStatusChange();
                                }

                                @Override
                                public void onProductConnect(BaseProduct baseProduct) {
                                    Log.d(TAG,
                                            String.format("onProductConnect newProduct:%s",
                                                    baseProduct));
                                    showToast("Product Connected");
                                    notifyStatusChange();
                                }

                                @Override
                                public void onComponentChange(BaseProduct.ComponentKey componentKey,
                                                              BaseComponent oldComponent,
                                                              BaseComponent newComponent) {
                                    if (newComponent != null) {
                                        newComponent.setComponentListener(new BaseComponent.ComponentListener() {
                                            @Override
                                            public void onConnectivityChange(boolean isConnected) {
                                                Log.d(TAG, "onComponentConnectivityChanged: " +
                                                        isConnected);
                                                notifyStatusChange();
                                            }
                                        });
                                    }
                                    Log.d(TAG,
                                            String.format("onComponentChange key:%s, " +
                                                    "oldComponent:%s, " +
                                                    "newComponent:%s",
                                                    componentKey,
                                                    oldComponent,
                                                    newComponent));
                                }

                                @Override
                                public void onInitProcess(DJISDKInitEvent djisdkInitEvent, int i) {

                                }

                                @Override
                                public void onDatabaseDownloadProgress(long l, long l1) {

                                }
                            });
                }
            });
        }
    }

    private void notifyStatusChange() {
        mHandler.removeCallbacks(updateRunnable);
        mHandler.postDelayed(updateRunnable, 500);
    }

    private Runnable updateRunnable = new Runnable() {

        @Override
        public void run() {
            Intent intent = new Intent(FLAG_CONNECTION_CHANGE);
            sendBroadcast(intent);
        }
    };

    private void showToast(final String toastMsg) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_LONG).show();
            }
        });

    }
}