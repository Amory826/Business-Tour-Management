    package com.nguyentrongtuan.businesstourmanagement.Controller;

    import android.content.Context;
    import android.util.Log;
    import android.view.View;
    import android.widget.ProgressBar;
    import android.widget.TextView;

    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.nguyentrongtuan.businesstourmanagement.Models.Tours;
    import com.nguyentrongtuan.businesstourmanagement.R;

    import java.util.ArrayList;
    import java.util.List;

    public class TourManagementController {

        Context context;
        AdapterTourManagement adapter;

        public TourManagementController(Context context) {
            this.context = context;
        }

        public void getTourList(RecyclerView recyclerView, ProgressBar loadListView, TextView txtNoTour){
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layoutManager);
            final List<Tours> list = new ArrayList<>();

            Tours tour = new Tours();
            tour.getAllListTour(new FirebaseCallbackTours() {
                @Override
                public void onCallback(List<Tours> fetchedList) {
                    if (fetchedList != null && !fetchedList.isEmpty()) {
                        list.addAll(fetchedList);
                        if(list.size() == 0){
                            txtNoTour.setVisibility(View.VISIBLE);
                            loadListView.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                        }else {
                            if(recyclerView.getVisibility() == View.GONE){
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                            txtNoTour.setVisibility(View.GONE);
                            adapter = new AdapterTourManagement(list, R.layout.custom_listview);
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

        public void getTourListByName(RecyclerView recyclerView, String nameTour, ProgressBar loadListView, TextView txtNoTour){
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layoutManager);
            txtNoTour.setVisibility(View.GONE);
            final List<Tours> list = new ArrayList<>();
            List<Tours> tourListByName = new ArrayList<>();

            Tours tour = new Tours();
            tour.getAllListTour(new FirebaseCallbackTours() {
                @Override
                public void onCallback(List<Tours> fetchedList) {
                    if (fetchedList != null && !fetchedList.isEmpty()) {
                        list.addAll(fetchedList);
                        for (Tours t : list) {
                            if (t.getName().toLowerCase().contains(nameTour.toLowerCase())) {
                                tourListByName.add(t);
                            }
                        }
                        if(tourListByName.size() == 0){
                            txtNoTour.setVisibility(View.VISIBLE);
                            loadListView.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);

                        }else {
                            if(recyclerView.getVisibility() == View.GONE)
                                recyclerView.setVisibility(View.VISIBLE);
                            adapter = new AdapterTourManagement(tourListByName, R.layout.custom_listview);
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
