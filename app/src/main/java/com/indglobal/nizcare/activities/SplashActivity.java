package com.indglobal.nizcare.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.commons.Comman;

/**
 * Created by readyassist on 5/6/17.
 */

public class SplashActivity extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;
    ProgressBar prgLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splas_activity);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                String loggedIn = Comman.getPreferences(SplashActivity.this,"loggedIn");

                if (loggedIn.equalsIgnoreCase("1")){
                    Intent mainIntent = new Intent(SplashActivity.this, BaseActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                }else {
                    Intent mainIntent = new Intent(SplashActivity.this, IntroActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                }

            }
        }, SPLASH_DISPLAY_LENGTH);

    }

}
