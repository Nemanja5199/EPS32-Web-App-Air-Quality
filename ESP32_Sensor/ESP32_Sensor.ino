  #include <DHT.h>
  #include <WiFi.h>
  #include<HTTPClient.h>
  #include<ArduinoJson.h>



  #define DHTPIN 23       
  #define DHTTYPE DHT22   

  const char* ssid = "YOUR_SSID";      
  const char* password = "YOUR_PASSWORD";
  const char* apiEndpoint = "http://192.168.1.108:8080/api/sensor";

  hw_timer_t * timer = NULL;
  volatile bool shouldMeasure = false;

  DHT dht(DHTPIN, DHTTYPE);

  struct SensorData {
      float temperature;
      float humidity;
  };

  void IRAM_ATTR onTimer() {
      shouldMeasure = true;  
  }


  void connectToWifi(){

    Serial.print("Connecting to Wifi");
    WiFi.begin(ssid, password);
    while (WiFi.status() != WL_CONNECTED) {
          delay(500);
          Serial.print(".");
  }


    Serial.println();
        Serial.println("WiFi connected!");
        Serial.print("IP address: ");
        Serial.println(WiFi.localIP());

  }


  void sendSensorData(SensorData data){

    

    StaticJsonDocument  <200> doc;

    doc["temperature"]= data.temperature;
    doc["humidity"]= data.humidity;

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
        dht.begin();
        timer = timerBegin(0, 80, true);
        timerAttachInterrupt(timer, &onTimer, true);
        timerAlarmWrite(timer, 10000000, true);  
        timerAlarmEnable(timer);
        connectToWifi();
    }

    void loop() {
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
        return data;
    }

    void printSensorData(SensorData data) {
        Serial.print("Temperature: ");
        Serial.print(data.temperature);
        Serial.print("°C  Humidity: ");
        Serial.print(data.humidity);
        Serial.println("%");
    }