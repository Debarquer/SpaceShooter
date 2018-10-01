class Bullet extends GameObject{

  boolean playerBullet;

  public Bullet(){
    super();
  }

  public Bullet(PVector pos, PVector vel, PVector a, PVector colStroke, PVector colFill, float r, float health, boolean playerBullet){
    super(pos, vel, a, colStroke, colFill, r, health);

    this.playerBullet = playerBullet;
  }

  public void Update(){
    super.Update();

    if(pos.x > width){
      enabled = false;
    }

    ellipseMode(RADIUS);
    ellipse(pos.x, pos.y, r, r);
  }

  public void Move(){
    super.Move();

    pos.x += vel.x;
  }
}
