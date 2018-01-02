package com.indglobal.nizcare.model;

import java.util.ArrayList;

/**
 * Created by readyassist on 12/30/17.
 */

public class ApointTimeItem {

    private boolean opened;
    private String name,timecount;
    private ArrayList<ApointItem> apointItems;

    public ApointTimeItem(boolean opened, String name, String timecount, ArrayList<ApointItem> apointItems) {
        this.opened = opened;
        this.name = name;
        this.timecount = timecount;
        this.apointItems = apointItems;
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

    public ArrayList<ApointItem> getApointItems() {
        return apointItems;
    }

    public void setApointItems(ArrayList<ApointItem> apointItems) {
        this.apointItems = apointItems;
    }
}
