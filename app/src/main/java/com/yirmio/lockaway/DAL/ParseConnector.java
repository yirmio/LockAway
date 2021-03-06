package com.yirmio.lockaway.DAL;

import android.util.Log;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.yirmio.lockaway.BL.GlobalConsts;
import com.yirmio.lockaway.BL.MenuItemTypesEnum;
import com.yirmio.lockaway.BL.Order;
import com.yirmio.lockaway.BL.OrderStatusEnum;
import com.yirmio.lockaway.BL.RestaurantMenu;
import com.yirmio.lockaway.BL.RestaurantMenuObject;

import com.yirmio.lockaway.BL.Store;
import com.yirmio.lockaway.BL.UserOrder;
import com.yirmio.lockaway.Interfaces.Observer;
import com.yirmio.lockaway.Interfaces.Subject;
import com.yirmio.lockaway.LockAwayApplication;
import com.yirmio.lockaway.R;

import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yirmio on 19/07/2015.
 */
public final class ParseConnector implements Subject {
    //DB Attributes
    private static final String PHOTO_FILE_ATTR = "PhotoFile";
    private static final String MENU_OBJECT_ATTRIBUTE = "MenuObjects";
    private static final String STORE_ID_ATTRIBUTE = "StoreID";
    private static HashMap<String, MenuItemTypesEnum> objectsTypesToId = new HashMap<>();


    private static ParseObject rest;
    private static String TAG = "In ParseConnector Class";
    private static boolean tmpResult = false;
    private List<Observer> observers = new ArrayList<Observer>();


    public boolean initConnector(String storeId) {
        boolean res = true;
        initObjectTypes();

        return res;


    }

    //query all object types and init hash map
    public boolean initObjectTypes() {
        boolean res = true;
        String tmpStr;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(GlobalConsts.MenuObjectTypes);
        try {
            List<ParseObject> objects = query.find();
            if (objects.size() > 0) {
                for (ParseObject o : objects) {
                    tmpStr = o.getString(GlobalConsts.MenuObjectTypes_TypeName);
                    if (tmpStr != null) {
                        objectsTypesToId.put(o.getObjectId(), MenuItemTypesEnum.valueOf(tmpStr));
                    }
                }

            } else {
                res = false;
            }
        } catch (ParseException e) {
            res = false;
        }


        return res;
    }

