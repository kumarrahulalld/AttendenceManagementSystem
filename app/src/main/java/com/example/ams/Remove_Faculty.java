package com.example.ams;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Remove_Faculty extends AppCompatActivity {
Database d=new Database(this);
    List<String> faculties=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove__faculty);

        faculties=d.getFaculty();
        Spinner spin = (Spinner) findViewById(R.id.spinner_faculty);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,faculties);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
    }

    public void removeFaculty(View view){
        Spinner s=(Spinner)findViewById(R.id.spinner_faculty);
        String sel=s.getSelectedItem().toString();
        if(s.getSelectedItem().toString().equals("No Faculty Found.")){
            Toast.makeText(this,"No Faculty Data Found To Remove.",Toast.LENGTH_LONG).show();
        }
        else{
        if(d.deleteFaculty(s.getSelectedItem().toString()))
        {
            faculties.remove(faculties.indexOf(sel));
            ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,faculties);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            s.setAdapter(aa);
            Toast.makeText(this,"Faculty Removed Successfully.",Toast.LENGTH_LONG).show();

        }
        else {
            Toast.makeText(this,"Unable To Process Your Request Now.",Toast.LENGTH_LONG).show();
        }
        }
    }
}
