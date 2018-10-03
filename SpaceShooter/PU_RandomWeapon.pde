class PU_RandomWeapon extends PowerUp{

  ArrayList<Weapon> weapons;

  public PU_RandomWeapon(){
    super();

    weapons = new ArrayList<Weapon>();
    weapons.add(new FastWeapon());

    colFill = new PVector(50, 255, 50);
  }

  public void activate(){
    super.activate();
    //print("Enjoy your new weapon \n");
    //player.weapon = new FastWeapon(1, 0.01);
    player.weapon = weapons.get((int)random(weapons.size()));

    //player.weapon.fireRate = 0.01;
  }

  public void deactivate(){
    //test
    super.deactivate();
    //print("Deactivated power up");

    player.weapon = new Weapon();
    //player.weapon.fireRate = 0.3;
    //RandNum();
  }

  void Message(){
    DrawText(32, width/2, height/2, "You HAve A New Weapon");
  }
}
