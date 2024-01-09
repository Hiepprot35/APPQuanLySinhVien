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
import com.example.quanlysinhvien.model.Student;
import com.example.quanlysinhvien.student.StudentActivity;
import com.example.quanlysinhvien.student.UpdateStudent;

import java.io.IOException;
import java.io.InputStream;

public class UserActivity extends AppCompatActivity {


    DatabaseHelper dbHelper;
    TextView edtphone_update, edtEmailSV_update, edtcountry_update, edtnameSV_update,edtmaSV_update;
    byte[] avatarData;
    ImageView avatarStudent;
    Button updateStudent;
    Button edtbirthday_update;
    Function_user function_user;
    private static final int PICK_IMAGE_UPDATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        function_user=new Function_user();
        dbHelper=new DatabaseHelper(getApplicationContext());
        edtbirthday_update=findViewById(R.id.edtbirthday_user);
        edtcountry_update = findViewById(R.id.edtcountry_user);
        avatarStudent=findViewById(R.id.img_avatar_user);
        edtphone_update = findViewById(R.id.edtphone_user);
        edtEmailSV_update = findViewById(R.id.edtEmailSV_user);
        edtnameSV_update = findViewById(R.id.edtnameSV_user);
        edtmaSV_update = findViewById(R.id.edtmaSV_user);
        Intent intent = getIntent();
        if (intent != null) {
            String studentId=intent.getStringExtra("user_name");
            Student student;

            student=dbHelper.getStudentBy("phone_number",studentId);
            updateStudent=findViewById(R.id.btn_user_student);
            edtcountry_update.setText("Quê quán: " +student.getCountry());
            edtbirthday_update.setText("Ngày sinh: "+student.getDate_of_birth());
            edtphone_update.setText("SĐT: "+ student.getPhone_number());
            edtEmailSV_update.setText("Email: "+student.getEmail());
            edtnameSV_update.setText("Họ và tên: "+student.getFullname());
            edtmaSV_update.setText("MSSV: "+student.getId());
            avatarData=student.getAvatar();
            Bitmap bitmap = BitmapFactory.decodeByteArray(student.getAvatar(), 0, student.getAvatar().length);
            avatarStudent.setImageBitmap(bitmap);

        }

        avatarStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, PICK_IMAGE_UPDATE);
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