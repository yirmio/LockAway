package com.yirmio.lockaway.UI;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yirmio.lockaway.Location.LocationUtils;
import com.yirmio.lockaway.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class SendOrderActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private boolean mMapIsReady;
    private GoogleMap mGogleMap;
    private TextView mCurAddressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_send_order);
        this.mCurAddressTextView = (TextView) findViewById(R.id.txtVw_send_order_current_address_label);
        buildGoogleApiClient();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frgmnt_send_order_map);
        mapFragment.getMapAsync(this);

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
            if (mMapIsReady){
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
            }
        });

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        mLastLocation = LocationServices.FusedLocationApi.
        refreshMap(googleMap);

    }

    private void putMarkerOnMap(GoogleMap mGogleMap, LatLng latLng) {
        mGogleMap.addMarker(new MarkerOptions().position(latLng).title("You"));
        mGogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));


        List<String> addresses;

        addresses = LocationUtils.getLocationInfo(latLng.latitude,latLng.longitude);
        if (addresses != null){
            mCurAddressTextView.setText(addresses.get(0));
        }


    }

    private void refreshMap(GoogleMap googleMap) {
        if (mLastLocation != null) {
            LatLng mLatLang = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            putMarkerOnMap(googleMap,mLatLang);

        }
    }
}
