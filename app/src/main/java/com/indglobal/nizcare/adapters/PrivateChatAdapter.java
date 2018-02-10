package com.indglobal.nizcare.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.model.PrivateChatItem;

import java.util.ArrayList;

/**
 * Created by readyassist on 1/12/18.
 */

public class PrivateChatAdapter extends RecyclerView.Adapter<PrivateChatAdapter.MyViewHolder>{

    private Context context = null;

    public interface OnItemClickListener {
        void onItemClick(PrivateChatItem privateChatItem);
    }

    private ArrayList<PrivateChatItem> privateChatItemArrayList;
    private final PrivateChatAdapter.OnItemClickListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle,tvSubTitle;

        MyViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvSubTitle = (TextView)itemView.findViewById(R.id.tvSubTitle);

        }

        void bind(final PrivateChatItem privateChatItem, final PrivateChatAdapter.OnItemClickListener listener) {

            tvTitle.setText(Comman.capitalize(privateChatItem.getTitle()));
            tvSubTitle.setText(Comman.capitalize(privateChatItem.getName())+", "+Comman.capitalize(privateChatItem.getGender())+"*"+privateChatItem.getAge()+" Years Old * "+privateChatItem.getTime_ago());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(privateChatItem);
                }
            });

        }
    }

    public PrivateChatAdapter(Context context,ArrayList<PrivateChatItem> privateChatItemArrayList, PrivateChatAdapter.OnItemClickListener listener) {
        this.privateChatItemArrayList = privateChatItemArrayList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public PrivateChatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_grp_item, parent, false);

        return new PrivateChatAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PrivateChatAdapter.MyViewHolder holder, final int position) {
        holder.bind(privateChatItemArrayList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return privateChatItemArrayList.size();
    }


}