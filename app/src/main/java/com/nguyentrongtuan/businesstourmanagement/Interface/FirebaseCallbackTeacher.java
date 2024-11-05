package com.nguyentrongtuan.businesstourmanagement.Interface;

import com.nguyentrongtuan.businesstourmanagement.Models.Teachers;

import java.util.List;

public interface FirebaseCallbackTeacher {
    void onCallback(List<Teachers> list);
}
