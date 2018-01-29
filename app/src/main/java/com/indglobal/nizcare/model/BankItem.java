package com.indglobal.nizcare.model;

/**
 * Created by readyassist on 1/25/18.
 */

public class BankItem {

    private String bank_id,country,bank_name,account_holder_name,account_number,account_type,ifsc,bank_address,micr,deflt,pan_card_no;

    public BankItem(String bank_id, String country, String bank_name, String account_holder_name, String account_number, String account_type, String ifsc, String bank_address, String micr, String deflt, String pan_card_no) {
        this.bank_id = bank_id;
        this.country = country;
        this.bank_name = bank_name;
        this.account_holder_name = account_holder_name;
        this.account_number = account_number;
        this.account_type = account_type;
        this.ifsc = ifsc;
        this.bank_address = bank_address;
        this.micr = micr;
        this.deflt = deflt;
        this.pan_card_no = pan_card_no;
    }

    public String getBank_id() {
        return bank_id;
    }

    public void setBank_id(String bank_id) {
        this.bank_id = bank_id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getAccount_holder_name() {
        return account_holder_name;
    }

    public void setAccount_holder_name(String account_holder_name) {
        this.account_holder_name = account_holder_name;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getBank_address() {
        return bank_address;
    }

    public void setBank_address(String bank_address) {
        this.bank_address = bank_address;
    }

    public String getMicr() {
        return micr;
    }

    public void setMicr(String micr) {
        this.micr = micr;
    }

    public String getDeflt() {
        return deflt;
    }

    public void setDeflt(String deflt) {
        this.deflt = deflt;
    }

    public String getPan_card_no() {
        return pan_card_no;
    }

    public void setPan_card_no(String pan_card_no) {
        this.pan_card_no = pan_card_no;
    }
}
