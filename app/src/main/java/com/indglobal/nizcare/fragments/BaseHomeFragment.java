package com.indglobal.nizcare.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import com.indglobal.nizcare.activities.BaseActivity;
import com.indglobal.nizcare.adapters.ApointMainAdapter;
import com.indglobal.nizcare.adapters.CalendarAdapter;
import com.indglobal.nizcare.adapters.CncltnMainAdapter;
import com.indglobal.nizcare.adapters.DatesAdapter;
import com.indglobal.nizcare.adapters.DealsAdapter;
import com.indglobal.nizcare.adapters.EnquiryAdapter;
import com.indglobal.nizcare.adapters.ForumAdapter;
import com.indglobal.nizcare.adapters.InstantAdapter;
import com.indglobal.nizcare.adapters.NewsAdapter;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.commons.ExpandableHeightGridView;
import com.indglobal.nizcare.commons.VolleyJSONRequest;
import com.indglobal.nizcare.commons.VolleySingleton;
import com.indglobal.nizcare.model.ApointItem;
import com.indglobal.nizcare.model.ApointMainItem;
import com.indglobal.nizcare.model.ApointTimeItem;
import com.indglobal.nizcare.model.CnsltnItem;
import com.indglobal.nizcare.model.CnsltnMainItem;
import com.indglobal.nizcare.model.CnsltnTimeItem;
import com.indglobal.nizcare.model.DealsItem;
import com.indglobal.nizcare.model.EnquiryItem;
import com.indglobal.nizcare.model.ForumItem;
import com.indglobal.nizcare.model.InstantItem;
import com.indglobal.nizcare.model.NewsItem;
import com.indglobal.nizcare.model.ReplyItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by readyassist on 12/16/17.
 */

public class BaseHomeFragment extends Fragment implements View.OnClickListener{

    LayoutInflater inflater;
    public static ProgressBar prgLoading;

    TextView tvTabApoints,tvTabConsltns,tvTabOpinon,tvIndApoints,tvIndConsltns,tvIndOpinion,tvMonth;
    LinearLayout llApoints,llConsultations,llOpinion,llMain,llMonth;
    RecyclerView rvDates,rvApoints,rvBstDeals,rvNews,rvInstantCnsltn,rvReglrCnsltn,rvDctrFrm;

    ApointMainItem apointMainItem;
    ApointTimeItem apointTimeItem;
    ApointItem apointItem;
    public HashMap<String,ApointMainItem> apointMainItemHashMap= new HashMap<>();

    ArrayList<Date> dateArrayList = new ArrayList<>();
    DatesAdapter datesAdapter;

    ApointMainAdapter apointMainAdapter;
    public static ArrayList<ApointTimeItem> apointTimeItemArrayList = new ArrayList<>();

    private boolean isViewShown = false;
    Date topDate;

    ProgressBar prgDLoading;
    ImageView ivClndrBack,ivClndrNext;
    TextView tvClndrMonth;
    ExpandableHeightGridView grdClndr;

    public Calendar month, itemmonth;
    public CalendarAdapter calendarAdapter;
    public Handler handler;
    public static View slctd = null;
    boolean nexttime = false,fromClndr = false;
    ArrayList<String> dataUploaded = new ArrayList<>();

    DealsItem dealsItem;
    ArrayList<DealsItem> dealsItemArrayList = new ArrayList<>();
    DealsAdapter dealsAdapter;

    NewsItem newsItem;
    ArrayList<NewsItem> newsItemArrayList = new ArrayList<>();
    NewsAdapter newsAdapter;

    InstantItem instantItem;
    ArrayList<InstantItem> instantItemArrayList = new ArrayList<>();
    InstantAdapter instantAdapter;

    CnsltnItem cnsltnItem;
    CnsltnTimeItem cnsltnTimeItem;
    CnsltnMainItem cnsltnMainItem;
    public HashMap<String,CnsltnMainItem> cnsltnMainItemHashMap= new HashMap<>();
    CncltnMainAdapter cncltnMainAdapter;
    public static ArrayList<CnsltnTimeItem> cnsltnTimeItemArrayList = new ArrayList<>();

    ForumItem forumItem;
    ArrayList<ForumItem> forumItemArrayList = new ArrayList<>();
    ForumAdapter forumAdapter;

