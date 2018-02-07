package com.indglobal.nizcare.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.activities.AddPrscrptnActivity;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.model.MedicineItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by readyassist on 2/7/18.
 */

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MyViewHolder>{

    private Context context;
    ArrayList<MedicineItem> medicineItemArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName,tvDays,tvEdit,tvDelete;

        public MyViewHolder(View view) {
            super(view);
            tvName = (TextView)view.findViewById(R.id.tvName);
            tvDays = (TextView)view.findViewById(R.id.tvDays);
            tvEdit = (TextView)view.findViewById(R.id.tvEdit);
            tvDelete = (TextView) view.findViewById(R.id.tvDelete);

        }
    }

    public MedicineAdapter(Context context) {
        this.context = context;
    }

    public MedicineAdapter(Context context, ArrayList<MedicineItem> medicineItemArrayList) {
        this.context = context;
        this.medicineItemArrayList = medicineItemArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final MedicineItem medicineItem = medicineItemArrayList.get(position);

        holder.tvName.setText(Comman.capitalize(medicineItem.getMedicine_name())+" "+medicineItem.getDosageType());

        String timings = "";
        if (medicineItem.isMorning()){
            timings = "Morning";
        }

        if (medicineItem.isAfternoon()){
            if (timings.equalsIgnoreCase("")){
                timings = "Afternoon";
            }else {
                timings = timings+", Afternoon";
            }

        }

        if (medicineItem.isNight()){
            if (timings.equalsIgnoreCase("")){
                timings = "Night";
            }else {
                timings = timings+", Night";
            }
        }

        holder.tvDays.setText(medicineItem.getDuration()+" * "+timings);

        holder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (context instanceof AddPrscrptnActivity){
                    ((AddPrscrptnActivity)context).editMedicineItem(medicineItem);
                    int pstn = holder.getAdapterPosition();
                    AddPrscrptnActivity.medicineItemArrayList.remove(pstn);
                    notifyItemRemoved(pstn);
                    notifyItemRangeChanged(pstn, AddPrscrptnActivity.medicineItemArrayList.size());
                    notifyItemChanged(pstn);
                }

            }
        });

        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pstn = holder.getAdapterPosition();
                AddPrscrptnActivity.medicineItemArrayList.remove(pstn);
                notifyItemRemoved(pstn);
                notifyItemRangeChanged(pstn, AddPrscrptnActivity.medicineItemArrayList.size());
                notifyItemChanged(pstn);
            }
        });

    }

    @Override
    public int getItemCount() {
        return medicineItemArrayList.size();
    }

}