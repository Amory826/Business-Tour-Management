package com.nguyentrongtuan.businesstourmanagement.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nguyentrongtuan.businesstourmanagement.Interface.FirebaseCallbackCompany;
import com.nguyentrongtuan.businesstourmanagement.Interface.FirebaseCallbackStudent;
import com.nguyentrongtuan.businesstourmanagement.Interface.FirebaseCallbackTeacher;
import com.nguyentrongtuan.businesstourmanagement.Models.Companies;
import com.nguyentrongtuan.businesstourmanagement.Models.Students;
import com.nguyentrongtuan.businesstourmanagement.Models.Teachers;
import com.nguyentrongtuan.businesstourmanagement.Models.Tours;
import com.nguyentrongtuan.businesstourmanagement.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdapterTourManagement extends RecyclerView.Adapter<AdapterTourManagement.ViewHolder> {

    private List<Tours> listTours;
    private int resource;

    public AdapterTourManagement() {

    }

    public AdapterTourManagement(List<Tours> listTours, int resource) {
        this.listTours = listTours;
        this.resource = resource;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNameTour;
        TextView txtDescription;
        TextView txtDate;
        TextView txtAvailable;
        TextView txtQuantity;
        TextView txtTeacher;
        TextView txtCompany;
        View line;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameTour = itemView.findViewById(R.id.txtNameTour);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtAvailable = itemView.findViewById(R.id.txtAvailable);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            txtTeacher = itemView.findViewById(R.id.txtTeacher);
            txtCompany = itemView.findViewById(R.id.txtCompany);
            line = itemView.findViewById(R.id.line);
        }
    }

    @NonNull
    @Override
    public AdapterTourManagement.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTourManagement.ViewHolder holder, int position) {
        Tours t = listTours.get(position);
        if (t != null) {
            holder.txtNameTour.setText(t.getName());
            holder.txtDescription.setText("Chi tiết: " + t.getDescription());
            holder.txtAvailable.setText("Số lượng: " + t.getAvailable());

            // Đặt giá trị mặc định ban đầu cho số lượng sinh viên
            holder.txtQuantity.setText("Số sinh viên đã đăng ký: Đang tải...");

            // Lấy danh sách sinh viên qua callback
            t.getStudentsList(position, new FirebaseCallbackStudent() {
                @Override
                public void onCallback(List<Students> list) {
                    if (list != null && !list.isEmpty()) {
                        holder.txtQuantity.setText("Số sinh viên đã đăng ký: " + list.size());
                    } else {
                        holder.txtQuantity.setText("Số sinh viên đã đăng ký: 0");
                    }
                }
            });

            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");

            try {
                // Convert string to Date
                Date date = inputFormat.parse(t.getStartDate());

                // Format Date to desired format
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = outputFormat.format(date);

                // Set formatted date
                holder.txtDate.setText("Ngày bắt đầu: " + formattedDate);

            } catch (Exception e) {
                e.printStackTrace();
                holder.txtDate.setText("Thời gian bắt đầu: N/A");
            }

            // Fetch teacher and company information
            Teachers teacher = new Teachers();
            Companies company = new Companies();

            List<Companies> listCompany = new ArrayList<>();
            List<Teachers> listTeacher = new ArrayList<>();

            teacher.getAllListTeacher(new FirebaseCallbackTeacher() {
                @Override
                public void onCallback(List<Teachers> list) {
                    if (list != null && !list.isEmpty()) {
                        listTeacher.addAll(list);

                        Teachers temp = teacher.getATeacher(listTeacher, t.getIdTeacher());
                        if (temp != null) {
                            holder.txtTeacher.setText("Giáo viên: " + temp.getName());
                        } else {
                            holder.txtTeacher.setText("Giáo viên: Không có giáo viên phụ trách.");
                        }

//                        Log.d("Login", "List after fetching teacher : " + listTeacher.size());
                    } else {
                        Log.d("Login", "No tours found.");
                        // Optionally, handle the empty state (e.g., show a message)
                    }
                }
            });

            company.getAllListCompany(new FirebaseCallbackCompany() {
                @Override
                public void onCallback(List<Companies> listTemp) {
                    if (listTemp != null && !listTemp.isEmpty()) {
                        listCompany.addAll(listTemp);

                        Companies temp = company.getACompany(listCompany, t.getIdCompany());
                        if (temp != null) {
                            holder.txtCompany.setText("Công ty: " + temp.getName());
                        } else {
                            holder.txtCompany.setText("Công ty: Chưa xác định.");
                        }
                    } else {
                        Log.d("Login", "No tours found.");
                    }
                }
            });
        }
        if (position == listTours.size() - 1)
            holder.line.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return listTours.size();
    }


}
