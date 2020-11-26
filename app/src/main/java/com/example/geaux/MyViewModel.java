package com.example.geaux;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class MyViewModel extends ViewModel {
    private ArrayList<ItineraryObject> itineraries = new ArrayList<>();
    private String testStr = "Hello";


    public String test(){
        return testStr;
    }

    public void addItinerary(ItineraryObject itObj){
        this.itineraries.add(itObj);
    }

    public ArrayList<ItineraryObject> getItineraries(){
        return this.itineraries;
    }
}
