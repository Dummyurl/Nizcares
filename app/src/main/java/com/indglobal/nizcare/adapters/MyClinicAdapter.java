package com.indglobal.nizcare.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.indglobal.nizcare.R;
import com.indglobal.nizcare.activities.AddBankActivity;
import com.indglobal.nizcare.activities.MyClinicsActivity;
import com.indglobal.nizcare.activities.MyClinicsActivity;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.commons.VolleyJSONRequest;
import com.indglobal.nizcare.commons.VolleySingleton;
import com.indglobal.nizcare.model.BankItem;
import com.indglobal.nizcare.model.ClinicItem;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by readyassist on 2/3/18.
 */

public class MyClinicAdapter extends RecyclerView.Adapter<MyClinicAdapter.MyViewHolder>{

    private Context context = null;

    public interface OnItemClickListener {
        void onItemClick(ClinicItem clinicItem);
    }

    private ArrayList<ClinicItem> clinicItemArrayList;
    private final MyClinicAdapter.OnItemClickListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName,tvFee,tvAdrs,tvEdit,tvRemove;

        MyViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvFee = (TextView) itemView.findViewById(R.id.tvFee);
            tvAdrs = (TextView) itemView.findViewById(R.id.tvAdrs);
            tvEdit = (TextView) itemView.findViewById(R.id.tvEdit);
            tvRemove = (TextView) itemView.findViewById(R.id.tvRemove);


        }

        void bind(final ClinicItem clinicItem, final MyClinicAdapter.OnItemClickListener listener, final int position) {

            tvName.setText(Comman.capitalize(clinicItem.getName()));
            tvFee.setText("Consultation Fee : "+clinicItem.getConsultation_fees()+"/-");
            tvAdrs.setText(Comman.capitalize(clinicItem.getAddress()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //listener.onItemClick(instantItem);
                }
            });

            tvRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Comman.isConnectionAvailable(context)){
                        Toast.makeText(context,context.getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                    }else {
                        MyClinicsActivity.prgLoading.setVisibility(View.VISIBLE);
                        removeClinic(clinicItem.getHospital_id(),position);
                    }
                }
            });

            tvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent ii = new Intent(context,AddBankActivity.class);
//                    ii.putExtra("isEdit","1");
//                    ii.putExtra("bank_id",bankItem.getBank_id());
//                    ii.putExtra("country",bankItem.getCountry());
//                    ii.putExtra("bank_name",bankItem.getBank_name());
//                    ii.putExtra("account_holder_name",bankItem.getAccount_holder_name());
//                    ii.putExtra("account_number",bankItem.getAccount_number());
//                    ii.putExtra("account_type",bankItem.getAccount_type());
//                    ii.putExtra("ifsc",bankItem.getIfsc());
//                    ii.putExtra("bank_address",bankItem.getBank_address());
//                    ii.putExtra("micr",bankItem.getMicr());
//                    ii.putExtra("pan_card_no",bankItem.getPan_card_no());
//                    ii.putExtra("default_pay",bankItem.getDeflt());
//                    context.startActivity(ii);
                }
            });

        }
    }

    public MyClinicAdapter(Context context,ArrayList<ClinicItem> clinicItemArrayList, MyClinicAdapter.OnItemClickListener listener) {
        this.clinicItemArrayList = clinicItemArrayList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public MyClinicAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_clinic_item, parent, false);

        return new MyClinicAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyClinicAdapter.MyViewHolder holder, final int position) {
        holder.bind(clinicItemArrayList.get(position), listener,position);
    }

    @Override
    public int getItemCount() {
        return clinicItemArrayList.size();
    }

    private void removeClinic(final String hospital_id,final int pstn) {

        String url = context.getResources().getString(R.string.removeClinicApi);
        String token = Comman.getPreferences(context,"token");
        url = url+"?token="+token+"&hospital_id="+hospital_id;

        String DLTCLNCHIT = "dlt_clnct_hit";
        VolleySingleton.getInstance(context).cancelRequestInQueue(DLTCLNCHIT);
        VolleyJSONRequest request = new VolleyJSONRequest(Request.Method.GET, url,null, null,new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                try {

                    JSONObject response = new JSONObject(result);

                    boolean success = response.getBoolean("success");
                    String msg = response.getString("message");

                    if (success){
                        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                        clinicItemArrayList.remove(pstn);
                        notifyItemRemoved(pstn);

                    }else {
                        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(context,context.getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                MyClinicsActivity.prgLoading.setVisibility(View.GONE);

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

                MyClinicsActivity.prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(DLTCLNCHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }


}