class PU_MoveFaster extends PowerUp{

  PVector bulletCol = new PVector(66, 66, 255);

  float oldSpeed;
  float newSpeed;
  public PU_MoveFaster(){
    super();
    oldSpeed = player.vel.y;
    newSpeed = player.vel.y * 2;
  }

void activate(boolean isPlayer2){
super.activate(isPlayer2);

if(!isPlayer2){
  player.weapon.fireRate = 0.03;
  player.vel.y = newSpeed;
}
else{
  player2.weapon.fireRate = 0.3;
  player2.vel.y = newSpeed;
}

}

public void deactivate(boolean isPlayer2){
  super.deactivate();

  if(!isPlayer2){
    player.weapon.fireRate = 0.3;
    player.vel.y = oldSpeed;
  }
  else{
    player2.weapon.fireRate = 0.3;
    player2.vel.y = oldSpeed;
  }
}

void Message(){
  DrawText(32, width/2, height/2, "You Move Faster");
}
}
