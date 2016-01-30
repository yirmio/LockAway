package com.yirmio.lockaway.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.yirmio.lockaway.R;

public class LoginActivity extends Activity implements View.OnClickListener {

    private Button btnLogin;
    private Button btnSignUp;
    private EditText txtInptUserName;
    private EditText txtInptEmail;
    private EditText txtInptPassword;


    private String usrNametxt;
    private String passtxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        this.attachView();
    }

    private void attachView() {
        //Buttons
        this.btnLogin = (Button) findViewById(R.id.btnLogin);
        this.btnLogin.setOnClickListener(this);
        this.btnSignUp = (Button) findViewById(R.id.btnSignUp);
        this.btnSignUp.setOnClickListener(this);

        //EditTexts
        this.txtInptUserName = (EditText) findViewById(R.id.txtInputUserName);
        this.txtInptEmail = (EditText) findViewById(R.id.txtInputEmail);
        this.txtInptPassword = (EditText) findViewById(R.id.txtInputPassword);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin: {
                //Handle Login
                this.handleLogin();
                break;
            }
            case R.id.btnSignUp: {
                //Handle SignUp
                this.handleSignUp();
                break;
            }

        }

    }

    private void handleSignUp() {
        //TODO implement
        //TODO Check Input
        this.checkUserInput();
        //Perform Sign Up


        ParseUser newUser = new ParseUser();
        newUser.setUsername(usrNametxt);
        newUser.setPassword(passtxt);

        //Save in background
        newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    //Success
                    Toast.makeText(getApplicationContext(), R.string.seccesssignup,Toast.LENGTH_SHORT).show();
                }
                else {
                    //Fail
                    Toast.makeText(getApplicationContext(), R.string.errorSignup,Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private boolean checkUserInput() {
        boolean isInputValid = false;

        this.usrNametxt = this.txtInptUserName.getText().toString();
        this.passtxt = this.txtInptPassword.getText().toString();


        return isInputValid;
    }

    private void handleLogin() {
        //TODO implement

        this.checkUserInput();
        //Perform Login


        ParseUser.logInInBackground(usrNametxt, passtxt, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    //Good Login
                    Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class); //To welcome screen
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), R.string.Successlogin, Toast.LENGTH_SHORT).show();
                } else {
                    //Bad Login
                    Toast.makeText(getApplicationContext(), R.string.loginError, Toast.LENGTH_SHORT).show();
                    //TODO - log to server and client
                }
            }
        });

    }
}
