package com.indglobal.nizcare.adapters;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.Toast;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.activities.ReScheduleActivity;
import com.indglobal.nizcare.model.SlotItem;
import com.indglobal.nizcare.model.SlotsMainItem;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by readyassist on 1/5/18.
 */

public class SlotsMainAdapter extends RecyclerView.Adapter<SlotsMainAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<SlotsMainItem> slotsMainItemArrayList;
    private SlotsMainAdapter.MyViewHolder myViewHolder;

    private RecyclerView slctdRV=null;
    private ImageView slctdImg = null;

    private int pstn,slctdPstn;

    public interface OnItemClickListener {
        void onItemClick(JSONObject object);
    }

    private SlotsMainAdapter.OnItemClickListener listener;


    public void setOnItemClickListener(SlotsMainAdapter.OnItemClickListener listener1) {
        this.listener = listener1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout rlHeader;
        private TextView tvName;
        private ImageView ivType,ivDroparw;
        private RecyclerView rvSlots;

        MyViewHolder(View view) {
            super(view);

            rlHeader = (RelativeLayout)view.findViewById(R.id.rlHeader);
            tvName = (TextView)view.findViewById(R.id.tvName);
            ivType = (ImageView)view.findViewById(R.id.ivType);
            ivDroparw = (ImageView)view.findViewById(R.id.ivDroparw);
            rvSlots = (RecyclerView) view.findViewById(R.id.rvSlots);
        }
    }

    public SlotsMainAdapter(Context context) {
        this.context = context;
    }

    public SlotsMainAdapter(Context context, ArrayList<SlotsMainItem> slotsMainItemArrayList) {
        this.context = context;
        this.slotsMainItemArrayList = slotsMainItemArrayList;
    }

    @Override
    public SlotsMainAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.slots_main_item, parent, false);

        return new SlotsMainAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SlotsMainAdapter.MyViewHolder holder, final int position) {

        myViewHolder = holder;

        final SlotsMainItem slotsMainItem = slotsMainItemArrayList.get(position);

        holder.tvName.setText(slotsMainItem.getName());
        if ((slotsMainItem.getName()).equalsIgnoreCase("Morning")){
            holder.ivType.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_morning));
        }else if ((slotsMainItem.getName()).equalsIgnoreCase("Afternoon")){
            holder.ivType.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_afternoon));
        }else {
            holder.ivType.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_evening));
        }


        holder.rvSlots.setAdapter(null);
        SlotAdapter slotAdapter = new SlotAdapter(context,slotsMainItem.getSlotItems(), new SlotAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SlotItem slotItem) {
                ReScheduleActivity.topslctdTime = slotItem.getSlot();
            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(context,3);
        holder.rvSlots.setLayoutManager(layoutManager);
        holder.rvSlots.setAdapter(slotAdapter);

        if (slotsMainItem.isOpened()){
            openCollapsingView(context,holder.ivDroparw,holder.rvSlots);
        }else {
            closeCollapsingView(context,holder.ivDroparw,holder.rvSlots);
        }

        holder.rlHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pstn = holder.getAdapterPosition();
                if (slotsMainItem.isOpened()){
                    closeCollapsingView(context,holder.ivDroparw,holder.rvSlots);
                    slotsMainItemArrayList.get(pstn).setOpened(false);
                }else {
                    if (slctdRV!=null){
                        closeCollapsingView(context,slctdImg,slctdRV);
                        slotsMainItemArrayList.get(slctdPstn).setOpened(false);
                    }
                    openCollapsingView(context,holder.ivDroparw,holder.rvSlots);
                    slctdPstn = pstn;
                    slotsMainItemArrayList.get(pstn).setOpened(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return slotsMainItemArrayList.size();
    }

    private void openCollapsingView(final Context context, final ImageView imageView, RecyclerView recyclerView) {
        slctdRV = recyclerView;
        slctdImg = imageView;
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