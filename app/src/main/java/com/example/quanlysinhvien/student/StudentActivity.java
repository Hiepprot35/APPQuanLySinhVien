package com.example.quanlysinhvien.student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.quanlysinhvien.R;
import com.example.quanlysinhvien.database.DatabaseHelper;
import com.example.quanlysinhvien.model.Student;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity {
    ImageButton btn_next_addSV;
    ListView student_listview;
    private StudentAdapter studentAdapter;
    DatabaseHelper dbHelper;
    ArrayList<Student> list_student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbHelper=new DatabaseHelper(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        student_listview=findViewById(R.id.lv_sv);
        list_student=dbHelper.getAllStudent();

        studentAdapter=new StudentAdapter(this,list_student,dbHelper);
        studentAdapter.setOnStudentDeleteListener(new StudentAdapter.OnStudentDeleteListener() {
            @Override
            public void onStudentDelete() {
                // Reload data or refresh the activity here
                // For example:
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        student_listview.setAdapter(studentAdapter);
        btn_next_addSV = findViewById(R.id.btn_next_addSV);
        btn_next_addSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_addsv = new Intent(StudentActivity.this, AddStudentActivity.class);
                startActivity(intent_addsv);
            }
        });



    }
}