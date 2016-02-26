package com.yirmio.lockaway.UI.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yirmio.lockaway.R;
import com.yirmio.lockaway.UI.OrderListRowItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oppenhime on 23/02/2016.
 */
public class OrdersListAdapter extends ArrayAdapter{
    private final Context context;
    private final ArrayList userOrders;

    public OrdersListAdapter(Context context, int resource, ArrayList objects) {
        super(context, resource, objects);
        this.context = context;
        this.userOrders = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        OrderListRowItem item = (OrderListRowItem)getItem(position);
        View viewToUse = null;
        final int pos = position;
        LayoutInflater mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView ==  null){
            viewToUse = mLayoutInflater.inflate(R.layout.order_list_item_layout,null);
            holder = new ViewHolder();
            holder.openOrderBtn = (ImageButton)viewToUse.findViewById(R.id.order_list_item_btn);
            holder.openOrderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO - implement
                }
            });
            holder.orderDate = (TextView)viewToUse.findViewById(R.id.order_date_txt_view);
            holder.orderItemsCount = (TextView)viewToUse.findViewById(R.id.order_list_item_total_items_textView);
            holder.orderPrice = (TextView)viewToUse.findViewById(R.id.order_price_txt_view);
            viewToUse.setTag(holder);
        }
        else {
            viewToUse = convertView;
            holder = (ViewHolder) viewToUse.getTag();
        }

        //insert data to view objects
        holder.orderDate.setText(item.getDate());
        holder.orderPrice.setText(item.getPrice());
        holder.orderItemsCount.setText(String.valueOf(item.getTotalItemsCount()));
        return viewToUse;
    }

    @Override
    public int getCount() {
        return this.userOrders.size();
    }



    private class ViewHolder {
        TextView orderDate;
        TextView orderPrice;
        TextView orderItemsCount;
        ImageButton openOrderBtn;
    }
}
