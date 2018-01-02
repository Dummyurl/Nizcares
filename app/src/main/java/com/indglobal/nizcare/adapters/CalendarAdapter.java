package com.indglobal.nizcare.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.indglobal.nizcare.R;
import com.indglobal.nizcare.fragments.BaseHomeFragment;
import com.indglobal.nizcare.model.ApointMainItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by readyassist on 12/30/17.
 */

public class CalendarAdapter  extends BaseAdapter {

    private Context mContext;
    private Calendar month;
    private GregorianCalendar pmonth,pmonthmaxset,selectedDate;

    private ViewHolder holder;
    private int firstDay,maxWeeknumber,maxP,calMaxP,mnthlength;
    private String itemvalue, curentDateString;
    private DateFormat df;

    private HashMap<String,ApointMainItem> apointMainItemHashMap;
    public static List<String> dayString;
    public static ArrayList<String> apoints;
    private View previousView;

    public CalendarAdapter(Context c, Calendar monthCalendar) {
        dayString = new ArrayList<String>();
        apoints = new ArrayList<>();
        month = monthCalendar;
        selectedDate = (GregorianCalendar) monthCalendar.clone();
        mContext = c;
        month.set(GregorianCalendar.DAY_OF_MONTH, 1);
        this.apointMainItemHashMap = new HashMap<>();
        df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        curentDateString = df.format(selectedDate.getTime());
        refreshDays();
    }

    public void setItems(HashMap<String,ApointMainItem> apointMainItemHashMap) {
        this.apointMainItemHashMap = apointMainItemHashMap;
    }

    public int getCount() {
        return dayString.size();
    }

    public Object getItem(int position) {
        return dayString.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        TextView tvDate,tvApntsCnts;
        RelativeLayout rlDate;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.calendr_item, parent, false);
            holder = new ViewHolder();

            holder.tvDate = (TextView) v.findViewById(R.id.tvDate);
            holder.tvApntsCnts = (TextView) v.findViewById(R.id.tvApntsCnts);
            holder.rlDate = (RelativeLayout) v.findViewById(R.id.rlDate);

            holder.rlDate.setBackgroundColor(mContext.getResources().getColor(R.color.white));

            v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
            holder.rlDate.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }

        String[] separatedTime = dayString.get(position).split("-");

        String gridvalue = separatedTime[2].replaceFirst("^0*", "");

        if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {
            holder.tvDate.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.rlDate.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            holder.tvDate.setClickable(false);
            holder.tvDate.setFocusable(false);

        } else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
            holder.tvDate.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.rlDate.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            holder.tvDate.setClickable(false);
            holder.tvDate.setFocusable(false);

        } else {
            if (dayString.get(position).equals(curentDateString)) {
                holder.rlDate.setBackground(mContext.getResources().getDrawable(R.drawable.round_stroke_green));
                holder.tvDate.setTextColor(mContext.getResources().getColor(R.color.lightGreen));
                BaseHomeFragment.slctd = v;
            }else {
                holder.rlDate.setBackground(mContext.getResources().getDrawable(R.drawable.round_stroke_white));
                holder.tvDate.setTextColor(mContext.getResources().getColor(R.color.lightBlack));
            }
        }

        holder.tvDate.setText(gridvalue);

        String date = dayString.get(position);

        if (date.length() == 1) {
            date = "0" + date;
        }

        String monthStr = "" + (month.get(GregorianCalendar.MONTH) + 1);
        if (monthStr.length() == 1) {
            monthStr = "0" + monthStr;
        }

        if (date.length() > 0 && apointMainItemHashMap != null && apointMainItemHashMap.containsKey(date)) {
            if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {
                holder.tvApntsCnts.setVisibility(View.GONE);
                apoints.set(position,"0");
            } else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
                holder.tvApntsCnts.setVisibility(View.GONE);
                apoints.set(position, "0");
            } else {
                holder.tvApntsCnts.setVisibility(View.VISIBLE);

                String tempDate= dayString.get(position);
                holder.tvApntsCnts.setText(apointMainItemHashMap.get(tempDate).getCount()+"");

                apoints.set(position, String.valueOf(apointMainItemHashMap.get(tempDate).getCount()));
            }
        } else {
            holder.tvApntsCnts.setVisibility(View.GONE);
            apoints.set(position,"0");
        }
        return v;
    }


    public void refreshDays() {

        dayString.clear();
        pmonth = (GregorianCalendar) month.clone();

        firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);

        maxWeeknumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);

        mnthlength = maxWeeknumber * 7;

        calMaxP = maxP - (firstDay - 1);

        pmonthmaxset = (GregorianCalendar) pmonth.clone();

        pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);

        for (int n = 0; n < mnthlength; n++) {
            itemvalue = df.format(pmonthmaxset.getTime());
            pmonthmaxset.add(GregorianCalendar.DATE, 1);
            dayString.add(itemvalue);
            apoints.add(itemvalue);
        }
    }
}
