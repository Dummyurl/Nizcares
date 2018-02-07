package com.indglobal.nizcare.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.model.MedicineNameItem;

import java.util.ArrayList;

/**
 * Created by readyassist on 2/7/18.
 */

public class AutoCmpleteAdapter extends RecyclerView.Adapter<AutoCmpleteAdapter.MyViewHolder>{

    private Context context = null;

    public interface OnItemClickListener {
        void onItemClick(MedicineNameItem medicineNameItem);
    }

    private ArrayList<MedicineNameItem> medicineNameItemArrayList;
    private final AutoCmpleteAdapter.OnItemClickListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvItem;

        MyViewHolder(View itemView) {
            super(itemView);

            tvItem = (TextView) itemView.findViewById(R.id.text1);

        }

        void bind(final MedicineNameItem medicineNameItem, final AutoCmpleteAdapter.OnItemClickListener listener) {

            tvItem.setText(Comman.capitalize(medicineNameItem.getMedicine_name()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(medicineNameItem);
                }

            });

        }
    }

    public AutoCmpleteAdapter(Context context,ArrayList<MedicineNameItem> medicineNameItemArrayList, AutoCmpleteAdapter.OnItemClickListener listener) {
        this.medicineNameItemArrayList = medicineNameItemArrayList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public AutoCmpleteAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.autocmplt_item, parent, false);

        return new AutoCmpleteAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AutoCmpleteAdapter.MyViewHolder holder, final int position) {
        holder.bind(medicineNameItemArrayList.get(position), listener);

    }

    @Override
    public int getItemCount() {
        return medicineNameItemArrayList.size();
    }

}