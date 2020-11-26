package com.example.geaux;

public class ItineraryItem {
    private String time;
    private String date;
    private String dateTime;
    private String description;
    private FlightObject flight;
    public ItineraryItem(String time, String date, String description, FlightObject flight, String dateTime){
        this.time = time;
        this.date = date;
        this.dateTime = dateTime;
        this.description = description;
        this.flight = flight;
    }

    public String getTime(){
        return this.time;
    }

    public String getDate(){
        return this.date;
    }

    public String getDescription(){
        return this.description;
    }

    public FlightObject getFlight(){
        return this.flight;
    }

    public String getDateTime(){
        return this.dateTime;
    }
}
