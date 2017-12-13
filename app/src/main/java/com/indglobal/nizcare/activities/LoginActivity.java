package com.indglobal.nizcare.activities;

import android.app.Activity;
import android.os.Bundle;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.commons.RippleView;

/**
 * Created by readyassist on 12/12/17.
 */

public class LoginActivity extends Activity implements RippleView.OnRippleCompleteListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);



    }

    @Override
    public void onComplete(RippleView rippleView) {
        int id = rippleView.getId();

        switch (id){

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
