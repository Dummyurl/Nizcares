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
import com.indglobal.nizcare.model.ApointItem;
import com.indglobal.nizcare.model.ApointMainItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.indglobal.nizcare.R.id.tvDay;

/**
 * Created by readyassist on 12/30/17.
 */

public class ApointmentAdapter extends RecyclerView.Adapter<ApointmentAdapter.MyViewHolder>{

    private Context context = null;

    public interface OnItemClickListener {
        void onItemClick(ApointItem apointItem);
    }

    private ArrayList<ApointItem> apointItemArrayList;

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTime,tvStatus,tvStatusDot,tvPName,tvHName;

        MyViewHolder(View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvStatus = (TextView)itemView.findViewById(R.id.tvStatus);
            tvStatusDot = (TextView)itemView.findViewById(R.id.tvStatusDot);
            tvPName = (TextView)itemView.findViewById(R.id.tvPName);
            tvHName = (TextView)itemView.findViewById(R.id.tvHName);
        }

        void bind(final int position, final ApointItem apointItem) {

            tvTime.setText(apointItem.getAppointment_time());
            tvPName.setText(apointItem.getPatient_name());
            tvHName.setText(apointItem.getHospital_name());

            String status = apointItem.getStatus();
            if (status.equalsIgnoreCase("1")){
                tvStatus.setText("UPCOMING");
                tvStatus.setTextColor(context.getResources().getColor(R.color.yellow));
                tvStatusDot.setBackground(context.getResources().getDrawable(R.drawable.circle_fill_yellow));
            }else if (status.equalsIgnoreCase("2")){
                tvStatus.setText("DONE");
                tvStatus.setTextColor(context.getResources().getColor(R.color.lightGreen));
                tvStatusDot.setBackground(context.getResources().getDrawable(R.drawable.circle_fill_green));
            }else if (status.equalsIgnoreCase("3")||status.equalsIgnoreCase("4")){
                tvStatus.setText("CANCELLED");
                tvStatus.setTextColor(context.getResources().getColor(R.color.red));
                tvStatusDot.setBackground(context.getResources().getDrawable(R.drawable.circle_fill_red));
            }else {
                tvStatus.setText("NO SHOW");
                tvStatus.setTextColor(context.getResources().getColor(R.color.lightRed));
                tvStatusDot.setBackground(context.getResources().getDrawable(R.drawable.circle_fill_light_red));
            }


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //listener.onItemClick(apointItem);
                }

            });
        }


    }


    public ApointmentAdapter(Context context,ArrayList<ApointItem> apointItemArrayList) {
        this.apointItemArrayList = apointItemArrayList;
        this.context = context;
    }

    @Override
    public ApointmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.apoints_sub_item, parent, false);

        return new ApointmentAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ApointmentAdapter.MyViewHolder holder, final int position) {
        holder.bind(position,apointItemArrayList.get(position));

    }


    @Override
    public int getItemCount() {
        return apointItemArrayList.size();
    }

}