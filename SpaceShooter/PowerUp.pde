class PowerUp extends GameObject {
  //ellipse (pos.x, pos.y, 20, 20);


  float PowerUpInc;
  public int numbers[];
  public int rand;
  int scoreupdate;
  int spawn = 0;
  int time;
  boolean trufalse = false;
  float sizemod = 50;
  

  public PowerUp() {
    numbers = new int [4];
    numbers[0] = 10;
    numbers[1] = 20;
    numbers[2] = 30;
    numbers[3] = 40;
    pos = new PVector();
    colStroke = new PVector(20, 255, 20);
    colFill = new PVector(20, 255, 20);
    pos.x= random(width);
    pos.y= random(height);
    RandNum();
  }
  void RandNum() {
    rand = (int)random(numbers.length);
    scoreupdate = (int)score + numbers[rand];
    println (scoreupdate);
  }

  void update () {
    if (score == scoreupdate) {
      trufalse = true;
      time = millis();
    }
    if (trufalse == true) {

      ellips();
    }
  }
  void ellips() {

    if (millis() < time + 3000) {
      float test = 1f -  ((float)millis() - time)/3000;
        r = test * sizemod;
      fill(255);
      ellipse (pos.x, pos.y, r, r);
      // trufalse = false;
    }
    
  }
  void activate(){
  //test
  
  }
}
