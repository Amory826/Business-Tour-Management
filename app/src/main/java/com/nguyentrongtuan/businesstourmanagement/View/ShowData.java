package com.nguyentrongtuan.businesstourmanagement.View;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.nguyentrongtuan.businesstourmanagement.Controller.CompanyWithSchoolController;
import com.nguyentrongtuan.businesstourmanagement.Controller.StudentManagementController;
import com.nguyentrongtuan.businesstourmanagement.Controller.TeacherManagementController;
import com.nguyentrongtuan.businesstourmanagement.Controller.TourManagementController;
import com.nguyentrongtuan.businesstourmanagement.R;

public class ShowData extends AppCompatActivity {

    RecyclerView recyclerTourManagement;
    EditText edtSearch;
    ImageButton  btnSearch;
    ProgressBar loadListView;
    TextView txtNoTour, txtContent;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_show_data);

        recyclerTourManagement = findViewById(R.id.listTourManagement);
        edtSearch = findViewById(R.id.edtSearch);
        btnSearch = findViewById(R.id.btnSearch);
        loadListView = findViewById(R.id.loadListView);
        txtNoTour = findViewById(R.id.txtNoTour);
        txtContent = findViewById(R.id.txtContent);
        loadListView.setVisibility(View.VISIBLE);


        String nameId = getIntent().getStringExtra("id");
        Log.d("Login", "id = " + nameId);
        showData(nameId);

    }



    private void showData(String nameId) {
        if ("btnTour".equals(nameId)) { // Use .equals to compare string content
            txtContent.setText("Các chuyến tham quan doanh nghiệp");
            TourManagementController tourManagementController;
            tourManagementController = new TourManagementController(this);

            tourManagementController.getTourList(recyclerTourManagement, loadListView, txtNoTour);

            btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nameTour = edtSearch.getText().toString();
                    tourManagementController.getTourListByName(recyclerTourManagement, nameTour, loadListView, txtNoTour);
                }
            });
        }else if("btnCompanyWithSchool".equals(nameId)){
            txtContent.setText("Các doanh nghiệp liên kết với nhà trường");
            CompanyWithSchoolController companyWithSchoolController;
            companyWithSchoolController = new CompanyWithSchoolController(this);

            companyWithSchoolController.getTourList(recyclerTourManagement, loadListView, txtNoTour);

            btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nameTour = edtSearch.getText().toString();
                    companyWithSchoolController.getTourListByName(recyclerTourManagement, nameTour, loadListView, txtNoTour);
                }
            });
        } else if ("btnStudentManagement".equals(nameId)) {
            txtContent.setText("Quản lý sinh viên");
            StudentManagementController studentManagementController;
            studentManagementController = new StudentManagementController(this, R.layout.custom_students_teachers_management_home);

            studentManagementController.getStudentList(recyclerTourManagement, loadListView, txtNoTour);

            btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nameStudent = edtSearch.getText().toString();
                    studentManagementController.getStudentListByName(recyclerTourManagement, nameStudent, loadListView, txtNoTour);
                }
            });
        } else if ("btnTeacherManagement".equals(nameId)) {
            txtContent.setText("Quản lý giảng viên");
            TeacherManagementController teacherManagementController;
            teacherManagementController = new TeacherManagementController(this, R.layout.custom_students_teachers_management_home);
            teacherManagementController.getTeacherList(recyclerTourManagement, loadListView, txtNoTour);
            btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nameTeacher = edtSearch.getText().toString();
                    teacherManagementController.getTeacherListByName(recyclerTourManagement, nameTeacher, loadListView, txtNoTour);
                }
            });
        }
    }

}

