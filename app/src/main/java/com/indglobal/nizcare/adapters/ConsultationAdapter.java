package com.indglobal.nizcare.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.model.ClinicItem;
import com.indglobal.nizcare.model.ConsultationItem;

import java.util.ArrayList;

/**
 * Created by readyassist on 1/4/18.
 */

public class ConsultationAdapter extends BaseAdapter {

    private ConsultationAdapter.ViewHolder holder;
    private Activity activity;
    private ArrayList<ConsultationItem> consultationItemArrayList;

    public ConsultationAdapter(Activity context, ArrayList<ConsultationItem> consultationItemArrayList) {
        this.activity = context;
        this.consultationItemArrayList = consultationItemArrayList;

    }

    @Override
    public int getCount() {
        return consultationItemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            v = inflater.inflate(R.layout.spinner_item, parent, false);
            holder = new ConsultationAdapter.ViewHolder();

            holder.name = (TextView)v.findViewById(R.id.text1);

            v.setTag(holder);

        } else {
            holder = (ConsultationAdapter.ViewHolder) v.getTag();
        }

        String name = consultationItemArrayList.get(position).getName();
        holder.name.setText(name);

        return v;
    }

    static class ViewHolder {
        TextView name;
    }

}
