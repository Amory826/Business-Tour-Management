package com.nguyentrongtuan.businesstourmanagement.Adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.nguyentrongtuan.businesstourmanagement.Interface.FirebaseCallbackCompany;
import com.nguyentrongtuan.businesstourmanagement.Interface.FirebaseCallbackStudent;
import com.nguyentrongtuan.businesstourmanagement.Interface.FirebaseCallbackTeacher;
import com.nguyentrongtuan.businesstourmanagement.Interface.ForegroundLayoutHolder;
import com.nguyentrongtuan.businesstourmanagement.Models.Companies;
import com.nguyentrongtuan.businesstourmanagement.Models.Students;
import com.nguyentrongtuan.businesstourmanagement.Models.Teachers;
import com.nguyentrongtuan.businesstourmanagement.Models.Tours;
import com.nguyentrongtuan.businesstourmanagement.R;
import com.nguyentrongtuan.businesstourmanagement.View.ManagementStudentTour;
import com.nguyentrongtuan.businesstourmanagement.View.ShowData;

import org.jetbrains.annotations.Contract;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AdapterBusinessTourManagement extends RecyclerView.Adapter<AdapterBusinessTourManagement.ViewHolder>{

    private List<Tours> listTours;
    private int resource;

    List<Teachers> listTeacher = new ArrayList<>();
    List<Companies> listCompany = new ArrayList<>();


    public AdapterBusinessTourManagement() {
    }

    public AdapterBusinessTourManagement(List<Tours> listTours, int resource) {
        this.resource = resource;
        this.listTours = listTours;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder implements ForegroundLayoutHolder {
        LinearLayout layoutForeground;
        TextView txtNameTour, txtDate, txtNameTeacher, txtNameCompany, txtAvailable;
        Button btnEdit, btnAddStudent;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameTour = itemView.findViewById(R.id.txtNameTour);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtNameTeacher = itemView.findViewById(R.id.txtNameTeacher);
            txtNameCompany = itemView.findViewById(R.id.txtNameCompany);
            txtAvailable = itemView.findViewById(R.id.txtAvailable);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnAddStudent = itemView.findViewById(R.id.btnAddStudent);
            layoutForeground = itemView.findViewById(R.id.layoutForeground);
        }

        @Override
        public View getForegroundView() {
            return layoutForeground;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
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
                holder.txtDate.setText("Ngày bắt đầu: N/A");
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

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(view.getContext(), R.style.FullScreenDialog);
                dialog.setContentView(R.layout.layout_dialog_add_edit_tour_with_business);

                TextInputEditText edtCodeTour = dialog.findViewById(R.id.edtCodeTour);
                TextInputEditText edtNameTour = dialog.findViewById(R.id.edtNameTour);
                TextInputEditText edtAvailable = dialog.findViewById(R.id.edtAvailable);
                TextInputEditText edtDescription = dialog.findViewById(R.id.edtDescription);
                TextInputEditText edtDateStat = dialog.findViewById(R.id.edtDateStat);
                AutoCompleteTextView edtNameTeacher = dialog.findViewById(R.id.edtNameTeacher);
                AutoCompleteTextView edtNameCompany = dialog.findViewById(R.id.edtNameCompany);

                Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
                Button btnCancel = dialog.findViewById(R.id.btnCancel);

                final String[] codeTeacher = {tour.getIdTeacher()};
                Teachers teacher = new Teachers();
                teacher.getAllListTeacher(new FirebaseCallbackTeacher() {
                    @Override
                    public void onCallback(List<Teachers> list) {
                        if (list != null && !list.isEmpty()) {
                            listTeacher .clear();
                            listTeacher.addAll(list);

                            Teachers tempTeacher = teacher.getATeacher(listTeacher, tour.getIdTeacher());

                            ArrayAdapter<Teachers> adapter = new ArrayAdapter<>(view.getContext(), R.layout.layout_custom_sprinner, listTeacher);
                            edtNameTeacher.setAdapter(adapter);

                            edtNameTeacher.setOnClickListener(v -> edtNameTeacher.showDropDown());

                            edtNameTeacher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Teachers selectedTeacher = (Teachers) listTeacher.get(i);
                                    codeTeacher[0] = selectedTeacher.getCode();
                                }
                            });
                            edtNameTeacher.setText(tempTeacher.getName(), false);
                        }
                    }
                });


                final String[] codeCompany = {tour.getIdCompany()};
                Companies company = new Companies();
                company.getAllListCompany(new FirebaseCallbackCompany() {
                    @Override
                    public void onCallback(List<Companies> list) {
                        if (list != null && !list.isEmpty()) {
                            listCompany.clear();
                            listCompany.addAll(list);
                            Companies tempCompany = company.getACompany(listCompany, tour.getIdCompany());
                            ArrayAdapter<Companies> adapter = new ArrayAdapter<>(view.getContext(), R.layout.layout_custom_sprinner, listCompany);
                            edtNameCompany.setAdapter(adapter);

                            // Hiển thị Dropdown khi nhấn vào AutoCompleteTextView
                            edtNameCompany.setOnClickListener(v -> edtNameCompany.showDropDown());

                            // Bắt sự kiện khi chọn một công ty
                            edtNameCompany.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Companies selectedCompany = (Companies) listCompany.get(i);
                                    codeCompany[0] = selectedCompany.getCode();
                                }
                            });
                            edtNameCompany.setText(tempCompany.getName(), false);

                        }
                    }
                });

                //get date start
                final String[] dateStart = {tour.getStartDate()};
                edtDateStat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Dialog dialogDate = new Dialog(view.getContext(), R.style.FullScreenDialog);
                        dialogDate.setContentView(R.layout.layout_custom_datepicker);

                        Button btnSubmit = dialogDate.findViewById(R.id.btnSubmit);
                        Button btnCancel = dialogDate.findViewById(R.id.btnCancel);
                        DatePicker datePicker = dialogDate.findViewById(R.id.datePicker);

                        dialogDate.show();

                        btnSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String date = getDatePicker(datePicker);

                                dateStart[0] = date;
                                edtDateStat.setText(date);
                                dialogDate.dismiss();
                                Log.d("Login","123" + dateStart[0]);
                            }
                        });

                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogDate.dismiss();
                            }
                        });
                    }
                });


                edtCodeTour.setText(tour.getCode());
                edtNameTour.setText(tour.getName());
                edtAvailable.setText(String.valueOf(tour.getAvailable()));
                edtDescription.setText(tour.getDescription());
                edtDateStat.setText(tour.getStartDate());

                List<Students>  listStudent = new ArrayList<>();
                tour.getStudentsList(position, new FirebaseCallbackStudent() {
                    @Override
                    public void onCallback(List<Students> list) {
                        listStudent.addAll(list);
                        Log.d("1234", String.valueOf(listStudent.size()));

                    }
                });


                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Lấy dữ liệu từ dialog
                        String codeTour = Objects.requireNonNull(edtCodeTour.getText()).toString().trim();
                        String nameTour = Objects.requireNonNull(edtNameTour.getText()).toString().trim();
                        String availableStr = Objects.requireNonNull(edtAvailable.getText()).toString().trim();
                        String description = Objects.requireNonNull(edtDescription.getText()).toString().trim();
                        String dateStat = Objects.requireNonNull(edtDateStat.getText()).toString().trim();

                        if (codeTour.isEmpty()) {
                            edtCodeTour.setError("Vui lòng nhập mã tour");
                            edtCodeTour.requestFocus();
                            return;
                        }

                        if (nameTour.isEmpty()) {
                            edtNameTour.setError("Vui lòng nhập tên tour");
                            edtNameTour.requestFocus();
                            return;
                        }

                        if (availableStr.isEmpty()) {
                            edtAvailable.setError("Vui lòng nhập số lượng sinh viên");
                            edtAvailable.requestFocus();
                            return;
                        }

                        long available = checkErrorNumber(availableStr);
                        if (available == -1) {
                            return;
                        }

                        if (description.isEmpty()) {
                            edtDescription.setError("Vui lòng nhập mô tả");
                            edtDescription.requestFocus();
                            return;
                        }

                        if (dateStat.isEmpty()) {
                            edtDateStat.setError("Vui lòng chọn ngày bắt đầu");
                            edtDateStat.requestFocus();
                            return;
                        }

                        // Cập nhật các trường thông tin mới mà không thay thế studentList
                        tour.setAvailable(available);
                        tour.setCode(codeTour);
                        tour.setName(nameTour);
                        tour.setIdCompany(codeCompany[0]);
                        tour.setIdTeacher(codeTeacher[0]);
                        tour.setStartDate(dateStart[0]);
                        tour.setDescription(description);

                        // Gọi addTour để cập nhật các thông tin chung của tour
                        new Tours().addTour(tour, position);

                        // Gọi addListByTour để chỉ cập nhật danh sách sinh viên mà không thay thế dữ liệu khác
                        new Tours().addListByTour(listStudent, position);

                        dialog.dismiss();
                    }
                });


                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });

        holder.btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iAddStudent = new Intent(view.getContext(), ManagementStudentTour.class);
                Bundle bundle = new Bundle();

                List<Students> studentsList = new ArrayList<>();

                assert tour != null;
                tour.getStudentsList(position, new FirebaseCallbackStudent() {
                    @Override
                    public void onCallback(List<Students> list) {
                        studentsList.addAll(list);

                        // Sau khi nhận được dữ liệu từ Firebase, thêm vào Bundle và start Activity
                        bundle.putSerializable("studentsList", (Serializable) studentsList);
                        bundle.putSerializable("Tour", (Serializable) tour);

                        Log.d("123456", list.size() + "");

                        iAddStudent.putExtra("putTour", bundle);
                        view.getContext().startActivity(iAddStudent);
                    }
                });
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

    public void addItem(Tours tour) {
        listTours.add(tour);
        notifyItemInserted(listTours.size() - 1);
    }

    @NonNull
    @Contract(pure = true)
    private String getDatePicker(DatePicker datePicker){
        String date = "";
        String day, month, year;
        day = String.valueOf(datePicker.getDayOfMonth());
        month = String.valueOf(datePicker.getMonth() + 1);
        year = String.valueOf(datePicker.getYear());
        date = year + "-" + month + "-" + day;

        return date;
    }

    private long checkErrorNumber(String s) {
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

}

