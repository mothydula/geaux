package com.example.geaux;

public class ItineraryItem implements Comparable{
    private String time;
    private String date;
    private String dateTime;
    private String description;
    private FlightObject flight;
    private String formattedDate;
    private String formattedTime;
    public ItineraryItem(String date, String time, String formattedDate, String formattedTime, String description, FlightObject flight, String dateTime){
        this.time = time;
        this.date = date;
        this.formattedTime = formattedTime;
        this.formattedDate = formattedDate;
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

    public void setTime(String time){
        this.time = time;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getFormattedDate(){
        return this.formattedDate;
    }

    public String getFormattedTime(){
        return this.formattedTime;
    }

    public void setFormattedDate(String formattedDate){
        this.formattedDate = formattedDate;
    }

    public void setFormattedTime(String formattedTime){
        this.formattedTime = formattedTime;
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

    @Override
    public int compareTo(Object o) {
        System.out.println("LOOK AT ME: " + ((ItineraryItem)o).getTime());
        System.out.println("LOOK AT ME 2: " + (this.time));
        int myDate = Integer.parseInt(this.date);
        int myTime = Integer.parseInt(this.time);
        System.out.println("DONE ONCE");
        int compareDate = Integer.parseInt(((ItineraryItem)o).getDate());
        int compareTime = Integer.parseInt(((ItineraryItem)o).getTime());
        System.out.println("DONE TWICE");
        int myDateTime = Integer.parseInt(this.date+this.time);
        int compareDateTime = Integer.parseInt(((ItineraryItem)o).getDate() + ((ItineraryItem)o).getTime());
        return myDateTime - compareDateTime;
    }
}
