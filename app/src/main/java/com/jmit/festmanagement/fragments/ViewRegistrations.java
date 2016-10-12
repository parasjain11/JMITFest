package com.jmit.festmanagement.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jmit.festmanagement.R;
import com.jmit.festmanagement.activities.MainActivity;
import com.jmit.festmanagement.adapters.EventAdapter;
import com.jmit.festmanagement.adapters.RegistrationAdapter;
import com.jmit.festmanagement.adapters.SpinnerEventAdapter;
import com.jmit.festmanagement.adapters.SpinnerFestAdapter;
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
import java.util.List;

/**
 * Created by arpitkh96 on 11/10/16.
 */

public class ViewRegistrations extends BaseFragment {
    EmptyRecyclerView emptyRecyclerView;
    ArrayList<Fest> festList;
    ArrayList<Event> eventList;
    ArrayList<Registration> registrations;
    MainActivity mainActivity;
    ArrayAdapter festdataAdapter, eventdataAdapter;
    Spinner spinnerFest, spinnerEvent;
    RegistrationAdapter registrationAdapter;
    boolean skipOnce,skipOnce2;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
        festList = mainActivity.getHeaderList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_registrations, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View rootView, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        skipOnce=skipOnce2=true;
        spinnerFest = (Spinner) rootView.findViewById(R.id.spinner_fest);
        spinnerEvent = (Spinner) rootView.findViewById(R.id.spinner_event);
        registrations = new ArrayList<>();
        if (festList == null)
            festList = new ArrayList<>();
        eventList = new ArrayList<>();
        if (savedInstanceState != null) {
            eventList = savedInstanceState.getParcelableArrayList("eventList");
            festList = savedInstanceState.getParcelableArrayList("festList");
            registrations = savedInstanceState.getParcelableArrayList("registrations");
        }
        initialiseList(rootView);

        festdataAdapter = new SpinnerFestAdapter(getActivity(), R.layout.spinner_row, festList);
        eventdataAdapter = new SpinnerEventAdapter(getActivity(), R.layout.spinner_row, eventList);
        spinnerFest.setAdapter(festdataAdapter);
        spinnerEvent.setAdapter(eventdataAdapter);
        spinnerFest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadEvents(festList.get(position).getFestId());
                eventList = new ArrayList<Event>();
                eventdataAdapter = new SpinnerEventAdapter(getActivity(), R.layout.spinner_row, eventList);
                spinnerEvent.setAdapter(eventdataAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerEvent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadRegistrations(festList.get(spinnerFest.getSelectedItemPosition()).getFestId(), eventList.get(spinnerEvent.getSelectedItemPosition()).getEventId());
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
        outState.putParcelableArrayList("festList", festList);
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
