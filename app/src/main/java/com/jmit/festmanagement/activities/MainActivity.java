package com.jmit.festmanagement.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jmit.festmanagement.R;
import com.jmit.festmanagement.adapters.DrawerAdapter;
import com.jmit.festmanagement.data.Fest;
import com.jmit.festmanagement.utils.EmptyRecyclerView;
import com.jmit.festmanagement.utils.RequestCodes;
import com.jmit.festmanagement.utils.URL_API;
import com.jmit.festmanagement.utils.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener,DrawerAdapter.OnItemClickListener {

    private EmptyRecyclerView recyclerView;
    private ArrayList<Fest> headerList;
    private DrawerAdapter customAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        headerList=new ArrayList<>();
        recyclerView = (EmptyRecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setEmptyView((ContentLoadingProgressBar)findViewById(R.id.progressBar),findViewById(R.id.nodata));
        if(savedInstanceState==null)
            VolleyHelper.postRequestVolley(this, URL_API.FESTS, new HashMap<String, String>(), RequestCodes.FESTS);
        else
            headerList=savedInstanceState.getParcelableArrayList("headerList");
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        customAdapter = new DrawerAdapter(headerList, getApplicationContext());
        customAdapter.setOnItemClickListener(this);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(customAdapter);
        recyclerView.checkIfEmpty();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("headerList",headerList);
    }

    @Override
    public void requestStarted(int requestCode) {
        super.requestStarted(requestCode);
        if(requestCode==RequestCodes.FESTS)
            recyclerView.setTaskRunning(true);
    }

    @Override
    public void requestEndedWithError(int requestCode, VolleyError error) {
        super.requestEndedWithError(requestCode, error);
        if(requestCode==RequestCodes.FESTS)
            recyclerView.setTaskRunning(false);
    }

    @Override
    public void requestCompleted(int requestCode, String response) {
        super.requestCompleted(requestCode, response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            headerList = new Gson().fromJson(jsonObject.get("fests").toString(), new TypeToken<ArrayList<Fest>>() {
            }.getType());
            customAdapter.setHeaderList(headerList);
            customAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(requestCode==RequestCodes.FESTS)
            recyclerView.setTaskRunning(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDrawerItemClick(int item) {

    }
}
