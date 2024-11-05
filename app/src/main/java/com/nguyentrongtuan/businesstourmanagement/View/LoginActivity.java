package com.nguyentrongtuan.businesstourmanagement.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;
import com.nguyentrongtuan.businesstourmanagement.Models.Accounts;
import com.nguyentrongtuan.businesstourmanagement.R;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText txtUser, txtPassword;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("tbl_account");

        btnLogin = findViewById(R.id.btnLogin);
        txtUser = findViewById(R.id.txtUserName);
        txtPassword = findViewById(R.id.txtPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String username = txtUser.getText().toString().trim(); // Sử dụng trim để loại bỏ khoảng trắng
                    String password = txtPassword.getText().toString().trim();

                    // Kiểm tra tài khoản hoặc mật khẩu có để trống không
                    if (username.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Tài khoản không được để trống!", Toast.LENGTH_SHORT).show();
                    } else if (password.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Mật khẩu không được để trống!", Toast.LENGTH_SHORT).show();
                    } else {
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int dem = 0;
                                Accounts acc = new Accounts();
                                Iterable<DataSnapshot> node = snapshot.getChildren();
                                for (DataSnapshot child : node) {
                                    acc = child.getValue(Accounts.class);
                                    assert acc != null;
                                    if(username.equals(acc.getUsername()) && password.equals(acc.getPassword())){
                                        dem ++;
                                        break;
                                    }
                                }

                                if(dem == 0){
                                    Toast.makeText(getApplicationContext(), "Tài khoản hoặc mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                                }else{
                                    switch (acc.getRole()){
                                        case "student":
                                            Intent iLoginStudent = new Intent(LoginActivity.this, MenuStudentActivity.class);
                                            startActivity(iLoginStudent);
                                            break;
                                        case "teacher":
                                            Intent iLoginTeacher = new Intent(LoginActivity.this, MenuTeacherActivity.class);
                                            startActivity(iLoginTeacher);
                                            break;
                                        case "admin":
                                            Intent iLoginAdmin = new Intent(LoginActivity.this, MenuAdminActivity.class);
                                            startActivity(iLoginAdmin);
                                            break;
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("FirebaseError", "Lỗi truy xuất dữ liệu", error.toException());
                            }
                        });
                    }

                } catch (Exception ex) {
                    Log.e("LoginError", "Lỗi xảy ra khi đăng nhập", ex);
                }
            }
        });
    }
}
