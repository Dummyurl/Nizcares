package com.indglobal.nizcare.model;

/**
 * Created by readyassist on 1/3/18.
 */

public class CheckLanguageItem {

    public boolean isSelected;
    private String id,name;

    public CheckLanguageItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public CheckLanguageItem() {

    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
