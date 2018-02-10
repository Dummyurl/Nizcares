package com.indglobal.nizcare.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.indglobal.nizcare.R;
import com.indglobal.nizcare.adapters.ClinicAdapter;
import com.indglobal.nizcare.adapters.CountryAdapter;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.commons.CustomRequest;
import com.indglobal.nizcare.commons.RippleView;
import com.indglobal.nizcare.commons.VolleyJSONRequest;
import com.indglobal.nizcare.commons.VolleySingleton;
import com.indglobal.nizcare.model.ClinicItem;
import com.indglobal.nizcare.model.CountryItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by readyassist on 2/10/18.
 */

public class AddHolidayActivity  extends Activity implements RippleView.OnRippleCompleteListener{

    ProgressBar prgLoading;
    RippleView rplBack,rplAddHolyday;
    RelativeLayout rlMain;
    Spinner spinClinic;
    TextView tvFrmDate,tvToDate;

    String isEdit,ids,doctor_id,hospital_name,from_date,to_date,total_days;

    ClinicItem clinicItem;
    ArrayList<ClinicItem> clinicItemArrayList = new ArrayList<>();
    ClinicAdapter clinicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_holiday_main);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        rplBack = (RippleView)findViewById(R.id.rplBack);
        rlMain = (RelativeLayout)findViewById(R.id.rlMain);
        spinClinic = (Spinner)findViewById(R.id.spinClinic);
        tvFrmDate = (TextView)findViewById(R.id.tvFrmDate);
        tvToDate = (TextView)findViewById(R.id.tvToDate);
        rplAddHolyday = (RippleView)findViewById(R.id.rplAddHolyday);

        Intent ii = getIntent();
        isEdit = ii.getStringExtra("isEdit");
        if (isEdit.equalsIgnoreCase("1")){
            ids = ii.getStringExtra("id");
            doctor_id = ii.getStringExtra("doctor_id");
            hospital_name = ii.getStringExtra("hospital_name");
            from_date = ii.getStringExtra("from_date");
            to_date = ii.getStringExtra("to_date");
            total_days = ii.getStringExtra("total_days");
        }

        if (!Comman.isConnectionAvailable(AddHolidayActivity.this)){
            rlMain.setVisibility(View.GONE);
            Toast.makeText(AddHolidayActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
        }else {
            prgLoading.setVisibility(View.VISIBLE);
            getClinics();
        }

        tvFrmDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCalnderClockDialog(1);
            }
        });

        tvToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCalnderClockDialog(2);
            }
        });

        rplBack.setOnRippleCompleteListener(this);
        rplAddHolyday.setOnRippleCompleteListener(this);
    }

    private void openCalnderClockDialog(final int type) {

        int mYear, mMonth, mDay;

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(AddHolidayActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {

                Calendar datetime = Calendar.getInstance();
                datetime.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                datetime.set(Calendar.MONTH,monthOfYear);
                datetime.set(Calendar.YEAR,year);

                Date dropSchdldDate = datetime.getTime();

                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date = simpleDateFormat.format(dropSchdldDate);
                if (type==1){
                    tvFrmDate.setText(date);
                }else {
                    tvToDate.setText(date);
                }

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

            case R.id.rplAddHolyday:
                String hospital_id = "";
                if (clinicItemArrayList.size()!=0){
                    hospital_id = clinicItemArrayList.get(spinClinic.getSelectedItemPosition()).getHospital_id();
                }
                from_date = tvFrmDate.getText().toString();
                to_date = tvToDate.getText().toString();

                if (!Comman.isConnectionAvailable(AddHolidayActivity.this)){
                    Toast.makeText(AddHolidayActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                }else if (hospital_id.equalsIgnoreCase("")){
                    Toast.makeText(AddHolidayActivity.this,getResources().getString(R.string.clinicidnot),Toast.LENGTH_SHORT).show();
                }else if (from_date.equalsIgnoreCase("")){
                    Toast.makeText(AddHolidayActivity.this,getResources().getString(R.string.prvdfromdate),Toast.LENGTH_SHORT).show();
                }else if (to_date.equalsIgnoreCase("")){
                    Toast.makeText(AddHolidayActivity.this,getResources().getString(R.string.prvdtodate),Toast.LENGTH_SHORT).show();
                }else {
                    prgLoading.setVisibility(View.VISIBLE);
                    if (isEdit.equalsIgnoreCase("0")){
                        prgLoading.setVisibility(View.VISIBLE);
                        addHoliday(hospital_id,from_date,to_date);
                    }else {
                        prgLoading.setVisibility(View.VISIBLE);
                        updateHoliday(ids,hospital_id,from_date,to_date);
                    }
                }

                break;
        }
    }

    private void getClinics() {

        String url = getResources().getString(R.string.getClinicsApi);
        String token = Comman.getPreferences(AddHolidayActivity.this,"token");
        url = url+"?token="+token;

        String CLINICSHIT = "get_clinics_hit";
        VolleySingleton.getInstance(AddHolidayActivity.this).cancelRequestInQueue(CLINICSHIT);
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

                        clinicAdapter = new ClinicAdapter(AddHolidayActivity.this,clinicItemArrayList);
                        spinClinic.setAdapter(clinicAdapter);

                        if (isEdit.equalsIgnoreCase("1")){
                            for (int i = 0;i<clinicItemArrayList.size();i++){
                                if ((clinicItemArrayList.get(i).getName()).equalsIgnoreCase(hospital_name)){
                                    spinClinic.setSelection(i);
                                }
                            }

                            tvFrmDate.setText(from_date);
                            tvToDate.setText(to_date);
                        }

                        rlMain.setVisibility(View.VISIBLE);
                        prgLoading.setVisibility(View.GONE);

                    }else {
                        rlMain.setVisibility(View.GONE);
                        Toast.makeText(AddHolidayActivity.this,getResources().getString(R.string.citynotfound),Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    rlMain.setVisibility(View.GONE);
                    Toast.makeText(AddHolidayActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                prgLoading.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddHolidayActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                rlMain.setVisibility(View.GONE);
                prgLoading.setVisibility(View.GONE);
            }
        });

        request.setTag(CLINICSHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(AddHolidayActivity.this).addToRequestQueue(request);
    }

    private void addHoliday(String hospital_id,String from_date,String to_date) {

        String url = getResources().getString(R.string.addHolidayApi);
        String token = Comman.getPreferences(AddHolidayActivity.this,"token");
        url = url+"?token="+token;

        String ADDHLDYHIT = "add_holyday_hit";

        Map<String, String> params = new HashMap<>();
        params.put("hospital_id",hospital_id);
        params.put("from_date",from_date);
        params.put("to_date",to_date);

        VolleySingleton.getInstance(AddHolidayActivity.this).cancelRequestInQueue(ADDHLDYHIT);
        CustomRequest request = new CustomRequest(Request.Method.POST, url,params,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    boolean success = response.getBoolean("success");
                    String message = response.getString("message");

                    if (success){

                        Toast.makeText(AddHolidayActivity.this,message,Toast.LENGTH_SHORT).show();
                        Comman.setPreferences(AddHolidayActivity.this,"HolidayListUpdated","1");
                        prgLoading.setVisibility(View.GONE);
                        onBackPressed();

                    }else {
                        Toast.makeText(AddHolidayActivity.this,message,Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(AddHolidayActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(AddHolidayActivity.this,message,Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(AddHolidayActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(AddHolidayActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }
                prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(ADDHLDYHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(AddHolidayActivity.this).addToRequestQueue(request);
    }

    private void updateHoliday(String id,String hospital_id,String from_date,String to_date) {

        String url = getResources().getString(R.string.updateHolidayApi);
        String token = Comman.getPreferences(AddHolidayActivity.this,"token");
        url = url+"?token="+token;

        String UPDTEHLDYHIT = "update_holyday_hit";

        Map<String, String> params = new HashMap<>();
        params.put("id",id);
        params.put("hospital_id",hospital_id);
        params.put("from_date",from_date);
        params.put("to_date",to_date);

        VolleySingleton.getInstance(AddHolidayActivity.this).cancelRequestInQueue(UPDTEHLDYHIT);
        CustomRequest request = new CustomRequest(Request.Method.POST, url,params,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    boolean success = response.getBoolean("success");
                    String message = response.getString("message");

                    if (success){

                        Toast.makeText(AddHolidayActivity.this,message,Toast.LENGTH_SHORT).show();
                        Comman.setPreferences(AddHolidayActivity.this,"HolidayListUpdated","1");
                        prgLoading.setVisibility(View.GONE);
                        onBackPressed();

                    }else {
                        Toast.makeText(AddHolidayActivity.this,message,Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(AddHolidayActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(AddHolidayActivity.this,message,Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(AddHolidayActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(AddHolidayActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }
                prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(UPDTEHLDYHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(AddHolidayActivity.this).addToRequestQueue(request);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

}
