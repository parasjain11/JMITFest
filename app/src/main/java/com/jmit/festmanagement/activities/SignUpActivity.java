package com.jmit.festmanagement.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
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
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                    hashMap.put("user_name",name.getText().toString());
                    hashMap.put("department",department.getText().toString());
                    hashMap.put("ph_no",phoneno.getText().toString());
                    hashMap.put("email",email.getText().toString());
                    VolleyHelper.postRequestVolley(SignUpActivity.this, URL_API.SIGN_UP,hashMap, RequestCodes.SIGN_UP);
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(this,StudentLogin.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(StudentLogin.class);

    }

    @Override
    public void requestStarted(int requestCode) {
        super.requestStarted(requestCode);
        showDialog();
    }

    @Override
    public void requestEndedWithError(int requestCode, VolleyError error) {
        super.requestEndedWithError(requestCode, error);
        dismissDialog();
    }

    @Override
    public void requestCompleted(int requestCode, String response) {
        super.requestCompleted(requestCode, response);
        dismissDialog();
        try {
            JSONObject jsonObject=new JSONObject(response);
            int i=jsonObject.getInt("success");
            if(i==1){
                Toast.makeText(this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                startActivity(StudentLogin.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
