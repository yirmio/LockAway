package com.yirmio.lockaway;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;

public class WelcomeActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ParseUser curUser = ParseUser.getCurrentUser();

        String strUserName = curUser.getUsername().toString();

        TextView txtVewUserName = (TextView)findViewById(R.id.txtViewWelcmActUserName);

        txtVewUserName.setText(strUserName);

        Button btnLogOut = (Button)findViewById(R.id.btnWelcmActLogOut);
        btnLogOut.setOnClickListener(this);

        Button btnContinue = (Button)findViewById(R.id.btnWelcmActContinue);
        btnContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnWelcmActContinue:
                //TODO - go to next activity
                break;
            case R.id.btnWelcmActLogOut:
                //Logout
                ParseUser.logOut();
                finish();
                break;
        }
    }
}
