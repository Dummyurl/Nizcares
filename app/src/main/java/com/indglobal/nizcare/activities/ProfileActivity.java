package com.indglobal.nizcare.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import com.indglobal.nizcare.adapters.AwardPrflAdapter;
import com.indglobal.nizcare.adapters.BankAdapter;
import com.indglobal.nizcare.adapters.EduPrflAdapter;
import com.indglobal.nizcare.adapters.HsptlPrflAdapter;
import com.indglobal.nizcare.adapters.IntroAdapter;
import com.indglobal.nizcare.adapters.MemberPrflAdapter;
import com.indglobal.nizcare.adapters.PrflSliderAdapter;
import com.indglobal.nizcare.adapters.TrtmntPrflAdapter;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.commons.RippleView;
import com.indglobal.nizcare.commons.VolleyJSONRequest;
import com.indglobal.nizcare.commons.VolleySingleton;
import com.indglobal.nizcare.commons.roundedimageview.RoundedImageView;
import com.indglobal.nizcare.model.AwardPrflItem;
import com.indglobal.nizcare.model.BankItem;
import com.indglobal.nizcare.model.EduPrflItem;
import com.indglobal.nizcare.model.HsptlPrflItem;
import com.indglobal.nizcare.model.MemberPrflItem;
import com.indglobal.nizcare.model.TreatmentPrflItem;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by readyassist on 1/8/18.
 */

public class ProfileActivity extends Activity implements RippleView.OnRippleCompleteListener{

    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    RippleView rplBack;
    ProgressBar prgLoading;

    LinearLayout llMain,llGetDirctn,llCall;
    ViewPager pager;
    CirclePageIndicator indicator;
    RoundedImageView rivPrfl;
    TextView tvOnline,tvName,tvDegree,tvMedicalCntr,tvLocation,tvExp,tvCnsltnFee,tvLangs,tvWeblnk,
            tvTimeings,tvTapToviewTmngs,tvEditPrfl;

    RecyclerView rvEducation,rvAwards,rvMembrships,rvClinics,rvTreatmnt;

    public ArrayList<String> ImagesArray = new ArrayList<>();
    public ArrayList<String> Images = new ArrayList<>();

    EduPrflItem eduPrflItem;
    public ArrayList<EduPrflItem> eduPrflItemArrayList = new ArrayList<>();
    EduPrflAdapter eduPrflAdapter;

    HsptlPrflItem hsptlPrflItem;
    ArrayList<HsptlPrflItem> hsptlPrflItemArrayList = new ArrayList<>();
    HsptlPrflAdapter hsptlPrflAdapter;

    AwardPrflItem awardPrflItem;
    ArrayList<AwardPrflItem> awardPrflItemArrayList = new ArrayList<>();
    AwardPrflAdapter awardPrflAdapter;

    TreatmentPrflItem treatmentPrflItem;
    ArrayList<TreatmentPrflItem> treatmentPrflItemArrayList = new ArrayList<>();
    TrtmntPrflAdapter trtmntPrflAdapter;

