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
import com.indglobal.nizcare.commons.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by readyassist on 1/12/18.
 */

public class EnquiryReplyActivity extends Activity implements RippleView.OnRippleCompleteListener{

    ProgressBar prgLoading;
    RippleView rplBack;
    TextView tvSubmit;
    EditText etAnswer;
    String enquiry_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enq_rply_main);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        rplBack = (RippleView)findViewById(R.id.rplBack);
        tvSubmit = (TextView)findViewById(R.id.tvSubmit);
        etAnswer = (EditText) findViewById(R.id.etAnswer);

        Intent ii = getIntent();
        enquiry_id = ii.getStringExtra("enquiry_id");

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = etAnswer.getText().toString();

                if (msg.equalsIgnoreCase("")){
                    etAnswer.setError(getResources().getString(R.string.prvdanswr));
                    etAnswer.requestLayout();
                }else if (!Comman.isConnectionAvailable(EnquiryReplyActivity.this)){
                    Toast.makeText(EnquiryReplyActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                }else {
                    prgLoading.setVisibility(View.VISIBLE);
                    submitReply(enquiry_id,msg);
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


    private void submitReply(String enquiry_id,String answer) {

        String url = getResources().getString(R.string.sbmtReplyApi);
        String token = Comman.getPreferences(EnquiryReplyActivity.this,"token");
        url = url+"?token="+token;

        Map<String, String> params = new HashMap<>();
        params.put("enquiry_id",enquiry_id);
        params.put("message",answer);

        String SBMTRPLYHIT = "sbmt_rply_hit";
        VolleySingleton.getInstance(EnquiryReplyActivity.this).cancelRequestInQueue(SBMTRPLYHIT);
        CustomRequest request = new CustomRequest(Request.Method.POST, url,params,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    boolean success = response.getBoolean("success");
                    String message = response.getString("message");

                    if (success){

                        Toast.makeText(EnquiryReplyActivity.this,message,Toast.LENGTH_SHORT).show();
                        prgLoading.setVisibility(View.GONE);
                        onBackPressed();

                    }else {
                        Toast.makeText(EnquiryReplyActivity.this,message,Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(EnquiryReplyActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(EnquiryReplyActivity.this,message,Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(EnquiryReplyActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(EnquiryReplyActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }
                prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(SBMTRPLYHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(EnquiryReplyActivity.this).addToRequestQueue(request);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
