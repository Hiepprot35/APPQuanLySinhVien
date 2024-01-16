package com.example.quanlysinhvien.home;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
//                String msg = "";
                if(now_password.isEmpty() || new_password.isEmpty()) {
                    Toast.makeText(ChangePasswordActivity.this,"Yêu cầu nhập đầy đủ thông tin",Toast.LENGTH_SHORT).show();
                }else{
                    if(!checkPassword(now_password)) {

                        Toast.makeText(ChangePasswordActivity.this,"Yêu cầu nhập đúng mật khẩu hiện tại",Toast.LENGTH_SHORT).show();
                    }else{
                        if(new_password.length() < 4) {
                            Toast.makeText(ChangePasswordActivity.this,"Mật khẩu phải lớn hơn 4 ký tự",Toast.LENGTH_SHORT).show();
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this);
                            builder.setTitle("Xác nhận đổi mật khẩu");
                            builder.setMessage("Bạn có chắc chắn muốn đổi mật khẩu ?");
                            builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(changePassword(new_password)) {
                                        Toast.makeText(ChangePasswordActivity.this,"Thay đổi mật khẩu thành công",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
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
                    }
                }
//                Toast.makeText(ChangePasswordActivity.this,msg,Toast.LENGTH_SHORT).show();
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
            Log.d("input",now_password);
            Log.d("now_input",currentPassword);
            // Kiểm tra xem mật khẩu cũ nhập vào có khớp với mật khẩu hiện tại trong cơ sở dữ liệu
            return currentPassword.equals(now_password);
        }

        cursor.close();
        return false;
    }
}