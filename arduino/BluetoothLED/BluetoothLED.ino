//#include <DMXSerial.h>
#include <SoftwareSerial.h>
#include <ArduinoJson.h>

#define BTSerial Serial1
#define BAUD 115200
#define JSON_BUFFER 200

// JSON Buffer + Counter + Flag
char JSONObject[JSON_BUFFER] = {0};
int JSONCounter = 0;
bool JSONObjectIsFullyRead = false;

// Resetted Pan and Tilt values when game is finished
const int resetPan = 42;
const int resetTilt = 130;

// velocity for MH engines in-game
const int velocity = 12;

// PlayerMH DMX Variables
int playerPan;
int playerTilt;
int playerDimmer = 80;
int playerShutter = 30;
int playerFarbe = 150;
int playerIris = 200;
int playerFokus = 0;

// randomBlueMH DMX Variables
int randomBluePan;
int randomBlueTilt;
int randomBlueDimmer = 80;
int randomBlueShutter = 30;
int randomBlueFarbe = 160;
int randomBlueIris = 200;
int randomBlueFokus = 10;

// randomYellowMH DMX Variables
int randomYellowPan;
int randomYellowTilt;
int randomYellowDimmer = 80;
int randomYellowShutter = 30;
int randomYellowFarbe = 100;
int randomYellowIris = 200;
int randomYellowFokus = 10;

// randomRedMH DMX Variables
int randomRedPan;
int randomRedTilt;
int randomRedDimmer = 39;
int randomRedShutter = 30;
int randomRedFarbe = 60;
int randomRedIris = 200;
int randomRedFokus = 10;

void setup() {
  Serial.begin(BAUD);
  BTSerial.begin(BAUD);

  //Register all MHs at Arduino Port 2
  pinMode(2, OUTPUT);  
  digitalWrite(2, HIGH);                  //We need high Level (5V) for the MHs.
  
  initializeMHs();
}

void loop() {
  //Read incoming BT Data:
    readBTSerial();

  //When Reading is done:
    if (JSONObjectIsFullyRead) {
      //Serial.println(JSONObject);
      
      //evaluate JSON String for pan and tilt values
        StaticJsonDocument<JSON_BUFFER> doc;
        if (parseJSONObject(&doc, JSONObject)) {
          evaluateJSONObject(doc);
        //if no pan and tilt values are detected then check for collision of Headlights
          //onCollision(doc);
        }

      //empty JSON Buffer
        emptyJSONBuffer();
    }
}

// Fill Buffer with incoming BT Data
void readBTSerial() {
  if (BTSerial.available()) {
    char data = BTSerial.read();
    
    // Add Characters to Buffer
    JSONObject[JSONCounter] = data;
    JSONCounter++;
    
    // Check for ending of an JSONObject
    JSONObjectIsFullyRead = data == '}';
  }
}

// JSONObject Parser
bool parseJSONObject(JsonDocument *doc, char buff[]) {
  //Check for deserialization errors
  DeserializationError error = deserializeJson(*doc, buff);
    
  // Test if parsing succeeds and reset buffer if failed
  if (!error){
    return true;
  }
  else{
    //Serial.print("deserializeJson() failed!");
    emptyJSONBuffer();
    return false;
  }
}

// Evaluates incoming JSONObject
void evaluateJSONObject(JsonDocument doc) {
  if (doc["C"]){
    //int value = doc["C"];
    //Serial.println(value);
    onCollision(doc);
  }
  if (doc["1"]){
    String value = doc["1"];
    playerPan = value.toInt();
  }
  if (doc["3"]){
    String value = doc["3"];
    playerTilt = value.toInt();
  }
  if (doc["26"]){
    String value = doc["26"];
    randomBluePan = value.toInt();
  }
  if (doc["28"]){
    String value = doc["28"];
    randomBlueTilt = value.toInt();
  }
  if (doc["51"]){
    String value = doc["51"];
    randomYellowPan = value.toInt();
  }
  if (doc["53"]){
    String value = doc["53"];
    randomYellowTilt = value.toInt();
  }
  if (doc["76"]){
    String value = doc["76"];
    randomRedPan = value.toInt();
  }
  if (doc["78"]){
    String value = doc["78"];
    randomRedTilt = value.toInt();
    sendPanAndTiltValuesToMHs();
  }

  /*Serial.print("PlayerPan: ");
  Serial.println(playerPan);
  Serial.print("PlayerTilt: ");
  Serial.println(playerTilt);
  Serial.print("randomBluePan: ");
  Serial.println(randomBluePan);
  Serial.print("randomBlueTilt: ");
  Serial.println(randomBlueTilt);
  Serial.print("randomYellowPan: ");
  Serial.println(randomYellowPan);
  Serial.print("randomYellowTilt: ");
  Serial.println(randomYellowTilt);
  Serial.print("randomRedPan: ");
  Serial.println(randomRedPan);
  Serial.print("randomRedTilt: ");
  Serial.println(randomRedTilt);
  */
}

