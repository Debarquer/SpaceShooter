class StrongWeapon extends Weapon{
  float damage;
  float fireRate;

  PVector bulletCol = new PVector(0, 0, 255);

  public StrongWeapon(){
    fireRate = 0.3;
    damage = 4;
  }

  public StrongWeapon(float damage, float fireRate){

    super(damage, fireRate);

    fireRate = 0.3;
    damage = 4;

    print("Instantiated new strong weapon\n");
  }

  public void Shoot(Player p, boolean player2){
    float rBullet = 5;
    PVector posBullet = new PVector(p.pos.x + p.size.x, p.pos.y + p.size.y/2 - p.r/2, p.pos.z);
    PVector velBullet = new PVector(10, 10, 10);
    PVector aBullet = new PVector(0, 0, 0);
    PVector colStrokeBullet = new PVector(0, 0, 0);
    PVector colFillBullet = new PVector(0, 255, 0);
    float healthBullet = 1;

    Bullet bullet = new Bullet(posBullet, velBullet, aBullet, colStrokeBullet, colFillBullet, rBullet, healthBullet, true, p.weapon.damage);
    bullet.player2 = player2;
    bullets.add(bullet);
    posBullet = new PVector(p.pos.x + p.size.x, p.pos.y + p.size.y/2 - p.r/2 + 15, p.pos.z);
    bullet = new Bullet(posBullet, velBullet, aBullet, colStrokeBullet, colFillBullet, rBullet, healthBullet, true, p.weapon.damage);
    bullet.player2 = player2;
    bullets.add(bullet);
    posBullet = new PVector(p.pos.x + p.size.x, p.pos.y + p.size.y/2 - p.r/2 - 15, p.pos.z);
    bullet = new Bullet(posBullet, velBullet, aBullet, colStrokeBullet, colFillBullet, rBullet, healthBullet, true, p.weapon.damage);
    bullet.player2 = player2;
    bullets.add(bullet);
  }
}
