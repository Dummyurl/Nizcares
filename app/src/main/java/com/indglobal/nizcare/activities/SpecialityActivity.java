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
import com.indglobal.nizcare.adapters.SpecialityAdapter;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.commons.CustomRequest;
import com.indglobal.nizcare.commons.VolleyJSONRequest;
import com.indglobal.nizcare.commons.VolleySingleton;
import com.indglobal.nizcare.model.CheckSpecialityItem;
import com.indglobal.nizcare.model.SpecialityItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by readyassist on 12/26/17.
 */

public class SpecialityActivity extends AppCompatActivity implements View.OnClickListener{

    ProgressBar prgLoading;
    TextView tvCancel,tvDone;
    ListView lvSpecility;

    CheckSpecialityItem checkSpecialityItem;
    ArrayList<CheckSpecialityItem> checkSpecialityItems = new ArrayList<>();
    SpecialityAdapter specialityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speciality_activity);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        tvCancel = (TextView)findViewById(R.id.tvCancel);
        tvDone = (TextView)findViewById(R.id.tvDone);
        lvSpecility = (ListView)findViewById(R.id.lvSpecility);

        if (!Comman.isConnectionAvailable(SpecialityActivity.this)){
            Toast.makeText(SpecialityActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
        }else {
            prgLoading.setVisibility(View.VISIBLE);
            getSpecialities();
        }

        tvCancel.setOnClickListener(this);
        tvDone.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.tvCancel:
                onBackPressed();
                break;

            case R.id.tvDone:
                AccountSetupActivity.mySpecialityItems.clear();
                try {
                    String specilties = "";
                    for (CheckSpecialityItem s : SpecialityAdapter.getBox()){
                        if (s.isSelected()){
                            AccountSetupActivity.mySpecialityItems.add(new SpecialityItem(s.getId(),s.getName()));
                            if (specilties.equalsIgnoreCase("")){
                                specilties = s.getName();
                            }else {
                                specilties = specilties+","+s.getName();
                            }
                        }
                    }
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result",specilties);
                    setResult(Activity.RESULT_OK,returnIntent);
                    onBackPressed();
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }
    }

    private void getSpecialities() {

        String url = getResources().getString(R.string.getSpeclitiesApi);

        String GETSPECHIT = "get_speclties_hit";
        VolleySingleton.getInstance(SpecialityActivity.this).cancelRequestInQueue(GETSPECHIT);
        VolleyJSONRequest request = new VolleyJSONRequest(Request.Method.GET, url,null, null,new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                try {
                    JSONObject response  = new JSONObject(result);
                    boolean success = response.getBoolean("success");

                    if (success){

                        JSONArray data = response.getJSONArray("data");
                        for (int i=0;i<data.length();i++){
                            JSONObject object = data.getJSONObject(i);

                            String id = object.getString("master_speciality_id");
                            String name = object.getString("master_speciality_name");

                            checkSpecialityItem = new CheckSpecialityItem(id,name);
                            checkSpecialityItems.add(checkSpecialityItem);
                        }

                        specialityAdapter = new SpecialityAdapter(SpecialityActivity.this, checkSpecialityItems);
                        lvSpecility.setAdapter(specialityAdapter);
                        prgLoading.setVisibility(View.GONE);

                    }else {
                        Toast.makeText(SpecialityActivity.this,getResources().getString(R.string.noFound),Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(SpecialityActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(SpecialityActivity.this,message,Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Toast.makeText(SpecialityActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(SpecialityActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(GETSPECHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(SpecialityActivity.this).addToRequestQueue(request);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

}