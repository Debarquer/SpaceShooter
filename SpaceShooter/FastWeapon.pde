class FastWeapon extends Weapon{
  float damage;
  float fireRate;

  public FastWeapon(){
    fireRate = 0.01;
  }

  public FastWeapon(float damage, float fireRate){
    this.damage = damage;
    this.fireRate = fireRate;
  }
}
