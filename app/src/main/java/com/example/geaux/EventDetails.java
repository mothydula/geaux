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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        TextView description = (TextView)findViewById(R.id.description_in_details);
        description.setText(currentItineraryItem.getDescription());
        Activity thisActivity = this;
        dateText = (TextView) findViewById(R.id.date_in_details);
        timeText = (TextView) findViewById(R.id.time_in_details);

        dateText.setText(currentItineraryItem.getFormattedDate());
        timeText.setText(currentItineraryItem.getFormattedTime());

        AlertDialog LDialog = new AlertDialog.Builder(this)
                .setTitle("Remove Event")
                .setMessage("Are you sure you want to remove this event?")
                .setOnCancelListener(this)
                .setOnDismissListener(this)
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.out.println("CANCELLED");
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
        System.out.println("MONTH: " + month);
        String date = "month/day/month: " + month + "/" + dayOfMonth + "/" + year;
        this.newDateFormatted = month + "/" + dayOfMonth + "/" + year;
        this.newDate = getSingleDigitValue(year-2000) + "" + getSingleDigitValue(month) + "" + getSingleDigitValue(dayOfMonth);
        currentItineraryItem.setDate(this.newDate);
        currentItineraryItem.setFormattedDate(this.newDateFormatted);
        TextView textView = (TextView)findViewById(R.id.date_in_details);
        textView.setText(this.newDateFormatted);
        Collections.sort(currentItinerary.getEvents());
        itineraryEventArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        this.newTime = getTimeOfDayValuedHour(hour).get(0) + "" + getSingleDigitValue(minute);
        this.newTimeFormatted = getTimeOfDayValuedHour(hour).get(0) + ":" + getSingleDigitValue(minute) + " " + getTimeOfDayValuedHour(hour).get(1);
        this.timeOfDay = getSingleDigitValue(hour) + "" + getSingleDigitValue(minute);
        currentItineraryItem.setTime(this.newTime);
        currentItineraryItem.setFormattedTime(this.newTimeFormatted);
        TextView textView = (TextView) findViewById(R.id.time_in_details);
        textView.setText(this.newTimeFormatted);
        Collections.sort(currentItinerary.getEvents());
        itineraryEventArrayAdapter.notifyDataSetChanged();
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
        new GetFlightsTask().execute();
    }
}