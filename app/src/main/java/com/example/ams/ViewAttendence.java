package com.example.ams;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
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

public class ViewAttendence extends AppCompatActivity {

    Database d=new Database(this);
    List<String> courses=new ArrayList<String>();
    List<String> semesters=new ArrayList<String>();
    List<String> batches=new ArrayList<String>();
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendence);;
        batches=d.getAtt();
        courses=d.getAttc();
        semesters=d.getAtts();
        Spinner spin1 = (Spinner) findViewById(R.id.spinner_batch);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_list_item_1,batches);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setAdapter(aa);
        Spinner spin2 = (Spinner) findViewById(R.id.spinner_course);
        ArrayAdapter ab = new ArrayAdapter(this,android.R.layout.simple_list_item_1,courses);
        ab.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin2.setAdapter(ab);
        Spinner spin3 = (Spinner) findViewById(R.id.spinner_semester);
        ArrayAdapter ac = new ArrayAdapter(this,android.R.layout.simple_list_item_1,semesters);
        ac.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin3.setAdapter(ac);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void printdata(View view){
        TableLayout t = (TableLayout) findViewById(R.id.viewTable);
        if(t.getChildCount()==0){
            Toast.makeText(this,"No Data Available To Print.",Toast.LENGTH_LONG).show();
        }
        else
        {
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
    public void showdta(View view){
        Spinner spin1 = (Spinner) findViewById(R.id.spinner_batch);
        Spinner spin2 = (Spinner) findViewById(R.id.spinner_course);
        Spinner spin3 = (Spinner) findViewById(R.id.spinner_semester);
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

            List<List<String>> c = d.getAttend(spin1.getSelectedItem().toString(), spin2.getSelectedItem().toString(), spin3.getSelectedItem().toString(),ds,de);
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
}
