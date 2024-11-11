package com.nguyentrongtuan.businesstourmanagement.Controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.nguyentrongtuan.businesstourmanagement.Adapter.AdapterBusinessTourManagement;
import com.nguyentrongtuan.businesstourmanagement.Adapter.RecyclerViewItemTouchHelper;
import com.nguyentrongtuan.businesstourmanagement.Interface.FirebaseCallbackStudent;
import com.nguyentrongtuan.businesstourmanagement.Interface.FirebaseCallbackTours;
import com.nguyentrongtuan.businesstourmanagement.Interface.ItemTouchHelperListener;
import com.nguyentrongtuan.businesstourmanagement.Models.Students;
import com.nguyentrongtuan.businesstourmanagement.Models.Tours;
import com.nguyentrongtuan.businesstourmanagement.R;

import java.util.ArrayList;
import java.util.List;

public class BusinessTourManagementController implements ItemTouchHelperListener {
    Context context;
    AdapterBusinessTourManagement adapter;
    final List<Tours> list = new ArrayList<>();
    RecyclerView recyclerView;

    public long getSizeList() {
        return list.size();
    }


    public BusinessTourManagementController(Context context) {
        this.context = context;
    }

    public BusinessTourManagementController(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView; // Initialize RecyclerView
    }

    public void getTourList(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        Tours tour = new Tours();
        tour.getAllListTour(new FirebaseCallbackTours() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onCallback(List<Tours> fetchedList) {
                if (fetchedList != null && !fetchedList.isEmpty()) {
                    list.clear();
                    list.addAll(fetchedList);
                    if (list.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        if (recyclerView.getVisibility() == View.GONE) {
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                        adapter = new AdapterBusinessTourManagement(list, R.layout.custom_layout_business_tour_management);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        deleteTour(recyclerView);
                        Log.d("Login", "Tours loaded: " + fetchedList.size());
                    }
                } else {
                    Log.d("Login", "No tours found.");
                }
            }
        });
    }

    private void deleteTour(RecyclerView recyclerView) {
        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerViewItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof AdapterBusinessTourManagement.ViewHolder) {
            String nameTour = list.get(viewHolder.getAdapterPosition()).getName();
            Tours tourDelete = list.get(viewHolder.getAdapterPosition());

            int position = viewHolder.getAdapterPosition();

            adapter.removeItem(position);

            // Display the Snackbar with the viewHolder itemView as root
            Snackbar snackbar = Snackbar.make(viewHolder.itemView, nameTour + " deleted", Snackbar.LENGTH_LONG);
            snackbar.setAction("Undo", view -> {
                adapter.undoItem(tourDelete, position);

                // Check if recyclerView is not null before scrolling
                if (recyclerView != null) {
                    recyclerView.smoothScrollToPosition(position);
                }
            });

            // Add callback to detect dismissal
            snackbar.addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                        new Tours().deleteTour(position);
                    }
                }
            });

            snackbar.show();


        }
    }

    public boolean checkCodeTour(String code){
        for(Tours tour : list){
            if(tour.getCode().contains(code))
                return true;
        }
        return false;
    }

    public void addItemAdapter(Tours tours){
        adapter.addItem(tours);
        new Tours().addTour(tours, list.size()-1);
    }
}
