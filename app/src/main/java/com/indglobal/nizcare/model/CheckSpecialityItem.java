package com.indglobal.nizcare.model;

/**
 * Created by readyassist on 12/26/17.
 */

public class CheckSpecialityItem {

    public boolean isSelected;
    private String id,name;

    public CheckSpecialityItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public CheckSpecialityItem() {

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
