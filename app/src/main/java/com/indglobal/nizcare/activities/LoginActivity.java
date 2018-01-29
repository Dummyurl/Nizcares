package com.indglobal.nizcare.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.indglobal.nizcare.R;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.commons.CustomRequest;
import com.indglobal.nizcare.commons.IncomingSms;
import com.indglobal.nizcare.commons.RippleView;
import com.indglobal.nizcare.commons.VolleySingleton;
import com.indglobal.nizcare.commons.countrycodes.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.toolbox.HttpHeaderParser.parseCacheHeaders;

/**
 * Created by readyassist on 12/12/17.
 */

public class LoginActivity extends Activity implements RippleView.OnRippleCompleteListener,View.OnClickListener{

    ProgressBar prgLoading;

    RippleView rplBack,rplLogin;
    LinearLayout llLogin,llSignup,llVerifctn;
    EditText etNumber,etPassword,etSNumber,etSEmail,etSPassword,etOTP;
    TextView tvForgot,tvSignup,tvLogin,tvRsndOtp;
    CountryCodePicker spinSCountry;

    String type="1";
    boolean clickedSignup = false;
    String country_code,email,mobile_no,password,role="2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        rplBack = (RippleView)findViewById(R.id.rplBack);
        llLogin = (LinearLayout)findViewById(R.id.llLogin);
        etNumber = (EditText)findViewById(R.id.etNumber);
        etPassword = (EditText)findViewById(R.id.etPassword);
        tvForgot = (TextView)findViewById(R.id.tvForgot);
        tvSignup = (TextView)findViewById(R.id.tvSignup);
        llSignup = (LinearLayout)findViewById(R.id.llSignup);
        spinSCountry = (CountryCodePicker)findViewById(R.id.spinSCountry);
        etSNumber = (EditText)findViewById(R.id.etSNumber);
        etSEmail = (EditText)findViewById(R.id.etSEmail);
        etSPassword = (EditText)findViewById(R.id.etSPassword);
        tvLogin = (TextView)findViewById(R.id.tvLogin);
        rplLogin = (RippleView)findViewById(R.id.rplLogin);
        llVerifctn = (LinearLayout)findViewById(R.id.llVerifctn);
        etOTP = (EditText)findViewById(R.id.etOTP);
        tvRsndOtp = (TextView)findViewById(R.id.tvRsndOtp);


        Intent ii = getIntent();
        type = ii.getStringExtra("type");

        if (type.equalsIgnoreCase("1")){
            llVerifctn.setVisibility(View.GONE);
            llSignup.setVisibility(View.GONE);
            llLogin.setVisibility(View.VISIBLE);
            tvLogin.setText(getResources().getString(R.string.login));
        }else {
            llVerifctn.setVisibility(View.GONE);
            llLogin.setVisibility(View.GONE);
            llSignup.setVisibility(View.VISIBLE);
            tvLogin.setText(getResources().getString(R.string.signup));
        }

