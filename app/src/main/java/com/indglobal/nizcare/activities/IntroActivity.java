package com.indglobal.nizcare.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.adapters.IntroAdapter;
import com.indglobal.nizcare.commons.RippleView;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by indglobal on 12/12/17.
 */

public class IntroActivity extends Activity implements RippleView.OnRippleCompleteListener{

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    public ArrayList<Integer> ImagesArray = new ArrayList<>();
    public ArrayList<Integer> IMAGES = new ArrayList<>();
    public static ArrayList<String> IntroTitle = new ArrayList<>();

    RippleView rplLogin,rplgetStrt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_main);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        IMAGES.add(R.drawable.intro_one);
        IMAGES.add(R.drawable.intro_two);
        IMAGES.add(R.drawable.intro_three);

        IntroTitle.add(getResources().getString(R.string.intro_one_title));
        IntroTitle.add(getResources().getString(R.string.intro_two_title));
        IntroTitle.add(getResources().getString(R.string.intro_three_title));

        rplLogin = (RippleView)findViewById(R.id.rplLogin);
        rplgetStrt = (RippleView)findViewById(R.id.rplgetStrt);

        init();


        rplLogin.setOnRippleCompleteListener(this);
        rplgetStrt.setOnRippleCompleteListener(this);

    }

    private void init() {

        for(int i=0;i<IMAGES.size();i++)
            ImagesArray.addAll(IMAGES);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new IntroAdapter(IntroActivity.this, ImagesArray));
        //mPager.setPageTransformer(true, new flipCard());

        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        indicator.setFillColor(getResources().getColor(R.color.white));
        indicator.setStrokeColor(getResources().getColor(R.color.newfewGray));
        indicator.setRadius(5 * density);

        NUM_PAGES = IMAGES.size();

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 5000, 5000);

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
    public void onComplete(RippleView rippleView) {
        int id = rippleView.getId();

        switch (id){
            case R.id.rplLogin:
                Intent ii = new Intent(IntroActivity.this,LoginActivity.class);
                startActivity(ii);
                break;

            case R.id.rplgetStrt:
                Intent intent = new Intent(IntroActivity.this,SignUpActivity.class);
                startActivity(intent);
                break;
        }
    }

    private class flipCard implements ViewPager.PageTransformer {
        @Override
        public void transformPage(View page, float position) {

            final float rotation = 180f * position;

            page.setAlpha(rotation > 90f || rotation < -90f ? 0 : 1);
            page.setPivotX(page.getWidth() * 0.5f);
            page.setPivotY(page.getHeight() * 0.5f);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                page.setScrollBarFadeDuration(6000);
            }
            page.setRotationY(rotation);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
