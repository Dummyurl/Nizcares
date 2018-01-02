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
import com.indglobal.nizcare.model.DocumentItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by readyassist on 12/27/17.
 */

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.MyViewHolder>{

    private Context context;
    ArrayList<DocumentItem> documentItemArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView docType;
        private ImageView ivIds;
        private LinearLayout llIdEdit;

        public MyViewHolder(View view) {
            super(view);
            docType = (TextView)view.findViewById(R.id.docType);
            ivIds = (ImageView)view.findViewById(R.id.ivIds);
            llIdEdit = (LinearLayout) view.findViewById(R.id.llIdEdit);

        }
    }

    public DocumentAdapter(Context context) {
        this.context = context;
    }

    public DocumentAdapter(Context context, ArrayList<DocumentItem> documentItemArrayList) {
        this.context = context;
        this.documentItemArrayList = documentItemArrayList;
    }

    @Override
    public DocumentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.idproof_item, parent, false);

        return new DocumentAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DocumentAdapter.MyViewHolder holder, final int position) {

        DocumentItem documentItem = documentItemArrayList.get(position);

        holder.ivIds.setImageBitmap(documentItem.getDocument_image());
        holder.docType.setText(documentItem.getDocument_name());

        holder.llIdEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pstn = holder.getAdapterPosition();

                List<String> stringList = new ArrayList<String>();
                for (int i=0;i<AccountSetupActivity.doctypeItemArrayList.size();i++){
                    stringList.add(AccountSetupActivity.doctypeItemArrayList.get(i).getId());
                }
                int position = stringList.indexOf(documentItemArrayList.get(pstn).getDocument_id());
                if (position!=-1){
                    AccountSetupActivity.spinIdType.setSelection(position);
                }

                AccountSetupActivity.document_image = documentItemArrayList.get(pstn).getDocument_image();

                AccountSetupActivity.documentItemArrayList.remove(pstn);
                notifyItemRemoved(pstn);
                notifyItemRangeChanged(pstn, AccountSetupActivity.documentItemArrayList.size());
                notifyItemChanged(pstn);

            }
        });


        setScaleAnimation(holder.itemView);

    }

    @Override
    public int getItemCount() {
        return documentItemArrayList.size();
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.ZORDER_TOP, 0.5f, Animation.ZORDER_TOP, 0.5f);
        anim.setDuration(700);
        view.startAnimation(anim);
    }

}