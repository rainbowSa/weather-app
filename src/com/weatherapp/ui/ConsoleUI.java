package com.weatherapp.ui;

import com.weatherapp.model.WeatherData;
import com.weatherapp.service.WeatherService;

import java.util.Scanner;

/**
 * Interfaccia utente basata su console
 * Gestisce l'input dall'utente e la visualizzazione dei risultati
 */
public class ConsoleUI {
    private WeatherService weatherService;
    private Scanner scanner;

    public ConsoleUI() {
        this.weatherService = new WeatherService();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Avvia l'applicazione e gestisce il ciclo principale
     */
    public void start() {
        printWelcome();
        
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    searchWeather();
                    break;
                case "2":
                    printAbout();
                    break;
                case "3":
                    running = false;
                    printGoodbye();
                    break;
                default:
                    System.out.println("Scelta non valida. Riprova.");
            }
        }
        
        scanner.close();
    }

    /**
     * Permette all'utente di cercare il meteo di una città
     */
    private void searchWeather() {
        System.out.println("\nRicerca meteo");
        System.out.print("Inserisci il nome della città: ");
        String cityName = scanner.nextLine().trim();
        
        if (cityName.isEmpty()) {
            System.out.println("Nome città non valido.");
            return;
        }
        
        System.out.println("Caricamento dati...");
        
        WeatherData weather = weatherService.getWeatherByCity(cityName);
        
        if (weather == null) {
            System.out.println("Impossibile recuperare i dati meteorologici per: " + cityName);
            return;
        }
        
        displayWeather(weather);
    }

    /**
     * Visualizza i dati meteorologici in formato leggibile
     */
    private void displayWeather(WeatherData weather) {
        System.out.println("\n" + repeatString("=", 60));
        System.out.println("DATI METEOROLOGICI - " + weather.getLocation().getName().toUpperCase());
        System.out.println(repeatString("=", 60));
        System.out.println("Località: " + weather.getLocation());
        System.out.println("Temperatura: " + String.format("%.1f", weather.getTemperature()) + "°C");
        System.out.println("Umidità: " + String.format("%.1f", weather.getHumidity()) + "%");
        System.out.println("Vento: " + String.format("%.1f", weather.getWindSpeed()) + " km/h");
        System.out.println("Condizione: " + weather.getWeatherDescription());
        System.out.println("Precipitazioni: " + String.format("%.1f", weather.getPrecipitation()) + " mm");
        System.out.println(repeatString("=", 60) + "\n");
    }

    /**
     * Visualizza il menu principale
     */
    private void printMenu() {
        System.out.println("MENU PRINCIPALE");
        System.out.println("1. Cerca meteo per una città");
        System.out.println("2. Informazioni app");
        System.out.println("3. Esci");
        System.out.print("Scegli un'opzione (1-3): ");
    }

    /**
     * Ripete una stringa n volte
     */
    private String repeatString(String str, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * Visualizza il messaggio di benvenuto
     */
    private void printWelcome() {
        System.out.println("\n" + repeatString("=", 60));
        System.out.println("BENVENUTO IN WEATHER APP");
        System.out.println(repeatString("=", 60));
        System.out.println("Scopri il meteo di qualsiasi città nel mondo!");
        System.out.println("Dati forniti da Open-Meteo API");
        System.out.println(repeatString("=", 60) + "\n");
    }

    /**
     * Visualizza informazioni sull'app
     */
    private void printAbout() {
        System.out.println("\n" + repeatString("=", 60));
        System.out.println("ℹ️  INFORMAZIONI APP");
        System.out.println(repeatString("=", 60));
        System.out.println("Weather App v1.0");
        System.out.println("Una semplice applicazione per consultare le previsioni meteo");
        System.out.println("API: Open-Meteo (https://open-meteo.com)");
        System.out.println("Linguaggio: Java");
        System.out.println(repeatString("=", 60) + "\n");
    }

    /**
     * Visualizza il messaggio di arrivederci
     */
    private void printGoodbye() {
        System.out.println("\n👋 Grazie per aver usato Weather App!");
        System.out.println("Arrivederci!\n");
    }
}
