package com.indglobal.nizcare.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.indglobal.nizcare.R;
import com.indglobal.nizcare.commons.roundedimageview.RoundedImageView;
import com.indglobal.nizcare.model.ReferDocImgItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by readyassist on 1/29/18.
 */

public class ReferImagesAdapter extends RecyclerView.Adapter<ReferImagesAdapter.MyViewHolder>{

    private Context context = null;

    public interface OnItemClickListener {
        void onItemClick(ReferDocImgItem referDocImgItem);
    }

    private ArrayList<ReferDocImgItem> referDocImgItemArrayList;

    class MyViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView ivImage;

        MyViewHolder(View itemView) {
            super(itemView);
            ivImage = (RoundedImageView) itemView.findViewById(R.id.ivImage);
        }

        void bind(final int position, final ReferDocImgItem referDocImgItem) {

            Picasso.with(context).load(referDocImgItem.getMedia()).into(ivImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //openRescheduleDialog(apointItem,position);
                }
            });
        }
    }


    public ReferImagesAdapter(Context context,ArrayList<ReferDocImgItem> referDocImgItemArrayList) {
        this.referDocImgItemArrayList = referDocImgItemArrayList;
        this.context = context;
    }

    @Override
    public ReferImagesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.refer_doctr_img_item, parent, false);

        return new ReferImagesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ReferImagesAdapter.MyViewHolder holder, final int position) {
        holder.bind(position,referDocImgItemArrayList.get(position));

    }

    @Override
    public int getItemCount() {
        return referDocImgItemArrayList.size();
    }

}