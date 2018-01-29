package com.indglobal.nizcare.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.indglobal.nizcare.R;
import com.indglobal.nizcare.commons.RippleView;

/**
 * Created by readyassist on 1/25/18.
 */

public class SettingActivity extends Activity implements RippleView.OnRippleCompleteListener,View.OnClickListener{

    ProgressBar prgLoading;
    RippleView rplBack;

    LinearLayout llMyClinics,llMyHolidays,llCnsltnFee,llSmsEmail,llSubcrptn,llBankDetails,llSignature;
    TextView tvHelp,tvCntctus,tvRateus,tvLeaveFdbk,tvRedeem,tvLangsStng,tvLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_main);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        rplBack = (RippleView)findViewById(R.id.rplBack);

        llMyClinics = (LinearLayout)findViewById(R.id.llMyClinics);
        llMyHolidays = (LinearLayout)findViewById(R.id.llMyHolidays);
        llCnsltnFee = (LinearLayout)findViewById(R.id.llCnsltnFee);
        llSmsEmail = (LinearLayout)findViewById(R.id.llSmsEmail);
        llSubcrptn = (LinearLayout)findViewById(R.id.llSubcrptn);
        llBankDetails = (LinearLayout)findViewById(R.id.llBankDetails);
        llSignature = (LinearLayout)findViewById(R.id.llSignature);

        tvHelp = (TextView)findViewById(R.id.tvHelp);
        tvCntctus = (TextView)findViewById(R.id.tvCntctus);
        tvRateus = (TextView)findViewById(R.id.tvRateus);
        tvLeaveFdbk = (TextView)findViewById(R.id.tvLeaveFdbk);
        tvRedeem = (TextView)findViewById(R.id.tvRedeem);
        tvLangsStng = (TextView)findViewById(R.id.tvLangsStng);
        tvLogout = (TextView)findViewById(R.id.tvLogout);

        rplBack.setOnRippleCompleteListener(this);
        llMyClinics.setOnClickListener(this);
        llBankDetails.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.llBankDetails:
                Intent iiBank = new Intent(SettingActivity.this,BankDtlActivity.class);
                startActivity(iiBank);
                break;

            case R.id.llMyClinics:
                Intent iiClinics = new Intent(SettingActivity.this,MyClinicsActivity.class);
                startActivity(iiClinics);
                break;
        }

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
