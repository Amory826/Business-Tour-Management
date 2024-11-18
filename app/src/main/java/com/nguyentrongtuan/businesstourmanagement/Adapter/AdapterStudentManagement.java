package com.nguyentrongtuan.businesstourmanagement.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import com.nguyentrongtuan.businesstourmanagement.Interface.FirebaseCallbackClass;
import com.nguyentrongtuan.businesstourmanagement.Interface.ForegroundLayoutHolder;
import com.nguyentrongtuan.businesstourmanagement.Models.Class;
import com.nguyentrongtuan.businesstourmanagement.Models.Students;
import com.nguyentrongtuan.businesstourmanagement.R;

import org.jetbrains.annotations.Contract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AdapterStudentManagement extends RecyclerView.Adapter<AdapterStudentManagement.ViewHolder> {

    private final List<Students> listStudent;
    private static int resource;

    public AdapterStudentManagement() {
        this.listStudent = new ArrayList<>(); // Khởi tạo danh sách trống
    }

    public AdapterStudentManagement(List<Students> listStudent, int resource) {
        this.listStudent = (listStudent != null) ? listStudent : new ArrayList<>(); // Nếu null thì khởi tạo danh sách trống
        AdapterStudentManagement.resource = resource;
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
            if(resource == R.layout.layout_custom_fragmment_student_teacher_management){
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
    public void onBindViewHolder(@NonNull AdapterStudentManagement.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
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
            if(resource == R.layout.layout_custom_fragmment_student_teacher_management){
                holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Dialog dialog = new Dialog(view.getContext(), R.style.FullScreenDialog);
                        dialog.setContentView(R.layout.layout_dialog_add_edit_student_management);

                        TextInputEditText edtCodeStudent = dialog.findViewById(R.id.edtCodeStudent);
                        TextInputEditText edtNameStudent = dialog.findViewById(R.id.edtNameStudent);
                        TextInputEditText edtAddress = dialog.findViewById(R.id.edtAddress);
                        TextInputEditText edtBirthDate = dialog.findViewById(R.id.edtBirthDate);
                        TextInputEditText edtEmail = dialog.findViewById(R.id.edtEmail);
                        TextInputEditText edtPhoneNumber = dialog.findViewById(R.id.edtPhoneNumber);
                        AutoCompleteTextView edtNameClass = dialog.findViewById(R.id.edtNameClass);

                        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
                        Button btnCancel = dialog.findViewById(R.id.btnCancel);

                        final String[] codeClass = {student.getIdClass()};
                        Class aClass = new Class();
                        aClass.getAllClass(new  FirebaseCallbackClass() {
                            @Override
                            public void onCallback(List<Class> list) {
                                if (list != null && !list.isEmpty()) {
                                    listClass .clear();
                                    listClass.addAll(list);
                                    Class tempClass = aClass.getAClass(listClass, student.getIdClass());
                                    ArrayAdapter<Class> adapter = new ArrayAdapter<>(view.getContext(), R.layout.layout_custom_sprinner, listClass);
                                    edtNameClass.setAdapter(adapter);

                                    edtNameClass.setOnClickListener(v -> edtNameClass.showDropDown());

                                    edtNameClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            Class selectedClass = (Class) listClass.get(i);
                                            codeClass[0] = selectedClass.getCode();
                                        }
                                    });

                                    edtNameClass.setText(tempClass.getName(), false);
                                }
                            }
                        });

                        //get date startR
                        final String[] BirthDate = {student.getBirthDate()};
                        edtBirthDate.setOnClickListener(new View.OnClickListener() {
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

                                        BirthDate[0] = date;
                                        edtBirthDate.setText(date);
                                        dialogDate.dismiss();
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


                        edtCodeStudent.setText(student.getCode());
                        edtNameStudent.setText(student.getName());
                        edtAddress.setText(student.getAddress());
                        edtBirthDate.setText(student.getBirthDate());
                        edtEmail.setText(student.getEmail());
                        edtPhoneNumber.setText(student.getPhoneNumber());


                        btnSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String codeStudent = Objects.requireNonNull(edtCodeStudent.getText()).toString().trim();
                                String nameStudent = Objects.requireNonNull(edtNameStudent.getText()).toString().trim();
                                String address = Objects.requireNonNull(edtAddress.getText()).toString().trim();
                                String birthDate = Objects.requireNonNull(edtBirthDate.getText()).toString().trim();
                                String email = Objects.requireNonNull(edtEmail.getText()).toString().trim();
                                String phoneNumber = Objects.requireNonNull(edtPhoneNumber.getText()).toString().trim();
                                String NameClass = Objects.requireNonNull(edtNameClass.getText()).toString().trim();

                                if (codeStudent.isEmpty()) {
                                    edtCodeStudent.setError("Vui lòng nhập mã sinh viên");
                                    edtCodeStudent.requestFocus();
                                    return;
                                }

                                if (nameStudent.isEmpty()) {
                                    edtNameStudent.setError("Vui lòng nhập tên sinh viên");
                                    edtNameStudent.requestFocus();
                                    return;
                                }

                                if (address.isEmpty()) {
                                    edtAddress.setError("Vui lòng nhập địa chỉ");
                                    edtAddress.requestFocus();
                                    return;
                                }

                                if (birthDate.isEmpty()) {
                                    edtBirthDate.setError("Vui lòng nhập ngày sinh");
                                    edtBirthDate.requestFocus();
                                    return;
                                }

                                if (email.isEmpty()) {
                                    edtEmail.setError("Vui lòng nhập email");
                                    edtEmail.requestFocus();
                                    return;
                                }

                                if (phoneNumber.isEmpty()) {
                                    edtPhoneNumber.setError("Vui lòng nhập số điện thoại");
                                    edtPhoneNumber.requestFocus();
                                    return;
                                }

                                if(!isValidPhoneNumber(phoneNumber)){
                                    edtPhoneNumber.setError("Vui lòng nhập đúng dữ liệu");
                                    edtPhoneNumber.requestFocus();
                                    return;
                                }

                                if (NameClass.isEmpty()) {
                                    edtNameClass.setError("Vui lòng nhập tên lớp");
                                    edtNameClass.requestFocus();
                                    return;
                                }else{
                                    edtNameClass.setError(null);
                                }

                                student.setCode(codeStudent);
                                student.setName(nameStudent);
                                student.setAddress(address);
                                student.setBirthDate(birthDate);
                                student.setEmail(email);
                                student.setPhoneNumber(phoneNumber);
                                student.setIdClass(codeClass[0]);

                                new Students().addStudent(student, position);
                                notifyItemChanged(position);
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
            }

        }
    }

    @Override
    public int getItemCount() {
        return listStudent.size();
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

    // Hàm kiểm tra số điện thoại hợp lệ
    public boolean isValidPhoneNumber(String phoneNumber) {
        // Regex kiểm tra số điện thoại hợp lệ
        String regex = "^(\\+84|0)[1-9][0-9]{8,9}$";
        return phoneNumber.matches(regex);
    }

}