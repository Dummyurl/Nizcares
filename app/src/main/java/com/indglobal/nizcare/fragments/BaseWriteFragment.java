package com.indglobal.nizcare.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
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
import com.indglobal.nizcare.activities.BaseActivity;
import com.indglobal.nizcare.activities.PrivateAnswerActivity;
import com.indglobal.nizcare.activities.SpecialityActivity;
import com.indglobal.nizcare.activities.WriteHealthTipsActivity;
import com.indglobal.nizcare.adapters.ApointMainAdapter;
import com.indglobal.nizcare.adapters.EnquiryAdapter;
import com.indglobal.nizcare.adapters.HealthFeedAdapter;
import com.indglobal.nizcare.adapters.PrivateChatAdapter;
import com.indglobal.nizcare.adapters.PublicChatAdapter;
import com.indglobal.nizcare.adapters.QaAdapter;
import com.indglobal.nizcare.adapters.SpecialityAdapter;
import com.indglobal.nizcare.adapters.SpinSpecAdapter;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.commons.VolleyJSONRequest;
import com.indglobal.nizcare.commons.VolleySingleton;
import com.indglobal.nizcare.model.AnswerItem;
import com.indglobal.nizcare.model.CheckSpecialityItem;
import com.indglobal.nizcare.model.EnquiryItem;
import com.indglobal.nizcare.model.HealthFeedItem;
import com.indglobal.nizcare.model.PatientItem;
import com.indglobal.nizcare.model.PrivateChatItem;
import com.indglobal.nizcare.model.PublicChatItem;
import com.indglobal.nizcare.model.QAItem;
import com.indglobal.nizcare.model.ReplyItem;
import com.indglobal.nizcare.model.SpecialityItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by readyassist on 12/16/17.
 */

public class BaseWriteFragment extends Fragment implements View.OnClickListener{

    LayoutInflater inflater;
    ProgressBar prgLoading;
    TextView tvTabQstns,tvTabEnqrs,tvTabHlth,tvTabQA,tvIndQstns,tvIndEnqrs,tvIndHelth,tvIndQa,tvWriteHealth;
    LinearLayout llMain,llQstns,llHealthfeed;
    RecyclerView rvQuestns,rvEnquirs,rvHealthfds,rvQA;
    RelativeLayout rlQA;
    Spinner spinLastDays,spinAllSpeclts;
    RadioGroup rgChat;

    SpecialityItem specialityItem;
    ArrayList<SpecialityItem> specialityItemArrayList = new ArrayList<>();
    SpinSpecAdapter spinSpecAdapter;

    PublicChatItem publicChatItem;
    ArrayList<PublicChatItem> publicChatItemArrayList = new ArrayList<>();
    PublicChatAdapter publicChatAdapter;

    PrivateChatItem privateChatItem;
    ArrayList<PrivateChatItem> privateChatItemArrayList = new ArrayList<>();
    PrivateChatAdapter privateChatAdapter;

    EnquiryItem enquiryItem;
    ArrayList<EnquiryItem> enquiryItemArrayList = new ArrayList<>();
    EnquiryAdapter enquiryAdapter;

    HealthFeedItem healthFeedItem;
    ArrayList<HealthFeedItem> healthFeedItemArrayList = new ArrayList<>();
    HealthFeedAdapter healthFeedAdapter;

    QAItem qaItem;
    ArrayList<QAItem> qaItemArrayList = new ArrayList<>();
    QaAdapter qaAdapter;

