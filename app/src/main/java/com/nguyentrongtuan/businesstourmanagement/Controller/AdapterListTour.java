// setAdapterListTour.java
package com.nguyentrongtuan.businesstourmanagement.Controller;

import android.content.Context;
import android.util.Log;
import android.view.*;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.nguyentrongtuan.businesstourmanagement.Models.Companies;
import com.nguyentrongtuan.businesstourmanagement.Models.Students;
import com.nguyentrongtuan.businesstourmanagement.Models.Teachers;
import com.nguyentrongtuan.businesstourmanagement.Models.Tours;
import com.nguyentrongtuan.businesstourmanagement.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdapterListTour extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Tours> list;

    public AdapterListTour(int layout, Context context, List<Tours> list ) {
        this.list = list;
        this.layout = layout;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Tours getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position; // Or use a unique ID from your Tours model
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(layout, parent, false);
            holder = new ViewHolder();
            holder.txtNameTour = convertView.findViewById(R.id.txtNameTour);
            holder.txtDescription = convertView.findViewById(R.id.txtDescription);
            holder.txtDate = convertView.findViewById(R.id.txtDate);
            holder.txtAvailable = convertView.findViewById(R.id.txtAvailable);
            holder.txtQuantity = convertView.findViewById(R.id.txtQuantity);
            holder.txtTeacher = convertView.findViewById(R.id.txtTeacher);
            holder.txtCompany = convertView.findViewById(R.id.txtCompany);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the current tour
        Tours t = getItem(position);
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
                        if(temp != null){
                            holder.txtTeacher.setText("Giáo viên: "  + temp.getName());
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
                        if(temp != null){
                            holder.txtCompany.setText("Công ty: "  + temp.getName());
                        } else {
                            holder.txtCompany.setText("Công ty: Chưa xác định.");
                        }
                    } else {
                        Log.d("Login", "No tours found.");
                    }
                }
            });
        }
        if(position == list.size()-1)
            convertView.findViewById(R.id.line).setVisibility(View.GONE);
        return convertView;
    }


    // ViewHolder pattern to optimize list performance
    private static class ViewHolder {
        TextView txtNameTour;
        TextView txtDescription;
        TextView txtDate;
        TextView txtAvailable;
        TextView txtQuantity;
        TextView txtTeacher;
        TextView txtCompany;
    }
}
