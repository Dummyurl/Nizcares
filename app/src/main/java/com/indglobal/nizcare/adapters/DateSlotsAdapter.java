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

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by readyassist on 1/5/18.
 */

public class DateSlotsAdapter  extends RecyclerView.Adapter<DateSlotsAdapter.MyViewHolder>{

    private Context context = null;
    private TextView slctdDay,slctdDate,slctdCount;
    private RelativeLayout slctdView;
    private int slctdpstn = 543;

    public interface OnItemClickListener {
        void onItemClick(Date date);
    }

    private ArrayList<Date> dateArrayList;
    private final DateSlotsAdapter.OnItemClickListener listener;

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

        void bind(final int position, final Date date, final DateSlotsAdapter.OnItemClickListener listener) {

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

            tvApntsCnts.setVisibility(View.GONE);

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


    public DateSlotsAdapter(Context context,ArrayList<Date> dateArrayList, DateSlotsAdapter.OnItemClickListener listener) {
        this.dateArrayList = dateArrayList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public DateSlotsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_date_item, parent, false);

        return new DateSlotsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DateSlotsAdapter.MyViewHolder holder, final int position) {
        holder.bind(position,dateArrayList.get(position), listener);

    }


    @Override
    public int getItemCount() {
        return dateArrayList.size();
    }

}