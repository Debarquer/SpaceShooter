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

  public void Shoot(Player p){
    float rBullet = 5;
    PVector posBullet = new PVector(p.pos.x + p.size.x, p.pos.y + p.size.y/2 - p.r/2, p.pos.z);
    PVector velBullet = new PVector(10, 10, 10);
    PVector aBullet = new PVector(0, 0, 0);
    PVector colStrokeBullet = new PVector(0, 0, 0);
    PVector colFillBullet = new PVector(0, 255, 0);
    float healthBullet = 1;

    Bullet bullet = new Bullet(posBullet, velBullet, aBullet, colStrokeBullet, colFillBullet, rBullet, healthBullet, true, p.weapon.damage);
    bullets.add(bullet);
  }
}
