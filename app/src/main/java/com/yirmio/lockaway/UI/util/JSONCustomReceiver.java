package com.yirmio.lockaway.UI.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.parse.ParsePushBroadcastReceiver;
import com.yirmio.lockaway.LockAwayApplication;
import com.yirmio.lockaway.R;
import com.yirmio.lockaway.UI.Activities.MainActivity;
import com.yirmio.lockaway.UI.Activities.OrderStatusActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by oppenhime on 06/03/2016.
 */
public class JSONCustomReceiver extends ParsePushBroadcastReceiver {
    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);
        HashMap<String,String> map = this.getDataFromJson(intent.getExtras().getString("com.parse.Data"));
        Intent newIntent = new Intent(context, OrderStatusActivity.class);
        newIntent.putExtra("fromNotification", true);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        LockAwayApplication.setUserOrder(map.get("orderid"));
        //TODO - update the BL order

        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,newIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.afeyalogo)
                .setContentText(map.get("orderid"))
                .setContentTitle(map.get("message"))
                .setContentIntent(pendingIntent).setAutoCancel(true);

        Notification notification = builder.build();
        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        nm.notify(1410,notification);
    }
    private HashMap<String, String> getDataFromJson(String jsonData){
        HashMap<String,String>map = new HashMap<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            map.put("orderid",jsonObject.getString("orderid"));
            map.put("message",jsonObject.getString("message"));

            return map;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
