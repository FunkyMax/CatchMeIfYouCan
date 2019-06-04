//#include <DMXSerial.h>
#include <SoftwareSerial.h>
#define BTSerial Serial1
#define BAUD 115200

// Arduino Variablen
int counter;
int data;

// Android Variablen
String ON;
String OFF;

// PlayerMH DMX Variablen
int playerPan1 = 45;
int playerPan2 = 0;
int playerTilt1 = 130;
int playerTilt2 = 0;
int playerDimmer = 30;
int playerShutter = 30;
int playerFarbe = 160;
int playerIris = 200;
int playerFokus = 0;

// randomBlueMH DMX Variablen
int randomBluePan1 = 45;
int randomBluePan2 = 0;
int randomBlueTilt1 = 130;
int randomBlueTilt2 = 0;
int randomBlueDimmer = 30;
int randomBlueShutter = 30;
int randomBlueFarbe = 80;
int randomBlueIris = 200;
int randomBlueFokus = 10;

// randomYellowMH DMX Variablen
int randomYellowPan1 = 45;
int randomYellowPan2 = 0;
int randomYellowTilt1 = 130;
int randomYellowTilt2 = 0;
int randomYellowDimmer = 30;
int randomYellowShutter = 30;
int randomYellowFarbe = 80;
int randomYellowIris = 200;
int randomYellowFokus = 10;

// randomRedMH DMX Variablen
int randomRedPan1 = 45;
int randomRedPan2 = 0;
int randomRedTilt1 = 130;
int randomRedTilt2 = 0;
int randomRedDimmer = 30;
int randomRedShutter = 30;
int randomRedFarbe = 80;
int randomRedIris = 200;
int randomRedFokus = 10;

void setup() {
  // Arduino vorbereiten
  Serial.begin(BAUD);
  BTSerial.begin(BAUD);

  // MH am Arduino registieren
  pinMode(2, OUTPUT);     // Digital pin 2 is declared as Output since the second MH is connected to that pin.
  digitalWrite(2,HIGH);   // We need high Level (5V) for the second MH.
  //DmxSimple.usePin(36);
  pinMode(LED_BUILTIN, OUTPUT);
  digitalWrite(LED_BUILTIN, LOW);
  //DMXSerial.init(DMXController);
  

  // Player - MH vorbereiten
  //DMXSerial.write(62, 45);
  sendDataToDMX();

  // Die Ein- und Ausschaltwerte für die arduinointerne LED sind im UTF8 Format, da die Bluetooth LE Datenübertragung von Android zum Bluetooth Modul nur Werte in Form eines ByteArrays nach UTF Standard annimmt.
  ON = "111110";  // Ursprünglicher Wert in Android Studio zum Einschalten der LED war "on", dessen Wert in der UTF8 Tabelle 49 ist.
  OFF = "111102102"; // Ursprünglicher Wert in Android Studio zum Ausschalten der LED war "off", dessen Wert in der UTF8 Tabelle 48 ist.
  counter = -9;
}

