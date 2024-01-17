package com.example.quanlysinhvien.home;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.quanlysinhvien.InfoActivity;
import com.example.quanlysinhvien.R;
import com.example.quanlysinhvien.classes.ClassesActivity;
import com.example.quanlysinhvien.database.DatabaseHelper;
import com.example.quanlysinhvien.major.MajorActivity;
import com.example.quanlysinhvien.student.StudentActivity;
import com.example.quanlysinhvien.subject.SubjectActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    HomeAdapter homeAdapter;
    List<Home> myhome;
    String name_home [] = {"Quản lý lớp","Quản lý sinh viên","Quản lý môn học","Quản lý ngành","Thông tin","Đăng xuất"};
    int img_home [] = {R.drawable.sv,R.drawable.sv,R.drawable.book,R.drawable.sv,R.drawable.info,R.drawable.logout};
    GridView gv_home;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    TextView txt_useradmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        myhome = new ArrayList<>();
        dbHelper = new DatabaseHelper(getApplicationContext());
        txt_useradmin = findViewById(R.id.txt_useradmin);
        for(int i = 0; i < name_home.length; i++) {
            myhome.add(new Home(img_home[i],name_home[i]));
        }
        gv_home = findViewById(R.id.gv_home);
        homeAdapter = new HomeAdapter(this,myhome);
        gv_home.setAdapter(homeAdapter);
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myintent;
                if(Objects.equals(name_home[position], name_home[0])) {
                    myintent = new Intent(HomeActivity.this, ClassesActivity.class);
                    startActivity(myintent);
                }
                if(Objects.equals(name_home[position], name_home[1])) {
                    myintent = new Intent(HomeActivity.this, StudentActivity.class);
                    startActivity(myintent);
                }
                if(Objects.equals(name_home[position], name_home[2])) {
                    myintent = new Intent(HomeActivity.this, SubjectActivity.class);
                    startActivity(myintent);
                }
                if(Objects.equals(name_home[position], name_home[3])) {
                    myintent = new Intent(HomeActivity.this, MajorActivity.class);
                    startActivity(myintent);
                }
                if(Objects.equals(name_home[position], name_home[4])) {
                    myintent = new Intent(HomeActivity.this, InfoActivity.class);
                    startActivity(myintent);
                }

            }
        });
        showUserName();
        txt_useradmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptions();
            }
        });

    }

    private void showOptions() {
        PopupMenu popupMenu = new PopupMenu(HomeActivity.this, txt_useradmin);
        popupMenu.getMenuInflater().inflate(R.menu.pop_menu, popupMenu.getMenu());
        popupMenu.setGravity(Gravity.END);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.menu_update) {
                    updateUserAdmin();
                    return true;
                }
                if(item.getItemId() == R.id.menu_logout) {
                    LogoutUserAdmin(); return true;
                }
                return false;
            }

        });

        popupMenu.show();
    }

    private void LogoutUserAdmin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle("Xác nhận đăng xuất");
        builder.setMessage("Bạn có chắc chắn muốn đăng xuất ?");
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Đóng hộp thoại
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateUserAdmin() {
        String username = txt_useradmin.getText().toString();
        Intent myintent = new Intent(HomeActivity.this, ChangePasswordActivity.class);
        myintent.putExtra("user_name", username);
        startActivity(myintent);
    }

    private void showUserName() {
        Intent intent = getIntent();
        String username = intent.getStringExtra("user_name");
        txt_useradmin.setText(username);
    }
}