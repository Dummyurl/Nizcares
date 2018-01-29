package com.indglobal.nizcare.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.model.CityItem;
import com.indglobal.nizcare.model.CountryItem;

import java.util.ArrayList;

/**
 * Created by readyassist on 1/29/18.
 */

public class CountryAdapter  extends BaseAdapter {

    private CountryAdapter.ViewHolder holder;
    private Activity activity;
    private ArrayList<CountryItem> countryItemArrayList;

    public CountryAdapter(Activity context, ArrayList<CountryItem> countryItemArrayList) {
        this.activity = context;
        this.countryItemArrayList = countryItemArrayList;

    }

    @Override
    public int getCount() {
        return countryItemArrayList.size();
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
            holder = new CountryAdapter.ViewHolder();

            holder.name = (TextView)v.findViewById(R.id.text1);

            v.setTag(holder);

        } else {
            holder = (CountryAdapter.ViewHolder) v.getTag();
        }

        String name = countryItemArrayList.get(position).getName();
        holder.name.setText(name);

        return v;
    }

    static class ViewHolder {
        TextView name;
    }

}
