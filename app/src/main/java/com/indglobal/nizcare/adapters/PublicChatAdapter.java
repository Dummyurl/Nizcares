package com.indglobal.nizcare.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.model.NewsItem;
import com.indglobal.nizcare.model.PublicChatItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by readyassist on 1/12/18.
 */

public class PublicChatAdapter extends RecyclerView.Adapter<PublicChatAdapter.MyViewHolder>{

    private Context context = null;

    public interface OnItemClickListener {
        void onItemClick(PublicChatItem publicChatItem);
    }

    private ArrayList<PublicChatItem> publicChatItemArrayList;
    private final PublicChatAdapter.OnItemClickListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle,tvSubTitle;

        MyViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvSubTitle = (TextView)itemView.findViewById(R.id.tvSubTitle);

        }

        void bind(final PublicChatItem publicChatItem, final PublicChatAdapter.OnItemClickListener listener) {

            tvTitle.setText(publicChatItem.getGroup_name());
            tvSubTitle.setText(publicChatItem.getMembers()+" Members");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(publicChatItem);
                }
            });

        }
    }

    public PublicChatAdapter(Context context,ArrayList<PublicChatItem> publicChatItemArrayList, PublicChatAdapter.OnItemClickListener listener) {
        this.publicChatItemArrayList = publicChatItemArrayList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public PublicChatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_grp_item, parent, false);

        return new PublicChatAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PublicChatAdapter.MyViewHolder holder, final int position) {
        holder.bind(publicChatItemArrayList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return publicChatItemArrayList.size();
    }


}