package com.indglobal.nizcare.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.indglobal.nizcare.R;
import com.indglobal.nizcare.activities.LoginActivity;
import com.indglobal.nizcare.activities.ReScheduleActivity;
import com.indglobal.nizcare.activities.ReferDoctorActivity;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.commons.CustomRequest;
import com.indglobal.nizcare.commons.RippleView;
import com.indglobal.nizcare.commons.VolleySingleton;
import com.indglobal.nizcare.fragments.BaseHomeFragment;
import com.indglobal.nizcare.model.ApointItem;
import com.indglobal.nizcare.model.ApointMainItem;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by readyassist on 12/30/17.
 */

public class ApointmentAdapter extends RecyclerView.Adapter<ApointmentAdapter.MyViewHolder>{

    private Context context = null;

    public interface OnItemClickListener {
        void onItemClick(ApointItem apointItem);
    }

    private ArrayList<ApointItem> apointItemArrayList;

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTime,tvStatus,tvStatusDot,tvPName,tvHName;

        MyViewHolder(View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvStatus = (TextView)itemView.findViewById(R.id.tvStatus);
            tvStatusDot = (TextView)itemView.findViewById(R.id.tvStatusDot);
            tvPName = (TextView)itemView.findViewById(R.id.tvPName);
            tvHName = (TextView)itemView.findViewById(R.id.tvHName);
        }

