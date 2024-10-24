package com.nguyentrongtuan.businesstourmanagement.Controller;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nguyentrongtuan.businesstourmanagement.Adapter.AdapterStudentManagement;
import com.nguyentrongtuan.businesstourmanagement.Adapter.AdapterTeacherManagement;
import com.nguyentrongtuan.businesstourmanagement.Models.Teachers;
import com.nguyentrongtuan.businesstourmanagement.R;

import java.util.ArrayList;
import java.util.List;

public class TeacherManagementController {
    Context context;
    AdapterTeacherManagement adapter;

    public TeacherManagementController(Context context){
        this.context = context;
    }

    public void getTeacherList(RecyclerView recyclerView, ProgressBar loadListView, TextView txtNoTeacher){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        final List<Teachers> list = new ArrayList<>();

        Teachers Teacher = new Teachers();

        Teacher.getAllListTeacher(new FirebaseCallbackTeacher() {
            @Override
            public void onCallback(List<Teachers> listtemp) {
                if (listtemp != null && !listtemp.isEmpty()) {
                    list.addAll(listtemp);
                    if(list.size() == 0){
                        txtNoTeacher.setVisibility(View.VISIBLE);
                        loadListView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        txtNoTeacher.setText("Không có giảng viên nào.");
                    }else {
                        if(recyclerView.getVisibility() == View.GONE){
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                        txtNoTeacher.setVisibility(View.GONE);
                        adapter = new AdapterTeacherManagement(list, R.layout.custom_students_teachers_management);
                        recyclerView.setAdapter(adapter);
                        loadListView.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Log.d("Login", "No students found.");
                }
            }
        });

    }

    public void getTeacherListByName(RecyclerView recyclerView, String nameTeacher, ProgressBar loadListView, TextView txtNoTour){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        txtNoTour.setVisibility(View.GONE);
        final List<Teachers> list = new ArrayList<>();
        List<Teachers> TeacherListByName = new ArrayList<>();

        Teachers Teacher = new Teachers();
        Teacher.getAllListTeacher(new FirebaseCallbackTeacher() {
            @Override
            public void onCallback(List<Teachers> fetchedList) {
                if (fetchedList != null && !fetchedList.isEmpty()) {
                    list.addAll(fetchedList);
                    for (Teachers t : list) {
                        if (t.getName().toLowerCase().contains(nameTeacher.toLowerCase())) {
                            TeacherListByName.add(t);
                        }
                    }
                    if(TeacherListByName.size() == 0){
                        txtNoTour.setVisibility(View.VISIBLE);
                        loadListView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        txtNoTour.setText("Không có sinh viên nào có tên chứa " + nameTeacher + ".");

                    }else {
                        if(recyclerView.getVisibility() == View.GONE)
                            recyclerView.setVisibility(View.VISIBLE);
                        adapter = new AdapterTeacherManagement(TeacherListByName, R.layout.custom_students_teachers_management);
                        recyclerView.setAdapter(adapter);
                        loadListView.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Log.d("Login", "No tours found.");
                }
            }
        });
    }
}
