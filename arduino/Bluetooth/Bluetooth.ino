#include <DMXSerial.h>
//#include <SoftwareSerial.h>
#include <ArduinoJson.h>

#define BTSerial Serial1
#define BAUD 115200
#define JSON_BUFFER 200

// JSON Buffer + Counter + Flag
char JSONObject[JSON_BUFFER] = {0};
int JSONCounter = 0;
bool JSONObjectIsFullyRead = false;

// Resetted pan and tilt values when game is finished
const int resetPan = 42;
const int resetTilt = 127;

// Initial and reset dimmer value when game is finished
const int dimmer = 100;
// Initial shutter value for every MH
const int shutter = 30;
// Velocity for MH engines in-game
const int velocity = 12;

// PlayerMH DMX variables
int playerPan;
int playerPanTuning;
int playerTilt;
int playerTiltTuning;
int playerColour = 160;
int playerGoboMode = 128;
int playerGoboEffect = 200;
int playerIris = 130;
int playerFocus = 50;

// randomGreenMH DMX variables
int randomGreenPan;
int randomGreenPanTuning;
int randomGreenTilt;
int randomGreenTiltTuning;
int randomGreenColour = 150;
int randomGreenGoboMode = 100;
int randomGreenGoboEffect = 130;
int randomGreenGoboVelocity = 57;
int randomGreenIris = 165;
int randomGreenFocus = 50;

// randomYellowMH DMX variables
int randomYellowPan;
int randomYellowPanTuning;
int randomYellowTilt;
int randomYellowTiltTuning;
int randomYellowColour = 100;
int randomYellowGoboMode = 100;
int randomYellowGoboEffect = 130;
int randomYellowGoboVelocity = 57;
int randomYellowIris = 165;
int randomYellowFocus = 50;

// randomRedMH DMX variables
int randomRedPan;
int randomRedPanTuning;
int randomRedTilt;
int randomRedTiltTuning;
int randomRedColour = 60;
int randomRedGoboMode = 100;
int randomRedGoboEffect = 130;
int randomRedGoboVelocity = 10;
int randomRedIris = 165;
int randomRedFocus = 50;

void setup() {
  //Serial.begin(BAUD);
  BTSerial.begin(BAUD);

  // Register all MHs at Arduino Port 2
  pinMode(2, OUTPUT);  
  digitalWrite(2, HIGH);                  //We need high Level (5V) for the MHs.
  
  initializeMHs();
}

