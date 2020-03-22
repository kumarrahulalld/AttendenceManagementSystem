package com.example.ams;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

public class Admin extends AppCompatActivity {
    Intent intent;
    Database db=new Database(this);
    Dialog dialog;
    final String[] items = {"Promote All Students To New Semester.", "Remove 6th Semester Students.", "Clear All Assigned Subjects."};
    final ArrayList itemsSelected = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
    }

    public void addStudent(View view) {
        intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/csv");
        startActivityForResult(intent, 7);
    }

    public void remove(View view){
    Intent i=new Intent(Admin.this,RemoveStudent.class);
    startActivity(i);

    }
    public void remfac(View view){
        Intent i=new Intent(Admin.this,Remove_Faculty.class);
        startActivity(i);

    }
    public void viewAttd(View view){
        Intent i=new Intent(Admin.this,ViewAttendence.class);
        startActivity(i);

    }


    public void addFaculty(View view){
        Intent i=new Intent(Admin.this,Add_Faculty.class);
        startActivity(i);
    }

    public void assignSubject(View view){
            Intent i=new Intent(Admin.this,AssignSubjects.class);
            startActivity(i);
        }

        public void viewData(View view){
            Intent i=new Intent(Admin.this,ViewData.class);
            i.putExtra("Source","admin");
            startActivity(i);
        }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case 7:

                if (resultCode == RESULT_OK) {

                    Uri PathHolder = data.getData();
                    String p="";
                    p+=data.getData().getPath();

                    DocumentFile d = DocumentFile.fromSingleUri(this, PathHolder);
                    if (d != null) {
                        String n=d.getName();
                        if(p.endsWith(".csv")){
                            Toast.makeText(Admin.this, p, Toast.LENGTH_LONG).show();
                            Intent i = new Intent(Admin.this, CsvData.class);
                            i.putExtra("path", p);
                            startActivity(i);
                           // p+= d.getName();
                        }
                        else{
                            Toast.makeText(Admin.this, "Only CSV Files Are Allowed.", Toast.LENGTH_LONG).show();
                            intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/csv");
                            startActivityForResult(intent, 7);
                        }


                    }


                }
                break;

        }
    }

    public void resetSemester(View view){
        AlertDialog.Builder ab=new AlertDialog.Builder(this);
        ab.setTitle("Warning ! Choose Operations To Perform.");
        final boolean[] checkedItems = {false, false, false};
       // ab.setMessage("This Operation Will Do Following Things.\n Select Opeartions That You Want To Perform And Press Reset Button. Else Press Cancel.");
        ab.setMultiChoiceItems(items, checkedItems,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedItemId,
                                        boolean isSelected) {
                        if (isSelected) {
                            itemsSelected.add(selectedItemId);
                        } else if (itemsSelected.contains(selectedItemId)) {
                            itemsSelected.remove(Integer.valueOf(selectedItemId));
                        }
                    }
                })
                .setPositiveButton("Reset !", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Your logic when OK button is clicked
                        if(itemsSelected.isEmpty()){
                            Toast.makeText(Admin.this,"No Operation Selected.",Toast.LENGTH_LONG).show();
                        }
                        if(checkedItems[0]==true){
                            if(db.promoteStudent())
                                Toast.makeText(Admin.this,"Students Promoted.",Toast.LENGTH_LONG).show();
                        }
                        if(checkedItems[1]==true){
                            if(db.removeLastSem()){
                                Toast.makeText(Admin.this,"Removed 6th Semester Students Successfully.",Toast.LENGTH_LONG).show();
                            }
                        }
                        if(checkedItems[2]==true){
                            if(db.cancelAssignment()){
                                Toast.makeText(Admin.this,"Assigned Subjects Removed Successfully.",Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(Admin.this,"Reset Clicked.",Toast.LENGTH_LONG).show();
                    }
                });
        dialog = ab.create();
        dialog.show();


    }
}
