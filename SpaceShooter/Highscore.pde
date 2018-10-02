
PImage mainMenuImage;

ButtonRect mainMenuButton;

void DrawHighscore(){
  background(155, 155, 155);

  String s = "Highscore";
  DrawText(32, width/2 - 100, 30, s);

  s = loadHighscore();
  DrawText(32, width/2 - 100, 130, s);

  mainMenuImage = loadImage("Resources/MenuButton.png");
  image(mainMenuImage, width/2 - mainMenuImage.width/2, 600);
  mainMenuButton = new ButtonRect(width/2 - mainMenuImage.width/2, width/2 + mainMenuImage.width/2, 600, 600+mainMenuImage.height);
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

String loadHighscore(){
  String[] highscores = loadStrings("data/highscores.txt");

  String s = "";
  for(String string : highscores){
    s += string + "\n";
  }
  return s;
}
