package com.jmit.festmanagement.activities;

import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.jmit.festmanagement.adapters.EventAdapter;
import com.jmit.festmanagement.data.Event;
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

public class MainActivity extends BaseActivity implements View.OnClickListener, DrawerAdapter.OnItemClickListener, EventAdapter.OnItemClickListener {

    private EmptyRecyclerView drawerRecycler, eventRecycler;
    private ArrayList<Fest> headerList;
    ArrayList<Event> currentEventList;
    private DrawerAdapter customAdapter;
    EventAdapter eventAdapter;
    DrawerLayout drawer;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intialiseViews();
        headerList = new ArrayList<>();
        currentEventList = new ArrayList<>();
        title = getResources().getString(R.string.app_name);
        if (savedInstanceState == null)
            VolleyHelper.postRequestVolley(this, URL_API.FESTS, new HashMap<String, String>(), RequestCodes.FESTS);
        else {
            headerList = savedInstanceState.getParcelableArrayList("headerList");
            currentEventList = savedInstanceState.getParcelableArrayList("eventList");
            title = savedInstanceState.getString("title");
        }
        getSupportActionBar().setTitle(title);
        initialiseLists();
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
        outState.putParcelableArrayList("eventList", currentEventList);
        outState.putString("title", title);
    }

    @Override
    public void requestStarted(int requestCode) {
        super.requestStarted(requestCode);

        if (requestCode == RequestCodes.FESTS) {
            drawerRecycler.setTaskRunning(true);
        } else if (requestCode == RequestCodes.EVENTS) {
            eventRecycler.setTaskRunning(true);
        }
        else showDialog();
    }

    @Override
    public void requestEndedWithError(int requestCode, VolleyError error) {
        super.requestEndedWithError(requestCode, error);

        if (requestCode == RequestCodes.FESTS) {
            drawerRecycler.setTaskRunning(false);
        } else if (requestCode == RequestCodes.EVENTS) {
            eventRecycler.setTaskRunning(false);
        }
        else dismissDialog();
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
        } else if (requestCode == RequestCodes.EVENTS) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
                currentEventList = new Gson().fromJson(jsonObject.get("events").toString(), new TypeToken<ArrayList<Event>>() {
                }.getType());
                String fest_id = jsonObject.getString("fest_id");
                for (Event event : currentEventList)
                    event.setFest_id(fest_id);
                eventAdapter.setHeaderList(currentEventList);
                eventAdapter.notifyDataSetChanged();
                eventRecycler.checkIfEmpty();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == RequestCodes.FESTS) {
            drawerRecycler.setTaskRunning(false);
        } else if (requestCode == RequestCodes.EVENTS) {
            eventRecycler.setTaskRunning(false);
        }
        else dismissDialog();
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
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("fest_id", headerList.get(item).getFestId());
        getSupportActionBar().setTitle(headerList.get(item).getFestName());
        title = headerList.get(item).getFestName();
        VolleyHelper.postRequestVolley(this, URL_API.EVENTS, hashMap, RequestCodes.EVENTS);
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
        eventRecycler = (EmptyRecyclerView) findViewById(R.id.eventRecycler);
        eventRecycler.setEmptyView((ContentLoadingProgressBar) findViewById(R.id.event_progressBar), findViewById(R.id.event_nodata));

    }

    void initialiseLists() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        drawerRecycler.setLayoutManager(manager);
        customAdapter = new DrawerAdapter(headerList, getApplicationContext());
        customAdapter.setOnItemClickListener(this);
        drawerRecycler.setAdapter(customAdapter);
        drawerRecycler.checkIfEmpty();
        LinearLayoutManager manager1 = new LinearLayoutManager(this);
        eventRecycler.setLayoutManager(manager1);
        eventAdapter = new EventAdapter(currentEventList, getApplicationContext());
        eventAdapter.setOnItemClickListener(this);
        eventRecycler.setAdapter(eventAdapter);
        eventRecycler.checkIfEmpty();

    }

    @Override
    public void onEventItemClick(Event event) {

    }

    @Override
    public void onRegisterClick(Event event) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("fest_id", event.getFest_id());
        hashMap.put("event_id", event.getEventId());
        hashMap.put("user_id", PreferenceManager.getDefaultSharedPreferences(this).getString("uid", null));
        VolleyHelper.postRequestVolley(this, URL_API.REGISTRATION, hashMap, RequestCodes.REGISTRATION);

    }
}
