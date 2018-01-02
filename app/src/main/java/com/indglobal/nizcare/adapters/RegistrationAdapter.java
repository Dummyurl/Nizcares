package com.indglobal.nizcare.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.activities.AccountSetupActivity;
import com.indglobal.nizcare.model.RegistrationItem;

import java.util.ArrayList;

/**
 * Created by readyassist on 12/27/17.
 */

public class RegistrationAdapter extends RecyclerView.Adapter<RegistrationAdapter.MyViewHolder>{

    private Context context;
    ArrayList<RegistrationItem> registrationItemArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvRegNo,tvCouncil,tvYear;
        private ImageView ivRegDoc;
        private LinearLayout llRegEdit;

        public MyViewHolder(View view) {
            super(view);
            tvRegNo = (TextView)view.findViewById(R.id.tvRegNo);
            tvCouncil = (TextView)view.findViewById(R.id.tvCouncil);
            tvYear = (TextView)view.findViewById(R.id.tvYear);
            ivRegDoc = (ImageView)view.findViewById(R.id.ivRegDoc);
            llRegEdit = (LinearLayout) view.findViewById(R.id.llRegEdit);

        }
    }

    public RegistrationAdapter(Context context) {
        this.context = context;
    }

    public RegistrationAdapter(Context context, ArrayList<RegistrationItem> registrationItemArrayList) {
        this.context = context;
        this.registrationItemArrayList = registrationItemArrayList;
    }

    @Override
    public RegistrationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.registration_item, parent, false);

        return new RegistrationAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RegistrationAdapter.MyViewHolder holder, final int position) {

        RegistrationItem registrationItem = registrationItemArrayList.get(position);

        holder.ivRegDoc.setImageBitmap(registrationItem.getImage());
        holder.tvRegNo.setText(registrationItem.getRegno());
        holder.tvCouncil.setText(registrationItem.getCouncilname());
        holder.tvYear.setText(registrationItem.getYear());

        holder.llRegEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pstn = holder.getAdapterPosition();

                AccountSetupActivity.etRegno.setText(registrationItemArrayList.get(pstn).getRegno());
                AccountSetupActivity.etCouncil.setText(registrationItemArrayList.get(pstn).getCouncilname());
                AccountSetupActivity.etYear.setText(registrationItemArrayList.get(pstn).getYear());

                AccountSetupActivity.registration_image = registrationItemArrayList.get(pstn).getImage();

                AccountSetupActivity.registrationItemArrayList.remove(pstn);
                notifyItemRemoved(pstn);
                notifyItemRangeChanged(pstn, AccountSetupActivity.registrationItemArrayList.size());
                notifyItemChanged(pstn);

            }
        });


        setScaleAnimation(holder.itemView);

    }

    @Override
    public int getItemCount() {
        return registrationItemArrayList.size();
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.ZORDER_TOP, 0.5f, Animation.ZORDER_TOP, 0.5f);
        anim.setDuration(700);
        view.startAnimation(anim);
    }

}