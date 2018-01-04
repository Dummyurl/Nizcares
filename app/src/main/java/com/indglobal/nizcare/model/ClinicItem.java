package com.indglobal.nizcare.model;

/**
 * Created by readyassist on 1/3/18.
 */

public class ClinicItem {

    private String hospital_id,logo,logo_thumb,name,address,consultation_fees;

    public ClinicItem(String hospital_id, String logo, String logo_thumb, String name, String address, String consultation_fees) {
        this.hospital_id = hospital_id;
        this.logo = logo;
        this.logo_thumb = logo_thumb;
        this.name = name;
        this.address = address;
        this.consultation_fees = consultation_fees;
    }

    public String getHospital_id() {
        return hospital_id;
    }

    public void setHospital_id(String hospital_id) {
        this.hospital_id = hospital_id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLogo_thumb() {
        return logo_thumb;
    }

    public void setLogo_thumb(String logo_thumb) {
        this.logo_thumb = logo_thumb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getConsultation_fees() {
        return consultation_fees;
    }

    public void setConsultation_fees(String consultation_fees) {
        this.consultation_fees = consultation_fees;
    }
}
