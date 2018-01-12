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
import com.indglobal.nizcare.model.EnquiryItem;
import com.indglobal.nizcare.model.PrivateChatItem;

import java.util.ArrayList;

/**
 * Created by readyassist on 1/12/18.
 */

public class EnquiryAdapter extends RecyclerView.Adapter<EnquiryAdapter.MyViewHolder>{

    private Context context = null;

    public interface OnItemClickListener {
        void onItemClick(EnquiryItem enquiryItem);
    }

    private ArrayList<EnquiryItem> enquiryItemArrayList;
    private final EnquiryAdapter.OnItemClickListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle,tvDays,tvDescrptn,tvName,tvGender;
        private RippleView rplReply,rplCall;

        MyViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvDays = (TextView)itemView.findViewById(R.id.tvDays);
            tvDescrptn = (TextView)itemView.findViewById(R.id.tvDescrptn);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvGender = (TextView)itemView.findViewById(R.id.tvGender);

            rplReply = (RippleView) itemView.findViewById(R.id.rplReply);
            rplCall = (RippleView)itemView.findViewById(R.id.rplCall);

        }

        void bind(final EnquiryItem enquiryItem, final EnquiryAdapter.OnItemClickListener listener) {

            tvTitle.setText(enquiryItem.getProblem_name());
            tvDays.setText(enquiryItem.getTime_ago());
            tvDescrptn.setText(enquiryItem.getDescription());
            tvName.setText(Comman.capitalize(enquiryItem.getPatient_name()));
            tvGender.setText(Comman.capitalize(enquiryItem.getGender())+"*"+enquiryItem.getAge()+" Years Old");

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //listener.onItemClick(enquiryItem);
//                }
//            });

            rplCall.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                @Override
                public void onComplete(RippleView rippleView) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", enquiryItem.getCountry_code()+enquiryItem.getMobile_no(), null));
                    context.startActivity(intent);
                }
            });

            rplReply.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                @Override
                public void onComplete(RippleView rippleView) {
                    Intent ii = new Intent(context,EnquiryReplyActivity.class);
                    ii.putExtra("enquiry_id",enquiryItem.getEnquiry_id());
                    context.startActivity(ii);
                }
            });

        }
    }

    public EnquiryAdapter(Context context,ArrayList<EnquiryItem> enquiryItemArrayList, EnquiryAdapter.OnItemClickListener listener) {
        this.enquiryItemArrayList = enquiryItemArrayList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public EnquiryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.enqrs_item, parent, false);

        return new EnquiryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final EnquiryAdapter.MyViewHolder holder, final int position) {
        holder.bind(enquiryItemArrayList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return enquiryItemArrayList.size();
    }


}