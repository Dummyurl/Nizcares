package com.indglobal.nizcare.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.indglobal.nizcare.R;
import com.indglobal.nizcare.adapters.CityAdapter;
import com.indglobal.nizcare.adapters.CountryAdapter;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.commons.CustomRequest;
import com.indglobal.nizcare.commons.RippleView;
import com.indglobal.nizcare.commons.VolleyJSONRequest;
import com.indglobal.nizcare.commons.VolleyMultipartRequest;
import com.indglobal.nizcare.commons.VolleySingleton;
import com.indglobal.nizcare.model.CityItem;
import com.indglobal.nizcare.model.CountryItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by readyassist on 1/25/18.
 */

public class AddBankActivity extends Activity implements RippleView.OnRippleCompleteListener{

    ProgressBar prgLoading;
    RippleView rplBack,rplAddBank;
    RelativeLayout rlMain;
    Spinner spinCountry,spinAcntType;
    EditText etAcntHldrName,etAcntNumbr,etIfsc,etBnkMcr,etPanCard;
    ImageView ivTick;
    TextView tvBnkAdrs;
    Switch swtchAvlbl;

    String isEdit,bank_id,country,bank_name,account_holder_name,account_number,account_type,ifsc,
            bank_address,micr,pan_card_no,default_pay="0";

    CountryItem countryItem;
    ArrayList<CountryItem> countryItemArrayList = new ArrayList<>();
    CountryAdapter countryAdapter;

