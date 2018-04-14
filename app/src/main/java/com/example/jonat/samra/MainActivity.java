package com.example.jonat.samra;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * This class will act as the main controller for the entry to the app and the required routing
 */
public class MainActivity extends AppCompatActivity {

    /**
     * This is the first entry point to the app, logic for routing and nfc will be executed from here
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Test", "Bypass Main Screen"); //Test for console log
        Intent welcomeIntent = new Intent(this, WelcomeScreen.class);
        startActivity(welcomeIntent);
    }
}