        void bind(final int position, final ApointItem apointItem) {

            tvTime.setText(apointItem.getAppointment_time());
            tvPName.setText(Comman.capitalize(apointItem.getPatient_name()));
            tvHName.setText(Comman.capitalize(apointItem.getHospital_name()));

            final String status = apointItem.getStatus();
            if (status.equalsIgnoreCase("1")){
                tvStatus.setText("UPCOMING");
                tvStatus.setTextColor(context.getResources().getColor(R.color.yellow));
                tvStatusDot.setBackground(context.getResources().getDrawable(R.drawable.circle_fill_yellow));
            }else if (status.equalsIgnoreCase("2")){
                tvStatus.setText("DONE");
                tvStatus.setTextColor(context.getResources().getColor(R.color.lightGreen));
                tvStatusDot.setBackground(context.getResources().getDrawable(R.drawable.circle_fill_green));
            }else if (status.equalsIgnoreCase("3")||status.equalsIgnoreCase("4")){
                tvStatus.setText("CANCELLED");
                tvStatus.setTextColor(context.getResources().getColor(R.color.red));
                tvStatusDot.setBackground(context.getResources().getDrawable(R.drawable.circle_fill_red));
            }else {
                tvStatus.setText("NO SHOW");
                tvStatus.setTextColor(context.getResources().getColor(R.color.lightRed));
                tvStatusDot.setBackground(context.getResources().getDrawable(R.drawable.circle_fill_light_red));
            }


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!status.equalsIgnoreCase("3")&&!status.equalsIgnoreCase("4")){
                        openRescheduleDialog(apointItem,position);
                    }

                }
            });
        }
    }

    private void openRescheduleDialog(final ApointItem apointItem,final int pstn) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.apointment_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView tvPName = (TextView)dialog.findViewById(R.id.tvPName);
        TextView tvGndrAge = (TextView)dialog.findViewById(R.id.tvGndrAge);
        ImageView ivChat = (ImageView)dialog.findViewById(R.id.ivChat);
        ImageView ivCall = (ImageView)dialog.findViewById(R.id.ivCall);
        TextView tvHName = (TextView)dialog.findViewById(R.id.tvHName);
        TextView tvTime = (TextView)dialog.findViewById(R.id.tvTime);
        TextView tvDate = (TextView)dialog.findViewById(R.id.tvDate);
        RippleView rplReschdule = (RippleView) dialog.findViewById(R.id.rplReschdule);
        RippleView rplCancel = (RippleView)dialog.findViewById(R.id.rplCancel);

        if ((apointItem.getStatus()).equalsIgnoreCase("3")){
            rplCancel.setVisibility(View.GONE);
        }else if ((apointItem.getStatus()).equalsIgnoreCase("4")){
            rplCancel.setVisibility(View.GONE);
            rplReschdule.setVisibility(View.GONE);
        }

        if (apointItem.getStatus().equalsIgnoreCase("1")){
            rplReschdule.setVisibility(View.VISIBLE);
        }else {
            rplReschdule.setVisibility(View.GONE);
        }

        tvTime.setText(apointItem.getAppointment_time());
        tvPName.setText(Comman.capitalize(apointItem.getPatient_name()));
        tvHName.setText(Comman.capitalize(apointItem.getHospital_name()));
        tvGndrAge.setText(Comman.capitalize(apointItem.getGender())+" * "+apointItem.getAge()+" Years old");
        tvDate.setText(apointItem.getAppointment_date());

        rplCancel.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if (!Comman.isConnectionAvailable(context)){
                    Toast.makeText(context,context.getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                }else {
                    dialog.cancel();
                    BaseHomeFragment.prgLoading.setVisibility(View.VISIBLE);
                    cancelApointment(apointItem.getApointment_id(),apointItem.getPatient_name(),pstn);
                }

            }
        });

        rplReschdule.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                dialog.cancel();
                Intent ii = new Intent(context,ReScheduleActivity.class);
                ii.putExtra("name",apointItem.getPatient_name());
                ii.putExtra("image",apointItem.getProfile_image());
                ii.putExtra("age",apointItem.getAge());
                ii.putExtra("gender",apointItem.getGender());
                ii.putExtra("apoint_id",apointItem.getApointment_id());
                context.startActivity(ii);
            }
        });

        dialog.show();
    }


    public ApointmentAdapter(Context context,ArrayList<ApointItem> apointItemArrayList) {
        this.apointItemArrayList = apointItemArrayList;
        this.context = context;
    }

    @Override
    public ApointmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.apoints_sub_item, parent, false);

        return new ApointmentAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ApointmentAdapter.MyViewHolder holder, final int position) {
        holder.bind(position,apointItemArrayList.get(position));

    }

    @Override
    public int getItemCount() {
        return apointItemArrayList.size();
    }

    private void cancelApointment(final String appointment_id, final String patient_name,final int pstn) {

        String url = context.getResources().getString(R.string.cancelApointApi);
        String token = Comman.getPreferences(context,"token");
        url = url+"?token="+token;

        Map<String, String> params = new HashMap<>();
        params.put("appointment_id",appointment_id);

        String CNCLAPNTHIT = "cncl_apnt_hit";
        VolleySingleton.getInstance(context).cancelRequestInQueue(CNCLAPNTHIT);
        CustomRequest request = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    boolean success = response.getBoolean("success");
                    String msg = response.getString("message");

                    if (success){
                        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                        apointItemArrayList.get(pstn).setStatus("3");
                        notifyDataSetChanged();
                        openCancelledDialog(appointment_id,patient_name,pstn);

                    }else {
                        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(context,context.getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                }

                BaseHomeFragment.prgLoading.setVisibility(View.GONE);

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

                BaseHomeFragment.prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(CNCLAPNTHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    private void openCancelledDialog(final String appointment_id, String patient_name,final int pstn) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.canceled_apoint_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView tvCancledMsg = (TextView)dialog.findViewById(R.id.tvCancledMsg);
        RippleView rplRefranthrdoc = (RippleView) dialog.findViewById(R.id.rplRefranthrdoc);
        RippleView rplUndoCncl = (RippleView)dialog.findViewById(R.id.rplUndoCncl);

        tvCancledMsg.setText("Appointment with "+Comman.capitalize(patient_name)+" was successfully cancelled. You may want to refer another doctor or undo cancellation.");

        rplUndoCncl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Comman.isConnectionAvailable(context)){
                    Toast.makeText(context,context.getResources().getString(R.string.noInternet),Toast.LENGTH_SHORT).show();
                }else {
                    dialog.cancel();
                    BaseHomeFragment.prgLoading.setVisibility(View.VISIBLE);
                    undoCanceledApointment(appointment_id,pstn);
                }

            }
        });

        rplRefranthrdoc.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                dialog.cancel();
                Intent ii = new Intent(context,ReferDoctorActivity.class);
                ii.putExtra("appointment_id",appointment_id);
                context.startActivity(ii);
            }
        });

        dialog.show();
    }

    private void undoCanceledApointment(final String appointment_id,final int pstn) {

        String url = context.getResources().getString(R.string.undoCanceledApointApi);
        String token = Comman.getPreferences(context,"token");
        url = url+"?token="+token;

        Map<String, String> params = new HashMap<>();
        params.put("appointment_id",appointment_id);

        String UNDOCNCLAPNTHIT = "undo_cncl_apnt_hit";
        VolleySingleton.getInstance(context).cancelRequestInQueue(UNDOCNCLAPNTHIT);
        CustomRequest request = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    boolean success = response.getBoolean("success");
                    String msg = response.getString("message");

                    if (success){
                        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                        apointItemArrayList.get(pstn).setStatus("1");
                        notifyDataSetChanged();
                        BaseHomeFragment.prgLoading.setVisibility(View.GONE);

                    }else {
                        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                        BaseHomeFragment.prgLoading.setVisibility(View.GONE);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(context,context.getResources().getString(R.string.somethingwrong),Toast.LENGTH_SHORT).show();
                    BaseHomeFragment.prgLoading.setVisibility(View.GONE);
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

                BaseHomeFragment.prgLoading.setVisibility(View.GONE);
            }
        });
        request.setTag(UNDOCNCLAPNTHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

}