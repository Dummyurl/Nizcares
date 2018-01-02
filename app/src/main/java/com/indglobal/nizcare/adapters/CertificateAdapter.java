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
import com.indglobal.nizcare.model.CertificateItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by readyassist on 12/26/17.
 */

public class CertificateAdapter extends RecyclerView.Adapter<CertificateAdapter.MyViewHolder>{

    private Context context;
    ArrayList<CertificateItem> certificateItemArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDegree,tvCollege,tvYear;
        private ImageView ivCertificate;
        private LinearLayout llEduEdit;

        public MyViewHolder(View view) {
            super(view);
            tvDegree = (TextView)view.findViewById(R.id.tvDegree);
            tvCollege = (TextView)view.findViewById(R.id.tvCollege);
            tvYear = (TextView)view.findViewById(R.id.tvYear);
            ivCertificate = (ImageView)view.findViewById(R.id.ivCertificate);
            llEduEdit = (LinearLayout) view.findViewById(R.id.llEduEdit);

        }
    }

    public CertificateAdapter(Context context) {
        this.context = context;
    }

    public CertificateAdapter(Context context, ArrayList<CertificateItem> certificateItemArrayList) {
        this.context = context;
        this.certificateItemArrayList = certificateItemArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.education_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        CertificateItem certificateItem = certificateItemArrayList.get(position);

        holder.ivCertificate.setImageBitmap(certificateItem.getImage());
        holder.tvDegree.setText(certificateItem.getDegree());
        holder.tvCollege.setText(certificateItem.getCollege());
        holder.tvYear.setText(certificateItem.getYear());

        holder.llEduEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pstn = holder.getAdapterPosition();

                AccountSetupActivity.etDegree.setText(certificateItemArrayList.get(pstn).getDegree());
                AccountSetupActivity.etClg.setText(certificateItemArrayList.get(pstn).getCollege());
                AccountSetupActivity.etYear.setText(certificateItemArrayList.get(pstn).getYear());

                AccountSetupActivity.logo = certificateItemArrayList.get(pstn).getImage();

                AccountSetupActivity.certificateItemArrayList.remove(pstn);
                notifyItemRemoved(pstn);
                notifyItemRangeChanged(pstn, AccountSetupActivity.certificateItemArrayList.size());
                notifyItemChanged(pstn);

            }
        });


        setScaleAnimation(holder.itemView);

    }

    @Override
    public int getItemCount() {
        return certificateItemArrayList.size();
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.ZORDER_TOP, 0.5f, Animation.ZORDER_TOP, 0.5f);
        anim.setDuration(700);
        view.startAnimation(anim);
    }

}