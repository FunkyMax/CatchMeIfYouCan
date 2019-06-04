#include <DMXSerial.h>

//#include <DmxSimple.h>

//#include <Softwar eSerial.h>
#define BTSerial Serial1
#define BAUD 115200

// Arduino Variablen
int counter;
int data;

// Android Variablen
String ON;
String OFF;

// Player - MH DMX Variablen
int kPan1 = 43;
int kPan2 = 0;
int kTilt1 = 130;
int kTilt2 = 0;
int kDimmer = 50;
int kShutter = 30;
int kFarbe = 160;
int kIris = 200;
int kFokus = 0;

// Computer - MH DMX Variablen
int mPan1 = 42;
int mPan2 = 0;
int mTilt1 = 45;
int mTilt2 = 0;
int mDimmer = 30;
int mShutter = 30;
int mFarbe = 80;
int mIris = 200;
int mFokus = 10;

void setup() {
  // Arduino vorbereiten
  //Serial.begin(BAUD);
  BTSerial.begin(BAUD);

  // MH am Arduino registieren
  pinMode(2, OUTPUT);
  digitalWrite(2,HIGH);
  //DmxSimple.usePin(36);
  pinMode(LED_BUILTIN, OUTPUT);
  digitalWrite(LED_BUILTIN, LOW);
  DMXSerial.init(DMXController);
  

  // Player - MH vorbereiten
  DMXSerial.write(62, 45);
  sendDataToDMX();
  /*DMXSerial.write(1, kPan1); //Pan
  DMXSerial.write(2, kPan2); //Panfeintuning
  DMXSerial.write(3, kTilt1); //Tilt
  DMXSerial.write(4, kTilt2); //Tiltfeintuning
  DMXSerial.write(7, kDimmer); //Dimmer
  DMXSerial.write(8, kShutter); //Shutter
  DMXSerial.write(9, kFarbe); //Farbe
  DMXSerial.write(18, kIris); //Iris
  DMXSerial.write(22, kFokus); //Schärfe

  // Random  - MH vorbereiten
  DMXSerial.write(33, mPan1); //Pan
  DMXSerial.write(34, mPan2); //Panfeintuning
  DMXSerial.write(35, mTilt1); //Tilt
  DMXSerial.write(36, mTilt2); //Tiltfeintuning
  DMXSerial.write(39, mDimmer); //Dimmer
  DMXSerial.write(40, mShutter); //Shutter
  DMXSerial.write(41, mFarbe); //Farbe
  DMXSerial.write(50, mIris); //Iris
  DMXSerial.write(54, mFokus); //Schärfe*/

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
      sendDataToDMX();
      counter = 0;
      /*Serial.println(kPan1);
      Serial.println(kPan2);
      Serial.println(kTilt1);
      Serial.println(kTilt2);

      Serial.println(mPan1);
      Serial.println(mPan2);
      Serial.println(mTilt1);
      Serial.println(mTilt2);*/
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
  
  DMXSerial.write(1, kPan1); //Pan
  DMXSerial.write(2, kPan2); //Panfeintuning
  DMXSerial.write(3, kTilt1); //Tilt
  DMXSerial.write(4, kTilt2); //Tiltfeintuning
  DMXSerial.write(5, 12);     // Motorgeschwindigkeit
  DMXSerial.write(7, kDimmer); //Dimmer
  DMXSerial.write(8, 30);      // Shutter
  DMXSerial.write(9, kFarbe); //Farbe
  

  DMXSerial.write(26, mPan1); //Pan
  DMXSerial.write(27, mPan2); //Panfeintuning
  DMXSerial.write(28, mTilt1); //Tilt
  DMXSerial.write(29, mTilt2); //Tiltfeintuning
  DMXSerial.write(30, 12);      // Motorgeschwindigkeit
  DMXSerial.write(32, mDimmer); //Dimmer
  DMXSerial.write(33, 30);      // Shutter
  DMXSerial.write(34, mFarbe); //Farbe

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
