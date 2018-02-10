package com.indglobal.nizcare.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.indglobal.nizcare.R;
import com.indglobal.nizcare.adapters.BankAdapter;
import com.indglobal.nizcare.adapters.HolydaysAdapter;
import com.indglobal.nizcare.adapters.MyClinicAdapter;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.commons.RippleView;
import com.indglobal.nizcare.commons.VolleyJSONRequest;
import com.indglobal.nizcare.commons.VolleySingleton;
import com.indglobal.nizcare.model.BankItem;
import com.indglobal.nizcare.model.ClinicItem;
import com.indglobal.nizcare.model.HolidayItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by readyassist on 2/10/18.
 */

public class MyHolidaysActivity extends Activity implements RippleView.OnRippleCompleteListener,View.OnClickListener{

    public static ProgressBar prgLoading;
    RippleView rplBack;
    RecyclerView rvHolidays;
    CardView crdAddNew;

    HolidayItem holidayItem;
    ArrayList<HolidayItem> holidayItemArrayList = new ArrayList<>();
    HolydaysAdapter holydaysAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.holdy_dtls_main);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        rplBack = (RippleView)findViewById(R.id.rplBack);
        rvHolidays  = (RecyclerView)findViewById(R.id.rvHolidays);
        crdAddNew = (CardView)findViewById(R.id.crdAddNew);

        Comman.setPreferences(MyHolidaysActivity.this,"HolidayListUpdated","0");

        if (!Comman.isConnectionAvailable(MyHolidaysActivity.this)){
            Toast.makeText(MyHolidaysActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
        }else {
            prgLoading.setVisibility(View.VISIBLE);
            getHolydays();
        }

        rplBack.setOnRippleCompleteListener(this);
        crdAddNew.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.crdAddNew:
                Intent ii = new Intent(MyHolidaysActivity.this,AddHolidayActivity.class);
                ii.putExtra("isEdit","0");
                startActivity(ii);
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
        }
    }

    private void getHolydays() {

        String url = getResources().getString(R.string.getHolydaysApi);
        String token = Comman.getPreferences(MyHolidaysActivity.this,"token");
        url = url+"?token="+token;

        String GETHLDYSHIT = "get_hlydays_hit";
        VolleySingleton.getInstance(MyHolidaysActivity.this).cancelRequestInQueue(GETHLDYSHIT);
        VolleyJSONRequest request = new VolleyJSONRequest(Request.Method.GET, url,null, null,new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                try {
                    JSONObject response  = new JSONObject(result);
                    boolean success = response.getBoolean("success");

                    if (success){

                        holidayItemArrayList.clear();

                        JSONArray data = response.getJSONArray("data");
                        for (int i=0;i<data.length();i++){

                            JSONObject object = data.getJSONObject(i);

                            String id = object.getString("id");
                            String doctor_id = object.getString("doctor_id");
                            String hospital_name = object.getString("hospital_name");
                            String from_date = object.getString("from_date");
                            String to_date = object.getString("to_date");
                            String total_days = object.getString("total_days");

                            holidayItem = new HolidayItem(id,doctor_id,hospital_name,from_date,to_date,total_days);
                            holidayItemArrayList.add(holidayItem);

                        }

                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MyHolidaysActivity.this.getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                        holydaysAdapter = new HolydaysAdapter(MyHolidaysActivity.this,holidayItemArrayList, new HolydaysAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(HolidayItem holidayItem) {

                            }
                        });
                        rvHolidays.setLayoutManager(layoutManager);
                        rvHolidays.setAdapter(holydaysAdapter);

                        prgLoading.setVisibility(View.GONE);


                    }else {
                        String message = response.getString("message");
                        Toast.makeText(MyHolidaysActivity.this,message,Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(MyHolidaysActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(MyHolidaysActivity.this,message,Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(MyHolidaysActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(MyHolidaysActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }
                prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(GETHLDYSHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(MyHolidaysActivity.this).addToRequestQueue(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String HolidayListUpdated = Comman.getPreferences(MyHolidaysActivity.this,"HolidayListUpdated");
        if (HolidayListUpdated.equalsIgnoreCase("1")){
            Comman.setPreferences(MyHolidaysActivity.this,"HolidayListUpdated","0");
            if (!Comman.isConnectionAvailable(MyHolidaysActivity.this)){
                Toast.makeText(MyHolidaysActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
            }else {
                prgLoading.setVisibility(View.VISIBLE);
                getHolydays();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

}
