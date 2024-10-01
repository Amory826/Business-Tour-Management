package com.nguyentrongtuan.businesstourmanagement.View;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.nguyentrongtuan.businesstourmanagement.Controller.FirebaseCallbackTours;
import com.nguyentrongtuan.businesstourmanagement.Controller.setAdapterListTour;
import com.nguyentrongtuan.businesstourmanagement.Models.Tours;
import com.nguyentrongtuan.businesstourmanagement.R;
import java.util.ArrayList;
import java.util.List;

public class MenuAdminActivity extends AppCompatActivity {
    private ListView listTour;
    private setAdapterListTour adapter;
    private List<Tours> list = new ArrayList<>();
    ProgressBar loadListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_menu_admin);

        listTour = findViewById(R.id.listTour);
        loadListView = findViewById(R.id.loadListView);

        Tours tour = new Tours();
        tour.getAllListTour(new FirebaseCallbackTours() {
            @Override
            public void onCallback(List<Tours> fetchedList) {
                if (fetchedList != null && !fetchedList.isEmpty()) {
                    list.addAll(fetchedList);
                    adapter = new setAdapterListTour(R.layout.custom_listview, MenuAdminActivity.this, list);
                    listTour.setAdapter(adapter);
                    loadListView.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("Login", "No tours found.");
                }
            }
        });
    }
}
