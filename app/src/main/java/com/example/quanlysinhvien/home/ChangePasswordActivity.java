package com.example.quanlysinhvien.home;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlysinhvien.R;
import com.example.quanlysinhvien.database.DatabaseHelper;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText edt_now_password, edt_new_password;
    Button btn_save;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        edt_now_password = findViewById(R.id.edt_now_password);
        edt_new_password = findViewById(R.id.edt_new_password);
        btn_save = findViewById(R.id.btn_save);
        updatePassword();

    }

    private void updatePassword() {
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper = new DatabaseHelper(getApplicationContext());
                String now_password = edt_now_password.getText().toString();
                String new_password = edt_new_password.getText().toString();
                String msg = "";
                if(now_password.isEmpty() || new_password.isEmpty()) {
                    msg = "Yêu cầu nhập đầy đủ thông tin";
                }else{
                    if(!checkPassword(now_password)) {
                        msg = "Yêu cầu nhập đúng mật khẩu hiện tại";
                    }else{
                        if(new_password.length() < 4) {
                            msg = "Mật khẩu phải lớn hơn 4 ký tự";
                        }else{
                            if(changePassword(new_password)) {
                                msg = "Thay đổi mật khẩu thành công";
                                finish();
                            }
                        }
                    }
                }
                Toast.makeText(ChangePasswordActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean changePassword(String newPassword) {
        Intent intent = getIntent();
        String username = intent.getStringExtra("user_name");
        SQLiteDatabase db = openOrCreateDatabase("quan_ly_sinh_vien.db",MODE_PRIVATE,null);
        ContentValues values = new ContentValues();
        values.put("password",newPassword);
        db.update("user",values,"username = ?",new String[]{username});
        return true;
    }

    private boolean checkPassword(String now_password) {
        Intent intent = getIntent();
        String username = intent.getStringExtra("user_name");
        SQLiteDatabase db = openOrCreateDatabase("quan_ly_sinh_vien.db",MODE_PRIVATE,null);

        String query = "SELECT password FROM user WHERE username = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        if (cursor.moveToFirst()) {
            String currentPassword = cursor.getString(0);
            cursor.close();

            // Kiểm tra xem mật khẩu cũ nhập vào có khớp với mật khẩu hiện tại trong cơ sở dữ liệu
            return currentPassword.equals(now_password);
        }

        cursor.close();
        return false;
    }
}