package com.yirmio.lockaway.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.yirmio.lockaway.DAL.ParseConnector;
import com.yirmio.lockaway.LockAwayApplication;
import com.yirmio.lockaway.R;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity implements View.OnClickListener {

    private Button btnLogin;
    private Button btnSignUp;
    private Button btnAnonym;
    private EditText txtInptUserName;
    private EditText txtInptEmail;
    private EditText txtInptPassword;
    private EditText txtInptPassword2;
    private EditText txtInptDisplaName;
    private EditText txtInptPhoneNumber;
    private CheckBox chBxIsVeg;
    private CheckBox chBxIsGloton;


    private List<View> signUpViews;

    private String usrNametxt;
    private String passtxt;
    private String passTxt2;
    private String phoneNumberTxt;
    private String emailTxt;
    private String displayName;

    private boolean isFirstSignupClick;
    private boolean isAnonym;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        this.isFirstSignupClick = true;
        this.isAnonym = false;


        this.attachView();
    }

    private void attachView() {
        //Buttons
        this.btnLogin = (Button) findViewById(R.id.btnLogin);
        this.btnLogin.setOnClickListener(this);
        this.btnSignUp = (Button) findViewById(R.id.btnSignUp);
        this.btnSignUp.setOnClickListener(this);
        this.btnAnonym = (Button) findViewById(R.id.btnAnonym);
        this.btnAnonym.setOnClickListener(this);

        //EditTexts
        this.txtInptUserName = (EditText) findViewById(R.id.txtInputUserName);
        this.txtInptUserName.setOnClickListener(this);
        this.txtInptEmail = (EditText) findViewById(R.id.txtInputEmail);
        this.txtInptEmail.setOnClickListener(this);
        this.txtInptPassword = (EditText) findViewById(R.id.txtInputPassword);
        this.txtInptPassword.setOnClickListener(this);
        this.txtInptPassword2 = (EditText) findViewById(R.id.txtInputPassword2);
        this.txtInptPassword2.setOnClickListener(this);
        this.txtInptPhoneNumber = (EditText) findViewById(R.id.txtInputPhone);
        this.txtInptPhoneNumber.setOnClickListener(this);
        this.txtInptDisplaName = (EditText) findViewById(R.id.txtInputDisplayName);
        this.txtInptDisplaName.setOnClickListener(this);

        //CheckBoxes
        this.chBxIsVeg = (CheckBox) findViewById(R.id.chkBxIsVeg);
        this.chBxIsGloton = (CheckBox) findViewById(R.id.chkBxIsGlotonSensitive);

        //Update SignUp's Views List
        this.signUpViews = new ArrayList<>();
        this.signUpViews.add(txtInptEmail);
        this.signUpViews.add(txtInptPassword2);
        this.signUpViews.add(txtInptPhoneNumber);
        this.signUpViews.add(chBxIsGloton);
        this.signUpViews.add(chBxIsVeg);
        this.signUpViews.add(txtInptDisplaName);

        //Set UI For Login
        this.updateSignUpViewsVisibility(View.GONE);
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
            case R.id.txtInputEmail: {
                this.clearTxtInput(view, R.string.dummyEmail);
                break;
            }
            case R.id.txtInputPassword: {
                this.clearTxtInput(view, R.string.yourPassword);
                break;
            }
            case R.id.txtInputPassword2: {
                this.clearTxtInput(view, R.string.yourPassword);
                break;
            }
            case R.id.txtInputPhone: {
                this.clearTxtInput(view, R.string.PhoneNumber);
                break;
            }
            case R.id.txtInputUserName: {
                this.clearTxtInput(view, R.string.dumyUserName);
                break;
            }
            case R.id.btnAnonym:
//                this.handleAnonymous();
                Toast.makeText(this,"Not Supportes yet...",Toast.LENGTH_LONG).show();
                break;

        }

    }

    private void handleAnonymous() {
        this.updateSignUpViewsVisibility(View.GONE);
        this.txtInptDisplaName.setVisibility(View.GONE);
        this.txtInptUserName.setVisibility(View.GONE);
        this.txtInptPassword.setVisibility(View.GONE);
        this.txtInptPhoneNumber.setVisibility(View.VISIBLE);
        this.btnSignUp.setVisibility(View.GONE);
        Toast.makeText(this, R.string.must_insert_phone, Toast.LENGTH_LONG).show();
        this.isAnonym = true;

    }

    private void clearTxtInput(View view, int txtInt) {
        EditText tmp = (EditText) view;
        if (tmp.getText().toString().equals(getString(txtInt))) {
            tmp.setText("");
        }
    }

    private void handleSignUp() {
        // if first click on button - just update ui
        if (isFirstSignupClick) {
            //Update UI
            updateSignUpViewsVisibility(View.VISIBLE);
            isFirstSignupClick = false;
        } else {
            //Check Input
            checkUserInput("sign");
            //Perform Sign Up
            performSignUp();
        }


    }

    private void updateSignUpViewsVisibility(int statusCode) {
        for (View v : this.signUpViews) {
            v.setVisibility(statusCode);
        }

    }

    private void performSignUp() {
        ParseUser newUser = new ParseUser();
        newUser.setUsername(usrNametxt);
        newUser.setPassword(passtxt);
        newUser.put(getString(R.string.parse_user_phone_number), phoneNumberTxt);
        newUser.setEmail(emailTxt);
        newUser.put(getString(R.string.parse_user_is_veg), this.chBxIsVeg.isChecked());
        newUser.put(getString(R.string.parse_user_is_gloten), this.chBxIsGloton.isChecked());
        newUser.put(getString(R.string.parse_user_display_name), displayName);

        //Save in background
        newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //Success
                    Toast.makeText(getApplicationContext(), R.string.seccesssignup, Toast.LENGTH_SHORT).show();
                    updateSignUpViewsVisibility(View.GONE);
                } else {
                    //Fail
                    if (e.getCode() == 203) {
                        //the email address yirmio@gmail.com has already been taken
                        Toast.makeText(getApplicationContext(), R.string.errorSignupEmailTaken, Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(getApplicationContext(), R.string.errorSignup, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkUserInput(String op) {

        boolean isInputValid = false;
//        //anonymous login
//        if (isAnonym){
//            this.phoneNumberTxt = this.txtInptPhoneNumber.getText().toString();
//            if (this.phoneNumberTxt.length() != 10){
//                return false;
//            }
//            else {
//                return true;
//            }
//        }

        //for just login
        this.usrNametxt = this.txtInptUserName.getText().toString();
        this.passtxt = this.txtInptPassword.getText().toString();
        if (passtxt.length() > 0 && usrNametxt.length() > 0) {
            isInputValid = true;
        }
        this.passTxt2 = this.txtInptPassword2.getText().toString();
        this.emailTxt = this.txtInptEmail.getText().toString();
        this.phoneNumberTxt = this.txtInptPhoneNumber.getText().toString();
        this.displayName = this.txtInptDisplaName.getText().toString();


        //For SignUp
        if (op.toLowerCase().contains("sign")) {
            //Pass not mach
            if (!this.passTxt2.equals(passtxt)) {
                isInputValid = false;
                return isInputValid;
            }
            //Phone number
            if (this.phoneNumberTxt.length() != 10) {
                isInputValid = false;
                return isInputValid;
            }
            //Email Validation
            if (!Patterns.EMAIL_ADDRESS.matcher(this.emailTxt).matches()) {
                isInputValid = false;
                return isInputValid;
            }
            if (this.displayName.length() == 0) {
                isInputValid = false;
                return isInputValid;
            }
        }


        return isInputValid;
    }

    private void handleLogin() {

        //Update UI For Login
        this.updateSignUpViewsVisibility(View.GONE);
        if (this.checkUserInput("login")) {
//            if (isAnonym) {
//                ParseConnector.setInstallationCurrentUserId();
//                LockAwayApplication.workAnonymous(true,phoneNumberTxt);
//                setResult(RESULT_OK);
//                finish();
//                return;
//            } else {
//                ParseConnector.setInstallationCurrentUserId();
//            }
            setResult(RESULT_OK);
//            return;
        } else {
            //error

        }

        //Perform Login
        DoLoginTask loginTask = new DoLoginTask(this);
        loginTask.execute();


    }

    private class DoLoginTask extends AsyncTask {
        ProgressDialog dialog;
        LoginActivity activity;
        boolean res;

        public DoLoginTask(LoginActivity loginActivity) {
            dialog = new ProgressDialog(loginActivity);
            activity = loginActivity;
        }


        @Override
        protected void onPreExecute() {
            dialog.setMessage(getResources().getString(R.string.wait_login_msg));
            dialog.show();
        }

        @Override
        protected void onPostExecute(Object o) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if (res) {
                setResult(RESULT_OK);
                activity.finish();
            } else {
                Toast.makeText(activity, R.string.loginError, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            res = true;
            try {
                ParseUser usr = ParseUser.logIn(usrNametxt, passtxt);
                if (usr != null) {
                    res = true;
                    ParseConnector.setInstallationCurrentUserId();
                } else {
                    res = false;
                }
            } catch (ParseException e) {
                res = false;
            }
//            ParseUser.logIn(usrNametxt, passtxt, new LogInCallback(); {
//                @Override
//                public void done(ParseUser parseUser, ParseException e) {
//                    if (parseUser != null) {
//                        //Good Login
//                        ParseConnector.setInstallationCurrentUserId();
//                        setResult(RESULT_OK);
//                        res = true;
//
//                    } else {
//                        //Bad Login
//                        res = false;
//                        Toast.makeText(getApplicationContext(), R.string.loginError, Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
            return res;
        }
    }
}