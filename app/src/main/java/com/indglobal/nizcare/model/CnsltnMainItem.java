package com.indglobal.nizcare.model;

import java.util.ArrayList;

/**
 * Created by readyassist on 1/24/18.
 */

public class CnsltnMainItem {

    private String count;
    private ArrayList<CnsltnTimeItem> cnsltnTimeItems;

    public CnsltnMainItem(String count, ArrayList<CnsltnTimeItem> cnsltnTimeItems) {
        this.count = count;
        this.cnsltnTimeItems = cnsltnTimeItems;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public ArrayList<CnsltnTimeItem> getCnsltnTimeItems() {
        return cnsltnTimeItems;
    }

    public void setCnsltnTimeItems(ArrayList<CnsltnTimeItem> cnsltnTimeItems) {
        this.cnsltnTimeItems = cnsltnTimeItems;
    }
}
