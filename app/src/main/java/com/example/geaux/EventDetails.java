package com.example.geaux;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static com.example.geaux.EventArrayAdapter.currentItineraryItem;
import static com.example.geaux.ItineraryItems.itineraryEventArrayAdapter;
import static com.example.geaux.MainActivity.currentItinerary;

public class EventDetails extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, DialogInterface.OnCancelListener, DialogInterface.OnDismissListener{
    private String newDateTime = "";
    private String newTimeFormatted = "";
    private String newDate = "";
    private String newDateFormatted = "";
    private String newTime = "";
    private String timeOfDay = "";
    private TextView dateText;
    private TextView timeText;
    private boolean flightShowing = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        TextView description = (TextView)findViewById(R.id.description_in_details);

        //Set the description to be the global "current itinerary's" descriptioin
        description.setText(currentItineraryItem.getDescription());
        Activity thisActivity = this;
        dateText = (TextView) findViewById(R.id.date_in_details);
        timeText = (TextView) findViewById(R.id.time_in_details);

        //Grab the date and time from currentItinerary
        dateText.setText(currentItineraryItem.getFormattedDate());
        timeText.setText(currentItineraryItem.getFormattedTime());

        //Create dialog for removing this event
        AlertDialog LDialog = new AlertDialog.Builder(this)
                .setTitle("Remove Event")
                .setMessage("Are you sure you want to remove this event?")
                .setOnCancelListener(this)
                .setOnDismissListener(this)
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        currentItinerary.getEvents().remove(currentItineraryItem);
                        Intent intent = new Intent(thisActivity, Itinerary.class);
                        startActivity(intent);
                    }
                }).create();

        Button removeEventButton = (Button)findViewById(R.id.remove_event);
        removeEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LDialog.show();
            }
        });

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
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
        int[] months = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        month = months[month];

        //Create a formatted version of the date for displaying purposes
        this.newDateFormatted = month + "/" + dayOfMonth + "/" + year;

        //Create a specially constructed version of the date for sorting purposes
        this.newDate = getSingleDigitValue(year-2000) + "" + getSingleDigitValue(month) + "" + getSingleDigitValue(dayOfMonth);

        //Set the date for this itinerary item
        currentItineraryItem.setDate(this.newDate);
        currentItineraryItem.setFormattedDate(this.newDateFormatted);
        TextView textView = (TextView)findViewById(R.id.date_in_details);

        //
        textView.setText(this.newDateFormatted);

        //Sort the events in the current itinerary based on time/date
        Collections.sort(currentItinerary.getEvents());

        //Update array adapter
        itineraryEventArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        //Get specially constructed time for sorting purposes
        this.newTime = getTimeOfDayValuedHour(hour).get(0) + "" + getSingleDigitValue(minute);

        //Get formated time version for displat purposes
        this.newTimeFormatted = getTimeOfDayValuedHour(hour).get(0) + ":" + getSingleDigitValue(minute) + " " + getTimeOfDayValuedHour(hour).get(1);

        this.timeOfDay = getSingleDigitValue(hour) + "" + getSingleDigitValue(minute);

        //Set the time of the itinerary item
        currentItineraryItem.setTime(this.newTime);
        currentItineraryItem.setFormattedTime(this.newTimeFormatted);


        TextView textView = (TextView) findViewById(R.id.time_in_details);
        textView.setText(this.newTimeFormatted);

        //Sort the itinerary events
        Collections.sort(currentItinerary.getEvents());
        itineraryEventArrayAdapter.notifyDataSetChanged();
    }

    private List<String> getTimeOfDayValuedHour(int hour){
        //Convert the hour to a twelve hour time along with a PM or AM
        if(hour == 12){
            List<String> returnArray = Arrays.asList("12", "PM");
            return returnArray;
        }
        else if(hour == 0) {
            List<String> returnArray = Arrays.asList("12", "AM");
            return returnArray;
        }
        if(hour > 12) {
            List<String> returnArray = Arrays.asList(getSingleDigitValue(hour-12), "PM");
            return returnArray;
        }
        else{
            List<String> returnArray = Arrays.asList(getSingleDigitValue(hour-12), "PM");
            return returnArray;
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        new RetrieveViewModelTask(this, "WRITE").execute();
    }

    private String getSingleDigitValue(int value){
        if(value < 10){
            return "0"+value;
        }
        else {
            return ""+value;
        }
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        System.out.println("CANCELLED");
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {

    }

    public void getFlight(View view) {
        //If the flight is already showing, hide it
        if(flightShowing){
            ((Button)view).setText("show flight details");
            if(getSupportFragmentManager().findFragmentById(R.id.flight_details_outer_container) != null) {
                getSupportFragmentManager()
                        .beginTransaction().
                        remove(getSupportFragmentManager().findFragmentById(R.id.flight_details_outer_container)).commit();
            }
            flightShowing = false;
        }
        //If the flight is not showing, show it
        else{
            ((Button)view).setText("hide flight details");
            //Start itinerary events list fragment
            FlightDetailsFragment flightDetailsFrag = new FlightDetailsFragment();
            flightDetailsFrag.setContainerActivity(this);
            //Create transaction for list fragment
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.flight_details_outer_container, flightDetailsFrag);
            transaction.commit();
            flightShowing = true;
        }

    }
}