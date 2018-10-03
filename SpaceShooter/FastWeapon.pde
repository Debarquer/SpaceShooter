class FastWeapon extends Weapon{
  float damage;
  float fireRate;

  PVector bulletCol = new PVector(0, 255, 255);

  public FastWeapon(){
    fireRate = 0.01;
    damage = 1;
  }

  public FastWeapon(float damage, float fireRate){

    print("Instantiated new fast weapon\n");

    this.damage = damage;
    this.fireRate = fireRate;
  }
}
