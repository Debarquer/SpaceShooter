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

ButtonRect buttonA;

void DrawMainMenu(){
  background(155, 155, 155);
  PFont f;
  f = createFont("Arial", 32, true);
  textFont(f, 32);
  fill(0, 0, 0);

  String s = "Space Shooter";
  text(s, width/2 - 100, 30);

  PImage img;
  img = loadImage("Resources/PlayButton.png");
  image(img, width/2 - img.width/2, 200);
  buttonA = new ButtonRect(width/2 - img.width/2, width/2 + img.width/2, 200, 200+img.height);
}

void mouseReleased(){
  //print(buttonA.Clicked(mouseX, mouseY));

  if(buttonA.Clicked(mouseX, mouseY)){
    gameState = GameState.Playing;
  }
}
