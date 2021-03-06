package com.yirmio.lockaway.UI.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.yirmio.lockaway.BL.MenuItemTypesEnum;
import com.yirmio.lockaway.BL.RestaurantMenu;
import com.yirmio.lockaway.BL.RestaurantMenuObject;
import com.yirmio.lockaway.LockAwayApplication;
import com.yirmio.lockaway.R;
import com.yirmio.lockaway.UI.ListsItems.MenuListRowLayoutItem;
import com.yirmio.lockaway.UI.util.MenuAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class menuListFragment extends Fragment implements AbsListView.OnItemClickListener {
    //public LockAwayApplication app = ((LockAwayApplication) this.getActivity().getApplicationContext());
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    LockAwayApplication app;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;
    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;
    private List menuList;
    private RestaurantMenu restaurantMenu;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public menuListFragment() {
        this.restaurantMenu = LockAwayApplication.getRestaurantMenu();

    }

    public menuListFragment(MenuItemTypesEnum tmpType) {
        menuList = new ArrayList();
        for (RestaurantMenuObject obj : LockAwayApplication.getRestaurantMenu().getMenuByType(tmpType)) {
            menuList.add(new MenuListRowLayoutItem(obj));
        }
        //this.menuList = LockAwayApplication.getRestaurantMenu().getMenuByType(tmpType);
    }

    // TODO: Rename and change types of parameters
    public static menuListFragment newInstance(String param1, String param2) {
        menuListFragment fragment = new menuListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public OnFragmentInteractionListener getmListener() {
        return mListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = ((LockAwayApplication) this.getActivity().getApplicationContext());
        //No init by types so show all items
        //TODO = disable it
        if (menuList == null) {
            menuList = new ArrayList();
            for (RestaurantMenuObject obj : this.restaurantMenu.getAllItems()) {
                menuList.add(new MenuListRowLayoutItem(obj));
            }
        }
        mAdapter = new MenuAdapter(getActivity(), menuList, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_list, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);


        return view;
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        if (null != mListener) {
//            // Notify the active callbacks interface (the activity, if the
//            // fragment is attached to one) that an item has been selected.
//            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
//        }

//        MenuListRowLayoutItem item = (MenuListRowLayoutItem) menuList.get(position);
//        mListener.onFragmentInteraction(item.getId(),app.GetOrder());
    }

    public void onPlusButtonClicked(String id) {
        MenuListRowLayoutItem item = this.getItemById(id);
        //DB
        mListener.onFragmentInteraction(id, app.GetOrderID(), "add");
        //Local
        mListener.onFragmentInteraction(item, "add");
        Toast.makeText(getActivity(), getString(R.string.item_addedd), Toast.LENGTH_SHORT).show();

    }

    private MenuListRowLayoutItem getItemById(String id) {
        MenuListRowLayoutItem tmpMenuListRowLayoutItem = null;
        for (Object tmpItem : menuList) {
            tmpMenuListRowLayoutItem = (MenuListRowLayoutItem) (tmpItem);
            if (tmpMenuListRowLayoutItem.getId().equals(id)) {
                return tmpMenuListRowLayoutItem;
            }
        }
        return tmpMenuListRowLayoutItem;
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    public void onObjectPhotoClicked(String id) {
//        MenuListRowLayoutItem item = (MenuListRowLayoutItem) menuList.get(pos);
//        mListener.onFragmentInteraction(item.getId(), "showobject");
        mListener.onFragmentInteraction(id,"showobject");
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
        public void onFragmentInteraction(String id, String orderID, String opp);

        public void onFragmentInteraction(String id, String opp);//For object menu activity

        public void onFragmentInteraction(MenuListRowLayoutItem item, String opp);
    }


}
