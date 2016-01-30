package com.yirmio.lockaway;

import android.app.Application;

import com.google.android.gms.maps.model.LatLng;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.yirmio.lockaway.BL.RestaurantMenu;
import com.yirmio.lockaway.BL.UserOrder;
import com.yirmio.lockaway.DAL.ParseConnector;

//import com.parse.ParseCrashReporting;

/**
 * Created by yirmio on 13/06/2015.
 */
public class LockAwayApplication extends Application {
    //32.0831538,34.9435619
    public static final ParseConnector parseConector = new ParseConnector();
    private static RestaurantMenu restaurantMenu;
    private static UserOrder userOrder;
    public  static LatLng AfeyaLatLang = new LatLng(32.0831538,34.9435619);
    private static ParseUser curParseUser;

    public static RestaurantMenu getRestaurantMenu() {
        return restaurantMenu;
    }

    public static void setRestaurantMenu(RestaurantMenu restMenu) {
        restaurantMenu = restMenu;
    }

    public static String GetOrderID() {
        String idToReturn = null;
        if (userOrder != null) {
            idToReturn = userOrder.getOrderId();
        } else {

            try {
                //ParseUser usr = ParseUser.logIn("Yirmi", "Z69Hus&&");
                ParseObject order = new ParseObject("UserToOrders");
                order.put("UserID", ParseUser.getCurrentUser().getObjectId());
                order.put("ResturantID","g1bzMQEXoj");
                order.put("OrderStatus","Active");

                order.save();
                userOrder = new UserOrder(order.getObjectId());

                idToReturn = userOrder.getOrderId();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return idToReturn;
    }

    public static UserOrder getUserOrder() {
        UserOrder order = null;
        if (userOrder == null){
            GetOrderID();
            return userOrder;
        }
        else {
            order = userOrder;
        }
        return order;
    }

    ;


    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Crash Reporting.
        //ParseCrashReporting.enable(this);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(this, "i6hUPoLJOlkH8j0p4nB3q1r2x18Kbr2SHlKocuua", "32VPtaO5T2Rg62uZWO3i9x4jr9mfxOKePlSL0rlW");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        this.curParseUser = ParseUser.getCurrentUser();

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
         defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }


}