    public boolean removeObjectFromOrder(final String orderID, final String objectID) {
        boolean res = true;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("OrderedObjects");
        query.whereEqualTo("MenuObjectID", objectID);
        try {
            String tmpID;
            List<ParseObject> objects = query.find();
            for (ParseObject p : objects) {
                tmpID = p.getString("OrderID");
                if (tmpID.equals(orderID)) {
                    p.deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            //Success
                            if (e == null) {
                                notifyObservers(R.string.item_removed);
                                Log.i(TAG, "Object " + objectID + " Removed from order " + orderID);

                            } else {
                                notifyObservers(R.string.errorRemovingItemFromOrder);
                                Log.e(TAG, "Error reoving object " + objectID + " From order " + orderID);
                                //throw new ParseException(9,"Error reoving object " + objectID + " From order " + orderID);                            }
                            }
                        }
                    });
                }
            }


        } catch (ParseException e) {
            res = false;
        }


        return res;

    }

    public static boolean setRestaurantID(String id) {
        tmpResult = false;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Stores");
        query.whereEqualTo("objectId", id);
        try {
            List<ParseObject> stores = query.find();
            rest = stores.get(0);
            Log.i(TAG, "Get restaurant: " + rest.getString("Name"));
            Log.i(TAG, "Get restaurant: " + rest.getString("Name"));
            tmpResult = true;
        } catch (ParseException e) {
            Log.e(TAG, "Error: " + e.getMessage());
            tmpResult = false;
        }
        /*query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> stores, ParseException e) {
                if (e == null) {
                    rest = stores.get(0);
                    Log.i(TAG, "Get restaurant: " + rest.getString("Name"));
                    tmpResult = true;
                } else {
                    Log.d(TAG, "Error: " + e.getMessage());
                    tmpResult = false;
                }
            }
        });*/


        return tmpResult;
    }

    public List<ParseFile> getImagesFilesForObject(String objID, final int maxImages) {
        int imagesToGet = 0;
        ArrayList<ParseFile> imagesArr = new ArrayList<ParseFile>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("MenuPhotos");
        query.whereEqualTo("MenuObjectID", objID);
        try {
            List<ParseObject> objs = query.find();
            if (objs.size() > 0) {
                if (maxImages == 0) {
                    imagesToGet = objs.size();
                } else {
                    imagesToGet = maxImages;
                }
                for (ParseObject obj : objs) {
                    //Up To maxImages files
                    if (imagesArr.size() < imagesToGet) {
                        imagesArr.add(getParseFileFromParseObject(obj, PHOTO_FILE_ATTR));
                    }
                }

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
       /* query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                //No Error
                if (e == null){
                    //Not empty list
                    if (list.size() > 0){
                        for (ParseObject obj :list) {
                            //Up To maxImages files
                            if (imagesArr.size() < maxImages){
                                imagesArr.add(getParseFileFromParseObject(obj,PHOTO_FILE_ATTR));
                            }
                        }

                    }
                }
            }
        });*/

        return imagesArr;

    }

    private static ParseFile getParseFileFromParseObject(ParseObject object, String attribute) {
        ParseFile res;
        try {
            res = object.getParseFile(attribute);
        } catch (Exception ex) {
            Log.e(TAG, "Cant get file for object: " + object.getObjectId());
            return null;
        }
        return res;
    }

    public RestaurantMenu getMenu() {
        final RestaurantMenu restaurantMenuResult = new RestaurantMenu();
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(MENU_OBJECT_ATTRIBUTE);
        query.whereEqualTo(STORE_ID_ATTRIBUTE, rest.getObjectId());
        try {
            List<ParseObject> list = query.find();
            if (list.size() > 0) {
                for (ParseObject obj : list) {
                    restaurantMenuResult.addItemToMenu(CreateMenuItemFromParseObject(obj));
                }
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return restaurantMenuResult;
    }

    //Get ObjectType From id
    public MenuItemTypesEnum getMenuObjectTyprByID(String id){
        MenuItemTypesEnum itemTypesEnum = objectsTypesToId.get(id);
        return itemTypesEnum;
    }

    //Create MenuItem From ParseObject
    private RestaurantMenuObject CreateMenuItemFromParseObject(ParseObject obj) {
        try {
            float price = obj.getNumber("Price").floatValue();
            String title = obj.getString("Name");
            int timeToMake = obj.getNumber("TimeToMake").intValue();
            String description = obj.getString("Description");
            boolean isVeg = obj.getBoolean("Veg");
            boolean isGlotenFree = obj.getBoolean("GlotenFree");
            String id = obj.getObjectId();
            MenuItemTypesEnum type = objectsTypesToId.get(obj.getString(GlobalConsts.MenuItemTypeId));

//            //TODO handle real type
//            if (obj.getParseObject("Type") != null) {
//                type = (obj.getParseObject("Type")).getString("TypeName");
//            } else {
//                type = "No Type";
//            }
            boolean isReady = false;

            //Get First Image for object

            List<ParseFile> tmpFilesArray = getImagesFilesForObject(obj.getObjectId(), 1);
            ParseFile tmpFile = null;
            if (tmpFilesArray != null) {
                if (tmpFilesArray.size() > 0) {
                    tmpFile = getImagesFilesForObject(obj.getObjectId(), 1).get(0);
                }
            }
            return new RestaurantMenuObject(id, description, price, title, timeToMake, tmpFile, type, isVeg, isGlotenFree);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }


    }

    public static boolean addObjectToOrder(String id, String orderID) {
        ParseObject tmpItem = new ParseObject("OrderedObjects");

        tmpItem.put("MenuObjectID", id);
        tmpItem.put("OrderID", orderID);
        tmpItem.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //TODO - Log...
                    ///TODO handle exception back
                }
            }
        });
        return false;
    }

    public int addItemToFavorite(String objectId) {
        final int[] res = {0};
        final String fObjectId = objectId;
        final boolean[] flagContinu = {true};
        ParseQuery query = ParseQuery.getQuery("FavoriteObject");
        query.whereEqualTo("UserId", ParseUser.getCurrentUser());
        query.whereEqualTo("MenuObjectId", fObjectId);
        try {
            query.findInBackground(new FindCallback<ParseObject>() {

                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        //Already in favorite
                        if (objects.size() > 0) {
                            res[0] = 2;
                            notifyObservers(R.string.itemalreadyfavorite);

                        } else {
                            ParseObject newFav = new ParseObject("FavoriteObject");
                            newFav.put("UserId", ParseUser.getCurrentUser());
                            newFav.put("MenuObjectId", fObjectId);
                            newFav.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e != null) {
                                        //TODO - Log...
                                        ///TODO handle exception back
                                        res[0] = 0;
                                        notifyObservers(R.string.erroraddingitemtofavorite);
                                    } else {
                                        notifyObservers(R.string.itemaddedtofavorite);
                                    }

                                }
                            });
                        }
                    } else {
                        notifyObservers(R.string.erroraddingitemtofavorite);
                    }
                }
            });
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
//        //Not Found
//        if (flagContinu[0]) {
//
//        }
        return res[0];

    }

    private void notifyObservers(int msgCode) {
        this.notifyObservers(LockAwayApplication.getAppResources().getString(msgCode));
    }


    @Override
    public void registerObserver(Observer o) {
        this.observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        this.observers.remove(o);

    }

    @Override
    public void notifyObservers() {

    }

    @Override
    public void notifyObservers(String msg) {
        for (Observer ob : this.observers) {
            ob.update(msg, ob);
        }

    }

    public Store getStore(String restID) {
        Store loadedStore = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Stores");
        query.whereEqualTo("objectId", restID);
        try {
            List<ParseObject> stores = query.find();
            rest = stores.get(0);
            loadedStore = new Store();
            loadedStore.setPhoneNumber(rest.getString("PhoneNumber"));
            loadedStore.setIsOpen(rest.getBoolean("isOpen"));
            ParseGeoPoint gp = rest.getParseGeoPoint(GlobalConsts.storeLocation);
            loadedStore.setLocation(gp.getLatitude() + "," + gp.getLongitude());

        } catch (ParseException e) {

        }


        return loadedStore;
    }

    public void setNewETAToOrder(String orderId, int hourOfDay, int minute) {


        ParseObject orderObject = this.getParseObjectOrderByID(orderId);
        LocalDateTime tmpTime;
        if (orderObject.getDate(GlobalConsts.orderETA) != null) {
            tmpTime = LocalDateTime.fromDateFields(orderObject.getDate(GlobalConsts.orderETA));
        } else {

            tmpTime = LocalDateTime.now();
        }

//        LocalTime tmpTime = new LocalTime(this.ETA);
        int plusH, plusM;

        plusH = hourOfDay - tmpTime.getHourOfDay();
        plusM = minute - tmpTime.getMinuteOfHour();
//        int newH = tmpTime.hourOfDay().addToCopy(plusH).geth
        LocalDateTime newTime = new LocalDateTime(tmpTime.getYear(), tmpTime.getMonthOfYear(), tmpTime.getDayOfMonth(), tmpTime.hourOfDay().addToCopy(plusH).getHourOfDay(), tmpTime.minuteOfHour().addToCopy(plusM).getMinuteOfHour());
//        LocalDateTime newTime = new LocalDateTime(tmpTime.plusHours(plusH).getHourOfDay(),tmpTime.plusMinutes(plusM).getMinuteOfHour());

        //Send To Cloud
        HashMap<String, Object> dict = new HashMap<String, Object>();
        dict.put("orderID", orderId);
        dict.put("userETA", newTime.toString());


        ParseCloud.callFunctionInBackground("updateTimeToArrive", dict, new FunctionCallback<Object>() {
            @Override
            public void done(Object o, ParseException e) {
                if (e != null) {
                    notifyObservers(GlobalConsts.OrderETAChangeError);
                } else {
                    notifyObservers(GlobalConsts.OrderETAChangedSuccessfully);
                }
            }
        });
//
//        orderObject.put(GlobalConsts.orderETA,newTime.toDate());
//        orderObject.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e==null){
//                    notifyObservers(GlobalConsts.OrderETAChangedSuccessfully);
//                }
//                else {
//                    notifyObservers(GlobalConsts.OrderETAChangeError);
//                }
//            }
//        });


    }

    private ParseObject getParseObjectOrderByID(String orderId) {
        ParseQuery query = new ParseQuery(GlobalConsts.UserToOrders);
        ParseObject res = null;
        try {
            res = query.get(orderId);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    public UserOrder getOrderByID(String orderId) {
        return buildOrderFromParseObject(getParseObjectOrderByID(orderId));
    }

    private UserOrder buildOrderFromParseObject(ParseObject orderObject) {
        UserOrder tmpOrder = new UserOrder(orderObject.getObjectId());
        //Add order items
        List<ParseObject> orderItems = this.getOrdersItems(orderObject.getObjectId());
        RestaurantMenuObject r;
        if (orderItems != null) {
            for (ParseObject p : orderItems) {
                r = new RestaurantMenuObject();
                r.setDescription(p.getString(GlobalConsts.Description));
                r.setId(p.getObjectId());
                r.setIsGlootenFree(p.getBoolean(GlobalConsts.isGlootenFree));
                r.setIsReady(false);
                r.setIsVeg(p.getBoolean(GlobalConsts.isVeg));
                r.setPic(null);
                r.setPrice(p.getNumber(GlobalConsts.price).floatValue());
                r.setTimeToMake(p.getInt(GlobalConsts.timeToMake));
                r.setTitle(p.getString(GlobalConsts.title));
                r.setType(getMenuObjectTyprByID(p.getString(GlobalConsts.type)));

                tmpOrder.addMenuItemToOrder(r);

            }
        }
        //Add more info
        tmpOrder.setOrderStatus(OrderStatusEnum.valueOf(orderObject.getString(GlobalConsts.UserOrder_orderStatus)));

        return tmpOrder;
    }

    private List<ParseObject> getOrdersItems(String orderID) {
        List<ParseObject> res = null;
        List<ParseObject> finalRes = new ArrayList<>();
        ParseQuery parseQuery = new ParseQuery(GlobalConsts.OrderedObjects);
        parseQuery.whereEqualTo(GlobalConsts.orderID, orderID);
        try {
            res = parseQuery.find();
            for (ParseObject p : res) {
                finalRes.add(getMenuObjectByID(p.getString(GlobalConsts.MenuObjectID)));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return finalRes;
    }

    private ParseObject getMenuObjectByID(String id) {
        ParseObject object = null;
        ParseQuery query = new ParseQuery(GlobalConsts.MenuObject);
        try {
            object = query.get(id);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return object;
    }

    public ArrayList<UserOrder> getAllUserOrders(String objectId) {
        ArrayList arr = new ArrayList();
        List<ParseObject> res;
        ParseQuery parseQuery = new ParseQuery(GlobalConsts.UserToOrders);
        parseQuery.whereEqualTo(GlobalConsts.UserToOrders_UserID, objectId);
        UserOrder tmpOrder;
        try {
            res = parseQuery.find();
            //TODO do it better
            for (ParseObject o : res) {
                tmpOrder = new UserOrder(o.getObjectId());
                tmpOrder.setCreateDate(o.getCreatedAt().toString());
                tmpOrder.setETA(o.getString(GlobalConsts.UserOrder_UserETA));
                for (ParseObject pObj:getOrdersItems(o.getObjectId())) {
                    tmpOrder.addItemToOrder(CreateMenuItemFromParseObject(pObj),true);
                }

                //TODO - complete order build
                //tmpOrder.setTotalPrice();
                arr.add(tmpOrder);
            }

        } catch (ParseException e) {
            //TODO handle exception
        }
        return arr;
    }

    public void cancelOrder(String orderId) {
        ParseObject orderToCancel = getParseObjectOrderByID(orderId);
        orderToCancel.put(GlobalConsts.UserOrder_orderStatus,OrderStatusEnum.Canceled.toString());
        orderToCancel.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    notifyObservers(R.string.msg_OrderCanceledSuccess);
                }
                else {
                    notifyObservers(R.string.msg_CantCancelOrder);

                }
            }
        });


    }

//    public static void setInstallationCurrentUserId(String phone) {
//        ParseInstallation tmpInstallation = ParseInstallation.getCurrentInstallation();
//        tmpInstallation.put("userIdAsString",phone);
//        tmpInstallation.saveInBackground();
//
//    }

    public static void setInstallationCurrentUserId() {
        ParseInstallation tmpInstallation = ParseInstallation.getCurrentInstallation();
        tmpInstallation.put("userIdAsString",ParseUser.getCurrentUser().getObjectId());
        tmpInstallation.saveInBackground();

    }
}
