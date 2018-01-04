package com.indglobal.nizcare.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.indglobal.nizcare.adapters.LanguageAdapter;
import com.indglobal.nizcare.adapters.SpecialityAdapter;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.commons.VolleyJSONRequest;
import com.indglobal.nizcare.commons.VolleySingleton;
import com.indglobal.nizcare.model.CheckLanguageItem;
import com.indglobal.nizcare.model.LanguageItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by readyassist on 1/3/18.
 */

public class LanguageActivity extends AppCompatActivity implements View.OnClickListener{

    ProgressBar prgLoading;
    TextView tvCancel,tvDone;
    ListView lvLangs;

    CheckLanguageItem checkLanguageItem;
    ArrayList<CheckLanguageItem> checkLanguageItems = new ArrayList<>();
    LanguageAdapter languageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_activity);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        tvCancel = (TextView)findViewById(R.id.tvCancel);
        tvDone = (TextView)findViewById(R.id.tvDone);
        lvLangs = (ListView)findViewById(R.id.lvLangs);

        if (!Comman.isConnectionAvailable(LanguageActivity.this)){
            Toast.makeText(LanguageActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
        }else {
            prgLoading.setVisibility(View.VISIBLE);
            getLanguages();
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
                AddPatientActivity.myLanguagesItems.clear();
                try {
                    String langs = "";
                    for (CheckLanguageItem s : LanguageAdapter.getBox()){
                        if (s.isSelected()){
                            AddPatientActivity.myLanguagesItems.add(new LanguageItem(s.getId(),s.getName()));
                            if (langs.equalsIgnoreCase("")){
                                langs = s.getName();
                            }else {
                                langs = langs+","+s.getName();
                            }
                        }
                    }
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result",langs);
                    setResult(Activity.RESULT_OK,returnIntent);
                    onBackPressed();
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }
    }

    private void getLanguages() {

        String url = getResources().getString(R.string.getLanguageApi);

        String GETLANGSHIT = "get_languages_hit";
        VolleySingleton.getInstance(LanguageActivity.this).cancelRequestInQueue(GETLANGSHIT);
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

                            String id = object.getString("id");
                            String language = object.getString("language");

                            checkLanguageItem = new CheckLanguageItem(id,language);
                            checkLanguageItems.add(checkLanguageItem);
                        }

                        languageAdapter = new LanguageAdapter(LanguageActivity.this, checkLanguageItems);
                        lvLangs.setAdapter(languageAdapter);
                        prgLoading.setVisibility(View.GONE);

                    }else {
                        Toast.makeText(LanguageActivity.this,getResources().getString(R.string.noFound),Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(LanguageActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(LanguageActivity.this,message,Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Toast.makeText(LanguageActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(LanguageActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(GETLANGSHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(LanguageActivity.this).addToRequestQueue(request);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

}