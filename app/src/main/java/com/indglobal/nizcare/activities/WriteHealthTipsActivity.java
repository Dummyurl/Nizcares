package com.indglobal.nizcare.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.indglobal.nizcare.R;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.commons.CustomRequest;
import com.indglobal.nizcare.commons.RippleView;
import com.indglobal.nizcare.commons.VolleySingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by readyassist on 1/13/18.
 */

public class WriteHealthTipsActivity extends Activity implements RippleView.OnRippleCompleteListener{

    ProgressBar prgLoading;
    RippleView rplBack;
    TextView tvSubmit;
    EditText etTitle,etAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_healthtip_main);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        rplBack = (RippleView)findViewById(R.id.rplBack);
        tvSubmit = (TextView)findViewById(R.id.tvSubmit);
        etTitle = (EditText)findViewById(R.id.etTitle);
        etAnswer = (EditText) findViewById(R.id.etAnswer);


        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString();
                String msg = etAnswer.getText().toString();

                if (title.equalsIgnoreCase("")){
                    etTitle.setError(getResources().getString(R.string.prvdtitle));
                    etTitle.requestLayout();
                }else if (msg.equalsIgnoreCase("")){
                    etAnswer.setError(getResources().getString(R.string.prvdanswr));
                    etAnswer.requestLayout();
                }else if (!Comman.isConnectionAvailable(WriteHealthTipsActivity.this)){
                    Toast.makeText(WriteHealthTipsActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                }else {
                    prgLoading.setVisibility(View.VISIBLE);
                    submitPost(title,msg);
                }
            }
        });

        rplBack.setOnRippleCompleteListener(this);
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


    private void submitPost(String title,String post) {

        String url = getResources().getString(R.string.sbmtHealthfeesApi);
        String token = Comman.getPreferences(WriteHealthTipsActivity.this,"token");
        url = url+"?token="+token;

        Map<String, String> params = new HashMap<>();
        params.put("title",title);
        params.put("post",post);

        String SBMTHLTHHIT = "health_post_hit";
        VolleySingleton.getInstance(WriteHealthTipsActivity.this).cancelRequestInQueue(SBMTHLTHHIT);
        CustomRequest request = new CustomRequest(Request.Method.POST, url,params,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    boolean success = response.getBoolean("success");
                    String message = response.getString("message");

                    if (success){

                        Toast.makeText(WriteHealthTipsActivity.this,message,Toast.LENGTH_SHORT).show();
                        prgLoading.setVisibility(View.GONE);
                        onBackPressed();

                    }else {
                        Toast.makeText(WriteHealthTipsActivity.this,message,Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(WriteHealthTipsActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(WriteHealthTipsActivity.this,message,Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(WriteHealthTipsActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(WriteHealthTipsActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }
                prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(SBMTHLTHHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(WriteHealthTipsActivity.this).addToRequestQueue(request);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
