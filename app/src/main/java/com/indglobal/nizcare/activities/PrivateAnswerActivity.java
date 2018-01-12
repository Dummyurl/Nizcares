package com.indglobal.nizcare.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
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
import com.indglobal.nizcare.adapters.PublicChatAdapter;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.commons.CustomRequest;
import com.indglobal.nizcare.commons.RippleView;
import com.indglobal.nizcare.commons.VolleyJSONRequest;
import com.indglobal.nizcare.commons.VolleySingleton;
import com.indglobal.nizcare.commons.roundedimageview.RoundedImageView;
import com.indglobal.nizcare.model.PublicChatItem;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by readyassist on 1/12/18.
 */

public class PrivateAnswerActivity extends Activity implements RippleView.OnRippleCompleteListener{

    ProgressBar prgLoading;
    RippleView rplBack;
    TextView tvSubmit,tvTitle,tvDescrptn,tvSubTitle,tvName,tvSpeclty;
    EditText etAnswer;
    RoundedImageView ivDoctr;

    String id,title,descrptn,name,gender,age,time,drName,drImg,drSpeclt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prvt_answer_main);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        rplBack = (RippleView)findViewById(R.id.rplBack);
        tvSubmit = (TextView)findViewById(R.id.tvSubmit);
        tvTitle = (TextView)findViewById(R.id.tvTitle);
        tvDescrptn = (TextView)findViewById(R.id.tvDescrptn);
        tvSubTitle = (TextView)findViewById(R.id.tvSubTitle);
        tvName = (TextView)findViewById(R.id.tvName);
        tvSpeclty = (TextView)findViewById(R.id.tvSpeclty);
        etAnswer = (EditText) findViewById(R.id.etAnswer);
        ivDoctr = (RoundedImageView) findViewById(R.id.ivDoctr);

        Intent ii = getIntent();
        id = ii.getStringExtra("id");
        title = ii.getStringExtra("title");
        descrptn = ii.getStringExtra("descrptn");
        name = ii.getStringExtra("name");
        gender = ii.getStringExtra("gender");
        age = ii.getStringExtra("age");
        time = ii.getStringExtra("time");

        drName = Comman.getPreferences(PrivateAnswerActivity.this,"name");
        drImg = Comman.getPreferences(PrivateAnswerActivity.this,"profile_pic");
        drSpeclt = Comman.getPreferences(PrivateAnswerActivity.this,"speciality");

        tvTitle.setText(title);
        tvDescrptn.setText(descrptn);
        tvSubTitle.setText(Comman.capitalize(name)+", "+Comman.capitalize(gender)+"*"+age+" Years Old"+" * "+time);
        tvName.setText(drName);
        Picasso.with(PrivateAnswerActivity.this).load(drImg).into(ivDoctr);
        tvSpeclty.setText(drSpeclt);

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = etAnswer.getText().toString();

                if (msg.equalsIgnoreCase("")){
                    etAnswer.setError(getResources().getString(R.string.prvdanswr));
                    etAnswer.requestLayout();
                }else if (!Comman.isConnectionAvailable(PrivateAnswerActivity.this)){
                    Toast.makeText(PrivateAnswerActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                }else {
                    prgLoading.setVisibility(View.VISIBLE);
                    submitAnswer(id,msg);
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


    private void submitAnswer(String question_id,String answer) {

        String url = getResources().getString(R.string.sbmtAnswrApi);
        String token = Comman.getPreferences(PrivateAnswerActivity.this,"token");
        url = url+"?token="+token;

        Map<String, String> params = new HashMap<>();
        params.put("question_id",question_id);
        params.put("answer",answer);

        String SBMTANSWRHIT = "sbmt_prvt_answr_hit";
        VolleySingleton.getInstance(PrivateAnswerActivity.this).cancelRequestInQueue(SBMTANSWRHIT);
        CustomRequest request = new CustomRequest(Request.Method.POST, url,params,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    boolean success = response.getBoolean("success");
                    String message = response.getString("message");

                    if (success){

                        Toast.makeText(PrivateAnswerActivity.this,message,Toast.LENGTH_SHORT).show();
                        prgLoading.setVisibility(View.GONE);
                        onBackPressed();

                    }else {
                        Toast.makeText(PrivateAnswerActivity.this,message,Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(PrivateAnswerActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(PrivateAnswerActivity.this,message,Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(PrivateAnswerActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(PrivateAnswerActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }
                prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(SBMTANSWRHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(PrivateAnswerActivity.this).addToRequestQueue(request);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
