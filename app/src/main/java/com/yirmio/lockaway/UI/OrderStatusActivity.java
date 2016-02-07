package com.yirmio.lockaway.UI;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.yirmio.lockaway.BL.UserOrder;
import com.yirmio.lockaway.LockAwayApplication;
import com.yirmio.lockaway.R;

public class OrderStatusActivity extends Activity implements View.OnClickListener {

    private TextView txtViewETAValue;
    private TextView txtViewOrderCode;
    private TextView txtViewTitle;
    private Button btnNavToRest;
    private Button btnAction;
    private ImageButton btnCallRest;
    private ImageView imgViewOrderStatusBigIcon;


    private UserOrder curOrder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        curOrder = LockAwayApplication.getUserOrder();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);


        attachUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDataInUI();
    }

    private void updateDataInUI() {
        String curETA = curOrder.getETA();
        if (curETA == null || curETA.length() == 0){
//            LockAwayApplication;
        }
        this.txtViewETAValue.setText(curETA);//TODO - insert info into field
        this.txtViewOrderCode.append(curOrder.getOrderId());
        //TODO - continue add info to ui
    }

    private void attachUI() {
        this.txtViewETAValue = (TextView)findViewById(R.id.orderStatusTextViewETAValue);
        this.txtViewOrderCode = (TextView)findViewById(R.id.orderStatusTextViewOrderCode);
        this.txtViewTitle = (TextView)findViewById(R.id.orderStatusTextViewTitle);

        this.btnNavToRest = (Button)findViewById(R.id.orderStatusBtnNavToPlace);
        this.btnNavToRest.setOnClickListener(this);
        this.btnCallRest = (ImageButton)findViewById(R.id.orderStatusBtnCallResturant);
        this.btnCallRest.setOnClickListener(this);
        this.btnAction = (Button) findViewById(R.id.orderStatusBtnAction);
        this.btnAction.setOnClickListener(this);

        this.imgViewOrderStatusBigIcon = (ImageView)findViewById(R.id.orderStatusImgViewStatusBigIcon);//TODO - change by order status


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.orderStatusBtnNavToPlace:
                //TODO navigate
                break;
            case R.id.orderStatusBtnCallResturant:
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + LockAwayApplication.getStore().getPhoneNumber()));
                startActivity(intent);
                break;
            case R.id.orderStatusBtnAction:
                ///TODO - handle action
                break;
        }
    }
}
