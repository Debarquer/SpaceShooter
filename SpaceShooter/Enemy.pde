class Enemy extends GameObject{

  float angle, amplitude;
  boolean sine = true;

  float shootTimerMax = 1;
  float shootTimerCurr = 0;
  boolean canFire = true;

  float baseSpeed = 5;
  float baseHealth = 1;

  public Enemy(){
    super();
  }

  public Enemy(float level, float angle, PVector pos, float r){
    this.pos = new PVector();
    this.pos.x = pos.x;
    this.pos.y = pos.y;
    this.pos.z = pos.z;

    this.vel = new PVector();
    this.vel.x = baseSpeed + level;
    this.vel.y = baseSpeed + level;
    this.vel.z = baseSpeed + level;

    this.a = new PVector();
    this.a.x = a.x;
    this.a.y = a.y;
    this.a.z = a.z;

    this.colStroke = new PVector();
    this.colStroke.x = 0;
    this.colStroke.y = 0;
    this.colStroke.z = 0;

    this.colFill = new PVector();
    this.colFill.x = 255;
    this.colFill.y = 0;
    this.colFill.z = 0;

    this.r = r;
    this.currHealth = this.health = baseHealth + level;

    enabled = true;

    this.angle = angle;
    amplitude = 100;
  }

  public Enemy(PVector pos, PVector vel, PVector a, PVector colStroke, PVector colFill, float r, float health, float angle){
    super(pos, vel, a, colStroke, colFill, r, health);

    this.angle = angle;
    amplitude = 100;
  }

  public void Update(){
    super.Update();

    if(!canFire){
      //print(shootTimerCurr + "\n");
      shootTimerCurr += (float)1/60;
      if(shootTimerCurr >= shootTimerMax){
        shootTimerCurr = 0;
        canFire = true;
      }
    }
    else{
      Shoot();
      canFire = false;
    }

    angle += (float)1/60;

    if(pos.y > height){
      enabled = false;
    }
    if(pos.x < -r){
      //sine = !sine;
      score -= scoreIncrement;
      //pos.x = width;
      enabled = false;
    }

    if(sine)
      pos.y = height/2 + sin(angle) * amplitude;
    else
      pos.y = height/2 + cos(angle) * amplitude;

    fill(colFill.x, colFill.y, colFill.z);
    ellipseMode(RADIUS);
    ellipse(pos.x, pos.y, r, r);
    //ellipse(pos.x, pos.y, currHealth/this.health*r, currHealth/this.health*r);

    PFont f;
    f = createFont("Arial", 16, true);
    textFont(f, 16);
    fill(0, 0, 0);

    String s = ""+(int)this.currHealth;
    text(s, pos.x, pos.y);
  }

  public void Shoot(){
    float rBullet = 5;
    PVector posBullet = new PVector(pos.x - this.r, pos.y - this.r/2 + rBullet/2, pos.z);
    PVector velBullet = new PVector(-10, -10, -10);
    PVector aBullet = new PVector(0, 0, 0);
    PVector colStrokeBullet = new PVector(0, 0, 0);
    PVector colFillBullet = new PVector(255, 0, 0);
    float healthBullet = 1;

    Bullet bullet = new Bullet(posBullet, velBullet, aBullet, colStrokeBullet, colFillBullet, rBullet, healthBullet, false, 1);
    bullets.add(bullet);
  }

  public void Move(){
    super.Move();

    pos.x -= vel.x;
    amplitude = 400;

    if(sine)
      pos.y = height/2 + sin(angle) * amplitude;
    else
      pos.y = height/2 + cos(angle) * amplitude;
  }

  public boolean TakeDamage(float damage){
    currHealth -= damage;
    if(currHealth <= 0){
      return true;
    }
    else{
      colFill = new PVector(currHealth/this.health * 255, (1/(currHealth/this.health)) * 255, 0);
      return false;
    }
  }
}
