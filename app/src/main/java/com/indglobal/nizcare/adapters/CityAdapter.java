package com.indglobal.nizcare.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.model.CityItem;

import java.util.ArrayList;

/**
 * Created by readyassist on 12/27/17.
 */

public class CityAdapter extends BaseAdapter {

    private ViewHolder holder;
    private Activity activity;
    private ArrayList<CityItem> cityItemArrayList;

    public CityAdapter(Activity context, ArrayList<CityItem> cityItemArrayList) {
        this.activity = context;
        this.cityItemArrayList = cityItemArrayList;

    }

    @Override
    public int getCount() {
        return cityItemArrayList.size();
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
            holder = new ViewHolder();

            holder.name = (TextView)v.findViewById(R.id.text1);

            v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }

        String name = cityItemArrayList.get(position).getName();
        holder.name.setText(name);

        return v;
    }

    static class ViewHolder {
        TextView name;
    }

}
