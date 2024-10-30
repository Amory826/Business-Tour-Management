package com.nguyentrongtuan.businesstourmanagement.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nguyentrongtuan.businesstourmanagement.Controller.FirebaseCallbackCompany;
import com.nguyentrongtuan.businesstourmanagement.Controller.FirebaseCallbackStudent;
import com.nguyentrongtuan.businesstourmanagement.Controller.FirebaseCallbackTeacher;
import com.nguyentrongtuan.businesstourmanagement.Models.Companies;
import com.nguyentrongtuan.businesstourmanagement.Models.Students;
import com.nguyentrongtuan.businesstourmanagement.Models.Teachers;
import com.nguyentrongtuan.businesstourmanagement.Models.Tours;
import com.nguyentrongtuan.businesstourmanagement.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdapterBusinessTourManagement extends RecyclerView.Adapter<AdapterBusinessTourManagement.ViewHolder> {

    private List<Tours> listTours;
    private int resource;

    public AdapterBusinessTourManagement() {
    }

    public AdapterBusinessTourManagement(List<Tours> listTours, int resource) {
        this.resource = resource;
        this.listTours = listTours;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutForeground;
        TextView txtNameTour, txtDate, txtNameTeacher, txtNameCompany, txtAvailable;
        Button btnEdit, btnDelete, btnDetail;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameTour = itemView.findViewById(R.id.txtNameTour);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtNameTeacher = itemView.findViewById(R.id.txtNameTeacher);
            txtNameCompany = itemView.findViewById(R.id.txtNameCompany);
            txtAvailable = itemView.findViewById(R.id.txtAvailable);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnDetail = itemView.findViewById(R.id.btnDetail);
            layoutForeground = itemView.findViewById(R.id.layoutForeground);
        }
    }

    @NonNull
    @Override
    public AdapterBusinessTourManagement.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBusinessTourManagement.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Tours tour = listTours.get(position);
        if (tour != null) {
            holder.txtNameTour.setText(tour.getName());
            holder.txtAvailable.setText("Số lương: " + tour.getAvailable());

            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                // Convert string to Date
                Date date = inputFormat.parse(tour.getStartDate());

                // Format Date to desired format
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = outputFormat.format(date);

                // Set formatted date
                holder.txtDate.setText("Ngày bắt đầu: " + formattedDate);

            } catch (Exception e) {
                e.printStackTrace();
                holder.txtDate.setText("Ngày bắt đầu:: N/A");
            }

            Teachers teacher = new Teachers();
            Companies company = new Companies();

            List<Companies> listCompany = new ArrayList<>();
            List<Teachers> listTeacher = new ArrayList<>();

            teacher.getAllListTeacher(new FirebaseCallbackTeacher() {
                @Override
                public void onCallback(List<Teachers> list) {
                    if (list != null && !list.isEmpty()) {
                        listTeacher.addAll(list);

                        Teachers temp = teacher.getATeacher(listTeacher, tour.getIdTeacher());
                        if (temp != null) {
                            holder.txtNameTeacher.setText("Giáo viên: " + temp.getName());
                        } else {
                            holder.txtNameTeacher.setText("Giáo viên: Không có giáo viên phụ trách.");
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

                        Companies temp = company.getACompany(listCompany, tour.getIdCompany());
                        if (temp != null) {
                            holder.txtNameCompany.setText("Công ty: " + temp.getName());
                        } else {
                            holder.txtNameCompany.setText("Công ty: Chưa xác định.");
                        }
                    } else {
                        Log.d("Login", "No tours found.");
                    }
                }
            });
        }

        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.layout_dialog_custom_detail_business_tour_management);

                TextView txtNameTourDetail = dialog.findViewById(R.id.txtNameTour);
                TextView txtDescriptionDetail = dialog.findViewById(R.id.txtDescription);
                TextView txtDateDetail = dialog.findViewById(R.id.txtDate);
                TextView txtAvailableDetail = dialog.findViewById(R.id.txtAvailable);
                TextView txtQuantityDetail = dialog.findViewById(R.id.txtQuantity);
                TextView txtTeacherDetail = dialog.findViewById(R.id.txtTeacher);
                TextView txtCompanyDetail = dialog.findViewById(R.id.txtCompany);

                txtNameTourDetail.setText(tour.getName());
                txtDescriptionDetail.setText("Chi tiết: " + tour.getDescription());
                txtAvailableDetail.setText("Số lượng: " + tour.getAvailable());

                // Đặt giá trị mặc định ban đầu cho số lượng sinh viên
                txtQuantityDetail.setText("Số sinh viên đã đăng ký: Đang tải...");

                // Lấy danh sách sinh viên qua callback
                tour.getStudentsList(position, new FirebaseCallbackStudent() {
                    @Override
                    public void onCallback(List<Students> list) {
                        if (list != null && !list.isEmpty()) {
                            txtQuantityDetail.setText("Số sinh viên đã đăng ký: " + list.size());
                        } else {
                            txtQuantityDetail.setText("Số sinh viên đã đăng ký: 0");
                        }
                    }
                });

                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");

                try {
                    // Convert string to Date
                    Date date = inputFormat.parse(tour.getStartDate());

                    // Format Date to desired format
                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                    String formattedDate = outputFormat.format(date);

                    // Set formatted date
                    txtDateDetail.setText("Ngày bắt đầu: " + formattedDate);

                } catch (Exception e) {
                    e.printStackTrace();
                    txtDateDetail.setText("Thời gian bắt đầu: N/A");
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

                            Teachers temp = teacher.getATeacher(listTeacher, tour.getIdTeacher());
                            if (temp != null) {
                                txtTeacherDetail.setText("Giáo viên: " + temp.getName());
                            } else {
                                txtTeacherDetail.setText("Giáo viên: Không có giáo viên phụ trách.");
                            }
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

                            Companies temp = company.getACompany(listCompany, tour.getIdCompany());
                            if (temp != null) {
                                txtCompanyDetail.setText("Công ty: " + temp.getName());
                            } else {
                                txtCompanyDetail.setText("Công ty: Chưa xác định.");
                            }
                        } else {
                            Log.d("Login", "No tours found.");
                        }
                    }

                });
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTours.size();
    }


    public void removeItem(int position) {
        listTours.remove(position);
        notifyItemRemoved(position);
    }

    public void undoItem(Tours tour, int position) {
        listTours.add(position, tour);
        notifyItemInserted(position);
    }
}

