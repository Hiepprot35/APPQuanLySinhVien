package com.example.quanlysinhvien.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.quanlysinhvien.model.Student;
import com.example.quanlysinhvien.model.User;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME="quan_ly_sinh_vien.db";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void register_user(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());
        db.insert("user", null, values);
        db.close();
    }
    public ArrayList<Student> getAllStudent()
    {
        ArrayList<Student> list_student=new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c=db.query("student",null,null,null,null,null,null);
        if (c.moveToFirst()) {
            do {
                String fullname=c.getString(c.getColumnIndexOrThrow("fullname"));
                String major_id=c.getString(c.getColumnIndexOrThrow("major_id"));
                String email=c.getString(c.getColumnIndexOrThrow("email"));

                String id=c.getString(c.getColumnIndexOrThrow("id"));
                String class_id=c.getString(c.getColumnIndexOrThrow("class_id"));
                String date_of_birth=c.getString(c.getColumnIndexOrThrow("date_of_birth"));
                String phone_number=c.getString(c.getColumnIndexOrThrow("phone_number"));

                byte[] avatar=c.getBlob(c.getColumnIndexOrThrow("avatar"));
               Student student =new Student(id,fullname,email,null,null,phone_number,date_of_birth,null,null,null,avatar);
               list_student.add(student);

            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return list_student;
    }
    public boolean find_class_unique(String Name_class,String value)
    {
        Log.d("Value",value);
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = Name_class + " = ?";
        String[] selectionArgs = {value};

        Cursor c=db.query("student",null,selection,selectionArgs,null,null,null);
        if (c != null && c.moveToFirst()) {
            c.close();
            return false;
        } else {
            if (c != null) {
                c.close();
            }
            return true;
        }
    }
    public void add_Student(Student student)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        ContentValues user=new ContentValues();
        Field[] fields = student.getClass().getDeclaredFields();

        try {
            for (Field field : fields) {
                field.setAccessible(true);

                // Lấy tên của field và giá trị tương ứng của nó
                String fieldName = field.getName();
                Object value = field.get(student);

                // Thêm giá trị vào ContentValues
                if (value != null) {
                    values.put(fieldName, value.toString());
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        values.put("avatar",student.getAvatar());

        user.put("username",student.getPhone_number());
        user.put("role",1);
        user.put("password",student.getPhone_number());


        db.insert("student", null, values);
        db.insert("user",null,user);
        db.close();
    }
    public void delete_student(String phone_number) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete("student", "phone_number = ?", new String[]{String.valueOf(phone_number)});
        int deletedRows2 = db.delete("user", "username = ?", new String[]{String.valueOf(phone_number)});

        // Số lượng hàng bị xóa sẽ được lưu trong biến deletedRows

        if (deletedRows > 0) {
            Log.d("Xoa","xoa");
        } else {
            // Không có hàng nào bị xóa
        }
    }
    public void update_Student(Student student){
        if(student.getAvatar().toString()!=null) {
            Log.d("Zoo", student.getAvatar().toString());
        }
        else {

            Log.d("Không có ảnh","ok");
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Field[] fields = student.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);

                // Lấy tên của field và giá trị tương ứng của nó
                String fieldName = field.getName();
                Object value = field.get(student);

                // Thêm giá trị vào ContentValues
                if (value != null) {
                    values.put(fieldName, value.toString());
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        values.put("avatar",student.getAvatar());

        db.update("student",values,"id=?",new String[] {student.getId()});
        db.close();

    }
    public Student getStudentBy(String colum,String value) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = colum + " = ?";
        String[] selectionArgs = {value};
        Cursor cursor = db.query("student", null, selection, selectionArgs, null, null, null);

        Student student = null;
        if (cursor != null && cursor.moveToFirst()) {
            String id_student = cursor.getString(cursor.getColumnIndexOrThrow("id"));
            String fullname = cursor.getString(cursor.getColumnIndexOrThrow("fullname"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String country = cursor.getString(cursor.getColumnIndexOrThrow("country"));
            String gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
            String phone_number = cursor.getString(cursor.getColumnIndexOrThrow("phone_number"));
            String date_of_birth = cursor.getString(cursor.getColumnIndexOrThrow("date_of_birth"));
            String class_id = cursor.getString(cursor.getColumnIndexOrThrow("class_id"));
            String major_id = cursor.getString(cursor.getColumnIndexOrThrow("major_id"));
            String subject_id = cursor.getString(cursor.getColumnIndexOrThrow("subject_id"));
            byte[] avatar = cursor.getBlob(cursor.getColumnIndexOrThrow("avatar"));

            student = new Student(id_student, fullname, email, country, gender, phone_number, date_of_birth, class_id, major_id, subject_id, avatar);
            cursor.close();
        }
        return student;
    }

    public void getAllUser()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c=db.query("user",null,null,null,null,null,null);
        if (c.moveToFirst()) {
            do {
                String username = c.getString(1);
                String password = c.getString(2);

                Log.d("User", "Username: " + username + ", Password: " + password );
            } while (c.moveToNext());
        }

        c.close();
        db.close();
    }
    public boolean checkUser(String username, String password) {
//        List<User> user = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String[] user={username,password};
        Cursor c = db.query("user", null, "username=? AND password=?", user, null, null, null, null);
       if(c!=null) {
           if (c.moveToFirst()) {

               {
                   return true;
               }
           } else return false;

       }
        c.close();
        db.close();
    return false;
    }
    public int checkRole(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query("user", new String[]  {"role"}, "username=?",new String[] {username} , null, null, null, null);
        c.moveToFirst();
        int role_data = c.getInt(0);
        c.close();
        db.close();
        return role_data;
    }
    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {"username"};
        String selection = "username = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query("user", projection, selection, selectionArgs, null, null, null);

        boolean exists = cursor.moveToFirst();

        cursor.close();
        db.close();

        return exists;
    }

}
