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
import android.widget.TextView;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.fragments.BaseHomeFragment;
import com.indglobal.nizcare.model.ApointTimeItem;
import com.indglobal.nizcare.model.CnsltnTimeItem;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by readyassist on 1/24/18.
 */

public class CncltnMainAdapter extends RecyclerView.Adapter<CncltnMainAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<CnsltnTimeItem> cnsltnTimeItemArrayList;
    private CncltnMainAdapter.MyViewHolder myViewHolder;

    private int pstn;

    public interface OnItemClickListener {
        void onItemClick(JSONObject object);
    }

    private CncltnMainAdapter.OnItemClickListener listener;


    public void setOnItemClickListener(CncltnMainAdapter.OnItemClickListener listener1) {
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

    public CncltnMainAdapter(Context context) {
        this.context = context;
    }

    public CncltnMainAdapter(Context context, ArrayList<CnsltnTimeItem> cnsltnTimeItemArrayList) {
        this.context = context;
        this.cnsltnTimeItemArrayList = cnsltnTimeItemArrayList;
    }

    @Override
    public CncltnMainAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.apoints_item, parent, false);

        return new CncltnMainAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CncltnMainAdapter.MyViewHolder holder, final int position) {

        myViewHolder = holder;

        final CnsltnTimeItem cnsltnTimeItem = cnsltnTimeItemArrayList.get(position);

        holder.tvName.setText(cnsltnTimeItem.getName());
        holder.tvCount.setText(cnsltnTimeItem.getTimecount());
        if ((cnsltnTimeItem.getName()).equalsIgnoreCase("Morning")){
            holder.ivType.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_morning));
        }else if ((cnsltnTimeItem.getName()).equalsIgnoreCase("Afternoon")){
            holder.ivType.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_afternoon));
        }else {
            holder.ivType.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_evening));
        }


        holder.rvApoints.setAdapter(null);
        CnsltntAdapter cnsltntAdapter = new CnsltntAdapter(context,cnsltnTimeItem.getCnsltnItems());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext());
        holder.rvApoints.setLayoutManager(layoutManager);
        holder.rvApoints.setAdapter(cnsltntAdapter);

        if (cnsltnTimeItem.isOpened()){
            openCollapsingView(context,holder.ivDroparw,holder.rvApoints);
        }else {
            closeCollapsingView(context,holder.ivDroparw,holder.rvApoints);
        }

        holder.rlHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pstn = holder.getAdapterPosition();
                if (cnsltnTimeItem.isOpened()){
                    closeCollapsingView(context,holder.ivDroparw,holder.rvApoints);
                    BaseHomeFragment.cnsltnTimeItemArrayList.get(pstn).setOpened(false);
                }else {
                    openCollapsingView(context,holder.ivDroparw,holder.rvApoints);
                    BaseHomeFragment.cnsltnTimeItemArrayList.get(pstn).setOpened(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cnsltnTimeItemArrayList.size();
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