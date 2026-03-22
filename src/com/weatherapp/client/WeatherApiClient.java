package com.weatherapp.client;

import com.weatherapp.config.ApiConfig;
import com.weatherapp.model.Location;

import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.time.Duration;

/**
 * Client per comunicare con le API Open-Meteo
 * Gestisce le richieste di geolocalizzazione e dati meteorologici
 */
public class WeatherApiClient {

    /**
     * Recupera le coordinate geografiche di una città
     * @param cityName Nome della città
     * @return Oggetto Location con coordinate e info della città
     */
    public Location getLocationByCityName(String cityName) {
        if (cityName == null || cityName.trim().isEmpty()) {
            return null;
        }
        
        try {
            String encodedCity = URLEncoder.encode(cityName, "UTF-8");
            String urlString = ApiConfig.GEOCODING_API_URL + "?name=" + encodedCity + "&count=1&language=it";
            
            String response = makeHttpRequest(urlString);
            
            // Parse della risposta JSON (semplificato)
            return parseLocationFromJson(response, cityName);
        } catch (Exception e) {
            System.err.println("Errore nel recupero della posizione: " + e.getMessage());
            return null;
        }
    }

    /**
     * Recupera i dati meteorologici attuali per una posizione
     * @param location Posizione geografica
     * @return JSON con i dati meteo
     */
    public String getCurrentWeather(Location location) {
        try {
            String urlString = ApiConfig.WEATHER_API_URL + 
                    "?latitude=" + location.getLatitude() + 
                    "&longitude=" + location.getLongitude() + 
                    "&current=" + ApiConfig.WEATHER_PARAMS + 
                    "&timezone=auto";
            
            return makeHttpRequest(urlString);
        } catch (Exception e) {
            System.err.println("Errore nel recupero del meteo: " + e.getMessage());
            return null;
        }
    }

    /**
     * Effettua una richiesta HTTP GET
     * @param urlString URL della richiesta
     * @return Risposta in formato String
     */
    private String makeHttpRequest(String urlString) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlString))
                .GET()
                .header("User-Agent", "WeatherApp/1.0")
                .timeout(Duration.ofMillis(ApiConfig.CONNECTION_TIMEOUT))
                .build();
        
        HttpResponse<String> response = client.send(request, 
                HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new Exception("Errore HTTP: " + response.statusCode());
        }
        
        return response.body();
    }

    /**
     * Estrae i dati di localizzazione dal JSON di risposta
     * (Implementazione semplificata - per JSON reale usare una libreria come Gson)
     */
    private Location parseLocationFromJson(String json, String cityName) {
        try {
            // Ricerca dei campi nel JSON
            if (json.contains("\"results\":[]")) {
                return null; // Nessun risultato trovato
            }
            
            // Parsing basico (in produzione usare Gson/Jackson)
            double latitude = extractDouble(json, "\"latitude\":");
            double longitude = extractDouble(json, "\"longitude\":");
            
            // Se le coordinate sono 0,0, probabilmente nessun risultato valido
            if (latitude == 0.0 && longitude == 0.0) {
                return null;
            }
            
            String country = extractString(json, "\"country\":");
            if (country.isEmpty()) {
                country = "Sconosciuto";
            }
            
            return new Location(latitude, longitude, cityName, country);
        } catch (Exception e) {
            System.err.println("Errore nel parsing della localizzazione: " + e.getMessage());
            return null;
        }
    }

    /**
     * Estrae un valore double dal JSON (parsing semplificato)
     */
    private double extractDouble(String json, String key) {
        int startIndex = json.indexOf(key);
        if (startIndex == -1) return 0.0;
        
        startIndex += key.length();
        int endIndex = json.indexOf(",", startIndex);
        if (endIndex == -1) endIndex = json.indexOf("}", startIndex);
        
        String value = json.substring(startIndex, endIndex).trim();
        
        // Rimuove virgolette e caratteri non numerici (tranne il punto decimale e il segno meno)
        value = value.replaceAll("[\"°CcFfhHmMsS%]", "").trim();
        
        // Se la stringa è vuota dopo la pulizia, ritorna 0.0
        if (value.isEmpty()) {
            return 0.0;
        }
        
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            System.err.println("Errore nel parsing del valore: '" + value + "'");
            return 0.0;
        }
    }

    /**
     * Estrae un valore String dal JSON (parsing semplificato)
     */
    private String extractString(String json, String key) {
        int startIndex = json.indexOf(key);
        if (startIndex == -1) return "";
        
        startIndex = json.indexOf("\"", startIndex + key.length()) + 1;
        int endIndex = json.indexOf("\"", startIndex);
        
        return json.substring(startIndex, endIndex);
    }
}
