package com.nguyentrongtuan.businesstourmanagement.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nguyentrongtuan.businesstourmanagement.Controller.FirebaseCallbackTours;
import com.nguyentrongtuan.businesstourmanagement.Controller.AdapterListTour;
import com.nguyentrongtuan.businesstourmanagement.Models.Tours;
import com.nguyentrongtuan.businesstourmanagement.R;
import com.nguyentrongtuan.businesstourmanagement.View.LoginActivity;
import com.nguyentrongtuan.businesstourmanagement.View.MenuAdminActivity;
import com.nguyentrongtuan.businesstourmanagement.View.MenuStudentActivity;
import com.nguyentrongtuan.businesstourmanagement.View.OrganizedTourActivity;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment implements View.OnClickListener{

    private ListView listTour;
    private AdapterListTour adapter;
    private List<Tours> list = new ArrayList<>();
    ProgressBar loadListView;
    Button btnTour, btnCompanyWithSchool, btnStudentManagement, btnTeacherManagement;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_home, container, false);

        listTour = view.findViewById(R.id.listTour);
        loadListView = view.findViewById(R.id.loadListView);
        btnTour = view.findViewById(R.id.btnTour);
        btnCompanyWithSchool = view.findViewById(R.id.btnCompanyWithSchool);
        btnStudentManagement = view.findViewById(R.id.btnStudentManagement);
        btnTeacherManagement = view.findViewById(R.id.btnTeacherManagement);

        Tours tour = new Tours();
        tour.getAllListTour(new FirebaseCallbackTours() {
            @Override
            public void onCallback(List<Tours> fetchedList) {
                if (fetchedList != null && !fetchedList.isEmpty()) {
                    list.addAll(fetchedList);
                    adapter = new AdapterListTour(R.layout.custom_listview, view.getContext(), list);
                    listTour.setAdapter(adapter);
                    loadListView.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("Login", "No tours found.");
                }
            }
        });

        btnTour.setOnClickListener(this);
        btnCompanyWithSchool.setOnClickListener(this);
        btnStudentManagement.setOnClickListener(this);
        btnTeacherManagement.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnTour){
            Intent iBtnTour = new Intent(getActivity(), OrganizedTourActivity.class);
            startActivity(iBtnTour);
        }
    }
}
