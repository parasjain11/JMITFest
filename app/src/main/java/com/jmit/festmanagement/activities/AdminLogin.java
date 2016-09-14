package com.jmit.festmanagement.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jmit.festmanagement.R;

public class AdminLogin extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_login);
        EditText Rollno = (EditText) findViewById(R.id.roll);
        EditText Paswd = (EditText) findViewById(R.id.pwd);
        Button adminlogin = (Button) findViewById(R.id.adminLogin);
        adminlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a;
            }
        });

    }
}