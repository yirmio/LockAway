package com.yirmio.lockaway.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yirmio.lockaway.BL.RestaurantMenuObject;
import com.yirmio.lockaway.BL.UserOrder;
import com.yirmio.lockaway.LockAwayApplication;
import com.yirmio.lockaway.R;
import com.yirmio.lockaway.UI.util.OrderBuilderAdapter;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrderBuilderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrderBuilderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderBuilderFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ArrayList<OrderBuilderRowLayout> orderList;
    private UserOrder userOrder;
    private LockAwayApplication app;
    private OrderBuilderAdapter mAdapter;
    private AbsListView mListView;
    private TextView mTotalPriceTextView;
    private TextView mTotalTimeTextView;
    private Button mSendBtn;
    private Button mClearAllBtn;

    //region Ctor

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderBuilderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderBuilderFragment newInstance(String param1, String param2) {
        OrderBuilderFragment fragment = new OrderBuilderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    //endregion

    //region Getters & Setters
    public OrderBuilderAdapter getmAdapter() {
        return mAdapter;
    }


    public void setmAdapter(OrderBuilderAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    //endregion
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = ((LockAwayApplication) this.getActivity().getApplicationContext());
        orderList = new ArrayList();
        if (app.getUserOrder() != null) {
            this.userOrder = app.getUserOrder();
            for (RestaurantMenuObject obj : app.getUserOrder().getObjects()) {
                orderList.add(new OrderBuilderRowLayout(obj));
            }
        }
        mAdapter = new OrderBuilderAdapter(getActivity(), R.layout.order_builder_item_layout, orderList, this);

        //mListView = (AbsListView)getView().findViewById(R.id.user_order_listView);
        //((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_builder, container, false);


        mListView = (ListView) view.findViewById(R.id.user_order_listView);
        mListView.setAdapter(mAdapter);


        //Update Bottom Details
        mTotalPriceTextView = (TextView) view.findViewById(R.id.usrOrderFrgmntTxtViewTotalPriceValue);
        mTotalTimeTextView = (TextView) view.findViewById(R.id.usrOrderFrgmntTxtViewTotalTimeValue);
        mSendBtn = (Button) view.findViewById(R.id.frgmnt_user_order_btn_continu);
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCurrentOrder();
            }
        });
        mClearAllBtn = (Button) view.findViewById(R.id.frgmnt_user_order_btn_clear);
        mClearAllBtn.setOnClickListener(this);
        updateDetails();
        return view;
    }


    public void updateDetails() {
        if (app.getUserOrder() != null) {
            mTotalPriceTextView.setText(String.valueOf(app.getUserOrder().getTotalPrice()));
            mTotalTimeTextView.setText(String.valueOf(app.getUserOrder().getTotalTimeToMake()));
        }
    }

    private void sendCurrentOrder() {
        //Open sendOrderActivity
        Intent intent = new Intent(this.getActivity(), SendOrderActivity.class);

        intent.putExtra("totalPrice", this.mTotalPriceTextView.getText().toString());
        intent.putExtra("totalTimeToMake", this.mTotalTimeTextView.getText());
        intent.putExtra("itemsCount", String.valueOf(this.orderList.size()));
        intent.putExtra("OrderID", userOrder.getOrderId());
        startActivity(intent);

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link Activity#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();

        if (app.getUserOrder() != null) {
            if (app.getUserOrder().getObjects() != null) {
//                for (OrderBuilderRowLayout ob : orderList) {
//                    app.getUserOrder().removeItemFromOrder(ob.getId());
//                }
                orderList.clear();
                if (app.getUserOrder().getObjects().size() > 0) {
                    for (RestaurantMenuObject o : app.getUserOrder().getObjects()) {
                        orderList.add(new OrderBuilderRowLayout(o));
                    }
                }
//                orderList.addAll(app.getUserOrder().getObjects());
                mAdapter.notifyDataSetChanged();
            }
        }
        mListView.invalidate();
    }

    public void onXButtonClicked(int pos) {

        RestaurantMenuObject item = new RestaurantMenuObject(orderList.get(pos));
        MenuListRowLayoutItem tmpItem = new MenuListRowLayoutItem(item);
        mListener.onFragmentInteraction(item.getId(), app.GetOrderID(), "remove");//BL
        mListener.onFragmentInteraction(tmpItem, "remove");//UI


        updateDetails();

        Toast.makeText(getActivity(), getString(R.string.item_removed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.frgmnt_user_order_btn_clear:
                clearAllItems();
                break;
        }
    }

    private void clearAllItems() {
        if (app.getUserOrder() != null) {
            if (app.getUserOrder().getObjects() != null) {
                for (OrderBuilderRowLayout ob : orderList) {
                    app.getUserOrder().removeItemFromOrder(ob.getId());
                }
                orderList.clear();
                mAdapter.notifyDataSetChanged();
                updateDetails();
            }
        }
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String itemID, String orderID, String opp);

        public void onFragmentInteraction(MenuListRowLayoutItem item, String opp);
    }

}
