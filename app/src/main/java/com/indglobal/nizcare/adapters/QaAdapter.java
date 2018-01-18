package com.indglobal.nizcare.adapters;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.activities.ReScheduleActivity;
import com.indglobal.nizcare.model.AnswerItem;
import com.indglobal.nizcare.model.QAItem;
import com.indglobal.nizcare.model.SlotItem;
import com.indglobal.nizcare.model.SlotsMainItem;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by readyassist on 1/13/18.
 */

public class QaAdapter extends RecyclerView.Adapter<QaAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<QAItem> qaItemArrayList;
    private QaAdapter.MyViewHolder myViewHolder;

    public interface OnItemClickListener {
        void onItemClick(QAItem qaItem);
    }

    private OnItemClickListener listener;


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvQstn,tvSpeclty;
        private ImageView ivType,ivDroparw;
        private RecyclerView rvAnswrs;

        MyViewHolder(View view) {
            super(view);

            tvQstn = (TextView)view.findViewById(R.id.tvQstn);
            tvSpeclty = (TextView) view.findViewById(R.id.tvSpeclty);
            rvAnswrs = (RecyclerView) view.findViewById(R.id.rvAnswrs);
        }
    }

    public QaAdapter(Context context) {
        this.context = context;
    }

    public QaAdapter(Context context, ArrayList<QAItem> qaItemArrayList) {
        this.context = context;
        this.qaItemArrayList = qaItemArrayList;
    }

    @Override
    public QaAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.qa_item, parent, false);

        return new QaAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final QaAdapter.MyViewHolder holder, final int position) {

        myViewHolder = holder;

        final QAItem qaItem = qaItemArrayList.get(position);

        holder.tvQstn.setText(qaItem.getTitle());
        holder.tvSpeclty.setText("in "+qaItem.getSpeciality());

        holder.rvAnswrs.setAdapter(null);
        AnswerAdapter answerAdapter = new AnswerAdapter(context,qaItem.getAnswerItemArrayList(), new AnswerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AnswerItem answerItem) {

            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext());
        holder.rvAnswrs.setLayoutManager(layoutManager);
        holder.rvAnswrs.setAdapter(answerAdapter);

    }

    @Override
    public int getItemCount() {
        return qaItemArrayList.size();
    }

}