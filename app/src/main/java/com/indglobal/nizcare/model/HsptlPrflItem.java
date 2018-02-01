package com.indglobal.nizcare.model;

/**
 * Created by readyassist on 2/1/18.
 */

public class HsptlPrflItem {

    private String id,name,adress;

    public HsptlPrflItem(String id, String name, String adress) {
        this.id = id;
        this.name = name;
        this.adress = adress;
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

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }
}
