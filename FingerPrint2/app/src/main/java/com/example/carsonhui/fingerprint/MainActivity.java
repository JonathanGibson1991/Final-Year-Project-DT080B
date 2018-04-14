package com.example.carsonhui.fingerprint;


import android.app.KeyguardManager;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;


public class MainActivity extends AppCompatActivity {

    // Declare a string variable for the key we're going to use in our fingerprint authentication
    private static final String KEY_NAME = "yourKey";
    private Cipher cipher;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private TextView textView;
    private FingerprintManager.CryptoObject cryptoObject;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // If apps' minSdkVersion is lower than 23, need to verify device running Marshmallow or higher before running fingerprint code
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Get an instance of KeyguardManager and FingerprintManager
            keyguardManager =
                    (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            fingerprintManager =
                    (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

            textView = (TextView) findViewById(R.id.textview);

            //Check whether the device has a fingerprint sensor//
            if (!fingerprintManager.isHardwareDetected()) {
                // If a fingerprint sensor isn't available, then inform the user that they'll be unable to use your app's fingerprint functionality
                textView.setText("Your device doesn't support fingerprint authentication");
            }
            // Check whether the device has a fingerprint sensor
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT)!= PackageManager.PERMISSION_GRANTED) {
                // If the app doesn't have this permission, then display the following text
                textView.setText("Please enable the fingerprint permission");
            }

            // Check that the user has registered at least one fingerprint
            if (!fingerprintManager.hasEnrolledFingerprints()) {
                // If the user hasn't configured any fingerprints, then display the following message
                textView.setText("No fingerprint configured. Please register at least one fingerprint in your device's settings");
            }

            // Check that the lock screen is secured
            if (!keyguardManager.isKeyguardSecure()) {
                // if the user hasn't secured their lockscreen with a PIN password or pattern, then display the following text
                textView.setText("Please enable lockscreen security in your device's settings");
            } else {
                try {
                    generateKey();
                } catch (FingerprintException e) {
                    e.printStackTrace();
                }

                if (initCipher()) {
                    // if the cipher is initialized successfully, create CryptoObject instance
                    cryptoObject = new FingerprintManager.CryptoObject(cipher);

                    // Referencing the FingerprintHandler class that we'll create in the next section. This class will be responsible
                    // for starting the authentication process and process the authentication process events
                    FingerprintHandler helper = new FingerprintHandler(this);
                    helper.startAuth(fingerprintManager, cryptoObject);
                }
            }
        }

    }





// Create the generateKey method that we'll use to gain access to the android keystore and generate the encryption key

private void generateKey() throws FingerprintException {
    try {
        // Obtain reference to the keystore using the standard Android keystore container identifier("AndroidKeystore")
        keyStore = KeyStore.getInstance("AndroidKeyStore");

        //Generate the key
        keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

        // Initialize an empty KeyStore
        keyStore.load(null);

        // Initialize the KeyGenerator
        keyGenerator.init(new

                //Specify the operation that the key is used for
                KeyGenParameterSpec.Builder(KEY_NAME,
                KeyProperties.PURPOSE_ENCRYPT |
                        KeyProperties.PURPOSE_DECRYPT)
                .setBlockModels(KeyProperties.BLOCK_MODE_CBC)

                // Configure the key so the user has to confirm the identitiy with fingerprint each time they want to use it
                .setUserAuthenticationRequired(true)
                .setEncryptionPaddings(
                        KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .build());

        // Generate the Key
        keyGenerator.generateKey();

    } catch (KeyStoreException
            | NoSuchAlgorithmException
            | NoSuchProviderException
            | InvalidAlgorithmParameterException
            | CertificateException
            | IOException exc) {
        exc.printStackTrace();
        throw new FingerprintException(exc);
    }
}

// Create new method that we'll use to initialize cipher
    public boolean initCipher() {
        try {
            // Obtain cipher instance and configure it with properties required for fingerprint authentication
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {

            // Return false if cipher initialization failed
            return false;
        } catch (KeyStoreException | CertificateException
                | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }
}
    private class FingerprintException extends Exception {
        public FingerprintException(Exception e) {
            super(e);
        }
    }


