class Player extends GameObject{
  PVector size;

  float shootTimerMax = 0.5;
  float shootTimerCurr = 0;
  boolean canFire = true;

  Weapon weapon;

  public Player(){
    super();
  }

  public Player(PVector pos, PVector vel, PVector a, PVector colStroke, PVector colFill, float r, float health, PVector size, Weapon weapon){
    super(pos, vel, a, colStroke, colFill, r, health);

    this.size = new PVector();
    this.size.x = size.x;
    this.size.y = size.y;

    this.weapon = weapon;
    shootTimerMax = weapon.fireRate;
  }

  public void Update(){
    if(!canFire){
      //print(shootTimerCurr + "\n");
      shootTimerCurr += (float)1/60;
      if(shootTimerCurr >= shootTimerMax){
        shootTimerCurr = 0;
        canFire = true;
      }
    }

    if(pos.y > height){
      pos.y = 0;
    }
    else if(pos.y < 0){
      pos.y = height;
    }

    fill(colFill.x, colFill.y, colFill.z);
    stroke(colStroke.x,colStroke.y, colStroke.z);
    rect(pos.x, pos.y, size.x, size.y);
  }

  public void Move(){
    super.Move();

    if(moveDown && moveUp){

    }
    else if(moveDown){
      //print("Moving down \n");
      pos.y += vel.y;
    }
    else if(moveUp)
      pos.y -= vel.y;
  }

  void Shoot(){
    float rBullet = 5;
    PVector posBullet = new PVector(pos.x + size.x, pos.y + size.y/2 - r/2, pos.z);
    PVector velBullet = new PVector(10, 10, 10);
    PVector aBullet = new PVector(0, 0, 0);
    PVector colStrokeBullet = new PVector(0, 0, 0);
    PVector colFillBullet = new PVector(0, 255, 0);
    float healthBullet = 1;

    Bullet bullet = new Bullet(posBullet, velBullet, aBullet, colStrokeBullet, colFillBullet, rBullet, healthBullet, true, weapon.damage);
    bullets.add(bullet);
  }
}
