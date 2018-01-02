package com.indglobal.nizcare.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.indglobal.nizcare.R;
import com.indglobal.nizcare.fragments.BaseHomeFragment;
import com.indglobal.nizcare.model.ApointItem;
import com.indglobal.nizcare.model.ApointTimeItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by readyassist on 12/30/17.
 */

public class ApointMainAdapter extends RecyclerView.Adapter<ApointMainAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<ApointTimeItem> apointTimeItemArrayList;
    private ApointMainAdapter.MyViewHolder myViewHolder;

    private int pstn;

    public interface OnItemClickListener {
        void onItemClick(JSONObject object);
    }

    private OnItemClickListener listener;


    public void setOnItemClickListener(OnItemClickListener listener1) {
        this.listener = listener1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout rlHeader;
        private TextView tvName,tvCount;
        private ImageView ivType,ivDroparw;
        private RecyclerView rvApoints;

        MyViewHolder(View view) {
            super(view);

            rlHeader = (RelativeLayout)view.findViewById(R.id.rlHeader);
            tvName = (TextView)view.findViewById(R.id.tvName);
            tvCount = (TextView)view.findViewById(R.id.tvCount);
            ivType = (ImageView)view.findViewById(R.id.ivType);
            ivDroparw = (ImageView)view.findViewById(R.id.ivDroparw);
            rvApoints = (RecyclerView) view.findViewById(R.id.rvApoints);
        }
    }

    public ApointMainAdapter(Context context) {
        this.context = context;
    }

    public ApointMainAdapter(Context context, ArrayList<ApointTimeItem> apointTimeItemArrayList) {
        this.context = context;
        this.apointTimeItemArrayList = apointTimeItemArrayList;
    }

    @Override
    public ApointMainAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.apoints_item, parent, false);

        return new ApointMainAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ApointMainAdapter.MyViewHolder holder, final int position) {

        myViewHolder = holder;

        final ApointTimeItem apointTimeItem = apointTimeItemArrayList.get(position);

        holder.tvName.setText(apointTimeItem.getName());
        holder.tvCount.setText(apointTimeItem.getTimecount());
        if ((apointTimeItem.getName()).equalsIgnoreCase("Morning")){
            holder.ivType.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_morning));
        }else if ((apointTimeItem.getName()).equalsIgnoreCase("Afternoon")){
            holder.ivType.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_afternoon));
        }else {
            holder.ivType.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_evening));
        }


        holder.rvApoints.setAdapter(null);
        ApointmentAdapter apointmentAdapter = new ApointmentAdapter(context,apointTimeItem.getApointItems());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext());
        holder.rvApoints.setLayoutManager(layoutManager);
        holder.rvApoints.setAdapter(apointmentAdapter);

        if (apointTimeItem.isOpened()){
            openCollapsingView(context,holder.ivDroparw,holder.rvApoints);
        }else {
            closeCollapsingView(context,holder.ivDroparw,holder.rvApoints);
        }

        holder.rlHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pstn = holder.getAdapterPosition();
                if (apointTimeItem.isOpened()){
                    closeCollapsingView(context,holder.ivDroparw,holder.rvApoints);
                    BaseHomeFragment.apointTimeItemArrayList.get(pstn).setOpened(false);
                }else {
                    openCollapsingView(context,holder.ivDroparw,holder.rvApoints);
                    BaseHomeFragment.apointTimeItemArrayList.get(pstn).setOpened(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return apointTimeItemArrayList.size();
    }

    private void openCollapsingView(final Context context, final ImageView imageView, RecyclerView recyclerView) {
        RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(100);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_g_u));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(rotate);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void closeCollapsingView(final Context context, final ImageView imageView, RecyclerView recyclerView) {
        RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(100);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_g_d));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(rotate);
        recyclerView.setVisibility(View.GONE);
    }

}