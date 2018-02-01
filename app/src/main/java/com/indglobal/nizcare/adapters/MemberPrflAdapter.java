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
import com.indglobal.nizcare.model.MemberPrflItem;

import java.util.ArrayList;

/**
 * Created by readyassist on 2/1/18.
 */

public class MemberPrflAdapter  extends RecyclerView.Adapter<MemberPrflAdapter.MyViewHolder>{

    private Context context = null;

    public interface OnItemClickListener {
        void onItemClick(MemberPrflItem memberPrflItem);
    }

    private ArrayList<MemberPrflItem> memberPrflItemArrayList;
    private final MemberPrflAdapter.OnItemClickListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDegree,tvCollege,tvYear;

        MyViewHolder(View itemView) {
            super(itemView);

            tvDegree = (TextView) itemView.findViewById(R.id.tvDegree);
            tvCollege = (TextView) itemView.findViewById(R.id.tvCollege);
            tvYear = (TextView) itemView.findViewById(R.id.tvYear);
        }

        void bind(final MemberPrflItem memberPrflItem, final MemberPrflAdapter.OnItemClickListener listener, final int position) {

            tvDegree.setText(Comman.capitalize(memberPrflItem.getTitle()));
            tvCollege.setText(Comman.capitalize(memberPrflItem.getPosition()));
            tvYear.setText(memberPrflItem.getYear());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(memberPrflItem);
                }
            });

        }
    }

    public MemberPrflAdapter(Context context,ArrayList<MemberPrflItem> memberPrflItemArrayList, MemberPrflAdapter.OnItemClickListener listener) {
        this.memberPrflItemArrayList = memberPrflItemArrayList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public MemberPrflAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.education_item_view, parent, false);

        return new MemberPrflAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MemberPrflAdapter.MyViewHolder holder, final int position) {
        holder.bind(memberPrflItemArrayList.get(position), listener,position);
    }

    @Override
    public int getItemCount() {
        return memberPrflItemArrayList.size();
    }

}