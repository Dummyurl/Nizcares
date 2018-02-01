package com.indglobal.nizcare.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.model.AwardPrflItem;
import com.indglobal.nizcare.model.TreatmentPrflItem;

import java.util.ArrayList;

/**
 * Created by readyassist on 2/1/18.
 */

public class TrtmntPrflAdapter extends RecyclerView.Adapter<TrtmntPrflAdapter.MyViewHolder>{

    private Context context = null;

    public interface OnItemClickListener {
        void onItemClick(TreatmentPrflItem treatmentPrflItem);
    }

    private ArrayList<TreatmentPrflItem> treatmentPrflItemArrayList;
    private final TrtmntPrflAdapter.OnItemClickListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;

        MyViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tvName);
        }

        void bind(final TreatmentPrflItem treatmentPrflItem, final TrtmntPrflAdapter.OnItemClickListener listener, final int position) {

            tvName.setText(Comman.capitalize(treatmentPrflItem.getName()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(treatmentPrflItem);
                }
            });

        }
    }

    public TrtmntPrflAdapter(Context context,ArrayList<TreatmentPrflItem> treatmentPrflItemArrayList, TrtmntPrflAdapter.OnItemClickListener listener) {
        this.treatmentPrflItemArrayList = treatmentPrflItemArrayList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public TrtmntPrflAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.treatmnt_item_view, parent, false);

        return new TrtmntPrflAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TrtmntPrflAdapter.MyViewHolder holder, final int position) {
        holder.bind(treatmentPrflItemArrayList.get(position), listener,position);
    }

    @Override
    public int getItemCount() {
        return treatmentPrflItemArrayList.size();
    }

}