package com.indglobal.nizcare.model;

/**
 * Created by readyassist on 1/29/18.
 */

public class CountryItem {

    private String id,name;

    public CountryItem(String id, String name) {
        this.id = id;
        this.name = name;
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
