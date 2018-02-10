package com.indglobal.nizcare.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.indglobal.nizcare.R;
import com.indglobal.nizcare.activities.AddHolidayActivity;
import com.indglobal.nizcare.activities.PatientVisitActivity;
import com.indglobal.nizcare.activities.ProfileActivity;
import com.indglobal.nizcare.activities.SettingActivity;
import com.indglobal.nizcare.adapters.ClinicAdapter;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.commons.CustomRequest;
import com.indglobal.nizcare.commons.VolleyJSONRequest;
import com.indglobal.nizcare.commons.VolleySingleton;
import com.indglobal.nizcare.commons.roundedimageview.RoundedImageView;
import com.indglobal.nizcare.model.ClinicItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
    boolean fromApi = false;

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

        String isInstant = Comman.getPreferences(getActivity(),"isInstant");
        if (isInstant.equalsIgnoreCase("1")){
            swtchAvlbl.setChecked(true);
        }else {
            swtchAvlbl.setChecked(false);
        }

        if (Comman.isConnectionAvailable(getActivity())){
            getWalletBalance();
        }

        swtchAvlbl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!fromApi){
                    if(!Comman.isConnectionAvailable(getActivity())){
                        Toast.makeText(getActivity(),getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                    }else {
                        prgLoading.setVisibility(View.VISIBLE);
                        setOnlineOffline(b);
                    }
                }else {
                    fromApi = false;
                }

            }
        });


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

    private void getWalletBalance() {

        String url = getResources().getString(R.string.getWalletBlncApi);
        String token = Comman.getPreferences(getActivity(),"token");
        url = url+"?token="+token;

        String WLLTAMNTHIT = "get_wltamnt_hit";
        VolleySingleton.getInstance(getActivity()).cancelRequestInQueue(WLLTAMNTHIT);
        VolleyJSONRequest request = new VolleyJSONRequest(Request.Method.GET, url, null, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                try {
                    JSONObject response  = new JSONObject(result);
                    boolean isSuccess = response.getBoolean("success");

                    if (isSuccess){

                        JSONObject data = response.getJSONObject("data");
                        String amount = data.getString("amount");

                        tvWltBlnc.setText(amount+"/-");
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

                prgLoading.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                prgLoading.setVisibility(View.GONE);
            }
        });

        request.setTag(WLLTAMNTHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void setOnlineOffline(final boolean isAvailable) {
        String type = "0";
        if (isAvailable){
            type = "1";
        }

        String url = getResources().getString(R.string.instantonlineApi);
        String token = Comman.getPreferences(getActivity(),"token");
        url = url+"?token="+token;

        Map<String, String> params = new HashMap<>();
        params.put("type",type);

        String ADDINSTNTHIT = "add_instnt_hit";
        VolleySingleton.getInstance(getActivity()).cancelRequestInQueue(ADDINSTNTHIT);
        final String finalType = type;
        CustomRequest request = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    boolean success = response.getBoolean("success");
                    String msg = response.getString("message");

                    if (success){
                        Comman.setPreferences(getActivity(),"isInstant", finalType);
                    }else {
                        fromApi = true;
                        swtchAvlbl.setChecked(!isAvailable);
                    }

                    Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();

                }catch (Exception e){
                    e.printStackTrace();
                    fromApi = true;
                    swtchAvlbl.setChecked(!isAvailable);
                    Toast.makeText(getActivity(),getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                prgLoading.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse.data!=null){
                    try {

                        String jsonString = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        JSONObject errObject = new JSONObject(jsonString);

                        String message = errObject.getString("message");

                        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Toast.makeText(getActivity(),getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getActivity(),getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                fromApi = true;
                swtchAvlbl.setChecked(!isAvailable);
                prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(ADDINSTNTHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }
}