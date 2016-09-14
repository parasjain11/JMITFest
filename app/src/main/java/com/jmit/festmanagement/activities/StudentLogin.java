package com.jmit.festmanagement.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jmit.festmanagement.R;

public class StudentLogin extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stulogin);
        EditText Rollno = (EditText)findViewById(R.id.editText);
        Button stulogin = (Button)findViewById(R.id.stuLogin);
        TextView signUp = (TextView)findViewById(R.id.SignUp);
        SpannableString content = new SpannableString("Sign Up?");
        content.setSpan(new UnderlineSpan(),0,content.length(),0);
        signUp.setText(content);
        stulogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentLogin.this,MainActivity.class));

            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent b;
            }
        });
    }

}
