package com.example.quanlysinhvien;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlysinhvien.database.DatabaseHelper;
import com.example.quanlysinhvien.home.HomeActivity;
import com.example.quanlysinhvien.model.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String DB_PATH_SUFFIX = "/databases/";
    String DATABASE_NAME="quan_ly_sinh_vien.db";
    SQLiteDatabase database=null;
    DatabaseHelper dbHelper;
    Button btn_login;
    TextView txt_register;
    EditText edtUsername;
    EditText edtPassword;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_login = findViewById(R.id.btn_login);
        txt_register = findViewById(R.id.txt_register);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        dbHelper = new DatabaseHelper(getApplicationContext());
        processCopy();

        //Chuyển sang trang chủ quản lý sinh viên
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                if(username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Yêu cầu nhập đầy đủ thông tin", Toast.LENGTH_LONG).show();
                }else{
                    if(username.length() < 4 || password.length() < 4) {
                        Toast.makeText(MainActivity.this, "Username hoặc Passowrd phải lớn hơn 4 ký tự", Toast.LENGTH_LONG).show();
                    }else{
                        if(dbHelper.checkUser(username,password)) {
                            if(dbHelper.checkRole(username)==0) {
                                Toast.makeText(MainActivity.this, "Login thành công", Toast.LENGTH_LONG).show();
                                Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                                homeIntent.putExtra("user_name", username);
                                startActivity(homeIntent);
                            }
                            if(dbHelper.checkRole(username)==1) {
                                Toast.makeText(MainActivity.this, "Login thành công", Toast.LENGTH_LONG).show();
                                Intent userIntent = new Intent(MainActivity.this, UserActivity.class);
                                userIntent.putExtra("user_name", username);
                                userIntent.putExtra("password", password);

                                startActivity(userIntent);
                            }
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Login thất bại", Toast.LENGTH_LONG).show();
                        }
                    }

                }
            }
        });


        //Chuyển sang trang đăng ký
        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(myintent);
            }
        });
    }

    private void processCopy() {
        //pri p vate app
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists())
        {
            try{CopyDataBaseFromAsset();
                Toast.makeText(this, "Copying sucess from Assets folder", Toast.LENGTH_LONG).show();
            }
            catch (Exception e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }

    }
    public File getDatabasePath(String DATABASE_NAME) {
        return new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX + this.DATABASE_NAME);
    }
    public void CopyDataBaseFromAsset() {
        // TODO Auto-generated method stub
        try {
            InputStream myInput;
            myInput = getAssets().open(DATABASE_NAME);
            // Path to the just created empty db
            File outFileName = getDatabasePath(DATABASE_NAME);
            // if the path doesn't exist first, create it
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists())
                f.mkdir();
            // Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);
            // transfer bytes from the inputfile to t@he outputfile
            // Truyền bytes dữ liệu từ input đến output
            int size = myInput.available();
            byte[] buffer = new byte[size];
            myInput.read(buffer);
            myOutput.write(buffer);
            // Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}


