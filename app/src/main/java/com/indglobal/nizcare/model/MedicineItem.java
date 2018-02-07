package com.indglobal.nizcare.model;

/**
 * Created by readyassist on 2/7/18.
 */

public class MedicineItem {

    private String medicine_id,medicine_name,duration,dosageType,dosageId,breakfast,bdosage,lunch,ldosage,dinner,ddosage;
    boolean isMorning,isAfternoon,isNight;

    public MedicineItem(String medicine_id, String medicine_name, String duration, String dosageType, String dosageId, String breakfast, String bdosage, String lunch, String ldosage, String dinner, String ddosage, boolean isMorning, boolean isAfternoon, boolean isNight) {
        this.medicine_id = medicine_id;
        this.medicine_name = medicine_name;
        this.duration = duration;
        this.dosageType = dosageType;
        this.dosageId = dosageId;
        this.breakfast = breakfast;
        this.bdosage = bdosage;
        this.lunch = lunch;
        this.ldosage = ldosage;
        this.dinner = dinner;
        this.ddosage = ddosage;
        this.isMorning = isMorning;
        this.isAfternoon = isAfternoon;
        this.isNight = isNight;
    }

    public String getMedicine_id() {
        return medicine_id;
    }

    public void setMedicine_id(String medicine_id) {
        this.medicine_id = medicine_id;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDosageType() {
        return dosageType;
    }

    public void setDosageType(String dosageType) {
        this.dosageType = dosageType;
    }

    public String getDosageId() {
        return dosageId;
    }

    public void setDosageId(String dosageId) {
        this.dosageId = dosageId;
    }

    public String getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(String breakfast) {
        this.breakfast = breakfast;
    }

    public String getBdosage() {
        return bdosage;
    }

    public void setBdosage(String bdosage) {
        this.bdosage = bdosage;
    }

    public String getLunch() {
        return lunch;
    }

    public void setLunch(String lunch) {
        this.lunch = lunch;
    }

    public String getLdosage() {
        return ldosage;
    }

    public void setLdosage(String ldosage) {
        this.ldosage = ldosage;
    }

    public String getDinner() {
        return dinner;
    }

    public void setDinner(String dinner) {
        this.dinner = dinner;
    }

    public String getDdosage() {
        return ddosage;
    }

    public void setDdosage(String ddosage) {
        this.ddosage = ddosage;
    }

    public boolean isMorning() {
        return isMorning;
    }

    public void setMorning(boolean morning) {
        isMorning = morning;
    }

    public boolean isAfternoon() {
        return isAfternoon;
    }

    public void setAfternoon(boolean afternoon) {
        isAfternoon = afternoon;
    }

    public boolean isNight() {
        return isNight;
    }

    public void setNight(boolean night) {
        isNight = night;
    }
}
