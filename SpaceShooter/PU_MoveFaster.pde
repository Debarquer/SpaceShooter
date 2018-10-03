class PU_MoveFaster extends PowerUp{

  PVector bulletCol = new PVector(66, 66, 255);

  float oldSpeed;
  float newSpeed;
  public PU_MoveFaster(){
    super();
    oldSpeed = player.vel.y;
    newSpeed = player.vel.y * 2;
  }

void activate(){
super.activate();

player.weapon.fireRate = 0.03;
player.vel.y = newSpeed;

}

public void deactivate(){
  super.deactivate();
  player.weapon.fireRate = 0.3;
  player.vel.y = oldSpeed;
}

void Message(){
  DrawText(32, width/2, height/2, "You Move Faster");
}
}
