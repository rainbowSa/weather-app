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

## 🧪 Test Unitari

Il progetto include una suite completa di test unitari che coprono:

- ✅ **Formattazione richieste API** - Verifica che gli URL siano costruiti correttamente
- ✅ **Gestione input utente** - Test per input mancanti, null, o errati
- ✅ **Casi limite** - Problemi di rete, città inesistenti, dati malformati

### Dipendenze per i Test
I test utilizzano **JUnit 5** (Jupiter). Le dipendenze sono già incluse in `lib/`:
- `junit-jupiter-api-5.10.1.jar`
- `junit-jupiter-engine-5.10.1.jar`
- `junit-platform-launcher-1.10.1.jar`
- `opentest4j-1.3.0.jar`
- `apiguardian-api-1.1.2.jar`

### Eseguire i Test

#### Opzione 1: Script Automatico (Raccomandato)
```bash
# Windows
run-tests.bat
```

#### Opzione 2: Manualmente
```bash
# Compila progetto e test
javac -d bin -cp "lib/*" src/Main.java src/com/weatherapp/**/*.java
javac -d bin -cp "bin;lib/*" test/TestRunner.java test/com/weatherapp/**/*.java

# Esegui i test
java -cp "bin;lib/*" com.weatherapp.TestRunner
```

### Struttura dei Test
```
test/
├── TestRunner.java                        # Launcher principale dei test
└── com/weatherapp/
    ├── client/
    │   └── WeatherApiClientTest.java      # Test client API
    ├── service/
    │   └── WeatherServiceTest.java        # Test logica di business
    └── ui/
        └── ConsoleUITest.java             # Test interfaccia utente
```

## ▶️ Come Eseguire

### Modalità Console (esistente)
Da VS Code:
1. Seleziona **Run** dal menu Java Projects

Da terminale:
```bash
cd c:\Users\User\weather-app
java -cp bin Main
```

### Modalità Web (nuova)
1. Compila l'intero progetto:
```bash
cd c:\Users\User\weather-app
javac -d bin src/Main.java src/com/weatherapp/**/*.java
```
2. Esegui il server web:
```bash
java -cp bin com.weatherapp.web.WeatherWebServer
```
3. Apri il browser:
`http://localhost:8080/index.html`

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
