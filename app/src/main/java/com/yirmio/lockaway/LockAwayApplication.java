package com.yirmio.lockaway;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
//import com.parse.ParseCrashReporting;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.yirmio.lockaway.BL.Order;
import com.yirmio.lockaway.BL.RestaurantMenu;
import com.yirmio.lockaway.BL.UserOrder;
import com.yirmio.lockaway.DAL.ParseConnector;

/**
 * Created by yirmio on 13/06/2015.
 */
public class LockAwayApplication extends Application {
    public static final ParseConnector parseConector = new ParseConnector();
    private static RestaurantMenu restaurantMenu;
    private static UserOrder userOrder;

    public static RestaurantMenu getRestaurantMenu() {
        return restaurantMenu;
    }

    public static void setRestaurantMenu(RestaurantMenu restMenu) {
        restaurantMenu = restMenu;
    }

    public static String GetOrderID() {
        if (userOrder != null) {
            return userOrder.getOrderId();
        } else {

            try {
                ParseObject order = new ParseObject("UserToOrders");
                order.put("UserID", ParseUser.getCurrentUser().getObjectId());
                order.save();
                userOrder = new UserOrder(order.getObjectId());

                return userOrder.getOrderId();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static UserOrder getUserOrder() {
        return userOrder;
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

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }


}
