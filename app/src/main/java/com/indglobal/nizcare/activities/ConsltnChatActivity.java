package com.indglobal.nizcare.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.indglobal.nizcare.adapters.CnsltChatAdapter;
import com.indglobal.nizcare.adapters.InstantAdapter;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.commons.CustomRequest;
import com.indglobal.nizcare.commons.RippleView;
import com.indglobal.nizcare.commons.VolleyJSONRequest;
import com.indglobal.nizcare.commons.VolleySingleton;
import com.indglobal.nizcare.model.CnsltnChatItem;
import com.indglobal.nizcare.model.InstantItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by readyassist on 1/25/18.
 */

public class ConsltnChatActivity extends Activity implements RippleView.OnRippleCompleteListener{

    ProgressBar prgLoading;
    RippleView rplBack;
    TextView tvName;
    RecyclerView rvCnsltChat;
    EditText etMsg;
    CardView crdSend;
    String patient_name,consultation_id;

    CnsltnChatItem cnsltnChatItem;
    ArrayList<CnsltnChatItem> cnsltnChatItemArrayList = new ArrayList<>();
    CnsltChatAdapter cnsltChatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cnsltn_chat_main);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        rplBack = (RippleView)findViewById(R.id.rplBack);
        tvName = (TextView)findViewById(R.id.tvName);
        etMsg = (EditText) findViewById(R.id.etMsg);
        crdSend = (CardView)findViewById(R.id.crdSend);
        rvCnsltChat = (RecyclerView)findViewById(R.id.rvCnsltChat);

        Intent ii = getIntent();
        patient_name = ii.getStringExtra("patient_name");
        consultation_id = ii.getStringExtra("consultation_id");

        tvName.setText(Comman.capitalize(patient_name));

        if (!Comman.isConnectionAvailable(ConsltnChatActivity.this)){
            Toast.makeText(ConsltnChatActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
        }else {
            prgLoading.setVisibility(View.VISIBLE);
            getCnsltationMsgs();
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        cnsltChatAdapter = new CnsltChatAdapter(ConsltnChatActivity.this,cnsltnChatItemArrayList, new CnsltChatAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CnsltnChatItem cnsltnChatItem) {

            }
        });
        rvCnsltChat.setLayoutManager(layoutManager);
        rvCnsltChat.setAdapter(cnsltChatAdapter);

        crdSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = etMsg.getText().toString();

                if (msg.equalsIgnoreCase("")){
                    Animation shake = AnimationUtils.loadAnimation(ConsltnChatActivity.this, R.anim.shake_anim);
                    etMsg.startAnimation(shake);
                }else if (!Comman.isConnectionAvailable(ConsltnChatActivity.this)){
                    Toast.makeText(ConsltnChatActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                }else {
                    prgLoading.setVisibility(View.VISIBLE);
                    submitReply(consultation_id,msg);
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


    private void getCnsltationMsgs() {

        String url = getResources().getString(R.string.getCnsltMsgApi);
        String token = Comman.getPreferences(ConsltnChatActivity.this,"token");
        url = url+"?token="+token+"&consultation_id="+consultation_id;

        String GETMSGSHIT = "get_msgs_hit";
        VolleySingleton.getInstance(ConsltnChatActivity.this).cancelRequestInQueue(GETMSGSHIT);
        VolleyJSONRequest request = new VolleyJSONRequest(Request.Method.GET, url,null, null,new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                try {

                    JSONObject response = new JSONObject(result);

                    boolean success = response.getBoolean("success");
                    String message = response.getString("message");

                    if (success){

                        JSONObject data = response.getJSONObject("data");

                        JSONArray chats = data.getJSONArray("chats");
                        for (int i=0;i<chats.length();i++){
                            JSONObject object = chats.getJSONObject(i);

                            String msg = object.getString("message");
                            String time = object.getString("time");
                            String side = object.getString("side");

                            cnsltnChatItem = new CnsltnChatItem(msg,time,side);
                            cnsltnChatItemArrayList.add(cnsltnChatItem);
                        }

                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                        cnsltChatAdapter = new CnsltChatAdapter(ConsltnChatActivity.this,cnsltnChatItemArrayList, new CnsltChatAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(CnsltnChatItem cnsltnChatItem) {

                            }
                        });
                        rvCnsltChat.setLayoutManager(layoutManager);
                        rvCnsltChat.setAdapter(cnsltChatAdapter);

                        if (rvCnsltChat.getAdapter()!=null){
                            final int finalSlctdPosition = cnsltnChatItemArrayList.size()-1;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        rvCnsltChat.scrollToPosition(finalSlctdPosition);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            },10);
                        }

                        prgLoading.setVisibility(View.GONE);

                    }else {
                        Toast.makeText(ConsltnChatActivity.this,message,Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(ConsltnChatActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(ConsltnChatActivity.this,message,Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(ConsltnChatActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(ConsltnChatActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }
                prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(GETMSGSHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(ConsltnChatActivity.this).addToRequestQueue(request);
    }

    private void submitReply(String enquiry_id,String message) {

        String url = getResources().getString(R.string.sendCnsltMsgApi);
        String token = Comman.getPreferences(ConsltnChatActivity.this,"token");
        url = url+"?token="+token;

        Map<String, String> params = new HashMap<>();
        params.put("consultation_id",enquiry_id);
        params.put("message",message);

        String SBMTRPLYHIT = "sbmt_rply_hit";
        VolleySingleton.getInstance(ConsltnChatActivity.this).cancelRequestInQueue(SBMTRPLYHIT);
        CustomRequest request = new CustomRequest(Request.Method.POST, url,params,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    boolean success = response.getBoolean("success");
                    String message = response.getString("message");

                    if (success){

                        JSONObject data = response.getJSONObject("data");
                        JSONObject chats = data.getJSONObject("chats");
                        String msg = chats.getString("message");
                        String time = chats.getString("time");

                        cnsltnChatItem = new CnsltnChatItem(msg,time,"1");

                        cnsltChatAdapter.addItem(cnsltnChatItem);

//                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
//                        cnsltChatAdapter = new CnsltChatAdapter(ConsltnChatActivity.this,cnsltnChatItemArrayList, new CnsltChatAdapter.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(CnsltnChatItem cnsltnChatItem) {
//
//                            }
//                        });
//                        rvCnsltChat.setLayoutManager(layoutManager);
//                        rvCnsltChat.setAdapter(cnsltChatAdapter);

                        if (rvCnsltChat.getAdapter()!=null){
                            final int finalSlctdPosition = cnsltnChatItemArrayList.size()-1;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        rvCnsltChat.scrollToPosition(finalSlctdPosition);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            },10);
                        }

                        etMsg.setText("");
                        prgLoading.setVisibility(View.GONE);

                    }else {
                        Toast.makeText(ConsltnChatActivity.this,message,Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(ConsltnChatActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(ConsltnChatActivity.this,message,Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(ConsltnChatActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(ConsltnChatActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }
                prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(SBMTRPLYHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(ConsltnChatActivity.this).addToRequestQueue(request);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
