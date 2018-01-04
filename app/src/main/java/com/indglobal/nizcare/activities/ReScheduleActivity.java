package com.indglobal.nizcare.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.indglobal.nizcare.R;
import com.indglobal.nizcare.adapters.ClinicAdapter;
import com.indglobal.nizcare.adapters.ConsultationAdapter;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.commons.CustomRequest;
import com.indglobal.nizcare.commons.RippleView;
import com.indglobal.nizcare.commons.VolleyJSONRequest;
import com.indglobal.nizcare.commons.VolleySingleton;
import com.indglobal.nizcare.model.ClinicItem;
import com.indglobal.nizcare.model.ConsultationItem;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by readyassist on 1/4/18.
 */

public class ReScheduleActivity extends Activity implements RippleView.OnRippleCompleteListener{

    ProgressBar prgLoading;
    RelativeLayout rlMain;
    RippleView rplBack,rplReSchdld;
    Spinner spinClinic,spinCnsltnts;

    ImageView ivPatient;
    TextView tvName,tvGndrAge;
    RecyclerView rvDates,rvTimings;

    ClinicItem clinicItem;
    ArrayList<ClinicItem> clinicItemArrayList = new ArrayList<>();
    ClinicAdapter clinicAdapter;

    ConsultationItem consultationItem;
    ArrayList<ConsultationItem> consultationItemArrayList = new ArrayList<>();
    ConsultationAdapter consultationAdapter;

    String patient_name,patient_img,patient_age,patient_gender,appointment_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reschdl_apoint_main);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        rplBack = (RippleView)findViewById(R.id.rplBack);
        rplReSchdld = (RippleView)findViewById(R.id.rplReSchdld);
        rlMain = (RelativeLayout)findViewById(R.id.rlMain);
        spinClinic = (Spinner)findViewById(R.id.spinClinic);
        spinCnsltnts = (Spinner)findViewById(R.id.spinCnsltnts);
        ivPatient = (ImageView)findViewById(R.id.ivPatient);
        tvName = (TextView)findViewById(R.id.tvName);
        tvGndrAge = (TextView)findViewById(R.id.tvGndrAge);
        rvDates = (RecyclerView)findViewById(R.id.rvDates);
        rvTimings = (RecyclerView)findViewById(R.id.rvTimings);

        Intent ii = getIntent();
        patient_name = ii.getStringExtra("name");
        patient_img = ii.getStringExtra("image");
        patient_age = ii.getStringExtra("age");
        patient_gender = ii.getStringExtra("gender");
        appointment_id = ii.getStringExtra("apoint_id");

        Picasso.with(ReScheduleActivity.this).load(patient_img).into(ivPatient);
        tvName.setText(Comman.capitalize(patient_name));
        tvGndrAge.setText(Comman.capitalize(patient_gender)+" * "+patient_age+" Years old");

        if (!Comman.isConnectionAvailable(ReScheduleActivity.this)){
            rlMain.setVisibility(View.GONE);
            Toast.makeText(ReScheduleActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
        }else {
            prgLoading.setVisibility(View.VISIBLE);
            getClinics();
        }

        spinClinic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String hospital_id = clinicItemArrayList.get(position).getHospital_id();
                if (!Comman.isConnectionAvailable(ReScheduleActivity.this)){
                    Toast.makeText(ReScheduleActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                }else {
                    prgLoading.setVisibility(View.VISIBLE);
                    getConsultations(hospital_id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rplBack.setOnRippleCompleteListener(this);
        rplReSchdld.setOnRippleCompleteListener(this);
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

    private void getClinics() {

        String url = getResources().getString(R.string.getClinicsApi);
        String token = Comman.getPreferences(ReScheduleActivity.this,"token");
        url = url+"?token="+token;

        String CLINICSHIT = "get_clinics_hit";
        VolleySingleton.getInstance(ReScheduleActivity.this).cancelRequestInQueue(CLINICSHIT);
        VolleyJSONRequest request = new VolleyJSONRequest(Request.Method.GET, url, null, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                try {
                    JSONObject response  = new JSONObject(result);
                    boolean isSuccess = response.getBoolean("success");

                    if (isSuccess){

                        clinicItemArrayList.clear();

                        JSONArray data = response.getJSONArray("data");
                        for (int i=0;i<data.length();i++){

                            JSONObject object = data.getJSONObject(i);

                            String hospital_id = object.getString("hospital_id");
                            String logo = object.getString("logo");
                            String logo_thumb = object.getString("logo_thumb");
                            String name = object.getString("name");
                            String address = object.getString("address");
                            String consultation_fees = object.getString("consultation_fees");

                            clinicItem = new ClinicItem(hospital_id,logo,logo_thumb,name,address,consultation_fees);
                            clinicItemArrayList.add(clinicItem);
                        }

                        clinicAdapter = new ClinicAdapter(ReScheduleActivity.this,clinicItemArrayList);
                        spinClinic.setAdapter(clinicAdapter);

                        prgLoading.setVisibility(View.GONE);
                        rlMain.setVisibility(View.VISIBLE);

                    }else {
                        rlMain.setVisibility(View.GONE);
                        Toast.makeText(ReScheduleActivity.this,getResources().getString(R.string.citynotfound),Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    rlMain.setVisibility(View.GONE);
                    Toast.makeText(ReScheduleActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                prgLoading.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ReScheduleActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                rlMain.setVisibility(View.GONE);
                prgLoading.setVisibility(View.GONE);
            }
        });

        request.setTag(CLINICSHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(ReScheduleActivity.this).addToRequestQueue(request);
    }

    private void getConsultations(String hospital_id) {

        String url = getResources().getString(R.string.getConsltnsApi);
        String token = Comman.getPreferences(ReScheduleActivity.this,"token");
        url = url+"?token="+token;

        Map<String, String> params = new HashMap<>();
        params.put("hospital_id",hospital_id);

        String CNSLTNTSHIT = "get_cnsltnt_hit";
        VolleySingleton.getInstance(ReScheduleActivity.this).cancelRequestInQueue(CNSLTNTSHIT);
        CustomRequest request = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean isSuccess = response.getBoolean("success");
                    String message  = response.getString("message");

                    if (isSuccess){

                        consultationItemArrayList.clear();

                        JSONArray data = response.getJSONArray("data");
                        for (int i=0;i<data.length();i++){

                            JSONObject object = data.getJSONObject(i);

                            String consultant_id = object.getString("consultant_id");
                            String name = object.getString("name");

                            consultationItem = new ConsultationItem(consultant_id,name);
                            consultationItemArrayList.add(consultationItem);
                        }

                        consultationAdapter = new ConsultationAdapter(ReScheduleActivity.this,consultationItemArrayList);
                        spinCnsltnts.setAdapter(consultationAdapter);

                        prgLoading.setVisibility(View.GONE);

                    }else {
                        Toast.makeText(ReScheduleActivity.this,message,Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(ReScheduleActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                prgLoading.setVisibility(View.GONE);
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ReScheduleActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                prgLoading.setVisibility(View.GONE);
            }
        });

        request.setTag(CNSLTNTSHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(ReScheduleActivity.this).addToRequestQueue(request);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
