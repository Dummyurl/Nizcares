package com.indglobal.nizcare.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.indglobal.nizcare.R;
import com.indglobal.nizcare.adapters.PatientAdapter;
import com.indglobal.nizcare.adapters.SpecialityAdapter;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.commons.VolleyJSONRequest;
import com.indglobal.nizcare.commons.VolleySingleton;
import com.indglobal.nizcare.model.CheckSpecialityItem;
import com.indglobal.nizcare.model.PatientItem;
import com.indglobal.nizcare.model.SpecialityItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by readyassist on 2/3/18.
 */

public class PatientActivity  extends AppCompatActivity implements View.OnClickListener{

    ProgressBar prgLoading;
    TextView tvCancel;
    RecyclerView rvPatients;

    PatientItem patientItem;
    ArrayList<PatientItem> patientItemArrayList = new ArrayList<>();
    PatientAdapter patientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patients_activity);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        tvCancel = (TextView)findViewById(R.id.tvCancel);
        rvPatients = (RecyclerView) findViewById(R.id.rvPatients);

        patientAdapter = new PatientAdapter(PatientActivity.this, patientItemArrayList, new PatientAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PatientItem patientItem) {
                try {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("patient_id",patientItem.getId());
                    returnIntent.putExtra("patient_name",patientItem.getName());
                    returnIntent.putExtra("patient_img",patientItem.getProfile_pic());
                    returnIntent.putExtra("patient_age",patientItem.getAge());
                    returnIntent.putExtra("patient_gender",patientItem.getGender());
                    returnIntent.putExtra("patient_type",patientItem.getPatient_type());
                    returnIntent.putExtra("patient_mobile",patientItem.getMobile_no());
                    setResult(Activity.RESULT_OK,returnIntent);
                    onBackPressed();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        rvPatients.setLayoutManager(layoutManager);
        rvPatients.setAdapter(patientAdapter);

        if (!Comman.isConnectionAvailable(PatientActivity.this)){
            Toast.makeText(PatientActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
        }else {
            prgLoading.setVisibility(View.VISIBLE);
            getPatients();
        }

        tvCancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.tvCancel:
                onBackPressed();
                break;
        }
    }

    private void getPatients() {

        String url = getResources().getString(R.string.getPatientApi);
        String token = Comman.getPreferences(PatientActivity.this,"token");
        url = url+"?token="+token;

        String GETPATIENTSHIT = "get_patient_hit";
        VolleySingleton.getInstance(PatientActivity.this).cancelRequestInQueue(GETPATIENTSHIT);
        VolleyJSONRequest request = new VolleyJSONRequest(Request.Method.GET, url,null, null,new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                try {
                    JSONObject response  = new JSONObject(result);
                    boolean success = response.getBoolean("success");

                    if (success){

                        patientItemArrayList.clear();

                        JSONArray data = response.getJSONArray("data");
                        for (int i=0;i<data.length();i++){

                            JSONObject object = data.getJSONObject(i);

                            String id = object.getString("id");
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String gender = object.getString("gender");
                            String Age = object.getString("Age");
                            String profile_pic = object.getString("profile_pic");
                            String patient_type = object.getString("patient_type");
                            String mobile_no = object.getString("mobile_no");

                            patientItem = new PatientItem(id,first_name+" "+last_name,gender,Age,profile_pic,patient_type,mobile_no);
                            patientItemArrayList.add(patientItem);

                        }

                        patientAdapter.notifyDataSetChanged();

                        prgLoading.setVisibility(View.GONE);


                    }else {
                        String message = response.getString("message");
                        Toast.makeText(PatientActivity.this,message,Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(PatientActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(PatientActivity.this,message,Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(PatientActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(PatientActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }
                prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(GETPATIENTSHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(PatientActivity.this).addToRequestQueue(request);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

}