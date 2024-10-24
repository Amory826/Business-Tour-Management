package com.nguyentrongtuan.businesstourmanagement.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nguyentrongtuan.businesstourmanagement.Models.Companies;
import com.nguyentrongtuan.businesstourmanagement.R;

import java.util.List;

public class AdapterCompanyWithSchool extends RecyclerView.Adapter<AdapterCompanyWithSchool.ViewHolder> {

    private List<Companies> listCompany;
    private int resource;

    public AdapterCompanyWithSchool() {

    }

    public AdapterCompanyWithSchool(List<Companies> listCompany, int resource) {
        this.listCompany = listCompany;
        this.resource = resource;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtIdCompany, txtNameCompany, txtAddress, txtDescription, txtEmailCompany, txtPhoneCompany;
        View line;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtIdCompany = itemView.findViewById(R.id.txtIdCompany);
            txtNameCompany = itemView.findViewById(R.id.txtNameCompany);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtEmailCompany = itemView.findViewById(R.id.txtEmailCompany);
            txtPhoneCompany = itemView.findViewById(R.id.txtPhoneCompany);
            line = itemView.findViewById(R.id.line);
        }
    }

    @NonNull
    @Override
    public AdapterCompanyWithSchool.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);

        AdapterCompanyWithSchool.ViewHolder holder = new AdapterCompanyWithSchool.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCompanyWithSchool.ViewHolder holder, int position) {
        Companies company = listCompany.get(position);
        if (company != null) {
            holder.txtIdCompany.setText("Mã công ty: " + company.getCode());
            holder.txtNameCompany.setText(company.getName());
            holder.txtAddress.setText("Địa chỉ: " + company.getAddress());
            holder.txtDescription.setText("Thông tin chi tiết: " + company.getDescription());
            holder.txtEmailCompany.setText("Email: " + company.getEmail());
            holder.txtPhoneCompany.setText("SĐT: " + company.getPhoneNumber());
        }
        if(position == listCompany.size()-1)
            holder.line.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return listCompany.size();
    }
}
