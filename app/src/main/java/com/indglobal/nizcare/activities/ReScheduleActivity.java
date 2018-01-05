package com.indglobal.nizcare.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
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
import com.android.volley.toolbox.HttpHeaderParser;
import com.indglobal.nizcare.R;
import com.indglobal.nizcare.adapters.ClinicAdapter;
import com.indglobal.nizcare.adapters.ConsultationAdapter;
import com.indglobal.nizcare.adapters.DateSlotsAdapter;
import com.indglobal.nizcare.adapters.DatesAdapter;
import com.indglobal.nizcare.adapters.SlotsMainAdapter;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.commons.CustomRequest;
import com.indglobal.nizcare.commons.RippleView;
import com.indglobal.nizcare.commons.VolleyJSONRequest;
import com.indglobal.nizcare.commons.VolleySingleton;
import com.indglobal.nizcare.model.ClinicItem;
import com.indglobal.nizcare.model.ConsultationItem;
import com.indglobal.nizcare.model.SlotItem;
import com.indglobal.nizcare.model.SlotsMainItem;
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

    SlotsMainItem slotsMainItem;
    SlotsMainAdapter slotsMainAdapter;
    SlotItem slotItem;

    ArrayList<Date> dateArrayList = new ArrayList<>();
    DateSlotsAdapter dateSlotsAdapter;

    Date topslctdDate;
    public static String topslctdTime="";
    boolean firstTime=true;

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
            firstTime = true;
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

            case R.id.rplReSchdld:
                String hospital_id = clinicItemArrayList.get(spinClinic.getSelectedItemPosition()).getHospital_id();
                String consultant_id = consultationItemArrayList.get(spinCnsltnts.getSelectedItemPosition()).getConsultant_id();

                if (!Comman.isConnectionAvailable(ReScheduleActivity.this)){
                    Toast.makeText(ReScheduleActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                }else if (topslctdDate==null){
                    Toast.makeText(ReScheduleActivity.this,getResources().getString(R.string.nodateslctd),Toast.LENGTH_SHORT).show();
                }else if (topslctdTime.equalsIgnoreCase("")){
                    Toast.makeText(ReScheduleActivity.this,getResources().getString(R.string.slctTime),Toast.LENGTH_SHORT).show();
                }else if (hospital_id.equalsIgnoreCase("")){
                    Toast.makeText(ReScheduleActivity.this,getResources().getString(R.string.clinicidnot),Toast.LENGTH_SHORT).show();
                }else if (consultant_id.equalsIgnoreCase("")){
                    Toast.makeText(ReScheduleActivity.this,getResources().getString(R.string.noconsltnt),Toast.LENGTH_SHORT).show();
                }else {
                    prgLoading.setVisibility(View.VISIBLE);
                    reschduleApoint(appointment_id,topslctdDate,topslctdTime,hospital_id,consultant_id);
                }
                break;
        }
    }

    private void showHorizontalDates(Date slctdDate) {

        SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        dateArrayList.clear();
        prgLoading.setVisibility(View.VISIBLE);

        long timeutil = System.currentTimeMillis();
        Date crntDate = new Date(timeutil);
        String crntMonth = DateFormat.format("MMMM yyyy",crntDate).toString();

        String slctdMonth = DateFormat.format("MMMM yyyy",slctdDate).toString();

        int slctdPosition =0;
        String slctdFullDate = fullDateFormat.format(slctdDate);
        String crntFullDate = fullDateFormat.format(crntDate);
        boolean crntslctSame = crntFullDate.equalsIgnoreCase(slctdFullDate);

        if (crntMonth.equalsIgnoreCase(slctdMonth)){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(crntDate);

            String tempMonth = DateFormat.format("MMMM yyyy",calendar.getTime()).toString();
            while (crntMonth.equalsIgnoreCase(tempMonth)){
                if (!crntslctSame){
                    String temp = fullDateFormat.format(calendar.getTime());
                    if (temp.equalsIgnoreCase(slctdFullDate)){
                        slctdPosition = dateArrayList.size();
                    }
                }
                dateArrayList.add(calendar.getTime());
                calendar.add(Calendar.DATE,1);
                tempMonth = DateFormat.format("MMMM yyyy",calendar.getTime()).toString();
            }
        }else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(slctdDate);
            calendar.set(Calendar.DAY_OF_MONTH,1);

            String tempMonth = DateFormat.format("MMMM yyyy",calendar.getTime()).toString();
            while (slctdMonth.equalsIgnoreCase(tempMonth)){
                String temp = fullDateFormat.format(calendar.getTime());
                if (temp.equalsIgnoreCase(slctdFullDate)){
                    slctdPosition = dateArrayList.size();
                }
                dateArrayList.add(calendar.getTime());
                calendar.add(Calendar.DATE,1);
                tempMonth = DateFormat.format("MMMM yyyy",calendar.getTime()).toString();
            }
        }

        rvDates.setAdapter(null);
        if (dateSlotsAdapter!=null){
            dateSlotsAdapter.notifyDataSetChanged();
        }

        dateSlotsAdapter = new DateSlotsAdapter(ReScheduleActivity.this, dateArrayList,new DateSlotsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final Date date) {
                if (!Comman.isConnectionAvailable(ReScheduleActivity.this)){
                    Toast.makeText(ReScheduleActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                }else {
                    prgLoading.setVisibility(View.VISIBLE);
                    getSlots(date);
                }
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ReScheduleActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rvDates.setLayoutManager(linearLayoutManager);
        rvDates.setAdapter(dateSlotsAdapter);

        if (rvDates.getAdapter()!=null) {
            final int finalSlctdPosition = slctdPosition;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (rvDates.findViewHolderForAdapterPosition(finalSlctdPosition) != null) {
                            if (rvDates.findViewHolderForAdapterPosition(finalSlctdPosition).itemView != null) {
                                rvDates.findViewHolderForAdapterPosition(finalSlctdPosition).itemView.performClick();

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 100);
        }

        prgLoading.setVisibility(View.GONE);
    }

    private void showTimeSlots(ArrayList<SlotsMainItem> slotsMainItems) {
        slotsMainAdapter = new SlotsMainAdapter(ReScheduleActivity.this,slotsMainItems);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvTimings.setLayoutManager(layoutManager);
        rvTimings.setAdapter(slotsMainAdapter);
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
        url = url+"?token="+token+"&hospital_id="+hospital_id;

        String CNSLTNTSHIT = "get_cnsltnt_hit";
        VolleySingleton.getInstance(ReScheduleActivity.this).cancelRequestInQueue(CNSLTNTSHIT);
        VolleyJSONRequest request = new VolleyJSONRequest(Request.Method.GET, url, null,null, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                try {
                    JSONObject response = new JSONObject(result);
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

                        if (firstTime){
                            long timeutil = System.currentTimeMillis();
                            Date crntDate = new Date(timeutil);
                            prgLoading.setVisibility(View.VISIBLE);
                            firstTime=false;
                            showHorizontalDates(crntDate);
                        }

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

    private void getSlots(final Date slctdDate) {

        topslctdDate = slctdDate;

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(slctdDate);

        String url = getResources().getString(R.string.getSlotsApi);
        String token = Comman.getPreferences(ReScheduleActivity.this,"token");
        url = url+"?token="+token+"&date="+date;

        String GETSLOTSHIT = "get_slots_hit";
        VolleySingleton.getInstance(ReScheduleActivity.this).cancelRequestInQueue(GETSLOTSHIT);
        VolleyJSONRequest request = new VolleyJSONRequest(Request.Method.GET, url,null, null,new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                try {
                    JSONObject response  = new JSONObject(result);
                    boolean success = response.getBoolean("success");

                    if (success){

                        JSONObject data = response.getJSONObject("data");
                        JSONObject slots = data.getJSONObject("slots");

                        ArrayList<SlotsMainItem> slotsMainItems = new ArrayList<>();

                        JSONArray morning = slots.getJSONArray("morning");
                        ArrayList<SlotItem> morningItems = new ArrayList<>();
                        for (int j=0;j<morning.length();j++){

                            JSONObject objmrng = morning.getJSONObject(j);

                            String slot = objmrng.getString("slot");
                            String booking_status = objmrng.getString("booking_status");

                            slotItem = new SlotItem(slot,booking_status);
                            morningItems.add(slotItem);
                        }

                        slotsMainItem = new SlotsMainItem(false,"Morning",morningItems);
                        slotsMainItems.add(slotsMainItem);

                        JSONArray afternoon = slots.getJSONArray("afternoon");
                        ArrayList<SlotItem> afternoonItems = new ArrayList<>();
                        for (int j=0;j<afternoon.length();j++){

                            JSONObject objmrng = afternoon.getJSONObject(j);

                            String slot = objmrng.getString("slot");
                            String booking_status = objmrng.getString("booking_status");

                            slotItem = new SlotItem(slot,booking_status);
                            afternoonItems.add(slotItem);
                        }

                        slotsMainItem = new SlotsMainItem(false,"Afternoon",afternoonItems);
                        slotsMainItems.add(slotsMainItem);

                        JSONArray evening = slots.getJSONArray("evening");
                        ArrayList<SlotItem> eveningItems = new ArrayList<>();
                        for (int j=0;j<evening.length();j++){

                            JSONObject objmrng = evening.getJSONObject(j);

                            String slot = objmrng.getString("slot");
                            String booking_status = objmrng.getString("booking_status");

                            slotItem = new SlotItem(slot,booking_status);
                            eveningItems.add(slotItem);
                        }

                        slotsMainItem = new SlotsMainItem(false,"Evening",eveningItems);
                        slotsMainItems.add(slotsMainItem);

                        showTimeSlots(slotsMainItems);

                        prgLoading.setVisibility(View.GONE);


                    }else {
                        String message = response.getString("message");
                        Toast.makeText(ReScheduleActivity.this,message,Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(ReScheduleActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(ReScheduleActivity.this,message,Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(ReScheduleActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(ReScheduleActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }
                prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(GETSLOTSHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(ReScheduleActivity.this).addToRequestQueue(request);
    }

    private void reschduleApoint(String appointment_id,Date appointment_date,String appointment_time,String hospital_id,
        String consultant_id) {

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(appointment_date);

        String url = getResources().getString(R.string.reschdlapointApi);
        String token = Comman.getPreferences(ReScheduleActivity.this,"token");
        url = url+"?token="+token+"&hospital_id="+hospital_id;

        Map<String, String> params = new HashMap<>();
        params.put("appointment_id",appointment_id);
        params.put("appointment_date",date);
        params.put("appointment_time",appointment_time);
        params.put("hospital_id",hospital_id);
        params.put("consultant_id",consultant_id);

        String RESCHDLAPNTHIT = "reschdl_apnt_hit";
        VolleySingleton.getInstance(ReScheduleActivity.this).cancelRequestInQueue(RESCHDLAPNTHIT);
        CustomRequest request = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    boolean success = response.getBoolean("success");
                    String msg = response.getString("message");
                    if (success){
                        onBackPressed();
                        BaseActivity.viewPager.setCurrentItem(1);
                        BaseActivity.viewPager.setCurrentItem(0);
                    }
                    Toast.makeText(ReScheduleActivity.this,msg,Toast.LENGTH_SHORT).show();

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(ReScheduleActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(ReScheduleActivity.this,message,Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Toast.makeText(ReScheduleActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(ReScheduleActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(RESCHDLAPNTHIT);
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
