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
import com.indglobal.nizcare.activities.BankDtlActivity;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.commons.VolleyJSONRequest;
import com.indglobal.nizcare.commons.VolleySingleton;
import com.indglobal.nizcare.model.BankItem;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by readyassist on 1/25/18.
 */

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.MyViewHolder>{

    private Context context = null;

    public interface OnItemClickListener {
        void onItemClick(BankItem bankItem);
    }

    private ArrayList<BankItem> bankItemArrayList;
    private final BankAdapter.OnItemClickListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvBankName,tvAcntNmbr,tvBankDflt,tvBankAdrs,tvIfsc,tvBankEdit,tvBankRemove;

        MyViewHolder(View itemView) {
            super(itemView);

            tvBankName = (TextView) itemView.findViewById(R.id.tvBankName);
            tvAcntNmbr = (TextView) itemView.findViewById(R.id.tvAcntNmbr);
            tvBankDflt = (TextView) itemView.findViewById(R.id.tvBankDflt);
            tvBankAdrs = (TextView) itemView.findViewById(R.id.tvBankAdrs);
            tvIfsc = (TextView) itemView.findViewById(R.id.tvIfsc);
            tvBankEdit = (TextView) itemView.findViewById(R.id.tvBankEdit);
            tvBankRemove = (TextView) itemView.findViewById(R.id.tvBankRemove);


        }

        void bind(final BankItem bankItem, final BankAdapter.OnItemClickListener listener, final int position) {

            tvBankName.setText(Comman.capitalize(bankItem.getBank_name()));
            tvAcntNmbr.setText(Comman.capitalize(bankItem.getAccount_type()+" : "+bankItem.getAccount_number()));
            tvBankAdrs.setText(Comman.capitalize(bankItem.getBank_address()));
            tvIfsc.setText("IFSC Code : "+bankItem.getIfsc());

            String isDefault = bankItem.getDeflt();
            if (isDefault.equalsIgnoreCase("1")){
                tvBankDflt.setVisibility(View.VISIBLE);
            }else {
                tvBankDflt.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //listener.onItemClick(instantItem);
                }
            });

            tvBankRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Comman.isConnectionAvailable(context)){
                        Toast.makeText(context,context.getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                    }else {
                        BankDtlActivity.prgLoading.setVisibility(View.VISIBLE);
                        removeAcount(bankItem.getBank_id(),position);
                    }
                }
            });

            tvBankEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ii = new Intent(context,AddBankActivity.class);
                    ii.putExtra("isEdit","1");
                    ii.putExtra("bank_id",bankItem.getBank_id());
                    ii.putExtra("country",bankItem.getCountry());
                    ii.putExtra("bank_name",bankItem.getBank_name());
                    ii.putExtra("account_holder_name",bankItem.getAccount_holder_name());
                    ii.putExtra("account_number",bankItem.getAccount_number());
                    ii.putExtra("account_type",bankItem.getAccount_type());
                    ii.putExtra("ifsc",bankItem.getIfsc());
                    ii.putExtra("bank_address",bankItem.getBank_address());
                    ii.putExtra("micr",bankItem.getMicr());
                    ii.putExtra("pan_card_no",bankItem.getPan_card_no());
                    ii.putExtra("default_pay",bankItem.getDeflt());
                    context.startActivity(ii);
                }
            });

        }
    }

    public BankAdapter(Context context,ArrayList<BankItem> bankItemArrayList, BankAdapter.OnItemClickListener listener) {
        this.bankItemArrayList = bankItemArrayList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public BankAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bank_acnt_item, parent, false);

        return new BankAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final BankAdapter.MyViewHolder holder, final int position) {
        holder.bind(bankItemArrayList.get(position), listener,position);
    }

    @Override
    public int getItemCount() {
        return bankItemArrayList.size();
    }

    private void removeAcount(final String bank_id,final int pstn) {

        String url = context.getResources().getString(R.string.removeAcuntApi);
        String token = Comman.getPreferences(context,"token");
        url = url+"?token="+token+"&bank_id="+bank_id;

        String DLTACNTHIT = "dlt_acnt_hit";
        VolleySingleton.getInstance(context).cancelRequestInQueue(DLTACNTHIT);
        VolleyJSONRequest request = new VolleyJSONRequest(Request.Method.GET, url,null, null,new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                try {

                    JSONObject response = new JSONObject(result);

                    boolean success = response.getBoolean("success");
                    String msg = response.getString("message");

                    if (success){
                        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                        bankItemArrayList.remove(pstn);
                        notifyItemRemoved(pstn);

                    }else {
                        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(context,context.getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                BankDtlActivity.prgLoading.setVisibility(View.GONE);

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

                BankDtlActivity.prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(DLTACNTHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }


}