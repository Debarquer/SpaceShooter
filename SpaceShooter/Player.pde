class Player extends GameObject{
  PVector size;

  float shootTimerMax = 0.5;
  float shootTimerCurr = 0;
  boolean canFire = true;

  Weapon weapon;

  float powerupTimerMax = 3;
  float powerupTimerCurr;

  boolean hasPowerup = false;

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

      if(shootTimerCurr >= weapon.fireRate){
        shootTimerCurr = 0;
        canFire = true;
      }
    }

    powerup.keepDrawing();

    //print("Timer: " + powerupTimerCurr + "\n");
    powerupTimerCurr+=(float)1/60;
    if(powerupTimerCurr >= powerupTimerMax){
      //powerupTimerCurr = 0;

      if(hasPowerup){

        //powerup.deactivate();
        powerup = powerUps.get((int)random(powerUps.size()));
        powerup.deactivate();
        hasPowerup = false;
        powerup.hasGeneratedGoal = false;
      }

      powerup.update();
    }
    else{
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

  void receivePowerup(){
    hasPowerup = true;
    powerupTimerCurr = 0;
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
    weapon.Shoot(this);
  }
}
