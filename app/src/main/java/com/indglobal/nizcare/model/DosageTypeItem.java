package com.indglobal.nizcare.model;

/**
 * Created by readyassist on 2/6/18.
 */

public class DosageTypeItem {

    private String id,name;

    public DosageTypeItem(String id, String name) {
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
