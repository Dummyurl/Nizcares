package com.indglobal.nizcare.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.indglobal.nizcare.R;
import com.indglobal.nizcare.activities.AddPatientActivity;
import com.indglobal.nizcare.activities.BaseActivity;
import com.indglobal.nizcare.adapters.NewsAdapter;
import com.indglobal.nizcare.adapters.PatientAdapter;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.commons.VolleyJSONRequest;
import com.indglobal.nizcare.commons.VolleySingleton;
import com.indglobal.nizcare.model.NewsItem;
import com.indglobal.nizcare.model.PatientItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by readyassist on 12/16/17.
 */

public class BasePatientFragment extends Fragment implements PatientAdapter.OnItemClickListener,View.OnClickListener{

    LayoutInflater inflater;
    ProgressBar prgLoading;

    LinearLayout llMain;
    EditText etSearch;
    TextView tvAddNew;
    Spinner spinAlltype;
    RecyclerView rvPatients;

    boolean isViewShown = false;

    PatientItem patientItem;
    ArrayList<PatientItem> patientItemArrayList = new ArrayList<>();
    PatientAdapter patientAdapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        inflater = (LayoutInflater) activity.getLayoutInflater();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_patient_fragment, container, false);

        prgLoading = (ProgressBar)view.findViewById(R.id.prgLoading);
        llMain = (LinearLayout)view.findViewById(R.id.llMain);
        etSearch = (EditText)view.findViewById(R.id.etSearch);
        tvAddNew = (TextView)view.findViewById(R.id.tvAddNew);
        spinAlltype = (Spinner)view.findViewById(R.id.spinAlltype);
        rvPatients = (RecyclerView)view.findViewById(R.id.rvPatients);

        ArrayAdapter adapterFilter = ArrayAdapter.createFromResource(getActivity(), R.array.filter_array, R.layout.spinner_dropdown_item_green);
        adapterFilter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinAlltype.setAdapter(adapterFilter);

        patientAdapter = new PatientAdapter(getActivity(), patientItemArrayList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        rvPatients.setLayoutManager(layoutManager);
        rvPatients.setAdapter(patientAdapter);


        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                patientAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        initMethod();

        tvAddNew.setOnClickListener(this);

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
                    llMain.setVisibility(View.GONE);
                    prgLoading.setVisibility(View.VISIBLE);
                    getPatients();
                }
            } else {
                isViewShown = false;
            }
        }

    }

    public void initMethod() {
        int crnt = BaseActivity.viewPager.getCurrentItem();
        if (crnt == 1) {
            if (!isViewShown) {
                if (!Comman.isConnectionAvailable(getActivity())){
                    llMain.setVisibility(View.GONE);
                    Toast.makeText(getActivity(),getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                }else {
                    llMain.setVisibility(View.GONE);
                    prgLoading.setVisibility(View.VISIBLE);
                    getPatients();
                }
            }
        }
    }

    private void getPatients() {

        String url = getResources().getString(R.string.getPatientApi);
        String token = Comman.getPreferences(getActivity(),"token");
        url = url+"?token="+token;

        String GETPATIENTSHIT = "get_patient_hit";
        VolleySingleton.getInstance(getActivity()).cancelRequestInQueue(GETPATIENTSHIT);
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

                        llMain.setVisibility(View.VISIBLE);
                        prgLoading.setVisibility(View.GONE);


                    }else {
                        String message = response.getString("message");
                        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
                        llMain.setVisibility(View.GONE);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    llMain.setVisibility(View.GONE);
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
                llMain.setVisibility(View.GONE);
                prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(GETPATIENTSHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    @Override
    public void onItemClick(PatientItem patientItem) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.tvAddNew:
                Intent ii = new Intent(getActivity(),AddPatientActivity.class);
                startActivityForResult(ii,4);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 4) {
            if(resultCode == Activity.RESULT_OK){
                String PatientAdded = Comman.getPreferences(getActivity(),"PatientAdded");
                if (PatientAdded.equalsIgnoreCase("1")){
                    if (!Comman.isConnectionAvailable(getActivity())){
                        llMain.setVisibility(View.GONE);
                        Toast.makeText(getActivity(),getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                    }else {
                        llMain.setVisibility(View.GONE);
                        prgLoading.setVisibility(View.VISIBLE);
                        getPatients();
                        Comman.setPreferences(getActivity(),"PatientAdded","0");
                    }
                }
            }
        }
    }
}