package com.indglobal.nizcare.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
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
import com.indglobal.nizcare.adapters.AutoCmpleteAdapter;
import com.indglobal.nizcare.adapters.DosageTypeAdapter;
import com.indglobal.nizcare.adapters.MedicineAdapter;
import com.indglobal.nizcare.adapters.PatientAdapter;
import com.indglobal.nizcare.adapters.TipAdapter;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.commons.CustomRequest;
import com.indglobal.nizcare.commons.RippleView;
import com.indglobal.nizcare.commons.VolleyJSONRequest;
import com.indglobal.nizcare.commons.VolleySingleton;
import com.indglobal.nizcare.model.DosageTypeItem;
import com.indglobal.nizcare.model.MedicineItem;
import com.indglobal.nizcare.model.MedicineNameItem;
import com.indglobal.nizcare.model.PatientItem;
import com.indglobal.nizcare.model.TipItem;
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
 * Created by readyassist on 2/7/18.
 */

public class AddTipActivity extends Activity implements RippleView.OnRippleCompleteListener,View.OnClickListener{

    ProgressBar prgLoading;
    RippleView rplBack;

    ScrollView scrlMain;
    TextView tvDone,tvAddPatnt,tvDate;
    Spinner spinTipPlans;
    RecyclerView rvPatients;

    TipItem tipItem;
    ArrayList<TipItem> tipItemArrayList = new ArrayList<>();
    TipAdapter tipAdapter;

