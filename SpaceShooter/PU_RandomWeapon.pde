class PU_RandomWeapon extends PowerUp{

  public PU_RandomWeapon(){
    super();

    colFill = new PVector(50, 255, 50);
  }

  public void activate(){

    //print("Enjoy your new weapon \n");
    player.weapon = new FastWeapon(1, 0.01);
    player.receivePowerup();

    player.weapon.fireRate = 0.01;
  }

  public void deactivate(){
  //test
    //print("Deactivated power up");

    //player.weapon = new Weapon();
    player.weapon.fireRate = 0.3;
    //RandNum();
  }
}
