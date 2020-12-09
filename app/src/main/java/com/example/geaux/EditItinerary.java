package com.example.geaux;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import static com.example.geaux.MainActivity.NEW_ITINERARY;
import static com.example.geaux.MainActivity.currentItinerary;
import static com.example.geaux.MainActivity.model;

public class EditItinerary extends AppCompatActivity implements DialogInterface.OnCancelListener, DialogInterface.OnDismissListener{
    //Toggle for if this activity is being used to edit an existing itinerary or to create a new itinerary
    public static boolean newItinerary = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_itinerary);
        Intent intent = getIntent();
        //If this activity was strarted from an existing itinerary, the value should be false, else it defaults to true
        this.newItinerary = intent.getBooleanExtra(NEW_ITINERARY, true);

        //Create reference to remove itinerary button
        Button removeItinerary = (Button)findViewById(R.id.remove_itinerary);

        //If this is a new itinerary then the remove itinerary button will be invisible
        if(this.newItinerary){
            removeItinerary.setVisibility(View.INVISIBLE);
        }
        else{
            removeItinerary.setVisibility(View.VISIBLE);
        }

        Activity thisActivity = this;

        //Set up the "are you sure" dialog for removing the itinerary
        AlertDialog LDialog = new AlertDialog.Builder(this)
                .setTitle("Remove Itinerary")
                .setMessage("Are you sure you want to remove this itinerary?")
                .setOnCancelListener(this)
                .setOnDismissListener(this)
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        model.getItineraries().remove(currentItinerary);
                        Intent intent = new Intent(thisActivity, Itineraries.class);
                        startActivity(intent);
                    }
                }).create();

        //Show dialog if remove itinerary button is pressed
        removeItinerary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LDialog.show();
            }
        });

        EditText itinTitle = (EditText)findViewById(R.id.edit_itinerary_title);
        //If this itinerary is not new, then pre-populate the edit name field with the existing name
        if(!this.newItinerary)
            itinTitle.setText(currentItinerary.getName());
    }

    public void finishEditing(View view) {

        EditText nameField = (EditText) findViewById(R.id.edit_itinerary_title);
        //Set the title to the input from the name input
        String itineraryTitle = nameField.getText().toString();
        if(this.newItinerary){
            //If this is a new itinerary, create a new itinerary with the name and an empty arraylist to hold itinerary items/events
            ItineraryObject newItinerary = new ItineraryObject(itineraryTitle, new ArrayList<ItineraryItem>());
            //Add this new itinerary to the view model
            model.addItinerary(newItinerary);
            //The global "current itinerary" variable is now this new itinerary
            currentItinerary = newItinerary;

        }
        else{
            //If this isn't a new itinerary, just change the title
            currentItinerary.setName(itineraryTitle);

        }

        //Go back to the itinerary activity
        Intent intent = new Intent(this, Itinerary.class);
        startActivity(intent);
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {

    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        //Save the model to system storage
        new RetrieveViewModelTask(this, "WRITE").execute();
    }
}