    PatientItem patientItem;
    ArrayList<PatientItem> patientItemArrayList = new ArrayList<>();
    PatientAdapter patientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_tip_main);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        rplBack = (RippleView)findViewById(R.id.rplBack);
        scrlMain = (ScrollView) findViewById(R.id.scrlMain);

        spinTipPlans = (Spinner)findViewById(R.id.spinTipPlans);
        tvDone = (TextView)findViewById(R.id.tvDone);
        tvAddPatnt = (TextView)findViewById(R.id.tvAddPatnt);
        tvDate = (TextView)findViewById(R.id.tvDate);
        rvPatients = (RecyclerView)findViewById(R.id.rvPatients);

        if (!Comman.isConnectionAvailable(AddTipActivity.this)){
            scrlMain.setVisibility(View.GONE);
            Toast.makeText(AddTipActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
        }else {
            prgLoading.setVisibility(View.VISIBLE);
            getTipPlans();
        }


        rplBack.setOnRippleCompleteListener(this);
        tvDone.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        tvAddPatnt.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.tvDone:

                String tip_plan_id = "";
                if (tipItemArrayList.size()!=0){
                    tip_plan_id = tipItemArrayList.get(spinTipPlans.getSelectedItemPosition()).getTip_plan_id();
                }

                String plan_start_date = tvDate.getText().toString();

                if (!Comman.isConnectionAvailable(AddTipActivity.this)){
                    Toast.makeText(AddTipActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                }else if (patientItemArrayList.size()==0){
                    Toast.makeText(AddTipActivity.this,getResources().getString(R.string.addPatients),Toast.LENGTH_SHORT).show();
                }else if (tip_plan_id.equalsIgnoreCase("")){
                    Toast.makeText(AddTipActivity.this,getResources().getString(R.string.slcttiplans),Toast.LENGTH_SHORT).show();
                }else if (plan_start_date.equalsIgnoreCase("")){
                    Toast.makeText(AddTipActivity.this,getResources().getString(R.string.addplanstartdate),Toast.LENGTH_SHORT).show();
                }else {
                    prgLoading.setVisibility(View.VISIBLE);
                    JSONArray patient_id = new JSONArray();
                    try{
                        for (int i=0;i<patientItemArrayList.size();i++){
                            PatientItem patientItem = patientItemArrayList.get(i);
                            patient_id.put(patientItem.getId());
                        }

                        addTipPLans(patient_id,tip_plan_id,plan_start_date);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
                break;

            case R.id.tvAddPatnt:
                Intent ii = new Intent(AddTipActivity.this,PatientActivity.class);
                startActivityForResult(ii,3);
                break;

            case R.id.tvDate:
                openCalnderClockDialog();
                break;
        }
    }

    private void openCalnderClockDialog() {

        int mYear, mMonth, mDay;

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(AddTipActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {

                Calendar datetime = Calendar.getInstance();
                datetime.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                datetime.set(Calendar.MONTH,monthOfYear);
                datetime.set(Calendar.YEAR,year);

                Date dropSchdldDate = datetime.getTime();

                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy");
                String date = simpleDateFormat.format(dropSchdldDate);
                tvDate.setText(date);
            }

        }, mYear, mMonth, mDay);
        datePickerDialog.show();

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


    private void getTipPlans() {

        String url = getResources().getString(R.string.getTipPlansApi);

        String TIPPLANSHIT = "get_tip_plans_hit";
        VolleySingleton.getInstance(AddTipActivity.this).cancelRequestInQueue(TIPPLANSHIT);
        VolleyJSONRequest request = new VolleyJSONRequest(Request.Method.GET, url, null, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                try {
                    JSONObject response  = new JSONObject(result);
                    boolean isSuccess = response.getBoolean("success");

                    if (isSuccess){

                        tipItemArrayList.clear();

                        JSONArray data = response.getJSONArray("data");
                        for (int i=0;i<data.length();i++){

                            JSONObject object = data.getJSONObject(i);

                            String tip_plan_id = object.getString("tip_plan_id");
                            String tip_name = object.getString("tip_name");

                            tipItem = new TipItem(tip_plan_id,tip_name);
                            tipItemArrayList.add(tipItem);
                        }

                        tipAdapter = new TipAdapter(AddTipActivity.this,tipItemArrayList);
                        spinTipPlans.setAdapter(tipAdapter);

                        prgLoading.setVisibility(View.GONE);
                        tvDone.setVisibility(View.VISIBLE);
                        scrlMain.setVisibility(View.VISIBLE);

                    }else {
                        scrlMain.setVisibility(View.GONE);
                        Toast.makeText(AddTipActivity.this,getResources().getString(R.string.citynotfound),Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    scrlMain.setVisibility(View.GONE);
                    Toast.makeText(AddTipActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                prgLoading.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddTipActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                scrlMain.setVisibility(View.GONE);
                prgLoading.setVisibility(View.GONE);
            }
        });

        request.setTag(TIPPLANSHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(AddTipActivity.this).addToRequestQueue(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 3) {
            if(resultCode == Activity.RESULT_OK){
                String patient_id = data.getStringExtra("patient_id");
                String patient_name = data.getStringExtra("patient_name");
                String patient_img = data.getStringExtra("patient_img");
                String patient_age = data.getStringExtra("patient_age");
                String patient_gender = data.getStringExtra("patient_gender");
                String patient_type = data.getStringExtra("patient_type");
                String patient_mobile = data.getStringExtra("patient_mobile");

                patientItem = new PatientItem(patient_id,patient_name,patient_gender,patient_age,patient_img,patient_type,patient_mobile);

                if (patientItemArrayList.size()==0){
                    patientItemArrayList.add(patientItem);
                    patientAdapter = new PatientAdapter(AddTipActivity.this, patientItemArrayList, new PatientAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(PatientItem patientItem) {
                        }
                    });
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                    rvPatients.setLayoutManager(layoutManager);
                    rvPatients.setAdapter(patientAdapter);
                }else {
                    patientItemArrayList.add(patientItem);
                    patientAdapter = new PatientAdapter(AddTipActivity.this, patientItemArrayList, new PatientAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(PatientItem patientItem) {
                        }
                    });
                    rvPatients.invalidate();
                    rvPatients.getAdapter().notifyItemInserted(patientItemArrayList.size()-1);
                }


            }
        }
    }

    private void addTipPLans(JSONArray patient_ids, String tip_plan_id, String plan_start_date) {

        String url = getResources().getString(R.string.addTipPlanApi);
        String token = Comman.getPreferences(AddTipActivity.this,"token");
        url = url+"?token="+token;

        Map<String, String> params = new HashMap<>();
        params.put("patient_id",patient_ids+"");
        params.put("tip_plan_id",tip_plan_id);
        params.put("plan_start_date",plan_start_date);

        String ADDTIPPLANSHIT = "add_tipplans_hit";
        VolleySingleton.getInstance(AddTipActivity.this).cancelRequestInQueue(ADDTIPPLANSHIT);
        CustomRequest request = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    boolean success = response.getBoolean("success");
                    String msg = response.getString("message");

                    if (success){

                        patientItemArrayList.clear();
                        patientAdapter = new PatientAdapter(AddTipActivity.this, patientItemArrayList, new PatientAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(PatientItem patientItem) {

                            }
                        });
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        rvPatients.setLayoutManager(layoutManager);
                        rvPatients.setAdapter(patientAdapter);

                        tvDate.setText("");
                        spinTipPlans.setSelection(0);

                    }

                    prgLoading.setVisibility(View.GONE);
                    Toast.makeText(AddTipActivity.this,msg,Toast.LENGTH_SHORT).show();

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(AddTipActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                prgLoading.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddTipActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(ADDTIPPLANSHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(AddTipActivity.this).addToRequestQueue(request);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

}
