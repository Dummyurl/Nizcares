package com.indglobal.nizcare.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.commons.roundedimageview.RoundedImageView;
import com.indglobal.nizcare.model.DealsItem;
import com.indglobal.nizcare.model.InstantItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by readyassist on 1/24/18.
 */

public class InstantAdapter extends RecyclerView.Adapter<InstantAdapter.MyViewHolder>{

    private Context context = null;

    public interface OnItemClickListener {
        void onItemClick(InstantItem instantItem);
    }

    private ArrayList<InstantItem> instantItemArrayList;
    private final InstantAdapter.OnItemClickListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView rivInstant;
        private TextView tvCount;

        MyViewHolder(View itemView) {
            super(itemView);

            rivInstant = (RoundedImageView) itemView.findViewById(R.id.rivInstant);
            tvCount = (TextView) itemView.findViewById(R.id.tvCount);

        }

        void bind(final InstantItem instantItem, final InstantAdapter.OnItemClickListener listener) {

            Picasso.with(context).load(instantItem.getProfile_pic()).into(rivInstant);

            tvCount.setText(instantItem.getText_notification_count());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(instantItem);
                }
            });

        }
    }

    public InstantAdapter(Context context,ArrayList<InstantItem> instantItemArrayList, InstantAdapter.OnItemClickListener listener) {
        this.instantItemArrayList = instantItemArrayList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public InstantAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.instnt_cnsltn_item, parent, false);

        return new InstantAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final InstantAdapter.MyViewHolder holder, final int position) {
        holder.bind(instantItemArrayList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return instantItemArrayList.size();
    }


}