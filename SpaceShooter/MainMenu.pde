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
PImage exitImage;

ButtonRect playButton;
ButtonRect exitButton;

void DrawMainMenu(){
  background(155, 155, 155);
  PFont f;
  f = createFont("Arial", 32, true);
  textFont(f, 32);
  fill(0, 0, 0);

  String s = "Space Shooter";
  text(s, width/2 - 100, 30);

  playImage = loadImage("Resources/PlayButton.png");
  image(playImage, width/2 - playImage.width/2, 200);
  playButton = new ButtonRect(width/2 - playImage.width/2, width/2 + playImage.width/2, 200, 200+playImage.height);

  exitImage = loadImage("Resources/ExitButton.png");
  image(exitImage, width/2 - exitImage.width/2, 400);
  exitButton = new ButtonRect(width/2 - exitImage.width/2, width/2 + exitImage.width/2, 400, 400+exitImage.height);
}

void mouseReleased(){
  //print(buttonA.Clicked(mouseX, mouseY));

  if(playButton.Clicked(mouseX, mouseY)){
    gameState = GameState.Playing;
  }
  if(exitButton.Clicked(mouseX, mouseY)){
    exit();
  }
}
