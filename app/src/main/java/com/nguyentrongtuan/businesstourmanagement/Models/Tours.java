package com.nguyentrongtuan.businesstourmanagement.Models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyentrongtuan.businesstourmanagement.Interface.FirebaseCallbackStudent;
import com.nguyentrongtuan.businesstourmanagement.Interface.FirebaseCallbackTours;

import java.util.ArrayList;
import java.util.List;

public class Tours {
    private long id;
    private String code;
    private long available;
    private String idTeacher;
    private String idCompany;
    private String description;
    private String name;
    private String startDate;
    private List<Students> studentsList ;

    DatabaseReference databaseReference;


    public Tours() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public Tours(long id, String code, long available, String idTeacher, String idCompany,
                 String description, String name, String startDate, List<Students> studentsList) {
        this.id = id;
        this.code = code;
        this.available = available;
        this.idTeacher = idTeacher;
        this.idCompany = idCompany;
        this.description = description;
        this.name = name;
        this.startDate = startDate;
        this.studentsList = studentsList;
    }

    public long getAvailable() {
        return available;
    }

    public void setAvailable(long available) {
        this.available = available;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(String idCompany) {
        this.idCompany = idCompany;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void getStudentsList(int index, final FirebaseCallbackStudent callback) {
        databaseReference.child("tbl_tour").child(index + "").child("studentList")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Students> students = new ArrayList<>();
                        for (DataSnapshot exp : snapshot.getChildren()) {
                            Students stu = exp.getValue(Students.class);
                            if (stu != null) {
                                students.add(stu);
                            }
                        }
                        callback.onCallback(students);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("FirebaseError", "Failed to load students.", error.toException());
                        callback.onCallback(new ArrayList<>()); // Trả về danh sách rỗng nếu có lỗi
                    }
                });
    }


    public void setStudentsList(List<Students> studentsList) {
        this.studentsList = studentsList;
    }

    public void getAllListTour(final FirebaseCallbackTours callback){
        databaseReference.child("tbl_tour").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Tours> tourList = new ArrayList<>();
                for (DataSnapshot exp : snapshot.getChildren()){
                    Tours tour = exp.getValue(Tours.class);
                    if(tour != null){
                        tourList.add(tour);
                    }
                }
                callback.onCallback(tourList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to load tours.", error.toException());
                callback.onCallback(new ArrayList<>()); // Return an empty list on error
            }
        });
    }

    public void addTour(Tours tour, int position){
        databaseReference.child("tbl_tour").child(position + "").setValue(tour);
    }
    public void deleteTour(int position){
            databaseReference.child("tbl_tour").child(position + "").removeValue();
        }

}
