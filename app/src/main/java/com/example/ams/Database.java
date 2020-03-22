package com.example.ams;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
public class Database extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "ams.db";
    public Database(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table user " +
                        "(id integer primary key autoincrement, name text not null ,email text not null unique, password text not null,type text not null)"
        );
        db.execSQL(
                "create table student " +
                        "(rollno integer not null unique, name text not null ,course text not null, semester text not null,batch text not null,status text not null)"
        );
        db.execSQL(
                "create table attendence " +
                        "(id integer primary key autoincrement, name text not null ,course text not null,startTime text not null,endTime text not null, semester text not null,paper text not null,date text not null,batch text,total integer not null,present text not null)"
        );
        db.execSQL(
                "create table assignment " +
                        "(id integer primary key autoincrement, course text not null ,semester text not null, name text not null,paper text not null,batch text not null)"
        );

    }
    public boolean takenAttendance(String c,String s,String d,String p,String b)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from attendence where course='"+c+"'and semester='"+s+"' and paper='"+p+"' and date='"+d+"' and batch='"+b+"'", null );
        if(res.moveToFirst())
        return true;
        else
            return false;
    }
    public boolean  DupliAttendance(String c,String s,String d,Integer st,Integer et,String b)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from attendence where course='"+c+"'and semester='"+s+"' and (startTime<="+st+" or endTime>="+et+") and date='"+d+"' and batch='"+b+"'", null );
        if(res.moveToNext())
            return true;
        else
            return false;
    }
    public String getAssign(String c,String s,String p,String b) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select name from assignment where course='"+c+"'and semester='"+s+"' and paper='"+p+"' and batch='"+b+"'", null );
        String m="";
        if(res.moveToFirst()) {
            m = res.getString(0);
            res.close();
        }
        return m;

    }
    public List<String> getAtt(){
        List<String> d=new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select distinct batch from student", null );
        if(res.moveToFirst()){
            do{
                if(res.getString(0)!=null)
                d.add(res.getString(0));
            }while(res.moveToNext());
        }
    return d;
    }
    public List<String> getAttc(){
        List<String> d=new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select distinct course from student", null );
        if(res.moveToFirst()){
            do{
                d.add(res.getString(0));
            }while(res.moveToNext());
        }
        return d;
    }
    public List<String> getCourse(String n,String b){
        List<String> d=new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select distinct course from assignment where name='"+n+"'and batch='"+b+"'", null );
        if(res.moveToFirst()){
            do{
                d.add(res.getString(0));
            }while(res.moveToNext());
        }
        return d;
    }
    public List<String> getSem(String n,String b,String c){
        List<String> d=new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select distinct semester from assignment where name='"+n+"'and batch='"+b+"' and course='"+c+"'", null );
        if(res.moveToFirst()){
            do{
                d.add(res.getString(0));
            }while(res.moveToNext());
        }
        return d;
    }
    public List<String> getPaper(String n,String b,String c,String s){
        List<String> d=new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select distinct paper from assignment where name='"+n+"'and batch='"+b+"' and course='"+c+"' and semester='"+s+"'", null );
        if(res.moveToFirst()){
            do{
                d.add(res.getString(0));
            }while(res.moveToNext());
        }
        return d;
    }
    public List<String> getAtts(){
        List<String> d=new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select distinct semester from student", null );
        if(res.moveToFirst()){
            do{
                d.add(res.getString(0));
            }while(res.moveToNext());
        }
        return d;
    }
    public List<String> getBatch(String n){
        List<String> d=new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select distinct batch from assignment where name='"+n+"'", null );
        if(res.moveToFirst()){
            do{
                if(res.getString(0)!=null)
                d.add(res.getString(0));
            }while(res.moveToNext());
        }
        return d;
    }

    public List<String> getBat(){
        List<String> d=new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select distinct batch from student", null );
        if(res.moveToFirst()){
            do{
                if(res.getString(0)!=null)
                    d.add(res.getString(0));
            }while(res.moveToNext());
        }
        return d;
    }
    public List<List<String>> getTable(String b,String c,String s,String p,String ds,String de){
        List<List<String>> l=new ArrayList<>();
        List<String> d=new ArrayList<String>();
        List<String> n=new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select distinct date from attendence where batch='"+b+"' and course='"+c+"' and semester='"+s+"' and paper='"+p+"' and date>=('"+de+"') and date<=('"+ds+"')", null );
       if(res.moveToFirst()){
           d.add("NAME");
           do{
               d.add(res.getString(0));
           }while(res.moveToNext());
           d.add("TOTAL CLASSES");
           d.add("ATTENDED CLASSES");
           d.add("MISSED CLASSES");
           d.add("ATTENDENCE PERCENTAGE");
           l.add(d);

       }
       n=getNames(c,s,b,"1");
       for(int i=0;i<n.size();i++){
           List<String> pr=new ArrayList<String>();
           int prs=0;
           int as=0;
           pr.add(n.get(i));
           for(int j=1;j<d.size();j++){
               Cursor rs=db.rawQuery("select present from attendence where name='"+n.get(i)+"' and date='"+d.get(j)+"' and batch='"+b+"' and course='"+c+"' and semester='"+s+"' and paper='"+p+"' ",null);
                if(rs.moveToFirst()){
                    String val=rs.getString(0);
                    if(val.equals("true"))
                    {    pr.add("P");
                    prs++;}
                    else{
                        pr.add("A");
                        as++;}
                }
           }
           pr.add(String.valueOf(d.size()-5));
           pr.add(String.valueOf(prs));
           pr.add(String.valueOf(as));
           double ors= (prs/(d.size()-5.0))*100.0;
           pr.add(ors+"%");
           l.add(pr);
          // pr.clear();

       }

        // Toast.makeText(this,"select * from attendence where batch='"+b+"' and course='"+c+"' and semester='"+s+"' and paper='"+p+"' and date>='"+ds+"' and date<='"+de+"'",Toast.LENGTH_LONG).show();
        return l ;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS student");
        onCreate(db);
    }
    public Boolean getData(String e) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select email from user where email='"+e+"'", null );
        if(res.moveToFirst()){
            String m=res.getString(0);
            res.close();
            if(m.equals(e))
                return false;
            else
                return true;
        }
        else
            return true;

    }
    public boolean checkuni(String r,String n,String c,String s,String b){

        SQLiteDatabase db = this.getReadableDatabase();
            Cursor res = db.rawQuery("select name from student where rollno='" + r + "' and name='" + n + "' and course='" + c + "' and semester='" + s + "' and batch='"+b+"'", null);

        String a="";
        if(res.moveToFirst()) {
             a= res.getString(0);
            res.close();

        }
        if(a.equals(""))
            return false;
        else
            return true;

    }

    public boolean deleteStudent(String c,String s, String n,String b){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update student set status='0' where course='"+c+"' and name='"+n+"' and semester='"+s+"' and batch='"+b+"'");
      return true;
    }

    public boolean deleteas(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from assignment");
            return true;
    }
    public boolean updateAssignment(String c,String s, String n,String p,String b){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update assignment set name='"+n+"' where course='"+c+"' and semester='"+s+"' and paper='"+p+"' and batch='"+b+"' ");
        return true;
    }
    public boolean insertAssignment(String c,String s, String n,String p,String b){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", n);
        contentValues.put("course", c);
        contentValues.put("paper", p);
        contentValues.put("semester", s);
        contentValues.put("batch", b);
        db.insert("assignment", null, contentValues);
        return true;
    }

    public boolean insertAttendance(String c,String s,String n,String p,String d,String st,String et,String total,String present,String b){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", n);
        contentValues.put("course", c);
        contentValues.put("paper", p);
        contentValues.put("date", d);
        contentValues.put("semester", s);
        contentValues.put("present", present);
        contentValues.put("total", total);
        contentValues.put("startTime", st);
        contentValues.put("endTime", et);
        contentValues.put("batch", b);
        db.insert("attendence", null, contentValues);
        return true;
    }
    public boolean updatetAttendance(String c,String s,String n,String p,String d,String st,String et,String total,String present,String b){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update attendence set present='"+present+"'where course='"+c+"' and semester='"+s+"' and name='"+n+"' and paper='"+p+"' and date='"+d+"' and startTime='"+st+"'and endTime='"+et+"' and total='"+total+"' and batch='"+b+"'");
        return true;
    }

    public boolean deleteFaculty(String n){
        SQLiteDatabase db = this.getWritableDatabase();
        int a=db.delete("user","name=?",new String[]{n});
        if(a==0)
            return false;
        else
            return true;
    }
    public String getData(String e,int a) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select password from user where email='"+e+"'", null );
        String m="";
        if(res.moveToFirst()) {
             m = res.getString(0);
            res.close();
        }
        return m;

    }
    public List<List<String>> getAttend(String b,String c,String s,String ds,String de) {
        List<List<String>> l = new ArrayList<>();
        List<String> d = new ArrayList<String>();
        List<String> pp = new ArrayList<String>();
        List<String> n = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select distinct paper from attendence where batch='" + b + "' and course='" + c + "' and semester='" + s + "'", null);
        if (res.moveToFirst()) {
            do {
                pp.add(res.getString(0));
            } while (res.moveToNext());
            for (int k = 0; k < pp.size(); k++) {
                Cursor res1 = db.rawQuery("select distinct date from attendence where batch='" + b + "' and course='" + c + "' and semester='" + s + "' and paper='" + pp.get(k) + "' and date>=('" + de + "') and date<=('" + ds + "')", null);
                if (res1.moveToFirst()) {
                    d.add("PAPER");
                    d.add("NAME");
                    do {
                        d.add(res1.getString(0));
                    } while (res1.moveToNext());
                    d.add("TOTAL CLASSES");
                    d.add("ATTENDED CLASSES");
                    d.add("MISSED CLASSES");
                    d.add("ATTENDENCE PERCENTAGE");
                    l.add(d);

                }
                n = getNames(c, s, b, "1");
                for (int i = 0; i < n.size(); i++) {
                    List<String> pr = new ArrayList<String>();
                    int prs = 0;
                    int as = 0;
                    pr.add(pp.get(k));
                    pr.add(n.get(i));
                    for (int j = 1; j < d.size(); j++) {
                        Cursor rs = db.rawQuery("select present from attendence where name='" + n.get(i) + "' and date='" + d.get(j) + "' and batch='" + b + "' and course='" + c + "' and semester='" + s + "' and paper='" + pp.get(k) + "' ", null);
                        if (rs.moveToFirst()) {
                            String val = rs.getString(0);
                            if (val.equals("true")) {
                                pr.add("P");
                                prs++;
                            } else {
                                pr.add("A");
                                as++;
                            }
                        }
                    }
                    pr.add(String.valueOf(d.size() - 6));
                    pr.add(String.valueOf(prs));
                    pr.add(String.valueOf(as));
                    double ors = (prs / (d.size() - 6.0)) * 100.0;
                    pr.add(ors + "%");
                    l.add(pr);
                    // pr.clear();

                }




            }
        }
        return l;
    }

    public List<String> getName(String c, String s,String b,String t) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select name from student where course='"+c+"' and semester='"+s+"' and batch='"+b+"' and status='"+t+"'", null );
        List<String> list=new ArrayList<String>();
        if(res.moveToFirst()) {
            do{
                list.add(res.getString(0));
            }while(res.moveToNext());
            res.close();
        }
        return list;

    }
    public List<String> getNames(String c, String s,String b,String t) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select distinct name from student where course='"+c+"' and semester='"+s+"' and batch='"+b+"' and status='"+t+"'", null );
        List<String> list=new ArrayList<String>();
        if(res.moveToFirst()) {
            do{
                list.add(res.getString(0));
            }while(res.moveToNext());
            res.close();
        }
        return list;

    }


    public void insertUser (String name, String email,String password,String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        if(getData(email)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", name);
            contentValues.put("email", email);
            contentValues.put("password", password);
            contentValues.put("type", type);
            db.insert("user", null, contentValues);
        }
    }
    public void insertstudent(String r,String n,String c,String s,String b){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("rollno",r);
        contentValues.put("name", n);
        contentValues.put("course", c.toUpperCase());
        contentValues.put("semester", s);
        contentValues.put("batch", b);
        contentValues.put("status", "1");
        db.insert("student", null, contentValues);
    }

    public Cursor getStudent() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from student", null );
        return res;
    }
    public List<String> getFaculty() {
        List<String> l=new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from user where type='teacher'", null );
        if(res.moveToFirst()){
            do{
                l.add(res.getString(1));
            }while(res.moveToNext());
        }
        else
            l.add("No Faculty Found.");
        return l;
    }
    public boolean promoteStudent(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update student set semester= (semester+1) where semester<6 and status='1'");
        return true;
    }
    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from user", null );
        return res;
    }
    public Cursor getFacultyData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from user where type='teacher'", null );
        return res;
    }
    public Cursor getAssignmentData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from assignment", null );
        return res;
    }
    public boolean removeLastSem(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update student set status ='0' where semester='6'");
            return true;
    }
    public Cursor getData(String e,String p) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from user where email='"+e+"'and password='"+p+"'", null );
        return res;
    }

    public boolean cancelAssignment(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from assignment");


        return true;
    }
    public List<String> returnCourse(String n,String b){
        List<String> name=new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select distinct course from assignment where name='"+n+"' and batch='"+b+"'", null );
        if(res.moveToFirst()){
            do{
                name.add(res.getString(0));
            }while (res.moveToNext());
        }
        return name;
    }
    public List<String> returnSemester(String n,String c,String b){
        List<String> name=new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select distinct semester from assignment where name='"+n+"' and course='"+c+"' and batch='"+b+"'", null );
        if(res.moveToFirst()){
            do{
                name.add(res.getString(0));
            }while (res.moveToNext());
        }
        return name;
    }
    public List<String> returnSubjects(String n,String c,String s,String b){
        List<String> name=new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select distinct paper from assignment where name='"+n+"' and course='"+c+"' and semester='"+s+"' and batch='"+b+"'", null );
        if(res.moveToFirst()){
            do{
                name.add(res.getString(0));
            }while (res.moveToNext());
        }
        return name;
    }


}
