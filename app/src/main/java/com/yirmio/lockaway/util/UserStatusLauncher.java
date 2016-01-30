package com.yirmio.lockaway.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;
import com.yirmio.lockaway.UI.LoginActivity;
import com.yirmio.lockaway.UI.WelcomeActivity;

/**
 * Created by oppenhime on 30/01/2016.
 */
public class UserStatusLauncher extends Activity{
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Determine whether the current user is an anonymous user
        if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            // If user is anonymous, send the user to LoginActivity.class
            Intent intent = new Intent(UserStatusLauncher.this,
                    LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            // If current user is NOT anonymous user
            // Get current user data from Parse.com
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                // Send logged in users to Welcome.class
                Intent intent = new Intent(UserStatusLauncher.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                // Send user to LoginSignupActivity.class
                Intent intent = new Intent(UserStatusLauncher.this,
                        LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }

    }
}
