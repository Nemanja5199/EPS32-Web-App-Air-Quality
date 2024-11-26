const ws = new WebSocket('ws://localhost:8080/ws/sensor');
const statusDiv = document.getElementById('status');
const sensorDiv = document.getElementById('sensorData');
const debugDiv = document.getElementById('debug');

function addDebugMessage(message) {
   const time = new Date().toLocaleTimeString();
   debugDiv.innerHTML = `[${time}] ${message}\n`;
}

ws.onopen = () => {
   addDebugMessage('WebSocket Connected');
   statusDiv.textContent = 'Connected ✅';
   statusDiv.style.color = 'green';
};

ws.onmessage = (event) => {
   try {
       const data = JSON.parse(event.data);
       sensorDiv.innerHTML = `
           <div>Temperature: ${data.temperature}°C</div>
           <div>Humidity: ${data.humidity}%</div>
           <div>CO2: ${data.co2}ppm</div>
           <div>TVCO: ${data.tvco}</div>
           <div>Updated: ${new Date().toLocaleTimeString()}</div>
       `;
   } catch (e) {
       addDebugMessage(`Error parsing message: ${e.message}`);
   }
};

ws.onclose = () => {
   addDebugMessage('WebSocket Disconnected');
   statusDiv.textContent = 'Disconnected ⚠️';
   statusDiv.style.color = 'orange';
};

ws.onerror = (error) => {
   addDebugMessage(`WebSocket Error: ${error}`);
   statusDiv.textContent = 'Error ❌';
   statusDiv.style.color = 'red';
};