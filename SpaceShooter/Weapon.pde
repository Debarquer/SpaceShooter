class Weapon{
  float damage;
  float fireRate;

  PVector bulletCol = new PVector(255, 255, 0);

  public Weapon(){
    damage = 1;
    fireRate = 0.3;
  }

  public Weapon(float damage, float fireRate){
    this.damage = damage;
    this.fireRate = fireRate;
  }

  public void Shoot(){
    float rBullet = 5;
    PVector posBullet = new PVector(player.pos.x + player.size.x, player.pos.y + player.size.y/2 - player.r/2, player.pos.z);
    PVector velBullet = new PVector(10, 10, 10);
    PVector aBullet = new PVector(0, 0, 0);
    PVector colStrokeBullet = new PVector(0, 0, 0);
    PVector colFillBullet = new PVector(0, 255, 0);
    float healthBullet = 1;

    Bullet bullet = new Bullet(posBullet, velBullet, aBullet, colStrokeBullet, colFillBullet, rBullet, healthBullet, true, player.weapon.damage);
    bullets.add(bullet);
  }
}
