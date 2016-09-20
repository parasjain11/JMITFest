package com.jmit.festmanagement.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jmit.festmanagement.R;

public class SignUpActivity extends AppCompatActivity {
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
                if (rollno.getText().toString() != null && name.getText().toString() != null && department.getText().toString() != null && phoneno.getText().toString() != null && email.getText().toString() != null && cemail.getText().toString() != null) {
                    Toast.makeText(getApplicationContext(), "Fill All The Blanks", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Successfully Sign Up", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
