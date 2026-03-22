@echo off
REM Script per eseguire i test unitari
REM Compila prima il progetto e i test, poi esegue i test

echo Compilazione del progetto principale...
javac -d bin -cp "lib/*" src/Main.java src/com/weatherapp/client/WeatherApiClient.java src/com/weatherapp/config/ApiConfig.java src/com/weatherapp/model/Forecast.java src/com/weatherapp/model/Location.java src/com/weatherapp/model/WeatherData.java src/com/weatherapp/service/WeatherService.java src/com/weatherapp/ui/ConsoleUI.java

if %errorlevel% neq 0 (
    echo Errore nella compilazione del progetto principale
    pause
    exit /b 1
)

echo Compilazione dei test...
javac -d bin -cp "bin;lib/*" test/com/weatherapp/client/WeatherApiClientTest.java test/com/weatherapp/service/WeatherServiceTest.java test/com/weatherapp/ui/ConsoleUITest.java test/com/weatherapp/TestRunner.java

if %errorlevel% neq 0 (
    echo Errore nella compilazione dei test
    pause
    exit /b 1
)

echo.
echo === ESECUZIONE TEST ===
echo.

REM Esegui i test usando JUnit Console Launcher
java -cp "bin;lib/*" org.junit.platform.console.ConsoleLauncher --scan-classpath --include-classname=.*Test

if %errorlevel% neq 0 (
    echo.
    echo Alcuni test sono falliti!
    echo Puoi anche provare: java -cp "bin;lib/*" com.weatherapp.TestRunner
) else (
    echo.
    echo Tutti i test sono passati! ✅
)

pause