package com.example.ams;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Add_Faculty extends AppCompatActivity {
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Database d=new Database(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__faculty);
    }

    public void RegFaculty(View view){
        EditText n=(EditText)findViewById(R.id.editText);
        EditText e=(EditText)findViewById(R.id.editText1);
        EditText p=(EditText)findViewById(R.id.editText2);
        String email=e.getText().toString();
        boolean b=email.matches(emailPattern);
        if(n.getText().toString().equals("")){
            Toast.makeText(this,"Name Field Can't Be Empty.",Toast.LENGTH_LONG).show();
        }
        else if(e.getText().toString().equals("")){
            Toast.makeText(this,"Email Field Can't Be Empty.",Toast.LENGTH_LONG).show();
        }
        else if(p.getText().toString().equals("")){
            Toast.makeText(this,"Password Field Can't Be Empty.",Toast.LENGTH_LONG).show();
        }
        else if(b==false){
            Toast.makeText(this,"Email Id Is Invalid."+email+n.getText().toString()+p.getText().toString(),Toast.LENGTH_LONG).show();
        }
        else if(!(d.getData(email))){
            Toast.makeText(this,"Faculty Already Registered With This Email Id.",Toast.LENGTH_LONG).show();
        }
        else
        {
            d.insertUser(n.getText().toString().trim(),email,p.getText().toString().trim(),"teacher");
            Toast.makeText(this,"Faculty Registered Successfully.",Toast.LENGTH_LONG).show();
        }

    }
}
