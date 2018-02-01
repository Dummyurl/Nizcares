package com.indglobal.nizcare.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.model.EduPrflItem;
import com.indglobal.nizcare.model.HsptlPrflItem;

import java.util.ArrayList;

/**
 * Created by readyassist on 2/1/18.
 */

public class HsptlPrflAdapter extends RecyclerView.Adapter<HsptlPrflAdapter.MyViewHolder>{

    private Context context = null;

    public interface OnItemClickListener {
        void onItemClick(HsptlPrflItem hsptlPrflItem);
    }

    private ArrayList<HsptlPrflItem> hsptlPrflItemArrayList;
    private final HsptlPrflAdapter.OnItemClickListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDegree,tvCollege,tvYear;

        MyViewHolder(View itemView) {
            super(itemView);

            tvDegree = (TextView) itemView.findViewById(R.id.tvDegree);
            tvCollege = (TextView) itemView.findViewById(R.id.tvCollege);
            tvYear = (TextView) itemView.findViewById(R.id.tvYear);
        }

        void bind(final HsptlPrflItem hsptlPrflItem, final HsptlPrflAdapter.OnItemClickListener listener, final int position) {

            tvDegree.setText(Comman.capitalize(hsptlPrflItem.getName()));
            tvCollege.setText(Comman.capitalize(hsptlPrflItem.getAdress()));
            tvYear.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(hsptlPrflItem);
                }
            });

        }
    }

    public HsptlPrflAdapter(Context context,ArrayList<HsptlPrflItem> hsptlPrflItemArrayList, HsptlPrflAdapter.OnItemClickListener listener) {
        this.hsptlPrflItemArrayList = hsptlPrflItemArrayList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public HsptlPrflAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.education_item_view, parent, false);

        return new HsptlPrflAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final HsptlPrflAdapter.MyViewHolder holder, final int position) {
        holder.bind(hsptlPrflItemArrayList.get(position), listener,position);
    }

    @Override
    public int getItemCount() {
        return hsptlPrflItemArrayList.size();
    }

}