    MemberPrflItem memberPrflItem;
    ArrayList<MemberPrflItem> memberPrflItemArrayList = new ArrayList<>();
    MemberPrflAdapter memberPrflAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profle_activity);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        rplBack = (RippleView)findViewById(R.id.rplBack);
        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);

        llMain = (LinearLayout)findViewById(R.id.llMain);
        pager = (ViewPager)findViewById(R.id.pager);
        indicator = (CirclePageIndicator)findViewById(R.id.indicator);
        rivPrfl = (RoundedImageView)findViewById(R.id.rivPrfl);
        tvOnline = (TextView)findViewById(R.id.tvOnline);
        tvName = (TextView)findViewById(R.id.tvName);
        tvDegree = (TextView)findViewById(R.id.tvDegree);
        tvMedicalCntr = (TextView)findViewById(R.id.tvMedicalCntr);
        tvLocation = (TextView)findViewById(R.id.tvLocation);
        llGetDirctn = (LinearLayout)findViewById(R.id.llGetDirctn);
        llCall = (LinearLayout)findViewById(R.id.llCall);
        tvExp = (TextView)findViewById(R.id.tvExp);
        tvCnsltnFee = (TextView)findViewById(R.id.tvCnsltnFee);
        tvLangs = (TextView)findViewById(R.id.tvLangs);
        tvWeblnk = (TextView)findViewById(R.id.tvWeblnk);
        tvTimeings = (TextView)findViewById(R.id.tvTimeings);
        tvTapToviewTmngs = (TextView)findViewById(R.id.tvTapToviewTmngs);
        rvEducation = (RecyclerView)findViewById(R.id.rvEducation);
        rvAwards = (RecyclerView)findViewById(R.id.rvAwards);
        rvMembrships = (RecyclerView)findViewById(R.id.rvMembrships);
        rvClinics = (RecyclerView)findViewById(R.id.rvClinics);
        rvTreatmnt = (RecyclerView)findViewById(R.id.rvTreatmnt);
        tvEditPrfl = (TextView)findViewById(R.id.tvEditPrfl);

        if(!Comman.isConnectionAvailable(ProfileActivity.this)){
            llMain.setVisibility(View.GONE);
            Toast.makeText(ProfileActivity.this,getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
        }else {
            prgLoading.setVisibility(View.VISIBLE);
            getDoctorProfile();
        }

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

    private void getDoctorProfile() {

        String url = getResources().getString(R.string.getDocPrflApi);
        String token = Comman.getPreferences(ProfileActivity.this,"token");
        url = url+"?token="+token;

        String GETDOCPRFLHIT = "get_docprfl_hit";
        VolleySingleton.getInstance(ProfileActivity.this).cancelRequestInQueue(GETDOCPRFLHIT);
        VolleyJSONRequest request = new VolleyJSONRequest(Request.Method.GET, url,null, null,new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                try {
                    JSONObject response  = new JSONObject(result);
                    boolean success = response.getBoolean("success");

                    if (success){

                        JSONObject data = response.getJSONObject("data");

                        String doctor_id = data.getString("doctor_id");

                        Images.clear();
                        JSONArray slider_images = data.getJSONArray("slider_images");
                        for (int i=0;i<slider_images.length();i++){
                            JSONObject object = slider_images.getJSONObject(i);
                            Images.add(object.getString("media"));
                        }

                        String prefix = data.getString("prefix");
                        String name = data.getString("name");
                        String speciality = data.getString("speciality");
                        String degree = data.getString("degree");
                        String country_code = data.getString("country_code");
                        String mobile_no = data.getString("mobile_no");
                        String profile_pic = data.getString("profile_pic");
                        String gender = data.getString("gender");
                        String experience = data.getString("experience");
                        String location = data.getString("location");
                        String hospital_id = data.getString("hospital_id");
                        String hospital_name = data.getString("hospital_name");
                        String hospital_country_code = data.getString("hospital_country_code");
                        String hospital_phone_no = data.getString("hospital_phone_no");
                        String hospital_address = data.getString("hospital_address");
                        String consultation_fees = data.getString("consultation_fees");
                        String weblink = data.getString("weblink");
                        String language = data.getString("language");
                        String timeings = data.getString("timeings");

                        eduPrflItemArrayList.clear();
                        JSONArray education = data.getJSONArray("education");
                        for (int i=0;i<education.length();i++){
                            JSONObject object = education.getJSONObject(i);

                            String id = object.getString("id");
                            String degre = object.getString("degree");
                            String college = object.getString("college");
                            String year = object.getString("year");

                            eduPrflItem = new EduPrflItem(id,degre,college,year);
                            eduPrflItemArrayList.add(eduPrflItem);
                        }

                        RecyclerView.LayoutManager lmEdu = new LinearLayoutManager(ProfileActivity.this.getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                        eduPrflAdapter = new EduPrflAdapter(ProfileActivity.this,eduPrflItemArrayList, new EduPrflAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(EduPrflItem eduPrflItem) {

                            }
                        });
                        rvEducation.setLayoutManager(lmEdu);
                        rvEducation.setAdapter(eduPrflAdapter);

                        hsptlPrflItemArrayList.clear();
                        JSONArray hospitals = data.getJSONArray("hospitals");
                        for (int i=0;i<hospitals.length();i++){
                            JSONObject object = hospitals.getJSONObject(i);

                            String id = object.getString("id");
                            String names = object.getString("name");
                            String address = object.getString("address");

                            hsptlPrflItem = new HsptlPrflItem(id,names,address);
                            hsptlPrflItemArrayList.add(hsptlPrflItem);
                        }
                        RecyclerView.LayoutManager lmHsptls = new LinearLayoutManager(ProfileActivity.this.getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                        hsptlPrflAdapter = new HsptlPrflAdapter(ProfileActivity.this,hsptlPrflItemArrayList, new HsptlPrflAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(HsptlPrflItem hsptlPrflItem) {

                            }
                        });
                        rvClinics.setLayoutManager(lmHsptls);
                        rvClinics.setAdapter(hsptlPrflAdapter);

                        awardPrflItemArrayList.clear();
                        JSONArray awards = data.getJSONArray("awards");
                        for (int i=0;i<awards.length();i++){
                            JSONObject object = awards.getJSONObject(i);

                            String id = object.getString("id");
                            String title = object.getString("title");
                            String college = object.getString("college");
                            String year = object.getString("year");

                            awardPrflItem = new AwardPrflItem(id,title,college,year);
                            awardPrflItemArrayList.add(awardPrflItem);
                        }
                        RecyclerView.LayoutManager lmAwards = new LinearLayoutManager(ProfileActivity.this.getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                        awardPrflAdapter = new AwardPrflAdapter(ProfileActivity.this,awardPrflItemArrayList, new AwardPrflAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(AwardPrflItem awardPrflItem) {

                            }
                        });
                        rvAwards.setLayoutManager(lmAwards);
                        rvAwards.setAdapter(awardPrflAdapter);

                        treatmentPrflItemArrayList.clear();
                        JSONArray treatments = data.getJSONArray("treatments");
                        for (int i=0;i<treatments.length();i++){
                            JSONObject object = treatments.getJSONObject(i);

                            String treatment_id = object.getString("treatment_id");
                            String treatment_name = object.getString("treatment_name");

                            treatmentPrflItem = new TreatmentPrflItem(treatment_id,treatment_name);
                            treatmentPrflItemArrayList.add(treatmentPrflItem);
                        }
                        RecyclerView.LayoutManager lmTreatment = new LinearLayoutManager(ProfileActivity.this.getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                        trtmntPrflAdapter = new TrtmntPrflAdapter(ProfileActivity.this,treatmentPrflItemArrayList, new TrtmntPrflAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(TreatmentPrflItem treatmentPrflItem) {

                            }
                        });
                        rvTreatmnt.setLayoutManager(lmTreatment);
                        rvTreatmnt.setAdapter(trtmntPrflAdapter);

                        memberPrflItemArrayList.clear();
                        JSONArray membership = data.getJSONArray("membership");
                        for (int i=0;i<membership.length();i++){
                            JSONObject object = membership.getJSONObject(i);

                            String id = object.getString("id");
                            String title = object.getString("title");
                            String position = object.getString("position");
                            String year = object.getString("year");

                            memberPrflItem = new MemberPrflItem(id,title,position,year);
                            memberPrflItemArrayList.add(memberPrflItem);
                        }
                        RecyclerView.LayoutManager lmMember = new LinearLayoutManager(ProfileActivity.this.getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                        memberPrflAdapter = new MemberPrflAdapter(ProfileActivity.this,memberPrflItemArrayList, new MemberPrflAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(MemberPrflItem memberPrflItem) {

                            }
                        });
                        rvMembrships.setLayoutManager(lmMember);
                        rvMembrships.setAdapter(awardPrflAdapter);

                        Picasso.with(ProfileActivity.this).load(profile_pic).into(rivPrfl);
                        tvName.setText(prefix+" "+Comman.capitalize(name));
                        tvDegree.setText(Comman.capitalize(speciality)+" * "+degree);
                        tvMedicalCntr.setText(Comman.capitalize(hospital_name));
                        tvLocation.setText(hospital_address);
                        tvExp.setText(experience+" years");
                        tvCnsltnFee.setText(consultation_fees+"/-");
                        tvLangs.setText(language);
                        tvWeblnk.setText(weblink);
                        tvTimeings.setText(timeings);

                        if (slider_images.length()!=0){
                            initSlider();
                        }

                        llMain.setVisibility(View.VISIBLE);
                        prgLoading.setVisibility(View.GONE);


                    }else {
                        String message = response.getString("message");
                        Toast.makeText(ProfileActivity.this,message,Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(ProfileActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(ProfileActivity.this,message,Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(ProfileActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(ProfileActivity.this,getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }
                prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(GETDOCPRFLHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(ProfileActivity.this).addToRequestQueue(request);
    }

    private void initSlider() {

        for(int i=0;i<Images.size();i++)
            ImagesArray.addAll(Images);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new PrflSliderAdapter(ProfileActivity.this, ImagesArray));
        //pager.setPageTransformer(true, new flipCard());

        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);

        final float density = getResources().getDisplayMetrics().density;

        indicator.setFillColor(getResources().getColor(R.color.lightGreen));
        indicator.setStrokeColor(getResources().getColor(R.color.lightGreen));
        indicator.setRadius(4 * density);

        NUM_PAGES = Images.size();

//        final Handler handler = new Handler();
//        final Runnable Update = new Runnable() {
//            public void run() {
//                if (currentPage == NUM_PAGES) {
//                    currentPage = 0;
//                }
//                pager.setCurrentItem(currentPage++, true);
//            }
//        };

//        Timer swipeTimer = new Timer();
//        swipeTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(Update);
//            }
//        }, 5000, 5000);

        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
