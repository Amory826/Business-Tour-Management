package com.nguyentrongtuan.businesstourmanagement.Controller;

import com.nguyentrongtuan.businesstourmanagement.Models.Students;

import java.util.List;

public interface FirebaseCallbackStudent {
    void onCallback(List<Students> list);
}
