package com.indglobal.nizcare.model;

import java.util.ArrayList;

/**
 * Created by readyassist on 12/30/17.
 */

public class ApointMainItem {

    private String count;
    private ArrayList<ApointTimeItem> apointTimeItems;

    public ApointMainItem(String count, ArrayList<ApointTimeItem> apointTimeItems) {
        this.count = count;
        this.apointTimeItems = apointTimeItems;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public ArrayList<ApointTimeItem> getApointTimeItems() {
        return apointTimeItems;
    }

    public void setApointTimeItems(ArrayList<ApointTimeItem> apointTimeItems) {
        this.apointTimeItems = apointTimeItems;
    }
}