    public static Date selectedDate;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        inflater = (LayoutInflater) activity.getLayoutInflater();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_home_fragment, container, false);

        prgLoading = (ProgressBar)view.findViewById(R.id.prgLoading);
        tvTabApoints = (TextView)view.findViewById(R.id.tvTabApoints);
        tvTabConsltns = (TextView)view.findViewById(R.id.tvTabConsltns);
        tvTabOpinon = (TextView)view.findViewById(R.id.tvTabOpinon);
        tvIndApoints = (TextView)view.findViewById(R.id.tvIndApoints);
        tvIndConsltns = (TextView)view.findViewById(R.id.tvIndConsltns);
        tvIndOpinion = (TextView)view.findViewById(R.id.tvIndOpinion);

        llApoints = (LinearLayout)view.findViewById(R.id.llApoints);
        llConsultations = (LinearLayout)view.findViewById(R.id.llConsultations);
        llOpinion = (LinearLayout)view.findViewById(R.id.llOpinion);

        llMain = (LinearLayout)view.findViewById(R.id.llMain);
        llMonth = (LinearLayout)view.findViewById(R.id.llMonth);
        tvMonth = (TextView)view.findViewById(R.id.tvMonth);
        rvDates = (RecyclerView)view.findViewById(R.id.rvDates);
        rvApoints = (RecyclerView)view.findViewById(R.id.rvApoints);
        rvBstDeals = (RecyclerView)view.findViewById(R.id.rvBstDeals);
        rvNews = (RecyclerView)view.findViewById(R.id.rvNews);
        rvInstantCnsltn = (RecyclerView)view.findViewById(R.id.rvInstantCnsltn);
        rvReglrCnsltn = (RecyclerView)view.findViewById(R.id.rvReglrCnsltn);
        rvDctrFrm = (RecyclerView)view.findViewById(R.id.rvDctrFrm);

        tvTabApoints.setOnClickListener(this);
        tvTabConsltns.setOnClickListener(this);
        tvTabOpinon.setOnClickListener(this);
        llMonth.setOnClickListener(this);

        initMethod();

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (getView() != null) {
                isViewShown = true;
                if (!Comman.isConnectionAvailable(getActivity())){
                    Toast.makeText(getActivity(),getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                }else {
                    prgLoading.setVisibility(View.VISIBLE);
                    long timeutil = System.currentTimeMillis();
                    Date crntDate = new Date(timeutil);
                    nexttime = false;
                    getApointments(crntDate);
                }
            } else {
                isViewShown = false;
            }
        }

    }

    public void initMethod() {
        int crnt = BaseActivity.viewPager.getCurrentItem();
        if (crnt == 0) {
            if (!isViewShown) {
                if (!Comman.isConnectionAvailable((BaseActivity)getActivity())){
                    Toast.makeText(getActivity(),getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                }else {
                    prgLoading.setVisibility(View.VISIBLE);
                    long timeutil = System.currentTimeMillis();
                    Date crntDate = new Date(timeutil);
                    nexttime = false;
                    getApointments(crntDate);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){

            case R.id.tvTabApoints:
                llOpinion.setVisibility(View.GONE);
                llConsultations.setVisibility(View.GONE);
                llApoints.setVisibility(View.VISIBLE);
                tvIndOpinion.setBackgroundColor(getResources().getColor(R.color.lightGray));
                tvTabOpinon.setTextColor(getResources().getColor(R.color.lightBlack));
                tvIndConsltns.setBackgroundColor(getResources().getColor(R.color.lightGray));
                tvTabConsltns.setTextColor(getResources().getColor(R.color.lightBlack));
                tvIndApoints.setBackgroundColor(getResources().getColor(R.color.lightGreen));
                tvTabApoints.setTextColor(getResources().getColor(R.color.lightGreen));
                break;

            case R.id.tvTabConsltns:
                llOpinion.setVisibility(View.GONE);
                llApoints.setVisibility(View.GONE);
                llConsultations.setVisibility(View.VISIBLE);
                tvIndOpinion.setBackgroundColor(getResources().getColor(R.color.lightGray));
                tvTabOpinon.setTextColor(getResources().getColor(R.color.lightBlack));
                tvIndConsltns.setBackgroundColor(getResources().getColor(R.color.lightGreen));
                tvTabConsltns.setTextColor(getResources().getColor(R.color.lightGreen));
                tvIndApoints.setBackgroundColor(getResources().getColor(R.color.lightGray));
                tvTabApoints.setTextColor(getResources().getColor(R.color.lightBlack));
                if (!Comman.isConnectionAvailable(getActivity())){
                    Toast.makeText(getActivity(),getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                }else {
                    prgLoading.setVisibility(View.VISIBLE);
                    getConsultations();
                }
                break;

            case R.id.tvTabOpinon:
                llApoints.setVisibility(View.GONE);
                llConsultations.setVisibility(View.GONE);
                llOpinion.setVisibility(View.VISIBLE);
                tvIndConsltns.setBackgroundColor(getResources().getColor(R.color.lightGray));
                tvTabConsltns.setTextColor(getResources().getColor(R.color.lightBlack));
                tvIndApoints.setBackgroundColor(getResources().getColor(R.color.lightGray));
                tvTabApoints.setTextColor(getResources().getColor(R.color.lightBlack));
                tvIndOpinion.setBackgroundColor(getResources().getColor(R.color.lightGreen));
                tvTabOpinon.setTextColor(getResources().getColor(R.color.lightGreen));
                break;

            case R.id.llMonth:
                openCalendarSheet(topDate);
                break;
        }
    }

    private void getApointments(final Date slctdDate) {

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(slctdDate);

        String url = getResources().getString(R.string.getApointApi);
        String token = Comman.getPreferences(getActivity(),"token");
        url = url+"?token="+token+"&date="+date;

        String GETAPOINTHIT = "get_apoints_hit";
        VolleySingleton.getInstance(getActivity()).cancelRequestInQueue(GETAPOINTHIT);
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
                            String date = object.getString("date");
                            String count = object.getString("count");

                            ArrayList<ApointTimeItem> apointTimeItems = new ArrayList<>();

                            JSONArray morning = object.getJSONArray("morning");
                            ArrayList<ApointItem> morningItems = new ArrayList<>();
                            for (int j=0;j<morning.length();j++){

                                JSONObject objmrng = morning.getJSONObject(j);

                                String apointment_id = objmrng.getString("apointment_id");
                                String patient_name = objmrng.getString("patient_name");
                                String hospital_name = objmrng.getString("hospital_name");
                                String appointment_date = objmrng.getString("appointment_date");
                                String appointment_time = objmrng.getString("appointment_time");
                                String status = objmrng.getString("status");
                                String patient_id = objmrng.getString("patient_id");
                                String gender = objmrng.getString("gender");
                                String age = objmrng.getString("age");
                                String createdby = objmrng.getString("appointment_created_by");
                                String profile_image = objmrng.getString("profile_image");
                                String profile_image_thumb = objmrng.getString("profile_image_thumb");

                                apointItem = new ApointItem(apointment_id,patient_name,hospital_name,appointment_date,
                                        appointment_time,status,patient_id,gender,age,createdby,profile_image,profile_image_thumb);
                                morningItems.add(apointItem);
                            }

                            apointTimeItem = new ApointTimeItem(false,"Morning",morning.length()+"",morningItems);
                            apointTimeItems.add(apointTimeItem);

                            JSONArray afternoon = object.getJSONArray("afternoon");
                            ArrayList<ApointItem> afternoonItems = new ArrayList<>();
                            for (int j=0;j<afternoon.length();j++){

                                JSONObject objmrng = afternoon.getJSONObject(j);

                                String apointment_id = objmrng.getString("apointment_id");
                                String patient_name = objmrng.getString("patient_name");
                                String hospital_name = objmrng.getString("hospital_name");
                                String appointment_date = objmrng.getString("appointment_date");
                                String appointment_time = objmrng.getString("appointment_time");
                                String status = objmrng.getString("status");
                                String patient_id = objmrng.getString("patient_id");
                                String gender = objmrng.getString("gender");
                                String age = objmrng.getString("age");
                                String createdby = objmrng.getString("appointment_created_by");
                                String profile_image = objmrng.getString("profile_image");
                                String profile_image_thumb = objmrng.getString("profile_image_thumb");

                                apointItem = new ApointItem(apointment_id,patient_name,hospital_name,appointment_date,
                                        appointment_time,status,patient_id,gender,age,createdby,profile_image,profile_image_thumb);
                                afternoonItems.add(apointItem);
                            }

                            apointTimeItem = new ApointTimeItem(false,"Afternoon",afternoon.length()+"",afternoonItems);
                            apointTimeItems.add(apointTimeItem);

                            JSONArray evening = object.getJSONArray("evening");
                            ArrayList<ApointItem> eveningItems = new ArrayList<>();
                            for (int j=0;j<evening.length();j++){

                                JSONObject objmrng = evening.getJSONObject(j);

                                String apointment_id = objmrng.getString("apointment_id");
                                String patient_name = objmrng.getString("patient_name");
                                String hospital_name = objmrng.getString("hospital_name");
                                String appointment_date = objmrng.getString("appointment_date");
                                String appointment_time = objmrng.getString("appointment_time");
                                String status = objmrng.getString("status");
                                String patient_id = objmrng.getString("patient_id");
                                String gender = objmrng.getString("gender");
                                String age = objmrng.getString("age");
                                String createdby = objmrng.getString("appointment_created_by");
                                String profile_image = objmrng.getString("profile_image");
                                String profile_image_thumb = objmrng.getString("profile_image_thumb");

                                apointItem = new ApointItem(apointment_id,patient_name,hospital_name,appointment_date,
                                        appointment_time,status,patient_id,gender,age,createdby,profile_image,profile_image_thumb);
                                eveningItems.add(apointItem);
                            }

                            apointTimeItem = new ApointTimeItem(false,"Evening",evening.length()+"",eveningItems);
                            apointTimeItems.add(apointTimeItem);

                            apointMainItem = new ApointMainItem(count,apointTimeItems);
                            apointMainItemHashMap.put(date,apointMainItem);

                        }

                        SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMMM yyyy");
                        String monthYear = monthYearFormat.format(slctdDate);
                        tvMonth.setText(monthYear);
                        topDate = slctdDate;

                        showHorizontalDates(slctdDate);

                        llMain.setVisibility(View.VISIBLE);
                        prgLoading.setVisibility(View.GONE);

                        if (fromClndr){
                            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
                            String crnt = sdf1.format(month.getTime());
                            dataUploaded.add(crnt);
                            refreshCalendar();
                            prgDLoading.setVisibility(View.GONE);
                        }

                    }else {
                        String message = response.getString("message");
                        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getActivity(),getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                if (!Comman.isConnectionAvailable(getActivity())){
                    Toast.makeText(getActivity(),getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                    prgLoading.setVisibility(View.GONE);
                }else {
                    prgLoading.setVisibility(View.VISIBLE);
                    getDeals();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    if (error.networkResponse.data!=null){
                        String jsonString = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        JSONObject errObject = new JSONObject(jsonString);

                        String message = errObject.getString("message");

                        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(getActivity(),getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(),getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }
                prgLoading.setVisibility(View.GONE);
                if (!Comman.isConnectionAvailable(getActivity())){
                    Toast.makeText(getActivity(),getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                    prgLoading.setVisibility(View.GONE);
                }else {
                    prgLoading.setVisibility(View.VISIBLE);
                    getDeals();
                }
            }
        });
        request.setTag(GETAPOINTHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void showHorizontalDates(Date slctdDate) {

        SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        topDate = slctdDate;
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
        if (datesAdapter!=null){
            datesAdapter.notifyDataSetChanged();
        }

        datesAdapter = new DatesAdapter(getActivity(), dateArrayList, apointMainItemHashMap, new DatesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final Date date) {
                showApointments(date);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvDates.setLayoutManager(linearLayoutManager);
        rvDates.setAdapter(datesAdapter);

        if (rvDates.getAdapter()!=null){
            final int finalSlctdPosition = slctdPosition;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        rvDates.scrollToPosition(finalSlctdPosition);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            },10);
        }

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

    private void showApointments(Date slctdDate) {
        selectedDate = slctdDate;
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(slctdDate);
        topDate = slctdDate;

        if (apointMainItemHashMap.containsKey(date)){

            ApointMainItem apointMainItem = apointMainItemHashMap.get(date);
            apointTimeItemArrayList.clear();
            apointTimeItemArrayList.addAll(apointMainItem.getApointTimeItems());

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.VERTICAL,false);
            apointMainAdapter = new ApointMainAdapter(getActivity(),apointTimeItemArrayList);
            rvApoints.setLayoutManager(layoutManager);
            rvApoints.setAdapter(apointMainAdapter);
            rvApoints.invalidate();

        }else {
            apointTimeItemArrayList.clear();
            rvApoints.setAdapter(null);
        }

        llOpinion.setVisibility(View.GONE);
        llConsultations.setVisibility(View.GONE);
        llApoints.setVisibility(View.VISIBLE);
        tvIndOpinion.setBackgroundColor(getResources().getColor(R.color.lightGray));
        tvTabOpinon.setTextColor(getResources().getColor(R.color.lightBlack));
        tvIndConsltns.setBackgroundColor(getResources().getColor(R.color.lightGray));
        tvTabConsltns.setTextColor(getResources().getColor(R.color.lightBlack));
        tvIndApoints.setBackgroundColor(getResources().getColor(R.color.lightGreen));
        tvTabApoints.setTextColor(getResources().getColor(R.color.lightGreen));
    }

    public void openCalendarSheet (Date slctdDate) {

        View view = getActivity().getLayoutInflater().inflate (R.layout.calendar_topsheet, null);
        prgDLoading = (ProgressBar)view.findViewById(R.id.prgLoading);
        ivClndrBack = (ImageView)view.findViewById(R.id.ivClndrBack);
        ivClndrNext = (ImageView)view.findViewById(R.id.ivClndrNext);
        tvClndrMonth = (TextView)view.findViewById(R.id.tvClndrMonth);
        grdClndr = (ExpandableHeightGridView)view.findViewById(R.id.grdClndr);


        final Dialog mTopSheetDialog = new Dialog (getActivity(), R.style.MaterialDialogTopSheet);
        mTopSheetDialog.setContentView (view);
        mTopSheetDialog.setCancelable (true);
        mTopSheetDialog.getWindow().setLayout (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mTopSheetDialog.getWindow().setGravity (Gravity.TOP);
        mTopSheetDialog.show ();

        month = Calendar.getInstance();
        month.setTime(slctdDate);
        itemmonth = (Calendar) month.clone();

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
        String crnt = sdf1.format(month.getTime());
        dataUploaded.add(crnt);

        calendarAdapter = new CalendarAdapter(getActivity(), month);

        tvClndrMonth.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
        nexttime = false;
        if (!nexttime){
            grdClndr.setAdapter(calendarAdapter);
            grdClndr.setExpanded(true);
            handler = new Handler();
            handler.post(calendarUpdater);
            prgLoading.setVisibility(View.GONE);
            nexttime = true;
            llMain.setVisibility(View.VISIBLE);
            long date = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = sdf.format(date);
            //loadFilterdata(dateString);

        }else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            dataUploaded.add(sdf.format(month.getTime()));
            refreshCalendar();
        }


        ivClndrBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (month.get(Calendar.MONTH)==Calendar.getInstance().get(Calendar.MONTH)){
                    Toast.makeText(getActivity(),"No data available for past month!",Toast.LENGTH_SHORT).show();
                }else {
                    setPreviousMonth();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
                    String crnt = sdf1.format(month.getTime());
                    if (dataUploaded.contains(crnt)){
                        refreshCalendar();
                    }else {
                        if (!Comman.isConnectionAvailable(getActivity())){
                            Toast.makeText(getActivity(), getResources().getString(R.string.noInternet), Toast.LENGTH_SHORT).show();
                        }else {
                            prgDLoading.setVisibility(View.VISIBLE);
                            getApointments(month.getTime());
                        }
                    }
                }

            }
        });

        ivClndrNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setNextMonth();
                //refreshCalendar();
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
                String crnt = sdf1.format(month.getTime());
                if (dataUploaded.contains(crnt)){
                    refreshCalendar();
                }else {
                    if (!Comman.isConnectionAvailable(getActivity())){
                        Toast.makeText(getActivity(), getResources().getString(R.string.noInternet), Toast.LENGTH_SHORT).show();
                    }else {
                        prgDLoading.setVisibility(View.VISIBLE);
                        fromClndr = true;
                        getApointments(month.getTime());
                    }
                }
            }
        });

        grdClndr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String dd = CalendarAdapter.dayString.get(position);
                String ss = CalendarAdapter.apoints.get(position);

                if (!ss.equalsIgnoreCase("0")) {
                    if (slctd!=null){
                        slctd.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                    view.setBackground(getResources().getDrawable(R.drawable.round_stroke_green));
                    slctd = view;

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date date = simpleDateFormat.parse(dd);
                        showHorizontalDates(date);
                        mTopSheetDialog.dismiss();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getActivity(), "No data available on this date!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    protected void setNextMonth() {
        if (month.get(Calendar.MONTH) == month.getActualMaximum(Calendar.MONTH)) {
            month.set((month.get(Calendar.YEAR) + 1),
                    month.getActualMinimum(Calendar.MONTH), 1);
        } else {
            month.set(Calendar.MONTH, month.get(Calendar.MONTH) + 1);
        }
    }

    protected void setPreviousMonth() {
        if (month.get(Calendar.MONTH) == month.getActualMinimum(Calendar.MONTH)) {
            month.set((month.get(Calendar.YEAR) - 1), month.getActualMaximum(Calendar.MONTH), 1);
        } else {
            month.set(Calendar.MONTH, month.get(Calendar.MONTH) - 1);
        }
    }

    public void refreshCalendar() {
        calendarAdapter.refreshDays();
        calendarAdapter.notifyDataSetChanged();
        handler.post(calendarUpdater);
        tvClndrMonth.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
    }

    public Runnable calendarUpdater = new Runnable() {

        @Override
        public void run() {
            itemmonth.add(Calendar.DATE, 1);
            calendarAdapter.setItems(apointMainItemHashMap);
            calendarAdapter.notifyDataSetChanged();
        }
    };

    private void getDeals() {

        String url = getResources().getString(R.string.getDealsApi);

        String GETADSHIT = "get_ads_hit";
        VolleySingleton.getInstance(getActivity()).cancelRequestInQueue(GETADSHIT);
        VolleyJSONRequest request = new VolleyJSONRequest(Request.Method.GET, url,null, null,new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                try {
                    JSONObject response  = new JSONObject(result);
                    boolean success = response.getBoolean("success");

                    if (success){

                        dealsItemArrayList.clear();

                        JSONArray data = response.getJSONArray("data");
                        for (int i=0;i<data.length();i++){

                            JSONObject object = data.getJSONObject(i);

                            String master_ads_id = object.getString("master_ads_id");
                            String offer = object.getString("offer");
                            String description = object.getString("description");
                            String media = object.getString("media");
                            String media_thumb = object.getString("media_thumb");

                            dealsItem = new DealsItem(master_ads_id,offer,description,media,media_thumb);
                            dealsItemArrayList.add(dealsItem);

                        }

                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
                        dealsAdapter = new DealsAdapter(getActivity(),dealsItemArrayList,new DealsAdapter.OnItemClickListener(){

                            @Override
                            public void onItemClick(DealsItem dealsItem) {

                            }
                        });
                        rvBstDeals.setLayoutManager(layoutManager);
                        rvBstDeals.setAdapter(dealsAdapter);

                        prgLoading.setVisibility(View.GONE);


                    }else {
                        String message = response.getString("message");
                        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getActivity(),getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                prgLoading.setVisibility(View.GONE);
                if (!Comman.isConnectionAvailable(getActivity())){
                    Toast.makeText(getActivity(),getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                    prgLoading.setVisibility(View.GONE);
                }else {
                    prgLoading.setVisibility(View.VISIBLE);
                    getNews();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    if (error.networkResponse.data!=null){
                        String jsonString = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        JSONObject errObject = new JSONObject(jsonString);

                        String message = errObject.getString("message");

                        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(getActivity(),getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(),getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }
                prgLoading.setVisibility(View.GONE);
                if (!Comman.isConnectionAvailable(getActivity())){
                    Toast.makeText(getActivity(),getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                    prgLoading.setVisibility(View.GONE);
                }else {
                    prgLoading.setVisibility(View.VISIBLE);
                    getNews();
                }
            }
        });
        request.setTag(GETADSHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void getNews() {

        String url = getResources().getString(R.string.getNewsApi);

        String GETNEWSHIT = "get_news_hit";
        VolleySingleton.getInstance(getActivity()).cancelRequestInQueue(GETNEWSHIT);
        VolleyJSONRequest request = new VolleyJSONRequest(Request.Method.GET, url,null, null,new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                try {
                    JSONObject response  = new JSONObject(result);
                    boolean success = response.getBoolean("success");

                    if (success){

                        newsItemArrayList.clear();

                        JSONArray data = response.getJSONArray("data");
                        for (int i=0;i<data.length();i++){

                            JSONObject object = data.getJSONObject(i);

                            String master_news_id = object.getString("master_news_id");
                            String description = object.getString("description");
                            String media = object.getString("media");
                            String media_thumb = object.getString("media_thumb");
                            String logo = object.getString("logo");
                            String link = object.getString("link");

                            newsItem = new NewsItem(master_news_id,description,media,media_thumb,logo,link);
                            newsItemArrayList.add(newsItem);

                        }

                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
                        newsAdapter = new NewsAdapter(getActivity(),newsItemArrayList,new NewsAdapter.OnItemClickListener(){
                            @Override
                            public void onItemClick(NewsItem newsItem) {
                                String url = newsItem.getLink();
                                if (!url.startsWith("http://") && !url.startsWith("https://")){
                                    url = "http://" + url;
                                }

                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(getActivity(),getResources().getString(R.string.brwsrnotavlbe),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        rvNews.setLayoutManager(layoutManager);
                        rvNews.setAdapter(newsAdapter);

                        prgLoading.setVisibility(View.GONE);


                    }else {
                        String message = response.getString("message");
                        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getActivity(),getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(getActivity(),getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(),getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }
                prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(GETNEWSHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void getConsultations() {

        String url = getResources().getString(R.string.getConsultationsApi);
        String token = Comman.getPreferences(getActivity(),"token");
        url = url+"?token="+token;

        String GETCNSLTNHIT = "get_cnsltatn_hit";
        VolleySingleton.getInstance(getActivity()).cancelRequestInQueue(GETCNSLTNHIT);
        VolleyJSONRequest request = new VolleyJSONRequest(Request.Method.GET, url,null, null,new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                try {
                    JSONObject response  = new JSONObject(result);
                    boolean success = response.getBoolean("success");

                    if (success){

                        instantItemArrayList.clear();

                        JSONObject data = response.getJSONObject("data");

                        JSONArray instant = data.getJSONArray("instant");
                        for (int i=0;i<instant.length();i++){

                            JSONObject object = instant.getJSONObject(i);

                            String consultation_id = object.getString("consultation_id");
                            String patient_id = object.getString("patient_id");
                            String name = object.getString("name");
                            String status = object.getString("status");
                            String consult_type = object.getString("consult_type");
                            String type = object.getString("type");
                            String profile_pic = object.getString("profile_pic");
                            String profile_pic_thumb = object.getString("profile_pic_thumb");
                            String text_notification_count = object.getString("text_notification_count");

                            instantItem = new InstantItem(consultation_id,patient_id,name,status,consult_type,type,profile_pic,profile_pic_thumb,text_notification_count);
                            instantItemArrayList.add(instantItem);

                        }

                        rvInstantCnsltn.setAdapter(null);
                        rvInstantCnsltn.invalidate();

                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
                        instantAdapter = new InstantAdapter(getActivity(),instantItemArrayList, new InstantAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(InstantItem instantItem) {

                            }
                        });
                        rvInstantCnsltn.setLayoutManager(layoutManager);
                        rvInstantCnsltn.setAdapter(instantAdapter);

                        JSONArray regular = data.getJSONArray("regular");
                        for (int i=0;i<regular.length();i++){

                            JSONObject object = regular.getJSONObject(i);

                            String date = object.getString("date");
                            String count = object.getString("count");

                            ArrayList<CnsltnTimeItem> cnsltnTimeItems = new ArrayList<>();

                            JSONArray morning = object.getJSONArray("morning");
                            ArrayList<CnsltnItem> morningItems = new ArrayList<>();
                            for (int j=0;j<morning.length();j++){

                                JSONObject objmrng = morning.getJSONObject(j);

                                String consultation_id = objmrng.getString("consultation_id");
                                String patient_id = objmrng.getString("patient_id");
                                String name = objmrng.getString("name");
                                String status = objmrng.getString("status");
                                String consult_type = objmrng.getString("consult_type");
                                String consultation_time = objmrng.getString("consult_time");
                                String type = objmrng.getString("type");
                                String profile_pic = objmrng.getString("profile_pic");
                                String profile_pic_thumb = objmrng.getString("profile_pic_thumb");

                                cnsltnItem = new CnsltnItem(consultation_id,patient_id,name,status,consult_type,type,profile_pic,profile_pic_thumb,consultation_time);
                                morningItems.add(cnsltnItem);
                            }

                            cnsltnTimeItem = new CnsltnTimeItem(false,"Morning",morning.length()+"",morningItems);
                            cnsltnTimeItems.add(cnsltnTimeItem);

                            JSONArray afternoon = object.getJSONArray("afternoon");
                            ArrayList<CnsltnItem> afternoonItems = new ArrayList<>();
                            for (int j=0;j<afternoon.length();j++){

                                JSONObject objmrng = afternoon.getJSONObject(j);

                                String consultation_id = objmrng.getString("consultation_id");
                                String patient_id = objmrng.getString("patient_id");
                                String name = objmrng.getString("name");
                                String status = objmrng.getString("status");
                                String consult_type = objmrng.getString("consult_type");
                                String consultation_time = objmrng.getString("consult_time");
                                String type = objmrng.getString("type");
                                String profile_pic = objmrng.getString("profile_pic");
                                String profile_pic_thumb = objmrng.getString("profile_pic_thumb");

                                cnsltnItem = new CnsltnItem(consultation_id,patient_id,name,status,consult_type,type,profile_pic,profile_pic_thumb,consultation_time);
                                afternoonItems.add(cnsltnItem);
                            }

                            cnsltnTimeItem = new CnsltnTimeItem(false,"Afternoon",afternoon.length()+"",afternoonItems);
                            cnsltnTimeItems.add(cnsltnTimeItem);

                            JSONArray evening = object.getJSONArray("evening");
                            ArrayList<CnsltnItem> eveningItems = new ArrayList<>();
                            for (int j=0;j<evening.length();j++){

                                JSONObject objmrng = evening.getJSONObject(j);

                                String consultation_id = objmrng.getString("consultation_id");
                                String patient_id = objmrng.getString("patient_id");
                                String name = objmrng.getString("name");
                                String status = objmrng.getString("status");
                                String consult_type = objmrng.getString("consult_type");
                                String consultation_time = objmrng.getString("consult_time");
                                String type = objmrng.getString("type");
                                String profile_pic = objmrng.getString("profile_pic");
                                String profile_pic_thumb = objmrng.getString("profile_pic_thumb");

                                cnsltnItem = new CnsltnItem(consultation_id,patient_id,name,status,consult_type,type,profile_pic,profile_pic_thumb,consultation_time);
                                eveningItems.add(cnsltnItem);
                            }

                            cnsltnTimeItem = new CnsltnTimeItem(false,"Evening",evening.length()+"",eveningItems);
                            cnsltnTimeItems.add(cnsltnTimeItem);

                            cnsltnMainItem = new CnsltnMainItem(count,cnsltnTimeItems);
                            cnsltnMainItemHashMap.put(date,cnsltnMainItem);

                        }

                        showConsultation(topDate);

                        JSONArray forums = data.getJSONArray("forums");
                        for (int i=0;i<forums.length();i++){

                            JSONObject jsonObject = forums.getJSONObject(i);

                            String forum_id = jsonObject.getString("forum_id");
                            String forum_name = jsonObject.getString("forum_name");
                            String members = jsonObject.getString("members");
                            String join_status = jsonObject.getString("join_status");

                            forumItem = new ForumItem(forum_id,forum_name,members,join_status);
                            forumItemArrayList.add(forumItem);

                        }

                        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                        forumAdapter = new ForumAdapter(getActivity(),forumItemArrayList, new ForumAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(ForumItem forumItem) {

                            }
                        });
                        rvDctrFrm.setLayoutManager(layoutManager1);
                        rvDctrFrm.setAdapter(forumAdapter);

                        prgLoading.setVisibility(View.GONE);


                    }else {
                        String message = response.getString("message");
                        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getActivity(),getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(getActivity(),getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(),getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }
                prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(GETCNSLTNHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void showConsultation(Date slctdDate) {
        selectedDate = slctdDate;
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(slctdDate);
        topDate = slctdDate;

        if (cnsltnMainItemHashMap.containsKey(date)){

            CnsltnMainItem cnsltnMainItem = cnsltnMainItemHashMap.get(date);
            cnsltnTimeItemArrayList.clear();
            cnsltnTimeItemArrayList.addAll(cnsltnMainItem.getCnsltnTimeItems());

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.VERTICAL,false);
            cncltnMainAdapter = new CncltnMainAdapter(getActivity(),cnsltnTimeItemArrayList);
            rvReglrCnsltn.setLayoutManager(layoutManager);
            rvReglrCnsltn.setAdapter(cncltnMainAdapter);
            rvReglrCnsltn.invalidate();

        }else {
            cnsltnTimeItemArrayList.clear();
            rvReglrCnsltn.setAdapter(null);
        }

    }
}