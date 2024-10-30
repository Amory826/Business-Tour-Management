package com.nguyentrongtuan.businesstourmanagement.Controller;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nguyentrongtuan.businesstourmanagement.Adapter.AdapterCompanyWithSchool;
import com.nguyentrongtuan.businesstourmanagement.Models.Companies;
import com.nguyentrongtuan.businesstourmanagement.R;

import java.util.ArrayList;
import java.util.List;

public class CompanyWithSchoolController {
    Context context;
    AdapterCompanyWithSchool adapter;

    public CompanyWithSchoolController(Context context) {
        this.context = context;
    }

    public void getTourList(RecyclerView recyclerView, ProgressBar loadListView, TextView txtNoTour) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        final List<Companies> list = new ArrayList<>();

        Companies company = new Companies();
        company.getAllListCompany(new FirebaseCallbackCompany() {
            @Override
            public void onCallback(List<Companies> fetchedList) {
                if (fetchedList != null && !fetchedList.isEmpty()) {
                    list.addAll(fetchedList);
                    if (list.size() == 0) {
                        txtNoTour.setVisibility(View.VISIBLE);
                        txtNoTour.setText("Không có công ty nào.");
                        loadListView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        if (recyclerView.getVisibility() == View.GONE) {
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                        txtNoTour.setVisibility(View.GONE);
                        adapter = new AdapterCompanyWithSchool(list, R.layout.custom_information_company);
                        recyclerView.setAdapter(adapter);
                        loadListView.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                        Log.d("Login", "Tours loaded: " + fetchedList.size());
                    }
                } else {
                    Log.d("Login", "No tours found.");
                }
            }
        });
    }

    public void getTourListByName(RecyclerView recyclerView, String nameTour, ProgressBar loadListView, TextView txtNoTour) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        txtNoTour.setVisibility(View.GONE);
        final List<Companies> list = new ArrayList<>();
        List<Companies> companyListByName = new ArrayList<>();

        Companies company = new Companies();
        company.getAllListCompany(new FirebaseCallbackCompany() {
            @Override
            public void onCallback(List<Companies> fetchedList) {
                if (fetchedList != null && !fetchedList.isEmpty()) {
                    list.addAll(fetchedList);
                    for (Companies t : list) {
                        if (t.getName().toLowerCase().contains(nameTour.toLowerCase())) {
                            companyListByName.add(t);
                        }
                    }
                    if (companyListByName.size() == 0) {
                        txtNoTour.setVisibility(View.VISIBLE);
                        loadListView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        txtNoTour.setText("Không có công ty nào có tên chứa " + nameTour + ".");

                    } else {
                        if (recyclerView.getVisibility() == View.GONE)
                            recyclerView.setVisibility(View.VISIBLE);
                        adapter = new AdapterCompanyWithSchool(companyListByName, R.layout.custom_information_company);
                        recyclerView.setAdapter(adapter);
                        loadListView.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                        Log.d("Login", "Tours loaded: " + fetchedList.size());
                    }
                } else {
                    Log.d("Login", "No tours found.");
                }
            }
        });
    }
}

