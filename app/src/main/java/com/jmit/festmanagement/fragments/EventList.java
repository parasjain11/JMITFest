package com.jmit.festmanagement.fragments;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jmit.festmanagement.R;
import com.jmit.festmanagement.adapters.EventAdapter;
import com.jmit.festmanagement.data.Event;
import com.jmit.festmanagement.utils.DataHandler;
import com.jmit.festmanagement.utils.EmptyRecyclerView;
import com.jmit.festmanagement.utils.RequestCodes;
import com.jmit.festmanagement.utils.URL_API;
import com.jmit.festmanagement.utils.VolleyHelper;
import com.jmit.festmanagement.utils.VolleyInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by arpitkh96 on 10/10/16.
 */

public class EventList extends Fragment implements VolleyInterface, EventAdapter.OnItemClickListener, DataHandler.RegisteredEventsListener {
    ArrayList<Event> currentEventList;
    EmptyRecyclerView eventRecycler;
    EventAdapter eventAdapter;
    String uid;

    public static EventList newInstance(int mode, String fest_id) {
        Bundle args = new Bundle();
        args.putInt("mode", mode);
        args.putString("fest_id", fest_id);
        EventList fragment = new EventList();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("uid", null);
        currentEventList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return getActivity().getLayoutInflater().inflate(R.layout.event_list, null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("list", currentEventList);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialiseList(view);
        String fest_id = getArguments().getString("fest_id");
        int mode = getArguments().getInt("mode");
        if (savedInstanceState == null) {
            if (fest_id != null) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("fest_id", fest_id);
                VolleyHelper.postRequestVolley(getActivity(), this, URL_API.EVENTS, hashMap, RequestCodes.EVENTS);
            } else if (mode == -2) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("user_id", uid);
                VolleyHelper.postRequestVolley(getActivity(), this, URL_API.REGISTERED_EVENTS, hashMap, RequestCodes.GET_REGISTRATION);
            }
        } else {
            currentEventList = savedInstanceState.getParcelableArrayList("list");
            initialiseList(view);
        }
    }

    @Override
    public void requestStarted(int requestCode) {
        if (requestCode == RequestCodes.EVENTS || requestCode == RequestCodes.GET_REGISTRATION) {
            eventRecycler.setTaskRunning(true);
        }
    }

    @Override
    public void requestCompleted(int requestCode, String response) {
        if (requestCode == RequestCodes.EVENTS) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
                if (jsonObject.getInt("success") == 1) {
                    currentEventList = new Gson().fromJson(jsonObject.get("events").toString(), new TypeToken<ArrayList<Event>>() {
                    }.getType());
                    String fest_id = jsonObject.getString("fest_id");
                    for (Event event : currentEventList) {
                        event.setFest_id(fest_id);
                        if (DataHandler.getRegistered_events().contains(event))
                            event.setRegistered(true);
                    }
                    eventAdapter.setHeaderList(currentEventList);
                    eventAdapter.notifyDataSetChanged();
                    eventRecycler.checkIfEmpty();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (requestCode == RequestCodes.GET_REGISTRATION) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
                if (jsonObject.getInt("success") == 1) {
                    currentEventList = new Gson().fromJson(jsonObject.get("registrations").toString(), new TypeToken<ArrayList<Event>>() {
                    }.getType());
                    for (Event e : currentEventList) e.setRegistered(true);
                    DataHandler.setRegistered_events(currentEventList, false);
                    eventAdapter.setHeaderList(currentEventList);
                    eventAdapter.notifyDataSetChanged();
                    eventRecycler.checkIfEmpty();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (requestCode == RequestCodes.REGISTRATION) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
                jsonObject.getInt("success");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == RequestCodes.EVENTS || requestCode == RequestCodes.GET_REGISTRATION) {
            eventRecycler.setTaskRunning(false);
        }
    }

    @Override
    public void requestEndedWithError(int requestCode, VolleyError error) {
        if (requestCode == RequestCodes.EVENTS || requestCode == RequestCodes.GET_REGISTRATION) {
            eventRecycler.setTaskRunning(false);
        }
    }

    void initialiseList(View view) {
        eventRecycler = (EmptyRecyclerView) view.findViewById(R.id.eventRecycler);
        eventRecycler.setEmptyView((ContentLoadingProgressBar) view.findViewById(R.id.event_progressBar), view.findViewById(R.id.event_nodata));
        LinearLayoutManager manager1 = new LinearLayoutManager(getActivity());
        eventRecycler.setLayoutManager(manager1);
        eventAdapter = new EventAdapter(currentEventList, getActivity());
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
        hashMap.put("user_id", uid);
        VolleyHelper.postRequestVolley(getActivity(), this, URL_API.REGISTRATION, hashMap, RequestCodes.REGISTRATION);

    }

    @Override
    public void onRegisteredEventsChanged(ArrayList<Event> events) {
        for (Event event : currentEventList) {
            if (DataHandler.getRegistered_events().contains(event))
                event.setRegistered(true);
        }

        eventAdapter.setHeaderList(currentEventList);
        eventAdapter.notifyDataSetChanged();
        eventRecycler.checkIfEmpty();
    }
}