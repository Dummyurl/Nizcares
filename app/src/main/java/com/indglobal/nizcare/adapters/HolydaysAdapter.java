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
import com.indglobal.nizcare.activities.AddHolidayActivity;
import com.indglobal.nizcare.activities.MyHolidaysActivity;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.commons.VolleyJSONRequest;
import com.indglobal.nizcare.commons.VolleySingleton;
import com.indglobal.nizcare.model.HolidayItem;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by readyassist on 2/10/18.
 */

public class HolydaysAdapter  extends RecyclerView.Adapter<HolydaysAdapter.MyViewHolder>{

    private Context context = null;

    public interface OnItemClickListener {
        void onItemClick(HolidayItem holidayItem);
    }

    private ArrayList<HolidayItem> holidayItemArrayList;
    private final HolydaysAdapter.OnItemClickListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDay,tvDate,tvHsptlName,tvHolydayEdit,tvHolydayRemove;

        MyViewHolder(View itemView) {
            super(itemView);

            tvDay = (TextView) itemView.findViewById(R.id.tvDay);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvHsptlName = (TextView) itemView.findViewById(R.id.tvHsptlName);
            tvHolydayEdit = (TextView) itemView.findViewById(R.id.tvHolydayEdit);
            tvHolydayRemove = (TextView) itemView.findViewById(R.id.tvHolydayRemove);


        }

        void bind(final HolidayItem holidayItem, final HolydaysAdapter.OnItemClickListener listener, final int position) {

            tvDay.setText(holidayItem.getTotal_days());

            String fromdate = holidayItem.getFrom_date();
            String todate = holidayItem.getTo_date();

            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outDateFormat = new SimpleDateFormat("dd-MMM-yyyy");

            try {
                Date from = inputDateFormat.parse(fromdate);
                Date to = outDateFormat.parse(todate);

                tvDate.setText(from+" - "+to);

            }catch (Exception e){
                e.printStackTrace();
            }


            tvHsptlName.setText(Comman.capitalize(holidayItem.getHospital_name()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //listener.onItemClick(instantItem);
                }
            });

            tvHolydayRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Comman.isConnectionAvailable(context)){
                        Toast.makeText(context,context.getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                    }else {
                        MyHolidaysActivity.prgLoading.setVisibility(View.VISIBLE);
                        removeAcount(holidayItem.getId(),position);
                    }
                }
            });

            tvHolydayEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ii = new Intent(context,AddHolidayActivity.class);
                    ii.putExtra("isEdit","1");
                    ii.putExtra("id",holidayItem.getId());
                    ii.putExtra("doctor_id",holidayItem.getDoctor_id());
                    ii.putExtra("hospital_name",holidayItem.getHospital_name());
                    ii.putExtra("from_date",holidayItem.getFrom_date());
                    ii.putExtra("to_date",holidayItem.getTo_date());
                    ii.putExtra("total_days",holidayItem.getTotal_days());
                    context.startActivity(ii);
                }
            });

        }
    }

    public HolydaysAdapter(Context context,ArrayList<HolidayItem> holidayItemArrayList, HolydaysAdapter.OnItemClickListener listener) {
        this.holidayItemArrayList = holidayItemArrayList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public HolydaysAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.holiday_item, parent, false);

        return new HolydaysAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final HolydaysAdapter.MyViewHolder holder, final int position) {
        holder.bind(holidayItemArrayList.get(position), listener,position);
    }

    @Override
    public int getItemCount() {
        return holidayItemArrayList.size();
    }

    private void removeAcount(final String id,final int pstn) {

        String url = context.getResources().getString(R.string.removeHolydayApi);
        String token = Comman.getPreferences(context,"token");
        url = url+"?token="+token+"&id="+id;

        String DLTHLDYHIT = "dlt_hldy_hit";
        VolleySingleton.getInstance(context).cancelRequestInQueue(DLTHLDYHIT);
        VolleyJSONRequest request = new VolleyJSONRequest(Request.Method.GET, url,null, null,new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                try {

                    JSONObject response = new JSONObject(result);

                    boolean success = response.getBoolean("success");
                    String msg = response.getString("message");

                    if (success){
                        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                        holidayItemArrayList.remove(pstn);
                        notifyItemRemoved(pstn);

                    }else {
                        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(context,context.getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                MyHolidaysActivity.prgLoading.setVisibility(View.GONE);

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

                MyHolidaysActivity.prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(DLTHLDYHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }


}