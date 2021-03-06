package com.yirmio.lockaway.UI.Activities;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.parse.ParseFile;
import com.yirmio.lockaway.BL.RestaurantMenuObject;
import com.yirmio.lockaway.Interfaces.Observer;
import com.yirmio.lockaway.LockAwayApplication;
import com.yirmio.lockaway.R;

import java.util.List;

public class MenuObjectActivity extends AppCompatActivity implements View.OnClickListener, Observer {
    private String objectId;
    private SliderLayout slider;
    private RestaurantMenuObject resObj;
    private List<ParseFile> images;
    private TextView txtViewTitle;
    private TextView txtViewDescription;
    private TextView txtViewPrice;
    private TextView txtViewTimeToMake;
    private ImageView imgViewGloten;
    private ImageView imgViewVeg;
    private Button btnAddToOrder;
    private Button btnAddToFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        // Enable up icon

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_menu_object);

        //TODO - get objectid from bundle
        Bundle extras = getIntent().getExtras();
        this.objectId = extras.getString("objectId");

        //Attach UI Elements
        this.attachUIElements();


        //Get item from menu
        this.resObj = LockAwayApplication.getRestaurantMenu().getItemById(objectId);
        this.putInfoInUI(resObj);
        //Get Photos
        this.images = LockAwayApplication.parseConector.getImagesFilesForObject(objectId, 0);


        //Build photo slider
        this.slider = (SliderLayout) findViewById(R.id.menuObjectActivityImagesSlider);
        for (ParseFile f : images) {
            DefaultSliderView defaultSliderView = new DefaultSliderView(this);
            defaultSliderView.image(f.getUrl().toString());
            this.slider.addSlider(defaultSliderView);
        }


    }

    private void putInfoInUI(RestaurantMenuObject resObj) {
        this.txtViewTitle.setText(resObj.getTitle());
        this.txtViewDescription.setText(resObj.getDescription());
        this.txtViewPrice.setText(String.valueOf(resObj.getPrice()));
        this.txtViewTimeToMake.setText(String.valueOf(resObj.getTimeToMake()));
        if (resObj.isGlootenFree()) {
            this.imgViewGloten.setVisibility(View.VISIBLE);
        }
        if (resObj.isVeg()) {
            this.imgViewVeg.setVisibility(View.VISIBLE);
        }
    }

    private void attachUIElements() {
        this.txtViewTitle = (TextView) findViewById(R.id.mnuObjActTitle);
        this.txtViewDescription = (TextView) findViewById(R.id.mnuObjActDesc);
        this.txtViewPrice = (TextView) findViewById(R.id.mnuObjActTextViewPrice);
        this.txtViewTimeToMake = (TextView) findViewById(R.id.mnuObjActTextViewTimeToMake);

        this.imgViewGloten = (ImageView) findViewById(R.id.mnuObjActImgViewGloten);
        this.imgViewGloten.setVisibility(View.GONE);
        this.imgViewVeg = (ImageView) findViewById(R.id.mnuObjActImgViewVeg);
        this.imgViewVeg.setVisibility(View.GONE);

        this.btnAddToOrder = (Button) findViewById(R.id.mnuObjActBtnAddToOrder);
        this.btnAddToFavorite = (Button) findViewById(R.id.mnuObjActBtnAddToFavorite);
        this.btnAddToFavorite.setOnClickListener(this);
        this.btnAddToOrder.setOnClickListener(this);
    }

    @Override
    protected void onStop() {
        this.slider.stopAutoCycle();
        super.onStop();
    }

    @Override
    protected void onPause() {
LockAwayApplication.parseConector.removeObserver(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
LockAwayApplication.parseConector.registerObserver(this);
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mnuObjActBtnAddToFavorite:
                LockAwayApplication.parseConector.addItemToFavorite(objectId);
                break;
            case R.id.mnuObjActBtnAddToOrder:
                //TODO - do it with connector
                LockAwayApplication.getUserOrder().addItemToOrder(resObj, true);
                update(getString(R.string.item_addedd),this);
                finish();
                break;
        }
    }

    @Override
    public void update(String msg, Observer ob) {
        if (ob == this){
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }
    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            // This is the up button

            case android.R.id.home:

                NavUtils.navigateUpFromSameTask(this);

                // overridePendingTransition(R.animator.anim_left, R.animator.anim_right);

                return true;

            default:

                return super.onOptionsItemSelected(item);

        }

    }
}
