package com.nguyentrongtuan.businesstourmanagement.DAO;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyentrongtuan.businesstourmanagement.Models.Companies;
import com.nguyentrongtuan.businesstourmanagement.Models.Teachers;

import java.util.ArrayList;
import java.util.List;

public class CompanyDAO {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    List<Companies> list;

    public CompanyDAO() {
        list = new ArrayList<>();
        // Initialize Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("tbl_company");
    }

    public List<Companies> getAllCompany() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int dem = 0;
                Companies company = new Companies();
                Iterable<DataSnapshot> node = snapshot.getChildren();
                for (DataSnapshot child : node) {
                    company = child.getValue(Companies.class);
                    list.add(company);
                    dem++;
                }
                Log.d("Login", dem  + "Company");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Lỗi truy xuất dữ liệu", error.toException());
            }
        });
        return list;
    }

    public Companies getACompany(String idCompany, List<Companies> list){
        Companies company = new Companies();
        int dem = 0;
        for(Companies t : list){
            if(t.getCode() == idCompany){
                company = t;
                dem++;
                break;
            }
        }
        if(dem == 0 )
            return null;
        return company;
    }
}