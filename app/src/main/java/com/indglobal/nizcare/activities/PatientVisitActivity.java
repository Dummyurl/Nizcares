package com.indglobal.nizcare.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.indglobal.nizcare.R;
import com.indglobal.nizcare.adapters.ClinicAdapter;
import com.indglobal.nizcare.adapters.TreatmentAdapter;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.commons.CustomRequest;
import com.indglobal.nizcare.commons.RippleView;
import com.indglobal.nizcare.commons.VolleyJSONRequest;
import com.indglobal.nizcare.commons.VolleySingleton;
import com.indglobal.nizcare.model.ClinicItem;
import com.indglobal.nizcare.model.TreatmentItem;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by readyassist on 2/5/18.
 */

public class PatientVisitActivity  extends Activity implements RippleView.OnRippleCompleteListener,View.OnClickListener{

    ProgressBar prgLoading;
    RippleView rplBack,rplAddnextapoint,rplAddprscrptn;

    ScrollView scrlMain;
    ImageView ivPatient;
    TextView tvDone,tvName,tvGndrAge,tvDate;
    LinearLayout llSelectPtnt,llNewPtnt;
    Spinner spinClinic,spinTreatment;
    EditText etComplaint;

    ClinicItem clinicItem;
    ArrayList<ClinicItem> clinicItemArrayList = new ArrayList<>();
    ClinicAdapter clinicAdapter;

    TreatmentItem treatmentItem;
    ArrayList<TreatmentItem> treatmentItemArrayList = new ArrayList<>();
    TreatmentAdapter treatmentAdapter;

    String patient_id="",patient_name,patient_img,patient_age,patient_gender,hospital_id="",visit_date="",treatment="",patient_complaint="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_visit_main);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        rplBack = (RippleView)findViewById(R.id.rplBack);
        rplAddnextapoint = (RippleView)findViewById(R.id.rplAddnextapoint);
        rplAddprscrptn = (RippleView)findViewById(R.id.rplAddprscrptn);
        scrlMain = (ScrollView) findViewById(R.id.scrlMain);
        llSelectPtnt = (LinearLayout)findViewById(R.id.llSelectPtnt);
        llNewPtnt = (LinearLayout) findViewById(R.id.llNewPtnt);

        spinClinic = (Spinner)findViewById(R.id.spinClinic);
        spinTreatment = (Spinner)findViewById(R.id.spinTreatment);
        ivPatient = (ImageView)findViewById(R.id.ivPatient);
        tvName = (TextView)findViewById(R.id.tvName);
        tvGndrAge = (TextView)findViewById(R.id.tvGndrAge);
        tvDone = (TextView)findViewById(R.id.tvDone);
        tvDate = (TextView)findViewById(R.id.tvDate);
        etComplaint = (EditText)findViewById(R.id.etComplaint);


        if (!Comman.isConnectionAvailable(PatientVisitActivity.this)){
            scrlMain.setVisibility(View.GONE);
            Toast.makeText(PatientVisitActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
        }else {
            prgLoading.setVisibility(View.VISIBLE);
            getClinics();
        }

        rplBack.setOnRippleCompleteListener(this);
        tvDone.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        llSelectPtnt.setOnClickListener(this);
        llNewPtnt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.tvDone:
                hospital_id = clinicItemArrayList.get(spinClinic.getSelectedItemPosition()).getHospital_id();
                treatment = treatmentItemArrayList.get(spinTreatment.getSelectedItemPosition()).getName();
                visit_date = tvDate.getText().toString();
                patient_complaint = etComplaint.getText().toString();

                if (!Comman.isConnectionAvailable(PatientVisitActivity.this)){
                    Toast.makeText(PatientVisitActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                }else if (patient_id.equalsIgnoreCase("")){
                    Toast.makeText(PatientVisitActivity.this,getResources().getString(R.string.slctpatnt),Toast.LENGTH_SHORT).show();
                }else if (visit_date.equalsIgnoreCase("")){
                    Toast.makeText(PatientVisitActivity.this,getResources().getString(R.string.nodateslctd),Toast.LENGTH_SHORT).show();
                }else if (hospital_id.equalsIgnoreCase("")){
                    Toast.makeText(PatientVisitActivity.this,getResources().getString(R.string.clinicidnot),Toast.LENGTH_SHORT).show();
                }else if (treatment.equalsIgnoreCase("")){
                    Toast.makeText(PatientVisitActivity.this,getResources().getString(R.string.slcttreatment),Toast.LENGTH_SHORT).show();
                }else if (patient_complaint.equalsIgnoreCase("")){
                    Toast.makeText(PatientVisitActivity.this,getResources().getString(R.string.prvdPtntCompalaint),Toast.LENGTH_SHORT).show();
                }else {
                    prgLoading.setVisibility(View.VISIBLE);
                    addPatntVisit(patient_id,hospital_id,visit_date,treatment,patient_complaint);
                }
                break;

            case R.id.tvDate:
                openCalnderClockDialog();
                break;

            case R.id.llSelectPtnt:
                Intent ii = new Intent(PatientVisitActivity.this,PatientActivity.class);
                startActivityForResult(ii,3);
                break;

            case R.id.llNewPtnt:
                Intent iiAddPtnt = new Intent(PatientVisitActivity.this,AddPatientActivity.class);
                startActivity(iiAddPtnt);
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

            case R.id.rplAddnextapoint:
                Intent iiAddApoint = new Intent(PatientVisitActivity.this,CreateApointActivity.class);
                startActivity(iiAddApoint);
                break;

            case R.id.rplAddprscrptn:
                Intent iiAddPrscrptn = new Intent(PatientVisitActivity.this,AddPrscrptnActivity.class);
                startActivity(iiAddPrscrptn);
                break;
        }
    }

