package com.indglobal.nizcare.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.commons.roundedimageview.RoundedImageView;
import com.indglobal.nizcare.model.CnsltnChatItem;
import com.indglobal.nizcare.model.ConsultationItem;
import com.indglobal.nizcare.model.InstantItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by readyassist on 1/25/18.
 */

public class CnsltChatAdapter  extends RecyclerView.Adapter<CnsltChatAdapter.MyViewHolder>{

    private Context context = null;

    public interface OnItemClickListener {
        void onItemClick(CnsltnChatItem cnsltnChatItem);
    }

    private ArrayList<CnsltnChatItem> cnsltnChatItemArrayList;
    private final CnsltChatAdapter.OnItemClickListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout llRecieve,llSend;
        private TextView tvRMsg,tvRTime,tvSMsg,tvSTime;

        MyViewHolder(View itemView) {
            super(itemView);

            llRecieve = (LinearLayout) itemView.findViewById(R.id.llRecieve);
            llSend = (LinearLayout) itemView.findViewById(R.id.llSend);
            tvRMsg = (TextView)itemView.findViewById(R.id.tvRMsg);
            tvRTime = (TextView)itemView.findViewById(R.id.tvRTime);
            tvSMsg = (TextView)itemView.findViewById(R.id.tvSMsg);
            tvSTime = (TextView)itemView.findViewById(R.id.tvSTime);

        }

        void bind(final CnsltnChatItem cnsltnChatItem, final CnsltChatAdapter.OnItemClickListener listener) {

            String side = cnsltnChatItem.getSide();

            if (side.equalsIgnoreCase("1")){
                llRecieve.setVisibility(View.GONE);
                tvSMsg.setText(cnsltnChatItem.getMessege());
                tvSTime.setText(cnsltnChatItem.getTime());
                llSend.setVisibility(View.VISIBLE);
            }else {
                llSend.setVisibility(View.GONE);
                tvRMsg.setText(cnsltnChatItem.getMessege());
                tvRTime.setText(cnsltnChatItem.getTime());
                llRecieve.setVisibility(View.VISIBLE);
            }


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }

    public CnsltChatAdapter(Context context,ArrayList<CnsltnChatItem> cnsltnChatItemArrayList, CnsltChatAdapter.OnItemClickListener listener) {
        this.cnsltnChatItemArrayList = cnsltnChatItemArrayList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public CnsltChatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cnslt_chat_item, parent, false);

        return new CnsltChatAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CnsltChatAdapter.MyViewHolder holder, final int position) {
        holder.bind(cnsltnChatItemArrayList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return cnsltnChatItemArrayList.size();
    }

    public void addItem(CnsltnChatItem cncltnItem){
        cnsltnChatItemArrayList.add(cncltnItem);
        notifyItemInserted(cnsltnChatItemArrayList.size() - 1);
    }

}