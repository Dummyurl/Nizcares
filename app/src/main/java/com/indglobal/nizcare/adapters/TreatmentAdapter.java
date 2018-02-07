package com.indglobal.nizcare.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.model.ClinicItem;
import com.indglobal.nizcare.model.TreatmentItem;

import java.util.ArrayList;

/**
 * Created by readyassist on 2/5/18.
 */

public class TreatmentAdapter  extends BaseAdapter {

    private TreatmentAdapter.ViewHolder holder;
    private Activity activity;
    private ArrayList<TreatmentItem> treatmentItemArrayList;

    public TreatmentAdapter(Activity context, ArrayList<TreatmentItem> treatmentItemArrayList) {
        this.activity = context;
        this.treatmentItemArrayList = treatmentItemArrayList;

    }

    @Override
    public int getCount() {
        return treatmentItemArrayList.size();
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
            holder = new TreatmentAdapter.ViewHolder();

            holder.name = (TextView)v.findViewById(R.id.text1);

            v.setTag(holder);

        } else {
            holder = (TreatmentAdapter.ViewHolder) v.getTag();
        }

        String name = treatmentItemArrayList.get(position).getName();
        holder.name.setText(name);

        return v;
    }

    static class ViewHolder {
        TextView name;
    }

}
