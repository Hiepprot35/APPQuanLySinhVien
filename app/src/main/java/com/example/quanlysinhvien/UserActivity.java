package com.example.quanlysinhvien;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlysinhvien.database.DatabaseHelper;
import com.example.quanlysinhvien.function.Function_user;
import com.example.quanlysinhvien.home.ChangePasswordActivity;
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
    TextView edtbirthday_update;
    Function_user function_user;
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
        Intent intent = getIntent();
        if (intent != null) {
            String studentId = intent.getStringExtra("user_name");
            String password = intent.getStringExtra("password");

            Student student;

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
//            avatarData = student.getAvatar();
//            Bitmap bitmap = BitmapFactory.decodeByteArray(student.getAvatar(), 0, student.getAvatar().length);
//            avatarStudent.setImageBitmap(bitmap);

            changepass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(UserActivity.this, ChangePasswordActivity.class);
                    intent.putExtra("user_name", student.getId());
                    intent.putExtra("password", student.getId());
                    startActivity(intent);

                }
            });
        }

    }


}