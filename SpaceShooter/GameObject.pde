abstract class GameObject{
  PVector pos, vel, a, colStroke, colFill;
  float r, health;
  boolean enabled;

  public GameObject(){
    this.pos = new PVector();
    this.vel = new PVector();
    this.a = new PVector();
    this.colStroke = new PVector();
    this.colFill = new PVector();

    enabled = true;
  }

  public GameObject(PVector pos, PVector vel, PVector a, PVector colStroke, PVector colFill, float r, float health){
    this.pos = new PVector();
    this.pos.x = pos.x;
    this.pos.y = pos.y;
    this.pos.z = pos.z;

    this.vel = new PVector();
    this.vel.x = vel.x;
    this.vel.y = vel.y;
    this.vel.z = vel.z;

    this.a = new PVector();
    this.a.x = a.x;
    this.a.y = a.y;
    this.a.z = a.z;

    this.colStroke = new PVector();
    this.colStroke.x = colStroke.x;
    this.colStroke.y = colStroke.y;
    this.colStroke.z = colStroke.z;

    this.colFill = new PVector();
    this.colFill.x = colFill.x;
    this.colFill.y = colFill.y;
    this.colFill.z = colFill.z;

    this.r = r;
    this.health = health;

    enabled = true;
  }

  public void Update(){
    stroke(colStroke.x, colStroke.y,colStroke.z);
    fill(colFill.x, colFill.y, colFill.z);
  }

  public void Move(){

  }

  public void Shoot(){

  }
}
