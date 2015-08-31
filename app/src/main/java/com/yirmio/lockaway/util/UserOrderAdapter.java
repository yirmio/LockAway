package com.yirmio.lockaway.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.yirmio.lockaway.BL.RestaurantMenuObject;
import com.yirmio.lockaway.R;
import com.yirmio.lockaway.UI.UserOrderRowLayout;

import java.util.ArrayList;

/**
 * Created by yirmio on 27/07/2015.
 */
public class UserOrderAdapter extends ArrayAdapter {
    //region Properties
    private Context context;
    private boolean useList = true;
    private ArrayList userOrder;


    //endregion
    //region Ctor

    /**
     * Constructor
     *
     * @param context The current context.
     * @param objects The objects to represent in the ListView.
     */
    public UserOrderAdapter(Context context, int resource, ArrayList objects) {
        super(context, resource, objects);
        this.context = context;
        this.userOrder = objects;

    }

    /**
     * {@inheritDoc}
     *
     * @param position
     * @param convertView
     * @param parent
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        UserOrderRowLayout item = new UserOrderRowLayout((RestaurantMenuObject) getItem(position));
        View viewToUse = null;
        final int pos = position;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            if (useList) {
                viewToUse = mInflater.inflate(R.layout.user_order_item_layout, null);
            } else {
                viewToUse = mInflater.inflate(R.layout.user_order_item_layout, null);
            }
            //Create holder and bind to view objects
            holder = new ViewHolder();
            holder.btnX = (Button) viewToUse.findViewById(R.id.user_order_btn_X);
            holder.lable = (TextView) viewToUse.findViewById(R.id.user_order_textView_name);
            holder.price = (TextView) viewToUse.findViewById(R.id.user_order_textViewPrice);
            holder.timeToMake = (TextView) viewToUse.findViewById(R.id.user_order_textView_time_toMake);
            holder.photo = (ParseImageView) viewToUse.findViewById(R.id.user_order_imageView_photo);

            viewToUse.setTag(holder);
        } else {
            viewToUse = convertView;
            holder = (ViewHolder) viewToUse.getTag();
        }
        //insert data to view objects

        holder.lable.setText(item.getLable());
        holder.price.setText(item.getPrice().toString());

        holder.timeToMake.setText(String.valueOf(item.getTimeToMake()));
        loadParseImageFromParseFile(viewToUse, holder.photo, item.getPhotoParseFile());

        ((ListView) (parent)).invalidate();

        return viewToUse;
    }
    //endregion

    private void loadParseImageFromParseFile(View viewToUse, ParseImageView photo, ParseFile photoParseFile) {
        try {
            photo.setParseFile(photoParseFile);
            photo.loadInBackground(new GetDataCallback() {
                                       @Override
                                       public void done(byte[] data, ParseException e) {
                                           if (e == null && data != null && data.length > 0) {
                                               Log.i("ParseImageView",
                                                       "Fetched! Data length: " + data.length);
                                           } else if (e != null) {
                                               Log.e("ParseImageView", e.getMessage());
                                           }
                                       }
                                   }
            );
        } catch (Exception e) {
            Log.e("loadParseImageFromParse", e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount() {
        return this.userOrder.size();
    }

    /**
     * {@inheritDoc}
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    //endregion
    //region ViewHolder Class
    private class ViewHolder {

        TextView lable;
        TextView price;
        TextView timeToMake;
        ParseImageView photo;
        Button btnX;
    }
}
