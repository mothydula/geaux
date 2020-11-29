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
    public static boolean newItinerary = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_itinerary);
        Intent intent = getIntent();
        intent.getBooleanExtra(NEW_ITINERARY, true);

        Activity thisActivity = this;
        AlertDialog LDialog = new AlertDialog.Builder(this)
                .setTitle("Remove Itinerary")
                .setMessage("Are you sure you want to remove this itinerary?")
                .setOnCancelListener(this)
                .setOnDismissListener(this)
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.out.println("CANCELLED");
                    }
                })
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        model.getItineraries().remove(currentItinerary);
                        Intent intent = new Intent(thisActivity, Itineraries.class);
                        startActivity(intent);
                    }
                }).create();

        Button removeItineraryButton = (Button)findViewById(R.id.remove_itinerary);
        removeItineraryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LDialog.show();
            }
        });

        EditText itinTitle = (EditText)findViewById(R.id.edit_itinerary_title);
        if(!newItinerary)
            itinTitle.setText(currentItinerary.getName());
    }

    public void finishEditing(View view) {

        EditText nameField = (EditText) findViewById(R.id.edit_itinerary_title);
        String itineraryTitle = nameField.getText().toString();
        if(newItinerary){
            ItineraryObject newItinerary = new ItineraryObject(itineraryTitle, new ArrayList<ItineraryItem>());
            model.addItinerary(newItinerary);
            currentItinerary = newItinerary;
        }
        else{
            currentItinerary.setName(itineraryTitle);

        }
        Intent intent = new Intent(this, Itinerary.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {

    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {

    }
}