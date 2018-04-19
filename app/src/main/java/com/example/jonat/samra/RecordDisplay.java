package com.example.jonat.samra;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jonat.samra.database.ApplicationDataBase;
import com.example.jonat.samra.database.pojo.UserInfo;

import java.util.List;

import static com.example.jonat.samra.utils.TagUtils.getTagId;

public class RecordDisplay extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    TextView nameField = null;
    TextView ageField = null;
    TextView addressField = null;
    TextView bloodTypeField = null;
    Button emailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_display);
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        this.nameField = findViewById(R.id.nameField);
        this.ageField = findViewById(R.id.ageField);
        this.addressField = findViewById(R.id.addressField);
        this.bloodTypeField = findViewById(R.id.bloodTypeField);
        this.emailButton = findViewById(R.id.emailButton);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String action = intent.getAction();
        ApplicationDataBase db = ApplicationDataBase.getAppDatabase(getApplicationContext());

        if (!NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            Toast.makeText(this, "onResume() - NOT NFC", Toast.LENGTH_SHORT).show();
            Log.i("Test", "onResume() - NOT NFC");
            return;
        }

        Toast.makeText(this, "onResume() - ACTION_TAG_DISCOVERED", Toast.LENGTH_SHORT).show();

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String tagId = getTagId(tag);
        if(tag == null || tagId == null || tagId.equals("")){
            toastInvlaidTag(null);
            Log.i("Test", tagId.toString() +" -- " + tag.toString());
            return;
        }
//
        List<UserInfo> dbAll = db.userInfoDao().getAll();
        Log.i("Test", "ALL DB = " + dbAll.toString());
        Log.i("Test", "TAG ID = " + tagId);
        UserInfo encryptedUserInfo = db.userInfoDao().getByCardId(tagId);
        final UserInfo userInfo = PersonalInfo.decryptUser(encryptedUserInfo, PersonalInfo.myUUID);

        if (userInfo == null) {
            toastInvlaidTag(tagId);
            Log.i("Test", "USERINFO null");
            return;
        }

        Log.i("Test", userInfo.toString());

        nameField.setText(userInfo.getName());
        ageField.setText(userInfo.getAge());
        addressField.setText(userInfo.getAddress());
        bloodTypeField.setText(userInfo.getBloodType());

        //TODO place this in an email button
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail(userInfo.toString());
            }
        });


    }

    private void toastInvlaidTag(String tagId) {
        Intent validationIntent;
        if (tagId == null) {
            validationIntent = new Intent(this, RegisterActivity.class);
        } else {
            validationIntent = new  Intent(this, PersonalInfo.class).putExtra("NFC-ID", tagId);
        }
        Toast.makeText(this, "Card not recognised", Toast.LENGTH_SHORT).show();
        this.startActivity(validationIntent);
    }

    private void sendEmail(String userInfo){
        Intent email = new Intent(Intent.ACTION_SEND);
        //email.putExtra(Intent.EXTRA_EMAIL, new String[]{ });
        email.putExtra(Intent.EXTRA_SUBJECT, "Medical Records");
        email.putExtra(Intent.EXTRA_TEXT, userInfo.toString());

        //need this to prompts email client only
        email.setType("message/rfc822");

        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }
}

