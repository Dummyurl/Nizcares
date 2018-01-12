package com.indglobal.nizcare.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ProgressBar;
import com.indglobal.nizcare.R;
import com.indglobal.nizcare.commons.RippleView;

/**
 * Created by readyassist on 1/8/18.
 */

public class ProfileActivity extends Activity implements RippleView.OnRippleCompleteListener{

    ProgressBar prgLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profle_activity);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);

    }

    @Override
    public void onComplete(RippleView rippleView) {
        int id = rippleView.getId();

        switch (id){
            case R.id.rplBack:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
