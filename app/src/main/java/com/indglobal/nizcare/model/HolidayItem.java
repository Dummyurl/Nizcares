package com.indglobal.nizcare.model;

/**
 * Created by readyassist on 2/10/18.
 */

public class HolidayItem {

    private String id,doctor_id,hospital_name,from_date,to_date,total_days;

    public HolidayItem(String id, String doctor_id, String hospital_name, String from_date, String to_date, String total_days) {
        this.id = id;
        this.doctor_id = doctor_id;
        this.hospital_name = hospital_name;
        this.from_date = from_date;
        this.to_date = to_date;
        this.total_days = total_days;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getTotal_days() {
        return total_days;
    }

    public void setTotal_days(String total_days) {
        this.total_days = total_days;
    }
}
