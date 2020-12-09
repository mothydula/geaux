package com.example.geaux;

public class FlightObject {
    private String departureDate;
    private String arrivalDate;
    private String departureTime;
    private String arrivalTime;
    private String airline;
    private String departureLocation;
    private String arrivalLocation;
    public FlightObject(String depatureDate, String arrivalDate, String departureTime, String arrivalTime, String airline, String departureLocation, String arrivalLocation){
        this.departureDate = depatureDate;
        this.arrivalDate = arrivalDate;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.airline = airline;
        this.departureLocation = departureLocation;
        this.arrivalLocation = arrivalLocation;
    }

    public String getDepartureDate(){
        return this.departureDate;
    }

    public String getArrivalDate(){
        return this.arrivalDate;
    }

    public String getDepartureTime(){
        return this.departureTime;
    }

    public String getArrivalTime(){
        return this.arrivalTime;
    }

    public String getAirline(){
        return this.airline;
    }

    public String getDepartureLocation(){
        return this.departureLocation;
    }

    public String getArrivalLocation(){
        return this.arrivalLocation;
    }
}

