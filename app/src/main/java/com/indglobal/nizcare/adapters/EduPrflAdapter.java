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

import java.util.ArrayList;

/**
 * Created by readyassist on 2/1/18.
 */

public class EduPrflAdapter extends RecyclerView.Adapter<EduPrflAdapter.MyViewHolder>{

    private Context context = null;

    public interface OnItemClickListener {
        void onItemClick(EduPrflItem eduPrflItem);
    }

    private ArrayList<EduPrflItem> eduPrflItemArrayList;
    private final EduPrflAdapter.OnItemClickListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDegree,tvCollege,tvYear;

        MyViewHolder(View itemView) {
            super(itemView);

            tvDegree = (TextView) itemView.findViewById(R.id.tvDegree);
            tvCollege = (TextView) itemView.findViewById(R.id.tvCollege);
            tvYear = (TextView) itemView.findViewById(R.id.tvYear);
        }

        void bind(final EduPrflItem eduPrflItem, final EduPrflAdapter.OnItemClickListener listener, final int position) {

            tvDegree.setText(Comman.capitalize(eduPrflItem.getDegree()));
            tvCollege.setText(Comman.capitalize(eduPrflItem.getCollege()));
            tvYear.setText(eduPrflItem.getYear());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(eduPrflItem);
                }
            });

        }
    }

    public EduPrflAdapter(Context context,ArrayList<EduPrflItem> eduPrflItemArrayList, EduPrflAdapter.OnItemClickListener listener) {
        this.eduPrflItemArrayList = eduPrflItemArrayList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public EduPrflAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.education_item_view, parent, false);

        return new EduPrflAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final EduPrflAdapter.MyViewHolder holder, final int position) {
        holder.bind(eduPrflItemArrayList.get(position), listener,position);
    }

    @Override
    public int getItemCount() {
        return eduPrflItemArrayList.size();
    }

}