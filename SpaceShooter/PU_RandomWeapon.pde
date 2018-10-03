class PU_RandomWeapon extends PowerUp{

  public PU_RandomWeapon(){
    super();

    colFill = new PVector(50, 255, 50);
  }

  public void activate(){
    print("Enjoy your new weapon \n");
    player.weapon = new FastWeapon();
  }
}
