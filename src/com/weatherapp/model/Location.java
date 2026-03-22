package com.weatherapp.model;

/**
 * Rappresenta una posizione geografica
 */
public class Location {
    private double latitude;
    private double longitude;
    private String name;
    private String country;

    public Location(double latitude, double longitude, String name, String country) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.country = country;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return name + ", " + country + " (" + latitude + ", " + longitude + ")";
    }
}