void onCollision(JsonDocument doc){
  if (doc["32"]){
    Serial.println("BLUE");
    //DMXSerial.write(32, doc["32"]);
  }
  if (doc["57"]){
    Serial.println("YELLOW");
    //DMXSerial.write(57, doc["57"]);
  }
  if (doc["82"]){
    Serial.println("RED");
    //DMXSerial.write(82, doc["82"]);
  }
}

// Sends Pan and Tilt values to all MHs
void sendPanAndTiltValuesToMHs(){
  /*DMXSerial.write(1, playerPan);          //Pan
  DMXSerial.write(3, playerTilt);           //Tilt
  
  DMXSerial.write(26, randomBluePan);       //Pan
  DMXSerial.write(28, randomBlueTilt);      //Tilt

  DMXSerial.write(51, randomYellowPan);     //Pan
  DMXSerial.write(53, randomYellowTilt);    //Tilt

  DMXSerial.write(76, randomRedPan);        //Pan
  DMXSerial.write(78, randomRedTilt);       //Tilt
  */
  
}

// Initializes MHs values that rarely or never change
void initializeMHs(){
  /*DMXSerial.init(DMXController);
  DMXSerial.write(110, 45);

  DMXSerial.write(5, velocity);             //Motorgeschwindigkeit
  DMXSerial.write(7, playerDimmer);         //Dimmer
  DMXSerial.write(8, playerShutter);        //Shutter
  DMXSerial.write(9, playerFarbe);          //Farbe

  DMXSerial.write(30, velocity);            //Motorgeschwindigkeit
  DMXSerial.write(32, randomBlueDimmer);    //Dimmer
  DMXSerial.write(33, randomBlueShutter);   //Shutter
  DMXSerial.write(34, randomBlueFarbe);     //Farbe

  DMXSerial.write(55, velocity);            //Motorgeschwindigkeit
  DMXSerial.write(57, randomYellowDimmer);  //Dimmer
  DMXSerial.write(58, randomYellowShutter); //Shutter
  DMXSerial.write(59, randomYellowFarbe);   //Farbe
  
  DMXSerial.write(80, velocity);            //Motorgeschwindigkeit
  DMXSerial.write(82, randomRedDimmer);     //Dimmer
  DMXSerial.write(83, randomRedDimmer);     //Shutter
  DMXSerial.write(84, randomRedFarbe);      //Farbe
  */
  
}

void resetMHsDimmer(){
  //Serial.println("done");
  /*DMXSerial.write(7, playerDimmer);         //Dimmer
  DMXSerial.write(32, randomBlueDimmer);    //Dimmer
  DMXSerial.write(57, randomYellowDimmer);  //Dimmer
  DMXSerial.write(82, randomRedDimmer);     //Dimmer
  */
}

// Is called when the game is finished and the user returns to menu
void stopGame(){
  /*
  DMXSerial.write(1, resetPan);
  DMXSerial.write(3, resetTilt);

  DMXSerial.write(26, resetPan);
  DMXSerial.write(28, resetTilt);

  DMXSerial.write(51, resetPan);
  DMXSerial.write(53, resetTilt);

  DMXSerial.write(76, resetPan);
  DMXSerial.write(78, resetTilt);
  */
}

// Empties JSON Buffer
void emptyJSONBuffer() {
  for (int i = 0; i < JSONCounter; i++) {
    JSONObject[i] = 0;
  }

  JSONCounter = 0;
  // Flag reset
  JSONObjectIsFullyRead = false;
}
