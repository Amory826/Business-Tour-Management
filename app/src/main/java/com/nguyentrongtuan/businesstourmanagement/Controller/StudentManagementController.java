package com.nguyentrongtuan.businesstourmanagement.Controller;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nguyentrongtuan.businesstourmanagement.Adapter.AdapterStudentManagement;
import com.nguyentrongtuan.businesstourmanagement.Models.Students;
import com.nguyentrongtuan.businesstourmanagement.R;

import java.util.ArrayList;
import java.util.List;

public class StudentManagementController {
    Context context;
    AdapterStudentManagement adapter;

    public StudentManagementController(Context context){
        this.context = context;
    }

    public void getStudentList(RecyclerView recyclerView, ProgressBar loadListView, TextView txtNoStudent){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        final List<Students> list = new ArrayList<>();

        Students student = new Students();

        student.getAllStudent(new FirebaseCallbackStudent() {
            @Override
            public void onCallback(List<Students> listtemp) {
                if (listtemp != null && !listtemp.isEmpty()) {
                    list.addAll(listtemp);
                    if(list.size() == 0){
                        txtNoStudent.setVisibility(View.VISIBLE);
                        loadListView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        txtNoStudent.setText("Không có sinh viên nào.");
                    }else {
                        if(recyclerView.getVisibility() == View.GONE){
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                        txtNoStudent.setVisibility(View.GONE);
                        adapter = new AdapterStudentManagement(list, R.layout.custom_students_teachers_management);
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

    public void getStudentListByName(RecyclerView recyclerView, String nameStudent, ProgressBar loadListView, TextView txtNoTour){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        txtNoTour.setVisibility(View.GONE);
        final List<Students> list = new ArrayList<>();
        List<Students> studentListByName = new ArrayList<>();

        Students student = new Students();
        student.getAllStudent(new FirebaseCallbackStudent() {
            @Override
            public void onCallback(List<Students> fetchedList) {
                if (fetchedList != null && !fetchedList.isEmpty()) {
                    list.addAll(fetchedList);
                    for (Students t : list) {
                        if (t.getName().toLowerCase().contains(nameStudent.toLowerCase())) {
                            studentListByName.add(t);
                        }
                    }
                    if(studentListByName.size() == 0){
                        txtNoTour.setVisibility(View.VISIBLE);
                        loadListView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        txtNoTour.setText("Không có sinh viên nào có tên chứa " + nameStudent + ".");

                    }else {
                        if(recyclerView.getVisibility() == View.GONE)
                            recyclerView.setVisibility(View.VISIBLE);
                        adapter = new AdapterStudentManagement(studentListByName, R.layout.custom_students_teachers_management);
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