        tvSignup.setOnClickListener(this);
        rplBack.setOnRippleCompleteListener(this);
        rplLogin.setOnRippleCompleteListener(this);
        tvRsndOtp.setOnClickListener(this);


        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equalsIgnoreCase("otp")) {
                    final String message = intent.getStringExtra("message");
                    etOTP.setText(message);
                    if (prgLoading.getVisibility()==View.VISIBLE){
                        Toast.makeText(LoginActivity.this,"Already in process, please wait!",Toast.LENGTH_SHORT).show();
                    }else {

                        String otp = etOTP.getText().toString();

                        if (!Comman.isConnectionAvailable(LoginActivity.this)){
                            Toast.makeText(LoginActivity.this,"Please check your internet connection!",Toast.LENGTH_SHORT).show();
                        }else if (otp.length()<6){
                            etOTP.setError("Invalid OTP!");
                            etOTP.requestFocus();
                        }else {
                            prgLoading.setVisibility(View.VISIBLE);
                            if (type.equalsIgnoreCase("3")){
                                verifyOTP(country_code,mobile_no,otp);
                            }

                        }
                    }
                }
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));

    }

    @Override
    public void onComplete(RippleView rippleView) {
        int id = rippleView.getId();

        switch (id){
            case R.id.rplBack:
                onBackPressed();
                break;

            case R.id.rplLogin:
                String bottontype = tvLogin.getText().toString();
                if (bottontype.equalsIgnoreCase(getResources().getString(R.string.login))){

                    mobile_no = etNumber.getText().toString();
                    password = etPassword.getText().toString();

                    if (!Comman.isConnectionAvailable(LoginActivity.this)){
                        Toast.makeText(LoginActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                    }else if (mobile_no.length()<8){
                        etNumber.setError("Enter valid mobile number!");
                        etNumber.requestLayout();
                    }else if (password.equalsIgnoreCase("")){
                        etPassword.setError("Provide proper password!");
                        etPassword.requestLayout();
                    }else {
                        prgLoading.setVisibility(View.VISIBLE);
                        loginDoctor(mobile_no,password);
                    }
                }else if (bottontype.equalsIgnoreCase(getResources().getString(R.string.signup))){

                    country_code = spinSCountry.getSelectedCountryCode();
                    mobile_no = etSNumber.getText().toString();
                    email = etSEmail.getText().toString();
                    password = etSPassword.getText().toString();

                    if (!Comman.isConnectionAvailable(LoginActivity.this)){
                        Toast.makeText(LoginActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                    }else if (mobile_no.length()<8){
                        etSNumber.setError("Enter valid mobile number!");
                        etSNumber.requestLayout();
                    }else if (!Comman.emailValidator(email)){
                        etSEmail.setError("Enter valid email id!");
                        etSEmail.requestLayout();
                    }else if (password.equalsIgnoreCase("")){
                        etSPassword.setError("Provide a strong password!");
                        etSPassword.requestLayout();
                    }else {
                        prgLoading.setVisibility(View.VISIBLE);
                        signupDoctor(country_code,mobile_no,email,password);
                    }
                }else {

                    String otp = etOTP.getText().toString();

                    if (!Comman.isConnectionAvailable(LoginActivity.this)){
                        Toast.makeText(LoginActivity.this,"Please check your internet connection!",Toast.LENGTH_SHORT).show();
                    }else if (otp.length()<6){
                        etOTP.setError("Invalid OTP!");
                        etOTP.requestFocus();
                    }else {
                        prgLoading.setVisibility(View.VISIBLE);
                        if (type.equalsIgnoreCase("3")){
                            verifyOTP(country_code,mobile_no,otp);
                        }
                    }
                }
                break;

        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.tvSignup:
                type = "2";
                llVerifctn.setVisibility(View.GONE);
                llLogin.setVisibility(View.GONE);
                llSignup.setVisibility(View.VISIBLE);
                tvLogin.setText(getResources().getString(R.string.signup));
                clickedSignup = true;
                break;

            case R.id.tvRsndOtp:
                if (!Comman.isConnectionAvailable(LoginActivity.this)){
                    Toast.makeText(LoginActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                }else {
                    prgLoading.setVisibility(View.VISIBLE);
                    resendOtp(country_code,mobile_no);
                }
                break;
        }
    }

    private void loginDoctor(String mobile, String pass) {

        String url = getResources().getString(R.string.loginApi);
        Map<String, String> params = new HashMap<>();
        params.put("mobile_no",mobile);
        params.put("password",pass);
        params.put("role",role);

        String LoginHIT = "login_hit";
        VolleySingleton.getInstance(LoginActivity.this).cancelRequestInQueue(LoginHIT);
        CustomRequest request = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    boolean success = response.getBoolean("success");

                    if (success){

                        JSONObject data = response.getJSONObject("data");

                        String country_code = data.getString("country_code");
                        String mobile_no = data.getString("mobile_no");
                        String account_setup  = data.getString("account_setup");
                        String token = data.getString("token");

                        Comman.setPreferences(LoginActivity.this,"country_code",country_code);
                        Comman.setPreferences(LoginActivity.this,"mobile_no",mobile_no);
                        Comman.setPreferences(LoginActivity.this,"token",token);

                        Toast.makeText(LoginActivity.this,"Login Successfully!",Toast.LENGTH_SHORT).show();

                        if (account_setup.equalsIgnoreCase("0")){
                            Intent ii = new Intent(LoginActivity.this,AccountSetupActivity.class);
                            startActivity(ii);
                        }else {
                            String name = data.getString("name");
                            String profile_pic = data.getString("profile_pic");
                            String speciality = data.getString("speciality");
                            Comman.setPreferences(LoginActivity.this,"loggedIn","1");
                            Comman.setPreferences(LoginActivity.this,"name",name);
                            Comman.setPreferences(LoginActivity.this,"profile_pic",profile_pic);
                            Comman.setPreferences(LoginActivity.this,"speciality",speciality);
                            Intent ii = new Intent(LoginActivity.this,BaseActivity.class);
                            startActivity(ii);
                        }

                    }else {
                        Toast.makeText(LoginActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                    }


                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                prgLoading.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR",error+"");
                Toast.makeText(LoginActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(LoginHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(LoginActivity.this).addToRequestQueue(request);
    }

    private void signupDoctor(final String code, final String mobile, final String email, String pass) {

        String url = getResources().getString(R.string.regApi);
        Map<String, String> params = new HashMap<>();
        params.put("country_code",code);
        params.put("mobile_no",mobile);
        params.put("email",email);
        params.put("password",pass);
        params.put("role",role);

        String RegHIT = "reg_hit";
        VolleySingleton.getInstance(LoginActivity.this).cancelRequestInQueue(RegHIT);
        CustomRequest request = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    boolean success = response.getBoolean("success");
                    String msg = response.getString("message");
                    Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_SHORT).show();

                    Comman.setPreferences(LoginActivity.this,"country_code",code);
                    Comman.setPreferences(LoginActivity.this,"mobile_no",mobile);
                    Comman.setPreferences(LoginActivity.this,"email",email);

                    enableBroadcastReceiver();

                    type = "3";
                    llLogin.setVisibility(View.GONE);
                    llSignup.setVisibility(View.GONE);
                    llVerifctn.setVisibility(View.VISIBLE);
                    tvLogin.setText(getResources().getString(R.string.sbmit));

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                prgLoading.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR",error+"");
                Toast.makeText(LoginActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(RegHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(LoginActivity.this).addToRequestQueue(request);
    }

    private void verifyOTP(String code, String mobile, String otp) {

        String url = getResources().getString(R.string.cnfrmOtpApi);
        Map<String, String> params = new HashMap<>();
        params.put("country_code",code);
        params.put("mobile_no",mobile);
        params.put("password",password);
        params.put("otp",otp);

        String CNFRMOTPHIT = "cnfrm_otp_hit";
        VolleySingleton.getInstance(LoginActivity.this).cancelRequestInQueue(CNFRMOTPHIT);
        CustomRequest request = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    boolean success = response.getBoolean("success");
                    String msg = response.getString("message");
                    Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_SHORT).show();

                    disableBroadcastReceiver();

                    //Comman.setPreferences(LoginActivity.this,"loggedIn","1");

                    Intent ii = new Intent(LoginActivity.this,AccountSetupActivity.class);
                    startActivity(ii);

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                prgLoading.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR",error+"");
                Toast.makeText(LoginActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(CNFRMOTPHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(LoginActivity.this).addToRequestQueue(request);
    }

    private void resendOtp(String code, String mobile) {

        String url = getResources().getString(R.string.rsndOtpApi);
        Map<String, String> params = new HashMap<>();
        params.put("country_code",code);
        params.put("mobile_no",mobile);

        String RSNDOTPHIT = "rsnd_otp_hit";
        VolleySingleton.getInstance(LoginActivity.this).cancelRequestInQueue(RSNDOTPHIT);
        CustomRequest request = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    boolean success = response.getBoolean("success");
                    String msg = response.getString("message");
                    Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_SHORT).show();

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                prgLoading.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR",error+"");
                Toast.makeText(LoginActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(RSNDOTPHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(LoginActivity.this).addToRequestQueue(request);
    }

    public void enableBroadcastReceiver() {

        ComponentName receiver = new ComponentName(this, IncomingSms.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void disableBroadcastReceiver(){
        ComponentName receiver = new ComponentName(this, IncomingSms.class);
        PackageManager pm = this.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

    }

    @Override
    public void onBackPressed() {
        if (type.equalsIgnoreCase("1")){
            super.onBackPressed();
            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        }else if (type.equalsIgnoreCase("2")){
            if (clickedSignup){
                llVerifctn.setVisibility(View.GONE);
                llSignup.setVisibility(View.GONE);
                llLogin.setVisibility(View.VISIBLE);
                tvLogin.setText(getResources().getString(R.string.login));
                type = "1";
            }else {
                super.onBackPressed();
                overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
            }
        }else{
            llVerifctn.setVisibility(View.GONE);
            llLogin.setVisibility(View.GONE);
            llSignup.setVisibility(View.VISIBLE);
            tvLogin.setText(getResources().getString(R.string.signup));
            type = "2";
        }

    }
}
