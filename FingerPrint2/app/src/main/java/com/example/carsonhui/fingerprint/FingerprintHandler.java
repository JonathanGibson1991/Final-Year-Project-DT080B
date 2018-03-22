package com.example.carsonhui.fingerprint;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.Manifest;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;


/**
 * Created by carsonhui on 26/02/2018.
 */

public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    // You should use the CancellationSingal method whenever app can no longer process user input, eg app goes
    // into background & dont use the method, other apps will not be unable to access the touch sensor which includes lock screen

    private CancellationSignal cancellationSignal;
    private Context context;

    public FingerprintHandler(Context mContext) {
            context = mContext;
    }

    // Implement the startAuth method which responsible for starting fingerprint authentication process

    public void startAuth(FingerprintManager mananger, FingerprintManager.CryptoObject cryptoObject) {

        cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mananger.authenticate(cryptoObject, cancellationSignal,0, this, null);
    }

    @Override
    //onAuthenticationError is called when a fatal error has occurred. Provides error code and error message as its parameters

    public void onAuthenticationError(int errMsgId, CharSequence errString) {

        // Going to display the results of fingerprint authentication as series of toasts.
        // Create message to display if an error occurs

        Toast.makeText(context, "Authentication error\n" + errString, Toast.LENGTH_LONG).show();
    }

    @Override

    // onAuthenticationFailed called when fingerprint does not match with any of the fingerprints registered on device

    public void onAuthenticationFailed() {
        Toast.makeText(context, "Authentication failed", Toast.LENGTH_LONG).show();
    }

    @Override

    // onAuthenticationHelp called when non-fatal occurred. Method provides addition information about the error
    // provides user with feedback.
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            Toast.makeText(context, "Authentication help\n" + helpString, Toast.LENGTH_LONG).show();

    } @Override

    //onAuthenticationSucceeded called when fingerprint has been successfully matched to one of the fingerprints
    // stored on the user's device

    public void onAuthenticationSucceeded(
            FingerprintManager.AuthenticationResult result) {

                    Toast.makeText(context, "Success!", Toast.LENGTH_LONG).show();
    }

}
