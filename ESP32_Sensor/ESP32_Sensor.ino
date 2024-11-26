  #include <DHT.h>
  #include <WiFi.h>
  #include <HTTPClient.h>
  #include <ArduinoJson.h>
  #include <esp_task_wdt.h>
  #include "Adafruit_CCS811.h"  

  #define WDT_TIMEOUT 30     
  #define DHTPIN 23       
  #define DHTTYPE DHT22   

  const char* ssid = "YOUR_SSID";      
  const char* password = "YOUR_PASSWORD";
  const char* apiEndpoint = "http://192.168.1.108:8080/api/sensor";

  hw_timer_t * timer = NULL;
  volatile bool shouldMeasure = false;

  DHT dht(DHTPIN, DHTTYPE);
  Adafruit_CCS811 ccs;

  struct SensorData {
      float temperature;
      float humidity;
       uint16_t  co2;
    uint16_t  tvoc;
  };


  void IRAM_ATTR onTimer() {
      shouldMeasure = true;  
  }

  void connectToWifi() {
      Serial.print("Connecting to Wifi");
      WiFi.begin(ssid, password);
      while (WiFi.status() != WL_CONNECTED) {
          delay(500);
          Serial.print(".");
          esp_task_wdt_reset();  
      }

      Serial.println();
      Serial.println("WiFi connected!");
      Serial.print("IP address: ");
      Serial.println(WiFi.localIP());
  }

  void sendSensorData(SensorData data) {
      StaticJsonDocument<400> doc;
      doc["temperature"] = data.temperature;
      doc["humidity"] = data.humidity;
      doc["co2"] = data.co2;
      doc["tvoc"] = data.tvoc;

      String jsonString;
      serializeJson(doc, jsonString);

      HTTPClient http;
      http.begin(apiEndpoint);
      http.addHeader("Content-Type", "application/json");
      int httpResponseCode = http.POST(jsonString);

      if (httpResponseCode > 0) {
          Serial.print("HTTP Response code: ");
          Serial.println(httpResponseCode);
          String response = http.getString();
          Serial.println(response);
      } else {
          Serial.println("Server Error");
      }

      http.end();
  }

  void setup() {
      Serial.begin(9600);
      
      
      esp_task_wdt_init(WDT_TIMEOUT, true);
      esp_task_wdt_add(NULL);
      Serial.println("Watchdog enabled!");
      
      dht.begin();
      ccs.begin();
      
      timer = timerBegin(0, 80, true);
      timerAttachInterrupt(timer, &onTimer, true);
      timerAlarmWrite(timer, 10000000, true);  
      timerAlarmEnable(timer);
      connectToWifi();
  }

  void loop() {
      esp_task_wdt_reset();  
      
      if (shouldMeasure) {
          shouldMeasure = false;
          SensorData data = readSensorData();
          printSensorData(data);
          sendSensorData(data);
      }

      if (WiFi.status() != WL_CONNECTED) {
          Serial.println("WiFi connection lost! Reconnecting...");
          connectToWifi();
      }
  }

  SensorData readSensorData() {
      SensorData data;
      data.temperature = dht.readTemperature();
      data.humidity = dht.readHumidity();
      if(ccs.available()){
      if(!ccs.readData()){
      data.co2 = ccs.geteCO2();
      data.tvoc = ccs.getTVOC();
    }
      return data;
  }
}


  void printSensorData(SensorData data) {
      Serial.print("Temperature: ");
      Serial.print(data.temperature);
      Serial.print("°C  Humidity: ");
      Serial.print(data.humidity);
      Serial.println("%");
      Serial.print("CO2: ");
      Serial.print(data.co2);
      Serial.print("ppm , TVOC: ");
      Serial.println(data.tvoc);
  }


 