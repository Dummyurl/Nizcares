package com.indglobal.nizcare.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.indglobal.nizcare.R;
import com.indglobal.nizcare.activities.ReferDoctorActivity;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.commons.CustomRequest;
import com.indglobal.nizcare.commons.VolleySingleton;
import com.indglobal.nizcare.commons.roundedimageview.RoundedImageView;
import com.indglobal.nizcare.model.ReferDoctorItem;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by readyassist on 1/29/18.
 */

public class ReferDoctAdapter extends RecyclerView.Adapter<ReferDoctAdapter.MyViewHolder> implements Filterable {

    private Context context = null;
    int referedCount = 0;

    public interface OnItemClickListener {
        void onItemClick(ReferDoctorItem referDoctorItem);
    }

    private ArrayList<ReferDoctorItem> referDoctorItemArrayList;
    private ArrayList<ReferDoctorItem> referDoctorItemArrayListFiltered;
    private final ReferDoctAdapter.OnItemClickListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView ivDoctr;
        private TextView tvName,tvSpeclty,tvExp,tvAdrs,tvRatings,tvReviews;
        private RelativeLayout rlOnline,rlRefer;
        private RecyclerView rvImages;
        private ImageView ivTick;

        MyViewHolder(View itemView) {
            super(itemView);

            ivDoctr = (RoundedImageView) itemView.findViewById(R.id.ivDoctr);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvSpeclty = (TextView)itemView.findViewById(R.id.tvSpeclty);
            rlOnline = (RelativeLayout)itemView.findViewById(R.id.rlOnline);
            rvImages = (RecyclerView)itemView.findViewById(R.id.rvImages);
            rlRefer = (RelativeLayout)itemView.findViewById(R.id.rlRefer);
            tvExp = (TextView)itemView.findViewById(R.id.tvExp);
            tvAdrs = (TextView)itemView.findViewById(R.id.tvAdrs);
            tvRatings = (TextView)itemView.findViewById(R.id.tvRatings);
            tvReviews = (TextView)itemView.findViewById(R.id.tvReviews);
            ivTick = (ImageView)itemView.findViewById(R.id.ivTick);

        }

        void bind(final ReferDoctorItem referDoctorItem, final ReferDoctAdapter.OnItemClickListener listener,final int pstn) {

            Picasso.with(context).load(referDoctorItem.getProfile_pic()).into(ivDoctr);

            tvName.setText(referDoctorItem.getPrefix()+" "+Comman.capitalize(referDoctorItem.getName()));
            tvSpeclty.setText(Comman.capitalize(referDoctorItem.getSpeciality())+" * "+referDoctorItem.getDegree());
            tvExp.setText(referDoctorItem.getExperience()+" yrs experience");
            tvAdrs.setText(referDoctorItem.getHospital_address());
            tvRatings.setText(referDoctorItem.getRating()+"/5");
            tvReviews.setText(referDoctorItem.getTotal_reviews()+" Reviews");

            String online = referDoctorItem.getOnline_status();
            if (online.equalsIgnoreCase("1")){
                rlOnline.setVisibility(View.VISIBLE);
            }else {
                rlOnline.setVisibility(View.GONE);
            }

            final String refered = referDoctorItem.getRefered();
            if (refered.equalsIgnoreCase("1")){
                rlRefer.setBackground(context.getResources().getDrawable(R.drawable.circle_fill_green));
                ivTick.setVisibility(View.VISIBLE);
                referedCount = referedCount+1;
            }else {
                rlRefer.setBackground(context.getResources().getDrawable(R.drawable.circle_fill_gray_stroke));
                ivTick.setVisibility(View.GONE);
            }

            ReferDoctorActivity.tvRefer.setText("Refer("+referedCount+")");

            rvImages.setAdapter(null);
            ReferImagesAdapter referImagesAdapter = new ReferImagesAdapter(context,referDoctorItem.getReferDocImgItemArrayList());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
            rvImages.setLayoutManager(layoutManager);
            rvImages.setAdapter(referImagesAdapter);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //listener.onItemClick(patientItem);
                }
            });

            rlRefer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (refered.equalsIgnoreCase("1")){
                        Toast.makeText(context,context.getResources().getString(R.string.alrdyRefered),Toast.LENGTH_SHORT).show();
                    }else {
                        ReferDoctorActivity.prgLoading.setVisibility(View.VISIBLE);
                        referDoctor(ReferDoctorActivity.appointment_id,referDoctorItem.getDoctor_id(),pstn);
                    }
                }
            });
        }
    }

    public ReferDoctAdapter(Context context,ArrayList<ReferDoctorItem> referDoctorItemArrayList, ReferDoctAdapter.OnItemClickListener listener) {
        this.referDoctorItemArrayList = referDoctorItemArrayList;
        this.referDoctorItemArrayListFiltered = referDoctorItemArrayList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public ReferDoctAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.refer_doctr_item, parent, false);

        return new ReferDoctAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ReferDoctAdapter.MyViewHolder holder, final int position) {
        holder.bind(referDoctorItemArrayListFiltered.get(position), listener,position);
    }

    @Override
    public int getItemCount() {
        return referDoctorItemArrayListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    referDoctorItemArrayListFiltered = referDoctorItemArrayList;
                } else {
                    ArrayList<ReferDoctorItem> filteredList = new ArrayList<>();
                    for (ReferDoctorItem row : referDoctorItemArrayList) {

                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getMobile_no().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    referDoctorItemArrayListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = referDoctorItemArrayListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults filterResults) {
                referDoctorItemArrayListFiltered = (ArrayList<ReferDoctorItem>)filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    private void referDoctor(String appointment_id, String doctor_id, final int pstn) {

        String url = context.getResources().getString(R.string.referDoctrApi);
        String token = Comman.getPreferences(context,"token");
        url = url+"?token="+token;

        Map<String, String> params = new HashMap<>();
        params.put("appointment_id",appointment_id);
        params.put("doctor_id",doctor_id);

        String RFRDOCTRHIT = "refer_doctr_hit";
        VolleySingleton.getInstance(context).cancelRequestInQueue(RFRDOCTRHIT);
        CustomRequest request = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    boolean success = response.getBoolean("success");
                    String msg = response.getString("message");

                    if (success){
                        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                        referDoctorItemArrayList.get(pstn).setRefered("1");
                        notifyDataSetChanged();
                        ReferDoctorActivity.prgLoading.setVisibility(View.GONE);

                    }else {
                        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                        ReferDoctorActivity.prgLoading.setVisibility(View.GONE);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(context,context.getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                    ReferDoctorActivity.prgLoading.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse.data!=null){
                    try {

                        String jsonString = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        JSONObject errObject = new JSONObject(jsonString);

                        String message = errObject.getString("message");

                        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Toast.makeText(context,context.getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(context,context.getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                ReferDoctorActivity.prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(RFRDOCTRHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

}