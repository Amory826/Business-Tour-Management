package com.nguyentrongtuan.businesstourmanagement.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nguyentrongtuan.businesstourmanagement.Controller.FirebaseCallbackClass;
import com.nguyentrongtuan.businesstourmanagement.Models.Teachers;
import com.nguyentrongtuan.businesstourmanagement.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdapterTeacherManagement extends RecyclerView.Adapter<AdapterTeacherManagement.ViewHolder> {

    private List<Teachers> listTeacher;
    private int resource;

    public AdapterTeacherManagement() {
    }

    public AdapterTeacherManagement(List<Teachers> listTeacher, int resource) {
        this.listTeacher = listTeacher;
        this.resource = resource;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNameStudent, txtIdStudent, txtNameClass, txtAddress, txtDateOfBirth, txtEmailStudent, txtPhoneStudent;
        View line;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameStudent = itemView.findViewById(R.id.nameStudent);
            txtIdStudent = itemView.findViewById(R.id.txtIdStudent);
            txtNameClass = itemView.findViewById(R.id.txtNameClass);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtDateOfBirth = itemView.findViewById(R.id.txtDateOfBirth);
            txtEmailStudent = itemView.findViewById(R.id.txtEmailStudent);
            txtPhoneStudent = itemView.findViewById(R.id.txtPhoneStudent);
            line = itemView.findViewById(R.id.line);
        }
    }

    @NonNull
    @Override
    public AdapterTeacherManagement.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        AdapterTeacherManagement.ViewHolder holder = new AdapterTeacherManagement.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTeacherManagement.ViewHolder holder, int position) {
        Teachers teacher = listTeacher.get(position);
        if (teacher != null) {
            holder.txtNameStudent.setText(teacher.getName());
            holder.txtIdStudent.setText("Mã giang viên: " + teacher.getCode());

            holder.txtNameClass.setVisibility(View.GONE);


            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");

            try {
                // Convert string to Date
                Date date = inputFormat.parse(teacher.getBirthDate());

                // Format Date to desired format
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = outputFormat.format(date);

                // Set formatted date
                holder.txtDateOfBirth.setText("Ngày sinh: " + formattedDate);
            } catch (ParseException e) {
                e.printStackTrace();
                holder.txtDateOfBirth.setText("Ngày sinh: N/A");
            }
            holder.txtAddress.setText("Địa chỉ: " + teacher.getAddress());
            holder.txtEmailStudent.setText("Email: " + teacher.getEmail());
            holder.txtPhoneStudent.setText("SĐT: " + teacher.getPhoneNumber());
        }
        if(position == listTeacher.size()-1)
            holder.line.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return listTeacher.size();
    }
}