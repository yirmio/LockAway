package com.yirmio.lockaway.UI.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.yirmio.lockaway.BL.GlobalConsts;
import com.yirmio.lockaway.BL.UserOrder;
import com.yirmio.lockaway.DAL.LocationUtils;
import com.yirmio.lockaway.DAL.ParseConnector;
import com.yirmio.lockaway.Interfaces.Observer;
import com.yirmio.lockaway.LockAwayApplication;
import com.yirmio.lockaway.R;

import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderStatusActivity extends AppCompatActivity implements View.OnClickListener, Observer, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, LocationListener {

    private TextView txtViewETAValue;
    private TextView txtViewOrderCode;
    private TextView txtViewTitle;
    private Button btnNavToRest;
    private Button btnAction;
    private ImageButton btnCallRest;
    private ImageView imgViewOrderStatusBigIcon;


    private UserOrder curOrder;
    private ParseConnector parseConnector;
    private boolean isFromNotification;
    private LocationManager locManager;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private boolean mMapIsReady;
    private GoogleMap mGogleMap;
    private Marker curMarker;
    private LocalDateTime newETATime;
    private String lastETAStr;
    private LocalDateTime lastETATime;
    private String orderID;
    private Button btnCancelOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        parseConnector = LockAwayApplication.parseConector;
        if (getIntent().getExtras() != null) {
            Bundle b = getIntent().getExtras();
            isFromNotification = b.getBoolean("fromNotification");
        }

        curOrder = LockAwayApplication.getUserOrder();
        this.orderID = curOrder.getOrderId();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);


        attachUI();

        //Build map
        buildGoogleApiClient();
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.frgmnt_send_order_map);
//        mapFragment.getMapAsync(this);

        //Location Listener
        this.locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    //For eta and address
    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDataInUI();
        parseConnector.registerObserver(this);
