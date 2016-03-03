package com.yirmio.lockaway.UI.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseUser;
import com.yirmio.lockaway.LockAwayApplication;
import com.yirmio.lockaway.R;
import com.yirmio.lockaway.UI.util.OrdersListAdapter;

import java.util.ArrayList;
import java.util.List;

public class UserOrdersActivity extends AppCompatActivity {
    private ListView ordersListView;
    private TextView titleTextView;
    private String userId;
    private ArrayList ordersListData;
    private ListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_orders);
        this.ordersListData = LockAwayApplication.getAllUserOrders();
        this.mAdapter = new OrdersListAdapter(this, 0, ordersListData);

        attachUIElements();
    }

    private void attachUIElements() {
        this.ordersListView = (ListView) findViewById(R.id.user_orders_listView);
        this.titleTextView = (TextView) findViewById(R.id.user_orders_title_textView);
        this.ordersListView.setAdapter(this.mAdapter);
    }
}
