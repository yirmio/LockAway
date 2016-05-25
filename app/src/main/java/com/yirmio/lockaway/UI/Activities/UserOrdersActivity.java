package com.yirmio.lockaway.UI.Activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.yirmio.lockaway.LockAwayApplication;
import com.yirmio.lockaway.R;
import com.yirmio.lockaway.UI.util.OrdersListAdapter;

import java.util.ArrayList;

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
        LoadOrdersTask loadOrdersTask = new LoadOrdersTask(this);
        loadOrdersTask.execute();

    }

    private void attachUIElements() {
        this.ordersListView = (ListView) findViewById(R.id.user_orders_listView);
        this.titleTextView = (TextView) findViewById(R.id.user_orders_title_textView);
        this.ordersListView.setAdapter(this.mAdapter);
    }

    private class LoadOrdersTask extends AsyncTask {
        ProgressDialog dialog;
        UserOrdersActivity activity;

        public LoadOrdersTask(UserOrdersActivity userOrdersActivity) {
            activity = userOrdersActivity;
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage(getResources().getString(R.string.loading_order_wait));
            dialog.show();
        }

        @Override
        protected void onPostExecute(Object o) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            attachUIElements();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            ordersListData = LockAwayApplication.getAllUserOrders();
            mAdapter = new OrdersListAdapter(activity, 0, ordersListData);
            return null;
        }
    }
}
