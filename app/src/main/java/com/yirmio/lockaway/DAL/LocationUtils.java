package com.yirmio.lockaway.DAL;

import android.location.Address;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.yirmio.lockaway.UI.SendOrderActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by oppenhime on 30/11/2015.
 */
public class LocationUtils {
public  static Map<String,String> getETAAndDistanceInfo(LatLng origin,LatLng dest){
    String getUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" +origin.latitude +"," + origin.longitude + "&destinations=" +dest.latitude +"," + dest.longitude + "&key=AIzaSyBvDtWX7IoDjmC5W7BGcEQYqWxRhZNqlk4";

    URL url = null;

    HttpURLConnection urlConnection = null;
    BufferedReader br = null;

    StringBuilder stringBuilder = null;
    int statusCode = 0;

    try {
        url = new URL(getUrl);

        urlConnection = (HttpURLConnection) url.openConnection();
        // set the connection timeout to 10 seconds and the read timeout to 120 seconds
        urlConnection.setConnectTimeout(10 * 1000);
        urlConnection.setReadTimeout(120 * 1000);
        urlConnection.connect();

        InputStream stream = new BufferedInputStream(urlConnection.getInputStream());

        statusCode = urlConnection.getResponseCode();

        InputStreamReader streamReader = new InputStreamReader(stream);
        br = new BufferedReader(streamReader);

        String line = "";
        stringBuilder = new StringBuilder();

        while ((line = br.readLine()) != null) {
            stringBuilder.append(line);
        }
    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException ex) {
        ex.printStackTrace();
    } finally {
        //close buffered reader and under lying stream
        if (br != null) {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (urlConnection != null) {
            urlConnection.disconnect();
        }
    }

    //create json object from result string and then parse it
    if (statusCode == HttpURLConnection.HTTP_OK) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.e("MyGeocoder", "JSONException e = " + e.getMessage());
            e.printStackTrace();
        }

        return parseJSONETAAndDistanceInfo(jsonObject);
    } else {
        return null;
    }
}

    private static Map<String, String> parseJSONETAAndDistanceInfo(JSONObject jsonObject) {
        Address address = null;

        Map<String, String> mapResult = new HashMap<String, String>();

        if (jsonObject != null) {
            try {
                //get and iterate through the rows array
                JSONArray results = jsonObject.getJSONArray("rows");
                JSONObject firstElement = results.getJSONObject(0).getJSONArray("elements").getJSONObject(0);
                JSONObject dur = firstElement.getJSONObject("duration");
                JSONObject dis = firstElement.getJSONObject("distance");
                mapResult.put("totalTime",dur.getString("text"));
                mapResult.put("totalTimeInSec",dur.getString("value"));
                mapResult.put("totalDistanceInMeters",dis.getString("value"));
                mapResult.put("totalDistance",dis.getString("text"));
                return mapResult;


            } catch (JSONException e) {
                Log.e("MyGeocoder", "parseJSONAddressList JSONException e = " + e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }

    public static List<String> getAddressByLocation(double latitude, double longitude) {

        String coords = (float) latitude + "," + (float) longitude;
        String getUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + coords + "&language=" + Locale.getDefault();
        URL url = null;

        HttpURLConnection urlConnection = null;
        BufferedReader br = null;

        StringBuilder stringBuilder = null;
        int statusCode = 0;

        try {
            url = new URL(getUrl);

            urlConnection = (HttpURLConnection) url.openConnection();
            // set the connection timeout to 10 seconds and the read timeout to 120 seconds
            urlConnection.setConnectTimeout(10 * 1000);
            urlConnection.setReadTimeout(120 * 1000);
            urlConnection.connect();

            InputStream stream = new BufferedInputStream(urlConnection.getInputStream());

            statusCode = urlConnection.getResponseCode();

            InputStreamReader streamReader = new InputStreamReader(stream);
            br = new BufferedReader(streamReader);

            String line = "";
            stringBuilder = new StringBuilder();

            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            //close buffered reader and under lying stream
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        Log.d("MyGeocoder", "statusCode = " + statusCode + " HTTPStatus.SC_OK = " + HttpURLConnection.HTTP_OK);

        //create json object from result string and then parse it
        if (statusCode == HttpURLConnection.HTTP_OK) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(stringBuilder.toString());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                Log.e("MyGeocoder", "JSONException e = " + e.getMessage());
                e.printStackTrace();
            }

            return parseJSONAddressList(jsonObject);
        } else {
            return null;
        }
    }


    //Method will parse JSONObject and return a List<Address> containing one Address.
//The Address will only contain data for county, state and country.
    public static List<String> parseJSONAddressList(JSONObject jsonObject) {

        Log.d("MyGeocoder", "In parseJSONAddressList");

        Address address = null;

        List<String> result = new ArrayList<String>();

        if (jsonObject != null) {
            try {
                //get and iterate through the results array
                JSONArray results = jsonObject.getJSONArray("results");
                for (int i = 0; i < results.length(); ++i) {
                    String addressData;

                    address = new Address(Locale.getDefault());
                    JSONObject resultItems = results.getJSONObject(i);
                    //get the addressComponents array
                    if (resultItems != null) {
                        addressData = resultItems.getString("formatted_address");
                        result.add(addressData);

                    }

                }



                return result;


            } catch (JSONException e) {
                Log.e("MyGeocoder", "parseJSONAddressList JSONException e = " + e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }


}