void loop() {
  
  // In jedem Loop Durchlauf wird exakt 1 Byte empfangen, bestehend aus 2 Zahlen, welches widerum eine Integer Zahl darstellt (zB wenn die Koordinate 713 vom Android Handy geschickt wurde,
  // entspricht das den Bytes [55,49,51]. Wenn nun die erste Koordinate bestehend aus 3-4 Bytes beim Arduino ankommt, gelangt der Code in die if-Abfrage, bei der der Counter = 0 ist.
  // Nachdem die Koordinate vollständig empfangen wurde (also alle 3-4 Bytes empfangen worden sind - abhängig von der Größe der Koordinate), wird als nächstes der ESCAPE CHARACTER empfangen,
  // welcher signalisiert, dass die Koordinate zuende ist und eine neue Koordinate geschickt wird.
  // Dies geschieht weiter unten in der if-Abfrage, ob "data" dem ESCAPE CHARACTER entspricht. Der Counter wird deshalb weitergezählt, sodass die nächste Variable mit ihrer Koordinate belegt wird.
  // Wenn alle 4 Variablen (playerHeadlightBeamViewCurrentX, playerHeadlightBeamViewCurrentY, randomHeadlightBeamViewCurrentX, randomHeadlightBeamViewCurrentY) mit ihrem Wert belegt sind,
  // werden die Werte an das DMX Endgerät weitergeleitet und die Variablen wieder resettet.
  
  if (BTSerial.available()) { 
    data = BTSerial.read();
    Serial.println(data);
    if (counter<0){
      counter += 1;
    }
    
    if (counter == 0){
      playerPan1 = data;
      counter +=1;
    }
    else if (counter == 1){
      playerPan2 = data;
      counter +=1;
    }
    else if (counter == 2){
      playerTilt1 = data;
      counter +=1;
    }
    else if (counter == 3){
      playerTilt2 = data;
      counter +=1;
    }
    else if (counter == 4){
      randomBluePan1 = data;
      counter +=1;
    }
    else if (counter == 5){
      randomBluePan2 = data;
      counter +=1;
    }
    else if (counter == 6){
      randomBlueTilt1 = data;
      counter +=1;
    }
    else if (counter == 7){
      randomBlueTilt2 = data;
    }
    else if (counter == 8){
      randomYellowPan1 = data;
      counter += 1;
    }
    else if (counter == 9){
      randomYellowPan2 = data;
      counter += 1;
    }
    else if (counter == 10){
      randomYellowTilt1 = data;
      counter += 1;
    }
    else if (counter == 11){
      randomYellowTilt2 = data;
      //sendDataToDMX();
      counter = 0;
      Serial.println(playerPan1);
      Serial.println(playerPan2);
      Serial.println(playerTilt1);
      Serial.println(playerTilt2);
      Serial.println("");
      Serial.println(randomBluePan1);
      Serial.println(randomBluePan2);
      Serial.println(randomBlueTilt1);
      Serial.println(randomBlueTilt2);
      Serial.println("");
      Serial.println(randomYellowPan1);
      Serial.println(randomYellowPan2);
      Serial.println(randomYellowTilt1);
      Serial.println(randomYellowTilt2);
    }

   // Code um die BluetoothLED ein und auszuschalten
    /*if (data.equals(ON)){
      digitalWrite(LED_BUILTIN, HIGH);
      data = "";
    }

    else if (data.equals(OFF)){
      digitalWrite(LED_BUILTIN, LOW);
      data = "";
    }*/
  }
}

void sendDataToDMX(){
  // Lampe manuell einmal starten
  // Bluetoothwerte auslesen und in Variable schreiben
  
  /*DMXSerial.write(1, playerPan1); //Pan
  DMXSerial.write(2, playerPan2); //Panfeintuning
  DMXSerial.write(3, playerTilt1); //Tilt
  DMXSerial.write(4, playerTilt2); //Tiltfeintuning
  DMXSerial.write(5, 12);     // Motorgeschwindigkeit
  DMXSerial.write(7, playerDimmer); //Dimmer
  DMXSerial.write(8, 30);      // Shutter
  DMXSerial.write(9, playerFarbe); //Farbe
  
  DMXSerial.write(26, randomBluePan1); //Pan
  DMXSerial.write(27, randomBluePan2); //Panfeintuning
  DMXSerial.write(28, randomBlueTilt1); //Tilt
  DMXSerial.write(29, randomBlueTilt2); //Tiltfeintuning
  DMXSerial.write(30, 12);      // Motorgeschwindigkeit
  DMXSerial.write(32, randomBlueDimmer); //Dimmer
  DMXSerial.write(33, 30);      // Shutter
  DMXSerial.write(34, randomBlueFarbe); //Farbe

  DMXSerial.write(51, randomYellowPan1); //Pan
  DMXSerial.write(52, randomYellowPan2); //Panfeintuning
  DMXSerial.write(53, randomYellowTilt1); //Tilt
  DMXSerial.write(54, randomYellowTilt2); //Tiltfeintuning
  DMXSerial.write(55, 12);      // Motorgeschwindigkeit
  DMXSerial.write(57, randomYellowDimmer); //Dimmer
  DMXSerial.write(58, 30);      // Shutter
  DMXSerial.write(59, randomYellowFarbe); //Farbe

  DMXSerial.write(76, randomRedPan1); //Pan
  DMXSerial.write(77, randomRedPan2); //Panfeintuning
  DMXSerial.write(78, randomRedTilt1); //Tilt
  DMXSerial.write(79, randomRedTilt2); //Tiltfeintuning
  DMXSerial.write(80, 12);      // Motorgeschwindigkeit
  DMXSerial.write(82, randomRedDimmer); //Dimmer
  DMXSerial.write(83, 30);      // Shutter
  DMXSerial.write(84, randomRedFarbe); //Farbe
  */

  /*if (data.equals("49")){
      kTilt1 = 130;
      data = "";
     }
    else if (data.equals("48")){
      kTilt1 = 45;
      data = "";
    }*/

    delay(10);
}
