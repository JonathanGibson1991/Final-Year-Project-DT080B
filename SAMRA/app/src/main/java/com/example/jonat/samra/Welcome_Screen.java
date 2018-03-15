package com.example.jonat.samra;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Welcome_Screen extends AppCompatActivity implements View.OnClickListener{

    Button regButton;
    Button recButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome__screen);

        regButton=(Button) findViewById(R.id.regButton);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Test", "Here 1");
                Intent registerActivity = new Intent(Welcome_Screen.this, registerActivity.class);
                Welcome_Screen.this.startActivity(registerActivity);
            }
        });
//
//        recButton=(Button) findViewById(R.id.recButton);
//        recButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent recordIntent = new Intent(this, recordActivity.class);
//            }
//        });

        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter != null && nfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC Available", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "NFC Not Available", Toast.LENGTH_SHORT).show();
        }
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

