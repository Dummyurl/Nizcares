package com.indglobal.nizcare.model;

/**
 * Created by readyassist on 12/30/17.
 */

public class ApointItem {

    private String apointment_id,patient_name,hospital_name,appointment_date,appointment_time,status;

    public ApointItem(String apointment_id, String patient_name, String hospital_name, String appointment_date, String appointment_time, String status) {
        this.apointment_id = apointment_id;
        this.patient_name = patient_name;
        this.hospital_name = hospital_name;
        this.appointment_date = appointment_date;
        this.appointment_time = appointment_time;
        this.status = status;
    }

    public String getApointment_id() {
        return apointment_id;
    }

    public void setApointment_id(String apointment_id) {
        this.apointment_id = apointment_id;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getAppointment_date() {
        return appointment_date;
    }

    public void setAppointment_date(String appointment_date) {
        this.appointment_date = appointment_date;
    }

    public String getAppointment_time() {
        return appointment_time;
    }

    public void setAppointment_time(String appointment_time) {
        this.appointment_time = appointment_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
