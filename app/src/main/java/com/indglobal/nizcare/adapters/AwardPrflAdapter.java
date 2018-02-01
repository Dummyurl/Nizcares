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
import com.indglobal.nizcare.model.EduPrflItem;

import java.util.ArrayList;

/**
 * Created by readyassist on 2/1/18.
 */

public class AwardPrflAdapter extends RecyclerView.Adapter<AwardPrflAdapter.MyViewHolder>{

    private Context context = null;

    public interface OnItemClickListener {
        void onItemClick(AwardPrflItem awardPrflItem);
    }

    private ArrayList<AwardPrflItem> awardPrflItemArrayList;
    private final AwardPrflAdapter.OnItemClickListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDegree,tvCollege,tvYear;

        MyViewHolder(View itemView) {
            super(itemView);

            tvDegree = (TextView) itemView.findViewById(R.id.tvDegree);
            tvCollege = (TextView) itemView.findViewById(R.id.tvCollege);
            tvYear = (TextView) itemView.findViewById(R.id.tvYear);
        }

        void bind(final AwardPrflItem awardPrflItem, final AwardPrflAdapter.OnItemClickListener listener, final int position) {

            tvDegree.setText(Comman.capitalize(awardPrflItem.getTitle()));
            tvCollege.setText(Comman.capitalize(awardPrflItem.getCollege()));
            tvYear.setText(awardPrflItem.getYear());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(awardPrflItem);
                }
            });

        }
    }

    public AwardPrflAdapter(Context context,ArrayList<AwardPrflItem> awardPrflItemArrayList, AwardPrflAdapter.OnItemClickListener listener) {
        this.awardPrflItemArrayList = awardPrflItemArrayList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public AwardPrflAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.education_item_view, parent, false);

        return new AwardPrflAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AwardPrflAdapter.MyViewHolder holder, final int position) {
        holder.bind(awardPrflItemArrayList.get(position), listener,position);
    }

    @Override
    public int getItemCount() {
        return awardPrflItemArrayList.size();
    }

}