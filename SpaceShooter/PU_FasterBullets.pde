class PU_FasterBullets extends PowerUp{
  public void spray(boolean isPlayer2){
    if(!isPlayer2)
      player.weapon.fireRate = 0.03;
    else
      player2.weapon.fireRate = 0.03;
  }
  void activate(boolean player2) {
    super.activate(player2);

    spray(player2);
  }
  void deactivate(boolean isPlayer2){
    super.deactivate(isPlayer2);

    if(!isPlayer2)
      player.weapon.fireRate = 0.3;
    else
      player2.weapon.fireRate = 0.3;
  }

  void Message(){
    DrawText(32, width/2, height/2, "You Shpot Faster");
  }
}
