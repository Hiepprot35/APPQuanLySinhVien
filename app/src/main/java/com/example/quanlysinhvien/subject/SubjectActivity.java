package com.example.quanlysinhvien.subject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.quanlysinhvien.R;
import com.example.quanlysinhvien.classes.ClassesActivity;
import com.example.quanlysinhvien.database.DatabaseHelper;
import com.example.quanlysinhvien.function.Function_user;
import com.example.quanlysinhvien.major.MajorActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class SubjectActivity extends AppCompatActivity {

    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database=null;
    String DATABASE_NAME="quan_ly_sinh_vien.db";
    Spinner spin_major_monhoc;

    EditText edt_mamon, edt_tenmon, edtSearchSubject;
    Button btn_insert, btn_update, btn_delete, btn_query, btn_nganh, btn_lop1;

    ListView lv;

    ArrayList<String> mylist;
    ArrayAdapter<String> myadapter;
    String major_selected;
    DatabaseHelper dbHelper;
    Function_user functionUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        dbHelper=new DatabaseHelper(getApplicationContext());
        spin_major_monhoc=findViewById(R.id.spin_major_monhoc);
        String[] majors=dbHelper.getAllMajor();
        functionUser=new Function_user();
        ArrayAdapter<String> major_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, majors);
        spin_major_monhoc.setAdapter(major_adapter);
        edt_tenmon = findViewById(R.id.edt_mon);
        edt_mamon = findViewById(R.id.edt_mamon);
        edtSearchSubject=findViewById(R.id.edtSearchSubject);
//        edt_siso = findViewById(R.id.edt_siso);

        btn_insert = findViewById(R.id.btn_insert);
        btn_delete = findViewById(R.id.btn_delete);
        btn_update = findViewById(R.id.btn_update);
        btn_query = findViewById(R.id.btn_query);

        btn_nganh = findViewById(R.id.btn_nganh);
        btn_lop1 = findViewById(R.id.btn_class);

        lv = findViewById(R.id.lv);
        mylist = new ArrayList<>();
        myadapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mylist);
        lv.setAdapter(myadapter);



        processCopy();
        //Mở CSDL lên để dùng
        database = openOrCreateDatabase("quan_ly_sinh_vien.db",MODE_PRIVATE, null);

        // Truy vấn CSDL và cập nhật hiển thị lên Listview
        Cursor c = database.query("subject",null,null,null,null,null,null,null);
        c.moveToFirst();
        String data ="";
        while (c.isAfterLast() == false)
        {
            data = c.getString(0)+" - "+c.getString(2)+" - "+c.getString(1);
            mylist.add(data);
            c.moveToNext();
        }
        c.close();
        myadapter.notifyDataSetChanged();
        spin_major_monhoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        edtSearchSubject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String name=edtSearchSubject.getText().toString();
                getListSubjectLike(name);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mamon = edt_mamon.getText().toString();
                String tenmon = edt_tenmon.getText().toString();
//                int sotinchi = Integer.parseInt(edt_siso.getText().toString());
                ContentValues myvalue = new ContentValues();
                myvalue.put("id", mamon);
                myvalue.put("name_subject", tenmon);
                if(major_selected!=null)
                {
                    myvalue.put("major_id",major_selected);

                }
                String msg = "";
                if (database.insert("subject", null, myvalue) == -1)
                {
                    msg = "Fail to Insert Record";
                }
                else {
                    msg = "Insert record Sucess";
                    loading();
                }
                Toast.makeText(SubjectActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mamon = edt_mamon.getText().toString();
                int n = database.delete("subject", "id = ?", new String[]{mamon});
                String msg = "";
                if (n == 0)
                {
                    msg = "No record to Delete";
                }
                else {
                    msg = n + "record is deleted";
                    loading();
                }
                Toast.makeText(SubjectActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int sotinchi = Integer.parseInt(edt_siso.getText().toString());
                String mamon = edt_mamon.getText().toString();
                String tenmon = edt_tenmon.getText().toString();

                ContentValues myvalue = new ContentValues();
                myvalue.put("name_subject", tenmon);
                myvalue.put("major_id", major_selected);

                int n = database.update("subject", myvalue, "id = ?", new String[]{mamon});
                String msg = "";
                if (n == 0)
                {
                    msg = "No record to Update";
                }
                else
                {
                    msg = n + "redcord is Updated";
                    loading();
                }
                Toast.makeText(SubjectActivity.this, msg, Toast.LENGTH_SHORT).show();

            }
        });
        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_mamon.setText("");
                edt_tenmon.setText("");
            }
        });

        btn_lop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent  = new Intent(SubjectActivity.this, ClassesActivity.class);
                startActivity(myintent);
            }
        });


        btn_nganh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent  = new Intent(SubjectActivity.this, MajorActivity.class);
                startActivity(myintent);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data=mylist.get(position);
                String [] data2=data.split(" - ");
                edt_mamon.setText(data2[0]);
                edt_tenmon.setText(data2[1]);
                spin_major_monhoc.setSelection(functionUser.checkPositon(majors,data2[2]));
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
    public void getListSubjectLike( String value)
    {
        mylist.clear();
        Cursor c=database.query( "subject",
                null,
                "name_subject" + " LIKE ?",
                new String[]{"%" + value + "%"},
                null,
                null,
                null);
        c.moveToNext();
        String data = "";
        while (c.isAfterLast() == false) {
            data = c.getString(0) + " - " + c.getString(2)+ " - " + c.getString(1);
            c.moveToNext();
            mylist.add(data);
        }
        c.close();
        myadapter.notifyDataSetChanged();

        lv.invalidateViews();
    }
    private void loading(){
        mylist.clear();
        Cursor c = database.query("subject", null, null,null, null,null, null,null);
        c.moveToNext();
        String data = "";
        while (c.isAfterLast() == false)
        {
            data = c.getString(0)+ " - " + c.getString(2) + " - " + c.getString(1);
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