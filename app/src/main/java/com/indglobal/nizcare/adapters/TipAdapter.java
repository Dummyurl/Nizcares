package com.indglobal.nizcare.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.commons.Comman;
import com.indglobal.nizcare.model.DosageTypeItem;
import com.indglobal.nizcare.model.TipItem;

import java.util.ArrayList;

/**
 * Created by readyassist on 2/7/18.
 */

public class TipAdapter extends BaseAdapter {

    private TipAdapter.ViewHolder holder;
    private Activity activity;
    private ArrayList<TipItem> tipItemArrayList;

    public TipAdapter(Activity context, ArrayList<TipItem> tipItemArrayList) {
        this.activity = context;
        this.tipItemArrayList = tipItemArrayList;

    }

    @Override
    public int getCount() {
        return tipItemArrayList.size();
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
            holder = new TipAdapter.ViewHolder();

            holder.name = (TextView)v.findViewById(R.id.text1);

            v.setTag(holder);

        } else {
            holder = (TipAdapter.ViewHolder) v.getTag();
        }

        String name = tipItemArrayList.get(position).getTip_name();
        holder.name.setText(Comman.capitalize(name));

        return v;
    }

    static class ViewHolder {
        TextView name;
    }

}
