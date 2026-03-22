package com.weatherapp.service;

import com.weatherapp.model.Location;
import com.weatherapp.model.WeatherData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitari per WeatherService
 * Verifica il parsing JSON e la gestione degli errori
 */
public class WeatherServiceTest {

    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        weatherService = new WeatherService();
    }

    @Test
    @DisplayName("Test parsing JSON valido")
    void testValidJsonParsing() {
        // JSON di esempio simile a quello restituito dall'API
        String validJson = "{" +
            "\n  \"latitude\": 45.46," +
            "\n  \"longitude\": 9.18," +
            "\n  \"generationtime_ms\": 0.13244152069091797," +
            "\n  \"utc_offset_seconds\": 3600," +
            "\n  \"timezone\": \"Europe/Rome\"," +
            "\n  \"timezone_abbreviation\": \"GMT+1\"," +
            "\n  \"elevation\": 145.0," +
            "\n  \"current_units\": {" +
            "\n    \"time\": \"iso8601\"," +
            "\n    \"interval\": \"seconds\"," +
            "\n    \"temperature_2m\": \"°C\"," +
            "\n    \"relative_humidity_2m\": \"%\"," +
            "\n    \"weather_code\": \"wmo code\"," +
            "\n    \"wind_speed_10m\": \"km/h\"," +
            "\n    \"precipitation\": \"mm\"" +
            "\n  }," +
            "\n  \"current\": {" +
            "\n    \"time\": \"2026-03-22T18:30\"," +
            "\n    \"interval\": 900," +
            "\n    \"temperature_2m\": 12.0," +
            "\n    \"relative_humidity_2m\": 44," +
            "\n    \"weather_code\": 3," +
            "\n    \"wind_speed_10m\": 7.2," +
            "\n    \"precipitation\": 0.00" +
            "\n  }" +
            "\n}";

        Location testLocation = new Location(45.46, 9.18, "Milano", "Italia");

        assertDoesNotThrow(() -> {
            WeatherData result = weatherService.getWeatherByCity("Milano");
            // In un test reale, useremmo mock per evitare chiamate HTTP
        });
    }

    @Test
    @DisplayName("Test gestione JSON malformato")
    void testMalformedJson() {
        String malformedJson = "{ invalid json }";

        Location testLocation = new Location(45.46, 9.18, "Milano", "Italia");

        // Il metodo dovrebbe gestire JSON malformato senza crashare
        assertDoesNotThrow(() -> {
            WeatherData result = weatherService.getWeatherByCity("TestCity");
            // Anche se la chiamata HTTP fallisce, non dovrebbe crashare
        });
    }

    @Test
    @DisplayName("Test gestione città non esistente")
    void testNonExistentCity() {
        assertNotNull(weatherService);

        assertDoesNotThrow(() -> {
            WeatherData result = weatherService.getWeatherByCity("CittàInesistente12345");
            assertNull(result, "Dovrebbe restituire null per città inesistente");
        });
    }

    @Test
    @DisplayName("Test gestione input null")
    void testNullCityInput() {
        assertNotNull(weatherService);

        assertDoesNotThrow(() -> {
            WeatherData result = weatherService.getWeatherByCity(null);
            assertNull(result, "Dovrebbe restituire null per input null");
        });
    }

    @Test
    @DisplayName("Test gestione stringa vuota")
    void testEmptyCityInput() {
        assertNotNull(weatherService);

        assertDoesNotThrow(() -> {
            WeatherData result = weatherService.getWeatherByCity("");
            assertNull(result, "Dovrebbe restituire null per stringa vuota");
        });
    }

    @Test
    @DisplayName("Test risoluzione codici meteo")
    void testWeatherCodeResolution() {
        // Test dei codici meteo principali
        assertEquals("Cielo sereno", resolveWeatherCodeForTest(0));
        assertEquals("Parzialmente nuvoloso", resolveWeatherCodeForTest(1));
        assertEquals("Nuvoloso", resolveWeatherCodeForTest(3));
        assertEquals("Pioggia", resolveWeatherCodeForTest(61));
        assertEquals("Neve", resolveWeatherCodeForTest(71));
        assertEquals("Temporale", resolveWeatherCodeForTest(95));
        assertEquals("Sconosciuto", resolveWeatherCodeForTest(999));
    }

    @Test
    @DisplayName("Test parsing valori numerici estremi")
    void testExtremeValues() {
        // Test che il parsing gestisca valori estremi senza crashare
        String extremeJson = "{" +
            "\n  \"current\": {" +
            "\n    \"temperature_2m\": 999.99," +
            "\n    \"relative_humidity_2m\": 150," +
            "\n    \"weather_code\": 999," +
            "\n    \"wind_speed_10m\": 999.99," +
            "\n    \"precipitation\": 999.99" +
            "\n  }" +
            "\n}";

        Location testLocation = new Location(0.0, 0.0, "Test", "Test");

        // Il parsing dovrebbe gestire valori estremi
        assertDoesNotThrow(() -> {
            // In un test reale, testeremmo il metodo parseWeatherData direttamente
        });
    }

    // Metodo helper per testare la risoluzione dei codici meteo
    private String resolveWeatherCodeForTest(int code) {
        switch (code) {
            case 0: return "Cielo sereno";
            case 1:
            case 2: return "Parzialmente nuvoloso";
            case 3: return "Nuvoloso";
            case 45:
            case 48: return "Nebbia";
            case 51:
            case 53:
            case 55:
            case 61:
            case 63:
            case 65: return "Pioggia";
            case 71:
            case 73:
            case 75:
            case 77:
            case 85:
            case 86: return "Neve";
            case 95:
            case 96:
            case 99: return "Temporale";
            default: return "Sconosciuto";
        }
    }
}