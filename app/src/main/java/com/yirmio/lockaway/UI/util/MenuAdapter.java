package com.yirmio.lockaway.UI.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.yirmio.lockaway.R;
import com.yirmio.lockaway.UI.MenuListRowLayoutItem;
import com.yirmio.lockaway.UI.menuListFragment;

import java.util.List;

/**
 * Created by yirmio on 23/07/2015.
 */
public class MenuAdapter extends ArrayAdapter {
    //region Properties
    private Context context;
    private boolean useList = true;
    private menuListFragment fragment;
    //endregion

    //region Ctor
    public MenuAdapter(Context context, List objects, menuListFragment menuListFragment) {
        super(context, R.layout.menu_item_row_layout, objects);
        this.context = context;
        this.fragment = menuListFragment;
    }
    //endregion

    //region ViewHolder Class
    private class ViewHolder {
        TextView info;
        TextView lable;
        TextView price;
        TextView timeToMake;
        ParseImageView photo;
        ImageView isVeg;
        ImageView isGlotenFree;
        Button btnPlus;
        String id;
    }
    //endregion

    //region Overrides

    /**
     * {@inheritDoc}
     *
     * @param position
     * @param convertView
     * @param parent
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final MenuListRowLayoutItem item = (MenuListRowLayoutItem) getItem(position);
        View viewToUse = null;
        final int pos = position;

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        //First time view

        if (convertView == null) {
            if (useList) {
                viewToUse = mInflater.inflate(R.layout.menu_item_row_layout, null);
            } else {
                viewToUse = mInflater.inflate(R.layout.menu_item_row_layout, null);
            }

            holder = new ViewHolder();

            setViewItems(holder, viewToUse, parent);    //Connect UI to holder properties
            final ViewHolder finalHolder = holder;
            holder.btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.onPlusButtonClicked(finalHolder.id);
                }
            });
            holder.photo.setOnClickListener(new View.OnClickListener() {
                int tmpPos = position;
                @Override
                public void onClick(View view) {
                    fragment.onObjectPhotoClicked(finalHolder.id);
                }
            });

            viewToUse.setTag(holder);
        }
        //Not first time

        else {
            viewToUse = convertView;
            holder = (ViewHolder) viewToUse.getTag();
        }

        loadParseImageFromParseFile(viewToUse, holder.photo, item.getPhotoParseFile());
        putDataInViewHolder(holder, item);
        return viewToUse;

    }

    private void putDataInViewHolder(ViewHolder holder, MenuListRowLayoutItem item) {
        //holder.info.setText(item.getInfo());
        holder.lable.setText(item.getLable());
        holder.price.setText(item.getPrice().toString());
        holder.timeToMake.setText(String.valueOf(item.getTimeToMake()));
        if (item.isVeg() == true) {
            holder.isVeg.setImageResource(R.drawable.vegan_icon);
        }
        if (item.isGlotenFree() == true) {
            holder.isGlotenFree.setImageResource(R.drawable.glooten_free);
        }
        //if (holder.photo == null
        holder.id = item.getId();


    }


    //endregion

    //region Help Methods
    //Get ViewHolder and View To Use and set the UI elements
    private void setViewItems(ViewHolder holder, View viewToUse, final ViewGroup parent) {
        holder.info = (TextView) viewToUse.findViewById(R.id.menu_item_description_lable);
        holder.info.setVisibility(View.GONE);
        holder.lable = (TextView) viewToUse.findViewById(R.id.menu_item_label);
        holder.price = (TextView) viewToUse.findViewById(R.id.menu_item_price_value);
        holder.timeToMake = (TextView) viewToUse.findViewById(R.id.menu_item_time_value);
        holder.isVeg = (ImageView) viewToUse.findViewById(R.id.menu_item_veg_icon);
        holder.isGlotenFree = (ImageView) viewToUse.findViewById(R.id.menu_item_gloten_free_icon);
        holder.photo = (ParseImageView) viewToUse.findViewById(R.id.menu_item_icon);
        holder.btnPlus = (Button) viewToUse.findViewById(R.id.menu_item_plus_btn);


    }

    private void loadParseImageFromParseFile(View viewToUse, ParseImageView photo, ParseFile photoParseFile) {
//photo.setPlaceholder(viewToUse.getResources().getDrawable(R.drawable.placeholder));
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
    //endregion
}
