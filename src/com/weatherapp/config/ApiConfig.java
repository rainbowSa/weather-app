package com.weatherapp.config;

/**
 * Configurazione centralizzata per l'API Open-Meteo
 */
public class ApiConfig {
    // URL API Open-Meteo (geolocalizzazione)
    public static final String GEOCODING_API_URL = "https://geocoding-api.open-meteo.com/v1/search";
    
    // URL API Open-Meteo (dati meteorologici)
    public static final String WEATHER_API_URL = "https://api.open-meteo.com/v1/forecast";
    
    // Parametri di configurazione
    public static final int CONNECTION_TIMEOUT = 5000; // 5 secondi
    public static final int READ_TIMEOUT = 5000;
    
    // Parametri di default per le richieste meteo
    public static final String WEATHER_PARAMS = "temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m,precipitation";
    
    // Codici meteo (semplificati)
    public static final String CLEAR_SKY = "Cielo sereno";
    public static final String PARTLY_CLOUDY = "Parzialmente nuvoloso";
    public static final String OVERCAST = "Nuvoloso";
    public static final String RAINY = "Pioggia";
    public static final String SNOWY = "Neve";
    public static final String THUNDERSTORM = "Temporale";
}
