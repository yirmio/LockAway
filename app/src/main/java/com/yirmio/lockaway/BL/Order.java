package com.yirmio.lockaway.BL;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.Time;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by yirmio on 2/10/2015.
 */
public class Order implements Parcelable {


    private String clientName;
    private float totalPrice;
    private List<RestaurantMenuObject> itemsByOrderToMake;
    private Time timeToBeReady;
    private float timeToMakeAllOrder;

    public OrderStatusEnum getOrderStatusEnum() {
        return orderStatusEnum;
    }

    public void setOrderStatusEnum(OrderStatusEnum orderStatusEnum) {
        this.orderStatusEnum = orderStatusEnum;
    }

    private OrderStatusEnum orderStatusEnum;


    public Order(String clientName, Time timeToBeReady) {
        this.clientName = clientName;
        this.timeToBeReady = timeToBeReady;
        this.totalPrice = 0;
        this.itemsByOrderToMake = new ArrayList<RestaurantMenuObject>();
        this.timeToMakeAllOrder = 0;

    }

    /**
     * Add MazeMenu Item to the order and keep the items list sorted by timeToMake.
     * Also Updtae the order's total price And timeToMakeAllOrder
     * @param mnuItm
     * @return True if succeeded
     */
    public boolean addMenuItemToOrder(RestaurantMenuObject mnuItm){
        try {
            this.itemsByOrderToMake.add(mnuItm);
            Collections.sort(this.itemsByOrderToMake);
            this.totalPrice += mnuItm.getPrice();
            this.timeToMakeAllOrder += mnuItm.getTimeToMake();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Delete item from menu and keep the list sorted
     * Also update total time and price of order
     * @param mnuItm
     * @return True if succeeded
     */
    public boolean dellItemFromOrder(RestaurantMenuObject mnuItm){
        try {
            //this.itemsByOrderToMake.remove(mnuItm);
            for (int i=0;i < this.itemsByOrderToMake.size();i++){
                if (this.itemsByOrderToMake.get(i).getTitle().equalsIgnoreCase(mnuItm.getTitle())){
                    this.itemsByOrderToMake.remove(i);
                    this.totalPrice -= mnuItm.getPrice();
                    this.timeToMakeAllOrder -= mnuItm.getTimeToMake();
                    return true;
                }
            }

        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public String getClientName() {
        return clientName;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public List<RestaurantMenuObject> getItemsByOrderToMake() {
        return itemsByOrderToMake;
    }

    public Time getTimeToBeReady() {
        return timeToBeReady;
    }

    public float getTimeToMakeAllOrder() {
        return timeToMakeAllOrder;
    }
    public void clearOrder(){
        this.itemsByOrderToMake.clear();
        this.totalPrice = 0;
        this.timeToMakeAllOrder = 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
