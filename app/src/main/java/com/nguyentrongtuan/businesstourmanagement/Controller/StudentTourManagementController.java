package com.nguyentrongtuan.businesstourmanagement.Controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.nguyentrongtuan.businesstourmanagement.Adapter.AdapterStudentManagement;
import com.nguyentrongtuan.businesstourmanagement.Adapter.RecyclerViewItemTouchHelper;
import com.nguyentrongtuan.businesstourmanagement.Interface.FirebaseCallbackStudent;
import com.nguyentrongtuan.businesstourmanagement.Interface.ItemTouchHelperListener;
import com.nguyentrongtuan.businesstourmanagement.Models.Students;
import com.nguyentrongtuan.businesstourmanagement.Models.Tours;
import com.nguyentrongtuan.businesstourmanagement.R;

import java.util.ArrayList;
import java.util.List;

public class StudentTourManagementController implements ItemTouchHelperListener {
    private final Context context;
    private AdapterStudentManagement adapter;
    private List<Students> list = new ArrayList<>();
    private RecyclerView recyclerView;
    int position;


    public StudentTourManagementController(Context context, List<Students> list, int position) {
        this.context = context;
        this.list = list;
        this.position = position;
    }

    public long getSizeList() {
        return list.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void getTourList(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;  // Assign the recyclerView here
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AdapterStudentManagement(list, R.layout.custom_students_teachers_management);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        deleteStudent();
    }

    private void deleteStudent() {
        if (recyclerView != null) {
            ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerViewItemTouchHelper(0, ItemTouchHelper.LEFT, this);
            new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
        } else {
            Log.e("RecyclerViewError", "RecyclerView is null, cannot attach ItemTouchHelper.");
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof AdapterStudentManagement.ViewHolder) {
            String nameStudent = list.get(viewHolder.getAdapterPosition()).getName();
            Students studentDelete = list.get(viewHolder.getAdapterPosition());
            int positionStudent = viewHolder.getAdapterPosition();

            adapter.removeItem(positionStudent);

            // Display the Snackbar with the viewHolder itemView as root
            Snackbar snackbar = Snackbar.make(viewHolder.itemView, nameStudent + " deleted", Snackbar.LENGTH_LONG);
            snackbar.setAction("Undo", view -> {
                adapter.undoItem(studentDelete, positionStudent);

                // Check if recyclerView is not null before scrolling
                if (recyclerView != null) {
                    recyclerView.smoothScrollToPosition(positionStudent);
                }
            });

            // Add callback to detect dismissal
            snackbar.addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                        new Tours().deleteStudentByTour(position, positionStudent);
                    }
                }
            });

            snackbar.show();
        }
    }
}
