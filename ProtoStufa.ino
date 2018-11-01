#include "DHT.h"
#include <WiFiEsp.h>
#include <WiFiEspClient.h>
#include <PubSubClient.h>
#include <SoftwareSerial.h>

#define AP_SSID "Mob-236578"
#define AP_PASSWORD "bolochocolate"
#define SERVER "192.168.1.6"
#define PORT 1883
#define DHTTYPE DHT11
#define estufaId "Estufa"

#define TOPICO_SUBSCRIBE "MQTTEstufaCasaEnvia"     //tópico MQTT de escuta
#define TOPICO_PUBLISH   "MQTTEstufaCasaRecebe"    //tópico MQTT de envio de informações para Broker                                                
#define ID_MQTT  "EstufaCasa"     //id mqtt (para identificação de sessão)

const byte DHTPIN = 8;
DHT dht(DHTPIN, DHTTYPE);

SoftwareSerial esp8266Serial(3, 4);
WiFiEspClient espClient;
PubSubClient MQTT(espClient);

char EstadoSaida = '0';  //variável que armazena o estado atual da saídai
const char* BROKER_MQTT = "192.168.1.6"; //URL do broker MQTT que se deseja utilizar
int BROKER_PORT = 1883; // Porta do Broker MQTT

int status = WL_IDLE_STATUS;
float h;
float t;
float f;
float hif;
float hic;

int led4Pin = 7;
int led3Pin = 6;
int led2Pin = 5;
int led1Pin = 2;

//Intervalos
  const unsigned long checkWifiInterval = 1000;
  const unsigned long umidTempInterval = 5000;
  const unsigned long sendDataInterval = 10000;

