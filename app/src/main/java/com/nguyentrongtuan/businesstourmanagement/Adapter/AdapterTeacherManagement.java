package com.nguyentrongtuan.businesstourmanagement.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.nguyentrongtuan.businesstourmanagement.Interface.ForegroundLayoutHolder;
import com.nguyentrongtuan.businesstourmanagement.Models.Students;
import com.nguyentrongtuan.businesstourmanagement.Models.Teachers;
import com.nguyentrongtuan.businesstourmanagement.R;

import org.jetbrains.annotations.Contract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AdapterTeacherManagement extends RecyclerView.Adapter<AdapterTeacherManagement.ViewHolder> {

    private List<Teachers> listTeacher;
    private static int resource;

    public AdapterTeacherManagement() {
    }

    public AdapterTeacherManagement(List<Teachers> listTeacher, int resource) {
        this.listTeacher = listTeacher;
        AdapterTeacherManagement.resource = resource;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements ForegroundLayoutHolder {
        LinearLayout layoutForeground;
        TextView txtNameStudent, txtIdStudent, txtNameClass, txtAddress, txtDateOfBirth, txtEmailStudent, txtPhoneStudent;
        Button btnEdit;


        public ViewHolder(@NonNull View itemView)  {
            super(itemView);
            txtNameStudent = itemView.findViewById(R.id.nameStudent);
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
    public AdapterTeacherManagement.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        if (view == null) {
            throw new IllegalStateException("Layout resource is invalid or not found.");
        }
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AdapterTeacherManagement.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (listTeacher == null || position >= listTeacher.size()) return;

        Teachers teacher = listTeacher.get(position);
        if (teacher != null) {
            holder.txtNameStudent.setText(teacher.getName());
            holder.txtIdStudent.setText("Mã giảng viên: " + teacher.getCode());
            if (holder.txtNameClass != null) {
                holder.txtNameClass.setVisibility(View.GONE);
            }

            @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = inputFormat.parse(teacher.getBirthDate());
                @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                assert date != null;
                holder.txtDateOfBirth.setText("Ngày sinh: " + outputFormat.format(date));
            } catch (ParseException e) {
                holder.txtDateOfBirth.setText("Ngày sinh: N/A");
            }

            holder.txtAddress.setText("Địa chỉ: " + teacher.getAddress());
            holder.txtEmailStudent.setText("Email: " + teacher.getEmail());
            holder.txtPhoneStudent.setText("SĐT: " + teacher.getPhoneNumber());

            if(resource == R.layout.layout_custom_fragmment_student_teacher_management){
                holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Dialog dialog = new Dialog(view.getContext(), R.style.FullScreenDialog);
                        dialog.setContentView(R.layout.layout_dialog_add_edit_student_teacher_management);

                        TextView txtNameTitle = dialog.findViewById(R.id.txtNameTitle);
                        TextInputEditText edtCodeTeacher = dialog.findViewById(R.id.edtCodeStudent);
                        TextInputEditText edtNameTeacher = dialog.findViewById(R.id.edtNameStudent);
                        TextInputEditText edtAddress = dialog.findViewById(R.id.edtAddress);
                        TextInputEditText edtBirthDate = dialog.findViewById(R.id.edtBirthDate);
                        TextInputEditText edtEmail = dialog.findViewById(R.id.edtEmail);
                        TextInputEditText edtPhoneNumber = dialog.findViewById(R.id.edtPhoneNumber);
                        AutoCompleteTextView edtNameClass = dialog.findViewById(R.id.edtNameClass);

                        TextInputLayout layoutNameClass = dialog.findViewById(R.id.layoutNameClass);
                        TextInputLayout layoutNameTeacher = dialog.findViewById(R.id.layoutNameStudent);
                        TextInputLayout layoutCodeTeacher = dialog.findViewById(R.id.layoutCodeStudent);

                        txtNameTitle.setText("Sửa thông tin giáo viên");
                        layoutCodeTeacher.setHint("Mã giáo viên");
                        layoutNameTeacher.setHint("Tên giáo viên");


                        edtNameClass.setVisibility(View.GONE);
                        layoutNameClass.setVisibility(View.GONE);

                        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
                        Button btnCancel = dialog.findViewById(R.id.btnCancel);

                        //get date startR
                        final String[] BirthDate = {teacher.getBirthDate()};

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

                        edtCodeTeacher.setText(teacher.getCode());
                        edtNameTeacher.setText(teacher.getName());
                        edtAddress.setText(teacher.getAddress());
                        edtBirthDate.setText(teacher.getBirthDate());
                        edtEmail.setText(teacher.getEmail());
                        edtPhoneNumber.setText(teacher.getPhoneNumber());

                        btnSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String codeTeacher = Objects.requireNonNull(edtCodeTeacher.getText()).toString().trim();
                                String nameTeacher = Objects.requireNonNull(edtNameTeacher.getText()).toString().trim();
                                String address = Objects.requireNonNull(edtAddress.getText()).toString().trim();
                                String birthDate = Objects.requireNonNull(edtBirthDate.getText()).toString().trim();
                                String email = Objects.requireNonNull(edtEmail.getText()).toString().trim();
                                String phoneNumber = Objects.requireNonNull(edtPhoneNumber.getText()).toString().trim();


                                if (codeTeacher.isEmpty()) {
                                    edtCodeTeacher.setError("Vui lòng nhập mã sinh viên");
                                    edtCodeTeacher.requestFocus();
                                    return;
                                }

                                if (nameTeacher.isEmpty()) {
                                    edtNameTeacher.setError("Vui lòng nhập tên sinh viên");
                                    edtNameTeacher.requestFocus();
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

                                teacher.setCode(codeTeacher);
                                teacher.setName(nameTeacher);
                                teacher.setAddress(address);
                                teacher.setBirthDate(birthDate);
                                teacher.setEmail(email);
                                teacher.setPhoneNumber(phoneNumber);

                                new Teachers().addTeacher(teacher, position);
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
        return listTeacher.size();
    }

    public void addItem(Teachers s) {
        listTeacher.add(s);
        notifyItemInserted(listTeacher.size() - 1);
    }


    public void removeItem(int position) {
        listTeacher.remove(position);
        notifyItemRemoved(position);
    }

    public void undoItem(Teachers s, int position) {
        listTeacher.add(position, s);
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