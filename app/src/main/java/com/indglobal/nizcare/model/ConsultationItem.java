package com.indglobal.nizcare.model;

/**
 * Created by readyassist on 1/4/18.
 */

public class ConsultationItem {

    private String consultant_id,name;

    public ConsultationItem(String consultant_id, String name) {
        this.consultant_id = consultant_id;
        this.name = name;
    }

    public String getConsultant_id() {
        return consultant_id;
    }

    public void setConsultant_id(String consultant_id) {
        this.consultant_id = consultant_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
