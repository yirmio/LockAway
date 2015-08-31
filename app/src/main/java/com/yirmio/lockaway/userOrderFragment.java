package com.yirmio.lockaway;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.yirmio.lockaway.BL.RestaurantMenuObject;
import com.yirmio.lockaway.BL.UserOrder;
import com.yirmio.lockaway.UI.RowLayoutItem;
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
        mAdapter = new UserOrderAdapter(getActivity(), R.layout.user_order_item_layout, orderList);
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

        //         mAdapter = new UserOrderAdapter(getActivity(),R.layout.user_order_item_layout,orderList);

        // mListView.invalidate();
        return  view;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(RowLayoutItem item);
    }

}
