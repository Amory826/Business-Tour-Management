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
import com.nguyentrongtuan.businesstourmanagement.Adapter.AdapterTeacherManagement;
import com.nguyentrongtuan.businesstourmanagement.Adapter.RecyclerViewItemTouchHelper;
import com.nguyentrongtuan.businesstourmanagement.Interface.FirebaseCallbackTeacher;
import com.nguyentrongtuan.businesstourmanagement.Interface.ItemTouchHelperListener;
import com.nguyentrongtuan.businesstourmanagement.Models.Teachers;
import com.nguyentrongtuan.businesstourmanagement.R;

import java.util.ArrayList;
import java.util.List;

public class TeacherManagementController implements ItemTouchHelperListener {
    private final Context context;
    private AdapterTeacherManagement adapter;
    private final List<Teachers> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private final int resource;

    public TeacherManagementController(Context context, int resource){
        this.context = context;
        this.resource = resource;
    }

    public long getSizeList() {
        return list.size();
    }

    public void getTeacherList(RecyclerView recyclerView, ProgressBar loadListView, TextView txtNoTeacher){
        this.recyclerView = recyclerView; // Gán RecyclerView cho biến toàn cục
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        Teachers Teacher = new Teachers();

        Teacher.getAllListTeacher(new FirebaseCallbackTeacher() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onCallback(List<Teachers> listtemp) {
                if (listtemp != null && !listtemp.isEmpty()) {
                    list.addAll(listtemp);
                    if(list.size() == 0){
                        txtNoTeacher.setVisibility(View.VISIBLE);
                        loadListView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        txtNoTeacher.setText("Không có giảng viên nào.");
                    }else {
                        if(recyclerView.getVisibility() == View.GONE){
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                        txtNoTeacher.setVisibility(View.GONE);
                        adapter = new AdapterTeacherManagement(list, resource);
                        recyclerView.setAdapter(adapter);
                        loadListView.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Log.d("Login", "No teacher found.");
                }
            }

        });
        if(resource == R.layout.layout_custom_fragmment_student_teacher_management){
            deleteTeacher();
        }
    }

    public void getTeacherListByName(RecyclerView recyclerView, String nameTeacher, ProgressBar loadListView, TextView txtNoTour){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        txtNoTour.setVisibility(View.GONE);
        final List<Teachers> list = new ArrayList<>();
        List<Teachers> TeacherListByName = new ArrayList<>();

        Teachers Teacher = new Teachers();
        Teacher.getAllListTeacher(new FirebaseCallbackTeacher() {
            @Override
            public void onCallback(List<Teachers> fetchedList) {
                if (fetchedList != null && !fetchedList.isEmpty()) {
                    list.addAll(fetchedList);
                    for (Teachers t : list) {
                        if (t.getName().toLowerCase().contains(nameTeacher.toLowerCase())) {
                            TeacherListByName.add(t);
                        }
                    }
                    if(TeacherListByName.size() == 0){
                        txtNoTour.setVisibility(View.VISIBLE);
                        loadListView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        txtNoTour.setText("Không có sinh viên nào có tên chứa " + nameTeacher + ".");

                    }else {
                        if(recyclerView.getVisibility() == View.GONE)
                            recyclerView.setVisibility(View.VISIBLE);
                        adapter = new AdapterTeacherManagement(TeacherListByName,resource);
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


    public boolean checkCodeTour(String code){
        for(Teachers teacher : list){
            if(teacher.getCode().contains(code))
                return true;
        }
        return false;
    }

    private void deleteTeacher() {
        if (recyclerView != null) {
            ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerViewItemTouchHelper(0, ItemTouchHelper.LEFT, this);
            new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
        } else {
            Log.d("RecyclerViewError", "RecyclerView is null, cannot attach ItemTouchHelper.");
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof AdapterTeacherManagement.ViewHolder) {
            String nameTeacher = list.get(viewHolder.getAdapterPosition()).getName();
            Teachers teacherDelete = list.get(viewHolder.getAdapterPosition());
            int positionTeacher = viewHolder.getAdapterPosition();

            adapter.removeItem(positionTeacher);

            // Display the Snackbar with the viewHolder itemView as root
            Snackbar snackbar = Snackbar.make(viewHolder.itemView, nameTeacher + " deleted", Snackbar.LENGTH_LONG);
            snackbar.setAction("Undo", view -> {
                adapter.undoItem(teacherDelete, positionTeacher);
                // Check if recyclerView is not null before scrolling
                if (recyclerView != null) {
                    recyclerView.smoothScrollToPosition(positionTeacher);
                }
            });

            // Add callback to detect dismissal
            snackbar.addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                        new Teachers().deleteTeacher(positionTeacher);
                    }
                }
            });

            snackbar.show();
        }
    }

    public void addItemAdapter(Teachers s){
        adapter.addItem(s);
        new Teachers().addTeacher(s, list.size()-1);
    }
}
