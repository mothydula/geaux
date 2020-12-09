package com.example.geaux;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class MyViewModel extends ViewModel {
    //ViewModel that holds the itinerary objects for the app
    private ArrayList<ItineraryObject> itineraries = new ArrayList<>();

    public void addItinerary(ItineraryObject itObj){
        this.itineraries.add(itObj);
    }

    public ArrayList<ItineraryObject> getItineraries(){
        return this.itineraries;
    }
}
