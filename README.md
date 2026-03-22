# ⛅ Weather App

Una semplice applicazione meteo in Java che recupera dati meteorologici in tempo reale usando l'API Open-Meteo.

## 🎯 Funzionalità

- ✅ Ricerca meteo per qualsiasi città
- ✅ Visualizzazione di temperatura, umidità, velocità del vento
- ✅ Descrizione delle condizioni meteorologiche
- ✅ Interfaccia console user-friendly

## 📁 Struttura del Progetto

```
weather-app/
├── src/
│   ├── Main.java                           # Punto d'ingresso
│   └── com/weatherapp/
│       ├── client/                         # Client API
│       │   └── WeatherApiClient.java
│       ├── config/                         # Configurazioni
│       │   └── ApiConfig.java
│       ├── model/                          # Modelli dati
│       │   ├── Location.java
│       │   ├── WeatherData.java
│       │   └── Forecast.java
│       ├── service/                        # Logica di business
│       │   └── WeatherService.java
│       └── ui/                             # Interfaccia utente
│           └── ConsoleUI.java
├── bin/                                    # Compilati (generato automaticamente)
├── lib/                                    # Dipendenze
└── README.md
```

## 🚀 Come Compilare

### Da VS Code:
1. Apri il progetto in VS Code
2. Usa la scorciatoia **Ctrl+Shift+B** per compilare
3. Oppure esegui tramite il Java Projects explorer

### Da terminale:
```bash
cd c:\Users\User\weather-app
javac -d bin src/Main.java src/com/weatherapp/**/*.java
```

## ▶️ Come Eseguire

### Da VS Code:
1. Seleziona **Run** dal menu Java Projects

### Da terminale:
```bash
cd c:\Users\User\weather-app
java -cp bin Main
```

## 📋 Guida all'Uso

1. **Avvia l'app** → Vedrai il menu principale
2. **Seleziona opzione 1** → "Cerca meteo per una città"
3. **Inserisci il nome della città** → Es. "Roma", "Milano", "Parigi"
4. **Visualizza i risultati** → Temperatura, umidità, vento, precipitazioni

## 🔧 Architettura

### Separazione dei Concerni

- **Client Layer** (`WeatherApiClient`) - Comunicazione con API Open-Meteo
- **Service Layer** (`WeatherService`) - Logica di business e coordinate
- **Model Layer** - Strutture dati (`Location`, `WeatherData`, `Forecast`)
- **UI Layer** (`ConsoleUI`) - Interfaccia con l'utente
- **Config Layer** - Configurazioni centrali

## 📚 API Utilizzata

- **Open-Meteo Geocoding API** - Per geolocalizzazione
- **Open-Meteo Weather API** - Per dati meteorologici

Documentazione: https://open-meteo.com/en/docs

## 🛠️ Prossimi Passi

- [ ] Aggiungere libreria JSON (Gson/Jackson) per parsing migliore
- [ ] Implementare cache locale dei dati
- [ ] Aggiungere previsioni a lungo termine
- [ ] Creare interfaccia grafica (Swing/JavaFX)
- [ ] Aggiungere unit test

## 📝 Note di Sviluppo

Il parsing JSON è attualmente implementato manualmente per semplicità. In un vero progetto, usare:
```java
// build.gradle o pom.xml
implementation 'com.google.code.gson:gson:2.10.1'
```

E poi usare Gson per il parsing:
```java
JsonElement element = JsonParser.parseString(json);
```
