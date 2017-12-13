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

import java.util.ArrayList;

/**
 * Created by readyassist on 12/12/17.
 */

public class IntroAdapter extends PagerAdapter {

    private ArrayList<Integer> IMAGES;
    private LayoutInflater inflater;
    private Context context;
    private int returnCount;


    public IntroAdapter(Context context,ArrayList<Integer> IMAGES) {
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
        View imageLayout = inflater.inflate(R.layout.intro_sliding_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.ivSliderImage);
        final TextView title = (TextView)imageLayout.findViewById(R.id.tvsliderTitle);

        String img = IMAGES.get(position) + "";
        img = img.replace("[", "").replace("]", "");

//        Picasso.with(context).load(img).error(R.drawable.about_why).into(imageView);

        imageView.setImageDrawable(context.getResources().getDrawable(IMAGES.get(position)));
        title.setText(IntroActivity.IntroTitle.get(position));

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

