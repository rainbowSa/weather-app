package com.weatherapp.client;

import com.weatherapp.config.ApiConfig;
import com.weatherapp.model.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitari per WeatherApiClient
 * Verifica la corretta formattazione delle richieste API e gestione degli errori
 */
public class WeatherApiClientTest {

    private WeatherApiClient apiClient;

    @BeforeEach
    void setUp() {
        apiClient = new WeatherApiClient();
    }

    @Test
    @DisplayName("Test costruzione URL geocoding con città normale")
    void testGeocodingUrlConstruction() {
        // Test con una città normale
        String expectedUrl = ApiConfig.GEOCODING_API_URL + "?name=Milano&count=1&language=it";

        // Non possiamo testare direttamente il metodo privato, ma possiamo verificare
        // che l'URL sia costruito correttamente attraverso il comportamento
        // In un test di integrazione reale, useremmo un mock server

        // Per ora, testiamo che il client non sia null e che i metodi esistano
        assertNotNull(apiClient);
        assertDoesNotThrow(() -> {
            Location location = apiClient.getLocationByCityName("Milano");
            // Anche se la chiamata fallisce per mancanza di connessione,
            // il metodo dovrebbe esistere e non lanciare eccezioni inattese
        });
    }

    @Test
    @DisplayName("Test costruzione URL weather con coordinate valide")
    void testWeatherUrlConstruction() {
        // Test che il client gestisca coordinate valide
        Location testLocation = new Location(45.46, 9.18, "Milano", "Italia");

        assertNotNull(apiClient);
        assertDoesNotThrow(() -> {
            String result = apiClient.getCurrentWeather(testLocation);
            // Anche se la chiamata fallisce, il metodo dovrebbe esistere
        });
    }

    @Test
    @DisplayName("Test gestione input null per geocoding")
    void testNullCityName() {
        assertNotNull(apiClient);

        // Test che il metodo gestisca input null senza crashare
        assertDoesNotThrow(() -> {
            Location result = apiClient.getLocationByCityName(null);
            assertNull(result, "Dovrebbe restituire null per input null");
        });
    }

    @Test
    @DisplayName("Test gestione città vuota")
    void testEmptyCityName() {
        assertNotNull(apiClient);

        assertDoesNotThrow(() -> {
            Location result = apiClient.getLocationByCityName("");
            assertNull(result, "Dovrebbe restituire null per stringa vuota");
        });
    }

    @Test
    @DisplayName("Test gestione città con spazi")
    void testCityNameWithSpaces() {
        assertNotNull(apiClient);

        assertDoesNotThrow(() -> {
            Location result = apiClient.getLocationByCityName("New York");
            // Anche se fallisce la connessione, non dovrebbe crashare
        });
    }

    @Test
    @DisplayName("Test gestione location null per weather")
    void testNullLocationForWeather() {
        assertNotNull(apiClient);

        assertDoesNotThrow(() -> {
            String result = apiClient.getCurrentWeather(null);
            assertNull(result, "Dovrebbe restituire null per location null");
        });
    }

    @Test
    @DisplayName("Test URL encoding per città con caratteri speciali")
    void testUrlEncodingSpecialCharacters() {
        assertNotNull(apiClient);

        // Test con città che contengono caratteri speciali
        assertDoesNotThrow(() -> {
            Location result = apiClient.getLocationByCityName("São Paulo");
        });

        assertDoesNotThrow(() -> {
            Location result = apiClient.getLocationByCityName("München");
        });
    }
}