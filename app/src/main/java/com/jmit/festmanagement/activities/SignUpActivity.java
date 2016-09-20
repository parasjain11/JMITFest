package com.jmit.festmanagement.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.jmit.festmanagement.R;
import com.jmit.festmanagement.utils.RequestCodes;
import com.jmit.festmanagement.utils.URL_API;
import com.jmit.festmanagement.utils.Utils;
import com.jmit.festmanagement.utils.VolleyHelper;

import org.json.JSONObject;

import java.util.HashMap;

public class SignUpActivity extends BaseActivity {
    EditText rollno, name, department, phoneno, email, cemail;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        rollno = (EditText) findViewById(R.id.roll);
        name = (EditText) findViewById(R.id.fName);
        department = (EditText) findViewById(R.id.department);
        phoneno = (EditText) findViewById(R.id.phone);
        email = (EditText) findViewById(R.id.Email);
        cemail = (EditText) findViewById(R.id.cemail);
        signup=(Button)findViewById(R.id.btn_signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rollno.getText().toString() == null || name.getText().toString() == null || department.getText().toString() == null || phoneno.getText().toString() == null || email.getText().toString() == null || cemail.getText().toString() == null) {
                    Toast.makeText(getApplicationContext(), "Fill All The Details", Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String,String> hashMap=new HashMap<String, String>();
                    hashMap.put("roll_no",rollno.getText().toString());
                    hashMap.put("username",name.getText().toString());
                    hashMap.put("department",department.getText().toString());
                    hashMap.put("ph_no",phoneno.getText().toString());
                    hashMap.put("email",email.getText().toString());
                    VolleyHelper.postRequestVolley(SignUpActivity.this, URL_API.SIGN_UP,hashMap, RequestCodes.SIGN_UP);
                }
            }
        });

    }

    @Override
    public void requestCompleted(int requestCode, String response) {
        super.requestCompleted(requestCode, response);
        try {
            JSONObject jsonObject=new JSONObject(response);
            int i=jsonObject.getInt("success");
            if(i==1){
                Toast.makeText(this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,StudentLogin.class));
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
