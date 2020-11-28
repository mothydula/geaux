package com.example.geaux;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import static com.example.geaux.MainActivity.NEW_ITINERARY;
import static com.example.geaux.MainActivity.currentItinerary;
import static com.example.geaux.MainActivity.model;

public class EditItinerary extends AppCompatActivity {
    public static boolean newItinerary = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_itinerary);
        Intent intent = getIntent();
        intent.getBooleanExtra(NEW_ITINERARY, true);
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

}