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

  boolean hasGeneratedGoal = false;

  float puTimerMax = 5;
  float puTimerCurr;
  float messageTimerMax = 1;
  float messageTimerCurr = messageTimerMax + 1;

  boolean keepDrawing = false;
  float disableTimerMax = 1.5;
  float diableTimerCurr = 0;

  public PowerUp() {
    numbers = new int [4];
    numbers[0] = 3;
    numbers[1] = 5;
    numbers[2] = 8;
    numbers[3] = 10;
    pos = new PVector();
    colStroke = new PVector(20, 255, 20);
    colFill = new PVector(20, 255, 20);
    pos.x= random(width);
    pos.y= random(height);
    RandNum();
  }
  void RandNum() {
    // if(puTimerCurr >= puTimerMax){
    //   rand = (int)random(numbers.length);
    //   scoreupdate = (int)score + numbers[rand];
    //   println (scoreupdate);
    //
    //   hasGeneratedGoal = true;
    // }
  }

  void keepDrawing(){
    //print(keepDrawing + ":" + diableTimerCurr + "\n");
    if(keepDrawing){
      diableTimerCurr += (float)1/60;
      if(diableTimerCurr < disableTimerMax){
        ellipse (pos.x, pos.y, r, r);
        powerup.Message();
      }
      else{
        keepDrawing = false;
        diableTimerCurr = 0;
      }
    }
  }

  void update () {
    RandNum();

    puTimerCurr += (float)1/60;
    if(puTimerCurr >= puTimerMax){
      time = millis();
    }

    ellips();

    messageTimerCurr += (float)1/60;
    if(messageTimerCurr < messageTimerMax){
      enabled = true;
    }
  }

  //draws the ellipse
  void ellips() {
    //print(millis()+":"+(time+3000)+"\n");
    if (millis() < time + 3000) {
      float test = 1f -  ((float)millis() - time)/3000;
      print(test + "\n");
      r = test * sizemod;
      fill(colFill.x, colFill.y, colFill.z);
      ellipse (pos.x, pos.y, r, r);
      // trufalse = false;
    }
  }
  public void activate(){
  //test
    print("Base activate function\n");

    messageTimerCurr = 0;
    player.receivePowerup();
  }

  public void deactivate(){
  //test
    print("Base deactivate function\n");
    //time = millis();
    hasGeneratedGoal = false;
    puTimerCurr = 0;
    rand = (int)random(numbers.length);
    puTimerMax = numbers[rand];
    println (puTimerMax + "\n");
    pos.x= random(width);
    pos.y= random(height);
  }

  public void Message(){

  }
}
