package com.nguyentrongtuan.businesstourmanagement.Models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyentrongtuan.businesstourmanagement.Controller.FirebaseCallbackClass;

import java.util.ArrayList;
import java.util.List;

public class Class {
    private long id;
    private String code;
    private String idTeacher;
    private String name;

    DatabaseReference databaseReference;

    public Class() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }


    public Class(long id, String code, String idTeacher, String name) {
        this.id = id;
        this.code = code;
        this.idTeacher = idTeacher;
        this.name = name;
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

    public String getIdTeacher() {
        return idTeacher;
    }

    public void setIdTeacher(String idTeacher) {
        this.idTeacher = idTeacher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void getAllClass(FirebaseCallbackClass callback){
        databaseReference.child("tbl_class").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Class> classList = new ArrayList<>();
                for(DataSnapshot node : snapshot.getChildren()){
                    Class aClass = node.getValue(Class.class);
                    if( aClass != null)
                        classList.add(aClass);
                }

                callback.onCallback(classList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Login", "Failed to load tours.", error.toException());
                callback.onCallback(new ArrayList<>()); // Return an empty list on error
            }
        });
    }

    public Class getAClass(List<Class> list, String idClass){
        for (Class aClass : list){
            if(aClass.getCode().equals(idClass)){
                return aClass;
            }
        }
        return null;
    }

}
