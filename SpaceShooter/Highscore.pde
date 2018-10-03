
PImage mainMenuImage;

ButtonRect mainMenuButton;

float topBoundary = 100;
float bottomBoundary = 400;
float highscoreAnim = bottomBoundary;
float scoreMargin = 50;

void DrawHighscore(){
  background(155, 155, 155);

  mainMenuImage = loadImage("Resources/MenuButton.png");
  image(mainMenuImage, width/2 - mainMenuImage.width/2, 600);
  mainMenuButton = new ButtonRect(width/2 - mainMenuImage.width/2, width/2 + mainMenuImage.width/2, 600, 600+mainMenuImage.height);

  String s = "Highscore";
  DrawText(32, width/2 - 100, 30, s);

  boolean drewAnything = false;
  String[] sa = loadHighscore();
  for(int i = 0; i < sa.length; i++){
    if(topBoundary + i * scoreMargin + highscoreAnim > topBoundary+25 && i * scoreMargin + highscoreAnim < bottomBoundary){
        drewAnything = true;
        DrawText(32, width/2 - 100, topBoundary + i * scoreMargin + highscoreAnim, sa[i]);
    }
  }

  line(0, topBoundary, width, topBoundary);
  line(0, bottomBoundary+100, width, bottomBoundary+100);

  if(highscoreAnim == -bottomBoundary-100)
    highscoreAnim = bottomBoundary;
  else
    highscoreAnim--;
}

void saveHighscore(){
  String[] highscores = loadStrings("data/highscores.txt");
  String[] tmp = new String[highscores.length + 1];
  for(int i = 0; i < highscores.length; i++){

    tmp[i] = highscores[i];
  }
  tmp[tmp.length - 1] = ""+(int)score;

  saveStrings(dataPath("highscores.txt"), tmp);
}

String[] loadHighscore(){
  // String[] highscores = loadStrings("data/highscores.txt");
  //
  // String s = "";
  // for(String string : highscores){
  //   s += string + "\n";
  // }
  // return s;

  return loadStrings("data/highscores.txt");
}
