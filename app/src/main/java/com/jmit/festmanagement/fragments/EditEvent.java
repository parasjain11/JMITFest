package com.jmit.festmanagement.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jmit.festmanagement.R;
import com.jmit.festmanagement.activities.MainActivity;
import com.jmit.festmanagement.adapters.SpinnerEventAdapter;
import com.jmit.festmanagement.data.Event;
import com.jmit.festmanagement.utils.DataHandler;
import com.jmit.festmanagement.utils.RequestCodes;
import com.jmit.festmanagement.utils.URL_API;
import com.jmit.festmanagement.utils.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by arpitkh96 on 11/10/16.
 */
public class EditEvent extends BaseFragment {

    EditText etStartTime,etEndTime,etStartDate,etEndDate,name,venue,desc;
    String fest_id;
    String fest_name;
    TextView fest_name_view;
    MainActivity mainActivity;
    ArrayAdapter eventdataAdapter;
    SharedPreferences sharedPreferences;
    Spinner spinnerEvent;
    ArrayList<Event> eventList;
    String event_id;
    View rootView;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }
    void loadEvents(String fest_id) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("fest_id", fest_id);
        VolleyHelper.postRequestVolley(getActivity(), this, URL_API.EVENTS, hashMap, RequestCodes.EVENTS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_event, container, false);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
        fest_id=sharedPreferences.getString("fest_id",null);

        etStartTime=(EditText)rootView.findViewById(R.id.tfStartTime);
        etEndTime=(EditText)rootView.findViewById(R.id.tfEndTime);
        etStartDate=(EditText) rootView.findViewById(R.id.tfStartDate);
        etEndDate=(EditText) rootView.findViewById(R.id.tfEndDate);
        name=(EditText)rootView.findViewById(R.id.event_name);
        desc=(EditText) rootView.findViewById(R.id.event_desc);
        venue=(EditText) rootView.findViewById(R.id.etVenue);
        rootView.findViewById(R.id.event_chooser).setVisibility(View.VISIBLE);
        if(mainActivity!=null)fest_name=mainActivity.getFestNameById(fest_id);
        fest_name_view=(TextView)rootView.findViewById(R.id.festLabel);
        fest_name_view.setText(fest_name);
        eventList=new ArrayList<>();
        if (savedInstanceState != null) {
            eventList = savedInstanceState.getParcelableArrayList("eventList");
            fest_id=savedInstanceState.getString("fest_id");
            fest_name=savedInstanceState.getString("fest_name");
        }
        final SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        final SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
        etStartTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                if(!etStartTime.getText().toString().isEmpty()){
                    try {
                        Date date = null;
                        date = sdf.parse(etStartTime.getText().toString());
                        Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
                        calendar.setTime(date);
                        hour=calendar.get(Calendar.HOUR);
                        minute=calendar.get(Calendar.MINUTE);
                        // assigns calendar to given date
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR,selectedHour>12?selectedHour-12:selectedHour+12);
                        calendar.set(Calendar.MINUTE,selectedMinute);
                        String date=sdf.format(calendar.getTime());
                        etStartTime.setText(date);

                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        etEndTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                if(!etEndTime.getText().toString().isEmpty()){
                    try {
                        Date date = null;
                        date = sdf.parse(etEndTime.getText().toString());
                        Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
                        calendar.setTime(date);
                        hour=calendar.get(Calendar.HOUR);
                        minute=calendar.get(Calendar.MINUTE);
                        // assigns calendar to given date
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        Calendar calendar = Calendar.getInstance();// creates a new calendar instance
                        calendar.set(Calendar.HOUR,selectedHour>12?selectedHour-12:selectedHour+12);
                        calendar.set(Calendar.MINUTE,selectedMinute);
                        String date=sdf.format(calendar.getTime());
                        etEndTime.setText(date);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        etStartDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                if(!etStartDate.getText().toString().isEmpty()){
                    try {
                        Date date = null;
                        date = dt1.parse(etStartDate.getText().toString());
                        Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
                        calendar.setTime(date);
                        mMonth=calendar.get(Calendar.MONTH);
                        mDay=calendar.get(Calendar.DAY_OF_MONTH);
                        mYear=calendar.get(Calendar.YEAR);
                        // assigns calendar to given date
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();// creates a new calendar instance
                                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                calendar.set(Calendar.MONTH,monthOfYear);
                                calendar.set(Calendar.YEAR,year);
                                String date=dt1.format(calendar.getTime());
                                etStartDate.setText(date);

                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        });

        etEndDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                if(!etEndDate.getText().toString().isEmpty()){
                    try {
                        Date date = null;
                        date = dt1.parse(etStartDate.getText().toString());
                        Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
                        calendar.setTime(date);
                        mMonth=calendar.get(Calendar.MONTH);
                        mDay=calendar.get(Calendar.DAY_OF_MONTH);
                        mYear=calendar.get(Calendar.YEAR);
                        // assigns calendar to given date
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();// creates a new calendar instance
                                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                calendar.set(Calendar.MONTH,monthOfYear);
                                calendar.set(Calendar.YEAR,year);
                                String date=dt1.format(calendar.getTime());
                                etEndDate.setText(date);

                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        Button button=(Button) rootView.findViewById(R.id.btn_add_event);
        button.setText("DONE");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddClick(event_id);
            }
        });
        spinnerEvent=(Spinner)rootView.findViewById(R.id.spinner_event);
        eventdataAdapter = new SpinnerEventAdapter(getActivity(), R.layout.spinner_row, eventList);
        spinnerEvent.setAdapter(eventdataAdapter);
        spinnerEvent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(eventList.get(spinnerEvent.getSelectedItemPosition()).getEventId().equals("-1"))return;
                Event  event=eventList.get(spinnerEvent.getSelectedItemPosition());
                event_id=event.getEventId();
                name.setText(event.getEventName());
                venue.setText(event.getVenue());
                etStartDate.setText(event.getStartDate());
                etStartTime.setText(event.getStartTime());
                etEndDate.setText(event.getEndDate());
                etEndTime.setText(event.getEndTime());
                desc.setText(event.getEventDesc());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        loadEvents(fest_id);

        return rootView;
    }
    @Override
    public void requestStarted(int requestCode) {
        super.requestStarted(requestCode);
        if(mainActivity!=null)
            mainActivity.showDialog();
    }

    @Override
    public void requestCompleted(int requestCode, String response) {
        super.requestCompleted(requestCode, response);
        if(mainActivity!=null)
            mainActivity.dismissDialog();
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
                    Event event=new Event();
                    event.setEventId("-1");
                    event.setEventName("Select Event");
                    eventList.add(0,event);
                    eventdataAdapter = new SpinnerEventAdapter(getActivity(), R.layout.spinner_row, eventList);
                    spinnerEvent.setAdapter(eventdataAdapter);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(requestCode==RequestCodes.EDIT_EVENT){
            try {
                JSONObject jsonObject=new JSONObject(response);
                Snackbar.make(rootView,jsonObject.getString("message"),Snackbar.LENGTH_LONG).show();
                if(mainActivity!=null)
                    mainActivity.fillList(-2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void requestEndedWithError(int requestCode, VolleyError error) {
        super.requestEndedWithError(requestCode, error);
        if(mainActivity!=null)
            mainActivity.dismissDialog();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("eventList", eventList);
        outState.putString("fest_name",fest_name);
        outState.putString("fest_id",fest_id);
    }
    void onAddClick(String event_id){
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("fest_id",fest_id);
        hashMap.put("event_id",event_id);
        hashMap.put("event_name",name.getText().toString());
        hashMap.put("event_desc",desc.getText().toString());
        hashMap.put("venue",venue.getText().toString());
        hashMap.put("start_date",etStartDate.getText().toString());
        hashMap.put("end_date",etEndDate.getText().toString());
        hashMap.put("start_time",etStartTime.getText().toString());
        hashMap.put("end_time",etEndTime.getText().toString());
        VolleyHelper.postRequestVolley(getActivity(),this, URL_API.EDIT_EVENT,hashMap, RequestCodes.EDIT_EVENT);
    }
    public void timePopUp(View view)
    {
        //showDialog(TIME_DIALOG_ID);
    }

}
