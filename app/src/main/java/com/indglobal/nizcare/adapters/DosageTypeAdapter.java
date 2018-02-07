package com.indglobal.nizcare.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.model.ClinicItem;
import com.indglobal.nizcare.model.DosageTypeItem;

import java.util.ArrayList;

/**
 * Created by readyassist on 2/6/18.
 */

public class DosageTypeAdapter extends BaseAdapter {

    private DosageTypeAdapter.ViewHolder holder;
    private Activity activity;
    private ArrayList<DosageTypeItem> dosageTypeItemArrayList;

    public DosageTypeAdapter(Activity context, ArrayList<DosageTypeItem> dosageTypeItemArrayList) {
        this.activity = context;
        this.dosageTypeItemArrayList = dosageTypeItemArrayList;

    }

    @Override
    public int getCount() {
        return dosageTypeItemArrayList.size();
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
            holder = new DosageTypeAdapter.ViewHolder();

            holder.name = (TextView)v.findViewById(R.id.text1);

            v.setTag(holder);

        } else {
            holder = (DosageTypeAdapter.ViewHolder) v.getTag();
        }

        String name = dosageTypeItemArrayList.get(position).getName();
        holder.name.setText(Comman.capitalize(name));

        return v;
    }

    static class ViewHolder {
        TextView name;
    }

}
