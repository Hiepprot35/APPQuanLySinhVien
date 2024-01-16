package com.example.quanlysinhvien.student;


import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlysinhvien.R;
import com.example.quanlysinhvien.database.DatabaseHelper;
import com.example.quanlysinhvien.function.Function_user;
import com.example.quanlysinhvien.model.Student;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;

public class UpdateStudent extends AppCompatActivity {
    DatabaseHelper dbHelper;
    EditText edtphone_update, edtEmailSV_update, edtcountry_update, edtnameSV_update;
    TextView edtmaSV_update;
    byte[] avatarData;
    ImageView avatarStudent;
    Button updateStudent;
    Button edtbirthday_update;
    RadioGroup rdg_gender_update;
    String gender;
    Function_user function_user;
    String[] majors,classes;
    String selected_major,selected_class;
    Spinner spin_class_update,spin_major_update;

    private static final int PICK_IMAGE_UPDATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_student);
        function_user=new Function_user();
        spin_major_update=findViewById(R.id.spin_major_update);
        spin_class_update=findViewById(R.id.spin_class_update);
        dbHelper=new DatabaseHelper(getApplicationContext());



        edtbirthday_update=findViewById(R.id.edtbirthday_update);
        edtcountry_update = findViewById(R.id.edtcountry_update);
        avatarStudent=findViewById(R.id.img_avatar_update);
        edtphone_update = findViewById(R.id.edtphone_update);
        edtEmailSV_update = findViewById(R.id.edtEmailSV_update);
        rdg_gender_update=findViewById(R.id.rdg_gender_update);
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
            gender=student.getGender();
            classes= dbHelper.getAllClassesbyMajor(student.getMajor_id());
            RadioButton nam_radio=findViewById(R.id.radioButton_nam_update);
            RadioButton nu_radio=findViewById(R.id.radioButton_nu_update);

            if(gender.equals(nam_radio.getText().toString()))
            {
                nam_radio.setChecked(true);
            }
            else
            {
                nu_radio.setChecked(true);
            }
//            int selectedId=rdg_gender_update.getCheckedRadioButtonId();
//            if(selectedId!=1)
//            {
//                RadioButton gender_radio=findViewById(selectedId);
//                gender=gender_radio.getText().toString();
//            }
            majors=dbHelper.getAllMajor();
            ArrayAdapter<String> spin_major_adapter=new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,majors);
            spin_major_update.setAdapter(spin_major_adapter);
            int position = function_user.checkPositon(majors,student.getMajor_id());
            Log.d("Position", String.valueOf(position));
            spin_major_update.setSelection(position);
            Bitmap bitmap = BitmapFactory.decodeByteArray(student.getAvatar(), 0, student.getAvatar().length);
            avatarStudent.setImageBitmap(bitmap);

        }
        edtbirthday_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            function_user.openDatePicker(v,edtbirthday_update,UpdateStudent.this);
            }
        });
        spin_major_update.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue = parent.getItemAtPosition(position).toString();
                String[] major_id_selected = selectedValue.split(" - ");
                selected_major = major_id_selected[0];
                if (selected_major != null) {
                    classes = dbHelper.getAllClassesbyMajor(selected_major);
                    ArrayAdapter<String> class_adapter = new ArrayAdapter<String>(UpdateStudent.this, android.R.layout.simple_spinner_dropdown_item, classes);
                    spin_class_update.setAdapter(class_adapter);
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spin_class_update.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        avatarStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, PICK_IMAGE_UPDATE);
            }
        });
        rdg_gender_update.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rd=findViewById(checkedId);
                gender=rd.getText().toString();
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
                    if(id.length()<4 || number_phone.length()<4) {
                        Toast.makeText(UpdateStudent.this, "Độ dài MSSV hay SĐT không nhỏ hơn 4 kí tự ", Toast.LENGTH_LONG).show();

                    }
                    else {
                        Student updateStudent_data = new Student(id, fullname, email, country, gender, number_phone, date, selected_class, selected_major, null, avatarData);
                        dbHelper.update_Student(updateStudent_data);
                        Intent intent_addsv = new Intent(UpdateStudent.this, StudentActivity.class);
                        startActivity(intent_addsv);
                        Toast.makeText(UpdateStudent.this, "Cập nhật thành công sinh viên "+id, Toast.LENGTH_LONG).show();
                    }
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