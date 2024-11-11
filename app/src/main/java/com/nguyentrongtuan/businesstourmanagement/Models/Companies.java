package com.nguyentrongtuan.businesstourmanagement.Models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyentrongtuan.businesstourmanagement.Interface.FirebaseCallbackCompany;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Companies implements Serializable {
    private long id;
    private String code;
    private String name;
    private String address;
    private String phoneNumber;
    private String description;
    private String email;

    private transient DatabaseReference databaseReference;

    public Companies() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public Companies(long id, String code, String name, String address, String phoneNumber, String description, String email) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void getAllListCompany(final FirebaseCallbackCompany callback){
        databaseReference.child("tbl_company").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Companies> companiesList = new ArrayList<>();
                for(DataSnapshot node : snapshot.getChildren()){
                    Companies company = node.getValue(Companies.class);
                    if( company != null)
                        companiesList.add(company);
                }

                callback.onCallback(companiesList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to load tours.", error.toException());
                callback.onCallback(new ArrayList<>()); // Return an empty list on error
            }
        });
    }


    public Companies getACompany(List<Companies> list, String idCompany){
        for (Companies company : list){
            if(company.getCode().equals(idCompany)){
                Log.d("Login", company.getName() );
                return company;
            }
        }
        return null;
    }

    @NonNull
    @Override
    public String toString() {
        return getName();
    }
}