//Timers
  unsigned long checkWifiTimer;
  unsigned long umidTempTimer;
  unsigned long sendDataTimer;

  
  void initMQTT();
  void mqtt_callback(char* topic, byte* payload, unsigned int length);
  void VerificaConexoesWiFIEMQTT(void);
  void InitOutput(void);
  
  void setup(){
    Serial.begin(9600);
    initWiFi();
    dht.begin();
    
    InitOutput();
    initMQTT();
    
    MQTT.setServer(SERVER, PORT);
  
    Serial.println("Connecting to the wireless...");
    while ( WiFi.status() != WL_CONNECTED) {
      Serial.print('.');
      delay(500);
    }
      
    delay(5000);  // Delay to allow interface to autoconfigure
  
    Serial.println("Connecting to the MQTT broker...");
    MQTT.connect(estufaId);
    delay(1000);
  
    MQTT.publish("mm/p", "Message from ESP8266 to MQTT");
  
    pinMode(led1Pin, OUTPUT);
    pinMode(led2Pin, OUTPUT);
    pinMode(led3Pin, OUTPUT);
    pinMode(led4Pin, OUTPUT);
  
    digitalWrite(led4Pin, HIGH);
    digitalWrite(led3Pin, HIGH);
    digitalWrite(led2Pin, HIGH);
    digitalWrite(led1Pin, HIGH);
    
    umidTempTimer = millis();
    checkWifiTimer = millis();
    sendDataTimer = millis();
  }
  
 

  
  void VerificaConexoesWiFIEMQTT(void)
  {
      if (!MQTT.connected()) 
          reconnectMQTT(); //se não há conexão com o Broker, a conexão é refeita
       
       reconectWiFi(); //se não há conexão com o WiFI, a conexão é refeita
  }

  void EnviaEstadoOutputMQTT(void)
  {
      if (EstadoSaida == '0')
        MQTT.publish(TOPICO_PUBLISH, "off");
   
      if (EstadoSaida == '1')
        MQTT.publish(TOPICO_PUBLISH, "on");
   
      Serial.println("- Estado da saida led4Pin enviado ao broker!");
      delay(1000);
  }
  
  void afereUmidTemp(){
  
    h = dht.readHumidity();
    t = dht.readTemperature();
    f = dht.readTemperature(true);
  
    if (isnan(h) || isnan(t) || isnan(f)) {
      Serial.println("Failed to read from DHT sensor!");
      return;
    }
  
    hif = dht.computeHeatIndex(f, h);
    hic = dht.computeHeatIndex(t, h, false);
  
    Serial.print("Umidade: ");
    Serial.print(h);
    Serial.println(" %\t");
    Serial.print("Temperatura: ");
    Serial.print(t);
    Serial.print(" °C ");
    Serial.print(f);
    Serial.println(" °F\t");
    Serial.print("índice de aquecimento: ");
    Serial.print(hic);
    Serial.print(" °C ");
    Serial.print(hif);
    Serial.println(" °F");
  
    String payload = "{";
    payload += "\"temperatura\":"; payload += t; payload += ",";
    payload += "\"umidade\":"; payload += h;
    payload += "}";
  
    char data[payload.length()+1];
    Serial.print("Sendind data ");
    Serial.println(payload);
    payload.toCharArray(data, payload.length()+1);
    sendData(data);
  
    umidTempTimer = millis();
  }

  void initMQTT() 
  {
      MQTT.setServer(BROKER_MQTT, BROKER_PORT);   //informa qual broker e porta deve ser conectado
      MQTT.setCallback(mqtt_callback);            //atribui função de callback (função chamada quando qualquer informação de um dos tópicos subescritos chega)
  }

  void mqtt_callback(char* topic, byte* payload, unsigned int length) 
  {
      String msg;
   
      //obtem a string do payload recebido
      for(int i = 0; i < length; i++) 
      {
         char c = (char)payload[i];
         msg += c;
      }
     
      //toma ação dependendo da string recebida:
      //verifica se deve colocar nivel alto de tensão na saída led4Pin:
      //IMPORTANTE: o Led já contido na placa é acionado com lógica invertida (ou seja,
      //enviar HIGH para o output faz o Led apagar / enviar LOW faz o Led acender)
      if (msg.equals("L"))
      {
          digitalWrite(led4Pin, LOW);
          EstadoSaida = '1';
      }
   
      //verifica se deve colocar nivel alto de tensão na saída led4Pin:
      if (msg.equals("D"))
      {
          digitalWrite(led4Pin, HIGH);
          EstadoSaida = '0';
      }
       
  }

  void reconnectMQTT() 
  {
      while (!MQTT.connected()) 
      {
          Serial.print("* Tentando se conectar ao Broker MQTT: ");
          Serial.println(BROKER_MQTT);
          if (MQTT.connect(ID_MQTT)) 
          {
              Serial.println("Conectado com sucesso ao broker MQTT!");
              MQTT.subscribe(TOPICO_SUBSCRIBE); 
          } 
          else
          {
              Serial.println("Falha ao reconectar no broker.");
              Serial.println("Havera nova tentatica de conexao em 2s");
              delay(2000);
          }
      }
  }


  //void checkWifiStatus(){
  //  status = WiFi.status();
  //  if (status != WL_CONNECTED){
  //    reconnectWiFi();
  //  }
  //  if (!MQTT.connected()){
  //    reconnectClient();
  //  }
  //
  //  checkWifiTimer = millis();
  //}

  void sendData(char* data){
    if (MQTT.publish(estufaId, data)){
      Serial.println("[PubSubClient] Payload sent sucessfully");
    } else {
      Serial.println("[PubSubClient] Error while trying to send payload");
    }
    sendDataTimer = millis();
  }

  void initWiFi(){
    esp8266Serial.begin(9600);
    WiFi.init(&esp8266Serial);
    if (WiFi.status() == WL_NO_SHIELD){
      Serial.println("[WiFiEsp] WiFi shield not present");
      while (true);
    }
  
    Serial.println("[WiFiEsp] Connecting to AP ...");
    while (status != WL_CONNECTED) {
      Serial.print("[WiFiEsp] Attempting to connect to WPA AP_SSID: ");
      Serial.println(AP_SSID);
      status = WiFi.begin(AP_SSID, AP_PASSWORD);
      delay(500);
    }
    Serial.println("[WiFiEsp] Connected to AP");
  }

  void reconectWiFi() 
  {
      //se já está conectado a rede WI-FI, nada é feito. 
      //Caso contrário, são efetuadas tentativas de conexão
      if (WiFi.status() == WL_CONNECTED)
          return;
           
      WiFi.begin(AP_SSID, AP_PASSWORD); // Conecta na rede WI-FI
       
      while (WiFi.status() != WL_CONNECTED) 
      {
          delay(100);
          Serial.print(".");
      }
     
      Serial.println();
      Serial.print("Conectado com sucesso na rede ");
      Serial.print(AP_SSID);
      Serial.println("IP obtido: ");
      Serial.println(WiFi.localIP());
  }

  void reconnectClient(){
    while (!MQTT.connected()){
      Serial.print("[PubSubClient] Connecting to ");
      Serial.println(SERVER);
      if (MQTT.connect(estufaId)){ //(clientId, username, password)
        Serial.println( "[PubSubClient] Connected" );
      } else {
        Serial.print("[PubSubClient] Failed to connect, retrying in 5 seconds");
        delay(5000);
      }
    }
  }

  void InitOutput(void)
  {
      //IMPORTANTE: o Led já contido na placa é acionado com lógica invertida (ou seja,
      //enviar HIGH para o output faz o Led apagar / enviar LOW faz o Led acender)
      pinMode(led4Pin, OUTPUT);
      digitalWrite(led4Pin, HIGH);          
  }

 void loop(){
  //  if((millis() - umidTempTimer) >= umidTempInterval){
  //   afereUmidTemp();
  //  }
  //  if((millis() - checkWifiTimer) >= checkWifiInterval){
  //   checkWifiStatus();
  //  }
  
    //garante funcionamento das conexões WiFi e ao broker MQTT
    VerificaConexoesWiFIEMQTT();
 
    //envia o status de todos os outputs para o Broker no protocolo esperado
    EnviaEstadoOutputMQTT();
 
    //keep-alive da comunicação com broker MQTT
    MQTT.loop();
  }
