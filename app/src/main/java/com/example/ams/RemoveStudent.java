package com.example.ams;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RemoveStudent extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    String[] courses={"Select Course","MCA","BCA"};
    String[] semester={"Select Semester","1","2","3","4","5","6"};
    List<String> bt=new ArrayList<String>();
    Spinner b;
    Database d=new Database(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_student);
        bt=d.getBat();
        Spinner spin = (Spinner) findViewById(R.id.spinner_semester);
        Spinner spin1 = (Spinner) findViewById(R.id.spinner_course);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_list_item_1,semester);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter ab = new ArrayAdapter(this,android.R.layout.simple_list_item_1,courses);
        ab.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin1.setAdapter(ab);
        spin.setAdapter(aa);
        b=(Spinner)findViewById(R.id.spinner_batch);
        ArrayAdapter ae = new ArrayAdapter(this,android.R.layout.simple_list_item_1,bt);
        ae.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        b.setAdapter(ae);

    }

    public void removeStudent(View view){
        Spinner spin = (Spinner) findViewById(R.id.spinner_course);
        Spinner spin1 = (Spinner) findViewById(R.id.spinner_semester);
        Spinner spin2 = (Spinner) findViewById(R.id.spinner_name);
        if(spin.getSelectedItemPosition()==0 || spin1.getSelectedItemPosition()==0 || spin2.getSelectedItemPosition()==-1)
            Toast.makeText(this,"Empty Field Found. Please Select Values.",Toast.LENGTH_LONG).show();
        else
        {
            if(d.deleteStudent(spin.getSelectedItem().toString(),spin1.getSelectedItem().toString(),spin2.getSelectedItem().toString(),b.getSelectedItem().toString())){
                Toast.makeText(this,"Student Deleted Successfully.",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this,"Sorry Request Can't Be Processed Now.",Toast.LENGTH_LONG).show();
            }
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Spinner spin1 = (Spinner) findViewById(R.id.spinner_course);

        if(spin1.getSelectedItemPosition()==0)
            Toast.makeText(this,"Course Not Selected.",Toast.LENGTH_LONG).show();
        else if(position==0)
            Toast.makeText(this,"Semester Not Selected.",Toast.LENGTH_LONG).show();
        else {
            List<String> names = new ArrayList<String>();
            names = d.getName(courses[spin1.getSelectedItemPosition()], semester[position],b.getSelectedItem().toString(),"1");
            Spinner spinname = (Spinner) findViewById(R.id.spinner_name);
            ArrayAdapter ab = new ArrayAdapter(this, android.R.layout.simple_list_item_1, names);
            ab.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinname.setAdapter(ab);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
}
