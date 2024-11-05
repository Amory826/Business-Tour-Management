// FirebaseCallback.java
package com.nguyentrongtuan.businesstourmanagement.Interface;

import java.util.List;
import com.nguyentrongtuan.businesstourmanagement.Models.Tours;

public interface FirebaseCallbackTours {
    void onCallback(List<Tours> list);
}
