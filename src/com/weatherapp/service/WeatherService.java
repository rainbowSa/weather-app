package com.weatherapp.service;

import com.weatherapp.client.WeatherApiClient;
import com.weatherapp.model.Location;
import com.weatherapp.model.WeatherData;

/**
 * Servizio di business logic per i dati meteorologici
 * Coordina le operazioni tra client API, modelli e UI
 */
public class WeatherService {
    private WeatherApiClient apiClient;

    public WeatherService() {
        this.apiClient = new WeatherApiClient();
    }

    /**
     * Recupera i dati meteorologici per una città
     * @param cityName Nome della città
     * @return Oggetto WeatherData con i dati attuali
     */
    public WeatherData getWeatherByCity(String cityName) {
        // 1. Recupera le coordinate geografiche
        Location location = apiClient.getLocationByCityName(cityName);
        
        if (location == null) {
            System.err.println("Città non trovata: " + cityName);
            return null;
        }
        
        // 2. Recupera i dati meteorologici
        String weatherJson = apiClient.getCurrentWeather(location);
        
        if (weatherJson == null) {
            System.err.println("Errore nel recupero dei dati meteorologici");
            return null;
        }
        
        // 3. Estrae i dati dal JSON e crea un oggetto WeatherData
        return parseWeatherData(location, weatherJson);
    }

    /**
     * Estrae i dati meteorologici dal JSON di risposta
     * (Implementazione semplificata)
     */
    private WeatherData parseWeatherData(Location location, String json) {
        try {
            // DEBUG: Mostra il JSON completo ricevuto
            System.out.println("[DEBUG] JSON completo ricevuto:");
            System.out.println(json);
            System.out.println("[DEBUG] Fine JSON\n");

            // Estrai solo la sezione "current" dal JSON
            String currentSection = extractCurrentSection(json);
            if (currentSection == null) {
                System.err.println("Errore: sezione 'current' non trovata nel JSON");
                return null;
            }

            System.out.println("[DEBUG] Sezione current estratta:");
            System.out.println(currentSection);
            System.out.println("[DEBUG] Fine sezione current\n");

            double temperature = extractDouble(currentSection, "\"temperature_2m\":");
            double humidity = extractDouble(currentSection, "\"relative_humidity_2m\":");
            double windSpeed = extractDouble(currentSection, "\"wind_speed_10m\":");
            double precipitation = extractDouble(currentSection, "\"precipitation\":");

            // DEBUG: Mostra i valori estratti
            System.out.println("[DEBUG] Valori estratti:");
            System.out.println("  Temperatura: " + temperature);
            System.out.println("  Umidità: " + humidity);
            System.out.println("  Vento: " + windSpeed);
            System.out.println("  Precipitazioni: " + precipitation);

            String description = resolveWeatherCode(extractInt(currentSection, "\"weather_code\":"));
            System.out.println("  Codice meteo: " + extractInt(currentSection, "\"weather_code\":") + " -> " + description);
            System.out.println("[DEBUG] Fine valori estratti\n");

            return new WeatherData(location, temperature, humidity, windSpeed, description, precipitation);
        } catch (Exception e) {
            System.err.println("Errore nel parsing dei dati meteorologici: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Estrae la sezione "current" dal JSON completo
     */
    private String extractCurrentSection(String json) {
        int currentStart = json.indexOf("\"current\":");
        if (currentStart == -1) return null;

        currentStart += "\"current\":".length();
        int braceCount = 0;
        int endIndex = currentStart;

        for (int i = currentStart; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '{') braceCount++;
            else if (c == '}') {
                braceCount--;
                if (braceCount == 0) {
                    endIndex = i + 1;
                    break;
                }
            }
        }

        return json.substring(currentStart, endIndex);
    }

    /**
     * Converte il codice meteorologico in descrizione leggibile
     */
    private String resolveWeatherCode(int code) {
        switch (code) {
            case 0:
                return "Cielo sereno";
            case 1:
            case 2:
                return "Parzialmente nuvoloso";
            case 3:
                return "Nuvoloso";
            case 45:
            case 48:
                return "Nebbia";
            case 51:
            case 53:
            case 55:
            case 61:
            case 63:
            case 65:
                return "Pioggia";
            case 71:
            case 73:
            case 75:
            case 77:
            case 85:
            case 86:
                return "Neve";
            case 95:
            case 96:
            case 99:
                return "Temporale";
            default:
                return "Sconosciuto";
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
     * Estrae un valore int dal JSON (parsing semplificato)
     */
    private int extractInt(String json, String key) {
        return (int) extractDouble(json, key);
    }
}
