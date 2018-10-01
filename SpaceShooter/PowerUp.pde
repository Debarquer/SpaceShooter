class PowerUp extends GameObject{
  
  float PowerUpInc;
 // int numbers[];
 
 public PowerUp(){
   //numbers = new int[]
//  super(pos, vel, a, colStroke, colFill, r, health);
  pos = new PVector();
  PowerUpInc = random(100);
  //vel = new PVector();
  colStroke = new PVector(20, 255, 20);
  colFill = new PVector(20, 255, 20);
    pos.x= random(width);
    pos.y= random(height);
    //PowerUpInc = %10;
  
  }
  void update (){
if (score == PowerUpInc){
  ellipse (pos.x, pos.y, 20, 20);
}

  }
  
  

}
