package com.nguyentrongtuan.businesstourmanagement.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nguyentrongtuan.businesstourmanagement.Controller.FirebaseCallbackTours;
import com.nguyentrongtuan.businesstourmanagement.Controller.setAdapterListTour;
import com.nguyentrongtuan.businesstourmanagement.Models.Tours;
import com.nguyentrongtuan.businesstourmanagement.R;
import com.nguyentrongtuan.businesstourmanagement.View.MenuAdminActivity;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {

    private ListView listTour;
    private setAdapterListTour adapter;
    private List<Tours> list = new ArrayList<>();
    ProgressBar loadListView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_home, container, false);

        listTour = view.findViewById(R.id.listTour);
        loadListView = view.findViewById(R.id.loadListView);

        Tours tour = new Tours();
        tour.getAllListTour(new FirebaseCallbackTours() {
            @Override
            public void onCallback(List<Tours> fetchedList) {
                if (fetchedList != null && !fetchedList.isEmpty()) {
                    list.addAll(fetchedList);
                    adapter = new setAdapterListTour(R.layout.custom_listview, view.getContext(), list);
                    listTour.setAdapter(adapter);
                    loadListView.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("Login", "No tours found.");
                }
            }
        });
        return view;
    }

}
