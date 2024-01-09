package com.example.quanlysinhvien.student;

import static androidx.core.app.ActivityCompat.recreate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quanlysinhvien.R;
import com.example.quanlysinhvien.database.DatabaseHelper;
import com.example.quanlysinhvien.model.Student;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends ArrayAdapter<Student> {
    private ArrayList<Student> students;
    DatabaseHelper dbHelper;
    private Context context;


    public StudentAdapter(@NonNull Context context, ArrayList<Student> students,DatabaseHelper dbHelper) {
        super(context,0, students);
        this.context=context;
        this.students = students;
        this.dbHelper=dbHelper;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.student_layout, parent, false);
        }
        Button btn_delete=listItem.findViewById(R.id.btn_delete);
        Button btn_update=listItem.findViewById(R.id.btn_update);

        ImageView avatar_student = listItem.findViewById(R.id.avatar_student_layout);
        TextView student_name_layout = listItem.findViewById(R.id.student_name_layout);
        TextView student_SDT_layout=listItem.findViewById(R.id.student_SDT_layout);
        TextView student_bd_layout=listItem.findViewById(R.id.student_bd_layout);
        TextView student_class_layout=listItem.findViewById(R.id.student_class_layout);

        TextView student_ID_layout=listItem.findViewById(R.id.student_ID_layout);
        TextView student_Email_layout=listItem.findViewById(R.id.student_Email_layout);
        Student currentStudent = students.get(position);
        student_name_layout.setText(currentStudent.getFullname());
        student_Email_layout.setText(currentStudent.getEmail());
        student_SDT_layout.setText(currentStudent.getPhone_number());
        student_bd_layout.setText(currentStudent.getDate_of_birth());

        student_ID_layout.setText(currentStudent.getId());
        byte[] avatarData = currentStudent.getAvatar();
        if (avatarData != null) {
            Bitmap bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(avatarData));
            avatar_student.setImageBitmap(bitmap);
        }
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.delete_student(currentStudent.getPhone_number());
                ArrayList<Student> updatedList = dbHelper.getAllStudent();
                students.clear();  // Xóa dữ liệu cũ
                students.addAll(updatedList);  // Thêm dữ liệu mới
                notifyDataSetChanged();  // Cập nhật Adapter
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateStudent.class);
                intent.putExtra("student_id", currentStudent.getId());
                context.startActivity(intent);
            }
        });
        return listItem;
    }
    public interface OnStudentDeleteListener {
        void onStudentDelete();
    }

    private OnStudentDeleteListener listener;

    public void setOnStudentDeleteListener(OnStudentDeleteListener listener) {
        this.listener = listener;
    }

}
