package com.jmit.festmanagement.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jmit.festmanagement.R;
import com.jmit.festmanagement.activities.MainActivity;
import com.jmit.festmanagement.adapters.RegistrationAdapter;
import com.jmit.festmanagement.adapters.SpinnerEventAdapter;
import com.jmit.festmanagement.data.Event;
import com.jmit.festmanagement.data.Fest;
import com.jmit.festmanagement.data.Registration;
import com.jmit.festmanagement.utils.DataHandler;
import com.jmit.festmanagement.utils.EmptyRecyclerView;
import com.jmit.festmanagement.utils.FLog;
import com.jmit.festmanagement.utils.RequestCodes;
import com.jmit.festmanagement.utils.URL_API;
import com.jmit.festmanagement.utils.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by arpitkh96 on 11/10/16.
 */

public class ViewRegistrations extends BaseFragment {
    EmptyRecyclerView emptyRecyclerView;
    ArrayList<Event> eventList;
    ArrayList<Registration> registrations;
    MainActivity mainActivity;
    ArrayAdapter  eventdataAdapter;
    SharedPreferences sharedPreferences;
    Spinner spinnerEvent;
    RegistrationAdapter registrationAdapter;
    String fest_id;
    String fest_name;
    TextView fest_name_view;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
     }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_registrations, container, false);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
        fest_id=sharedPreferences.getString("fest_id",null);
        return rootView;
    }
    @Override
    public void onViewCreated(View rootView, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        spinnerEvent = (Spinner) rootView.findViewById(R.id.spinner_event);
        registrations = new ArrayList<>();
        eventList = new ArrayList<>();
        if (savedInstanceState != null) {
            eventList = savedInstanceState.getParcelableArrayList("eventList");
            registrations = savedInstanceState.getParcelableArrayList("registrations");
            fest_id=savedInstanceState.getString("fest_id");
            fest_name=savedInstanceState.getString("fest_name");
        }
        initialiseList(rootView);
        loadEvents(fest_id);
        if(mainActivity!=null)fest_name=mainActivity.getFestNameById(fest_id);
        fest_name_view=(TextView)rootView.findViewById(R.id.festLabel);
        fest_name_view.setText(fest_name);
        eventdataAdapter = new SpinnerEventAdapter(getActivity(), R.layout.spinner_row, eventList);
        spinnerEvent.setAdapter(eventdataAdapter);
        spinnerEvent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadRegistrations(fest_id, eventList.get(spinnerEvent.getSelectedItemPosition()).getEventId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("registrations", registrations);
        outState.putParcelableArrayList("eventList", eventList);
        outState.putString("fest_name",fest_name);
        outState.putString("fest_id",fest_id);
    }

    void loadEvents(String fest_id) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("fest_id", fest_id);
        VolleyHelper.postRequestVolley(getActivity(), this, URL_API.EVENTS, hashMap, RequestCodes.EVENTS);
    }

    void loadRegistrations(String fest_id, String event_id) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("fest_id", fest_id);
        hashMap.put("event_id", event_id);
        VolleyHelper.postRequestVolley(getActivity(), this, URL_API.ADMIN_REGISTERED_EVENTS, hashMap, RequestCodes.ADMIN_REGISTERED_EVENTS);
    }

    void initialiseList(View view) {
        emptyRecyclerView = (EmptyRecyclerView) view.findViewById(R.id.eventRecycler);
        emptyRecyclerView.setEmptyView((ContentLoadingProgressBar) view.findViewById(R.id.event_progressBar), view.findViewById(R.id.event_nodata));
        LinearLayoutManager manager1 = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        emptyRecyclerView.setLayoutManager(manager1);
        registrationAdapter = new RegistrationAdapter(registrations, getActivity());
        emptyRecyclerView.setAdapter(registrationAdapter);
    }

    @Override
    public void requestStarted(int requestCode) {
        if (requestCode == RequestCodes.ADMIN_REGISTERED_EVENTS || requestCode == RequestCodes.EVENTS) {
            emptyRecyclerView.setTaskRunning(true);
        }
    }

    @Override
    public void requestEndedWithError(int requestCode, VolleyError error) {
        super.requestEndedWithError(requestCode, error);

        if (requestCode == RequestCodes.ADMIN_REGISTERED_EVENTS || requestCode == RequestCodes.EVENTS) {
            emptyRecyclerView.setTaskRunning(false);
        }
    }

    @Override
    public void requestCompleted(int requestCode, String response) {
        FLog.d(response);

        if (requestCode == RequestCodes.EVENTS) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
                if (jsonObject.getInt("success") == 1) {
                    eventList = new ArrayList<>();
                    eventList = new Gson().fromJson(jsonObject.get("events").toString(), new TypeToken<ArrayList<Event>>() {
                    }.getType());
                    String fest_id = jsonObject.getString("fest_id");
                    for (Event event : eventList) {
                        event.setFest_id(fest_id);
                        event.setRegistered(false);
                        if (DataHandler.getRegistered_events().contains(event))
                            event.setRegistered(true);
                    }
                    eventdataAdapter = new SpinnerEventAdapter(getActivity(), R.layout.spinner_row, eventList);
                    spinnerEvent.setAdapter(eventdataAdapter);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (requestCode == RequestCodes.ADMIN_REGISTERED_EVENTS) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
                registrations = new ArrayList<>();
                if (jsonObject.getInt("success") == 1) {
                    registrations = new Gson().fromJson(jsonObject.get("registrations").toString(), new TypeToken<ArrayList<Registration>>() {
                    }.getType());
                }

                registrationAdapter.setHeaderList(registrations);
                registrationAdapter.notifyDataSetChanged();
                emptyRecyclerView.setTaskRunning(false);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
