package com.indglobal.nizcare.commons;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by Android on 3/5/16.
 */
public class IncomingSms extends BroadcastReceiver {

    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(final Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String message = currentMessage.getDisplayMessageBody();

                    final String otp = getVerificationCode(message);

                    Intent myIntent = new Intent("otp");
                    myIntent.putExtra("message",otp);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);


                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }

    }

    private String getVerificationCode(String message) {
        String code = null;
        if (message.contains("15 minutes")){
            int index = message.indexOf(" is ");

            if (index != -1) {
                int start = index + 3;
                int length = 6;
                code = message.substring(start, start + length);
                return code;
            }
        }
        return code;
    }


}