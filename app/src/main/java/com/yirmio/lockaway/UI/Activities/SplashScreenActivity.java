package com.yirmio.lockaway.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.yirmio.lockaway.BL.RestaurantMenu;
import com.yirmio.lockaway.DAL.ParseConnector;
import com.yirmio.lockaway.LockAwayApplication;
import com.yirmio.lockaway.R;
import com.yirmio.lockaway.UI.util.SystemUiHider;


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
    private static final int LOGIN_REQUEST_CODE = 100;


    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;


    //Afeya Ksuma ObjectID
    private static final String AFEYA_KSUMA_ID = "g1bzMQEXoj";
    private TextView txtViewLoadingMenu;
    private ProgressBar prgsBarLoadingMenu;
    private TextView txtViewUserName;
    private TextView txtViewWelcomeLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);

        attachViews();

        //Check user login
        if (ParseUser.getCurrentUser().getObjectId() != null){
            updtaeUIAfterLogin();
            //load menu
            new LoadDataFromServer().execute();
        }
        else{
            //Send to login screen
//            Toast.makeText(getApplicationContext(),"To login...",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SplashScreenActivity.this,LoginActivity.class);
            startActivityForResult(intent,LOGIN_REQUEST_CODE);
        }




    }

    private void attachViews() {
        this.txtViewLoadingMenu = (TextView)findViewById(R.id.txtViewLoading);
        this.txtViewLoadingMenu.setVisibility(View.GONE);
        this.prgsBarLoadingMenu = (ProgressBar)findViewById(R.id.loadingmenuprogressbar);
        this.prgsBarLoadingMenu.setVisibility(View.GONE);
        this.txtViewUserName = (TextView)findViewById(R.id.splsScrnUserName);
        this.txtViewUserName.setVisibility(View.GONE);
        this.txtViewWelcomeLabel = (TextView)findViewById(R.id.splsScrnWelcomeLabel);
        this.txtViewWelcomeLabel.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                updtaeUIAfterLogin();
                new LoadDataFromServer().execute();
            }
        }
    }

    private void updtaeUIAfterLogin() {
        this.txtViewUserName.setVisibility(View.VISIBLE);
        this.txtViewWelcomeLabel.setVisibility(View.VISIBLE);
        this.txtViewUserName.setText(ParseUser.getCurrentUser().getString(getString(R.string.parse_user_display_name)));
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
            Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
            SplashScreenActivity.this.startActivity(mainIntent);
            SplashScreenActivity.this.finish();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            LockAwayApplication app = ((LockAwayApplication) getApplicationContext());
            ParseConnector con = LockAwayApplication.parseConector;
            con.initConnector(AFEYA_KSUMA_ID);
            con.setRestaurantID(AFEYA_KSUMA_ID);

            //Set Store
            app.setStore(con.getStore(AFEYA_KSUMA_ID));

            //Load Menu
            RestaurantMenu restMenu = con.getMenu();
            app.setRestaurantMenu(restMenu);
            return null;
        }
    }
}
