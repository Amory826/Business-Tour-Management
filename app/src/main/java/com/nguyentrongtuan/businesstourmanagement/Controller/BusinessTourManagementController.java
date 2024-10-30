package com.nguyentrongtuan.businesstourmanagement.Controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.nguyentrongtuan.businesstourmanagement.Adapter.AdapterBusinessTourManagement;
import com.nguyentrongtuan.businesstourmanagement.Adapter.RecyclerViewItemTouchHelper;
import com.nguyentrongtuan.businesstourmanagement.Interface.ItemTouchHelperListener;
import com.nguyentrongtuan.businesstourmanagement.Models.Tours;
import com.nguyentrongtuan.businesstourmanagement.R;

import java.util.ArrayList;
import java.util.List;

public class BusinessTourManagementController implements ItemTouchHelperListener {
    Context context;
    AdapterBusinessTourManagement adapter;
    final List<Tours> list = new ArrayList<>();

    public BusinessTourManagementController(Context context) {
        this.context = context;
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

            // Display the Snackbar with the recyclerView as the root view
            Snackbar snackbar = Snackbar.make(viewHolder.itemView, nameTour + " deleted", Snackbar.LENGTH_LONG);
            snackbar.setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapter.undoItem(tourDelete, position);
                }
            });
            snackbar.show();
        }
    }
}
