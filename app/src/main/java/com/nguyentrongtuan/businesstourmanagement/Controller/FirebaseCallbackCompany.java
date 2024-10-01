package com.nguyentrongtuan.businesstourmanagement.Controller;

import com.nguyentrongtuan.businesstourmanagement.Models.Companies;
import java.util.List;

public interface FirebaseCallbackCompany {
    void onCallback(List<Companies> list);
}
