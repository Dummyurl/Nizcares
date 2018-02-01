package com.indglobal.nizcare.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.activities.IntroActivity;
import com.indglobal.nizcare.activities.ProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by readyassist on 2/1/18.
 */

public class PrflSliderAdapter extends PagerAdapter {

    private ArrayList<String> IMAGES;
    private LayoutInflater inflater;
    private Context context;
    private int returnCount;


    public PrflSliderAdapter(Context context,ArrayList<String> IMAGES) {
        this.context = context;
        this.IMAGES=IMAGES;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        int df =IMAGES.size();
        Double d = Math.sqrt(df);
        returnCount = d.intValue();
        return returnCount;
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.prfl_slider_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.ivSliderImage);

        String img = IMAGES.get(position) + "";
        img = img.replace("[", "").replace("]", "");

        Picasso.with(context).load(img).into(imageView);

        view.addView(imageLayout, 0);
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}

