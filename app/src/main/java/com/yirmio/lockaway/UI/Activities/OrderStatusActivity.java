package com.yirmio.lockaway.UI.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.yirmio.lockaway.BL.GlobalConsts;
import com.yirmio.lockaway.BL.UserOrder;
import com.yirmio.lockaway.DAL.ParseConnector;
import com.yirmio.lockaway.Interfaces.Observer;
import com.yirmio.lockaway.LockAwayApplication;
import com.yirmio.lockaway.R;

import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;

public class OrderStatusActivity extends Activity implements View.OnClickListener, Observer {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().getExtras() != null) {
            Bundle b = getIntent().getExtras();
            isFromNotification = b.getBoolean("fromNotification");
        }
        curOrder = LockAwayApplication.getUserOrder();
        parseConnector = LockAwayApplication.parseConector;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);


        attachUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDataInUI();
        parseConnector.registerObserver(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        parseConnector.removeObserver(this);
    }

    private void updateDataInUI() {
        DateTimeFormatter dFmtr = DateTimeFormat.forPattern("HH:mm");
        String curETA = curOrder.getETA();
        if (curETA == null || curETA.length() == 0) {
//            LockAwayApplication;
        }
//        mETAValueTxtView.setText(localDateTime.toString(dFmtr));
        this.txtViewETAValue.setText(curETA);
        this.txtViewOrderCode.setText(getString(R.string.your_order_code) + curOrder.getOrderId());
        //TODO - continue add info to ui
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
        }
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
        }
    }

}
