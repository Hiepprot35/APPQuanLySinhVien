package com.example.quanlysinhvien.major;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.quanlysinhvien.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MajorActivity extends AppCompatActivity {

    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database = null;
    String DATABASE_NAME = "quan_ly_sinh_vien.db";


    EditText edt_manganh, edt_tennganh;
    Button btn_insert_nganh, btn_update_nganh, btn_delete_nganh, btn_query_nganh;

    ListView lv;

    ArrayList<String> mylist;
    ArrayAdapter<String> myadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_major);

        edt_manganh = findViewById(R.id.edt_manganh);
        edt_tennganh = findViewById(R.id.edt_tennganh);

        btn_insert_nganh = findViewById(R.id.btn_insert_nganh);
        btn_delete_nganh = findViewById(R.id.btn_delete_nganh);
        btn_update_nganh = findViewById(R.id.btn_update_nganh);
        btn_query_nganh = findViewById(R.id.btn_query_nganh);


        lv = findViewById(R.id.lv);
        mylist = new ArrayList<>();
        myadapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mylist);
        lv.setAdapter(myadapter);

        processCopy();

        database = openOrCreateDatabase("quan_ly_sinh_vien.db", MODE_PRIVATE, null);

        Cursor c = database.query("major", null, null, null, null, null, null, null);
        c.moveToFirst();
        String data = "";
        while (c.isAfterLast() == false) {
            data = c.getString(0) + " - " + c.getString(1);
            mylist.add(data);
            c.moveToNext();
        }
        c.close();
        myadapter.notifyDataSetChanged();

        btn_insert_nganh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String manganh = edt_manganh.getText().toString();
                String tennganh = edt_tennganh.getText().toString();
                ContentValues myvalue = new ContentValues();
                myvalue.put("id", manganh);
                myvalue.put("major_name", tennganh);

                String msg = "";
                if (database.insert("major", null, myvalue) == -1) {
                    msg = "Fail to Insert Record";
                } else {
                    loading();
                    msg = "Insert record Sucess";
                }
                Toast.makeText(MajorActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        btn_delete_nganh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String manganh = edt_manganh.getText().toString();
                int n = database.delete("major", "id = ?", new String[]{manganh});
                String msg = "";
                if (n == 0) {
                    msg = "No record to Delete";
                } else {
                    loading();
                    edt_manganh.setText("");
                    edt_tennganh.setText("");
                    msg = n + "record is deleted";
                }
                Toast.makeText(MajorActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        btn_update_nganh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tennganh = edt_tennganh.getText().toString();
                String manganh = edt_manganh.getText().toString();
                ContentValues myvalue = new ContentValues();
                myvalue.put("major_name", tennganh);
                int n = database.update("major", myvalue, "id = ?", new String[]{manganh});
                String msg = "";
                if (n == 0) {
                    msg = "No record to Update";
                } else {
                    loading();
                    msg = n + "redcord is Updated";
                }
                Toast.makeText(MajorActivity.this, msg, Toast.LENGTH_SHORT).show();

            }
        });
        btn_query_nganh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mylist.clear();
                Cursor c = database.query("major", null, null, null, null, null, null, null);
                c.moveToNext();
                String data = "";
                while (c.isAfterLast() == false) {
                    data = c.getString(0) + " - " + c.getString(1);
                    c.moveToNext();
                    mylist.add(data);
                }
                c.close();
                myadapter.notifyDataSetChanged();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = mylist.get(position);
                String[] data2 = data.split(" - ");
                edt_manganh.setText(data2[0]);
                edt_tennganh.setText(data2[1]);

            }
        });
    }

    private void processCopy() {
        //pri p vate app
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAsset();
                Toast.makeText(this, "Copying sucess from Assets folder",
                        Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, e.toString(),
//gfh
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void loading()
    {
        mylist.clear();
        Cursor c = database.query("major", null, null,null, null,null, null,null);
        c.moveToNext();
        String data = "";
        while (c.isAfterLast() == false)
        {
            data = c.getString(0)+ " - " + c.getString(1) ;
            c.moveToNext();
            mylist.add(data);
        }
        c.close();
        myadapter.notifyDataSetChanged();

        lv.invalidateViews();

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