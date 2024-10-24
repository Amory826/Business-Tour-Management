package com.nguyentrongtuan.businesstourmanagement.Models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyentrongtuan.businesstourmanagement.Controller.FirebaseCallbackStudent;

import java.util.ArrayList;
import java.util.List;

public class Students {
    private long id;
    private long idAccount;
    private String code;
    private String birthDate;
    private String phoneNumber;
    private String description;
    private String email;
    private String name;
    private String idClass;
    private String address;

    DatabaseReference databaseReference;

    public Students() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public Students(long id, long idAccount, String code, String birthDate, String phoneNumber,
                    String description, String email, String name, String idClass, String address) {
        this.id = id;
        this.idAccount = idAccount;
        this.code = code;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.email = email;
        this.name = name;
        this.idClass = idClass;
        this.address = address;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(long idAccount) {
        this.idAccount = idAccount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdClass() {
        return idClass;
    }

    public void setIdClass(String idClass) {
        this.idClass = idClass;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void getAllStudent(FirebaseCallbackStudent callback){
        databaseReference.child("tbl_student").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Students> studentsList = new ArrayList<>();
                for(DataSnapshot node : snapshot.getChildren()){
                    Students student = node.getValue(Students.class);
                    if( student != null){
                        studentsList.add(student);
                    }
                }
                callback.onCallback(studentsList);
            }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("FirebaseError", "Failed to load tours.", error.toException());
                    callback.onCallback(new ArrayList<>()); // Return an empty list on error
                }
        });
    }

    public Students getAStudent(List<Students> list, String idStudent) {
        for (Students student : list) {
            if (student.getId() == Long.parseLong(idStudent)) {
                return student;
            }
        }
        return null;
    }
}
