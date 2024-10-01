package com.nguyentrongtuan.businesstourmanagement.Controller;

import com.nguyentrongtuan.businesstourmanagement.Models.Class;

import java.util.List;

public interface FirebaseCallbackClass {
    void onCallback(List<Class> list);
}
