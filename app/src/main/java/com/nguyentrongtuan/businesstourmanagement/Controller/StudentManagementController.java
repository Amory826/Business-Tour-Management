package com.nguyentrongtuan.businesstourmanagement.Controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class StudentManagementController implements ItemTouchHelperListener {
    private final Context context;
    private AdapterStudentManagement adapter;
    private List<Students> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private final int resource;
    int position;

    public StudentManagementController(Context context, int resource){
        this.context = context;
        this.resource = resource;
    }

    public StudentManagementController(Context context, List<Students> list, int position, int resource) {
        this.context = context;
        this.list = list;
        this.position = position;
        this.resource = resource;
    }


    public void getStudentList(RecyclerView recyclerView, ProgressBar loadListView, TextView txtNoStudent){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        this.recyclerView = recyclerView;

        Students student = new Students();

        student.getAllStudent(new FirebaseCallbackStudent() {
            @Override
            public void onCallback(List<Students> listtemp) {
                if (listtemp != null && !listtemp.isEmpty()) {
                    list.addAll(listtemp);
                    if(list.size() == 0){
                        txtNoStudent.setVisibility(View.VISIBLE);
                        loadListView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        txtNoStudent.setText("Không có sinh viên nào.");
                    }else {
                        if(recyclerView.getVisibility() == View.GONE){
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                        txtNoStudent.setVisibility(View.GONE);
                        adapter = new AdapterStudentManagement(list, resource);
                        recyclerView.setAdapter(adapter);
                        loadListView.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Log.d("Login", "No students found.");
                }
            }
        });

        deleteStudent();
    }


    public void getStudentListByName(RecyclerView recyclerView, String nameStudent, ProgressBar loadListView, TextView txtNoTour){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        txtNoTour.setVisibility(View.GONE);
        final List<Students> list = new ArrayList<>();
        List<Students> studentListByName = new ArrayList<>();

        Students student = new Students();
        student.getAllStudent(new FirebaseCallbackStudent() {
            @Override
            public void onCallback(List<Students> fetchedList) {
                if (fetchedList != null && !fetchedList.isEmpty()) {
                    list.addAll(fetchedList);
                    for (Students t : list) {
                        if (t.getName().toLowerCase().contains(nameStudent.toLowerCase())) {
                            studentListByName.add(t);
                        }
                    }
                    if(studentListByName.size() == 0){
                        txtNoTour.setVisibility(View.VISIBLE);
                        loadListView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        txtNoTour.setText("Không có sinh viên nào có tên chứa " + nameStudent + ".");

                    }else {
                        if(recyclerView.getVisibility() == View.GONE)
                            recyclerView.setVisibility(View.VISIBLE);
                        adapter = new AdapterStudentManagement(studentListByName, resource);
                        recyclerView.setAdapter(adapter);
                        loadListView.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Log.d("Login", "No tours found.");
                }
            }
        });
    }


    public long getSizeList() {
        return list.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void getTourList(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;  // Assign the recyclerView here
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AdapterStudentManagement(list, resource);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
                        if(resource == R.layout.custom_student_teacher_management){
                            new Tours().deleteStudentByTour(position, positionStudent);
                        }else if(resource == R.layout.layout_custom_fragmment_student_teacher_management){
                            new Students().deleteStudent(positionStudent);
                        }

                    }
                }
            });

            snackbar.show();
        }
    }

    public boolean checkCodeTour(String code){
        for(Students students : list){
            if(students.getCode().contains(code))
                return true;
        }
        return false;
    }

    public void addItemAdapter(Students s){
        adapter.addItem(s);
        new Students().addStudent(s, list.size()-1);
    }
}
