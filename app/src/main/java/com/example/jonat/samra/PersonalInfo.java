package com.example.jonat.samra;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jonat.samra.database.ApplicationDataBase;
import com.example.jonat.samra.database.pojo.UserInfo;

import java.util.List;

public class PersonalInfo extends AppCompatActivity {

    private String myUUID;

    private TextView titleTextView;

    private Button saveButon;

    private ApplicationDataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        this.db = ApplicationDataBase.getAppDatabase(getApplicationContext());
        super.setTitle("Personal Details");
        this.titleTextView = findViewById(R.id.titleTextView);
        this.saveButon = findViewById(R.id.saveButton);
        List<UserInfo> userInfos = this.db.userInfoDao().getAll();
        if (userInfos == null || userInfos.size() <= 0) {
            this.titleTextView.setText("Enter Person Info");
            this.saveButon.setText("SAVE");
        } else {
            this.titleTextView.setText("Update Person Info");
            this.saveButon.setText("UPDATE");
        }
        Log.i("Test", getIntent().getStringExtra("NFC-ID"));
        myUUID = getIntent().getStringExtra("NFC-ID");
        handleSave();
    }

    /**
     * This method will handle logic required when the SAVE button is pressed
     */
    private void handleSave() {
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Test", "Here 1");
                Intent registerActivity = new Intent(PersonalInfo.this, WelcomeScreen.class);
                if (validateAndSaveInfo()) {
                    // Only go to next screen if info valid
                    PersonalInfo.this.startActivity(registerActivity);
                }
            }
        });
    }

    /**
     * This method will validate info and save to db if correct
     * @return false if info not valid
     */
    private boolean validateAndSaveInfo() {

        Log.i("test", "In Function");
        UserInfo userInfo = new UserInfo();
        userInfo.setUuid(myUUID);
        EditText namefields = findViewById(R.id.nameField);
        EditText agefield = findViewById(R.id.ageField);
        EditText addressfield = findViewById(R.id.addressField);
        EditText bloodTypefield = findViewById(R.id.bloodTypeField);

        boolean valid = true;
        Log.i("Test", "Save Name = " + namefields.getText().toString());

        if (namefields.getText().toString().equals("")) {
            namefields.setError("Name must not be null");
            valid = false;
        }else {
            userInfo.setName(namefields.getText().toString());
        }
        if (agefield.getText().toString().equals("")) {
            agefield.setError("Age must not be null");

            valid = false;
        }else {
            userInfo.setAge(agefield.getText().toString());
        }
        if (addressfield.getText().toString().equals("")) {
            addressfield.setError("Address must not be null");

            valid = false;
        }else {
            userInfo.setAddress(addressfield.getText().toString());
        }

        if (bloodTypefield.getText().toString().equals("")) {
            bloodTypefield.setError("Blood Type must not be null");

            valid = false;
        }else {
            userInfo.setBloodType(bloodTypefield.getText().toString());
        }
        Log.i("Test", db.userInfoDao().getAll().toString());
//        // TODO: Check other fields for errors
//
        if (valid) {
            //TODO: Save DB and figure out how to manipulate data everytime
            removeExistingUsers(db);
            db.userInfoDao().insertAll(userInfo);
            Log.i("test", db.userInfoDao().getAll().toString());
        }

        return valid;
    }

    private void removeExistingUsers(ApplicationDataBase db) {
        List<UserInfo> existingUser = db.userInfoDao().getAll();
        for (UserInfo u : existingUser) {
            db.userInfoDao().delete(u);
        }
    }
}