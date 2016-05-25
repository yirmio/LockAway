package com.yirmio.lockaway.UI.ListsItems;

import com.yirmio.lockaway.BL.UserOrder;

/**
 * Created by oppenhime on 23/02/2016.
 */
public class OrderListRowItem {
    private String OrderDate;
    private String OrderPrice;
    private int totalItems;
    private UserOrder userOrder;
    //TODO - add image

    public OrderListRowItem(UserOrder userOrder) {
        this.OrderDate = userOrder.getCreateDate();
        this.OrderPrice = String.valueOf(userOrder.getTotalPrice());
        this.totalItems = userOrder.getTotalItemsCount();
        this.userOrder = userOrder;

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

    public String[] getAllItemsNames() {

        String[] itemsToReturn = new String[totalItems];
        itemsToReturn = userOrder.getItemsNames();


        return itemsToReturn;
    }
}
