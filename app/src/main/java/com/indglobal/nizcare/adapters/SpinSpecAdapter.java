package com.indglobal.nizcare.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.model.ClinicItem;
import com.indglobal.nizcare.model.SpecialityItem;

import java.util.ArrayList;

/**
 * Created by readyassist on 1/12/18.
 */

public class SpinSpecAdapter extends BaseAdapter {

    private SpinSpecAdapter.ViewHolder holder;
    private Activity activity;
    private ArrayList<SpecialityItem> specialityItemArrayList;

    public SpinSpecAdapter(Activity context, ArrayList<SpecialityItem> specialityItemArrayList) {
        this.activity = context;
        this.specialityItemArrayList = specialityItemArrayList;

    }

    @Override
    public int getCount() {
        return specialityItemArrayList.size();
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
            v = inflater.inflate(R.layout.spinner_item_with_padding, parent, false);
            holder = new SpinSpecAdapter.ViewHolder();

            holder.name = (TextView)v.findViewById(R.id.text1);

            v.setTag(holder);

        } else {
            holder = (SpinSpecAdapter.ViewHolder) v.getTag();
        }

        String name = specialityItemArrayList.get(position).getName();
        holder.name.setText(name);

        return v;
    }

    static class ViewHolder {
        TextView name;
    }

}
