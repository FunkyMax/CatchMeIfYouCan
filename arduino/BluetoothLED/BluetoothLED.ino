#include <SoftwareSerial.h>
#define BTSerial Serial1
#define BAUD 115200
String c;
String ON;
String OFF;

void setup() {
  Serial.begin(BAUD);
  BTSerial.begin(BAUD);
  pinMode(LED_BUILTIN, OUTPUT);
  digitalWrite(LED_BUILTIN, LOW);

  // Die Ein- und Ausschaltwerte für die arduinointerne LED sind im UTF8 Format, da die Bluetooth LE Datenübertragung von Android zum Bluetooth Modul nur Werte in Form eines ByteArrays nach UTF Standard annimmt.
  ON = "49";  // Ursprünglicher Input Wert zum Einschalten der LED war "1", dessen Wert in der UTF8 Tabelle 49 ist.
  OFF = "48"; // Ursprünglicher Input Wert zum Ausschalten der LED war "0", dessen Wert in der UTF8 Tabelle 48 ist.
}

void loop() {
  // Wenn eine Nachricht vom Bluetooth Modul kommt, printe sie im seriellen Monitor und leite sie weiter an die arduinointerne LED
  if (BTSerial.available()) {
    c = BTSerial.read();
    Serial.print(c);
    
    if (c.equals(ON)){
      digitalWrite(LED_BUILTIN, HIGH);
    }
    
    else if (c.equals(OFF)){
      digitalWrite(LED_BUILTIN, LOW);
    }
  }
}
