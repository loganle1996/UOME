package com.activity.loganle.uome.other;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

/**
 * Created by LoganLe on 11/2/16.
 */

public class Contact{
    //Constants for references
    public static final String ID_CONTACT = "ID_CONTACT";
    public static final String NAME = "CONTACT_NAME";
    public static final String ADDRESS = "CONTACT_ADDRESS";
    public static final String PHONE = "CONTACT_PHONE";
    public static final String EMAIL = "CONTACT_EMAIL";
    public static final String OWED_MONEY = "CONTACT_OWED_MONEY";
    public static final String PROFILEPIC = "PROFILE_PIC";
    public static final String DEADLINEDATE = "DEADLINE_DATE";
    //attributes
    private long idContact;
    private String name;
    private String address;
    private String phone;
    private String email;
    private double owedMoney;
    private String imagePath;
    private String deadlineDate;

    //  Setter & Getter
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getOwedMoney() {
        return owedMoney;
    }

    public void setOwedMoney(double owedMoney) {
        this.owedMoney = owedMoney;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setPersonalpic(String imagePath) {
        this.imagePath = imagePath;
    }

    public long getIdContact() {
        return idContact;
    }

    public void setIdContact(long idContact) {
        this.idContact = idContact;
    }

    public String getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(String deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    //Constructors
    public Contact(Bundle b){
        if(b != null){
            this.idContact = b.getLong(ID_CONTACT);
            this.name = b.getString(NAME);
            this.email = b.getString(EMAIL);
            this.address = b.getString(ADDRESS);
            this.phone = b.getString(PHONE);
            this.owedMoney = b.getDouble(OWED_MONEY);
            this.imagePath = b.getString(PROFILEPIC);
            this.deadlineDate = b.getString(DEADLINEDATE);
        }
    }
    public Contact(){

    }

    public Contact( String name, String address, String phone, String email) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    public Contact( String name, String address, String phone, String email,double owedMoney,String deadlineDate) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.owedMoney = owedMoney;
        this.deadlineDate = deadlineDate;
    }

    //Package data to a bundle object ( dont delete this method)
    public Bundle toBundle(){
        Bundle bundle = new Bundle();
        // put data to bundle
        bundle.putLong(ID_CONTACT,this.getIdContact());
        bundle.putString(NAME,this.getName());
        bundle.putString(ADDRESS,this.getAddress());
        bundle.putString(PHONE,this.getPhone());
        bundle.putString(EMAIL,this.getEmail());
        bundle.putDouble(OWED_MONEY,this.getOwedMoney());
        bundle.putString(PROFILEPIC,this.getImagePath());
        bundle.putString(DEADLINEDATE,this.getDeadlineDate());
        return bundle;
    }

}
