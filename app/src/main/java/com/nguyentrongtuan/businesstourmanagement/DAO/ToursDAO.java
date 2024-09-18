package com.nguyentrongtuan.businesstourmanagement.DAO;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyentrongtuan.businesstourmanagement.Models.Tours;

import java.util.ArrayList;
import java.util.List;

public class ToursDAO {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    List<Tours> list;

    public ToursDAO() {
        list = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("tbl_tour");
    }

    public List<Tours> getAllTours() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int dem = 0;
                Tours tour = new Tours();
                Iterable<DataSnapshot> node = snapshot.getChildren();
                for (DataSnapshot child : node) {
                    tour = child.getValue(Tours.class);
                    list.add(tour);
                    dem++;
                }
                Log.d("Login", dem  + ": Tour");
                Log.d("Login", list.size() + "List size");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Lỗi truy xuất dữ liệu", error.toException());
            }
        });
        return list;
    }
}