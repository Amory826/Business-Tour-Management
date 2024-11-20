package com.nguyentrongtuan.businesstourmanagement.Models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyentrongtuan.businesstourmanagement.Interface.FirebaseCallbackTeacher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Teachers implements Serializable {
    private long id;
    private long idAccount;
    private String code;
    private String birthDate;
    private String phoneNumber;
    private String addressImage;
    private String email;
    private String name;
    private String address;

    private transient DatabaseReference databaseReference;

    public Teachers() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public Teachers(long id, long idAccount, String code, String birthDate, String phoneNumber,
                    String addressImage, String email, String name, String address) {
        this.id = id;
        this.idAccount = idAccount;
        this.code = code;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.addressImage = addressImage;
        this.email = email;
        this.name = name;
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

    public String getAddressImage() {
        return addressImage;
    }

    public void setAddressImage(String addressImage) {
        this.addressImage = addressImage;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void getAllListTeacher(final FirebaseCallbackTeacher callback) {
        databaseReference.child("tbl_teacher").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Teachers> teachersList = new ArrayList<>();
                for (DataSnapshot node : snapshot.getChildren()) {
                    Teachers teacher = node.getValue(Teachers.class);
                    if (teacher != null)
                        teachersList.add(teacher);
                }

                callback.onCallback(teachersList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to load tours.", error.toException());
                callback.onCallback(new ArrayList<>()); // Return an empty list on error
            }
        });
    }


    public Teachers getATeacher(List<Teachers> list, String idTeacher) {
        for (Teachers teacher : list) {
            if (teacher.getCode().equals(idTeacher)) {
                return teacher;
            }
        }
        return null;
    }

    @NonNull
    @Override
    public String toString() {
        return getName();
    }

    public void addTeacher(Teachers s, int position){
        databaseReference.child("tbl_teacher").child(position + "").setValue(s);
    }

    public void deleteTeacher(int position){
        databaseReference.child("tbl_teacher").child(position + "").removeValue();
    }
}
