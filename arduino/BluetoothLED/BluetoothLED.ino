#include <DmxSimple.h>

#include <SoftwareSerial.h>
#define BTSerial Serial1
#define BAUD 115200
// Arduino Variablen
int counter;
int data;

// Android Variablen
String ON;
String OFF;

// Player - MH DMX Variablen
String kPan1 = "43";
String kPan2 = "0";
String kTilt1 = "45";
String kTilt2 = "0";
int kDimmer = 100;
int kShutter = 30;
int kFarbe = 160;
int kIris = 4;
int kFokus = 0;

// Computer - MH DMX Variablen
String mPan1 = "42";
String mPan2 = "0";
String mTilt1 = "45";
String mTilt2 = "0";
int mDimmer = 50;
int mShutter = 30;
int mFarbe = 80;
int mIris = 4;
int mFokus = 10;

void setup() {
  // Arduino vorbereiten
  Serial.begin(BAUD);
  BTSerial.begin(BAUD);

  // MH am Arduino registieren
  pinMode(2, OUTPUT);
  digitalWrite(2,HIGH);
  DmxSimple.usePin(36);
  pinMode(LED_BUILTIN, OUTPUT);
  digitalWrite(LED_BUILTIN, LOW);
  

  // Player - MH vorbereiten
  /*DmxSimple.write(1, kPan1); //Pan
  DmxSimple.write(2, kPan2); //Panfeintuning
  DmxSimple.write(3, kTilt1); //Tilt
  DmxSimple.write(4, kTilt2); //Tiltfeintuning
  DmxSimple.write(7, kDimmer); //Dimmer
  DmxSimple.write(8, kShutter); //Shutter
  DmxSimple.write(9, kFarbe); //Farbe
  DmxSimple.write(18, kIris); //Iris
  DmxSimple.write(22, kFokus); //Schärfe

  // Random  - MH vorbereiten
  DmxSimple.write(33, mPan1); //Pan
  DmxSimple.write(34, mPan2); //Panfeintuning
  DmxSimple.write(35, mTilt1); //Tilt
  DmxSimple.write(36, mTilt2); //Tiltfeintuning
  DmxSimple.write(39, mDimmer); //Dimmer
  DmxSimple.write(40, mShutter); //Shutter
  DmxSimple.write(41, mFarbe); //Farbe
  DmxSimple.write(50, mIris); //Iris
  DmxSimple.write(54, mFokus); //Schärfe*/

  // Die Ein- und Ausschaltwerte für die arduinointerne LED sind im UTF8 Format, da die Bluetooth LE Datenübertragung von Android zum Bluetooth Modul nur Werte in Form eines ByteArrays nach UTF Standard annimmt.
  ON = "111110";  // Ursprünglicher Wert in Android Studio zum Einschalten der LED war "on", dessen Wert in der UTF8 Tabelle 49 ist.
  OFF = "111102102"; // Ursprünglicher Wert in Android Studio zum Ausschalten der LED war "off", dessen Wert in der UTF8 Tabelle 48 ist.
  ESCAPE_CHARACTER = "126"; // Escape Character, der als Tilde "~" zum Bluetooth Modul geschickt wird. Dessen Wert in der UTF8 Tabelle ist 126.
  counter = -1;
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
    
    if (counter<0){
      counter += 1;
    }
    
    if (counter == 0){
      kPan1 = data;
      counter +=1;
    }
    else if (counter == 1){
      kPan2 = data;
      counter +=1;
    }
    else if (counter == 2){
      kTilt1 = data;
      counter +=1;
    }
    else if (counter == 3){
      kTilt2 = data;
      counter +=1;
    }
    else if (counter == 4){
      mPan1 = data;
      counter +=1;
    }
    else if (counter == 5){
      mPan2 = data;
      counter +=1;
    }
    else if (counter == 6){
      mTilt1 = data;
      counter +=1;
    }
    else if (counter == 7){
      mTilt2 = data;
      counter = 0;
      Serial.println("kPan1: " + kPan1);
      Serial.println("kPan2: " + kPan2);
      Serial.println("kTilt1: " + kTilt1);
      Serial.println("kTilt2: " + kTilt2);

      Serial.println("mPan1: " + mPan1);
      Serial.println("mPan2: " + mPan2);
      Serial.println("mTilt1: " + mTilt1);
      Serial.println("mTilt2: " + mTilt2);
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
  
  /*DmxSimple.write(1, kPan1); //Pan
  DmxSimple.write(2, kPan2); //Panfeintuning
  DmxSimple.write(3, kTilt1); //Tilt
  DmxSimple.write(4,kTilt2); //Tiltfeintuning
  DmxSimple.write(7, kDimmer); //Dimmer
  DmxSimple.write(9, kFarbe); //Farbe

  DmxSimple.write(33, kPan1); //Pan
  DmxSimple.write(34, kPan2); //Panfeintuning
  DmxSimple.write(35, kTilt1); //Tilt
  DmxSimple.write(36,kTilt2); //Tiltfeintuning
  DmxSimple.write(39, kDimmer); //Dimmer
  DmxSimple.write(41, kFarbe); //Farbe*/

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
