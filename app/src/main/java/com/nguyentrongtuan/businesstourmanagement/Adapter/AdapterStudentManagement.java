package com.nguyentrongtuan.businesstourmanagement.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nguyentrongtuan.businesstourmanagement.Interface.FirebaseCallbackClass;
import com.nguyentrongtuan.businesstourmanagement.Interface.ForegroundLayoutHolder;
import com.nguyentrongtuan.businesstourmanagement.Models.Class;
import com.nguyentrongtuan.businesstourmanagement.Models.Students;
import com.nguyentrongtuan.businesstourmanagement.Models.Teachers;
import com.nguyentrongtuan.businesstourmanagement.Models.Tours;
import com.nguyentrongtuan.businesstourmanagement.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdapterStudentManagement extends RecyclerView.Adapter<AdapterStudentManagement.ViewHolder> {

    private List<Students> listStudent;
    private static int resource;

    public AdapterStudentManagement() {
        this.listStudent = new ArrayList<>(); // Khởi tạo danh sách trống
    }

    public AdapterStudentManagement(List<Students> listStudent, int resource) {
        this.listStudent = (listStudent != null) ? listStudent : new ArrayList<>(); // Nếu null thì khởi tạo danh sách trống
        this.resource = resource;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements ForegroundLayoutHolder {
        LinearLayout layoutForeground;
        TextView nameStudent, txtIdStudent, txtNameClass, txtAddress, txtDateOfBirth, txtEmailStudent, txtPhoneStudent;
        Button btnEdit;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameStudent = itemView.findViewById(R.id.nameStudent);
            txtIdStudent = itemView.findViewById(R.id.txtIdStudent);
            txtNameClass = itemView.findViewById(R.id.txtNameClass);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtDateOfBirth = itemView.findViewById(R.id.txtDateOfBirth);
            txtEmailStudent = itemView.findViewById(R.id.txtEmailStudent);
            txtPhoneStudent = itemView.findViewById(R.id.txtPhoneStudent);
            layoutForeground = itemView.findViewById(R.id.layoutForeground);
            if(resource == R.layout.custom_students_teachers_management_home){
                btnEdit = itemView.findViewById(R.id.btnEdit);
            }
        }

        @Override
        public View getForegroundView() {
            return layoutForeground;
        }
    }

    @NonNull
    @Override
    public AdapterStudentManagement.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterStudentManagement.ViewHolder holder, int position) {
        Students student = listStudent.get(position);
        final List<Class> listClass = new ArrayList<>();

        if (student != null) {
            holder.nameStudent.setText(student.getName());
            holder.txtIdStudent.setText("Mã sinh viên: " + student.getCode());

            Class class1 = new Class();
            class1.getAllClass(new FirebaseCallbackClass() {
                @Override
                public void onCallback(List<Class> list) {
                    if (list != null && !list.isEmpty()) {
                        listClass.addAll(list);

                        Class temp = class1.getAClass(listClass, student.getIdClass());
                        if(temp != null){
                            holder.txtNameClass.setText("Lớp: "  + temp.getName());
                        } else {
                            holder.txtNameClass.setText("Lớp: Chưa xác định.");
                        }
                    } else {
                        Log.d("Login", "No tours found.");
                    }
                }
            });

            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");

            try {
                // Convert string to Date
                Date date = inputFormat.parse(student.getBirthDate());

                // Format Date to desired format
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = outputFormat.format(date);

                // Set formatted date
                holder.txtDateOfBirth.setText("Ngày sinh: " + formattedDate);
            } catch (ParseException e) {
                e.printStackTrace();
                holder.txtDateOfBirth.setText("Thời gian bắt đầu: N/A");
            }
            holder.txtAddress.setText("Địa chỉ: " + student.getAddress());
            holder.txtEmailStudent.setText("Email: " + student.getEmail());
            holder.txtPhoneStudent.setText("SĐT: " + student.getPhoneNumber());


        }
    }

    @Override
    public int getItemCount() {
        return (listStudent != null) ? listStudent.size() : 0;
    }

    public void addItem(Students s) {
        listStudent.add(s);
        notifyItemInserted(listStudent.size() - 1);
    }


    public void removeItem(int position) {
        listStudent.remove(position);
        notifyItemRemoved(position);
    }

    public void undoItem(Students student, int position) {
        listStudent.add(position, student);
        notifyItemInserted(position);
    }


}