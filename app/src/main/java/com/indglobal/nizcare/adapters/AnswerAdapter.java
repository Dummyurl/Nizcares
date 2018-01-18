package com.indglobal.nizcare.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.commons.roundedimageview.RoundedImageView;
import com.indglobal.nizcare.model.AnswerItem;
import com.indglobal.nizcare.model.HealthFeedItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by readyassist on 1/13/18.
 */

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.MyViewHolder>{

    private Context context = null;

    public interface OnItemClickListener {
        void onItemClick(AnswerItem answerItem);
    }

    private ArrayList<AnswerItem> answerItemArrayList;
    private final AnswerAdapter.OnItemClickListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDrName,tvDrSpeclty,tvDrAnswr,tvVotes;
        private RoundedImageView ivDoc;

        MyViewHolder(View itemView) {
            super(itemView);

            tvDrName = (TextView) itemView.findViewById(R.id.tvDrName);
            tvDrSpeclty = (TextView)itemView.findViewById(R.id.tvDrSpeclty);
            tvDrAnswr = (TextView) itemView.findViewById(R.id.tvDrAnswr);
            tvVotes = (TextView)itemView.findViewById(R.id.tvVotes);
            ivDoc = (RoundedImageView)itemView.findViewById(R.id.ivDoc);

        }

        void bind(final AnswerItem answerItem, final AnswerAdapter.OnItemClickListener listener) {

            tvDrName.setText(Comman.capitalize(answerItem.getName()));
            tvDrSpeclty.setText(answerItem.getSpeciality()+" * "+answerItem.getTime_ago());
            tvDrAnswr.setText(answerItem.getAnswers());
            tvVotes.setText("Upvote * "+answerItem.getVotes());

            Picasso.with(context).load(answerItem.getProfilr_pic()).into(ivDoc);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //listener.onItemClick(enquiryItem);
//                }
//            });

        }
    }

    public AnswerAdapter(Context context,ArrayList<AnswerItem> answerItemArrayList, AnswerAdapter.OnItemClickListener listener) {
        this.answerItemArrayList = answerItemArrayList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public AnswerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_item, parent, false);

        return new AnswerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AnswerAdapter.MyViewHolder holder, final int position) {
        holder.bind(answerItemArrayList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return answerItemArrayList.size();
    }


}