package com.nguyentrongtuan.businesstourmanagement.View;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ComponentActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.nguyentrongtuan.businesstourmanagement.Fragment.FragmentHome;
import com.nguyentrongtuan.businesstourmanagement.R;

public class MenuAdminActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {

    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_BusinessTourManagement = 1;
    private static final int StudentInformationManagement = 2;
    private static final int FRAGMENT_TeacherInformationManagement = 3;
    private static final int FRAGMENT_BusinessInformationManagement = 4;
    private static final int FRAGMENT_ClassroomInformationManagement = 5;
    private static final int FRAGMENT_SystemManagement = 6;

    private int mCurrentFragment = FRAGMENT_HOME;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_menu_admin);

        Toolbar toolbarAdmin = findViewById(R.id.toolbar);
        setSupportActionBar(toolbarAdmin);

        drawerLayout = findViewById(R.id.drawer_layout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.naviDrawerOpen, R.string.naviDrawerClose);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // Hiển thị biểu tượng menu 3 sọc
        actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.iconmenu);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Cho phép nút 3 sọc thay thế nút home trên thanh công cụ
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(new FragmentHome());
        navigationView.getMenu().findItem(R.id.home).setChecked(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Xử lý sự kiện khi bấm vào biểu tượng 3 sọc
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.home){
            if(mCurrentFragment != FRAGMENT_HOME){
                replaceFragment(new FragmentHome());
                mCurrentFragment = FRAGMENT_HOME;
            }
        };
//        switch (item.getItemId()){
//            case R.id.home:
//                if(mCurrentFragment != FRAGMENT_HOME){
//                    replaceFragment(new FragmentHome());
//                    mCurrentFragment = FRAGMENT_HOME;
//                }
//                break;
//            case R.id.BusinessTourManagement:
//                break;
//            case R.id.StudentInformationManagement:
//                break;
//            case R.id.TeacherInformationManagement:
//                break;
//            case R.id.BusinessInformationManagement:
//                break;
//            case R.id.ClassroomInformationManagement:
//                break;
//            case R.id.SystemManagement:
//                break;
//        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else
            super.onBackPressed();
    }

    private void  replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }
}
