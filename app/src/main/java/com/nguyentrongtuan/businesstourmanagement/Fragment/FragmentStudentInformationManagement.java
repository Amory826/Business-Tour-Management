package com.nguyentrongtuan.businesstourmanagement.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.nguyentrongtuan.businesstourmanagement.Controller.StudentManagementController;
import com.nguyentrongtuan.businesstourmanagement.Interface.FirebaseCallbackClass;
import com.nguyentrongtuan.businesstourmanagement.Interface.FirebaseCallbackTeacher;
import com.nguyentrongtuan.businesstourmanagement.Models.Class;
import com.nguyentrongtuan.businesstourmanagement.Models.Students;
import com.nguyentrongtuan.businesstourmanagement.Models.Teachers;
import com.nguyentrongtuan.businesstourmanagement.R;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FragmentStudentInformationManagement extends Fragment {

    RecyclerView recyclerView;
    TextView txtNameTitle, txtNoTour;
    Button btnAddStudent;
    ProgressBar progressBar;
    StudentManagementController studentManagementController;
    List<Class> listClass = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_student_information_management, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        btnAddStudent = view.findViewById(R.id.btnAddStudent);
        txtNoTour = view.findViewById(R.id.txtNoTour);
        txtNameTitle = view.findViewById(R.id.txtNameTitle);
        recyclerView = view.findViewById(R.id.recyclerView);

        studentManagementController = new StudentManagementController(getContext(), R.layout.layout_custom_fragmment_student_teacher_management);

        studentManagementController.getStudentList(recyclerView, progressBar, txtNoTour);

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

        final String[] codeClass = {""};
        Class aClass = new Class();
        aClass.getAllClass(new  FirebaseCallbackClass() {
            @Override
            public void onCallback(List<Class> list) {
                if (list != null && !list.isEmpty()) {
                    listClass .clear();
                    listClass.addAll(list);

                    ArrayAdapter<Class> adapter = new ArrayAdapter<>(requireContext(), R.layout.layout_custom_sprinner, listClass);
                    edtNameClass.setAdapter(adapter);

                    edtNameClass.setOnClickListener(v -> edtNameClass.showDropDown());

                    edtNameClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Class selectedClass = (Class) listClass.get(i);
                            codeClass[0] = selectedClass.getCode();
                        }
                    });
//                    edtNameTeacher.setText(list.get(0).getName(), false);
                }
            }
        });

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

                String addressImage = "/images/john.jpg";
                Students studentAdd = new Students(studentManagementController.getSizeList(), 1, codeStudent, BirthDate[0],
                                                phoneNumber, addressImage, email, nameStudent, codeClass[0], address);

                if(studentManagementController.checkCodeTour(codeStudent)){
                    edtCodeStudent.setError("Mã sunh viên đã trùng vui lòng nhập lại!");
                    edtCodeStudent.requestFocus();
                    return;
                }else{
                    studentManagementController.addItemAdapter(studentAdd);
                    recyclerView.scrollToPosition((int)studentManagementController.getSizeList()-1);
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
