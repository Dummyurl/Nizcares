package com.indglobal.nizcare.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.indglobal.nizcare.R;
import com.indglobal.nizcare.activities.ConsltnChatActivity;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.model.CnsltnItem;

import java.util.ArrayList;

/**
 * Created by readyassist on 1/24/18.
 */

public class CnsltntAdapter extends RecyclerView.Adapter<CnsltntAdapter.MyViewHolder>{

    private Context context = null;

    public interface OnItemClickListener {
        void onItemClick(CnsltnItem cnsltnItem);
    }

    private ArrayList<CnsltnItem> cnsltnItemArrayList;

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

        void bind(final int position, final CnsltnItem cnsltnItem) {

            tvTime.setText(cnsltnItem.getConsultation_time());
            tvPName.setText(Comman.capitalize(cnsltnItem.getName()));
           // tvHName.setText(Comman.capitalize(apointItem.getHospital_name()));

            String status = cnsltnItem.getStatus();
            if (status.equalsIgnoreCase("1")){
                tvStatus.setText("UPCOMING");
                tvStatus.setTextColor(context.getResources().getColor(R.color.yellow));
                tvStatusDot.setBackground(context.getResources().getDrawable(R.drawable.circle_fill_yellow));
            }else if (status.equalsIgnoreCase("2")){
                tvStatus.setText("DONE");
                tvStatus.setTextColor(context.getResources().getColor(R.color.lightGreen));
                tvStatusDot.setBackground(context.getResources().getDrawable(R.drawable.circle_fill_green));
            }else{
                tvStatus.setText("No Response");
                tvStatus.setTextColor(context.getResources().getColor(R.color.red));
                tvStatusDot.setBackground(context.getResources().getDrawable(R.drawable.circle_fill_red));
            }

            String consult_type = cnsltnItem.getConsult_type();
            if (consult_type.equalsIgnoreCase("1")){
                tvHName.setText(context.getResources().getString(R.string.txtmsgs));
            }else if (consult_type.equalsIgnoreCase("2")){
                tvHName.setText(context.getResources().getString(R.string.phncall));
            }else if (consult_type.equalsIgnoreCase("3")){
                tvHName.setText(context.getResources().getString(R.string.vdocall));
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ii = new Intent(context,ConsltnChatActivity.class);
                    ii.putExtra("patient_name",cnsltnItem.getName());
                    ii.putExtra("consultation_id",cnsltnItem.getConsultation_id());
                    context.startActivity(ii);
                }
            });
        }
    }


    public CnsltntAdapter(Context context,ArrayList<CnsltnItem> cnsltnItemArrayList) {
        this.cnsltnItemArrayList = cnsltnItemArrayList;
        this.context = context;
    }

    @Override
    public CnsltntAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.apoints_sub_item, parent, false);

        return new CnsltntAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CnsltntAdapter.MyViewHolder holder, final int position) {
        holder.bind(position,cnsltnItemArrayList.get(position));

    }

    @Override
    public int getItemCount() {
        return cnsltnItemArrayList.size();
    }

}