package com.yirmio.lockaway.UI;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.yirmio.lockaway.BL.RestaurantMenuObject;
import com.yirmio.lockaway.DAL.ParseConnector;
import com.yirmio.lockaway.LockAwayApplication;
import com.yirmio.lockaway.R;
import com.yirmio.lockaway.util.OrderBuilderAdapter;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ActionBar.TabListener, AddMenuItemFragment.OnFragmentInteractionListener, menuListFragment.OnFragmentInteractionListener, OrderBuilderFragment.OnFragmentInteractionListener {

    private static final int ORDERFRAGMENTNUMBER = 2;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    ArrayList<Fragment> fragments;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        this.fragments = new ArrayList<Fragment>();
        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    //region onFragmentInteraction
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    //Add/Del item to DB in cloud
    public void onFragmentInteraction(String id, String orderID, String op) {
//        ParseObject tmpItem = new ParseObject("OrderedObjects");
        if (op == "add") {
            ParseConnector.addObjectToOrder(id, orderID);

        } else if (op == "remove") {
            ParseConnector.removeObjectFromOrder(orderID, id);

        }


    }


    //endregion


    @Override
    //Get MenuListRowLayoutItem and add it to user order and refresh adapter
    public void onFragmentInteraction(MenuListRowLayoutItem item, String opp) {
        if (opp == "add") {
            LockAwayApplication.getUserOrder().addItemToOrder(new RestaurantMenuObject(item), true);//Local BL
            //UI
            if (fragments.size() >= ORDERFRAGMENTNUMBER + 1) {
                OrderBuilderAdapter adapter = (OrderBuilderAdapter) ((OrderBuilderFragment) fragments.get(ORDERFRAGMENTNUMBER)).getmAdapter();
                adapter.clear();
                adapter.addAll(LockAwayApplication.getUserOrder().getObjects());

            }
        } else if (opp == "remove") {
            LockAwayApplication.getUserOrder().removeItemFromOrder(item.getId());
            if (fragments.size() >= ORDERFRAGMENTNUMBER + 1) {
                OrderBuilderAdapter adapter = (OrderBuilderAdapter) ((OrderBuilderFragment) fragments.get(ORDERFRAGMENTNUMBER)).getmAdapter();
                adapter.clear();
                adapter.addAll(LockAwayApplication.getUserOrder().getObjects());

            }
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {


            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch (position) {
                case 0: {
                    if (fragments.size() == 0) {
                        fragments.add(0, new menuListFragment());
                        return fragments.get(0);
                    } else {
                        return fragments.get(0);
                    }
                }

                case 1:
                    if (fragments.size() == 1) {
                        fragments.add(1, new AddMenuItemFragment());
                        return fragments.get(1);
                    }

                case 2:
                    if (fragments.size() == 2) {
                        fragments.add(2, new OrderBuilderFragment());
                        return fragments.get(2);
                    }
                    //return new OrderBuilderFragment();
            }


            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "תפריט";
                case 1:
                    return "הוספת פריט לתפריט";
                case 2:
                    return "ביצוע הזמנה";
            }
            return null;
        }
    }


}
