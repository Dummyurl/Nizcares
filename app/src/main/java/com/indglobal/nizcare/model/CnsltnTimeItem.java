package com.indglobal.nizcare.model;

import java.util.ArrayList;

/**
 * Created by readyassist on 1/24/18.
 */

public class CnsltnTimeItem {

    private boolean opened;
    private String name,timecount;
    private ArrayList<CnsltnItem> cnsltnItems;

    public CnsltnTimeItem(boolean opened, String name, String timecount, ArrayList<CnsltnItem> cnsltnItems) {
        this.opened = opened;
        this.name = name;
        this.timecount = timecount;
        this.cnsltnItems = cnsltnItems;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimecount() {
        return timecount;
    }

    public void setTimecount(String timecount) {
        this.timecount = timecount;
    }

    public ArrayList<CnsltnItem> getCnsltnItems() {
        return cnsltnItems;
    }

    public void setCnsltnItems(ArrayList<CnsltnItem> cnsltnItems) {
        this.cnsltnItems = cnsltnItems;
    }
}
