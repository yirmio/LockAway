package com.yirmio.lockaway.UI.ListsItems;

import android.widget.TextView;

import com.yirmio.lockaway.BL.UserOrder;
import com.yirmio.lockaway.UI.util.UserStatusLauncher;

/**
 * Created by oppenhime on 23/02/2016.
 */
public class OrderListRowItem {
    private String OrderDate;
    private String OrderPrice;
    private int totalItems;
    //TODO - add image

    public OrderListRowItem(UserOrder userOrder) {
        this.OrderDate = userOrder.getCreateDate();
        this.OrderPrice = String.valueOf(userOrder.getTotalPrice());
        this.totalItems = userOrder.getTotalItemsCount();

    }

    public OrderListRowItem(Object item) {
        this((UserOrder) item);
    }


    public String getDate() {
        return OrderDate;
    }

    public String getPrice() {
        return OrderPrice;
    }

    public int getTotalItemsCount() {
        return totalItems;
    }
}
