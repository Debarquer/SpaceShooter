
PImage mainMenuImage;
PImage highscoreMultiplayImage;
PImage submitImage;


ButtonRect mainMenuButton;
ButtonRect highscoreMultiplayButton;
ButtonRect submitButton;
ButtonRect textAreaA;
ButtonRect textAreaB;

String nameA = "";
String nameB = "";

float topBoundary = 100;
float bottomBoundary = 400;
float highscoreAnim = bottomBoundary;
float scoreMargin = 50;

void DrawHighscore(){
  background(155, 155, 155);

  float xOffset = 200;
  mainMenuImage = loadImage("Resources/MenuButton.png");
  image(mainMenuImage, xOffset + width/2 - mainMenuImage.width/2, 700);
  mainMenuButton = new ButtonRect(xOffset + width/2 - mainMenuImage.width/2, xOffset+width/2 + mainMenuImage.width/2, 700, 700+mainMenuImage.height);

  xOffset = -200;
  highscoreMultiplayImage = loadImage("Resources/ButtonMultiplay.png");
  image(highscoreMultiplayImage, xOffset + width/2 - highscoreMultiplayImage.width/2, 700);
  highscoreMultiplayButton = new ButtonRect(xOffset + width/2 - highscoreMultiplayImage.width/2, xOffset+width/2 + highscoreMultiplayImage.width/2, 700, 700+highscoreMultiplayImage.height);

  if(gameState == gameState.GameOver){
    xOffset = 450;
    submitImage = loadImage("Resources/SubmitButton.png");
    image(submitImage, xOffset + width/2 - submitImage.width/2, 567, 100, 50);
    submitButton = new ButtonRect(xOffset + width/2 - submitImage.width/2, xOffset+width/2 + submitImage.width/2, 567, 567+submitImage.height);

    float tXA = 348;
    float tXB = 250;
    float tYA = 565;
    float tYB = 50;
    fill(255, 255, 255);
    rect(tXA, tYA, tXB, tYB);
    textAreaA = new ButtonRect(tXA, tXA+tXB, tYA, tYA+tYB);
    DrawText(32, tXA, tYA+25+(textAscent() + textDescent())/4, nameA);

    fill(255, 255, 255);
    tXA *= 2;
    rect(tXA, tYA, tXB, tYB);
    textAreaB = new ButtonRect(tXA, tXA+tXB, tYA, tYA+tYB);
    DrawText(32, tXA, tYA+25+(textAscent() + textDescent())/4, nameB);
  }

  //print(nameA + ":" + nameB + ":" + inputTextA + ":" + inputTextB + "\n");

  String s = "";
  if(!multiplaying)
    s = "Highscores";
  else
    s = "2P Highscores";
  DrawText(32, width/2 - 100, 30, s);

  float textHeight = textAscent() + textDescent();

  boolean drewAnything = false;
  String[] sa = loadHighscore();

  float stringHeight = sa.length * textHeight;

  for(int i = 0; i < sa.length; i++){
    if(topBoundary + i * textHeight + highscoreAnim > topBoundary+25 && i * textHeight + highscoreAnim < bottomBoundary){
        drewAnything = true;
        DrawText(32, width/2 - 100, topBoundary + i * textHeight + highscoreAnim, sa[i]);
    }
  }

  line(0, topBoundary, width, topBoundary);
  line(0, bottomBoundary+100, width, bottomBoundary+100);

  if(highscoreAnim == -stringHeight)
    highscoreAnim = bottomBoundary;
  else
    highscoreAnim--;
}

void saveHighscore(){
  if(!multiplaying){
    String[] highscores = loadStrings("data/highscores.txt");
    String[] tmp = new String[highscores.length + 1];
    for(int i = 0; i < highscores.length; i++){

      tmp[i] = highscores[i];
    }
    tmp[tmp.length - 1] = nameA+" "+(int)score;

    saveStrings(dataPath("highscores.txt"), tmp);
  }
  else{
    String[] highscores = loadStrings("data/highscores2.txt");
    String[] tmp = new String[highscores.length + 1];
    for(int i = 0; i < highscores.length; i++){

      tmp[i] = highscores[i];
    }
    tmp[tmp.length - 1] = nameA+" "+nameB+" "+(int)score;

    saveStrings(dataPath("highscores2.txt"), tmp);
  }

}

String[] loadHighscore(){
  // String[] highscores = loadStrings("data/highscores.txt");
  //
  // String s = "";
  // for(String string : highscores){
  //   s += string + "\n";
  // }
  // return s;

  if(!multiplaying)
    return loadStrings("data/highscores.txt");
  else
    return loadStrings("data/highscores2.txt");
}