//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        //Register Location Change
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 100, this);//TODO - change params before release
    }

    @Override
    protected void onPause() {
        super.onPause();
        parseConnector.removeObserver(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        locManager.removeUpdates(this);
    }

    private void updateDataInUI() {
        DateTimeFormatter dFmtr = DateTimeFormat.forPattern("HH:mm");
        String curETA = curOrder.getETA();
        if (curETA == null || curETA.length() == 0) {
        }

        this.txtViewETAValue.setText(curETA);
        this.txtViewOrderCode.setText(getString(R.string.your_order_code) + curOrder.getOrderId());
        //TODO - continue add info to ui

        //order status text
//        if (!isFromNotification){
//            this.txtViewTitle.setText(getString(R.string.order_confirmed));
//        }
//        else{
            String txtToPut = "";
            switch (curOrder.getOrderStatus()){

                case Active:
                    txtToPut = getString(R.string.order_on_building);
                    break;
                case OnHold:
                    txtToPut = getString(R.string.order_on_hold);
                    break;
                case OnDelay:
                    txtToPut = getString(R.string.order_on_delay);
                    break;
                case OnMake:
                    txtToPut = getString(R.string.order_on_make);
                    break;
                case Ready:
                    txtToPut = getString(R.string.order_is_ready);
                    break;
                case WaitingToStart:
                    txtToPut = getString(R.string.order_on_waiting_to_start);
                    break;
                case Finish:
                    txtToPut = getString(R.string.order_closed);
                    break;
            }
            this.txtViewTitle.setText(txtToPut);
//        }

        //order status image
        //TODO - implement

    }

    private void attachUI() {
        this.txtViewETAValue = (TextView) findViewById(R.id.orderStatusTextViewETAValue);
        this.txtViewOrderCode = (TextView) findViewById(R.id.orderStatusTextViewOrderCode);
        this.txtViewTitle = (TextView) findViewById(R.id.orderStatusTextViewTitle);

        this.btnNavToRest = (Button) findViewById(R.id.orderStatusBtnNavToPlace);
        this.btnNavToRest.setOnClickListener(this);
        this.btnCallRest = (ImageButton) findViewById(R.id.orderStatusBtnCallResturant);
        this.btnCallRest.setOnClickListener(this);
        this.btnAction = (Button) findViewById(R.id.orderStatusBtnAction);
        this.btnAction.setOnClickListener(this);
        this.btnCancelOrder = (Button) findViewById(R.id.orderStatusBtnCancle);
        this.btnCancelOrder.setOnClickListener(this);

        this.imgViewOrderStatusBigIcon = (ImageView) findViewById(R.id.orderStatusImgViewStatusBigIcon);//TODO - change by order status


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.orderStatusBtnNavToPlace:
                Intent navIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + LockAwayApplication.getStore().getLocation()));
                startActivity(navIntent);
                break;
            case R.id.orderStatusBtnCallResturant:
                Intent phoneIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + LockAwayApplication.getStore().getPhoneNumber()));
                startActivity(phoneIntent);
                break;
            case R.id.orderStatusBtnAction:
                changeUserETA();
                break;
            case R.id.orderStatusBtnCancle:
                cancelOrder();
        }
    }

    private void cancelOrder() {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        curOrder.cancelOrder(true);
                        parseConnector.cancelOrder(curOrder.getOrderId());
                        //TODO - back to main
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.are_you_sure).setPositiveButton(R.string.yes, dialogClickListener)
                .setNegativeButton(R.string.no, dialogClickListener).show();


    }

    private void changeUserETA() {
        final Calendar calendar = Calendar.getInstance();
        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = calendar.get(Calendar.MINUTE);
        TimePickerDialog tpd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            int timeSetCounter = 0;

            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {

                if (timeSetCounter == 0) {
                    curOrder.setNewETA(hourOfDay, minute);
                    parseConnector.setNewETAToOrder(curOrder.getOrderId(), hourOfDay, minute);//DAL
                    timeSetCounter++;
                } else {
                    timeSetCounter = 0;
                }

            }
        }, mHour, mMinute, false);
        tpd.show();
    }

    @Override
    public void update(String msg, Observer ob) {
        if (ob == this) {
            if (msg == GlobalConsts.OrderETAChangedSuccessfully) {
                updateDataInUI();
            }
            if (msg == getResources().getString(R.string.msg_OrderCanceledSuccess)){
                Toast.makeText(this,R.string.msg_OrderCanceledSuccess,Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        if (mLastLocation != null) {
//            if (mMapIsReady) {
//                mGogleMap.setTrafficEnabled(true);
//
//                refreshMap(mGogleMap);
//            }
//        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void refreshMap(GoogleMap mGogleMap) {
        if (mLastLocation != null) {
            LatLng mLatLang = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            putMarkerOnMap(mGogleMap, mLatLang);
            updateMapInfo(mLatLang);

        }
    }

    private void updateMapInfo(LatLng latLng) {
        //Get ETA & Distance
        Map<String, String> disAndTimeMap = LocationUtils.getETAAndDistanceInfo(latLng, LockAwayApplication.AfeyaLatLang);
        if (disAndTimeMap != null) {
            LocalTime dateTime = LocalTime.now().plusSeconds(Integer.parseInt(disAndTimeMap.get("totalTimeInSec")));
            LocalDateTime localDateTime = LocalDateTime.now().plusSeconds(Integer.parseInt(disAndTimeMap.get("totalTimeInSec")));

            this.newETATime = localDateTime;
            DateTimeFormatter dFmtr = DateTimeFormat.forPattern("HH:mm");
            txtViewETAValue.setText(localDateTime.toString(dFmtr));
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

    @Override
    public void onLocationChanged(Location location) {
        this.mLastLocation = location;
        LatLng tmpLatLng = new LatLng(location.getLatitude(),location.getLongitude());

        //Get ETA & Distance
        new GetETAAndDistance().execute(tmpLatLng);
//        Map<String, String> disAndTimeMap = LocationUtils.getETAAndDistanceInfo(tmpLatLng, LockAwayApplication.AfeyaLatLang);
//        if (disAndTimeMap != null) {
//            LocalTime dateTime = LocalTime.now().plusSeconds(Integer.parseInt(disAndTimeMap.get("totalTimeInSec")));
//            LocalDateTime localDateTime = LocalDateTime.now().plusSeconds(Integer.parseInt(disAndTimeMap.get("totalTimeInSec")));
//
//            this.newETATime = localDateTime;
//            DateTimeFormatter dFmtr = DateTimeFormat.forPattern("HH:mm");
//            txtViewETAValue.setText(localDateTime.toString(dFmtr));
//            lastETAStr = dateTime.toString(dFmtr);
//
//        }
//
//
//        if (ETANeedUpdate(newETATime)) {
//            //Update local Order BL
//            DateTimeFormatter dFmtr = DateTimeFormat.forPattern("HH:mm");
////            mETAValueTxtView.setText(localDateTime.toString(dFmtr));
//            lastETAStr = newETATime.toString(dFmtr);
//            LockAwayApplication.getUserOrder().setETA(lastETAStr);
//            //Send To Cloud
//            HashMap<String, Object> dict = new HashMap<String, Object>();
//            dict.put("orderID", this.orderID);
//            dict.put("userETA", lastETATime.toString());
//
//
//            ParseCloud.callFunctionInBackground("updateTimeToArrive", dict, new FunctionCallback<Object>() {
//                @Override
//                public void done(Object o, ParseException e) {
//                    if (e != null) {
//                        Toast.makeText(getApplicationContext(), "Error Saving Order " + e.getMessage(), Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(getApplicationContext(), o.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//
//        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    private boolean ETANeedUpdate(LocalDateTime newETA) {
        //TODO - do it in BL
        boolean needUpdate = false;
        if (this.lastETATime == null) {
            this.lastETATime = newETA;
        } else {
            if (Math.abs(Minutes.minutesBetween(lastETATime, newETA).getMinutes()) > 1) {
                this.lastETATime = newETA;
                needUpdate = true;
            }
        }
        return needUpdate;
    }


    @Override
    public void onProviderDisabled(String s) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Toast.makeText(getBaseContext(), "Gps is turned off!! ",
                Toast.LENGTH_SHORT).show();

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

    private class GetETAAndDistance extends AsyncTask<LatLng,String,String>{
        Map<String, String> disAndTimeMap;
        @Override
        protected String doInBackground(LatLng... strings) {
            LatLng tmpLatLng = strings[0];
            disAndTimeMap = LocationUtils.getETAAndDistanceInfo(tmpLatLng, LockAwayApplication.AfeyaLatLang);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //TODO - remove progress bar
            //TODO - update UI and more


            if (disAndTimeMap != null) {
                LocalTime dateTime = LocalTime.now().plusSeconds(Integer.parseInt(disAndTimeMap.get("totalTimeInSec")));
                LocalDateTime localDateTime = LocalDateTime.now().plusSeconds(Integer.parseInt(disAndTimeMap.get("totalTimeInSec")));

                newETATime = localDateTime;
                DateTimeFormatter dFmtr = DateTimeFormat.forPattern("HH:mm");
                txtViewETAValue.setText(localDateTime.toString(dFmtr));
                lastETAStr = dateTime.toString(dFmtr);

            }

            if (ETANeedUpdate(newETATime)) {
                //Update local Order BL
                DateTimeFormatter dFmtr = DateTimeFormat.forPattern("HH:mm");
//            mETAValueTxtView.setText(localDateTime.toString(dFmtr));
                lastETAStr = newETATime.toString(dFmtr);
                LockAwayApplication.getUserOrder().setETA(lastETAStr);
                //Send To Cloud
                HashMap<String, Object> dict = new HashMap<String, Object>();
                dict.put("orderID", orderID);
                dict.put("userETA", lastETATime.toString());


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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //TODO - progress dialog
        }
    }
}
