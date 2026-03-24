const cityInput = document.getElementById('city');
const searchButton = document.getElementById('search');
const messageDiv = document.getElementById('message');
const resultDiv = document.getElementById('result');

searchButton.addEventListener('click', async () => {
  const city = cityInput.value.trim();
  if (!city) {
    showError('Inserisci una città valida.');
    return;
  }
  resultDiv.innerHTML = '';
  messageDiv.textContent = 'Caricamento...';

  try {
    const response = await fetch(`/api/weather?city=${encodeURIComponent(city)}`);
    let data;

    try {
      data = await response.json();
    } catch (parseError) {
      showError(`Errore nell'analisi della risposta (${response.status})`);
      console.error('JSON parsing error', parseError);
      return;
    }

    if (!response.ok) {
      showError(`Errore ${response.status}: ${data.error || response.statusText}`);
      return;
    }

    showWeather(data);

  } catch (err) {
    showError('Errore di rete: ' + err.message + '. Controlla server e connessione.');
    console.error(err);
  }
});

function setTheme(description) {
  const mapping = {
    'Cielo sereno': 'sunny',
    'Parzialmente nuvoloso': 'cloudy',
    'Nuvoloso': 'cloudy',
    'Pioggia': 'rainy',
    'Neve': 'snowy',
    'Temporale': 'stormy',
    'Nebbia': 'foggy'
  };

  const emojiMap = {
    'Cielo sereno': '☀️',
    'Parzialmente nuvoloso': '⛅',
    'Nuvoloso': '☁️',
    'Pioggia': '🌧️',
    'Neve': '❄️',
    'Temporale': '⛈️',
    'Nebbia': '🌫️'
  };

  document.body.className = mapping[description] || '';

  const smallEmoji = document.getElementById('weather-emoji');
  const heroEmoji = document.getElementById('hero-emoji');
  const value = emojiMap[description] || '☀️';

  smallEmoji.textContent = value;
  heroEmoji.textContent = value;

  // visibile solo dopo la ricerca
  heroEmoji.style.display = 'block';
  heroEmoji.style.opacity = '0';
  setTimeout(() => heroEmoji.style.opacity = '1', 100);

  smallEmoji.style.transform = 'scale(1.3)';
  setTimeout(() => smallEmoji.style.transform = 'scale(1)', 250);

  const existingClouds = document.querySelector('.clouds');
  if (existingClouds) existingClouds.remove();
  const existingRain = document.querySelector('.rain-layer');
  if (existingRain) existingRain.remove();

  if (['cloudy', 'rainy', 'stormy', 'foggy'].includes(document.body.className)) {
    const clouds = document.createElement('div');
    clouds.className = 'clouds';
    clouds.innerHTML = '<div class="cloud"></div><div class="cloud"></div><div class="cloud"></div>';
    document.body.appendChild(clouds);
  }

  if (['rainy', 'stormy'].includes(document.body.className)) {
    const rain = document.createElement('div');
    rain.className = 'rain-layer';

    for (let i = 0; i < 90; i++) {
      const drop = document.createElement('div');
      drop.className = 'drop';
      drop.style.left = `${Math.random() * 100}vw`;
      drop.style.animationDelay = `${Math.random() * 0.8}s`;
      drop.style.animationDuration = `${0.7 + Math.random() * 0.4}s`;
      drop.style.opacity = `${0.4 + Math.random() * 0.6}`;
      rain.appendChild(drop);
    }

    document.body.appendChild(rain);

    const splash = document.createElement('div');
    splash.className = 'splash-layer';

    for (let i = 0; i < 20; i++) {
      const s = document.createElement('div');
      s.className = 'splash';
      s.style.left = `${Math.random() * 100}vw`;
      s.style.top = `${80 + Math.random() * 18}vh`;
      s.style.animationDelay = `${Math.random() * 0.5}s`;
      s.style.animationDuration = `${0.45 + Math.random() * 0.1}s`;
      splash.appendChild(s);
    }

    document.body.appendChild(splash);
  }

  if (document.body.className === 'stormy') {
    const existingFlash = document.querySelector('.lightning');
    if (existingFlash) existingFlash.remove();

    const flash = document.createElement('div');
    flash.className = 'lightning';
    flash.style.animation = `lightning-flash ${0.3 + Math.random() * 0.8}s ease-in-out 1`;

    document.body.appendChild(flash);

    // ricrea flash in modo randomico ogni 2-5 secondi
    setTimeout(() => {
      if (document.body.className === 'stormy') {
        flash.remove();
        setTheme(description);
      }
    }, 2200 + Math.random() * 2800);
  }
}

function showError(text) {
  messageDiv.textContent = text;
  messageDiv.className = 'message error';
  resultDiv.innerHTML = '';
}

function showWeather(data) {
  messageDiv.textContent = '';
  messageDiv.className = 'message';

  const html = `
    <div class="card">
      <h2>${escapeHtml(data.location.name)}, ${escapeHtml(data.location.country)}</h2>
      <p><strong>Temperatura:</strong> ${data.temperature.toFixed(1)} °C</p>
      <p><strong>Condizioni:</strong> ${escapeHtml(data.weatherDescription)}</p>
      <p><strong>Umidità:</strong> ${data.humidity.toFixed(0)}%</p>
      <p><strong>Vento:</strong> ${data.windSpeed.toFixed(1)} km/h</p>
      <p><strong>Precipitazioni:</strong> ${data.precipitation.toFixed(1)} mm</p>
      <p><strong>Coordinate:</strong> ${data.location.latitude.toFixed(4)}, ${data.location.longitude.toFixed(4)}</p>
    </div>
  `;

  resultDiv.innerHTML = html;
  setTheme(data.weatherDescription);
}

function escapeHtml(text) {
  if (!text) return '';
  return text
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#039;');
}
