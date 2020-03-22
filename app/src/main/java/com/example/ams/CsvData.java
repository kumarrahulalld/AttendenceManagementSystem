package com.example.ams;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVReader;

import org.w3c.dom.Text;

import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

public class CsvData extends AppCompatActivity {
String [][] arr;
String err="";
    String n=("^[ A-Za-z]+$");
    String pattern = "\\d{1,2}";
    String sem = "\\d{1,1}";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csv_data);
        TextView tv;
        TableLayout t=(TableLayout)findViewById(R.id.tableLayout);
        t.setStretchAllColumns(true);

        try {
            CSVReader reader = new CSVReader(new FileReader(getIntent().getStringExtra("path").toString()));
            String[] nextLine;
            String v="";

            int col=0;
            while ((nextLine = reader.readNext()) != null) {
                TableRow tr =  new TableRow(this);
                tr.setBackgroundResource(R.drawable.row_border);
                TextView[] a = new TextView[100];
                for(int i=0;i<nextLine.length;i++){

                    a[i]=new TextView(this);
                    a[i].setBackgroundResource(R.drawable.row_border);
                    a[i].setText(nextLine[i].toString());
                    a[i].setPadding(10, 10, 10, 10);
                    tr.addView(a[i]);
                }

                t.addView(tr);
            }

            TableLayout tl=(TableLayout)findViewById(R.id.tableLayout);
            TableRow k1=(TableRow) tl.getChildAt(0);
            arr=new String[tl.getChildCount()][k1.getChildCount()];
            for(int i=0;i<tl.getChildCount();i++){
               TableRow k=(TableRow) tl.getChildAt(i);
                for(int j=0;j<k.getChildCount();j++) {
                    tv = (TextView) k.getChildAt(j);
                    arr[i][j]=tv.getText().toString();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();


        }
    }


    public void validate(View view){
        Database d=new Database(this);
        for(int i=0;i<arr.length;i++){
            if(arr[i].length!=5)
                err+="Only Two Fileds Are Acceptable In CSV File. At Row "+i;
            else if(arr[i][0].equals(""))
                err+="Roll Number Field Found Empty At Row "+i;
            else if(arr[i][1].equals(""))
                err+="Name Field Found Empty At Row "+i;
            else if(arr[i][2].equals(""))
                err+="Course Field Found Empty At Row "+i;
            else if(arr[i][3].equals(""))
                err+="Semester Field Found Empty At Row "+i;
            else if(arr[i][4].equals(""))
                err+="Batch Field Found Empty At Row "+i;
            else if(!(arr[i][0].trim().matches(pattern))){
                err+="Only Numbers Are Allowed In Roll Number Field Max 2 Digits Long.At Row "+i;
            }
            else if(!(arr[i][1].trim().matches(n))){
                err+="Only Alphabets And Spaces Are Allowed In Name Field. At Row "+i;
            }
            else if(!(arr[i][2].trim().matches(n))){
                err+="Only Alphabets And Spaces Are Allowed In Course Field. At Row "+i;
            }
            else if(!(arr[i][3].trim().matches(sem))){
                err+="Only Digits of Length 1 Are Allowed In Semester Field. At Row "+i;
            }
            else if(Integer.parseInt(arr[i][3].trim())>6 || Integer.parseInt(arr[i][3].trim())<1){
                err+="Semester Can't Be Greater Than 6 And Less Than 1 In Semester Field. At Row "+i;
            }
            else if(d.checkuni(arr[i][0].trim(),arr[i][1].trim(),arr[i][2].trim(),arr[i][3].trim(),arr[i][4].trim()))
                err+="Duplicate Entry Found In Database For Data At Row "+i;
            else{
                continue;
            }
        }
        if(err.equals("")){

            for(int i=0;i<arr.length;i++){
                d.insertstudent(arr[i][0].trim(),arr[i][1].trim(),arr[i][2].trim(),arr[i][3].trim(),arr[i][4].trim());
            }

            Toast.makeText(this,"Student Data Uploaded Successfully.",Toast.LENGTH_LONG).show();
            Cursor c=d.getStudent();
            String v="";
            if(c.moveToFirst()){
                do{
                    v+=c.getString(0)+"\n";
                    v+=c.getString(1)+"\n";
                    v+=c.getString(2)+"\n";
                    v+=c.getString(3)+"\n";
                } while (c.moveToNext());
                Toast.makeText(this,v,Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this,"Student Data Not Found.",Toast.LENGTH_LONG).show();
            }

        }
        else
        {
            Toast.makeText(this,err+"Error",Toast.LENGTH_LONG).show();
        }

    }
}
