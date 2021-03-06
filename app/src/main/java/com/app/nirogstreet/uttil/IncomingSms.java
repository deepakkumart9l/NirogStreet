package com.app.nirogstreet.uttil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by Preeti on 18-08-2017.
 */

public class IncomingSms extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    if (!currentMessage.getDisplayMessageBody().contains("of your account at")) {

                        String message = currentMessage.getDisplayMessageBody().split(" ")[0];

                        message = message.substring(0, message.length());
                        Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);

                        Intent myIntent = new Intent("otp");
                        myIntent.putExtra("message", message);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);
                        // Show Alert
                    } else if (currentMessage.getDisplayMessageBody().contains("OTP for online")) {
                        String message = currentMessage.getDisplayMessageBody().split(",")[1];
                        String otp1 = message.split(" ")[1];
                        Intent myIntent = new Intent("otp");
                        myIntent.putExtra("message", otp1);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);
                        break;
                    }
                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);

        }
    }
}
