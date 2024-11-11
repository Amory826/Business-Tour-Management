package com.nguyentrongtuan.businesstourmanagement.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.nguyentrongtuan.businesstourmanagement.Controller.StudentManagementController;
import com.nguyentrongtuan.businesstourmanagement.Controller.StudentTourManagementController;
import com.nguyentrongtuan.businesstourmanagement.Interface.FirebaseCallbackStudent;
import com.nguyentrongtuan.businesstourmanagement.Models.Students;
import com.nguyentrongtuan.businesstourmanagement.Models.Tours;
import com.nguyentrongtuan.businesstourmanagement.R;

import java.util.ArrayList;
import java.util.List;

public class ManagementStudentTour extends AppCompatActivity {
    RecyclerView listStudentTour;
    TextView txtNameTitle, txtNoTour;
    Button btnAddStudentTour;
    ProgressBar progressBar;

    StudentTourManagementController studentManagementController;

    List<Students> listStudent = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_management_student_tour);

        listStudentTour = findViewById(R.id.listStudentTour);
        btnAddStudentTour = findViewById(R.id.btnAddStudentTour);
        progressBar = findViewById(R.id.progressBar);
        txtNameTitle = findViewById(R.id.txtNameTitle);
        txtNoTour = findViewById(R.id.txtNoTour);


        int positionTour = 0;
        Bundle bundle = getIntent().getBundleExtra("putTour");
        if (bundle != null) {
            listStudent = (List<Students>) bundle.getSerializable("studentsList");
            Tours tour = (Tours) bundle.getSerializable("Tour");
            assert tour != null;
            txtNameTitle.setText("Quản lý sinh viên cho tour: " + tour.getName());
            positionTour = Math.toIntExact(tour.getId());
        }

        studentManagementController = new StudentTourManagementController(getApplicationContext(), listStudent, positionTour);

        studentManagementController.getTourList(listStudentTour);

    }
}
