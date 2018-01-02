package com.indglobal.nizcare.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.model.ApointMainItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by readyassist on 12/30/17.
 */

public class DatesAdapter extends RecyclerView.Adapter<DatesAdapter.MyViewHolder>{

    private Context context = null;
    private TextView slctdDay,slctdDate,slctdCount;
    private RelativeLayout slctdView;
    private int slctdpstn = 543;

    public interface OnItemClickListener {
        void onItemClick(Date date);
    }

    private ArrayList<Date> dateArrayList;
    private HashMap<String,ApointMainItem> apointMainItemHashMap;
    private final OnItemClickListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDay,tvDate,tvApntsCnts;
        private RelativeLayout rlDate;

        MyViewHolder(View itemView) {
            super(itemView);
            tvDay = (TextView) itemView.findViewById(R.id.tvDay);
            tvDate = (TextView)itemView.findViewById(R.id.tvDate);
            tvApntsCnts = (TextView)itemView.findViewById(R.id.tvApntsCnts);
            rlDate = (RelativeLayout)itemView.findViewById(R.id.rlDate);
        }

        void bind(final int position, final Date date, final OnItemClickListener listener) {

            String Day = DateFormat.format("dd",date).toString();
            String month = DateFormat.format("MMM",date).toString();
            String dayName = DateFormat.format("EEE",date).toString();

            tvDay.setText(dayName);
            tvDate.setText(Day);

            if (slctdpstn==position){
                tvDay.setTextColor(context.getResources().getColor(R.color.lightGreen));
                tvDate.setTextColor(context.getResources().getColor(R.color.lightGreen));
                tvApntsCnts.setVisibility(View.GONE);
                rlDate.setBackground(context.getResources().getDrawable(R.drawable.round_stroke_green));
            }else {
                tvDay.setTextColor(context.getResources().getColor(R.color.lightBlack));
                tvDate.setTextColor(context.getResources().getColor(R.color.lightBlack));
                tvApntsCnts.setVisibility(View.VISIBLE);
                rlDate.setBackground(context.getResources().getDrawable(R.drawable.round_stroke_white));
            }

            if (apointMainItemHashMap.containsKey(DateFormat.format("yyyy-MM-dd",date))){
                ApointMainItem apointMainItem = apointMainItemHashMap.get(DateFormat.format("yyyy-MM-dd",date));
                String count = apointMainItem.getCount();
                if (count.equalsIgnoreCase("0")){
                    tvApntsCnts.setVisibility(View.GONE);
                }else {
                    tvApntsCnts.setVisibility(View.VISIBLE);
                }
                tvApntsCnts.setText(count);
            }else {
                tvApntsCnts.setVisibility(View.GONE);
            }



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reverseAnimation(slctdDay,slctdDate,slctdView,slctdCount);
                    circleAnimation(tvDay,tvDate,rlDate,tvApntsCnts);
                    slctdpstn = position;
                    listener.onItemClick(date);
                }

            });
        }


    }

    private void reverseAnimation(TextView slctdDay,TextView slctdDate,RelativeLayout slctdView,TextView slctCount) {

        if (slctdDay!=null){
            slctdDay.setTextColor(context.getResources().getColor(R.color.lightBlack));
            slctdDate.setTextColor(context.getResources().getColor(R.color.lightBlack));
            String count  = slctCount.getText().toString();
            if (!count.equalsIgnoreCase("0")){
                slctCount.setVisibility(View.VISIBLE);
            }
            slctdView.setBackground(context.getResources().getDrawable(R.drawable.round_stroke_white));
        }
    }

    private void circleAnimation(TextView tvDay, TextView tvDate, RelativeLayout rlDate, TextView tvCount) {

        if (tvDay!=null){
            tvDay.setTextColor(context.getResources().getColor(R.color.lightGreen));
            tvDate.setTextColor(context.getResources().getColor(R.color.lightGreen));
            tvCount.setVisibility(View.GONE);
            rlDate.setBackground(context.getResources().getDrawable(R.drawable.round_stroke_green));
            slctdDay = tvDay;
            slctdDate = tvDate;
            slctdCount = tvCount;
            slctdView = rlDate;
        }

    }


    public DatesAdapter(Context context,ArrayList<Date> dateArrayList,HashMap<String,ApointMainItem> apointMainItemHashMap, OnItemClickListener listener) {
        this.dateArrayList = dateArrayList;
        this.listener = listener;
        this.context = context;
        this.apointMainItemHashMap = apointMainItemHashMap;
    }

    @Override
    public DatesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_date_item, parent, false);

        return new DatesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DatesAdapter.MyViewHolder holder, final int position) {
        holder.bind(position,dateArrayList.get(position), listener);

    }


    @Override
    public int getItemCount() {
        return dateArrayList.size();
    }

}