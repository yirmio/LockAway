package com.yirmio.lockaway.BL;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yirmio on 26/07/2015.
 */
public class UserOrder {
    //region Properties
    private String orderId;
    private List<RestaurantMenuObject> objects;
    private int totalPrice;
    private int totalTimeToMake;

    //endregion


    //region Ctor
    public UserOrder(String orderId) {
        this.orderId = orderId;
        this.totalPrice = 0;
        this.totalTimeToMake = 0;
        this.objects = new ArrayList<>();
    }
    //endregion

    //region Getters & Setters
    public String getOrderId() {
        return this.orderId;
    }

    ;

    public void setOrderId(String id) {
        this.orderId = id;
    }

    public List<RestaurantMenuObject> getObjects() {
        return objects;
    }

    public void setObjects(ArrayList<RestaurantMenuObject> objects) {
        this.objects = objects;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalTimeToMake() {
        return totalTimeToMake;
    }

    public void setTotalTimeToMake(int totalTimeToMake) {
        this.totalTimeToMake = totalTimeToMake;
    }

    //endregion

    //region Core Methods
    //addItemToOrder
    // boolean forceAdd = ignore if exist
    //return 0 = ok
    //return 1 = already inside
    public int addItemToOrder(RestaurantMenuObject item, boolean forceAdd) {
        boolean exist = false;
        if (forceAdd == true) {
            this.addItem(item);
            return 0;
        } else {
            //check if exist
            for (RestaurantMenuObject obj : objects) {
                if (obj.getId() == item.getId()) {
                    return 1;
                }
            }
            //not exist so add
            this.addItem(item);
            return 0;

        }

    }

    private void addItem(RestaurantMenuObject item) {
        this.objects.add(item);
        this.totalPrice += item.getPrice();
        this.totalTimeToMake += item.getTimeToMake();
    }


    //endregion
}
