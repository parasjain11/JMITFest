package com.jmit.festmanagement.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jmit.festmanagement.R;
import com.jmit.festmanagement.adapters.DrawerAdapter;
import com.jmit.festmanagement.data.Event;
import com.jmit.festmanagement.data.Fest;
import com.jmit.festmanagement.fragments.EventList;
import com.jmit.festmanagement.utils.DataHandler;
import com.jmit.festmanagement.utils.EmptyRecyclerView;
import com.jmit.festmanagement.utils.RequestCodes;
import com.jmit.festmanagement.utils.URL_API;
import com.jmit.festmanagement.utils.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends BaseActivity implements View.OnClickListener, DrawerAdapter.OnItemClickListener{

    private EmptyRecyclerView drawerRecycler;
    private ArrayList<Fest> headerList;
    private DrawerAdapter customAdapter;
    DrawerLayout drawer;
    String title;
    String uid;
    int mode = -2;
    boolean isAdmin=false;
    SharedPreferences sharedPreferences;
    /*
    *-3 for home (not present yet)
    * -2 for MyEvents
    * 0,1,2...for fests
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intialiseViews();
        sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
        uid = sharedPreferences.getString("uid", null);
        String user=sharedPreferences.getString("user",""),email=sharedPreferences.getString("email","");
        isAdmin=sharedPreferences.getBoolean("isAdmin",false);
        if(isAdmin)
            findViewById(R.id.adminPanel).setVisibility(View.VISIBLE);
        TextView textView=(TextView)findViewById(R.id.username);
        textView.setText(user);
        TextView email1=(TextView)findViewById(R.id.email);
        email1.setText(email);
        headerList = new ArrayList<>();
        title = getResources().getString(R.string.app_name);
        if (mode == -2)
            title = "My Events";
        else if (mode == -3)
            title = "Home";
        if (savedInstanceState == null) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("user_id", uid);
            VolleyHelper.postRequestVolley(this, URL_API.REGISTERED_EVENTS, hashMap, RequestCodes.GET_REGISTRATION);
            VolleyHelper.postRequestVolley(this, URL_API.FESTS, new HashMap<String, String>(), RequestCodes.FESTS);
            fillList(-2);
        } else {
            headerList = savedInstanceState.getParcelableArrayList("headerList");
            title = savedInstanceState.getString("title");
            mode = savedInstanceState.getInt("mode");
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("fest_id", "2");
        hashMap.put("event_id", "1");
        getSupportActionBar().setTitle(title);
        initialiseLists(savedInstanceState != null);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("headerList", headerList);
        outState.putInt("mode", mode);
        outState.putString("title", title);
    }

    @Override
    public void requestStarted(int requestCode) {
        super.requestStarted(requestCode);

        if (requestCode == RequestCodes.FESTS) {
            drawerRecycler.setTaskRunning(true);
        }
    }

    @Override
    public void requestEndedWithError(int requestCode, VolleyError error) {
        super.requestEndedWithError(requestCode, error);
        if (requestCode == RequestCodes.FESTS) {
            drawerRecycler.setTaskRunning(false);
        }
    }

    @Override
    public void requestCompleted(int requestCode, String response) {
        super.requestCompleted(requestCode, response);
        if (requestCode == RequestCodes.FESTS) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                headerList = new Gson().fromJson(jsonObject.get("fests").toString(), new TypeToken<ArrayList<Fest>>() {
                }.getType());
                customAdapter.setHeaderList(headerList);
                customAdapter.notifyDataSetChanged();
                drawerRecycler.checkIfEmpty();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (requestCode == RequestCodes.GET_REGISTRATION) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
                int i = jsonObject.getInt("success");
                if (i == 1) {
                    ArrayList<Event> arrayList = new Gson().fromJson(jsonObject.get("registrations").toString(), new TypeToken<ArrayList<Event>>() {
                    }.getType());
                    DataHandler.setRegistered_events(arrayList,true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == RequestCodes.FESTS) {
            drawerRecycler.setTaskRunning(false);
        }
    }
/*

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
*/


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDrawerItemClick(int item) {
        mode = item;
        title = headerList.get(item).getFestName();
        getSupportActionBar().setTitle(title);
        addEventsFragment(0,headerList.get(item).getFestId());
        if (drawer != null)
            drawer.closeDrawer(GravityCompat.START);
    }

    void intialiseViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        drawerRecycler = (EmptyRecyclerView) findViewById(R.id.recyclerview);
        drawerRecycler.setEmptyView((ContentLoadingProgressBar) findViewById(R.id.progressBar), findViewById(R.id.nodata));
        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putString("uid", null).commit();
                startActivity(new Intent(MainActivity.this, SplashActivity.class));
                finish();
            }
        });
    }

    void initialiseLists(boolean rotate) {
        LinearLayoutManager manager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        drawerRecycler.setLayoutManager(manager);
        customAdapter = new DrawerAdapter(headerList, getApplicationContext());
        customAdapter.setOnItemClickListener(this);
        drawerRecycler.setAdapter(customAdapter);
        drawerRecycler.checkIfEmpty();

    }

    void fillList(int mode) {

        if (mode == -2) {

            title = "My Events";
            getSupportActionBar().setTitle(title);
            addEventsFragment(-2,null);

        }
        this.mode = mode;
    }
    void addEventsFragment(int mode,String fest_id){
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_frame, EventList.newInstance(mode,fest_id));
        transaction.commit();
    }
    public void myEvents(View v) {
        fillList(-2);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void home(View v) {
        fillList(-3);
        drawer.closeDrawer(GravityCompat.START);
    }

}
