package com.indglobal.nizcare.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.model.CityItem;
import com.indglobal.nizcare.model.ClinicItem;

import java.util.ArrayList;

/**
 * Created by readyassist on 1/3/18.
 */

public class ClinicAdapter extends BaseAdapter {

    private ClinicAdapter.ViewHolder holder;
    private Activity activity;
    private ArrayList<ClinicItem> clinicItemArrayList;

    public ClinicAdapter(Activity context, ArrayList<ClinicItem> clinicItemArrayList) {
        this.activity = context;
        this.clinicItemArrayList = clinicItemArrayList;

    }

    @Override
    public int getCount() {
        return clinicItemArrayList.size();
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
            holder = new ClinicAdapter.ViewHolder();

            holder.name = (TextView)v.findViewById(R.id.text1);

            v.setTag(holder);

        } else {
            holder = (ClinicAdapter.ViewHolder) v.getTag();
        }

        String name = clinicItemArrayList.get(position).getName();
        holder.name.setText(name);

        return v;
    }

    static class ViewHolder {
        TextView name;
    }

}
