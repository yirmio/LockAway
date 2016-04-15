package com.yirmio.lockaway.BL;

import com.yirmio.lockaway.R;

import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;

/**
 * Created by yirmio on 26/07/2015.
 */
public class UserOrder {
    //region Properties
    private String orderId;
    private List<RestaurantMenuObject> objects;
    private int totalPrice;
    private int totalTimeToMake;
    private String ETA;

    public OrderStatusEnum getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatusEnum orderStatus) {
        this.orderStatus = orderStatus;
    }
    public void setOrderStatus(String str) {
        this.orderStatus = OrderStatusEnum.valueOf(str);
    }
    private OrderStatusEnum orderStatus;



    private String createDate;


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

    public RestaurantMenuObject getItemByID(String id) {
        //TODO - do it better
        for (RestaurantMenuObject item :
                this.objects) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public int getItemPositionByID(String id) {
        for (int i = 0; i <= this.objects.size(); i++) {
            if (this.objects.get(i).getId() == id) {
                return i;
            }

        }
        return 99999;

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

    public void removeItemFromOrder(String id) {
        RestaurantMenuObject tmpObj = this.getItemByID(id);
        this.totalPrice -= tmpObj.getPrice();
        this.totalTimeToMake -= tmpObj.getTimeToMake();
        this.objects.remove(tmpObj);
    }

    public String getETA() {
        return ETA;
    }

    public void setETA(String ETA) {
//        this.ETA = ETA;

        LocalTime tmpTime = new LocalTime(ETA);
        this.ETA = tmpTime.toString(DateTimeFormat.forPattern("HH:mm"));

    }

    public void setNewETA(int hourOfDay, int minute) {
        LocalTime tmpTime = new LocalTime(this.ETA);
        int plusH, plusM;

        plusH = hourOfDay - tmpTime.getHourOfDay();
        plusM = minute - tmpTime.getMinuteOfHour();
        LocalTime newTime = new LocalTime(tmpTime.plusHours(plusH).getHourOfDay(), tmpTime.plusMinutes(plusM).getMinuteOfHour());


        this.ETA = newTime.toString(DateTimeFormat.forPattern("HH:mm"));


    }

    /**
     * Add MenuItemToOrder - Will also update time and price of total items in order
     * @param mnuItm - the item to add
     */
    public void addMenuItemToOrder(RestaurantMenuObject mnuItm) {
        try {
            this.objects.add(mnuItm);
            Collections.sort(this.objects);
            this.totalPrice += mnuItm.getPrice();
            this.totalTimeToMake += mnuItm.getTimeToMake();
        } catch (Exception e) {
            //TODO handle
        }
    }

    public String getCreateDate() {
        return createDate;
    }
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public int getTotalItemsCount() {
        return this.objects.size();
    }




    //endregion
}
