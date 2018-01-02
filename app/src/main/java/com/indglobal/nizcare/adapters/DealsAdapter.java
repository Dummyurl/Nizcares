package com.indglobal.nizcare.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.indglobal.nizcare.R;
import com.indglobal.nizcare.commons.roundedimageview.RoundedImageView;
import com.indglobal.nizcare.model.DealsItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by readyassist on 1/2/18.
 */

public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.MyViewHolder>{

    private Context context = null;

    public interface OnItemClickListener {
        void onItemClick(DealsItem dealsItem);
    }

    private ArrayList<DealsItem> dealsItemArrayList;
    private final OnItemClickListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView rivDeal;
        private TextView tvTitle,tvDscrptn;

        MyViewHolder(View itemView) {
            super(itemView);

            rivDeal = (RoundedImageView) itemView.findViewById(R.id.rivDeal);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvDscrptn = (TextView)itemView.findViewById(R.id.tvDscrptn);

        }

        void bind(final DealsItem dealsItem, final DealsAdapter.OnItemClickListener listener) {

            Picasso.with(context).load(dealsItem.getMedia()).into(rivDeal);

            tvTitle.setText(dealsItem.getOffer());
            tvDscrptn.setText(dealsItem.getDescription());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(dealsItem);
                }
            });

        }
    }

    public DealsAdapter(Context context,ArrayList<DealsItem> dealsItemArrayList, DealsAdapter.OnItemClickListener listener) {
        this.dealsItemArrayList = dealsItemArrayList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public DealsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.deals_item, parent, false);

        return new DealsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DealsAdapter.MyViewHolder holder, final int position) {
        holder.bind(dealsItemArrayList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return dealsItemArrayList.size();
    }


}