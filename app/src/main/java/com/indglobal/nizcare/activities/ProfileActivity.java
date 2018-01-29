package com.indglobal.nizcare.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.commons.RippleView;
import com.indglobal.nizcare.commons.roundedimageview.RoundedImageView;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * Created by readyassist on 1/8/18.
 */

public class ProfileActivity extends Activity implements RippleView.OnRippleCompleteListener{

    RippleView rplBack;
    ProgressBar prgLoading;

    LinearLayout llMain,llGetDirctn,llCall;
    ViewPager pager;
    CirclePageIndicator indicator;
    RoundedImageView rivPrfl;
    TextView tvOnline,tvName,tvDegree,tvMedicalCntr,tvLocation,tvExp,tvCnsltnFee,tvLangs,tvWeblnk,
            tvTimeings,tvTapToviewTmngs,tvEditPrfl;

    RecyclerView rvEducation,rvAwards,rvMembrships,rvClinics,rvTreatmnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profle_activity);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        rplBack = (RippleView)findViewById(R.id.rplBack);
        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);

        llMain = (LinearLayout)findViewById(R.id.llMain);
        pager = (ViewPager)findViewById(R.id.pager);
        indicator = (CirclePageIndicator)findViewById(R.id.indicator);
        rivPrfl = (RoundedImageView)findViewById(R.id.rivPrfl);
        tvOnline = (TextView)findViewById(R.id.tvOnline);
        tvName = (TextView)findViewById(R.id.tvName);
        tvDegree = (TextView)findViewById(R.id.tvDegree);
        tvMedicalCntr = (TextView)findViewById(R.id.tvMedicalCntr);
        tvLocation = (TextView)findViewById(R.id.tvLocation);
        llGetDirctn = (LinearLayout)findViewById(R.id.llGetDirctn);
        llCall = (LinearLayout)findViewById(R.id.llCall);
        tvExp = (TextView)findViewById(R.id.tvExp);
        tvCnsltnFee = (TextView)findViewById(R.id.tvCnsltnFee);
        tvLangs = (TextView)findViewById(R.id.tvLangs);
        tvWeblnk = (TextView)findViewById(R.id.tvWeblnk);
        tvTimeings = (TextView)findViewById(R.id.tvTimeings);
        tvTapToviewTmngs = (TextView)findViewById(R.id.tvTapToviewTmngs);
        rvEducation = (RecyclerView)findViewById(R.id.rvEducation);
        rvAwards = (RecyclerView)findViewById(R.id.rvAwards);
        rvMembrships = (RecyclerView)findViewById(R.id.rvMembrships);
        rvClinics = (RecyclerView)findViewById(R.id.rvClinics);
        rvTreatmnt = (RecyclerView)findViewById(R.id.rvTreatmnt);
        tvEditPrfl = (TextView)findViewById(R.id.tvEditPrfl);


        rplBack.setOnRippleCompleteListener(this);
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
