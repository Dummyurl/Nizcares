package com.indglobal.nizcare.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.activities.EnquiryReplyActivity;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.commons.RippleView;
import com.indglobal.nizcare.commons.roundedimageview.RoundedImageView;
import com.indglobal.nizcare.model.EnquiryItem;
import com.indglobal.nizcare.model.HealthFeedItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by readyassist on 1/12/18.
 */

public class HealthFeedAdapter extends RecyclerView.Adapter<HealthFeedAdapter.MyViewHolder>{

    private Context context = null;

    public interface OnItemClickListener {
        void onItemClick(HealthFeedItem healthFeedItem);
    }

    private ArrayList<HealthFeedItem> healthFeedItemArrayList;
    private final HealthFeedAdapter.OnItemClickListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle,tvDescrptn,tvName,tvSpeclty;
        private RoundedImageView ivDoc;

        MyViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvDescrptn = (TextView)itemView.findViewById(R.id.tvDescrptn);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvSpeclty = (TextView)itemView.findViewById(R.id.tvSpeclty);
            ivDoc = (RoundedImageView)itemView.findViewById(R.id.ivDoc);

        }

        void bind(final HealthFeedItem healthFeedItem, final HealthFeedAdapter.OnItemClickListener listener) {

            tvTitle.setText(healthFeedItem.getTitle());
            tvDescrptn.setText(healthFeedItem.getPost());
            tvName.setText(Comman.capitalize(healthFeedItem.getDoctor_name()));
            tvSpeclty.setText(Comman.capitalize(healthFeedItem.getSpeciality_name())+"*"+healthFeedItem.getTime_ago());

            Picasso.with(context).load(healthFeedItem.getProfilr_pic()).into(ivDoc);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //listener.onItemClick(enquiryItem);
//                }
//            });

        }
    }

    public HealthFeedAdapter(Context context,ArrayList<HealthFeedItem> healthFeedItemArrayList, HealthFeedAdapter.OnItemClickListener listener) {
        this.healthFeedItemArrayList = healthFeedItemArrayList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public HealthFeedAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.hlthfeed_item, parent, false);

        return new HealthFeedAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final HealthFeedAdapter.MyViewHolder holder, final int position) {
        holder.bind(healthFeedItemArrayList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return healthFeedItemArrayList.size();
    }


}