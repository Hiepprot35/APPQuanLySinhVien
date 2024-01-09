package com.example.quanlysinhvien.student;


import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.quanlysinhvien.R;
import com.example.quanlysinhvien.database.DatabaseHelper;
import com.example.quanlysinhvien.function.Function_user;
import com.example.quanlysinhvien.model.Student;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

public class UpdateStudent extends AppCompatActivity {
    DatabaseHelper dbHelper;
    EditText edtphone_update, edtEmailSV_update, edtcountry_update, edtnameSV_update,edtmaSV_update;
    byte[] avatarData;
    ImageView avatarStudent;
    Button updateStudent;
    Button edtbirthday_update;
    Function_user function_user;
    private static final int PICK_IMAGE_UPDATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_student);
        function_user=new Function_user();
        dbHelper=new DatabaseHelper(getApplicationContext());
        edtbirthday_update=findViewById(R.id.edtbirthday_update);
        edtcountry_update = findViewById(R.id.edtcountry_update);
        avatarStudent=findViewById(R.id.img_avatar_update);
        edtphone_update = findViewById(R.id.edtphone_update);
        edtEmailSV_update = findViewById(R.id.edtEmailSV_update);
        edtnameSV_update = findViewById(R.id.edtnameSV_update);
        edtmaSV_update = findViewById(R.id.edtmaSV_update);
        Intent intent = getIntent();
        if (intent != null) {
            String studentId = intent.getStringExtra("student_id");
            Student student;

            student=dbHelper.getStudentBy("id",studentId);
            updateStudent=findViewById(R.id.btn_update_student);
            edtcountry_update.setText(student.getCountry());
            edtbirthday_update.setText (student.getDate_of_birth());
            edtphone_update.setText(student.getPhone_number());
            edtEmailSV_update.setText(student.getEmail());
            edtnameSV_update.setText(student.getFullname());
            edtmaSV_update.setText( student.getId());
            avatarData=student.getAvatar();
            Bitmap bitmap = BitmapFactory.decodeByteArray(student.getAvatar(), 0, student.getAvatar().length);
            avatarStudent.setImageBitmap(bitmap);

        }
        edtbirthday_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            function_user.openDatePicker(v,edtbirthday_update,UpdateStudent.this);
            }
        });
        avatarStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, PICK_IMAGE_UPDATE);
            }
        });
        updateStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname = edtnameSV_update.getText().toString();
                String date = edtbirthday_update.getText().toString();
                String number_phone = edtphone_update.getText().toString();
                String country = edtcountry_update.getText().toString();
                String email = edtEmailSV_update.getText().toString();
                String id = edtmaSV_update.getText().toString();
                if (avatarData == null || fullname.isEmpty() || date.isEmpty()||number_phone.isEmpty()||country.isEmpty()
                        || id.isEmpty()
                ) {
                    Toast.makeText(UpdateStudent.this, "Không được để trống thông tin", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Student updateStudent_data = new Student(id, fullname, email, country, null, number_phone, date, null, null, null, avatarData);
                     dbHelper.update_Student(updateStudent_data);
                        Intent intent_addsv = new Intent(UpdateStudent.this, StudentActivity.class);
                        startActivity(intent_addsv);
                        Toast.makeText(UpdateStudent.this, "Cập nhật thành công", Toast.LENGTH_LONG).show();

                }

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Function_user functionUser = new Function_user();
        if (requestCode == PICK_IMAGE_UPDATE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            android.net.Uri selectedImageUri = data.getData();
            try{
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                avatarData= functionUser.resizeImage(inputStream, 180, 180)  ;
                Log.d("Update new","Cap nhat");
                avatarStudent.setImageURI(selectedImageUri);

            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}