package com.weatherapp.model;

/**
 * Rappresenta i dati meteorologici attuali
 */
public class WeatherData {
    private Location location;
    private double temperature;
    private double humidity;
    private double windSpeed;
    private String weatherDescription;
    private double precipitation;

    public WeatherData(Location location, double temperature, double humidity, 
                       double windSpeed, String weatherDescription, double precipitation) {
        this.location = location;
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.weatherDescription = weatherDescription;
        this.precipitation = precipitation;
    }

    public Location getLocation() {
        return location;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public double getPrecipitation() {
        return precipitation;
    }

    @Override
    public String toString() {
        return "WeatherData{" +
                "location=" + location +
                ", temperature=" + temperature + "°C" +
                ", humidity=" + humidity + "%" +
                ", windSpeed=" + windSpeed + " km/h" +
                ", weatherDescription='" + weatherDescription + '\'' +
                ", precipitation=" + precipitation + " mm" +
                '}';
    }
}
