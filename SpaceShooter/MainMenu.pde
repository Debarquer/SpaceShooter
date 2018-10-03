class ButtonRect{
  float minX, maxX, minY, maxY;

  public ButtonRect(float minX, float maxX, float minY, float maxY){
    this.minX = minX;
    this.maxX = maxX;
    this.minY = minY;
    this.maxY = maxY;
  }

  public boolean Clicked(float x, float y){
    return x > minX && x < maxX && y > minY && y < maxY;
  }
}

PImage playImage;
PImage highscoreImage;
PImage exitImage;

ButtonRect playButton;
ButtonRect multiplayButton;
ButtonRect highscoreButton;
ButtonRect exitButton;

void DrawMainMenu(){
  background(155, 155, 155);

  String s = "Space Shooter";
  DrawText(32, width/2 - 100, 30, s);

  playImage = loadImage("Resources/PlayButton.png");
  image(playImage, width/2 - playImage.width/2, 200);
  playButton = new ButtonRect(width/2 - playImage.width/2, width/2 + playImage.width/2, 200, 200+playImage.height);

  highscoreImage = loadImage("Resources/HighscoreButton.png");
  image(highscoreImage, width/2 - highscoreImage.width/2, 400);
  highscoreButton = new ButtonRect(width/2 - highscoreImage.width/2, width/2 + highscoreImage.width/2, 400, 400+highscoreImage.height);

  exitImage = loadImage("Resources/ExitButton.png");
  image(exitImage, width/2 - exitImage.width/2, 600);
  exitButton = new ButtonRect(width/2 - exitImage.width/2, width/2 + exitImage.width/2, 600, 600+exitImage.height);
}
