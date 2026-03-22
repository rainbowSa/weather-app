package com.weatherapp.model;

/**
 * Rappresenta una previsione meteorologica per un giorno
 */
public class Forecast {
    private String date;
    private double maxTemperature;
    private double minTemperature;
    private String weatherDescription;
    private double precipitation;

    public Forecast(String date, double maxTemperature, double minTemperature, 
                    String weatherDescription, double precipitation) {
        this.date = date;
        this.maxTemperature = maxTemperature;
        this.minTemperature = minTemperature;
        this.weatherDescription = weatherDescription;
        this.precipitation = precipitation;
    }

    public String getDate() {
        return date;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public double getPrecipitation() {
        return precipitation;
    }

    @Override
    public String toString() {
        return "Data: " + date + 
               " | Max: " + maxTemperature + "°C" + 
               " | Min: " + minTemperature + "°C" +
               " | Descrizione: " + weatherDescription +
               " | Precipitazioni: " + precipitation + " mm";
    }
}
