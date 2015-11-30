package com.yirmio.lockaway;

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
import com.yirmio.lockaway.UI.RowLayoutItem;
import com.yirmio.lockaway.UI.SendOrderActivity;
import com.yirmio.lockaway.UI.UserOrderRowLayout;
import com.yirmio.lockaway.util.UserOrderAdapter;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link userOrderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link userOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class userOrderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ArrayList orderList;
    private UserOrder userOrder;
    private LockAwayApplication app;
    private UserOrderAdapter mAdapter;
    private AbsListView mListView;
    private TextView mTotalPriceTextView;
    private TextView mTotalTimeTextView;
    private Button mSendBtn;

    //region Ctor

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment userOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static userOrderFragment newInstance(String param1, String param2) {
        userOrderFragment fragment = new userOrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    //endregion

    //region Getters & Setters
    public UserOrderAdapter getmAdapter() {
        return mAdapter;
    }


    public void setmAdapter(UserOrderAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    //endregion
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = ((LockAwayApplication) this.getActivity().getApplicationContext());
        orderList = new ArrayList();
        if (app.getUserOrder() != null) {
            for (RestaurantMenuObject obj : app.getUserOrder().getObjects()) {
                orderList.add(new UserOrderRowLayout(obj));
            }
        }
        mAdapter = new UserOrderAdapter(getActivity(), R.layout.user_order_item_layout, orderList,this);
        //mListView = (AbsListView)getView().findViewById(R.id.user_order_listView);
        //((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_order, container, false);


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
        if (app.getUserOrder() != null) {
            mTotalPriceTextView.setText(String.valueOf(app.getUserOrder().getTotalPrice()));
            mTotalTimeTextView.setText(String.valueOf(app.getUserOrder().getTotalTimeToMake()));
        }
        return view;
    }

    private void sendCurrentOrder() {
        //Open sendOrderActivity
        Intent intent = new Intent(this.getActivity(),SendOrderActivity.class);
        intent.putExtra("totalPrice",this.mTotalPriceTextView.getText());
        intent.putExtra("totalTimeToMake",this.mTotalTimeTextView.getText());
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
                orderList.clear();
                orderList.addAll(app.getUserOrder().getObjects());
                mAdapter.notifyDataSetChanged();
            }
        }
        /*if (app.getUserOrder() != null) {
            if (orderList.size() < app.getUserOrder().getObjects().size()){
                orderList = new ArrayList();
                for (RestaurantMenuObject obj :
                        app.getUserOrder().getObjects()) {
                    orderList.add(new UserOrderRowLayout(obj));
                }
                mAdapter = new UserOrderAdapter(getActivity(),orderList);

            }
        }*/
        mListView.invalidate();
    }

    public void onXButtonClicked(int pos) {

        RestaurantMenuObject item = (RestaurantMenuObject) orderList.get(pos);
        RowLayoutItem tmpItem = new RowLayoutItem(item);
        mListener.onFragmentInteraction(item.getId(), app.GetOrderID(), "remove");
        mListener.onFragmentInteraction(tmpItem, "remove");


        if (app.getUserOrder() != null) {
            mTotalPriceTextView.setText(String.valueOf(app.getUserOrder().getTotalPrice()));
            mTotalTimeTextView.setText(String.valueOf(app.getUserOrder().getTotalTimeToMake()));
        }

        Toast.makeText(getActivity(), getString(R.string.item_removed), Toast.LENGTH_SHORT).show();
    }
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String itemID, String orderID,String opp);
        public void onFragmentInteraction(RowLayoutItem item,String opp);
    }

}
