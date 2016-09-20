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
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.jmit.festmanagement.R;
import com.jmit.festmanagement.utils.URL_API;
import com.jmit.festmanagement.utils.Utils;
import com.jmit.festmanagement.utils.VolleyHelper;
import com.jmit.festmanagement.utils.VolleyInterface;

import java.util.HashMap;

public class StudentLogin extends AppCompatActivity implements VolleyInterface{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stulogin);
        final EditText Rollno = (EditText)findViewById(R.id.editText);
        Button stulogin = (Button)findViewById(R.id.stuLogin);
        TextView signUp = (TextView)findViewById(R.id.SignUp);
        SpannableString content = new SpannableString("Sign Up?");
        content.setSpan(new UnderlineSpan(),0,content.length(),0);
        signUp.setText(content);
        stulogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> hashMap=new HashMap<String, String>();
                hashMap.put("roll_no",Rollno.getText().toString());
                hashMap.put("device_id", Utils.getdeviceId(StudentLogin.this));
                VolleyHelper.postRequestVolley(StudentLogin.this, URL_API.LOGIN_API,hashMap,1);
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentLogin.this,SignUpActivity.class));
                finish();
            }
        });
    }

    @Override
    public void requestStarted(int requestCode) {
        Toast.makeText(this,"request started",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void requestCompleted(int requestCode, String response) {
        Toast.makeText(this,response,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void requestEndedWithError(int requestCode, VolleyError error) {
        Toast.makeText(this,error.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
    }
}
