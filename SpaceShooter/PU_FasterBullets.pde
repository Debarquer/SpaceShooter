class PU_FasterBullets extends PowerUp{
  public void spray(){
    player.weapon.fireRate = 0.03;
  }
  void activate() {
    super.activate();

    spray();
  }
  void deactivate(){
    super.deactivate();
    player.weapon.fireRate = 0.3;
  }

  void Message(){
    DrawText(32, width/2, height/2, "You Shpot Faster");
  }
}
