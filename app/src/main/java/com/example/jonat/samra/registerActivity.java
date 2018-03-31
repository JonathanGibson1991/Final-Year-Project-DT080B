package com.example.jonat.samra;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class registerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTitle("Register SAMRA Card"); // Used to set the text in top toolbar https://stackoverflow.com/questions/7606471/android-change-app-label-programatically
        setContentView(R.layout.activity_register);

        // TODO: Change to detect nfc and pass id of card to intent. Use shared prefrences to store tagID
        Intent personalIntent = new Intent(this, personalInfo.class); //Change tomorrow to show to Piotr
        startActivity(personalIntent);


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

}
