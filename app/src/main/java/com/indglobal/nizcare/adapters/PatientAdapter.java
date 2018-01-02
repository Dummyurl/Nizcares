package com.indglobal.nizcare.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.commons.roundedimageview.RoundedImageView;
import com.indglobal.nizcare.model.NewsItem;
import com.indglobal.nizcare.model.PatientItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by readyassist on 1/2/18.
 */

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.MyViewHolder> implements Filterable {

    private Context context = null;

    public interface OnItemClickListener {
        void onItemClick(PatientItem patientItem);
    }

    private ArrayList<PatientItem> patientItemArrayList;
    private ArrayList<PatientItem> patientItemArrayListFiltered;
    private final PatientAdapter.OnItemClickListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView ivPatient;
        private TextView tvName,tvGndrAge;

        MyViewHolder(View itemView) {
            super(itemView);

            ivPatient = (RoundedImageView) itemView.findViewById(R.id.ivPatient);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvGndrAge = (TextView)itemView.findViewById(R.id.tvGndrAge);

        }

        void bind(final PatientItem patientItem, final PatientAdapter.OnItemClickListener listener) {

            Picasso.with(context).load(patientItem.getProfile_pic()).into(ivPatient);

            tvName.setText(Comman.capitalize(patientItem.getName()));
            String genderType = patientItem.getGender();
            String gender = "Male";
            if (genderType.equalsIgnoreCase("1")){
                gender = "Male";
            }else {
                gender = "Female";
            }

            tvGndrAge.setText(gender+" * "+patientItem.getAge()+" Years old");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(patientItem);
                }
            });

        }
    }

    public PatientAdapter(Context context,ArrayList<PatientItem> patientItemArrayList, PatientAdapter.OnItemClickListener listener) {
        this.patientItemArrayList = patientItemArrayList;
        this.patientItemArrayListFiltered = patientItemArrayList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public PatientAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_item, parent, false);

        return new PatientAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PatientAdapter.MyViewHolder holder, final int position) {
        holder.bind(patientItemArrayListFiltered.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return patientItemArrayListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    patientItemArrayListFiltered = patientItemArrayList;
                } else {
                    ArrayList<PatientItem> filteredList = new ArrayList<>();
                    for (PatientItem row : patientItemArrayList) {

                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getMobile_no().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    patientItemArrayListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = patientItemArrayListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults filterResults) {
                patientItemArrayListFiltered = (ArrayList<PatientItem>)filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}