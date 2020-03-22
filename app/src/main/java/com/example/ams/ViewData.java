package com.example.ams;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class ViewData extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    Date date = new Date();
    String fn=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+dateFormat.format(date)+".csv";
    String[] tables={"Choose Data Source","Student","Teacher","Assigned Subjects"};
    Database d=new Database(this);
    FileOutputStream fos;

    {
        try {
            fos = new FileOutputStream(fn);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    OutputStreamWriter osw;
    CSVWriter writer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);
        Spinner spin1 = (Spinner) findViewById(R.id.spinner_source);
        spin1.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_list_item_1,tables);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setAdapter(aa);
         osw= new OutputStreamWriter(fos,
                StandardCharsets.UTF_8);
         writer= new CSVWriter(osw);
    }

    public void createTable(Cursor c) {
        TableLayout t = (TableLayout) findViewById(R.id.datatab);
        t.removeAllViewsInLayout();
        if (c.moveToFirst()) {
            for (int i = 0; i < c.getCount(); i++) {
                TableRow tr = new TableRow(this);
                tr.setBackgroundResource(R.drawable.row_border);
               // tr.setBackgroundColor(Color.WHITE);
                TextView[] a = new TextView[100];
                for (int j = 0; j < c.getColumnCount(); j++) {
                    a[j]=new TextView(this);
                    a[j].setBackgroundResource(R.drawable.row_border);
                    a[j].setText(c.getString(j));
                    a[j].setPadding(10, 10, 10, 10);
                    tr.addView(a[j]);
                }
                t.addView(tr);
                c.moveToNext();
            }
        }
        else
        {
            Toast.makeText(this,"No Data Available In This Table.",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Spinner spin1 = (Spinner) findViewById(R.id.spinner_source);
         if(position==0)
            Toast.makeText(this,"Source Not Selected.",Toast.LENGTH_LONG).show();
        else if(position==1){
                Cursor r=d.getStudent();
                createTable(r);
            }
        else if(position==2){
            Cursor r=d.getFacultyData();
            createTable(r);
         }
        else if(position==3){
             Cursor r=d.getAssignmentData();
             createTable(r);
         }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void printdata(View view){
        TableLayout t = (TableLayout) findViewById(R.id.datatab);
        if(t.getChildCount()==0){
            Toast.makeText(this,"No Data Available To Print.",Toast.LENGTH_LONG).show();
        }
        else
        {
            Spinner spin1 = (Spinner) findViewById(R.id.spinner_source);
            if(spin1.getSelectedItemPosition()==1){
                String[] v={"Roll Numeber","Name","Course","Semester","Batch","Status"};
                writer.writeNext(v);
            }
            if(spin1.getSelectedItemPosition()==3){
                String[] v={"Id","Course","Semester","Faculty Name","Paper Id","Batch"};
                writer.writeNext(v);
            }
            if(spin1.getSelectedItemPosition()==2){
                String[] v={"Id","Name","Email","Password","Type"};
                writer.writeNext(v);
            }
            TableRow tr;
            tr=(TableRow) t.getChildAt(0);
            String[] lt=new String[tr.getChildCount()];
            for(int i=0;i<t.getChildCount();i++){
               tr =(TableRow) t.getChildAt(i);
                for(int j=0;j<tr.getChildCount();j++){
                    TextView tv=(TextView)tr.getChildAt(j);
                    lt[j]=(tv.getText().toString());
                }
                writer.writeNext(lt);

            }
            try {
                writer.close();
                Toast.makeText(this,"File Has been Saved To Download Folder.",Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
