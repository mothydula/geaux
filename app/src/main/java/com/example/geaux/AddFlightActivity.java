package com.example.geaux;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import static com.example.geaux.EventArrayAdapter.currentItineraryItem;
import static com.example.geaux.Itinerary.getSingleDigitValue;
import static com.example.geaux.Itinerary.getTimeOfDayValuedHour;

public class AddFlightActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener , TimePickerDialog.OnTimeSetListener{
    boolean editDepartureDate;
    boolean editArrivalDate;
    boolean editDepartureTime;
    boolean editArrivalTime;
    boolean arrivalLocationSet;
    boolean departureLocationSet;
    private FlightObject flight;
    private String departureTime = "";
    private String departureDate = "";
    private String arrivalTime = "";
    private String arrivalDate = "";
    private String departureLocation = "";
    private String arrivalLocation = "";
    private String airline = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flight);

        EditText departureLocation = (EditText)findViewById(R.id.departure_location);
        EditText arrivalLocation = (EditText)findViewById(R.id.arrival_location);
        Button addFlightButton = (Button)findViewById(R.id.add_flight);

    }

    public void showDatePickerDialog(View view){
        //Check which edit date button was chosen (departure of arrival)
        if (view.getId() == R.id.departure_date){
            editDepartureDate = true;
            editArrivalDate = false;
        }
        else if(view.getId() == R.id.arrival_date){
            editArrivalDate = true;
            editDepartureDate = false;
        }
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
        //Check which edit time button was chosen (departure of arrival)
        if (view.getId() == R.id.departure_time){
            editDepartureTime = true;
            editArrivalTime = false;
        }
        else if(view.getId() == R.id.arrival_time){
            editArrivalTime = true;
            editDepartureTime = false;
        }
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.HOUR),
                Calendar.getInstance().get(Calendar.MINUTE),
                false);
        timePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
        //Create reference for change date button
        Button changeDateButton;
        int[] months = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        month = months[month];
        //Create date string
        String date = month + "/" + dayOfMonth + "/" + year;

        TextView textView = (TextView)findViewById(R.id.test_date);
        if(editDepartureDate){
            this.departureDate = date;

            changeDateButton = (Button)findViewById(R.id.departure_date);
            //Set the text of the button to the date so user can keep track
            changeDateButton.setText(date);
        }
        else if(editArrivalDate){
            this.arrivalDate = date;
            changeDateButton = (Button)findViewById(R.id.arrival_date);
            //Set the text of the button to the date so user can keep track
            changeDateButton.setText(date);
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        //Create reference for change time button
        Button changeTimeButton;
        //Create time string
        String time = getTimeOfDayValuedHour(hour).get(0) + ":" + getSingleDigitValue(minute) + " " + getTimeOfDayValuedHour(hour).get(1);
        if(editDepartureTime){
            this.departureTime = time;
            changeTimeButton = (Button)findViewById(R.id.departure_time);
            //Set the text of the button to the date so user can keep track
            changeTimeButton.setText(time);
        }
        else if (editArrivalTime){
            this.arrivalTime = time;
            changeTimeButton = (Button)findViewById(R.id.arrival_time);
            //Set the text of the button to the date so user can keep track
            changeTimeButton.setText(time);
        }
    }

    public void addFlightToEvent(View view) {
        //Create references for departure and arrival location inputs
        EditText departureLocation = (EditText)findViewById(R.id.departure_location);
        EditText arrivalLocation = (EditText)findViewById(R.id.arrival_location);

        //Retreive text from the inputs
        this.departureLocation = departureLocation.getText().toString();
        this.arrivalLocation = arrivalLocation.getText().toString();

        //Create a new flight object with the information parsed from the AddFlightActivities different inputs
        this.flight = new FlightObject(this.departureDate, this.arrivalDate, this.departureTime, this.arrivalTime, this.airline, this.departureLocation, this.arrivalLocation);
        //Add this flight to the current itinerary item/event
        currentItineraryItem.setFlight(this.flight);

        //Go back to the itinerary
        Intent intent = new Intent(this, Itinerary.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        new RetrieveViewModelTask(this,"WRITE").execute();
    }
}