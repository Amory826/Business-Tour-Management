package com.nguyentrongtuan.businesstourmanagement.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.nguyentrongtuan.businesstourmanagement.Controller.BusinessTourManagementController;
import com.nguyentrongtuan.businesstourmanagement.Controller.FirebaseCallbackTeacher;
import com.nguyentrongtuan.businesstourmanagement.Models.Teachers;
import com.nguyentrongtuan.businesstourmanagement.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentBusinessTourManagement extends Fragment {

    private ProgressBar progressBar;
    private Button btnAddTour;
    private TextView txtNoTour;
    private RecyclerView recyclerTourManagement;
    private List<Teachers> listTeacher = new ArrayList<>();
    private BusinessTourManagementController businessTourManagementController;
    LinearLayout layoutForeground;

    ArrayAdapter<Teachers> adapter;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_business_tour_management, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        btnAddTour = view.findViewById(R.id.btnAddTour);
        txtNoTour = view.findViewById(R.id.txtNoTour);
        recyclerTourManagement = view.findViewById(R.id.listTourManagement);
        layoutForeground = view.findViewById(R.id.layoutForeground);


        businessTourManagementController = new BusinessTourManagementController(getContext());
        businessTourManagementController.getTourList(recyclerTourManagement);

        btnAddTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddTourDialog();
            }
        });

        return view;
    }

    private void showAddTourDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.layout_dialog_add_tour_with_business);

        TextInputLayout layoutCodeTour = dialog.findViewById(R.id.layoutCodeTour);
        TextInputEditText edtCodeTour = dialog.findViewById(R.id.edtCodeTour);
        AutoCompleteTextView edtNameTeacher = dialog.findViewById(R.id.edtNameTeacher);

        // Nạp danh sách giáo viên vào listTeacher
        Teachers teacher = new Teachers(); // Ensure this instantiation is valid
        teacher.getAllListTeacher(new FirebaseCallbackTeacher() {
            @Override
            public void onCallback(List<Teachers> list) {
                if (list != null && !list.isEmpty()) {
                    listTeacher.clear();
                    listTeacher.addAll(list);

                    // Instantiate the adapter
                    adapter = new ArrayAdapter<>(requireContext(), R.layout.layout_custom_sprinner, listTeacher);

                    edtNameTeacher.setAdapter(adapter);
                }
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

}