    private void getClinics() {

        String url = getResources().getString(R.string.getClinicsApi);
        String token = Comman.getPreferences(PatientVisitActivity.this,"token");
        url = url+"?token="+token;

        String CLINICSHIT = "get_clinics_hit";
        VolleySingleton.getInstance(PatientVisitActivity.this).cancelRequestInQueue(CLINICSHIT);
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

                        clinicAdapter = new ClinicAdapter(PatientVisitActivity.this,clinicItemArrayList);
                        spinClinic.setAdapter(clinicAdapter);

                        if (!Comman.isConnectionAvailable(PatientVisitActivity.this)){
                            scrlMain.setVisibility(View.GONE);
                            Toast.makeText(PatientVisitActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                        }else {
                            prgLoading.setVisibility(View.VISIBLE);
                            getTreatments();
                        }

                    }else {
                        scrlMain.setVisibility(View.GONE);
                        Toast.makeText(PatientVisitActivity.this,getResources().getString(R.string.citynotfound),Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    scrlMain.setVisibility(View.GONE);
                    Toast.makeText(PatientVisitActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                prgLoading.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PatientVisitActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                scrlMain.setVisibility(View.GONE);
                prgLoading.setVisibility(View.GONE);
            }
        });

        request.setTag(CLINICSHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(PatientVisitActivity.this).addToRequestQueue(request);
    }

    private void getTreatments() {

        String url = getResources().getString(R.string.getTreatmentApi);

        String GETTREATMNTHIT = "get_traetment_hit";
        VolleySingleton.getInstance(PatientVisitActivity.this).cancelRequestInQueue(GETTREATMNTHIT);
        VolleyJSONRequest request = new VolleyJSONRequest(Request.Method.GET, url, null,null, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                try {
                    JSONObject response = new JSONObject(result);
                    boolean isSuccess = response.getBoolean("success");
                    String message  = response.getString("message");

                    if (isSuccess){

                        treatmentItemArrayList.clear();

                        JSONArray data = response.getJSONArray("data");
                        for (int i=0;i<data.length();i++){

                            JSONObject object = data.getJSONObject(i);

                            String master_treatment_id = object.getString("master_treatment_id");
                            String master_treatment_name = object.getString("master_treatment_name");

                            treatmentItem = new TreatmentItem(master_treatment_id,master_treatment_name);
                            treatmentItemArrayList.add(treatmentItem);
                        }

                        treatmentAdapter = new TreatmentAdapter(PatientVisitActivity.this,treatmentItemArrayList);
                        spinTreatment.setAdapter(treatmentAdapter);

                        tvDone.setVisibility(View.VISIBLE);
                        prgLoading.setVisibility(View.GONE);
                        scrlMain.setVisibility(View.VISIBLE);

                    }else {
                        scrlMain.setVisibility(View.GONE);
                        Toast.makeText(PatientVisitActivity.this,message,Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    scrlMain.setVisibility(View.GONE);
                    Toast.makeText(PatientVisitActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                prgLoading.setVisibility(View.GONE);
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PatientVisitActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                prgLoading.setVisibility(View.GONE);
            }
        });

        request.setTag(GETTREATMNTHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(PatientVisitActivity.this).addToRequestQueue(request);
    }

    private void openCalnderClockDialog() {

        int mYear, mMonth, mDay;

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(PatientVisitActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {

                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(PatientVisitActivity.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        Calendar datetime = Calendar.getInstance();
                        datetime.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        datetime.set(Calendar.MONTH,monthOfYear);
                        datetime.set(Calendar.YEAR,year);
                        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        datetime.set(Calendar.MINUTE, minute);

                        Date dropSchdldDate = datetime.getTime();

                        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy");
                        String date = simpleDateFormat.format(dropSchdldDate);
                        tvDate.setText(date);

                    }

                }, mHour, mMinute, false);
                timePickerDialog.show();
            }

        }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

    private void addPatntVisit(String patient_id,String hospital_id,String visit_date,String treatment,String patient_complaint) {

        String url = getResources().getString(R.string.addPtntVstApi);
        String token = Comman.getPreferences(PatientVisitActivity.this,"token");
        url = url+"?token="+token;

        Map<String, String> params = new HashMap<>();
        params.put("patient_id",patient_id);
        params.put("hospital_id",hospital_id);
        params.put("visit_date",visit_date);
        params.put("treatment",treatment);
        params.put("patient_complaint",patient_complaint);

        String ADDPTNTVSTHIT = "add_ptnt_vst_hit";
        VolleySingleton.getInstance(PatientVisitActivity.this).cancelRequestInQueue(ADDPTNTVSTHIT);
        CustomRequest request = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    boolean success = response.getBoolean("success");
                    String msg = response.getString("message");
                    Toast.makeText(PatientVisitActivity.this,msg,Toast.LENGTH_SHORT).show();

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(PatientVisitActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(PatientVisitActivity.this,message,Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Toast.makeText(PatientVisitActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(PatientVisitActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(ADDPTNTVSTHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(PatientVisitActivity.this).addToRequestQueue(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 3) {
            if(resultCode == Activity.RESULT_OK){
                patient_id = data.getStringExtra("patient_id");
                patient_name = data.getStringExtra("patient_name");
                patient_img = data.getStringExtra("patient_img");
                patient_age = data.getStringExtra("patient_age");
                patient_gender = data.getStringExtra("patient_gender");

                Picasso.with(PatientVisitActivity.this).load(patient_img).into(ivPatient);
                tvName.setText(Comman.capitalize(patient_name));
                tvGndrAge.setText(Comman.capitalize(patient_gender)+" * "+patient_age+" Years old");
                tvGndrAge.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

}
