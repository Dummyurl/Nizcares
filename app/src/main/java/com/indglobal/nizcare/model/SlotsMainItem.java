package com.indglobal.nizcare.model;

import java.util.ArrayList;

/**
 * Created by readyassist on 1/5/18.
 */

public class SlotsMainItem {

    private boolean opened;
    private String name;
    private ArrayList<SlotItem> slotItems;

    public SlotsMainItem(boolean opened, String name, ArrayList<SlotItem> slotItems) {
        this.opened = opened;
        this.name = name;
        this.slotItems = slotItems;
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

    public ArrayList<SlotItem> getSlotItems() {
        return slotItems;
    }

    public void setSlotItems(ArrayList<SlotItem> slotItems) {
        this.slotItems = slotItems;
    }
}
