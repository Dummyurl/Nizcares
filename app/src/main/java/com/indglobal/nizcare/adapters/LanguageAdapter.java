package com.indglobal.nizcare.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.model.CheckLanguageItem;
import com.indglobal.nizcare.model.CheckSpecialityItem;

import java.util.ArrayList;

/**
 * Created by readyassist on 1/3/18.
 */

public class LanguageAdapter extends BaseAdapter {

    LanguageAdapter.ViewHolder holder;
    Activity activity;
    public static ArrayList<CheckLanguageItem> checkLanguageItems;
    String ids;

    public LanguageAdapter(Activity context,ArrayList<CheckLanguageItem> checkLanguageItems) {
        this.activity = context;
        LanguageAdapter.checkLanguageItems = checkLanguageItems;

    }

    @Override
    public int getCount() {
        return checkLanguageItems.size();
    }

    @Override
    public Object getItem(int position) {
        return checkLanguageItems.get(position);
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
            holder = new LanguageAdapter.ViewHolder();

            CheckSpecialityItem checkSpecialityItem = new CheckSpecialityItem();

            holder.cbSpecility = (CheckBox)v.findViewById(R.id.cbSpecility);

            holder.cbSpecility.setOnCheckedChangeListener(myCheckedList);
            holder.cbSpecility.setTag(position);
            holder.cbSpecility.setChecked(checkSpecialityItem.isSelected());

            v.setTag(holder);

        } else {
            holder = (LanguageAdapter.ViewHolder) v.getTag();
        }


        holder.cbSpecility.setChecked(checkLanguageItems.get(position).isSelected());
        holder.cbSpecility.setText(checkLanguageItems.get(position).getName());

        return v;
    }

    static class ViewHolder {
        CheckBox cbSpecility;
    }

    public static ArrayList<CheckLanguageItem> getBox(){

        ArrayList<CheckLanguageItem> box = new ArrayList<>();
        for (CheckLanguageItem p : checkLanguageItems ){
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

    private CheckLanguageItem getItemDetail(int position){
        return ((CheckLanguageItem)getItem(position));
    }


}
