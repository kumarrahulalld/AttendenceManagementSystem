package com.example.ams;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVReader;
import java.io.IOException;
import java.io.FileReader;
public class Teacher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
       // getIntent().getStringExtra("name").toString();
    }
    public void viewAttendence(View view){
        Intent i=new Intent(Teacher.this,ViewAttendFaculty.class);
        i.putExtra("name", getIntent().getStringExtra("name").toString());
        startActivity(i);
    }
    public void addAttendence(View view){
        Intent i=new Intent(Teacher.this,Attendence.class);
        i.putExtra("name", getIntent().getStringExtra("name").toString());
        startActivity(i);
    }

}
