package com.example.quanlysinhvien;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlysinhvien.database.DatabaseHelper;
import com.example.quanlysinhvien.model.User;

public class RegisterActivity extends AppCompatActivity {
    SQLiteDatabase db;
    DatabaseHelper dbHelper;
    Button btn_register;
    TextView txt_back;
    EditText edtUsername, edtPassword, edtConfrim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btn_register = findViewById(R.id.btn_register);
        txt_back = findViewById(R.id.txt_back);
        edtUsername = findViewById(R.id.edtUsername_register);
        edtPassword = findViewById(R.id.edtPassword_register);
        edtConfrim = findViewById(R.id.edt_confirm);
        btn_register = findViewById(R.id.btn_register);
        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        register();
    }

    private void register() {
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper = new DatabaseHelper(getApplicationContext());
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                String confim = edtConfrim.getText().toString();
                String msg = "";
                if(username.isEmpty() || password.isEmpty())
                    msg = "Nhap day du thong tin";
                else{
                    if(username.length() < 4 || password.length() < 4) {
                        Toast.makeText(RegisterActivity.this, "Username hoặc Passowrd phải lớn hơn 4 ký tự", Toast.LENGTH_LONG).show();
                    }else{
                        if(dbHelper.isUsernameExists(username)){
                            msg = "Username đã tồn tại";
                        }else{
                            if(confim.equals(password)) {
                                User user = new User(username,password);
                                dbHelper.register_user(user);
                                msg = "Đăng ký thành công";
                                finish();
                            }else{
                                msg = "Yêu cầu nhập lại đúng mật khẩu";
                            }
                        }
                    }
                }
                Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

    }


}