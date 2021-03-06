class Bullet extends GameObject{

  boolean playerBullet;
  float damage;
  boolean player2 = false;

  public Bullet(){
    super();
  }

  public Bullet(PVector pos, PVector vel, PVector a, PVector colStroke, PVector colFill, float r, float health, boolean playerBullet, float damage){
    super(pos, vel, a, colStroke, colFill, r, health);

    this.playerBullet = playerBullet;

    this.damage = damage;
  }

  public void Update(){
    super.Update();

    if(pos.x > width){
      enabled = false;
    }

    //fill(colFill.x, colFill.y, colFill.z);
    fill(255);
    stroke(colStroke.x,colStroke.y, colStroke.z);

    ellipseMode(RADIUS);
    ellipse(pos.x, pos.y, r, r);
  }

  public void Move(){
    super.Move();

    pos.x += vel.x;
  }
}
