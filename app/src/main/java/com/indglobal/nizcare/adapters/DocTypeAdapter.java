package com.indglobal.nizcare.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.model.CityItem;
import com.indglobal.nizcare.model.DoctypeItem;

import java.util.ArrayList;

/**
 * Created by readyassist on 12/29/17.
 */

public class DocTypeAdapter extends BaseAdapter {

    private DocTypeAdapter.ViewHolder holder;
    private Activity activity;
    private ArrayList<DoctypeItem> doctypeItemArrayList;

    public DocTypeAdapter(Activity context, ArrayList<DoctypeItem> doctypeItemArrayList) {
        this.activity = context;
        this.doctypeItemArrayList = doctypeItemArrayList;

    }

    @Override
    public int getCount() {
        return doctypeItemArrayList.size();
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
            holder = new DocTypeAdapter.ViewHolder();

            holder.name = (TextView)v.findViewById(R.id.text1);

            v.setTag(holder);

        } else {
            holder = (DocTypeAdapter.ViewHolder) v.getTag();
        }

        String name = doctypeItemArrayList.get(position).getName();
        holder.name.setText(name);

        return v;
    }

    static class ViewHolder {
        TextView name;
    }

}
