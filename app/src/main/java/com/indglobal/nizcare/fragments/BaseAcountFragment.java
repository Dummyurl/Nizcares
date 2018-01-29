package com.indglobal.nizcare.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.activities.ProfileActivity;
import com.indglobal.nizcare.activities.SettingActivity;
import com.indglobal.nizcare.commons.roundedimageview.RoundedImageView;

/**
 * Created by readyassist on 12/16/17.
 */

public class BaseAcountFragment extends Fragment implements View.OnClickListener{

    LayoutInflater inflater;

    ProgressBar prgLoading;
    LinearLayout llMain,llWallet,llReprts,llStore,llMyPrdct,llPckgs,llMangOrdr,llBookrmark,
            llMyHlthTip, llMyAnswrs,llAdvertise,llNtfctns,llRfrels,llFeedback,llRefrlCode,llStngs,
            llFacebook, llTwitter,llYoutube;
    TextView tvAvlbl,tvDocName,tvViewPrfl,tvWltBlnc,tvMngOrdrCount,tvMyAnswrCount;
    Switch swtchAvlbl;
    RoundedImageView ivDoctr;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        inflater = (LayoutInflater) activity.getLayoutInflater();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_acount_fragment, container, false);

        prgLoading = (ProgressBar)view.findViewById(R.id.prgLoading);
        llMain = (LinearLayout)view.findViewById(R.id.llMain);
        tvAvlbl = (TextView)view.findViewById(R.id.tvAvlbl);
        swtchAvlbl = (Switch)view.findViewById(R.id.swtchAvlbl);
        ivDoctr = (RoundedImageView)view.findViewById(R.id.ivDoctr);
        tvDocName = (TextView)view.findViewById(R.id.tvDocName);
        tvViewPrfl = (TextView)view.findViewById(R.id.tvViewPrfl);
        tvWltBlnc = (TextView)view.findViewById(R.id.tvWltBlnc);
        tvMngOrdrCount = (TextView)view.findViewById(R.id.tvMngOrdrCount);
        tvMyAnswrCount = (TextView)view.findViewById(R.id.tvMyAnswrCount);
        llWallet = (LinearLayout)view.findViewById(R.id.llWallet);
        llReprts = (LinearLayout)view.findViewById(R.id.llReprts);
        llStore = (LinearLayout)view.findViewById(R.id.llStore);
        llMyPrdct = (LinearLayout)view.findViewById(R.id.llMyPrdct);
        llPckgs = (LinearLayout)view.findViewById(R.id.llPckgs);
        llMangOrdr = (LinearLayout)view.findViewById(R.id.llMangOrdr);
        llBookrmark = (LinearLayout)view.findViewById(R.id.llBookrmark);
        llMyHlthTip = (LinearLayout)view.findViewById(R.id.llMyHlthTip);
        llMyAnswrs = (LinearLayout)view.findViewById(R.id.llMyAnswrs);
        llAdvertise = (LinearLayout)view.findViewById(R.id.llAdvertise);
        llNtfctns = (LinearLayout)view.findViewById(R.id.llNtfctns);
        llRfrels = (LinearLayout)view.findViewById(R.id.llRfrels);
        llFeedback = (LinearLayout)view.findViewById(R.id.llFeedback);
        llRefrlCode = (LinearLayout)view.findViewById(R.id.llRefrlCode);
        llStngs = (LinearLayout)view.findViewById(R.id.llStngs);
        llFacebook = (LinearLayout)view.findViewById(R.id.llFacebook);
        llTwitter = (LinearLayout)view.findViewById(R.id.llTwitter);
        llYoutube = (LinearLayout)view.findViewById(R.id.llYoutube);

        tvViewPrfl.setOnClickListener(this);
        llStngs.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.tvViewPrfl:
                Intent ii = new Intent(getActivity(),ProfileActivity.class);
                startActivity(ii);
                break;

            case R.id.llStngs:
                Intent iiSetng = new Intent(getActivity(),SettingActivity.class);
                startActivity(iiSetng);
                break;
        }
    }
}