package com.jmit.festmanagement.utils;

import android.content.Context;
import android.support.v7.app.AlertDialog;

/**
 * Created by arpitkh996 on 11-07-2016.
 */

//Dialog without background
public class CustomDialog extends AlertDialog {
    public CustomDialog(Context context) {
        super(context);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

}


