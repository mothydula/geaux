package com.example.geaux;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static com.example.geaux.ItineraryItems.itineraryEventArrayAdapter;
import static com.example.geaux.MainActivity.NEW_ITINERARY;
import static com.example.geaux.MainActivity.currentItinerary;

public class Itinerary extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener , TimePickerDialog.OnTimeSetListener{
    private ItineraryObject itinerary;
    private String newDateTime = "";
    private String newDate = "";
    private String newTime = "";
    private String timeOfDay = "";
    private String newDescription;
    private boolean addingToggle = false;
    public static ItineraryItem currentEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary);

        TextView itinTitle = (TextView)findViewById(R.id.itinerary_title);
        itinTitle.setText(currentItinerary.getName());

        //Start itinerary events list fragment
        ItineraryItems itinItemsFrag = new ItineraryItems();
        itinItemsFrag.setContainerActivity(this);

        //Create transaction for list fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.itinerary_items_outer_container, itinItemsFrag);
        transaction.commit();
    }

    public void editItinerary(View view) {
        Intent intent = new Intent(this, EditItinerary.class);
        intent.putExtra(NEW_ITINERARY, false);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, Itineraries.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public void addEvent(View view) {
        if(!addingToggle){
            if(getSupportFragmentManager().findFragmentById(R.id.itinerary_items_container) != null) {
                getSupportFragmentManager()
                        .beginTransaction().
                        remove(getSupportFragmentManager().findFragmentById(R.id.itinerary_items_container)).commit();
            }

            //Start itinerary events list fragment
            AddEventFragment addEventFrag = new AddEventFragment();
            addEventFrag.setContainerActivity(this);

            //Create transaction for list fragment
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.itinerary_items_outer_container, addEventFrag);
            transaction.commit();
            addingToggle = true;
        }
        else{
            this.newDateTime = this.newDate + "" + this.timeOfDay;
            EditText editText = (EditText)findViewById(R.id.description_input);
            this.newDescription = editText.getText().toString();
            ItineraryItem itineraryItem = new ItineraryItem(this.newDate, this.newTime, this.newDescription, new FlightObject(), this.newDateTime);
            currentItinerary.addEvent(itineraryItem);
            itineraryEventArrayAdapter.notifyDataSetChanged();

            if(getSupportFragmentManager().findFragmentById(R.id.add_event_container) != null) {
                getSupportFragmentManager()
                        .beginTransaction().
                        remove(getSupportFragmentManager().findFragmentById(R.id.add_event_container)).commit();
            }

            //Start itinerary events list fragment
            ItineraryItems itinItemsFrag = new ItineraryItems();
            itinItemsFrag.setContainerActivity(this);

            //Create transaction for list fragment
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.itinerary_items_outer_container, itinItemsFrag);
            transaction.commit();
            addingToggle = false;
        }
    }

    public void showDatePickerDialog(View view){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
        String date = "month/day/month: " + month + "/" + dayOfMonth + "/" + year;
        this.newDate = month + "" + dayOfMonth + "" + year;
        TextView textView = (TextView)findViewById(R.id.test_text);
        textView.setText(this.newDate);
    }

    public void showTimePickerDialog(View view){
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                false);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {

        this.newTime = getTimeOfDayValuedHour(hour).get(0) + ":" + minute + " " + getTimeOfDayValuedHour(hour).get(1);
        this.timeOfDay = hour + "" + minute;
        TextView textView = (TextView) findViewById(R.id.test_text_2);
        textView.setText(this.timeOfDay);
    }

    private List<String> getTimeOfDayValuedHour(int hour){
        if(hour == 12){
            List<String> returnArray = Arrays.asList("12", "PM");
            return returnArray;
        }
        else if(hour == 0) {
            List<String> returnArray = Arrays.asList("12", "AM");
            return returnArray;
        }
        if(hour > 12) {
            List<String> returnArray = Arrays.asList(Integer.toString(hour-12), "PM");
            return returnArray;
        }
        else{
            List<String> returnArray = Arrays.asList(Integer.toString(hour), "PM");
            return returnArray;
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        new RetrieveViewModelTask(this, "WRITE").execute();
    }
}