    boolean fromAdapter = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bank_main);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        rplBack = (RippleView)findViewById(R.id.rplBack);
        rlMain = (RelativeLayout)findViewById(R.id.rlMain);
        spinCountry = (Spinner)findViewById(R.id.spinCountry);
        etAcntHldrName = (EditText)findViewById(R.id.etAcntHldrName);
        etAcntNumbr = (EditText)findViewById(R.id.etAcntNumbr);
        spinAcntType = (Spinner)findViewById(R.id.spinAcntType);
        etIfsc = (EditText)findViewById(R.id.etIfsc);
        ivTick = (ImageView)findViewById(R.id.ivTick);
        tvBnkAdrs = (TextView)findViewById(R.id.tvBnkAdrs);
        etBnkMcr = (EditText)findViewById(R.id.etBnkMcr);
        etPanCard = (EditText)findViewById(R.id.etPanCard);
        swtchAvlbl = (Switch)findViewById(R.id.swtchAvlbl);
        rplAddBank = (RippleView)findViewById(R.id.rplAddBank);

        ArrayAdapter adapterAcntType = ArrayAdapter.createFromResource(this, R.array.acntType_array, R.layout.spinner_dropdown_item);
        adapterAcntType.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinAcntType.setAdapter(adapterAcntType);

        swtchAvlbl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    default_pay = "1";
                }else {
                    default_pay = "0";
                }
            }
        });

        Intent ii = getIntent();
        isEdit = ii.getStringExtra("isEdit");
        if (isEdit.equalsIgnoreCase("1")){
            bank_id = ii.getStringExtra("bank_id");
            country = ii.getStringExtra("country");
            bank_name = ii.getStringExtra("bank_name");
            account_holder_name = ii.getStringExtra("account_holder_name");
            account_number = ii.getStringExtra("account_number");
            account_type = ii.getStringExtra("account_type");
            ifsc = ii.getStringExtra("ifsc");
            bank_address = ii.getStringExtra("bank_address");
            micr = ii.getStringExtra("micr");
            pan_card_no = ii.getStringExtra("pan_card_no");
            default_pay = ii.getStringExtra("default_pay");
        }

        etIfsc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                etIfsc.setError(null);
                tvBnkAdrs.setText("");
                ivTick.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ss = etIfsc.getText().toString().toLowerCase();
                if (ss.length()>10){
                    if (fromAdapter){
                        fromAdapter = false;
                    }else {
                        if (!Comman.isConnectionAvailable(AddBankActivity.this)){
                            Toast.makeText(AddBankActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                        }else {
                            prgLoading.setVisibility(View.VISIBLE);
                            searchIfsc(ss);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (!Comman.isConnectionAvailable(AddBankActivity.this)){
            rlMain.setVisibility(View.GONE);
            Toast.makeText(AddBankActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
        }else {
            prgLoading.setVisibility(View.VISIBLE);
            getCountry();
        }

        rplBack.setOnRippleCompleteListener(this);
        rplAddBank.setOnRippleCompleteListener(this);
    }

    @Override
    public void onComplete(RippleView rippleView) {
        int id = rippleView.getId();

        switch (id){
            case R.id.rplBack:
                onBackPressed();
                break;

            case R.id.rplAddBank:
                String country = "";
                if (countryItemArrayList.size()!=0){
                    country = countryItemArrayList.get(spinCountry.getSelectedItemPosition()).getName();
                }
                account_holder_name = etAcntHldrName.getText().toString();
                account_number = etAcntNumbr.getText().toString();
                account_type = spinAcntType.getSelectedItem().toString();
                ifsc = etIfsc.getText().toString();
                bank_address = tvBnkAdrs.getText().toString();
                micr = etBnkMcr.getText().toString();
                pan_card_no = etPanCard.getText().toString();

                if (!Comman.isConnectionAvailable(AddBankActivity.this)){
                    Toast.makeText(AddBankActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                }else if (country.equalsIgnoreCase("")){
                    Toast.makeText(AddBankActivity.this,getResources().getString(R.string.contriesnotfound),Toast.LENGTH_SHORT).show();
                }else if (account_holder_name.equalsIgnoreCase("")){
                    etAcntHldrName.setError(getResources().getString(R.string.prvdacnthldrname));
                    etAcntHldrName.requestLayout();
                }else if (account_number.length()<6){
                    etAcntNumbr.setError(getResources().getString(R.string.entrAcntNumber));
                    etAcntNumbr.requestLayout();
                }else if (ifsc.length()<11){
                    etIfsc.setError(getResources().getString(R.string.provideifsc));
                    etIfsc.requestLayout();
                }else if (bank_address.equalsIgnoreCase("")){
                    etIfsc.setError(getResources().getString(R.string.provideifsc));
                    etIfsc.requestLayout();
                }else if (micr.equalsIgnoreCase("")){
                    etBnkMcr.setError(getResources().getString(R.string.providemicr));
                    etBnkMcr.requestLayout();
                }else if (pan_card_no.length()<7){
                    etPanCard.setError(getResources().getString(R.string.prvdPan));
                    etPanCard.requestLayout();
                }else {
                    prgLoading.setVisibility(View.VISIBLE);
                    if (isEdit.equalsIgnoreCase("0")){
                        prgLoading.setVisibility(View.VISIBLE);
                        addBankAcnt(country,bank_name,account_holder_name,account_number,account_type,
                                ifsc,bank_address,micr,pan_card_no,default_pay);
                    }else {
                        prgLoading.setVisibility(View.VISIBLE);
                        updateBankAcnt(bank_id,country,bank_name,account_holder_name,account_number,account_type,
                                ifsc,bank_address,micr,pan_card_no,default_pay);
                    }
                }

                break;
        }
    }

    private void getCountry() {

        String url = getResources().getString(R.string.getContriesApi);

        String CONTRYHIT = "contries_hit";
        VolleySingleton.getInstance(AddBankActivity.this).cancelRequestInQueue(CONTRYHIT);
        VolleyJSONRequest request = new VolleyJSONRequest(Request.Method.GET, url, null, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                try {
                    JSONObject response  = new JSONObject(result);
                    boolean isSuccess = response.getBoolean("success");

                    if (isSuccess){

                        countryItemArrayList.clear();

                        int pstn = 0;

                        JSONArray data = response.getJSONArray("data");
                        for (int i=0;i<data.length();i++){

                            JSONObject object = data.getJSONObject(i);

                            String country_id = object.getString("country_id");
                            String country_name = object.getString("country_name");

                            if (isEdit.equalsIgnoreCase("1")){
                                if (country_name.equalsIgnoreCase(country)){
                                    pstn = i;
                                }
                            }

                            countryItem = new CountryItem(country_id,country_name);
                            countryItemArrayList.add(countryItem);
                        }

                        countryAdapter = new CountryAdapter(AddBankActivity.this,countryItemArrayList);
                        spinCountry.setAdapter(countryAdapter);

                        if (isEdit.equalsIgnoreCase("1")){
                            spinCountry.setSelection(pstn);
                            if (account_type.equalsIgnoreCase("Current")||account_type.equalsIgnoreCase("Savings")){
                                spinAcntType.setSelection(0);
                            }else {
                                spinAcntType.setSelection(1);
                            }

                            etAcntHldrName.setText(account_holder_name);
                            etAcntNumbr.setText(account_number);
                            fromAdapter = true;
                            etIfsc.setText(ifsc);
                            tvBnkAdrs.setText(bank_address);
                            ivTick.setVisibility(View.VISIBLE);
                            etBnkMcr.setText(micr);
                            etPanCard.setText(pan_card_no);
                            if (default_pay.equalsIgnoreCase("1")){
                                swtchAvlbl.setChecked(true);
                            }else {
                                swtchAvlbl.setChecked(false);
                            }
                        }

                        rlMain.setVisibility(View.VISIBLE);
                        prgLoading.setVisibility(View.GONE);

                    }else {
                        Toast.makeText(AddBankActivity.this,getResources().getString(R.string.contriesnotfound),Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(AddBankActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                prgLoading.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddBankActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                prgLoading.setVisibility(View.GONE);
            }
        });

        request.setTag(CONTRYHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(AddBankActivity.this).addToRequestQueue(request);
    }

    private void searchIfsc(String code) {

        String url = getResources().getString(R.string.srchIfscApi);
        url = url+code;

        String IFSCHIT = "ifsc_hit";
        VolleySingleton.getInstance(AddBankActivity.this).cancelRequestInQueue(IFSCHIT);
        VolleyJSONRequest request = new VolleyJSONRequest(Request.Method.GET, url, null, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                try {
                    JSONObject response  = new JSONObject(result);

                    if (response.has("ADDRESS")){

                        String ADDRESS = response.getString("ADDRESS");
                        bank_name = response.getString("BANK");
                        tvBnkAdrs.setText(ADDRESS);
                        ivTick.setVisibility(View.VISIBLE);

                        prgLoading.setVisibility(View.GONE);

                    }else {
                        Toast.makeText(AddBankActivity.this,getResources().getString(R.string.ifscnotfound),Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(AddBankActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                prgLoading.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddBankActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                prgLoading.setVisibility(View.GONE);
            }
        });

        request.setTag(IFSCHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(AddBankActivity.this).addToRequestQueue(request);
    }

    private void addBankAcnt(String country,String bank_name,String account_holder_name,String account_number,
                             String account_type,String ifsc,String bank_address,String micr,String pan_card_no,
                             String default_pay) {

        String url = getResources().getString(R.string.addBankApi);
        String token = Comman.getPreferences(AddBankActivity.this,"token");
        url = url+"?token="+token;

        String ADDBANKHIT = "add_bank_hit";

        Map<String, String> params = new HashMap<>();
        params.put("country",country);
        params.put("bank_name",bank_name);
        params.put("account_holder_name",account_holder_name);
        params.put("account_number",account_number);
        params.put("account_type",account_type);
        params.put("ifsc",ifsc);
        params.put("bank_address",bank_address);
        params.put("micr",micr);
        params.put("pan_card_no",pan_card_no);
        params.put("default_pay",default_pay);

        VolleySingleton.getInstance(AddBankActivity.this).cancelRequestInQueue(ADDBANKHIT);
        CustomRequest request = new CustomRequest(Request.Method.POST, url,params,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    boolean success = response.getBoolean("success");
                    String message = response.getString("message");

                    if (success){

                        Toast.makeText(AddBankActivity.this,message,Toast.LENGTH_SHORT).show();
                        Comman.setPreferences(AddBankActivity.this,"BankListUpdated","1");
                        prgLoading.setVisibility(View.GONE);
                        onBackPressed();

                    }else {
                        Toast.makeText(AddBankActivity.this,message,Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(AddBankActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(AddBankActivity.this,message,Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(AddBankActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(AddBankActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }
                prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(ADDBANKHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(AddBankActivity.this).addToRequestQueue(request);
    }

    private void updateBankAcnt(String bank_id,String country,String bank_name,String account_holder_name,String account_number,
                             String account_type,String ifsc,String bank_address,String micr,String pan_card_no,
                             String default_pay) {

        String url = getResources().getString(R.string.updateBankApi);
        String token = Comman.getPreferences(AddBankActivity.this,"token");
        url = url+"?token="+token;

        String UPDTEBANKHIT = "update_bank_hit";

        Map<String, String> params = new HashMap<>();
        params.put("bank_id",bank_id);
        params.put("country",country);
        params.put("bank_name",bank_name);
        params.put("account_holder_name",account_holder_name);
        params.put("account_number",account_number);
        params.put("account_type",account_type);
        params.put("ifsc",ifsc);
        params.put("bank_address",bank_address);
        params.put("micr",micr);
        params.put("pan_card_no",pan_card_no);
        params.put("default_pay",default_pay);

        VolleySingleton.getInstance(AddBankActivity.this).cancelRequestInQueue(UPDTEBANKHIT);
        CustomRequest request = new CustomRequest(Request.Method.POST, url,params,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    boolean success = response.getBoolean("success");
                    String message = response.getString("message");

                    if (success){

                        Toast.makeText(AddBankActivity.this,message,Toast.LENGTH_SHORT).show();
                        Comman.setPreferences(AddBankActivity.this,"BankListUpdated","1");
                        prgLoading.setVisibility(View.GONE);
                        onBackPressed();

                    }else {
                        Toast.makeText(AddBankActivity.this,message,Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(AddBankActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(AddBankActivity.this,message,Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(AddBankActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(AddBankActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }
                prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(UPDTEBANKHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(AddBankActivity.this).addToRequestQueue(request);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

}
