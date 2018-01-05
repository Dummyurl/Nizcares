package com.indglobal.nizcare.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.model.SlotItem;

import java.util.ArrayList;

/**
 * Created by readyassist on 1/5/18.
 */

public class SlotAdapter extends RecyclerView.Adapter<SlotAdapter.MyViewHolder>{

    private Context context = null;
    private TextView slctdText;
    private int slctdpstn = 312;

    public interface OnItemClickListener {
        void onItemClick(SlotItem slotItem);
    }
    private ArrayList<SlotItem> slotItemArrayList;
    private final OnItemClickListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTime;

        MyViewHolder(View itemView) {
            super(itemView);
            tvTime = (TextView)itemView.findViewById(R.id.tvTime);
        }

        void bind(final int position, final SlotItem slotItem, final OnItemClickListener listener) {

            tvTime.setText(slotItem.getSlot());
            String status = slotItem.getBooking_status();
            if (status.equalsIgnoreCase("0")){
                if (slctdpstn==position){
                    tvTime.setTextColor(context.getResources().getColor(R.color.white));
                    tvTime.setBackground(context.getResources().getDrawable(R.drawable.round_fill_green_full));
                    slctdText = tvTime;
                }else {
                    tvTime.setTextColor(context.getResources().getColor(R.color.lightGreen));
                    tvTime.setBackground(context.getResources().getDrawable(R.drawable.round_stroke_green_full));
                }
            }else {
                tvTime.setTextColor(context.getResources().getColor(R.color.darkGray));
                tvTime.setBackground(context.getResources().getDrawable(R.drawable.round_corner_stroke_gray_full));
            }


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (slctdText!=null){
                        slctdText.setTextColor(context.getResources().getColor(R.color.lightGreen));
                        slctdText.setBackground(context.getResources().getDrawable(R.drawable.round_stroke_green_full));
                    }

                    tvTime.setTextColor(context.getResources().getColor(R.color.white));
                    tvTime.setBackground(context.getResources().getDrawable(R.drawable.round_fill_green_full));
                    slctdText = tvTime;

                    slctdpstn = position;
                    listener.onItemClick(slotItem);
                }

            });
        }

    }

    public SlotAdapter(Context context,ArrayList<SlotItem> slotItemArrayList, OnItemClickListener listener) {
        this.slotItemArrayList = slotItemArrayList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.slot_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.bind(position,slotItemArrayList.get(position), listener);

    }


    @Override
    public int getItemCount() {
        return slotItemArrayList.size();
    }

}