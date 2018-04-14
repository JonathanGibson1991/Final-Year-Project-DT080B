package com.example.jonat.samra;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class WelcomeScreen extends AppCompatActivity implements View.OnClickListener{

    Button regButton;
    Button recButton;
    //This is the main screen that offers the first step in the options of the app
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome__screen); //relates back to the XML of welcome screen

        regButton = findViewById(R.id.regButton);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Test", "Here 1");
                Intent registerActivity = new Intent(WelcomeScreen.this, RegisterActivity.class); //
                WelcomeScreen.this.startActivity(registerActivity);
            }
        });

        recButton = findViewById(R.id.recButton);
        recButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent validationIntent = new Intent(WelcomeScreen.this, CardValidation.class);
                WelcomeScreen.this.startActivity(validationIntent);
            }
        });
    }

    /**
     * This Prevents users going back from the main welcome screen
     */
    @Override
    public void onBackPressed() {
    }

    @Override
    public void onClick(View v) {




    }
}

