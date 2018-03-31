package com.example.jonat.samra;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.jonat.samra.database.pojo.UserInfo;

public class personalInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTitle("Personal Details");
        setContentView(R.layout.activity_personal_info);
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
                Intent registerActivity = new Intent(personalInfo.this, Welcome_Screen.class);
                if (validateAndSaveInfo()) {
                    // Only go to next screen if info valid
                    personalInfo.this.startActivity(registerActivity);
                }
            }
        });
    }

    /**
     * This method will validate info and save to db if correct
     * @return false if info not valid
     */
    private boolean validateAndSaveInfo() {
        EditText namefields = findViewById(R.id.nameField);
        if (!namefields.getText().toString().equals("")) {
            Log.i("Test", "Save Name = " + namefields.getText().toString());
            // TODO: Save Name To DB

//            // Entry point
//            byte[] key = CarsonKey.ProvideKey();
//            GarethEnc.EncryptAndSave(new personalInfo(), key);

            return true;
        }
        // Set error name info is invalid
        namefields.setError("Name must not be null");
        return false;
    }
}

//public static class CarsonKey{
//    public byte[] ProvideKey()
//    {
//        // SecretKey -> byte[]
//
//        return byte[];
//    }
//}
//
//public static class GarethEnc {
//    public static void EncryptAndSave(PersonalInfo data, byte[] privateKey){
//        // TODO
//        // if things go wrong, throw exceptions
//    }
//}
//
//public class PersonalInfo{
//    public String name;
//    //public String Address;
//}