package com.example.ams;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Attendence extends AppCompatActivity {
    Database d=new Database(this);
    Spinner spin1;
    Dialog dialog;
    Dialog dialog1;
      ArrayAdapter ad=null;
List<String> courses=new ArrayList<String>();
    List<String> semesters=new ArrayList<String>();
    String name="";
    List<String> subjects=new ArrayList<String>();
    List<String> r=new ArrayList<String>();
    String[] times={"Choose Start Time","8","9","10","11","12","13","14","15","16"};
    String[] times1={"Choose End Time","9","10","11","12","13","14","15","16","17"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence);
        DatePicker dp=(DatePicker)findViewById(R.id.datePicker1);
        Calendar c=Calendar.getInstance();
        dp.setMaxDate(c.getTimeInMillis());
        c.add(Calendar.DATE,-7);
        dp.setMinDate(c.getTimeInMillis());
        name=getIntent().getExtras().getString("name");
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
        final Spinner spin0=(Spinner)findViewById(R.id.spinner_batch);

        r=d.getAtt();
       ArrayAdapter az = new ArrayAdapter(this,android.R.layout.simple_list_item_1,r);
        az.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin0.setAdapter(az);

        courses=d.returnCourse(name,spin0.getSelectedItem().toString());
         spin1 = (Spinner) findViewById(R.id.spinner_course);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_list_item_1,courses);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setAdapter(aa);

        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                semesters=d.returnSemester(name,spin1.getSelectedItem().toString(),spin0.getSelectedItem().toString());
                semes();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spin2 = (Spinner) findViewById(R.id.spinner_time);
        ArrayAdapter ab = new ArrayAdapter(this,android.R.layout.simple_list_item_1,times);
        ab.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin2.setAdapter(ab);
        Spinner spin3 = (Spinner) findViewById(R.id.spinner_time1);
        ArrayAdapter ac = new ArrayAdapter(this,android.R.layout.simple_list_item_1,times1);
        ac.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin3.setAdapter(ac);
    }
    public void semes(){
        final Spinner spin4 = (Spinner) findViewById(R.id.spinner_semester);
        final Spinner spin0=(Spinner)findViewById(R.id.spinner_batch);
        ad= new ArrayAdapter(this,android.R.layout.simple_list_item_1,semesters);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin4.setAdapter(ad);
        spin4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subjects=d.returnSubjects(name,spin1.getSelectedItem().toString(),spin4.getSelectedItem().toString(),spin0.getSelectedItem().toString());
                subs();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void subs(){
        Spinner spin4 = (Spinner) findViewById(R.id.spinner_subject);
        ad= new ArrayAdapter(this,android.R.layout.simple_list_item_1,subjects);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin4.setAdapter(ad);
    }
    public void attend(View view){
       final int a;
         final Spinner spin4 = (Spinner) findViewById(R.id.spinner_time);
        final Spinner course = (Spinner) findViewById(R.id.spinner_course);
        final Spinner semes = (Spinner) findViewById(R.id.spinner_semester);
        final Spinner paper = (Spinner) findViewById(R.id.spinner_subject);
        final DatePicker datePicker=(DatePicker)findViewById(R.id.datePicker1);
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();
        final Spinner spin0=(Spinner)findViewById(R.id.spinner_batch);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        String pattern;
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        final String dte=dateFormat.format(calendar.getTime());
        //Toast.makeText(this,"Date"+dte,Toast.LENGTH_LONG).show();
        final Spinner spin5 = (Spinner) findViewById(R.id.spinner_time1);
        if(spin4.getSelectedItemPosition()==0 || spin5.getSelectedItemPosition()==0){
            Toast.makeText(this,"Time Span Field Can't Be Empty.",Toast.LENGTH_LONG).show();
        }
        else {
            a=(Integer.parseInt(spin5.getSelectedItem().toString()))-(Integer.parseInt(spin4.getSelectedItem().toString()));
            if (a<=0) {
                Toast.makeText(this, "Time Span Fields Are Invalid Duration Can't Be Lesser Than Or Equal To 0.", Toast.LENGTH_LONG).show();
            }
            else{
                    if(d.takenAttendance(course.getSelectedItem().toString(),semes.getSelectedItem().toString(),dte,paper.getSelectedItem().toString(),spin0.getSelectedItem().toString()))
                {
                    Toast.makeText(this, "Attendence Has Been Posted For This Date Of This Subject For Selected Semester And Course..", Toast.LENGTH_LONG).show();
                    AlertDialog.Builder up=new AlertDialog.Builder(this);
                    up.setTitle("Warning !");
                    up.setMessage("Attendance Has Been Posted For This Subject Of This Semester For This Date. Do You Want To Update Attendance.");
                    up.setPositiveButton("Update !", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                            final ArrayList itemsSelected = new ArrayList();
                            List<String> nm=new ArrayList<String>();
                            nm=d.getName(course.getSelectedItem().toString(),semes.getSelectedItem().toString(),spin0.getSelectedItem().toString(),"1");
                            nm.add(0,"All Present");
                            nm.add(1,"All Absent");
                            final String[] names=new String[nm.size()];
                            int i=0;
                            for(String e:nm)
                            {
                                names[i]=e;
                                i++;
                            }

                            final boolean[] stat=new boolean[nm.size()];
                            AlertDialog.Builder al=new AlertDialog.Builder(Attendence.this);
                            al.setTitle("Enter Attendence");
                            al.setMultiChoiceItems(names,stat,new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int selectedItemId,
                                                    boolean isSelected) {
                                    if (isSelected) {
                                        if(selectedItemId==0){
                                            ListView dialogListView = ((AlertDialog)dialog).getListView();
                                            for (int position = 2; position < dialogListView.getChildCount(); position++) {
                                                if (!itemsSelected.contains(position)) {
                                                    // Check items, disable and make them unclickable((CheckedTextView)dialogListView.getChildAt(position)).setChecked(true);
                                                    dialogListView.getChildAt(position).setEnabled(false);
                                                    dialogListView.setItemChecked(position,true);
                                                    dialogListView.getChildAt(position).setClickable(true);
                                                }
                                                dialogListView.getChildAt(1).setEnabled(false);
                                                dialogListView.setItemChecked(1,false);
                                                dialogListView.getChildAt(1).setClickable(false);
                                            }
                                        }
                                        else  if(selectedItemId==1){
                                            ListView dialogListView = ((AlertDialog)dialog).getListView();
                                            for (int position = 2; position < dialogListView.getChildCount(); position++) {
                                                if (!itemsSelected.contains(position)) {
                                                    // Check items, disable and make them unclickable((CheckedTextView)dialogListView.getChildAt(position)).setChecked(true);
                                                    dialogListView.getChildAt(position).setEnabled(false);
                                                    dialogListView.setItemChecked(position,false);
                                                    dialogListView.getChildAt(position).setClickable(true);
                                                } }
                                            dialogListView.getChildAt(0).setEnabled(false);
                                            dialogListView.setItemChecked(0,false);
                                            dialogListView.getChildAt(0).setClickable(true);
                                        }
                                        itemsSelected.add(selectedItemId);
                                    } else if (itemsSelected.contains(selectedItemId)) {
                                        if(selectedItemId==0){
                                            ListView dialogListView = ((AlertDialog)dialog).getListView();
                                            for (int position = 0; position < dialogListView.getChildCount(); position++) {
                                                if (!itemsSelected.contains(position)) {
                                                    // Check items, disable and make them unclickable((CheckedTextView)dialogListView.getChildAt(position)).setChecked(true);
                                                    dialogListView.getChildAt(position).setEnabled(true);
                                                    dialogListView.setItemChecked(position,false);
                                                    dialogListView.getChildAt(position).setClickable(false);
                                                } }
                                        }
                                        else  if(selectedItemId==1){
                                            ListView dialogListView = ((AlertDialog)dialog).getListView();
                                            for (int position = 0; position < dialogListView.getChildCount(); position++) {
                                                if (!itemsSelected.contains(position)) {
                                                    // Check items, disable and make them unclickable((CheckedTextView)dialogListView.getChildAt(position)).setChecked(true);
                                                    dialogListView.getChildAt(position).setEnabled(true);
                                                    dialogListView.setItemChecked(position,false);
                                                    dialogListView.getChildAt(position).setClickable(false);
                                                } }
                                        }
                                        itemsSelected.remove(Integer.valueOf(selectedItemId));
                                    }
                                }
                            })
                                    .setPositiveButton("Post Attendance !", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {

                                            if(stat[0]==true){
                                                for(int l=2;l<stat.length;l++){
                                                    d.updatetAttendance(course.getSelectedItem().toString(),semes.getSelectedItem().toString(),names[l],paper.getSelectedItem().toString(),dte,spin4.getSelectedItem().toString(),spin5.getSelectedItem().toString(),String.valueOf(a),"true",spin0.getSelectedItem().toString());
                                                }
                                            }
                                            else if(stat[1]==true)
                                            {
                                                for(int l=2;l<stat.length;l++){
                                                    d.updatetAttendance(course.getSelectedItem().toString(),semes.getSelectedItem().toString(),names[l],paper.getSelectedItem().toString(),dte,spin4.getSelectedItem().toString(),spin5.getSelectedItem().toString(),String.valueOf(a),"false",spin0.getSelectedItem().toString());
                                                }
                                            }
                                            else
                                            {
                                                for(int l=2;l<stat.length;l++){

                                                    d.updatetAttendance(course.getSelectedItem().toString(),semes.getSelectedItem().toString(),names[l],paper.getSelectedItem().toString(),dte,spin4.getSelectedItem().toString(),spin5.getSelectedItem().toString(),String.valueOf(a),String.valueOf(stat[l]),spin0.getSelectedItem().toString());
                                                }
                                            }
                                            Toast.makeText(Attendence.this,"Attendence Posted Successfully.",Toast.LENGTH_LONG).show();


                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            Toast.makeText(Attendence.this,"Cancel Clicked.",Toast.LENGTH_LONG).show();
                                        }
                                    });
                            dialog1 = al.create();
                            dialog1.show();



                    }
                })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(Attendence.this,"Cancel Clicked.",Toast.LENGTH_LONG).show();
                            }
                        });
                    dialog = up.create();
                    dialog.show();
                }
                    else{
                    if(d.DupliAttendance(course.getSelectedItem().toString(),semes.getSelectedItem().toString(),dte,Integer.parseInt(spin4.getSelectedItem().toString()),Integer.parseInt(spin5.getSelectedItem().toString()),spin0.getSelectedItem().toString())) {
                        Toast.makeText(this, "Invalid Time Span For This Subject For Selected Semester And Course.Because They Were Busy In Attending Another Class.", Toast.LENGTH_LONG).show();
                    }
                    else {
                        final ArrayList itemsSelected = new ArrayList();
                        List<String> nm=new ArrayList<String>();
                        nm=d.getName(course.getSelectedItem().toString(),semes.getSelectedItem().toString(),spin0.getSelectedItem().toString(),"1");
                        nm.add(0,"All Present");
                        nm.add(1,"All Absent");
                        final String[] names=new String[nm.size()];
                        int i=0;
                        for(String e:nm)
                        {
                            names[i]=e;
                            i++;
                        }

                       final boolean[] stat=new boolean[nm.size()];
                        AlertDialog.Builder al=new AlertDialog.Builder(this);
                        al.setTitle("Enter Attendence");
                        al.setMultiChoiceItems(names,stat,new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedItemId,
                                                boolean isSelected) {
                                if (isSelected) {
                                    if(selectedItemId==0){
                                        ListView dialogListView = ((AlertDialog)dialog).getListView();
                                        for (int position = 2; position < dialogListView.getChildCount(); position++) {
                                            if (!itemsSelected.contains(position)) {
                                                // Check items, disable and make them unclickable((CheckedTextView)dialogListView.getChildAt(position)).setChecked(true);
                                                dialogListView.getChildAt(position).setEnabled(false);
                                                dialogListView.setItemChecked(position,true);
                                                dialogListView.getChildAt(position).setClickable(true);
                                            }
                                            dialogListView.getChildAt(1).setEnabled(false);
                                            dialogListView.setItemChecked(1,false);
                                            dialogListView.getChildAt(1).setClickable(false);
                                        }
                                    }
                                    else  if(selectedItemId==1){
                                        ListView dialogListView = ((AlertDialog)dialog).getListView();
                                        for (int position = 2; position < dialogListView.getChildCount(); position++) {
                                            if (!itemsSelected.contains(position)) {
                                                // Check items, disable and make them unclickable((CheckedTextView)dialogListView.getChildAt(position)).setChecked(true);
                                                dialogListView.getChildAt(position).setEnabled(false);
                                                dialogListView.setItemChecked(position,false);
                                                dialogListView.getChildAt(position).setClickable(true);
                                            } }
                                        dialogListView.getChildAt(0).setEnabled(false);
                                        dialogListView.setItemChecked(0,false);
                                        dialogListView.getChildAt(0).setClickable(true);
                                    }
                                    itemsSelected.add(selectedItemId);
                                } else if (itemsSelected.contains(selectedItemId)) {
                                    if(selectedItemId==0){
                                        ListView dialogListView = ((AlertDialog)dialog).getListView();
                                        for (int position = 0; position < dialogListView.getChildCount(); position++) {
                                            if (!itemsSelected.contains(position)) {
                                                // Check items, disable and make them unclickable((CheckedTextView)dialogListView.getChildAt(position)).setChecked(true);
                                                dialogListView.getChildAt(position).setEnabled(true);
                                                dialogListView.setItemChecked(position,false);
                                                dialogListView.getChildAt(position).setClickable(false);
                                            } }
                                    }
                                    else  if(selectedItemId==1){
                                        ListView dialogListView = ((AlertDialog)dialog).getListView();
                                        for (int position = 0; position < dialogListView.getChildCount(); position++) {
                                            if (!itemsSelected.contains(position)) {
                                                // Check items, disable and make them unclickable((CheckedTextView)dialogListView.getChildAt(position)).setChecked(true);
                                                dialogListView.getChildAt(position).setEnabled(true);
                                                dialogListView.setItemChecked(position,false);
                                                dialogListView.getChildAt(position).setClickable(false);
                                            } }
                                    }
                                    itemsSelected.remove(Integer.valueOf(selectedItemId));
                                }
                            }
                        })
                                .setPositiveButton("Post Attendance !", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {

                                    if(stat[0]==true){
                                        for(int l=2;l<stat.length;l++){
                                            d.insertAttendance(course.getSelectedItem().toString(),semes.getSelectedItem().toString(),names[l],paper.getSelectedItem().toString(),dte,spin4.getSelectedItem().toString(),spin5.getSelectedItem().toString(),String.valueOf(a),"true",spin0.getSelectedItem().toString());
                                        }
                                    }
                                    else if(stat[1]==true)
                                    {
                                        for(int l=2;l<stat.length;l++){
                                            d.insertAttendance(course.getSelectedItem().toString(),semes.getSelectedItem().toString(),names[l],paper.getSelectedItem().toString(),dte,spin4.getSelectedItem().toString(),spin5.getSelectedItem().toString(),String.valueOf(a),"false",spin0.getSelectedItem().toString());
                                        }
                                    }
                                    else
                                    {
                                        for(int l=2;l<stat.length;l++){

                                            d.insertAttendance(course.getSelectedItem().toString(),semes.getSelectedItem().toString(),names[l],paper.getSelectedItem().toString(),dte,spin4.getSelectedItem().toString(),spin5.getSelectedItem().toString(),String.valueOf(a),String.valueOf(stat[l]),spin0.getSelectedItem().toString());
                                        }
                                    }
                                        Toast.makeText(Attendence.this,"Attendence Posted Successfully.",Toast.LENGTH_LONG).show();


                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        Toast.makeText(Attendence.this,"Cancel Clicked.",Toast.LENGTH_LONG).show();
                                    }
                                });
                        dialog = al.create();
                        dialog.show();
                    }
                    }

            }
        }
    }

}
