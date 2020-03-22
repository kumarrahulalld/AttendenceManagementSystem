package com.example.ams;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
public class MainActivity extends AppCompatActivity {
    Database d=new Database(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void log(View view) {

        EditText u = (EditText) findViewById(R.id.editText);
        EditText p = (EditText) findViewById(R.id.editText5);
        Switch s = (Switch) findViewById(R.id.switch1);
       Cursor c=d.getData(u.getText().toString(),p.getText().toString());
        if(c==null){
            Toast.makeText(this,"Credentials Not Matched. Not A Registered User.",Toast.LENGTH_LONG).show();
        }
        else{
            if(c.moveToFirst()){
                String type=c.getString(4);
                if(type.equals("admin")){
                    Intent intent=new Intent(MainActivity.this,Admin.class);
                    startActivity(intent);
                }
                else {
                    Intent intent=new Intent(MainActivity.this,Teacher.class);
                    intent.putExtra("name",c.getString(1));
                    startActivity(intent);
                }
            }
        }

    }
    Switch s;
    public void forgotpass(View view) {
        EditText u=(EditText) findViewById(R.id.editText);
        EditText p=(EditText) findViewById(R.id.editText5);
        s=(Switch)findViewById(R.id.switch1);
        if (s.isChecked()) {
            if (u.getText().toString().equals("")) {
                Toast.makeText(this, "Username Field Can't Be Empty.", Toast.LENGTH_LONG).show();
            } else if (d.getData(u.getText().toString())) {
                Toast.makeText(this, "Credentials Not Matched. Not A Registered User.", Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(this, ""+d.getData(u.getText().toString()), Toast.LENGTH_LONG).show();
                BackgroundMail.newBuilder(this)
                        .withUsername("kumarrahul.allduniv@gmail.com")
                        .withPassword("Rm@1749001")
                        .withMailto("kumarrahul.allduniv@gmail.com")
                        .withType(BackgroundMail.TYPE_PLAIN)
                        .withSubject("---- RESET PASSWORD ----")
                        .withBody("Greetings From CCE,IPS,UoA Attendence Manager.\nDear User We Have Encountered A Request For Forgot Password For Account Registered With This Email.\nHere Is Your Password :- " + d.getData(u.getText().toString(),1) + "\nKindly Change Your Password , After Successful Login.\nThanks.")
                        .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                            @Override
                            public void onSuccess() {
                                //do some magic
                            }
                        })
                        .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                            @Override
                            public void onFail() {
                                // Toast.makeText(this,"Sorry ! Your Request Can't Be Processed Now.Please Try After Some Time",Toast.LENGTH_LONG).show();
                            }
                        })
                        .send();

            }
        }
    }
}
