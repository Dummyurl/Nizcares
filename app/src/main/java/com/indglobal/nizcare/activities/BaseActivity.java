package com.indglobal.nizcare.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.indglobal.nizcare.R;
import com.indglobal.nizcare.adapters.SpecialityAdapter;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.commons.CustomRequest;
import com.indglobal.nizcare.commons.RippleView;
import com.indglobal.nizcare.commons.VolleyJSONRequest;
import com.indglobal.nizcare.commons.VolleySingleton;
import com.indglobal.nizcare.fragments.BaseAcountFragment;
import com.indglobal.nizcare.fragments.BaseHomeFragment;
import com.indglobal.nizcare.fragments.BasePatientFragment;
import com.indglobal.nizcare.fragments.BaseWriteFragment;
import com.indglobal.nizcare.model.ApointItem;
import com.indglobal.nizcare.model.ApointMainItem;
import com.indglobal.nizcare.model.ApointTimeItem;
import com.indglobal.nizcare.model.CheckSpecialityItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by readyassist on 12/13/17.
 */

public class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    ProgressBar prgLoading;
    LinearLayout llBtmHome,llBtmPtnts,llBtmPlus,llBtmWrite,llBtmAcnts;
    ImageView ivHome,ivPtnts,ivWrite,ivAcnts;
    TextView tvHomeCnt,tvHome,tvPtntsCnt,tvPtnts,tvWriteCnts,tvWrite,tvAcntsCnts,tvAcnts;

    Context context = BaseActivity.this;
    public static ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        prgLoading = (ProgressBar)findViewById(R.id.prgLoading);
        llBtmHome = (LinearLayout)findViewById(R.id.llBtmHome);
        ivHome = (ImageView)findViewById(R.id.ivHome);
        tvHomeCnt = (TextView)findViewById(R.id.tvHomeCnt);
        tvHome = (TextView)findViewById(R.id.tvHome);
        llBtmPtnts = (LinearLayout)findViewById(R.id.llBtmPtnts);
        ivPtnts = (ImageView)findViewById(R.id.ivPtnts);
        tvPtntsCnt = (TextView)findViewById(R.id.tvPtntsCnt);
        tvPtnts = (TextView)findViewById(R.id.tvPtnts);
        llBtmPlus = (LinearLayout)findViewById(R.id.llBtmPlus);
        llBtmWrite = (LinearLayout)findViewById(R.id.llBtmWrite);
        ivWrite = (ImageView)findViewById(R.id.ivWrite);
        tvWriteCnts = (TextView)findViewById(R.id.tvWriteCnts);
        tvWrite = (TextView)findViewById(R.id.tvWrite);
        llBtmAcnts = (LinearLayout) findViewById(R.id.llBtmAcnts);
        ivAcnts = (ImageView)findViewById(R.id.ivAcnts);
        tvAcntsCnts = (TextView)findViewById(R.id.tvAcntsCnts);
        tvAcnts = (TextView)findViewById(R.id.tvAcnts);

        viewPager = (ViewPager)findViewById(R.id.viewPager);
        setupViewPager(viewPager);
        enableHome();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        disableAcnts();
                        disableWrite();
                        disablePatient();
                        enableHome();
                        break;

                    case 1:
                        disableAcnts();
                        disableWrite();
                        disableHome();
                        enablePatient();
                        break;

                    case 2:
                        disableAcnts();
                        disableHome();
                        disablePatient();
                        enableWrite();
                        break;

                    case 3:
                        disableHome();
                        disableWrite();
                        disablePatient();
                        enableAcnts();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        llBtmHome.setOnClickListener(this);
        llBtmPtnts.setOnClickListener(this);
        llBtmPlus.setOnClickListener(this);
        llBtmWrite.setOnClickListener(this);
        llBtmAcnts.setOnClickListener(this);

    }

    private void enableHome() {
        ivHome.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_pressed));
        tvHome.setTextColor(getResources().getColor(R.color.lightBlack));
    }

    private void disableHome() {
        ivHome.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_normal));
        tvHome.setTextColor(getResources().getColor(R.color.fullGray));
    }

    private void enablePatient() {
        ivPtnts.setImageDrawable(getResources().getDrawable(R.drawable.ic_patients_pressed));
        tvPtnts.setTextColor(getResources().getColor(R.color.lightBlack));
    }

    private void disablePatient() {
        ivPtnts.setImageDrawable(getResources().getDrawable(R.drawable.ic_patients_normal));
        tvPtnts.setTextColor(getResources().getColor(R.color.fullGray));
    }

    private void enableWrite() {
        ivWrite.setImageDrawable(getResources().getDrawable(R.drawable.ic_write_pressed));
        tvWrite.setTextColor(getResources().getColor(R.color.lightBlack));
    }

    private void disableWrite() {
        ivWrite.setImageDrawable(getResources().getDrawable(R.drawable.ic_write_normal));
        tvWrite.setTextColor(getResources().getColor(R.color.fullGray));
    }

    private void enableAcnts() {
        ivAcnts.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_pressed));
        tvAcnts.setTextColor(getResources().getColor(R.color.lightBlack));
    }

    private void disableAcnts() {
        ivAcnts.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_normal));
        tvAcnts.setTextColor(getResources().getColor(R.color.fullGray));
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Intent mIntent = getIntent();
        int pstn = mIntent.getIntExtra("postion", 0);
        adapter.addFrag(new BaseHomeFragment(), getResources().getString(R.string.home));
        adapter.addFrag(new BasePatientFragment(), getResources().getString(R.string.patnt));
        adapter.addFrag(new BaseWriteFragment(), getResources().getString(R.string.write));
        adapter.addFrag(new BaseAcountFragment(), getResources().getString(R.string.acnts));

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(pstn);
    }

    private static class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){

            case R.id.llBtmHome:
                viewPager.setCurrentItem(0);
                break;

            case R.id.llBtmPtnts:
                viewPager.setCurrentItem(1);
                break;

            case R.id.llBtmWrite:
                viewPager.setCurrentItem(2);
                break;

            case R.id.llBtmAcnts:
                viewPager.setCurrentItem(3);
                break;

            case R.id.llBtmPlus:
                openBottomSheet();
                break;
        }
    }

    public void openBottomSheet () {

        View view = getLayoutInflater ().inflate (R.layout.home_menu_bottumsheet, null);

        LinearLayout llAddPatient = (LinearLayout)view.findViewById(R.id.llAddPatient);
        LinearLayout llCreatApoint = (LinearLayout)view.findViewById(R.id.llCreatApoint);
        LinearLayout llAddVisit = (LinearLayout)view.findViewById(R.id.llAddVisit);
        LinearLayout llAddPrscrptn = (LinearLayout)view.findViewById(R.id.llAddPrscrptn);
        TextView tvBtmShtCancle = (TextView)view.findViewById( R.id.tvBtmShtCancle);

        final Dialog mBottomSheetDialog = new Dialog (BaseActivity.this, R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView (view);
        mBottomSheetDialog.setCancelable (true);
        mBottomSheetDialog.getWindow ().setLayout (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity (Gravity.BOTTOM);
        mBottomSheetDialog.show ();

        tvBtmShtCancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });

        llCreatApoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                Intent iiCreate = new Intent(BaseActivity.this,CreateApointActivity.class);
                startActivity(iiCreate);
            }
        });

        llAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                Intent ii = new Intent(BaseActivity.this,AddPatientActivity.class);
                startActivity(ii);
            }
        });

        llAddVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                Intent iiVisit = new Intent(BaseActivity.this,PatientVisitActivity.class);
                startActivity(iiVisit);
            }
        });

        llAddPrscrptn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iiAddPrscrptn = new Intent(BaseActivity.this,AddPrscrptnActivity.class);
                startActivity(iiAddPrscrptn);
            }
        });

    }

    @Override
    public void onBackPressed() {
        android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(BaseActivity.this);
        builder1.setTitle("Exit");
        builder1.setMessage("Are you sure you want to exit?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //super.onBackPressed();
                        Intent setIntent = new Intent(Intent.ACTION_MAIN);
                        setIntent.addCategory(Intent.CATEGORY_HOME);
                        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(setIntent);
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        android.support.v7.app.AlertDialog alert11 = builder1.create();
        alert11.show();
    }


}
