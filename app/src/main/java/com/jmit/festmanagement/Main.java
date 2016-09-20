package com.jmit.festmanagement;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by arpitkh996 on 06-09-2016.
 */


import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by arpitkh996 on 06-09-2016.
 */

public class Main extends Application {
    private RequestQueue mRequestQueue;
    private static Main mInstance;
    public static synchronized Main getInstance() {
        return mInstance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());

    }
    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public <T> void add(Request<T> req) {
        req.setTag("tag");
        getRequestQueue().add(req);
    }

    public void cancel() {
        mRequestQueue.cancelAll("tag");
    }

}
