package com.jmit.festmanagement.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.jmit.festmanagement.R;
import com.jmit.festmanagement.utils.RequestCodes;
import com.jmit.festmanagement.utils.URL_API;
import com.jmit.festmanagement.utils.Utils;
import com.jmit.festmanagement.utils.VolleyHelper;

import java.util.HashMap;

public class AdminLogin extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final EditText Rollno = (EditText) findViewById(R.id.roll);
        final EditText Paswd = (EditText) findViewById(R.id.pwd);
        Button adminlogin = (Button) findViewById(R.id.adminLogin);
        adminlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Rollno.getText().toString() == null || Paswd.getText().toString() == null) {
                    Toast.makeText(getApplicationContext(), "Fill All The Details", Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String,String> hashMap=new HashMap<String, String>();
                    hashMap.put("admin_name",Rollno.getText().toString());
                    hashMap.put("admin_password",Paswd.getText().toString());
                    hashMap.put("device_id", Utils.getdeviceId(AdminLogin.this));
                    VolleyHelper.postRequestVolley(AdminLogin.this, URL_API.ADMIN_LOGIN,hashMap, RequestCodes.ADMIN_LOGIN);
                }
            }
        });

    }
    @Override
    public void requestStarted(int requestCode) {
        super.requestStarted(requestCode);
        showDialog();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(Home.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(( Home.class));
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
    }
    }