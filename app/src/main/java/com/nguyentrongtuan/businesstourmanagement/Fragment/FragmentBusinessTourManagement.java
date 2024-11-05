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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.nguyentrongtuan.businesstourmanagement.Controller.BusinessTourManagementController;
import com.nguyentrongtuan.businesstourmanagement.Interface.FirebaseCallbackCompany;
import com.nguyentrongtuan.businesstourmanagement.Interface.FirebaseCallbackTeacher;
import com.nguyentrongtuan.businesstourmanagement.Models.Companies;
import com.nguyentrongtuan.businesstourmanagement.Models.Students;
import com.nguyentrongtuan.businesstourmanagement.Models.Teachers;
import com.nguyentrongtuan.businesstourmanagement.Models.Tours;
import com.nguyentrongtuan.businesstourmanagement.R;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FragmentBusinessTourManagement extends Fragment {

    ProgressBar progressBar;
    Button btnAddTour;
    TextView txtNoTour;
    RecyclerView recyclerTourManagement;
    List<Teachers> listTeacher = new ArrayList<>();
    List<Companies> listCompany = new ArrayList<>();
    LinearLayout layoutForeground;
    BusinessTourManagementController businessTourManagementController;




    ArrayAdapter<Teachers> adapterTeacher;
    ArrayAdapter<Companies> adapterCompany;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_business_tour_management, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        btnAddTour = view.findViewById(R.id.btnAddTour);
        txtNoTour = view.findViewById(R.id.txtNoTour);
        recyclerTourManagement = view.findViewById(R.id.listTourManagement);
        layoutForeground = view.findViewById(R.id.layoutForeground);
        businessTourManagementController = new BusinessTourManagementController(getContext(), recyclerTourManagement);
        businessTourManagementController.getTourList(recyclerTourManagement);

        btnAddTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddTourDialog();
            }
        });
        return view;
    }

    private void showAddTourDialog() {
        // Apply custom full-screen style to dialog
        Dialog dialog = new Dialog(requireContext(), R.style.FullScreenDialog);
        dialog.setContentView(R.layout.layout_dialog_add_tour_with_business);

        TextInputEditText edtCodeTour = dialog.findViewById(R.id.edtCodeTour);
        TextInputEditText edtNameTour = dialog.findViewById(R.id.edtNameTour);
        TextInputEditText edtAvailable = dialog.findViewById(R.id.edtAvailable);
        TextInputEditText edtDescription = dialog.findViewById(R.id.edtDescription);
        TextInputEditText edtDateStat = dialog.findViewById(R.id.edtDateStat);
        AutoCompleteTextView edtNameTeacher = dialog.findViewById(R.id.edtNameTeacher);
        AutoCompleteTextView edtNameCompany = dialog.findViewById(R.id.edtNameCompany);

        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        final String[] codeTeacher = {""};
        Teachers teacher = new Teachers();
        teacher.getAllListTeacher(new FirebaseCallbackTeacher() {
            @Override
            public void onCallback(List<Teachers> list) {
                if (list != null && !list.isEmpty()) {
                    listTeacher .clear();
                    listTeacher.addAll(list);

                    ArrayAdapter<Teachers> adapter = new ArrayAdapter<>(requireContext(), R.layout.layout_custom_sprinner, listTeacher);
                    edtNameTeacher.setAdapter(adapter);

                    edtNameTeacher.setOnClickListener(v -> edtNameTeacher.showDropDown());

                    edtNameTeacher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Teachers selectedTeacher = (Teachers) listTeacher.get(i);
                            codeTeacher[0] = selectedTeacher.getCode();
                        }
                    });
                    edtNameTeacher.setText(list.get(0).getName(), false);
                }
            }
        });


        final String[] codeCompany = {""};
        Companies company = new Companies();
        company.getAllListCompany(new FirebaseCallbackCompany() {
            @Override
            public void onCallback(List<Companies> list) {
                if (list != null && !list.isEmpty()) {
                    listCompany.clear();
                    listCompany.addAll(list);

                    ArrayAdapter<Companies> adapter = new ArrayAdapter<>(requireContext(), R.layout.layout_custom_sprinner, listCompany);
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

                    // Đặt tên của công ty đầu tiên vào AutoCompleteTextView nếu danh sách không rỗng
                    edtNameCompany.setText(list.get(0).getName(), false);
                }
            }
        });

        //get date start
        final String[] dateStart = {""};
        edtDateStat.setOnClickListener(new View.OnClickListener() {
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

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                List<Students>  listStudent = new ArrayList<>();
                Log.d("Login","123 after: " + dateStart[0]);
                Tours tourAdd = new Tours(businessTourManagementController.getSizeList(), codeTour, available,
                        codeTeacher[0], codeCompany[0], description, nameTour,dateStart[0], listStudent);

                edtNameCompany.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        // Kiểm tra xem vị trí 'i' có nằm trong phạm vi của listTeacher hay không
                        if (i >= 0 && i < listTeacher.size()) {
                            String codeCompany = listTeacher.get(i).getCode();
                            if (codeCompany != null) {
                                Log.d("Login", codeCompany);
                                tourAdd.setIdCompany(codeCompany);
                            } else {
                                Log.d("Login", "Code of the selected teacher is null");
                            }
                        } else {
                            Log.d("Login", "Invalid index for listTeacher: " + i);
                        }
                    }
                });




                if(businessTourManagementController.checkCodeTour(codeTour)){
                    edtCodeTour.setError("Mã tour đã trùng vui lòng nhập lại!");
                    edtCodeTour.requestFocus();
                    return;
                }else{
                    businessTourManagementController.addItemAdapter(tourAdd);
                    recyclerTourManagement.scrollToPosition((int)businessTourManagementController.getSizeList()-1);
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
        date = year + "-" + month + "-" + day;
        date = year + "-" + month + "-" + day;

        return date;
    }

    private long checkErrorNumber(String s) {
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Vui lòng nhập số vào số lượng sinh viên !", Toast.LENGTH_SHORT).show();
            return -1;
        }
    }

}
