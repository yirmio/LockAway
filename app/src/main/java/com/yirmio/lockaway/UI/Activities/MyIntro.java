package com.yirmio.lockaway.UI.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.yirmio.lockaway.R;

/**
 * Created by oppenhime on 04/07/2016.
 */
public class MyIntro extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntroFragment.newInstance("Menu","Click the "+" button to add item to order... \n you will see the cart at the top with updated counter", R.drawable.menu, R.color.backgroundColor));
        addSlide(AppIntroFragment.newInstance("ExtraInfo","By clicking item photo you will see more details about it", R.drawable.extrainfo, R.color.backgroundColor));
        addSlide(AppIntroFragment.newInstance("Order Summery","Watch all items in order before send it to restaurant", R.drawable.ordersam, R.color.backgroundColor));
        addSlide(AppIntroFragment.newInstance("Send order","Watch ETA and order details before send it to restaurant", R.drawable.sendorder, R.color.backgroundColor));
        addSlide(AppIntroFragment.newInstance("Order status","Watch ETA and order details while driving to restaurant. \n You can also dial to navigate to the restaurant.", R.drawable.orderstatus, R.color.backgroundColor));
        addSlide(AppIntroFragment.newInstance("Update ETA","Update ETA and cancel order", R.drawable.orderupdate, R.color.backgroundColor));


        showSkipButton(true);
        setProgressButtonEnabled(true);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {

//        super.onDonePressed(currentFragment);
        setResult(MainActivity.INTROFINISH);
        this.finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }
}
