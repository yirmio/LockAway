package com.yirmio.lockaway.UI;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.yirmio.lockaway.LockAwayApplication;
import com.yirmio.lockaway.R;

public class OrderBuilderActivity extends FragmentActivity implements OrderBuilderFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 setContentView(R.layout.activity_order_builder);
    }

    @Override
    public void onFragmentInteraction(String itemID, String orderID, String opp) {
        if (opp == "remove") {
            LockAwayApplication.parseConector.removeObjectFromOrder(orderID, itemID);

        }

    }

    @Override
    public void onFragmentInteraction(MenuListRowLayoutItem item, String opp) {
        if (opp == "remove") {
            LockAwayApplication.getUserOrder().removeItemFromOrder(item.getId());
        }

    }
}