    private boolean isViewShown = false,fromRadio = false;
    int chatType = 1;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        inflater = (LayoutInflater) activity.getLayoutInflater();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_write_fragment, container, false);

        prgLoading = (ProgressBar)view.findViewById(R.id.prgLoading);
        tvTabQstns = (TextView)view.findViewById(R.id.tvTabQstns);
        tvTabEnqrs = (TextView)view.findViewById(R.id.tvTabEnqrs);
        tvTabHlth = (TextView)view.findViewById(R.id.tvTabHlth);
        tvTabQA = (TextView)view.findViewById(R.id.tvTabQA);
        tvIndQstns = (TextView)view.findViewById(R.id.tvIndQstns);
        tvIndEnqrs = (TextView)view.findViewById(R.id.tvIndEnqrs);
        tvIndHelth = (TextView)view.findViewById(R.id.tvIndHelth);
        tvIndQa = (TextView)view.findViewById(R.id.tvIndQa);
        tvWriteHealth = (TextView)view.findViewById(R.id.tvWriteHealth);

        llMain = (LinearLayout)view.findViewById(R.id.llMain);
        llQstns = (LinearLayout) view.findViewById(R.id.llQstns);
        llHealthfeed = (LinearLayout) view.findViewById(R.id.llHealthfeed);
        rvQuestns = (RecyclerView)view.findViewById(R.id.rvQuestns);
        rvEnquirs = (RecyclerView)view.findViewById(R.id.rvEnquirs);
        rvHealthfds = (RecyclerView)view.findViewById(R.id.rvHealthfds);
        rvQA = (RecyclerView)view.findViewById(R.id.rvQA);
        rlQA = (RelativeLayout)view.findViewById(R.id.rlQA);
        rgChat = (RadioGroup)view.findViewById(R.id.rgChat);
        spinLastDays = (Spinner) view.findViewById(R.id.spinLastDays);
        spinAllSpeclts = (Spinner)view.findViewById(R.id.spinAllSpeclts);

        ArrayAdapter adapterDays = ArrayAdapter.createFromResource(getActivity(), R.array.days_array, R.layout.spinner_dropdown_item_green);
        adapterDays.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinLastDays.setAdapter(adapterDays);

        initMethod();

        rgChat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.rbPblc:
                        chatType = 1;
                        spinLastDays.setSelection(0);
                        if (spinAllSpeclts.getSelectedItemPosition()==0){
                            chatFunction();
                        }else {
                            spinAllSpeclts.setSelection(0);
                        }
                        break;

                    case R.id.rbPrvt:
                        chatType = 2;
                        spinLastDays.setSelection(0);
                        if (spinAllSpeclts.getSelectedItemPosition()==0){
                            chatFunction();
                        }else {
                            spinAllSpeclts.setSelection(0);
                        }
                        break;
                }
            }
        });

        spinAllSpeclts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) view.findViewById(R.id.text1)).setTextColor(getResources().getColor(R.color.lightGreen));
                chatFunction();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tvTabQstns.setOnClickListener(this);
        tvTabEnqrs.setOnClickListener(this);
        tvTabHlth.setOnClickListener(this);
        tvTabQA.setOnClickListener(this);
        tvWriteHealth.setOnClickListener(this);

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (getView() != null) {
                isViewShown = true;
                if (!Comman.isConnectionAvailable(getActivity())){
                    llMain.setVisibility(View.GONE);
                    Toast.makeText(getActivity(),getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                }else {
                    prgLoading.setVisibility(View.VISIBLE);
                    getSpecialities();
                }
            } else {
                isViewShown = false;
            }
        }

    }

    public void initMethod() {
        int crnt = BaseActivity.viewPager.getCurrentItem();
        if (crnt == 2) {
            if (!isViewShown) {
                if (!Comman.isConnectionAvailable((BaseActivity)getActivity())){
                    llMain.setVisibility(View.GONE);
                    Toast.makeText(getActivity(),getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                }else {
                    prgLoading.setVisibility(View.VISIBLE);
                    getSpecialities();
                }
            }
        }
    }

    public void chatFunction(){

        String days = spinLastDays.getSelectedItem().toString();
        String speciality_id = specialityItemArrayList.get(spinAllSpeclts.getSelectedItemPosition()).getId();
        days = days.replace(" Days","");

        if (!Comman.isConnectionAvailable(getActivity())){
            Toast.makeText(getActivity(),getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
        }else {
            prgLoading.setVisibility(View.VISIBLE);
            if (chatType==1){
                getPublicChats(days,speciality_id);
            }else {
                getPrivateChats(days,speciality_id);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){

            case R.id.tvTabQstns:
                rlQA.setVisibility(View.GONE);
                rvEnquirs.setVisibility(View.GONE);
                llHealthfeed.setVisibility(View.GONE);
                llQstns.setVisibility(View.VISIBLE);
                tvTabQA.setTextColor(getResources().getColor(R.color.lightBlack));
                tvIndQa.setBackgroundColor(getResources().getColor(R.color.lightGray));
                tvTabHlth.setTextColor(getResources().getColor(R.color.lightBlack));
                tvIndHelth.setBackgroundColor(getResources().getColor(R.color.lightGray));
                tvTabEnqrs.setTextColor(getResources().getColor(R.color.lightBlack));
                tvIndEnqrs.setBackgroundColor(getResources().getColor(R.color.lightGray));
                tvTabQstns.setTextColor(getResources().getColor(R.color.lightGreen));
                tvIndQstns.setBackgroundColor(getResources().getColor(R.color.lightGreen));
                break;

            case R.id.tvTabEnqrs:
                rlQA.setVisibility(View.GONE);
                llHealthfeed.setVisibility(View.GONE);
                llQstns.setVisibility(View.GONE);
                rvEnquirs.setVisibility(View.VISIBLE);
                tvTabQA.setTextColor(getResources().getColor(R.color.lightBlack));
                tvIndQa.setBackgroundColor(getResources().getColor(R.color.lightGray));
                tvTabHlth.setTextColor(getResources().getColor(R.color.lightBlack));
                tvIndHelth.setBackgroundColor(getResources().getColor(R.color.lightGray));
                tvTabEnqrs.setTextColor(getResources().getColor(R.color.lightGreen));
                tvIndEnqrs.setBackgroundColor(getResources().getColor(R.color.lightGreen));
                tvTabQstns.setTextColor(getResources().getColor(R.color.lightBlack));
                tvIndQstns.setBackgroundColor(getResources().getColor(R.color.lightGray));
                if (!Comman.isConnectionAvailable(getActivity())){
                    Toast.makeText(getActivity(),getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                }else {
                    prgLoading.setVisibility(View.VISIBLE);
                    getEnquiries();
                }
                break;

            case R.id.tvTabHlth:
                rlQA.setVisibility(View.GONE);
                llQstns.setVisibility(View.GONE);
                rvEnquirs.setVisibility(View.GONE);
                llHealthfeed.setVisibility(View.VISIBLE);
                tvTabQA.setTextColor(getResources().getColor(R.color.lightBlack));
                tvIndQa.setBackgroundColor(getResources().getColor(R.color.lightGray));
                tvTabHlth.setTextColor(getResources().getColor(R.color.lightGreen));
                tvIndHelth.setBackgroundColor(getResources().getColor(R.color.lightGreen));
                tvTabEnqrs.setTextColor(getResources().getColor(R.color.lightBlack));
                tvIndEnqrs.setBackgroundColor(getResources().getColor(R.color.lightGray));
                tvTabQstns.setTextColor(getResources().getColor(R.color.lightBlack));
                tvIndQstns.setBackgroundColor(getResources().getColor(R.color.lightGray));
                if (!Comman.isConnectionAvailable(getActivity())){
                    Toast.makeText(getActivity(),getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                }else {
                    prgLoading.setVisibility(View.VISIBLE);
                    getHealthFeeds();
                }
                break;

            case R.id.tvTabQA:
                llQstns.setVisibility(View.GONE);
                rvEnquirs.setVisibility(View.GONE);
                llHealthfeed.setVisibility(View.GONE);
                rlQA.setVisibility(View.VISIBLE);
                tvTabQA.setTextColor(getResources().getColor(R.color.lightGreen));
                tvIndQa.setBackgroundColor(getResources().getColor(R.color.lightGreen));
                tvTabHlth.setTextColor(getResources().getColor(R.color.lightBlack));
                tvIndHelth.setBackgroundColor(getResources().getColor(R.color.lightGray));
                tvTabEnqrs.setTextColor(getResources().getColor(R.color.lightBlack));
                tvIndEnqrs.setBackgroundColor(getResources().getColor(R.color.lightGray));
                tvTabQstns.setTextColor(getResources().getColor(R.color.lightBlack));
                tvIndQstns.setBackgroundColor(getResources().getColor(R.color.lightGray));
                if (!Comman.isConnectionAvailable(getActivity())){
                    Toast.makeText(getActivity(),getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                }else {
                    prgLoading.setVisibility(View.VISIBLE);
                    getQA();
                }
                break;

            case R.id.tvWriteHealth:
                Intent ii = new Intent(getActivity(),WriteHealthTipsActivity.class);
                startActivity(ii);
                break;
        }
    }

    private void getSpecialities() {

        String url = getResources().getString(R.string.getSpeclitiesApi);

        String GETSPECHIT = "get_spin_speclties_hit";
        VolleySingleton.getInstance(getActivity()).cancelRequestInQueue(GETSPECHIT);
        VolleyJSONRequest request = new VolleyJSONRequest(Request.Method.GET, url,null, null,new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                try {
                    JSONObject response  = new JSONObject(result);
                    boolean success = response.getBoolean("success");

                    if (success){

                        specialityItemArrayList.clear();

                        specialityItem = new SpecialityItem("","All Specialities");
                        specialityItemArrayList.add(specialityItem);

                        JSONArray data = response.getJSONArray("data");
                        for (int i=0;i<data.length();i++){
                            JSONObject object = data.getJSONObject(i);

                            String id = object.getString("master_speciality_id");
                            String name = object.getString("master_speciality_name");

                            specialityItem = new SpecialityItem(id,name);
                            specialityItemArrayList.add(specialityItem);
                        }


                        spinSpecAdapter = new SpinSpecAdapter(getActivity(),specialityItemArrayList);
                        spinAllSpeclts.setAdapter(spinSpecAdapter);
                        prgLoading.setVisibility(View.GONE);

                        llMain.setVisibility(View.VISIBLE);

                    }else {
                        Toast.makeText(getActivity(),getResources().getString(R.string.noFound),Toast.LENGTH_SHORT).show();
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
                if (error.networkResponse.data!=null){
                    try {

                        String jsonString = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        JSONObject errObject = new JSONObject(jsonString);

                        String message = errObject.getString("message");

                        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Toast.makeText(getActivity(),getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getActivity(),getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(GETSPECHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void getPublicChats(String days,String speciality_id) {

        String url = getResources().getString(R.string.getPblcGrpsApi);
        String token = Comman.getPreferences(getActivity(),"token");
        url = url+"?token="+token+"&days="+days+"&speciality_id="+speciality_id;

        String GETPUBLICSHIT = "get_publc_grps_hit";
        VolleySingleton.getInstance(getActivity()).cancelRequestInQueue(GETPUBLICSHIT);
        VolleyJSONRequest request = new VolleyJSONRequest(Request.Method.GET, url,null, null,new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                try {
                    JSONObject response  = new JSONObject(result);
                    boolean success = response.getBoolean("success");

                    if (success){

                        publicChatItemArrayList.clear();

                        JSONArray data = response.getJSONArray("data");
                        for (int i=0;i<data.length();i++){

                            JSONObject object = data.getJSONObject(i);

                            String group_id = object.getString("group_id");
                            String group_name = object.getString("group_name");
                            String members = object.getString("members");
                            String join_status = object.getString("join_status");

                            publicChatItem = new PublicChatItem(group_id,group_name,members,join_status);
                            publicChatItemArrayList.add(publicChatItem);

                        }

                        rvQuestns.setAdapter(null);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                        publicChatAdapter = new PublicChatAdapter(getActivity(),publicChatItemArrayList, new PublicChatAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(PublicChatItem publicChatItem) {

                            }
                        });
                        rvQuestns.setLayoutManager(layoutManager);
                        rvQuestns.setAdapter(publicChatAdapter);
                        rvQuestns.invalidate();
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
        request.setTag(GETPUBLICSHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void getPrivateChats(String days,String speciality_id) {

        String url = getResources().getString(R.string.getPrvteGrpsApi);
        String token = Comman.getPreferences(getActivity(),"token");
        url = url+"?token="+token+"&days="+days+"&speciality_id="+speciality_id;

        String GETPRIVTSSHIT = "get_prvte_grps_hit";
        VolleySingleton.getInstance(getActivity()).cancelRequestInQueue(GETPRIVTSSHIT);
        VolleyJSONRequest request = new VolleyJSONRequest(Request.Method.GET, url,null, null,new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                try {
                    JSONObject response  = new JSONObject(result);
                    boolean success = response.getBoolean("success");

                    if (success){

                        privateChatItemArrayList.clear();

                        JSONArray data = response.getJSONArray("data");
                        for (int i=0;i<data.length();i++){

                            JSONObject object = data.getJSONObject(i);

                            String question_id = object.getString("question_id");
                            String patient_id = object.getString("patient_id");
                            String name = object.getString("name");
                            String gender = object.getString("gender");
                            String age = object.getString("age");
                            String title = object.getString("title");
                            String description = object.getString("description");
                            String time_ago = object.getString("time_ago");

                            privateChatItem = new PrivateChatItem(question_id,patient_id,name,gender,age,title,description,time_ago);
                            privateChatItemArrayList.add(privateChatItem);

                        }

                        rvQuestns.setAdapter(null);
                        rvQuestns.invalidate();

                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                        privateChatAdapter = new PrivateChatAdapter(getActivity(),privateChatItemArrayList, new PrivateChatAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(PrivateChatItem privateChatItem) {
                                Intent ii = new Intent(getActivity(),PrivateAnswerActivity.class);
                                ii.putExtra("id",privateChatItem.getQuestion_id());
                                ii.putExtra("title",privateChatItem.getTitle());
                                ii.putExtra("descrptn",privateChatItem.getDescription());
                                ii.putExtra("name",privateChatItem.getName());
                                ii.putExtra("gender",privateChatItem.getGender());
                                ii.putExtra("age",privateChatItem.getAge());
                                ii.putExtra("time",privateChatItem.getTime_ago());
                                startActivity(ii);
                            }
                        });
                        rvQuestns.setLayoutManager(layoutManager);
                        rvQuestns.setAdapter(privateChatAdapter);

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
        request.setTag(GETPRIVTSSHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void getEnquiries() {

        String url = getResources().getString(R.string.getEnquiriesApi);
        String token = Comman.getPreferences(getActivity(),"token");
        url = url+"?token="+token;

        String GETENQRYHIT = "get_enqry_hit";
        VolleySingleton.getInstance(getActivity()).cancelRequestInQueue(GETENQRYHIT);
        VolleyJSONRequest request = new VolleyJSONRequest(Request.Method.GET, url,null, null,new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                try {
                    JSONObject response  = new JSONObject(result);
                    boolean success = response.getBoolean("success");

                    if (success){

                        enquiryItemArrayList.clear();

                        JSONArray data = response.getJSONArray("data");
                        for (int i=0;i<data.length();i++){

                            JSONObject object = data.getJSONObject(i);

                            String enquiry_id = object.getString("enquiry_id");
                            String patient_id = object.getString("patient_id");
                            String patient_name = object.getString("patient_name");
                            String country_code = object.getString("country_code");
                            String mobile_no = object.getString("mobile_no");
                            String problem_name = object.getString("problem_name");
                            String description = object.getString("description");
                            String gender = object.getString("gender");
                            String age = object.getString("age");
                            String time_ago = object.getString("time_ago");

                            ArrayList<ReplyItem> replyItemArrayList = new ArrayList<>();
                            if (object.has("reply")){
                                JSONArray reply = object.getJSONArray("reply");
                                for (int j=0;j<reply.length();j++){
                                    JSONObject jsonObject = reply.getJSONObject(j);
                                    String reply_id = jsonObject.getString("reply_id");
                                    String doctor_id = jsonObject.getString("doctor_id");
                                    String message = jsonObject.getString("message");

                                    replyItemArrayList.add(new ReplyItem(reply_id,doctor_id,message));
                                }
                            }

                            enquiryItem = new EnquiryItem(enquiry_id,patient_id,patient_name,country_code,
                                    mobile_no,problem_name,description,gender,age,time_ago,replyItemArrayList);
                            enquiryItemArrayList.add(enquiryItem);

                        }

                        rvEnquirs.setAdapter(null);
                        rvEnquirs.invalidate();

                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                        enquiryAdapter = new EnquiryAdapter(getActivity(),enquiryItemArrayList, new EnquiryAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(EnquiryItem enquiryItem) {

                            }
                        });
                        rvEnquirs.setLayoutManager(layoutManager);
                        rvEnquirs.setAdapter(enquiryAdapter);

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
        request.setTag(GETENQRYHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void getHealthFeeds() {

        String url = getResources().getString(R.string.getHealthFeedApi);
        String token = Comman.getPreferences(getActivity(),"token");
        url = url+"?token="+token;

        String GETHEALTHHIT = "get_health_hit";
        VolleySingleton.getInstance(getActivity()).cancelRequestInQueue(GETHEALTHHIT);
        VolleyJSONRequest request = new VolleyJSONRequest(Request.Method.GET, url,null, null,new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                try {
                    JSONObject response  = new JSONObject(result);
                    boolean success = response.getBoolean("success");

                    if (success){

                        healthFeedItemArrayList.clear();

                        JSONArray data = response.getJSONArray("data");
                        for (int i=0;i<data.length();i++){

                            JSONObject object = data.getJSONObject(i);

                            String id = object.getString("id");
                            String doctor_id = object.getString("doctor_id");
                            String doctor_name = object.getString("doctor_name");
                            String profilr_pic = object.getString("profilr_pic");
                            String title = object.getString("title");
                            String post = object.getString("post");
                            String speciality_name = object.getString("speciality_name");
                            String time_ago = object.getString("time_ago");
                            String votes = object.getString("votes");

                            healthFeedItem = new HealthFeedItem(id,doctor_id,doctor_name,profilr_pic,title,post,speciality_name,time_ago,votes);
                            healthFeedItemArrayList.add(healthFeedItem);

                        }

                        rvHealthfds.setAdapter(null);
                        rvHealthfds.invalidate();

                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                        healthFeedAdapter = new HealthFeedAdapter(getActivity(),healthFeedItemArrayList, new HealthFeedAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(HealthFeedItem healthFeedItem) {

                            }
                        });
                        rvHealthfds.setLayoutManager(layoutManager);
                        rvHealthfds.setAdapter(healthFeedAdapter);

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
        request.setTag(GETHEALTHHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void getQA() {

        String url = getResources().getString(R.string.getQAApi);
        String token = Comman.getPreferences(getActivity(),"token");
        url = url+"?token="+token;

        String GETQAHIT = "get_qa_hit";
        VolleySingleton.getInstance(getActivity()).cancelRequestInQueue(GETQAHIT);
        VolleyJSONRequest request = new VolleyJSONRequest(Request.Method.GET, url,null, null,new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                try {
                    JSONObject response  = new JSONObject(result);
                    boolean success = response.getBoolean("success");

                    if (success){

                        qaItemArrayList.clear();

                        JSONArray data = response.getJSONArray("data");
                        for (int i=0;i<data.length();i++){

                            JSONObject object = data.getJSONObject(i);

                            String question_id = object.getString("question_id");
                            String title = object.getString("title");
                            String description = object.getString("description");
                            String speciality = object.getString("speciality");
                            String answers_count = object.getString("answers_count");
                            String time_ago = object.getString("time_ago");

                            ArrayList<AnswerItem> answerItemArrayList = new ArrayList<>();
                            JSONArray answers = object.getJSONArray("answers");
                            for (int j=0;j<answers.length();j++){

                                JSONObject jsonObject = answers.getJSONObject(j);

                                String doctor_id = jsonObject.getString("doctor_id");
                                String name = jsonObject.getString("name");
                                String profilr_pic = jsonObject.getString("profilr_pic");
                                String drspeciality = jsonObject.getString("speciality");
                                String ans_id = jsonObject.getString("ans_id");
                                String dranswers = jsonObject.getString("answers");
                                String drtime_ago = jsonObject.getString("time_ago");
                                String votes = jsonObject.getString("votes");

                                AnswerItem answerItem = new AnswerItem(doctor_id,name,profilr_pic,drspeciality,ans_id,dranswers,drtime_ago,votes);
                                answerItemArrayList.add(answerItem);
                            }

                            qaItem = new QAItem(question_id,title,description,speciality,answers_count,time_ago,answerItemArrayList);
                            qaItemArrayList.add(qaItem);

                        }

                        rvQA.setAdapter(null);
                        rvQA.invalidate();

                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                        qaAdapter = new QaAdapter(getActivity(),qaItemArrayList);
                        rvQA.setLayoutManager(layoutManager);
                        rvQA.setAdapter(qaAdapter);

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
        request.setTag(GETQAHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

}