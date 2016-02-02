package com.yirmio.lockaway.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yirmio.lockaway.BL.RestaurantMenu;
import com.yirmio.lockaway.DAL.ParseConnector;
import com.yirmio.lockaway.LockAwayApplication;
import com.yirmio.lockaway.R;
import com.yirmio.lockaway.UI.util.SystemUiHider;
import com.yirmio.lockaway.UI.util.UserStatusLauncher;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class SplashScreenActivity extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;


    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;


    //Afeya Ksuma ObjectID
    private static final String AFEYA_KSUMA_ID = "g1bzMQEXoj";
    private TextView txtViewLoadingMenu;
    private ProgressBar prgsBarLoadingMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        this.txtViewLoadingMenu = (TextView)findViewById(R.id.txtViewLoading);
        this.txtViewLoadingMenu.setVisibility(View.GONE);
        this.prgsBarLoadingMenu = (ProgressBar)findViewById(R.id.loadingmenuprogressbar);
        this.prgsBarLoadingMenu.setVisibility(View.GONE);

        new LoadDataFromServer().execute();


    }

    private class LoadDataFromServer extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            txtViewLoadingMenu.setVisibility(View.VISIBLE);
            prgsBarLoadingMenu.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            txtViewLoadingMenu.setVisibility(View.GONE);
            prgsBarLoadingMenu.setVisibility(View.GONE);
            Intent mainIntent = new Intent(SplashScreenActivity.this, UserStatusLauncher.class);
            SplashScreenActivity.this.startActivity(mainIntent);
            SplashScreenActivity.this.finish();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            LockAwayApplication app = ((LockAwayApplication) getApplicationContext());

            //Load Menu
            ParseConnector con = LockAwayApplication.parseConector;
            con.setRestaurantID(AFEYA_KSUMA_ID);
            RestaurantMenu restMenu = con.getMenu();
            app.setRestaurantMenu(restMenu);
            return null;
        }
    }
}
