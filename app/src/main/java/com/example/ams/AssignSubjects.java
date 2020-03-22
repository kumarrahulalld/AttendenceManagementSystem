package com.example.ams;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AssignSubjects extends AppCompatActivity  {
    String[] courses={"Select Course","MCA","BCA"};
    String[] subjects={"Select Paper","Paper I","Paper II","Paper III","Paper IV","Paper V","Paper VI","Lab"};
    String[] semester={"Select Semester","1","2","3","4","5","6"};
    List<String> fac=new ArrayList<String>();
    Database d=new Database(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_subjects);
        Spinner spin = (Spinner) findViewById(R.id.spinner_semester);
        Spinner spin1 = (Spinner) findViewById(R.id.spinner_course);
        Spinner spin2 = (Spinner) findViewById(R.id.spinner_name);
        Spinner spin3 = (Spinner) findViewById(R.id.spinner_subject);

        fac=d.getFaculty();
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_list_item_1,fac);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter ac = new ArrayAdapter(this,android.R.layout.simple_list_item_1,semester);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter ab = new ArrayAdapter(this,android.R.layout.simple_list_item_1,courses);
        ab.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter ad = new ArrayAdapter(this,android.R.layout.simple_list_item_1,subjects);
        ab.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin1.setAdapter(ab);
        spin.setAdapter(ac);
        spin2.setAdapter(aa);
        spin3.setAdapter(ad);
    }
    public void assignSubjects(View view){
       final EditText ed=(EditText)findViewById(R.id.editTextbatch);
        final AlertDialog alertDialog;
        final Spinner spin = (Spinner) findViewById(R.id.spinner_course);
        final Spinner spin1 = (Spinner) findViewById(R.id.spinner_semester);
        final Spinner spin2 = (Spinner) findViewById(R.id.spinner_name);
        final Spinner spin3 = (Spinner) findViewById(R.id.spinner_subject);
        if(spin1.getSelectedItemPosition()==0 || spin.getSelectedItemPosition()==0|| spin3.getSelectedItemPosition()==0)
            Toast.makeText(this,"Empty Field Found. Please Select Values.",Toast.LENGTH_LONG).show();
        else if(ed.getText().equals(""))
            Toast.makeText(this,"Batch Field Found Empty.",Toast.LENGTH_LONG).show();
        else
        {
            String re=d.getAssign(spin.getSelectedItem().toString(),spin1.getSelectedItem().toString(),spin3.getSelectedItem().toString(),ed.getText().toString());
            if((re.equals(""))){
                d.insertAssignment(spin.getSelectedItem().toString(),spin1.getSelectedItem().toString(),spin2.getSelectedItem().toString(),spin3.getSelectedItem().toString(),ed.getText().toString());
                Toast.makeText(this,"Subject Assigned Successfully.",Toast.LENGTH_LONG).show();
            }
            else {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Warning !");

                alertDialogBuilder.setMessage("This Paper Has Been Assigned To "+re+". Do You Want To Replace Faculty For This Subject.");
                        alertDialogBuilder.setPositiveButton("REPLACE",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        d.updateAssignment(spin.getSelectedItem().toString(),spin1.getSelectedItem().toString(),spin2.getSelectedItem().toString(),spin3.getSelectedItem().toString(),ed.getText().toString());
                                        Toast.makeText(AssignSubjects.this,"Subject Assigned Successfully.",Toast.LENGTH_LONG).show();
                                    }
                                });

                alertDialogBuilder.setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(AssignSubjects.this,"Thank You.",Toast.LENGTH_LONG).show();

                    }
                });

                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

        }


    }
}
