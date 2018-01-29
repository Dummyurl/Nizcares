package com.indglobal.nizcare.model;

/**
 * Created by readyassist on 1/24/18.
 */

public class CnsltnItem {

    private String consultation_id,patient_id,name,status,consult_type,type,profile_pic,profile_pic_thumb,consultation_time;

    public CnsltnItem(String consultation_id, String patient_id, String name, String status, String consult_type, String type, String profile_pic, String profile_pic_thumb,String consultation_time) {
        this.consultation_id = consultation_id;
        this.patient_id = patient_id;
        this.name = name;
        this.status = status;
        this.consult_type = consult_type;
        this.type = type;
        this.profile_pic = profile_pic;
        this.profile_pic_thumb = profile_pic_thumb;
        this.consultation_time = consultation_time;
    }

    public String getConsultation_id() {
        return consultation_id;
    }

    public void setConsultation_id(String consultation_id) {
        this.consultation_id = consultation_id;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConsult_type() {
        return consult_type;
    }

    public void setConsult_type(String consult_type) {
        this.consult_type = consult_type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getProfile_pic_thumb() {
        return profile_pic_thumb;
    }

    public void setProfile_pic_thumb(String profile_pic_thumb) {
        this.profile_pic_thumb = profile_pic_thumb;
    }

    public String getConsultation_time() {
        return consultation_time;
    }

    public void setConsultation_time(String consultation_time) {
        this.consultation_time = consultation_time;
    }
}
