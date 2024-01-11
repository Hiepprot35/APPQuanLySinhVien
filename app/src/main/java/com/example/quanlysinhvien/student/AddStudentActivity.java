package com.example.quanlysinhvien.student;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.quanlysinhvien.R;
import com.example.quanlysinhvien.database.DatabaseHelper;
import com.example.quanlysinhvien.function.Function_user;
import com.example.quanlysinhvien.model.Student;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

public class AddStudentActivity extends AppCompatActivity {
    Spinner spin_class, spin_major;
    DatabaseHelper dbHelper;
    Student student_new;
    EditText edt_phone_number, edtEmailSV, edtcountry, edtnameSV,edtmaSV;
    Button btn_add_student;
    ImageView img_avatar;
    Button date_picker_add_student;
    Function_user functionUser;
    byte[] imageData;
    private static final int PICK_IMAGE = 1;
    String selected_major;
    String selected_class;

    String classes[];
    String major[] ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_student);
        dbHelper = new DatabaseHelper(getApplicationContext());

        date_picker_add_student=findViewById(R.id.edtbirthday);
        functionUser=new Function_user();
        edtmaSV=findViewById(R.id.edtmaSV);
        img_avatar = findViewById(R.id.img_avatar);
        edt_phone_number = findViewById(R.id.edtphone);
        btn_add_student = findViewById(R.id.btn_add_student);
        edtnameSV = findViewById(R.id.edtnameSV);
        edtcountry = findViewById(R.id.edtcountry);
        edtEmailSV = findViewById(R.id.edtEmailSV);
        //tạo spinner cho lớp học
        spin_class = findViewById(R.id.spin_class);
        //tạo spinner cho môn học
        spin_major = findViewById(R.id.spin_major);
        major= dbHelper.getAllMajor();
        ArrayAdapter<String> major_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, major);
        spin_major.setAdapter(major_adapter);
        if(date_picker_add_student != null) {
            date_picker_add_student.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    functionUser.openDatePicker(v,date_picker_add_student,AddStudentActivity.this);
                }
            });
        } else {
            Log.e("DatePicker", "date_picker is null");
        }
        img_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, PICK_IMAGE);
            }
        });
        spin_major.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue = parent.getItemAtPosition(position).toString();
                String [] major_id_selected=selectedValue.split(" - ");
                selected_major=major_id_selected[0];
                if(selected_major!=null) {
                    classes = dbHelper.getAllClassesbyMajor(selected_major);
                    ArrayAdapter<String> class_adapter = new ArrayAdapter<String>(AddStudentActivity.this, android.R.layout.simple_spinner_dropdown_item, classes);
                    spin_class.setAdapter(class_adapter);
                }}
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spin_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue = parent.getItemAtPosition(position).toString();
                String [] class_id_selected=selectedValue.split(" - ");
                selected_class=class_id_selected[0];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_add_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname = edtnameSV.getText().toString();
                String date = date_picker_add_student.getText().toString();
                String number_phone = edt_phone_number.getText().toString();
                String country = edtcountry.getText().toString();
                String email = edtEmailSV.getText().toString();
                String id = edtmaSV.getText().toString();
                student_new = new Student(id, fullname, email, country, null, number_phone, date, selected_class, selected_major, null, imageData);
                if (fullname.isEmpty() || date.isEmpty()||number_phone.isEmpty()||country.isEmpty()
                || id.isEmpty()
                ) {
                    Toast.makeText(AddStudentActivity.this, "Không được để trống thông tin", Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(id.length()<4 || number_phone.length()<4) {
                        Toast.makeText(AddStudentActivity.this, "Độ dài MSSV hay SĐT không nhỏ hơn 4 kí tự ", Toast.LENGTH_LONG).show();

                    }
                        else{
                            if (dbHelper.find_class_unique("phone_number", number_phone) && dbHelper.find_class_unique("id", id)) {
                                dbHelper.add_Student(student_new);
                                Intent intent_addsv = new Intent(AddStudentActivity.this, StudentActivity.class);
                                startActivity(intent_addsv);
                                Toast.makeText(AddStudentActivity.this, "Thêm thành công", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(AddStudentActivity.this, "MSSV hoặc SĐT đã tồn tại", Toast.LENGTH_LONG).show();
                            }

                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Function_user functionUser = new Function_user();
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            android.net.Uri selectedImageUri = data.getData();
            try{
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                imageData = functionUser.resizeImage(inputStream, 180, 180)  ;
                img_avatar.setImageURI(selectedImageUri);

            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}