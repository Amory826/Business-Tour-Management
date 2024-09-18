package com.nguyentrongtuan.businesstourmanagement.View;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nguyentrongtuan.businesstourmanagement.Controller.setAdapterListTour;
import com.nguyentrongtuan.businesstourmanagement.DAO.ToursDAO;
import com.nguyentrongtuan.businesstourmanagement.Models.Tours;
import com.nguyentrongtuan.businesstourmanagement.R;

import java.util.List;

public class MenuAdminActivity extends AppCompatActivity {
    ListView listTour;
    setAdapterListTour adapter;
    List<Tours> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_menu_admin);

        ToursDAO toursDAO = new ToursDAO();
        list = toursDAO.getAllTours();
        listTour = findViewById(R.id.listTour);

        adapter = new setAdapterListTour(R.layout.custom_listview, this, list);

        listTour.setAdapter(adapter);
    }
}