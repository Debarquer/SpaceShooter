class PU_RandomWeapon extends PowerUp{

  ArrayList<Weapon> weapons;

  public PU_RandomWeapon(){
    super();

    weapons = new ArrayList<Weapon>();
    weapons.add(new FastWeapon());
    weapons.add(new StrongWeapon());

    colFill = new PVector(50, 255, 50);
  }

  public void activate(boolean isPlayer2){
    super.activate(isPlayer2);
    //print("Enjoy your new weapon \n");
    //player.weapon = new FastWeapon(1, 0.01);
    if(!isPlayer2)
      player.weapon = weapons.get((int)random(weapons.size()));
    else
      player2.weapon = weapons.get((int)random(weapons.size()));

    //player.weapon.fireRate = 0.01;
  }

  public void deactivate(boolean isPlayer2){
    //test
    super.deactivate(isPlayer2);
    //print("Deactivated power up");

    if(!isPlayer2)
      player.weapon = new Weapon();
    else
      player2.weapon = new Weapon();
    //player.weapon.fireRate = 0.3;
    //RandNum();
  }

  void Message(){
    DrawText(32, width/2, height/2, "You HAve A New Weapon: ");
  }
}
