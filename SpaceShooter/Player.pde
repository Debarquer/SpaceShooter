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
}
