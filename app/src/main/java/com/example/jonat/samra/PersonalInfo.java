package com.example.jonat.samra;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jonat.samra.database.ApplicationDataBase;
import com.example.jonat.samra.database.dao.UserInfoDao;
import com.example.jonat.samra.database.pojo.UserInfo;

import java.security.MessageDigest;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class PersonalInfo extends AppCompatActivity {

    public static String myUUID;
    private static String AES = "AES";
    private TextView titleTextView;
    private Button saveButon;
    private ApplicationDataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        //Setting up a DataBase Instance
        this.db = ApplicationDataBase.getAppDatabase(getApplicationContext());

        super.setTitle("Personal Details");
        this.titleTextView = findViewById(R.id.titleTextView);
        this.saveButon = findViewById(R.id.saveButton);

        List<UserInfo> userInfo = this.db.userInfoDao().getAll();
        if (userInfo == null || userInfo.size() <= 0) {
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

        //If all information is correct and valid in the adding personal info screen this if removes all previous user
        //data from the db instance and inserts the newly entered information.
        if (valid) {
            removeExistingUsers(db);
            db.userInfoDao().insertAll(encryptUser(userInfo, myUUID));
            Log.i("test", db.userInfoDao().getAll().toString());
        }

        return valid;
    }

    //This function will remove the user data every time a new card is registered or held against the device
    private void removeExistingUsers(ApplicationDataBase db) {
        List<UserInfo> existingUser = db.userInfoDao().getAll();
        for (UserInfo u : existingUser) {
            db.userInfoDao().delete(u);
        }
    }
//------------------------------ Encryption Section ------------------------------------------
    private String encrypt(String Data, String myUUID) throws Exception{
        SecretKeySpec key = generateKey(myUUID);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
        return encryptedValue;
    }

    private static SecretKeySpec generateKey(String password)throws Exception{
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }


    private static String decrypt (String Data, String password) throws Exception{
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE,key);
        byte[] decodedValue = Base64.decode(Data, Base64.DEFAULT);
        byte[] decVal = c.doFinal(decodedValue);
        String decryptedValue = new String(decVal);
        return decryptedValue;
    }

    private String encryptString(String input, String password){
        try
        {
            return encrypt(input, password);
        }
        catch( Exception e)
        {
            return "";
        }
    }

    private UserInfo encryptUser(UserInfo input, String password){
        UserInfo result = new UserInfo();
        result.setUuid(input.getUuid());
        result.setAge(encryptString(input.getAge(), password));
        result.setName(encryptString(input.getName(), password));
        result.setAddress(encryptString(input.getAddress(), password));
        result.setBloodType(encryptString(input.getBloodType(), password));
        return result;
    }

    private static String decryptString(String input, String password){
        try {
            return decrypt(input, password);
        }
        catch (Exception e)
        {
            return  "";
        }
    }

    public static UserInfo decryptUser(UserInfo input, String password){
        UserInfo result = new UserInfo();
        result.setUuid(input.getUuid());
        result.setAge(decryptString(input.getAge(), input.getUuid()));
        result.setName(decryptString(input.getName(), input.getUuid()));
        result.setAddress(decryptString(input.getAddress(), input.getUuid()));
        result.setBloodType(decryptString(input.getBloodType(), input.getUuid()));
        return result;
    }

}