package com.example.ams;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewAttendFaculty extends AppCompatActivity {


    String name="";
    Spinner sb;
    Spinner sc;
    Spinner ss;
    Spinner ssub;
    List<String> batch=new ArrayList<String>();
    List<String> course=new ArrayList<String>();
    List<String> semester=new ArrayList<String>();
    List<String> subjects=new ArrayList<String>();
    Database d=new Database(this);
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attend_faculty);
        name=getIntent().getStringExtra("name");
        batch=d.getBatch(name);
        sb=(Spinner)findViewById(R.id.spinner_batch);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_list_item_1,batch);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sb.setAdapter(aa);
        sb.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
             course=d.getCourse(name,sb.getSelectedItem().toString());
             cour();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        sc=(Spinner)findViewById(R.id.spinner_course);
        ArrayAdapter ab = new ArrayAdapter(this,android.R.layout.simple_list_item_1,course);
        ab.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sc.setAdapter(ab);
        sc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                semester=d.getSem(name,sb.getSelectedItem().toString(),sc.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




            Toast.makeText(this,name+" "+batch.size()+" "+course.size()+" "+semester.size()+" "+subjects.size()+"",Toast.LENGTH_LONG).show();
    }

    public void dtatab(View view) {
        Spinner sb = (Spinner) findViewById(R.id.spinner_batch);
        Spinner sc = (Spinner) findViewById(R.id.spinner_course);
        Spinner ss = (Spinner) findViewById(R.id.spinner_semester);
        Spinner sp = (Spinner) findViewById(R.id.spinner_subjects);
        DatePicker date = (DatePicker) findViewById(R.id.datePicker1);
        DatePicker dateto = (DatePicker) findViewById(R.id.datePicker2);
        Date dt = new Date(date.getYear(), date.getMonth(), date.getDayOfMonth());
        Date dt1 = new Date(dateto.getYear(), dateto.getMonth(), dateto.getDayOfMonth());
        String d1 = date.toString();
        String d2 = dateto.toString();
        long difference = Math.abs(dt1.getTime() - dt.getTime());
        long difftDays = difference / (24 * 60 * 60 * 1000);
        if (difftDays < 1)
            Toast.makeText(this, "Start Date And Date Are Invalid", Toast.LENGTH_LONG).show();
        else {

            String ds=date.getDayOfMonth()+"-"+(date.getMonth()+1)+"-"+date.getYear();
            String de=dateto.getDayOfMonth()+"-"+(dateto.getMonth()+1)+"-"+dateto.getYear();

            List<List<String>> c = d.getTable(sb.getSelectedItem().toString(), sc.getSelectedItem().toString(), ss.getSelectedItem().toString(), sp.getSelectedItem().toString(),ds,de);
            createTable(c);
        }
    }

    public void createTable(List<List<String>> c) {
        TableLayout t = (TableLayout) findViewById(R.id.viewTable);
        t.removeAllViewsInLayout();
        if (!c.isEmpty()) {
            for (int i = 0; i < c.size(); i++) {
                TableRow tr = new TableRow(this);
                tr.setBackgroundResource(R.drawable.row_border);
                // tr.setBackgroundColor(Color.WHITE);
                TextView[] a = new TextView[100];
                for (int j = 0; j < c.get(i).size(); j++) {
                    a[j]=new TextView(this);
                    a[j].setBackgroundResource(R.drawable.row_border);
                    a[j].setText(c.get(i).get(j));
                    a[j].setPadding(10, 10, 10, 10);
                    tr.addView(a[j]);
                }
                t.addView(tr);
            }
        }
        else
        {
            Toast.makeText(this,"No Data Available In This Table.",Toast.LENGTH_LONG).show();
        }
    }

    public void cour(){
        sc=(Spinner)findViewById(R.id.spinner_course);
        ArrayAdapter ab = new ArrayAdapter(this,android.R.layout.simple_list_item_1,course);
        ab.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sc.setAdapter(ab);
        sc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                semester=d.getSem(name,sb.getSelectedItem().toString(),sc.getSelectedItem().toString());
                sem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void printdata(View view){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Date date = new Date();
        String fn= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+dateFormat.format(date)+".csv";
        FileOutputStream fos=null;

        {
            try {
                fos = new FileOutputStream(fn);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        OutputStreamWriter osw;
        CSVWriter writer;
        osw= new OutputStreamWriter(fos,
                StandardCharsets.UTF_8);
        writer= new CSVWriter(osw);
        TableLayout t = (TableLayout) findViewById(R.id.viewTable);
        if(t.getChildCount()==0){
            Toast.makeText(this,"No Data Available To Print.",Toast.LENGTH_LONG).show();
        }
        else
        {
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
    public void sem(){
        ss=(Spinner)findViewById(R.id.spinner_semester);
        ArrayAdapter ac = new ArrayAdapter(this,android.R.layout.simple_list_item_1,semester);
        ac.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ss.setAdapter(ac);
        ss.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subjects=d.getPaper(name,sb.getSelectedItem().toString(),sc.getSelectedItem().toString(),ss.getSelectedItem().toString());
                sub();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void sub(){
        ssub=(Spinner)findViewById(R.id.spinner_subjects);
        ArrayAdapter ad = new ArrayAdapter(this,android.R.layout.simple_list_item_1,subjects);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ssub.setAdapter(ad);
    }
}
