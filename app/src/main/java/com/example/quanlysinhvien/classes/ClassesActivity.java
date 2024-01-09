package com.example.quanlysinhvien.classes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.quanlysinhvien.R;
import com.example.quanlysinhvien.database.DatabaseHelper;
import com.example.quanlysinhvien.function.Function_user;
import com.example.quanlysinhvien.model.Major;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class ClassesActivity extends AppCompatActivity {

    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database=null;
    String DATABASE_NAME="quan_ly_sinh_vien.db";

    Spinner major_class_spin;
    EditText edt_maLop, edt_tenLop;
    Button btn_insert_lop, btn_update_lop, btn_delete_lop, btn_query_lop;
    DatabaseHelper dbHelper;
    ListView lv;

    ArrayList<String> mylist;
    ArrayAdapter<String> myadapter;
    String[] majors;
    String major_selected;
    Function_user functionUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);
        dbHelper=new DatabaseHelper(getApplicationContext());
        functionUser=new Function_user();
        majors=dbHelper.getAllMajor();
        edt_maLop = findViewById(R.id.edt_malop1);
        edt_tenLop = findViewById(R.id.edt_tenLop1);
        major_class_spin=findViewById(R.id.spin_major_class);
        btn_insert_lop = findViewById(R.id.btn_insert_lop);
        btn_delete_lop = findViewById(R.id.btn_delete_lop);
        btn_update_lop = findViewById(R.id.btn_update_lop);
        btn_query_lop = findViewById(R.id.btn_query_lop);
        ArrayAdapter<String> major_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, majors);
        major_class_spin.setAdapter(major_adapter);
        lv = findViewById(R.id.lv);
        mylist = new ArrayList<>();
        myadapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mylist);
        lv.setAdapter(myadapter);

        processCopy();

        database = openOrCreateDatabase("quan_ly_sinh_vien.db", MODE_PRIVATE, null);

        Cursor c = database.query("classes", null, null,null,null,null,null,null);
        c.moveToFirst();
        String data = "";
        while (c.isAfterLast() == false)
        {
            data = c.getString(0) + " - " + c.getString(2)+" - "+ c.getString(1);
            mylist.add(data);
            c.moveToNext();
        }
        c.close();
        myadapter.notifyDataSetChanged();
        major_class_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue = parent.getItemAtPosition(position).toString();
                String [] major_id_selected=selectedValue.split(" - ");
                major_selected=major_id_selected[0];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_insert_lop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String malop = edt_maLop.getText().toString();
                String tenlop = edt_tenLop.getText().toString();
                Log.d("TenLop",tenlop);
                ContentValues value = new ContentValues();
                value.put("id",malop);
                value.put("classs_name", tenlop);
                if(major_selected!=null)
                {
                    value.put("major_id",major_selected);
                    Toast.makeText(ClassesActivity.this, "Chọn nghành", Toast.LENGTH_SHORT).show();

                }

                String msg = "";
                if (database.insert("classes", null, value) == -1)
                {
                    msg = "Fail to Insert Record";
                }
                else {
                    loading();
                    msg = "Insert record Sucess";
                }
                Toast.makeText(ClassesActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        btn_delete_lop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String malop = edt_maLop.getText().toString();
                int n = database.delete("classes", "id = ?" , new String[]{malop} );
                String msg = "";
                if (n == 0)
                {
                    msg = "No record to Delete";
                }
                else {
                    loading();
                    edt_maLop.setText("");
                    edt_tenLop.setText("");
                    msg = n + "record is deleted";
                }
                Toast.makeText(ClassesActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        btn_update_lop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String malop = edt_maLop.getText().toString();
                String tenlop = edt_tenLop.getText().toString();
                ContentValues value = new ContentValues();
                value.put("classs_name", tenlop);
                value.put("major_id",major_selected);
                int n = database.update("classes", value, "id = ?" , new String[]{malop} );
                String msg = "";
                if (n == 0)
                {
                    msg = "No record to Update";
                }
                else {
                    loading();
                    msg = n + "record is Update";
                }
                Toast.makeText(ClassesActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        btn_query_lop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data=mylist.get(position);
                String [] data2=data.split(" - ");
                edt_maLop.setText(data2[0]);
                edt_tenLop.setText(data2[1]);
                major_class_spin.setSelection(functionUser.checkPositon(majors,data2[2]));


            }
        });

    }

    private void processCopy() {
        //pri p vate app
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists())
        {
            try{CopyDataBaseFromAsset();
                Toast.makeText(this, "Copying sucess from Assets folder",
                        Toast.LENGTH_LONG).show();
            }
            catch (Exception e){
                Toast.makeText(this, e.toString(),
//gfh
                        Toast.LENGTH_LONG).show();
            }
        }
    }
    private void loading(){
        mylist.clear();
        Cursor c = database.query("classes", null, null,null, null,null, null,null);
        c.moveToNext();
        String data = "";
        while (c.isAfterLast() == false)
        {
            data = c.getString(0) + " - " + c.getString(2)+" - "+ c.getString(1);
            c.moveToNext();
            mylist.add(data);
        }
        c.close();
        myadapter.notifyDataSetChanged();
    }
    private String getDatabasePath() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX+ DATABASE_NAME;
    }
    public void CopyDataBaseFromAsset() {
        // TODO Auto-generated method stub
        try {
            InputStream myInput;
            myInput = getAssets().open(DATABASE_NAME);
            // Path to the just created empty db
            String outFileName = getDatabasePath();
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