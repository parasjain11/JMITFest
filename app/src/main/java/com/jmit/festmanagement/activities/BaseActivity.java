package com.jmit.festmanagement.activities;

import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.jmit.festmanagement.R;
import com.jmit.festmanagement.utils.CustomDialog;
import com.jmit.festmanagement.utils.FLog;
import com.jmit.festmanagement.utils.VolleyInterface;

public class BaseActivity extends AppCompatActivity implements VolleyInterface {
    boolean showing = false;
    CustomDialog alertDialog;

    void showDialog() {
        showing = true;
        if (alertDialog == null) {
            alertDialog = new CustomDialog(this);
            alertDialog.setView(getLayoutInflater().inflate(R.layout.progressbar, null));
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (showing)
                    alertDialog.show();
            }
        }, 150);
    }

    void dismissDialog() {
        showing = false;
        if (alertDialog != null && alertDialog.isShowing())
            alertDialog.dismiss();
    }

    @Override
    public void requestStarted(int requestCode) {
        showDialog();
    }

    @Override
    public void requestCompleted(int requestCode, String response) {
        dismissDialog();
        FLog.d(response);
    }

    @Override
    public void requestEndedWithError(int requestCode, VolleyError error) {
        dismissDialog();
        FLog.d(error.getMessage());
        String msg = "";
        if (error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof NetworkError) {
            msg = getResources().getString(R.string.network_error);
        } else if (error instanceof ParseError) {
            msg = "Response Parse Error";
        } else if (error instanceof AuthFailureError) {
            msg = "Authentication Failure Error";
        } else if (error instanceof ServerError) {
            msg = "Server Error";
        }
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
