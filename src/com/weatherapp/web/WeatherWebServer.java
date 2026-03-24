package com.weatherapp.web;

import com.weatherapp.model.Location;
import com.weatherapp.model.WeatherData;
import com.weatherapp.service.WeatherService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public class WeatherWebServer {
    private static final int PORT = 8080;
    private static final Path STATIC_DIR = Paths.get("src", "web", "static");

    private final WeatherService weatherService;

    public WeatherWebServer() {
        this.weatherService = new WeatherService();
    }

    public static void main(String[] args) throws IOException {
        new WeatherWebServer().start();
    }

    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/api/weather", new WeatherApiHandler());
        server.createContext("/", new StaticFileHandler());

        server.setExecutor(Executors.newCachedThreadPool());
        server.start();

        System.out.println("[WEB] Server avviato su http://localhost:" + PORT);
        System.out.println("[WEB] Usa http://localhost:" + PORT + "/index.html");
    }

    private class WeatherApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJson(exchange, 405, "{\"error\":\"Metodo non consentito\"}");
                return;
            }

            Map<String, String> params = queryToMap(exchange.getRequestURI().getRawQuery());
            String city = params.get("city");

            System.out.println("[WEB] /api/weather?city=" + city + " richiesta da " + exchange.getRemoteAddress());

            if (city == null || city.isBlank()) {
                sendJson(exchange, 400, "{\"error\":\"Parametro city obbligatorio\"}");
                return;
            }

            WeatherData weatherData = weatherService.getWeatherByCity(city);
            if (weatherData == null) {
                sendJson(exchange, 404, "{\"error\":\"Dati meteo non trovati per la città richiesta\"}");
                return;
            }

            sendJson(exchange, 200, toJson(weatherData));
        }
    }

    private class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            URI requestUri = exchange.getRequestURI();
            String path = requestUri.getPath();

            if (path == null || path.isBlank() || "/".equals(path)) {
                path = "/index.html";
            }

            // Forza la lettura delle risorse statiche dallo spazio statico
            Path filePath = STATIC_DIR.resolve(path.substring(1)).normalize();
            if (!filePath.startsWith(STATIC_DIR) || !Files.exists(filePath) || Files.isDirectory(filePath)) {
                String notFound = "<h1>404 Not Found</h1><p>File non trovato: " + path + "</p>";
                sendHtml(exchange, 404, notFound);
                return;
            }

            byte[] content = Files.readAllBytes(filePath);
            String contentType = URLConnection.guessContentTypeFromName(filePath.toString());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            exchange.getResponseHeaders().set("Content-Type", contentType + "; charset=UTF-8");
            exchange.sendResponseHeaders(200, content.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(content);
            }
        }
    }

    private Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        if (query == null || query.isBlank()) {
            return result;
        }

        for (String param : query.split("&")) {
            String[] split = param.split("=", 2);
            if (split.length == 2) {
                String key = URLDecoder.decode(split[0], StandardCharsets.UTF_8);
                String value = URLDecoder.decode(split[1], StandardCharsets.UTF_8);
                result.put(key, value);
            }
        }
        return result;
    }

    private String toJson(WeatherData weatherData) {
        Location loc = weatherData.getLocation();
        String locationJson = "{}";

        if (loc != null) {
            locationJson = String.format(java.util.Locale.US,
                    "{\"name\":\"%s\",\"country\":\"%s\",\"latitude\":%.8f,\"longitude\":%.8f}",
                    escapeJson(loc.getName()), escapeJson(loc.getCountry()), loc.getLatitude(), loc.getLongitude());
        }

        return "{" +
                "\"location\":" + locationJson + "," +
                "\"temperature\":" + String.format(java.util.Locale.US, "%.2f", weatherData.getTemperature()) + "," +
                "\"humidity\":" + weatherData.getHumidity() + "," +
                "\"windSpeed\":" + weatherData.getWindSpeed() + "," +
                "\"precipitation\":" + weatherData.getPrecipitation() + "," +
                "\"weatherDescription\":\"" + escapeJson(weatherData.getWeatherDescription()) + "\"" +
                "}";
    }

    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r");
    }

    private void sendJson(HttpExchange exchange, int statusCode, String body) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private void sendHtml(HttpExchange exchange, int statusCode, String html) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
