package com.indglobal.nizcare.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.indglobal.nizcare.R;

/**
 * Created by readyassist on 12/16/17.
 */

public class BaseAcountFragment extends Fragment{

    LayoutInflater inflater;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        inflater = (LayoutInflater) activity.getLayoutInflater();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_acount_fragment, container, false);


        return view;
    }


}