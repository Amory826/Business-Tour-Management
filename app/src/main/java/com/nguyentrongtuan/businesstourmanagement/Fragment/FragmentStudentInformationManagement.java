package com.nguyentrongtuan.businesstourmanagement.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.nguyentrongtuan.businesstourmanagement.Controller.StudentManagementController;
import com.nguyentrongtuan.businesstourmanagement.R;

public class FragmentStudentInformationManagement extends Fragment {

    RecyclerView recyclerView;
    TextView txtNameTitle, txtNoTour;
    Button btnAddStudent;
    ProgressBar progressBar;

    StudentManagementController studentManagementController;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_student_information_management, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        btnAddStudent = view.findViewById(R.id.btnAddStudent);
        txtNoTour = view.findViewById(R.id.txtNoTour);
        txtNameTitle = view.findViewById(R.id.txtNameTitle);
        recyclerView = view.findViewById(R.id.recyclerView);

        studentManagementController = new StudentManagementController(getContext(), R.layout.layout_custom_fragmment_student_teacher_management);

        studentManagementController.getStudentList(recyclerView, progressBar, txtNoTour);

        return view;
    }
}
