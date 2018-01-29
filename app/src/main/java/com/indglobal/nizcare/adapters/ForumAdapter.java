package com.indglobal.nizcare.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.indglobal.nizcare.activities.EnquiryReplyActivity;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.commons.CustomRequest;
import com.indglobal.nizcare.commons.RippleView;
import com.indglobal.nizcare.commons.VolleySingleton;
import com.indglobal.nizcare.fragments.BaseHomeFragment;
import com.indglobal.nizcare.model.EnquiryItem;
import com.indglobal.nizcare.model.ForumItem;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by readyassist on 1/24/18.
 */

public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.MyViewHolder>{

    private Context context = null;

    public interface OnItemClickListener {
        void onItemClick(ForumItem forumItem);
    }

    private ArrayList<ForumItem> forumItemArrayList;
    private final ForumAdapter.OnItemClickListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName,tvCount,tvJoin;

        MyViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvCount = (TextView)itemView.findViewById(R.id.tvCount);
            tvJoin = (TextView)itemView.findViewById(R.id.tvJoin);
        }

        void bind(final ForumItem forumItem, final ForumAdapter.OnItemClickListener listener, final int position) {

            tvName.setText(Comman.capitalize(forumItem.getForum_name()));
            tvCount.setText(forumItem.getMembers()+" Doctors");

            final String status = forumItem.getJoin_status();
            if (status.equalsIgnoreCase("1")){
                tvJoin.setTextColor(context.getResources().getColor(R.color.white));
                tvJoin.setBackground(context.getResources().getDrawable(R.drawable.round_fill_green_half));
            }else {
                tvJoin.setTextColor(context.getResources().getColor(R.color.lightGreen));
                tvJoin.setBackground(context.getResources().getDrawable(R.drawable.round_stroke_green));
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //listener.onItemClick(enquiryItem);
                }
            });

            tvJoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (status.equalsIgnoreCase("1")){
                        Toast.makeText(context,context.getResources().getString(R.string.alreadyjoined),Toast.LENGTH_SHORT).show();
                    }else {
                        BaseHomeFragment.prgLoading.setVisibility(View.VISIBLE);
                        joinGroup(forumItem.getForum_id(),position);
                    }
                }
            });

        }
    }

    public ForumAdapter(Context context,ArrayList<ForumItem> forumItemArrayList, ForumAdapter.OnItemClickListener listener) {
        this.forumItemArrayList = forumItemArrayList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public ForumAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.forum_item, parent, false);

        return new ForumAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ForumAdapter.MyViewHolder holder, final int position) {
        holder.bind(forumItemArrayList.get(position), listener,position);
    }

    @Override
    public int getItemCount() {
        return forumItemArrayList.size();
    }

    private void joinGroup(final String forum_id,final int pstn) {

        String url = context.getResources().getString(R.string.joinGroupApi);
        String token = Comman.getPreferences(context,"token");
        url = url+"?token="+token;

        Map<String, String> params = new HashMap<>();
        params.put("forum_id",forum_id);

        String JOINGROUPHIT = "join_group_hit";
        VolleySingleton.getInstance(context).cancelRequestInQueue(JOINGROUPHIT);
        CustomRequest request = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    boolean success = response.getBoolean("success");
                    String msg = response.getString("message");

                    if (success){
                        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                        int members = Integer.parseInt(forumItemArrayList.get(pstn).getMembers());
                        forumItemArrayList.get(pstn).setMembers((members+1)+"");
                        forumItemArrayList.get(pstn).setJoin_status("1");
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
        request.setTag(JOINGROUPHIT);
        request.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

}