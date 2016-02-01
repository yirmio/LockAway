package com.yirmio.lockaway.DAL;

import android.util.Log;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.yirmio.lockaway.BL.RestaurantMenu;
import com.yirmio.lockaway.BL.RestaurantMenuObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yirmio on 19/07/2015.
 */
public final class ParseConnector {
    //DB Attributes
    private static final String PHOTO_FILE_ATTR = "PhotoFile";
    private static final String MENU_OBJECT_ATTRIBUTE = "MenuObjects";
    private static final String STORE_ID_ATTRIBUTE = "StoreID";


    private static ParseObject rest;
    private static String TAG = "In ParseConnector Class";
    private static boolean tmpResult = false;

    public static boolean removeObjectFromOrder(final String orderID, final String objectID) {
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
                                Log.i(TAG, "Object " + objectID + " Removed from order " + orderID);

                            } else {
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
                }
                else {
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
            /*query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    //No Error
                    if (e == null){
                        for (ParseObject obj:list) {
                            restaurantMenuResult.addItemToMenu(CreateMenuItemFromParseObject(obj));
                        }

                    }
                    else {
                        Log.d(TAG, "Error: " + e.getMessage());
                    }
                }
            });*/

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return restaurantMenuResult;
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
            String type;
            //TODO handle real type
            if (obj.getParseObject("Type") != null) {
                type = (obj.getParseObject("Type")).getString("TypeName");
            } else {
                type = "No Type";
            }
            boolean isReady = false;

            //TODO - handle more than one image per object
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
                }
            }
        });
        return false;
    }

    public void addItemToFavorite(String objectId) {
        //TODO implement
    }
}
