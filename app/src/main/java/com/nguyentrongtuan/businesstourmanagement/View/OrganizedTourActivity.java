package com.nguyentrongtuan.businesstourmanagement.View;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.nguyentrongtuan.businesstourmanagement.Controller.TourManagementController;
import com.nguyentrongtuan.businesstourmanagement.R;

public class OrganizedTourActivity extends AppCompatActivity {

    RecyclerView recyclerTourManagement;
    TourManagementController tourManagementController;
    EditText edtSearch;
    ImageButton  btnSearch;
    ProgressBar loadListView;
    TextView txtNoTour;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tour_management);

        recyclerTourManagement = findViewById(R.id.listTourManagement);
        edtSearch = findViewById(R.id.edtSearch);
        btnSearch = findViewById(R.id.btnSearch);
        loadListView = findViewById(R.id.loadListView);
        txtNoTour = findViewById(R.id.txtNoTour);

        tourManagementController = new TourManagementController(this);

        tourManagementController.getTourList(recyclerTourManagement, loadListView, txtNoTour);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameTour = edtSearch.getText().toString();
                tourManagementController.getTourListByName(recyclerTourManagement, nameTour, loadListView, txtNoTour);
            }
        });
    }
}
