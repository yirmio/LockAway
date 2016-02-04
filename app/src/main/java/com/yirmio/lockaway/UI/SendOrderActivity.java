package com.yirmio.lockaway.UI;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.yirmio.lockaway.DAL.LocationUtils;
import com.yirmio.lockaway.LockAwayApplication;
import com.yirmio.lockaway.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class SendOrderActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, LocationListener {
    //Local Members
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private boolean mMapIsReady;
    private GoogleMap mGogleMap;
    private String orderID;
    private String lastETAStr;
    private LocalDateTime lastETATime;
    private LocalDateTime newETATime;

    //GUI Elements
    private TextView mCurAddressTextView;
    private TextView mTimeToMakeValue;
    private TextView mTotalPriceValue;
    private TextView mItemsCount;
    private TextView mETAValueTxtView;
    private Button mSendBtn;
    private LocationManager locManager;
    private Marker curMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        intent.putExtra("totalPrice",this.mTotalPriceTextView.getText());
//        intent.putExtra("totalTimeToMake",this.mTotalTimeTextView.getText());
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_order);
        Bundle extras = getIntent().getExtras();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //GUI Elements

        this.mCurAddressTextView = (TextView) findViewById(R.id.txtVw_send_order_current_address_label);
        this.mTotalPriceValue = (TextView) findViewById(R.id.txtVw_send_order_total_price_value);
        this.mTimeToMakeValue = (TextView) findViewById(R.id.txtVw_send_order_time_to_make_value);
        this.mETAValueTxtView = (TextView) findViewById(R.id.txtVw_send_order_eta_value);
        this.mItemsCount = (TextView) findViewById(R.id.txtVw_send_order_total_items_value);
        this.mSendBtn = (Button) findViewById(R.id.btn_send_order_confirm_send);

        this.mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOrderToCloud();

            }


        });

        //Get Data
        if (extras != null) {
            this.mTotalPriceValue.setText(extras.getString("totalPrice").toString());
            this.mTimeToMakeValue.setText(extras.getString("totalTimeToMake"));
            this.mItemsCount.setText(extras.getString("itemsCount"));
            this.orderID = extras.getString("OrderID");

        }
        //Build map
        buildGoogleApiClient();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frgmnt_send_order_map);
        mapFragment.getMapAsync(this);

        //Location Listener
        this.locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);



    }

    private void sendOrderToCloud() {
        //var orderID,userETA;
        HashMap<String, Object> dict = new HashMap<String, Object>();
        dict.put("orderID", this.orderID);
        if (this.lastETATime != null) {
            dict.put("userETA", lastETATime.toString());
        } else {
            dict.put("userETA",newETATime.toString());
        }
        Toast.makeText(this.getApplicationContext(), "Conecting to cloud", Toast.LENGTH_SHORT).show();

        ParseCloud.callFunctionInBackground("sendNewOrder", dict, new FunctionCallback<Object>() {
            @Override
            public void done(Object o, ParseException e) {
                if (e != null) {
                    Toast.makeText(getApplicationContext(), "Error Saving Order " + e.getMessage(), Toast.LENGTH_LONG).show();
                } else {

                    orderSavedSuccess(o.toString());
                }
            }
        });
    }

    private void orderSavedSuccess(String s) {

        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        this.mSendBtn.setText(R.string.order_confirmed);
        this.mSendBtn.setEnabled(false);

        //Register Location Change
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 100, this);//TODO - change params before release

        //Open new Activity
        Intent intent = new Intent(SendOrderActivity.this,OrderStatusActivity.class);
        startActivity(intent);

    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            if (mMapIsReady) {
                mGogleMap.setTrafficEnabled(true);

                refreshMap(mGogleMap);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMapIsReady = true;
        mGogleMap = googleMap;

        mGogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mGogleMap.clear();
                putMarkerOnMap(mGogleMap, latLng);
                updateMapInfo(latLng);
                refreshMap(mGogleMap);

            }
        });

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        mLastLocation = LocationServices.FusedLocationApi.
        refreshMap(googleMap);

    }

    private void updateMapInfo(LatLng latLng) {
        //Get Address
        List<String> addresses;

        addresses = LocationUtils.getAddressByLocation(latLng.latitude, latLng.longitude);
        if (addresses != null) {
            mCurAddressTextView.setText(addresses.get(0));
        }

        //Get ETA & Distance
        Map<String, String> disAndTimeMap = LocationUtils.getETAAndDistanceInfo(latLng, LockAwayApplication.AfeyaLatLang);
        if (disAndTimeMap != null) {
            LocalTime dateTime = LocalTime.now().plusSeconds(Integer.parseInt(disAndTimeMap.get("totalTimeInSec")));
            LocalDateTime localDateTime = LocalDateTime.now().plusSeconds(Integer.parseInt(disAndTimeMap.get("totalTimeInSec")));

            this.newETATime = localDateTime;
            DateTimeFormatter dFmtr = DateTimeFormat.forPattern("HH:mm");
            mETAValueTxtView.setText(localDateTime.toString(dFmtr));
            lastETAStr = dateTime.toString(dFmtr);

        }
    }

    private void putMarkerOnMap(GoogleMap mGogleMap, LatLng latLng) {

        //Add Marker
        if (this.curMarker != null) {
            this.curMarker.remove();
        }
        ;
        this.curMarker = mGogleMap.addMarker(new MarkerOptions().position(latLng).title("You"));
        mGogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));




    }

    private void refreshMap(GoogleMap googleMap) {
        if (mLastLocation != null) {
            LatLng mLatLang = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            putMarkerOnMap(googleMap, mLatLang);
            updateMapInfo(mLatLang);

        }
    }

    @Override
    public void onLocationChanged(Location location) {

            this.mLastLocation = location;
            this.refreshMap(this.mGogleMap);


        if (ETANeedUpdate(newETATime)) {
            //Send To Cloud
            HashMap<String, Object> dict = new HashMap<String, Object>();
            dict.put("orderID", this.orderID);
            dict.put("userETA", lastETATime.toString());
//            Toast.makeText(this.getApplicationContext(), "Conecting to cloud", Toast.LENGTH_SHORT).show();

            ParseCloud.callFunctionInBackground("updateTimeToArrive", dict, new FunctionCallback<Object>() {
                @Override
                public void done(Object o, ParseException e) {
                    if (e != null) {
                        Toast.makeText(getApplicationContext(), "Error Saving Order " + e.getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), o.toString(), Toast.LENGTH_SHORT).show();
                        }
                }
            });

        }

    }

    private boolean ETANeedUpdate(LocalDateTime newETA) {
        boolean needUpdate = false;
        if (this.lastETATime == null){
            this.lastETATime = newETA;
        }
        else {
            if (Math.abs(Minutes.minutesBetween(lastETATime,newETA).getMinutes()) > 1){
                this.lastETATime = newETA;
                needUpdate = true;
            }
        }
        return needUpdate;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Toast.makeText(getBaseContext(), "Gps is turned off!! ",
                Toast.LENGTH_SHORT).show();

    }
}
