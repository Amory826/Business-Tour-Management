package com.nguyentrongtuan.businesstourmanagement.DAO;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyentrongtuan.businesstourmanagement.Models.Teachers;
import com.nguyentrongtuan.businesstourmanagement.Models.Tours;

import java.util.ArrayList;
import java.util.List;

public class TeacherDAO {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    List<Teachers> list;

    public TeacherDAO() {
        list = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("tbl_teacher");
    }

    public List<Teachers> getAllTeacher() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int dem = 0;
                Teachers teacher = new Teachers();
                Iterable<DataSnapshot> node = snapshot.getChildren();
                for (DataSnapshot child : node) {
                    teacher = child.getValue(Teachers.class);
                    list.add(teacher);
                    dem++;
                }
                Log.d("Login", dem  + ": Teacher");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Lỗi truy xuất dữ liệu", error.toException());
            }
        });
        return list;
    }

    public Teachers getATeacher(String idTeacher, List<Teachers> list){
        Teachers teacher = new Teachers();
        int dem = 0;
        for(Teachers t : list){
            if(t.getCode() == idTeacher){
                teacher = t;
                dem++;
                break;
            }
        }
        if(dem == 0 )
            return null;
        return teacher;
    }
}