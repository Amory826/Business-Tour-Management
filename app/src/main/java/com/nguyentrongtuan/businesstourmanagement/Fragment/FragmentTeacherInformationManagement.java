package com.nguyentrongtuan.businesstourmanagement.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.nguyentrongtuan.businesstourmanagement.Controller.TeacherManagementController;
import com.nguyentrongtuan.businesstourmanagement.Models.Teachers;
import com.nguyentrongtuan.businesstourmanagement.R;

import org.jetbrains.annotations.Contract;
import java.util.Objects;


public class FragmentTeacherInformationManagement extends Fragment {
    RecyclerView recyclerView;
    TextView txtNameTitle, txtNoTour;
    Button btnAddStudent;
    ProgressBar progressBar;
    TeacherManagementController teacherManagementController;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_student_teacher_information_management, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        btnAddStudent = view.findViewById(R.id.btnAddStudent);
        txtNoTour = view.findViewById(R.id.txtNoTour);
        txtNameTitle = view.findViewById(R.id.txtNameTitle);
        recyclerView = view.findViewById(R.id.recyclerView);

        txtNameTitle.setText("Quản lý giáo viên");

        teacherManagementController = new TeacherManagementController(getContext(), R.layout.layout_custom_fragmment_student_teacher_management);

        teacherManagementController.getTeacherList(recyclerView, progressBar, txtNoTour);

        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        return view;
    }

    private void showDialog(){
        Dialog dialog = new Dialog(requireContext(), R.style.FullScreenDialog);
        dialog.setContentView(R.layout.layout_dialog_add_edit_student_teacher_management);

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


        layoutCodeTeacher.setHint("Mã giáo viên");
        layoutNameTeacher.setHint("Tên giáo viên");


        edtNameClass.setVisibility(View.GONE);
        layoutNameClass.setVisibility(View.GONE);

        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        //get date startR
        final String[] BirthDate = {""};
        edtBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialogDate = new Dialog(requireContext(), R.style.FullScreenDialog);
                dialogDate.setContentView(R.layout.layout_custom_datepicker);

                Button btnSubmit = dialogDate.findViewById(R.id.btnSubmit);
                Button btnCancel = dialogDate.findViewById(R.id.btnCancel);
                DatePicker datePicker = dialogDate.findViewById(R.id.datePicker);

                dialogDate.show();

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String date = getDatePicker(datePicker);

                        BirthDate[0] = String.valueOf(datePicker.getYear()) + "-"
                                + String.valueOf(datePicker.getMonth() + 1) + "-" + String.valueOf(datePicker.getDayOfMonth());
                        edtBirthDate.setText(date);
                        dialogDate.dismiss();
                        Log.d("Login","123" + BirthDate[0]);
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

                String addressImage = "/images/john.jpg";
                Teachers teacherAdd = new Teachers(teacherManagementController.getSizeList(), 1, codeTeacher, BirthDate[0],
                        phoneNumber, addressImage, email, nameTeacher, address);

                if(teacherManagementController.checkCodeTour(codeTeacher)){
                    edtCodeTeacher.setError("Mã sinh viên đã trùng vui lòng nhập lại!");
                    edtCodeTeacher.requestFocus();
                    return;
                }else{
                    teacherManagementController.addItemAdapter(teacherAdd);
                    recyclerView.scrollToPosition((int)teacherManagementController.getSizeList()-1);
                }
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

    @NonNull
    @Contract(pure = true)
    private String getDatePicker(DatePicker datePicker){
        String date = "";
        String day, month, year;
        day = String.valueOf(datePicker.getDayOfMonth());
        month = String.valueOf(datePicker.getMonth() + 1);
        year = String.valueOf(datePicker.getYear());
        date =  day + "-" + month + "-" + year;

        return date;
    }

    // Hàm kiểm tra số điện thoại hợp lệ
    public boolean isValidPhoneNumber(String phoneNumber) {
        // Regex kiểm tra số điện thoại hợp lệ
        String regex = "^(\\+84|0)[1-9][0-9]{8,9}$";
        return phoneNumber.matches(regex);
    }

}