package com.example.geaux;

import java.util.ArrayList;

public class ItineraryObject {
    private String name;
    private ArrayList<ItineraryItem> items;
    public ItineraryObject(String itineraryName, ArrayList<ItineraryItem> itineraryItems){
        this.name = itineraryName;
        this.items = itineraryItems;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void addEvent(ItineraryItem newEvent){
        this.items.add(newEvent);
    }

    public ArrayList<ItineraryItem> getEvents(){
        return this.items;
    }
}
