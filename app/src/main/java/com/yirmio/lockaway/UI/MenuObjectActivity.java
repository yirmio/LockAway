package com.yirmio.lockaway.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.yirmio.lockaway.BL.RestaurantMenuObject;
import com.yirmio.lockaway.LockAwayApplication;
import com.yirmio.lockaway.R;

public class MenuObjectActivity extends AppCompatActivity {
    private String objectId;
    private SliderLayout slider;
    private RestaurantMenuObject resObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_object);
        //TODO - get objectid from bundle
        Bundle extras = getIntent().getExtras();
        this.objectId = extras.getString("objectId");

        //Get item from menu
        this.resObj = LockAwayApplication.getRestaurantMenu().getItemById(objectId);


        //Build photo slider
        this.slider = (SliderLayout)findViewById(R.id.menuObjectActivityImagesSlider);
        DefaultSliderView defaultSliderView = new DefaultSliderView(this);
        defaultSliderView.image(resObj.getPic().getUrl().toString());
        this.slider.addSlider(defaultSliderView);


    }
    @Override
    protected void onStop(){
        this.slider.stopAutoCycle();
        super.onStop();
    }


}
