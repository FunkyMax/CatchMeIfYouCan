#include <SoftwareSerial.h>
#define BTSerial Serial1
#define BAUD 115200
int length;
int counter;
String data;
String ON;
String OFF;
String ESCAPE_CHARACTER;

String playerHeadlightBeamViewCurrentX;
String playerHeadlightBeamViewCurrentY;
String randomHeadlightBeamViewCurrentX;
String randomHeadlightBeamViewCurrentY;

void setup() {
  Serial.begin(BAUD);
  BTSerial.begin(BAUD);
  pinMode(LED_BUILTIN, OUTPUT);
  digitalWrite(LED_BUILTIN, LOW);

  // Die Ein- und Ausschaltwerte für die arduinointerne LED sind im UTF8 Format, da die Bluetooth LE Datenübertragung von Android zum Bluetooth Modul nur Werte in Form eines ByteArrays nach UTF Standard annimmt.
  ON = "111110";  // Ursprünglicher Wert in Android Studio zum Einschalten der LED war "on", dessen Wert in der UTF8 Tabelle 49 ist.
  OFF = "111102102"; // Ursprünglicher Wert in Android Studio zum Ausschalten der LED war "off", dessen Wert in der UTF8 Tabelle 48 ist.
  ESCAPE_CHARACTER = "195159195159";
  counter = 0;
}

void loop() {
  // Wenn Daten vom Bluetooth Modul kommen, printe sie im seriellen Monitor und leite sie weiter an die arduinointerne LED
  if (BTSerial.available()) {
    data += BTSerial.read();
    //length = data.length();
    Serial.println(data);
    if (data.substring(data.length()-12,data.length()).equals(ESCAPE_CHARACTER)){
      if (counter == 0){
          Serial.println(data.substring(0,data.length()-12));
          playerHeadlightBeamViewCurrentX = data.substring(0,data.length()-12);
          Serial.println(counter);
          Serial.print("playerHeadlightBeamViewCurrentX: " + playerHeadlightBeamViewCurrentX);
          data = "";
          counter +=1;
      }
      else if (counter == 1){
          playerHeadlightBeamViewCurrentY = data.substring(0,data.length()-12);
          Serial.println(counter);
          Serial.println("playerHeadlightBeamViewCurrentY: " + playerHeadlightBeamViewCurrentY);
          data = "";
          counter +=1;
      }
      else if (counter == 2){
          randomHeadlightBeamViewCurrentX = data.substring(0,data.length()-12);
          Serial.println(counter);
          Serial.println("randomHeadlightBeamViewCurrentX: " + randomHeadlightBeamViewCurrentX);
          data = "";
          counter +=1;
      }
      else if (counter == 3){
          randomHeadlightBeamViewCurrentY = data.substring(0,data.length()-12);
          Serial.println(counter);
          Serial.println("randomHeadlightBeamViewCurrentY: " + randomHeadlightBeamViewCurrentY);
          Serial.println("");
          data = "";
          counter = 0;
      }
   }
    /*if (data.equals(ON)){
      digitalWrite(LED_BUILTIN, HIGH);
      data = "";
    }

    else if (data.equals(OFF)){
      digitalWrite(LED_BUILTIN, LOW);
      data = "";
    }*/
  }
  //delay(400);
}