void loop() {
  // Read incoming BT Data:
    readBTSerial();

  // When Reading is done:
    if (JSONObjectIsFullyRead) {
      //Serial.println(JSONObject);
      
      // evaluate JSON String
        StaticJsonDocument<JSON_BUFFER> doc;
        if (parseJSONObject(&doc, JSONObject)) {
          evaluateJSONObject(doc);
        }

      // empty JSON Buffer
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

 // check for special data
  // "B" represents Brightness - these JSONObjects take care of each MH's dimmer value after collisions
  if (doc["B"]){
    //int value = doc["B"];
    //Serial.println(value);
    onCollision(doc);
  }

  // "R" represents Reset - this JSONObject is only sent once after the game has finished in order to reset each MH's pan and tilt values
  if (doc["R"]){
    stopGame();
  }

 // Pan and Tilt Values for Player MH
  if (doc["1"]){
    String value = doc["1"];
    playerPan = value.toInt();
  }
  if (doc["2"]){
    String value = doc["2"];
    playerPanTuning = value.toInt();
  }
  if (doc["3"]){
    String value = doc["3"];
    playerTilt = value.toInt();
  }
  if (doc["4"]){
    String value = doc["4"];
    playerTiltTuning = value.toInt();
  }

  // Pan and Tilt Values for randomGreen MH
  if (doc["26"]){
    String value = doc["26"];
    randomGreenPan = value.toInt();
  }
  if (doc["27"]){
    String value = doc["27"];
    randomGreenPanTuning = value.toInt();
  }
  if (doc["28"]){
    String value = doc["28"];
    randomGreenTilt = value.toInt();
  }
  if (doc["29"]){
    String value = doc["29"];
    randomGreenTiltTuning = value.toInt();
  }

  // Pan and Tilt Values for randomYellow MH
  if (doc["51"]){
    String value = doc["51"];
    randomYellowPan = value.toInt();
  }
  if (doc["52"]){
    String value = doc["52"];
    randomYellowPanTuning = value.toInt();
  }
  if (doc["53"]){
    String value = doc["53"];
    randomYellowTilt = value.toInt();
  }
  if (doc["54"]){
    String value = doc["54"];
    randomYellowTiltTuning = value.toInt();
  }

  // Pan and Tilt Values for randomRed MH
  if (doc["76"]){
    String value = doc["76"];
    randomRedPan = value.toInt();
  }
  if (doc["77"]){
    String value = doc["77"];
    randomRedPanTuning = value.toInt();
  }
  if (doc["78"]){
    String value = doc["78"];
    randomRedTilt = value.toInt();
  }
  if (doc["79"]){
    String value = doc["79"];
    randomRedTiltTuning = value.toInt();
    setPanAndTiltValues();
  }
}

void onCollision(JsonDocument doc){
  if (doc["32"]){
    //Serial.println("GREEN");
    DMXSerial.write(32, doc["32"]);
  }
  if (doc["57"]){
    //Serial.println("YELLOW");
    DMXSerial.write(57, doc["57"]);
  }
  if (doc["82"]){
    //Serial.println("RED");
    DMXSerial.write(82, doc["82"]);
  }
}

// Sets Pan and Tilt values for all MHs
void setPanAndTiltValues(){
  
  DMXSerial.write(1, playerPan);              // Pan
  DMXSerial.write(2, playerPanTuning);        // Pan tuning
  DMXSerial.write(3, playerTilt);             // Tilt
  DMXSerial.write(4, playerTiltTuning);       // Tilt tuning

  DMXSerial.write(26, randomGreenPan);        // Pan
  DMXSerial.write(27, randomGreenPanTuning);  // Pan tuning
  DMXSerial.write(28, randomGreenTilt);       // Tilt
  DMXSerial.write(29, randomGreenTiltTuning); // Tilt tuning

  DMXSerial.write(51, randomYellowPan);       // Pan
  DMXSerial.write(52, randomYellowPanTuning); // Pan tuning
  DMXSerial.write(53, randomYellowTilt);      // Tilt
  DMXSerial.write(54, randomYellowTiltTuning);// Tilt tuning

  DMXSerial.write(76, randomRedPan);          // Pan
  DMXSerial.write(77, randomRedPanTuning);    // Pan tuning
  DMXSerial.write(78, randomRedTilt);         // Tilt
  DMXSerial.write(79, randomRedTiltTuning);   // Tilt tuning

}

// Initializes MHs values that rarely or never change
void initializeMHs(){
  
  DMXSerial.init(DMXController);
  DMXSerial.write(110, 45);

  DMXSerial.write(5, velocity);               // Engine velocity
  DMXSerial.write(7, dimmer);                 // Dimmer
  DMXSerial.write(8, shutter);                // Shutter
  DMXSerial.write(9, playerColour);           // Colour
  DMXSerial.write(12, playerGoboMode);        // Gobo Modus
  DMXSerial.write(16, playerGoboEffect);      // Gobo Effect

  DMXSerial.write(30, velocity);              // Engine velocity
  DMXSerial.write(32, dimmer);                // Dimmer
  DMXSerial.write(33, shutter);               // Shutter
  DMXSerial.write(34, randomGreenColour);     // Colour
  DMXSerial.write(36, randomGreenGoboEffect);
  DMXSerial.write(37, randomGreenGoboMode);
  DMXSerial.write(38, randomGreenGoboVelocity);

  DMXSerial.write(55, velocity);              // Engine velocity
  DMXSerial.write(57, dimmer);                // Dimmer
  DMXSerial.write(58, shutter);               // Shutter
  DMXSerial.write(59, randomYellowColour);    // Colour
  DMXSerial.write(61, randomYellowGoboEffect);
  DMXSerial.write(62, randomYellowGoboMode);
  DMXSerial.write(63, randomYellowGoboVelocity);

  DMXSerial.write(80, velocity);              // Engine velocity
  DMXSerial.write(82, dimmer);                // Dimmer
  DMXSerial.write(83, shutter);               // Shutter
  DMXSerial.write(84, randomRedColour);       // Colour
  DMXSerial.write(86, randomRedGoboEffect);
  DMXSerial.write(87, randomRedGoboMode);
  DMXSerial.write(88, randomRedGoboVelocity);
  
}

// Is called when the game is finished and the user returns to menu
void stopGame(){
  
  DMXSerial.write(1, resetPan);
  DMXSerial.write(3, resetTilt);
  DMXSerial.write(7, dimmer);

  DMXSerial.write(26, resetPan);
  DMXSerial.write(28, resetTilt);
  DMXSerial.write(32, dimmer);

  DMXSerial.write(51, resetPan);
  DMXSerial.write(53, resetTilt);
  DMXSerial.write(57, dimmer);

  DMXSerial.write(76, resetPan);
  DMXSerial.write(78, resetTilt);
  DMXSerial.write(82, dimmer);
  
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
