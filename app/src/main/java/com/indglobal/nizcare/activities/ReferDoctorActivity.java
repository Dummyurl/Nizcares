package com.indglobal.nizcare.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.indglobal.nizcare.R;
import com.indglobal.nizcare.adapters.BankAdapter;
import com.indglobal.nizcare.adapters.PatientAdapter;
import com.indglobal.nizcare.adapters.ReferDoctAdapter;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.commons.RippleView;
import com.indglobal.nizcare.commons.VolleyJSONRequest;
import com.indglobal.nizcare.commons.VolleySingleton;
import com.indglobal.nizcare.model.BankItem;
import com.indglobal.nizcare.model.ReferDocImgItem;
import com.indglobal.nizcare.model.ReferDoctorItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by readyassist on 1/29/18.
 */

public class ReferDoctorActivity extends Activity implements RippleView.OnRippleCompleteListener{

    public static ProgressBar prgLoading;
    RippleView rplBack;
    public static TextView tvRefer;
    LinearLayout llMain;
    EditText etSearch;
    RecyclerView rvDoctrs;

    ReferDoctorItem referDoctorItem;
    ArrayList<ReferDoctorItem> referDoctorItemArrayList = new ArrayList<>();
    ReferDoctAdapter referDoctAdapter;
    ReferDocImgItem referDocImgItem;

    public static String appointment_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refer_doctr_main);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        rplBack = (RippleView)findViewById(R.id.rplBack);
        tvRefer = (TextView)findViewById(R.id.tvRefer);
        llMain = (LinearLayout)findViewById(R.id.llMain);
        etSearch = (EditText)findViewById(R.id.etSearch);
        rvDoctrs  = (RecyclerView)findViewById(R.id.rvDoctrs);

        Intent ii = getIntent();
        appointment_id = ii.getStringExtra("appointment_id");

        referDoctAdapter = new ReferDoctAdapter(ReferDoctorActivity.this, referDoctorItemArrayList, null);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        rvDoctrs.setLayoutManager(layoutManager);
        rvDoctrs.setAdapter(referDoctAdapter);

        if (!Comman.isConnectionAvailable(ReferDoctorActivity.this)){
            llMain.setVisibility(View.GONE);
            Toast.makeText(ReferDoctorActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
        }else {
            prgLoading.setVisibility(View.VISIBLE);
            getDoctors();
        }

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                referDoctAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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

    private void getDoctors() {

        String url = getResources().getString(R.string.getDoctrListApi);
        String token = Comman.getPreferences(ReferDoctorActivity.this,"token");
        url = url+"?token="+token;

        String GETDOCTRLISTHIT = "get_doctrs_hit";
        VolleySingleton.getInstance(ReferDoctorActivity.this).cancelRequestInQueue(GETDOCTRLISTHIT);
        VolleyJSONRequest request = new VolleyJSONRequest(Request.Method.GET, url,null, null,new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                try {
                    JSONObject response  = new JSONObject(result);
                    boolean success = response.getBoolean("success");

                    if (success){

                        referDoctorItemArrayList.clear();

                        JSONArray data = response.getJSONArray("data");
                        for (int i=0;i<data.length();i++){

                            JSONObject object = data.getJSONObject(i);

                            String doctor_id = object.getString("doctor_id");
                            String prefix = object.getString("prefix");
                            String name = object.getString("name");
                            String speciality = object.getString("speciality");
                            String degree = object.getString("degree");
                            String total_reviews = object.getString("total_reviews");
                            String rating = object.getString("rating");
                            String country_code = object.getString("country_code");
                            String mobile_no = object.getString("mobile_no");
                            String profile_pic = object.getString("profile_pic");
                            String profile_pic_thumb = object.getString("profile_pic_thumb");
                            String hospital_address = object.getString("hospital_address");
                            String gender = object.getString("gender");
                            String experience = object.getString("experience");
                            String location = object.getString("location");
                            String online_status = object.getString("online_status");

                            ArrayList<ReferDocImgItem> referDocImgItems = new ArrayList<>();
                            JSONArray arrayImages = object.getJSONArray("hospital_images");
                            for (int j=0;j<arrayImages.length();j++){

                                JSONObject jsonObject = arrayImages.getJSONObject(j);

                                String id = jsonObject.getString("id");
                                String media = jsonObject.getString("media");
                                String media_thumb = jsonObject.getString("media_thumb");

                                referDocImgItem = new ReferDocImgItem(id,media,media_thumb);
                                referDocImgItems.add(referDocImgItem);
                            }

                            referDoctorItem = new ReferDoctorItem(doctor_id,prefix,name,speciality,degree,total_reviews,rating,country_code,
                                    mobile_no,profile_pic,profile_pic_thumb,hospital_address,gender,experience,location,
                                    online_status,"0",referDocImgItems);
                            referDoctorItemArrayList.add(referDoctorItem);

                        }

                        referDoctAdapter.notifyDataSetChanged();

                        llMain.setVisibility(View.VISIBLE);
                        prgLoading.setVisibility(View.GONE);


                    }else {
                        String message = response.getString("message");
                        Toast.makeText(ReferDoctorActivity.this,message,Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(ReferDoctorActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                prgLoading.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    if (error.networkResponse.data!=null){
                        String jsonString = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        JSONObject errObject = new JSONObject(jsonString);

                        String message = errObject.getString("message");

                        Toast.makeText(ReferDoctorActivity.this,message,Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(ReferDoctorActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(ReferDoctorActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }
                prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(GETDOCTRLISTHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(ReferDoctorActivity.this).addToRequestQueue(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

}
