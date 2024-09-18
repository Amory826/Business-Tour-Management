package com.nguyentrongtuan.businesstourmanagement.Controller;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nguyentrongtuan.businesstourmanagement.DAO.CompanyDAO;
import com.nguyentrongtuan.businesstourmanagement.DAO.TeacherDAO;
import com.nguyentrongtuan.businesstourmanagement.DAO.ToursDAO;
import com.nguyentrongtuan.businesstourmanagement.Models.Companies;
import com.nguyentrongtuan.businesstourmanagement.Models.Teachers;
import com.nguyentrongtuan.businesstourmanagement.Models.Tours;
import com.nguyentrongtuan.businesstourmanagement.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class setAdapterListTour extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Tours> list;

    public setAdapterListTour(int layout, Context context, List<Tours> list ) {
        this.list = list;
        Log.d("Login", "List adapter" + this.list.size());
        this.layout = layout;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(layout, null);

        // Ánh xạ view
        TextView txtNameTour, txtDescription, txtDate, txtAvailable,
                txtQuantity, txtTeacher, txtCompany;

        txtNameTour = convertView.findViewById(R.id.txtNameTour);
        txtDescription = convertView.findViewById(R.id.txtDescription);
        txtDate = convertView.findViewById(R.id.txtDate);
        txtAvailable = convertView.findViewById(R.id.txtAvailable);
        txtQuantity = convertView.findViewById(R.id.txtQuantity);
        txtTeacher = convertView.findViewById(R.id.txtTeacher);
        txtCompany = convertView.findViewById(R.id.txtCompany);

        // Kết nối CSDL
        CompanyDAO companyDAO = new CompanyDAO();
        TeacherDAO teacherDAO = new TeacherDAO();

        List<Companies> companiesList = companyDAO.getAllCompany();
        List<Teachers> teachersList = teacherDAO.getAllTeacher();

        Tours t = list.get(position);

        //gán giá trị cho các textview

        txtNameTour.setText(t.getName());
        txtDescription.setText("Chi tiết: " + t.getDescription());
        txtAvailable.setText("Số lượng: " + t.getAvailable());
        txtQuantity.setText("Số sinh viên đã đăng ký: " + t.getStudentsList().size());

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Chuyển chuỗi thành đối tượng Date
            Date date = inputFormat.parse(t.getStartDate());

            // Định dạng lại ngày để hiển thị theo "dd-MM-yyyy"
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate = outputFormat.format(date);

            // Hiển thị ngày lên TextView
            txtDate.setText("Thời gian bắt đầu: " + formattedDate);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Companies exp = companyDAO.getACompany(t.getIdCompany(), companiesList);

        txtCompany.setText("Công ty: "  + exp.getName());

        Teachers tea = teacherDAO.getATeacher(t.getIdTeacher(), teachersList);
        txtCompany.setText("Giáo viên: "  + tea.getName());


        return convertView;
    }
}