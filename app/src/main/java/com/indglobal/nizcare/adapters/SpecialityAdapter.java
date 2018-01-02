package com.indglobal.nizcare.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.model.CheckSpecialityItem;

import java.util.ArrayList;

/**
 * Created by readyassist on 12/26/17.
 */

public class SpecialityAdapter extends BaseAdapter {

    ViewHolder holder;
    Activity activity;
    public static ArrayList<CheckSpecialityItem> checkSpecialityItems;
    String ids;

    public SpecialityAdapter(Activity context,ArrayList<CheckSpecialityItem> checkSpecialityItems) {
        this.activity = context;
        SpecialityAdapter.checkSpecialityItems = checkSpecialityItems;

    }

    @Override
    public int getCount() {
        return checkSpecialityItems.size();
    }

    @Override
    public Object getItem(int position) {
        return checkSpecialityItems.get(position);
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
            v = inflater.inflate(R.layout.specility_slctn_item, parent, false);
            holder = new ViewHolder();

            CheckSpecialityItem checkSpecialityItem = new CheckSpecialityItem();

            holder.cbSpecility = (CheckBox)v.findViewById(R.id.cbSpecility);

            holder.cbSpecility.setOnCheckedChangeListener(myCheckedList);
            holder.cbSpecility.setTag(position);
            holder.cbSpecility.setChecked(checkSpecialityItem.isSelected());

            v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }


        holder.cbSpecility.setChecked(checkSpecialityItems.get(position).isSelected());
        holder.cbSpecility.setText(checkSpecialityItems.get(position).getName());

        return v;
    }

    static class ViewHolder {
        CheckBox cbSpecility;
    }

    public static ArrayList<CheckSpecialityItem> getBox(){

        ArrayList<CheckSpecialityItem> box = new ArrayList<>();
        for (CheckSpecialityItem p : checkSpecialityItems ){
            if(p.isSelected())
                box.add(p);
        }
        return box;
    }

    CompoundButton.OnCheckedChangeListener myCheckedList = new CompoundButton.OnCheckedChangeListener(){

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            getItemDetail((Integer) buttonView.getTag()).isSelected = isChecked;
        }
    };

    private CheckSpecialityItem getItemDetail(int position){
        return ((CheckSpecialityItem)getItem(position));
    }


}
