class Player extends GameObject{
  PVector size;

  float shootTimerMax = 0.5;
  float shootTimerCurr = 0;
  boolean canFire = true;

  Weapon weapon;

  float powerupTimerMax = 3;
  float powerupTimerCurr;

  boolean hasPowerup = false;
  boolean isPlayer2 = false;

  boolean shield = false;

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
        powerup.deactivate(isPlayer2);
        powerup = powerUps.get((int)random(powerUps.size()));
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

    if(!shield){
      fill(colFill.x, colFill.y, colFill.z);
      stroke(colStroke.x,colStroke.y, colStroke.z);
    }
    else{
      fill(random(255), random(255), random(255));
      stroke(random(255), random(255), random(255));
    }

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
      player.pos.y += player.vel.y;
    }
    else if(moveUp){
      player.pos.y -= player.vel.y;
    }

    if(moveDownP2 && moveUpP2){

    }
    else if(moveDownP2){
      //print("Moving down \n");
      player2.pos.y += player2.vel.y;
    }
    else if(moveUpP2){
      player2.pos.y -= player2.vel.y;
    }

  }

  void Shoot(boolean player2){
    weapon.Shoot(this, player2);
  }
}
