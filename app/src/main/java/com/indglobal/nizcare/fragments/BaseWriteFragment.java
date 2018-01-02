package com.indglobal.nizcare.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.indglobal.nizcare.R;

/**
 * Created by readyassist on 12/16/17.
 */

public class BaseWriteFragment extends Fragment implements View.OnClickListener{

    LayoutInflater inflater;
    TextView tvTabQstns,tvTabEnqrs,tvTabHlth,tvTabQA,tvIndQstns,tvIndEnqrs,tvIndHelth,tvIndQa;
    LinearLayout llQstns,llHealthfeed;
    RecyclerView rvEnquirs;
    RelativeLayout rlQA;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        inflater = (LayoutInflater) activity.getLayoutInflater();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_write_fragment, container, false);

        tvTabQstns = (TextView)view.findViewById(R.id.tvTabQstns);
        tvTabEnqrs = (TextView)view.findViewById(R.id.tvTabEnqrs);
        tvTabHlth = (TextView)view.findViewById(R.id.tvTabHlth);
        tvTabQA = (TextView)view.findViewById(R.id.tvTabQA);
        tvIndQstns = (TextView)view.findViewById(R.id.tvIndQstns);
        tvIndEnqrs = (TextView)view.findViewById(R.id.tvIndEnqrs);
        tvIndHelth = (TextView)view.findViewById(R.id.tvIndHelth);
        tvIndQa = (TextView)view.findViewById(R.id.tvIndQa);

        llQstns = (LinearLayout) view.findViewById(R.id.llQstns);
        llHealthfeed = (LinearLayout) view.findViewById(R.id.llHealthfeed);
        rvEnquirs = (RecyclerView)view.findViewById(R.id.rvEnquirs);
        rlQA = (RelativeLayout)view.findViewById(R.id.rlQA);

        tvTabQstns.setOnClickListener(this);
        tvTabEnqrs.setOnClickListener(this);
        tvTabHlth.setOnClickListener(this);
        tvTabQA.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){

            case R.id.tvTabQstns:
                rlQA.setVisibility(View.GONE);
                rvEnquirs.setVisibility(View.GONE);
                llHealthfeed.setVisibility(View.GONE);
                llQstns.setVisibility(View.VISIBLE);
                tvTabQA.setTextColor(getResources().getColor(R.color.lightBlack));
                tvIndQa.setBackgroundColor(getResources().getColor(R.color.lightGray));
                tvTabHlth.setTextColor(getResources().getColor(R.color.lightBlack));
                tvIndHelth.setBackgroundColor(getResources().getColor(R.color.lightGray));
                tvTabEnqrs.setTextColor(getResources().getColor(R.color.lightBlack));
                tvIndEnqrs.setBackgroundColor(getResources().getColor(R.color.lightGray));
                tvTabQstns.setTextColor(getResources().getColor(R.color.lightGreen));
                tvIndQstns.setBackgroundColor(getResources().getColor(R.color.lightGreen));
                break;

            case R.id.tvTabEnqrs:
                rlQA.setVisibility(View.GONE);
                llHealthfeed.setVisibility(View.GONE);
                llQstns.setVisibility(View.GONE);
                rvEnquirs.setVisibility(View.VISIBLE);
                tvTabQA.setTextColor(getResources().getColor(R.color.lightBlack));
                tvIndQa.setBackgroundColor(getResources().getColor(R.color.lightGray));
                tvTabHlth.setTextColor(getResources().getColor(R.color.lightBlack));
                tvIndHelth.setBackgroundColor(getResources().getColor(R.color.lightGray));
                tvTabEnqrs.setTextColor(getResources().getColor(R.color.lightGreen));
                tvIndEnqrs.setBackgroundColor(getResources().getColor(R.color.lightGreen));
                tvTabQstns.setTextColor(getResources().getColor(R.color.lightBlack));
                tvIndQstns.setBackgroundColor(getResources().getColor(R.color.lightGray));
                break;

            case R.id.tvTabHlth:
                rlQA.setVisibility(View.GONE);
                llQstns.setVisibility(View.GONE);
                rvEnquirs.setVisibility(View.GONE);
                llHealthfeed.setVisibility(View.VISIBLE);
                tvTabQA.setTextColor(getResources().getColor(R.color.lightBlack));
                tvIndQa.setBackgroundColor(getResources().getColor(R.color.lightGray));
                tvTabHlth.setTextColor(getResources().getColor(R.color.lightGreen));
                tvIndHelth.setBackgroundColor(getResources().getColor(R.color.lightGreen));
                tvTabEnqrs.setTextColor(getResources().getColor(R.color.lightBlack));
                tvIndEnqrs.setBackgroundColor(getResources().getColor(R.color.lightGray));
                tvTabQstns.setTextColor(getResources().getColor(R.color.lightBlack));
                tvIndQstns.setBackgroundColor(getResources().getColor(R.color.lightGray));
                break;

            case R.id.tvTabQA:
                llQstns.setVisibility(View.GONE);
                rvEnquirs.setVisibility(View.GONE);
                llHealthfeed.setVisibility(View.GONE);
                rlQA.setVisibility(View.VISIBLE);
                tvTabQA.setTextColor(getResources().getColor(R.color.lightGreen));
                tvIndQa.setBackgroundColor(getResources().getColor(R.color.lightGreen));
                tvTabHlth.setTextColor(getResources().getColor(R.color.lightBlack));
                tvIndHelth.setBackgroundColor(getResources().getColor(R.color.lightGray));
                tvTabEnqrs.setTextColor(getResources().getColor(R.color.lightBlack));
                tvIndEnqrs.setBackgroundColor(getResources().getColor(R.color.lightGray));
                tvTabQstns.setTextColor(getResources().getColor(R.color.lightBlack));
                tvIndQstns.setBackgroundColor(getResources().getColor(R.color.lightGray));
                break;
        }
    }
}