package com.yirmio.lockaway;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class SendOrderActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private boolean mMapIsReady;
    private GoogleMap mGogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_order);
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
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        mLastLocation = LocationServices.FusedLocationApi.
        refreshMap(googleMap);

    }

    private void refreshMap(GoogleMap googleMap) {
        if (mLastLocation != null) {
            LatLng mLatLang = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(mLatLang).title("You"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(mLatLang));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLang,50));

        }
    }
}
