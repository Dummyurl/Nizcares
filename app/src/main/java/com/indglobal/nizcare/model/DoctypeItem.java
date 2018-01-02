package com.indglobal.nizcare.model;

/**
 * Created by readyassist on 12/29/17.
 */

public class DoctypeItem {

    private String id,name;

    public DoctypeItem(String id, String name) {
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
