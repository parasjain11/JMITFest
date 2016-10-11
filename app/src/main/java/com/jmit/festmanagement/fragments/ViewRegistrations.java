package com.jmit.festmanagement.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.jmit.festmanagement.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arpitkh96 on 11/10/16.
 */

public class ViewRegistrations extends BaseFragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_registrations, container, false);

        // Spinner element
        Spinner spinnerFest = (Spinner) rootView.findViewById(R.id.spinner_fest);
        Spinner spinnerEvent= (Spinner) rootView.findViewById(R.id.spinner_event);
        //spinnerFest.setOnItemSelectedListener(this);

        List<String> festList = new ArrayList<String>();
        festList.add("Automobile");
        festList.add("Business Services");
        festList.add("Computers");
        festList.add("Education");
        festList.add("Personal");
        festList.add("Travel");

        List<String> eventList = new ArrayList<String>();
        eventList.add("Automobile");
        eventList.add("Business Services");
        eventList.add("Computers");
        eventList.add("Education");
        eventList.add("Personal");
        eventList.add("Travel");


        // Creating adapter for spinner
        ArrayAdapter<String> festdataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, festList);
        ArrayAdapter<String> eventdataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, festList);
        // Drop down layout style - list view with radio button
        festdataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventdataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerFest.setAdapter(festdataAdapter);
        spinnerEvent.setAdapter(eventdataAdapter);

        return rootView;
    }


}
