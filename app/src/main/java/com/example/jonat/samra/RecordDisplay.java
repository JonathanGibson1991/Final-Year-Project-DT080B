package com.example.jonat.samra;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_display);
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        this.nameField = findViewById(R.id.nameField);
        this.ageField = findViewById(R.id.ageField);
        this.addressField = findViewById(R.id.addressField);
        this.bloodTypeField = findViewById(R.id.bloodTypeField);
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

        List<UserInfo> dbAll = db.userInfoDao().getAll();
        Log.i("Test", "ALL DB = " + dbAll.toString());
        Log.i("Test", "TAG ID = " + tagId);
        UserInfo userInfo = db.userInfoDao().getByCardId(tagId);

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
}

