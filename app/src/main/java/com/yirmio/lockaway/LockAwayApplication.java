package com.yirmio.lockaway;

import android.app.Application;
import android.content.res.Resources;

import com.google.android.gms.maps.model.LatLng;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.yirmio.lockaway.BL.GlobalConsts;
import com.yirmio.lockaway.BL.RestaurantMenu;
import com.yirmio.lockaway.BL.Store;
import com.yirmio.lockaway.BL.UserOrder;
import com.yirmio.lockaway.DAL.ParseConnector;

import java.util.ArrayList;
import java.util.List;

//import com.parse.ParseCrashReporting;

/**
 * Created by yirmio on 13/06/2015.
 */
public class LockAwayApplication extends Application {
    //32.0831538,34.9435619
    public static final ParseConnector parseConector = new ParseConnector();
    private static RestaurantMenu restaurantMenu;
    private static UserOrder userOrder;
    public static LatLng AfeyaLatLang = new LatLng(32.0831538, 34.9435619);
    private static ParseUser curParseUser;
    private static Resources appResources;
    private static Store curStore;


    public static RestaurantMenu getRestaurantMenu() {
        return restaurantMenu;
    }

    public static void setRestaurantMenu(RestaurantMenu restMenu) {
        restaurantMenu = restMenu;
    }

    public static Store getStore() {
        return curStore;
    }

    ;

    public static String GetOrderID() {
        String idToReturn = null;
        if (userOrder != null) {
            idToReturn = userOrder.getOrderId();
        } else {

            try {
                //ParseUser usr = ParseUser.logIn("Yirmi", "Z69Hus&&");
                ParseObject order = new ParseObject("UserToOrders");
                order.put("UserID", ParseUser.getCurrentUser().getObjectId());
                order.put("ResturantID", "g1bzMQEXoj");
                order.put("OrderStatusActivity", "Active");

                order.save();
                userOrder = new UserOrder(order.getObjectId());
                userOrder.setCreateDate(order.getCreatedAt().toString());

                idToReturn = userOrder.getOrderId();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return idToReturn;
    }

    public static UserOrder getUserOrder() {
        UserOrder order = null;
        if (userOrder == null) {
            GetOrderID();
            return userOrder;
        } else {
            order = userOrder;
        }
        return order;
    }

    ;


    @Override
    public void onCreate() {
        super.onCreate();
        appResources = this.getResources();
        // Initialize Crash Reporting.
        //ParseCrashReporting.enable(this);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(this, "i6hUPoLJOlkH8j0p4nB3q1r2x18Kbr2SHlKocuua", "32VPtaO5T2Rg62uZWO3i9x4jr9mfxOKePlSL0rlW");
        this.curParseUser = ParseUser.getCurrentUser();
        ParseInstallation currentInstall = ParseInstallation.getCurrentInstallation();
        if (this.curParseUser != null) {

//            currentInstall.put("userID",ParseUser.getCurrentUser());
            currentInstall.put("User",this.curParseUser);
        }
        currentInstall.saveInBackground();


        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setRoleWriteAccess("AfeyaAdmins",true);
        // Optionally enable public read access.
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }

    public static Resources getAppResources() {
        return appResources;
    }


    public void setStore(Store store) {
        this.curStore = store;
    }

    public static ArrayList getAllUserOrders() {
        ArrayList<UserOrder> allUserOrders = new ArrayList<>();
        for (UserOrder o:parseConector.getAllUserOrders(ParseUser.getCurrentUser().getObjectId())) {
            allUserOrders.add(o);
        }
        return allUserOrders;
    }
}
