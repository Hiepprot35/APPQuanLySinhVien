package com.example.quanlysinhvien;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlysinhvien.database.DatabaseHelper;
import com.example.quanlysinhvien.function.Function_user;
import com.example.quanlysinhvien.home.ChangePasswordActivity;
import com.example.quanlysinhvien.home.HomeActivity;
import com.example.quanlysinhvien.model.Student;
import com.example.quanlysinhvien.student.StudentActivity;
import com.example.quanlysinhvien.student.UpdateStudent;

import java.io.IOException;
import java.io.InputStream;

public class UserActivity extends AppCompatActivity {


    DatabaseHelper dbHelper;
    TextView edtgender_update, edtphone_update, edtEmailSV_update, edtcountry_update, edtnameSV_update,edtmaSV_update,txt_major_user,txt_class_user;
    byte[] avatarData;
    ImageView avatarStudent;
    Button changepass;
    TextView edtbirthday_update,txtuser_update;
    Function_user function_user;
    Student student;
    private static final int PICK_IMAGE_UPDATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        function_user = new Function_user();
        dbHelper = new DatabaseHelper(getApplicationContext());
        edtbirthday_update = findViewById(R.id.edtbirthday_user);
        txt_class_user=findViewById(R.id.txt_class_user);
        txt_major_user=findViewById(R.id.txt_major_user);
        edtcountry_update = findViewById(R.id.edtcountry_user);
        avatarStudent = findViewById(R.id.img_avatar_user);
        edtphone_update = findViewById(R.id.edtphone_user);
        edtEmailSV_update = findViewById(R.id.edtEmailSV_user);
        edtnameSV_update = findViewById(R.id.edtnameSV_user);
        edtmaSV_update = findViewById(R.id.edtmaSV_user);
        edtgender_update = findViewById(R.id.edtgender_user);
        txtuser_update = findViewById(R.id.txtuser_update);
        Intent intent = getIntent();
        if (intent != null) {
            String studentId = intent.getStringExtra("user_name");
            String password = intent.getStringExtra("password");

//            Student student;

            student = dbHelper.getStudentBy("id", studentId);
            changepass = findViewById(R.id.btn_user_student);
            edtcountry_update.setText("Quê quán: " + student.getCountry());
            edtbirthday_update.setText("Ngày sinh: " + student.getDate_of_birth());
            edtphone_update.setText("SĐT: " + student.getPhone_number());
            edtEmailSV_update.setText("Email: " + student.getEmail());
            edtnameSV_update.setText("Họ và tên: " + student.getFullname());
            edtmaSV_update.setText("MSSV: " + student.getId());
            txt_major_user.setText("Ngành: "+dbHelper.getByTableColumn("major","id","major_name",student.getMajor_id()));
            txt_class_user.setText("Lớp: "+dbHelper.getByTableColumn("classes","id","classs_name",student.getClass_id()));
            edtgender_update.setText("Giới tính: "+ student.getGender());
            txtuser_update.setText(student.getFullname());
//            txtuser_update.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    showOptions();
//                }
//            });

//            avatarData = student.getAvatar();
//            Bitmap bitmap = BitmapFactory.decodeByteArray(student.getAvatar(), 0, student.getAvatar().length);
//            avatarStudent.setImageBitmap(bitmap);

            changepass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(UserActivity.this, ChangePasswordActivity.class);
                    intent.putExtra("user_name", student.getId());
//                    intent.putExtra("password", student.getPhone_number());
                    startActivity(intent);

                }
            });
        }
    }
//    private void showOptions() {
//        PopupMenu popupMenu = new PopupMenu(UserActivity.this, txtuser_update);
//        popupMenu.getMenuInflater().inflate(R.menu.pop_menu, popupMenu.getMenu());
//        popupMenu.setGravity(Gravity.END);
//
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                if(item.getItemId() == R.id.menu_update) {
//                    updateUserAdmin();
//                    return true;
//                }
//                if(item.getItemId() == R.id.menu_logout) {
//                    LogoutUserAdmin(); return true;
//                }
//                return false;
//            }
//
//        });
//
//        popupMenu.show();
//    }
//
//    private void updateUserAdmin() {
//        String username = txtuser_update.getText().toString();
//        Intent intent=new Intent(UserActivity.this, ChangePasswordActivity.class);
//        intent.putExtra("user_name", username);
//        startActivity(intent);
//    }
//
//    private void LogoutUserAdmin() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
//        builder.setTitle("Xác nhận đăng xuất");
//        builder.setMessage("Bạn có chắc chắn muốn đăng xuất ?");
//        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                finish();
//            }
//        });
//        builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }


}