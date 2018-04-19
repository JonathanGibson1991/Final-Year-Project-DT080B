package com.example.jonat.samra;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import static com.example.jonat.samra.utils.TagUtils.getTagId;

public class RegisterActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTitle("Register SAMRA Card"); // Used to set the text in top toolbar https://stackoverflow.com/questions/7606471/android-change-app-label-programatically
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(this.nfcAdapter == null){
            Toast.makeText(this, "NFC NOT supported on this devices!", Toast.LENGTH_LONG).show();
        }else if(!this.nfcAdapter.isEnabled()){
            Toast.makeText(this, "NFC NOT Enabled!", Toast.LENGTH_LONG).show();
        }
        setUpForegroundDispatchSystem();
        setContentView(R.layout.activity_register);
    }


    private void setUpForegroundDispatchSystem() //The foreground dispatch system is used to set up and configure the NFC adapter of the local device
    {
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Bundle opts = new Bundle();
        opts.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 5000); //Sets a delay on each adapter

        int flags = NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;
        flags += NfcAdapter.FLAG_READER_NFC_A;
        this.nfcAdapter.enableReaderMode(this, new RegNfcCallBack(), flags, opts);
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpForegroundDispatchSystem();
    }

    private class RegNfcCallBack implements NfcAdapter.ReaderCallback {

        @Override
        public void onTagDiscovered(Tag tag) {
            String tagId = getTagId(tag);
            Intent intent = new  Intent(RegisterActivity.this, PersonalInfo.class).putExtra("NFC-ID", tagId);
            RegisterActivity.this.startActivity(intent);
        }
    }

}
