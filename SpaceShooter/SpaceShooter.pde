enum GameState {MainMenu, Highscore, Paused, Playing, GameOver};
GameState gameState = GameState.MainMenu;
boolean multiplaying = true;

ArrayList<Enemy> enemies;
ArrayList<Bullet> bullets;
Player player;
Player player2;
PowerUp powerup;

Stars stars;

float maxHealth = 10;
float health = maxHealth;
float score = 0;
float scoreIncrement = 10;
float level = 1;

PImage BGAImage;
PImage BGBImage;
PImage BGCImage;
PImage BGDImage;
PImage BGEImage;
float BGAPos = 0;
float BGBPos = 0;
float BGCPos = 0;
float BGDPos = 0;
float BGEPos = 0;

ArrayList<PowerUp> powerUps;

void setup(){
  size(1400, 900);

  PVector pos1 = new PVector(10, 100, 0);
  PVector vel1 = new PVector(7, 7, 7);
  PVector a1 = new PVector(0, 0, 0);
  PVector colStroke1 = new PVector(random(255), random(255), random(255));
  PVector colFill1 = new PVector(random(255), random(255), random(255));
  float r1 = 15;
  float health1 = 10;
  PVector size1 = new PVector(20, 15);
  Weapon weapon1 = new Weapon(1, 0.3);

  player = new Player(pos1, vel1, a1, colStroke1, colFill1, r1, health1, size1, weapon1);

  if(multiplaying){
    PVector pos2 = new PVector(10, height-100, 0);
    PVector vel2 = new PVector(7, 7, 7);
    PVector a2 = new PVector(0, 0, 0);
    PVector colStroke2 = new PVector(random(255), random(255), random(255));
    PVector colFill2 = new PVector(random(255), random(255), random(255));
    float r2 = 15;
    float health2 = 10;
    PVector size2 = new PVector(20, 15);
    Weapon weapon2 = new Weapon(1, 0.3);

    player2 = new Player(pos2, vel2, a2, colStroke2, colFill2, r2, health2, size2, weapon2);
    player2.isPlayer2 = true;
  }


  powerup = new PU_RandomWeapon();
  enemies = new ArrayList<Enemy>();
  SpawnEnemies();

  bullets = new ArrayList<Bullet>();

  stars = new Stars();

  BGAImage = loadImage("Resources/BGA.png");
  BGBImage = loadImage("Resources/BGB.png");
  BGCImage = loadImage("Resources/BGC.png");
  BGDImage = loadImage("Resources/BGD.png");
  BGEImage = loadImage("Resources/BGE.png");

  powerUps = new ArrayList<PowerUp>();
  powerUps.add(new PU_FasterBullets());
  powerUps.add(new PU_MoveFaster());
  powerUps.add(new PU_RandomWeapon());
}

void draw(){
  //print(gameState + "\n");

  if(gameState == GameState.Playing){
    background(0, 0, 55);
    //stars.UpdateStars();

    image(BGEImage, BGEPos, 0, 5600, 900);
    image(BGDImage, BGDPos, 0, 5600, 900);
    image(BGCImage, BGCPos, 0, 5600, 900);
    image(BGBImage, BGBPos, 0, 5600, 900);
    image(BGAImage, BGAPos, 0, 5600, 900);
    BGAPos-=5;
    if(BGAPos < -BGAImage.width/2){
      BGAPos = 0;
    }
    BGBPos-=4;
    if(BGBPos < -BGBImage.width/2){
      BGBPos = 0;
    }
    BGCPos-=3;
    if(BGCPos < -BGCImage.width/2){
      BGCPos = 0;
    }
    BGDPos-=2;
    if(BGDPos < -BGDImage.width/2){
      BGDPos = 0;
    }
    BGEPos-=1;
    if(BGEPos < -BGEImage.width/2){
      BGEPos = 0;
    }

    String s = "Health: "+(int)health + " Score: "+(int)score + " Level: " + (int)level;
    DrawText(32, 30, 30, s);

    if(enemies.size() <= 0){
      level++;
      SpawnEnemies();
    }

    player.Move();
    player.Update();
    if(multiplaying){
      player2.Move();
      player2.Update();
    }

    for(int i = 0; i < enemies.size(); i++){
      if(enemies.get(i).enabled){
        enemies.get(i).Move();

        if(BulletPlayerCollision(enemies.get(i), player)){
          enemies.get(i).enabled = false;
          health--;
          if(health <= 0){
            health = 0;
            print("YOU LOSE!\n");

            gameState = GameState.GameOver;
          }
        }
        if(multiplaying){
          if(BulletPlayerCollision(enemies.get(i), player2)){
            enemies.get(i).enabled = false;
            health--;
            if(health <= 0){
              health = 0;
              print("YOU LOSE!\n");

              gameState = GameState.GameOver;
            }
          }
        }

        enemies.get(i).Update();
      }
      else{
        enemies.remove(i);
      }
    }

    //print(bullets.size() + "\n");
    for(int i = 0; i < bullets.size(); i++){
      if(bullets.get(i).enabled){
        bullets.get(i).Move();

        if(bullets.get(i).playerBullet){
          for(int j = 0; j < enemies.size(); j++){
            if(BulletEnemyCollision(bullets.get(i), enemies.get(j))){
              if(enemies.get(j).TakeDamage(bullets.get(i).damage)){
                enemies.get(j).enabled = false;
                score += scoreIncrement;
              }
              bullets.get(i).enabled = false;
            }
          }
          if(powerup.enabled){
            if(BulletEnemyCollision(bullets.get(i), powerup)){
              //get powered up
              bullets.get(i).enabled = false;
              powerup.enabled = false;
              powerup.keepDrawing = true;
              powerup.diableTimerCurr = 0;
            }
          }
        }
        else{
          //check collision with the player
          if(BulletPlayerCollision(bullets.get(i), player)){
            bullets.get(i).enabled = false;
            health--;
            if(health <= 0){
              health = 0;
              print("YOU LOSE!\n");

              gameState = GameState.GameOver;
            }
          }
          if(multiplaying){
            if(BulletPlayerCollision(bullets.get(i), player2)){
              bullets.get(i).enabled = false;
              health--;
              if(health <= 0){
                health = 0;
                print("YOU LOSE!\n");

                gameState = GameState.GameOver;
              }
            }
          }
        }

        bullets.get(i).Update();
      }
      else{
        bullets.remove(i);
      }
    }

    if(space && player.canFire){
      player.canFire = false;

      player.Shoot(false);
    }
    if(multiplaying){
      if(spaceP2 && player2.canFire){
        player2.canFire = false;

        player2.Shoot(true);
      }
    }

    if(pressedEscape && releasedEscape){
      gameState = GameState.Paused;
      pressedEscape = false;
      releasedEscape = false;
    }
    if(pressedM && releasedM){
      gameState = GameState.MainMenu;
      pressedM = false;
      releasedM = false;
    }
  }
  else if(gameState == GameState.Paused){

    DrawText(64, width/2, height/2, "Paused");

    if(pressedEscape && releasedEscape){
      gameState = GameState.Playing;
      pressedEscape = false;
      releasedEscape = false;
    }
  }
  else if(gameState == GameState.MainMenu){
    DrawMainMenu();

    if(pressedM && releasedM){
      gameState = GameState.Playing;
      pressedM = false;
      releasedM = false;
    }
  }
  else if(gameState == GameState.Highscore){
    DrawHighscore();

    // if(pressedM && releasedM){
    //   gameState = GameState.Playing;
    //   pressedM = false;
    //   releasedM = false;
    // }
  }
  else if(gameState == GameState.GameOver){
    //saveHighscore();

    //gameState = GameState.Highscore;

    //nameA = "";
    //nameB = "";
    DrawHighscore();
  }
}

void ResetGame(){
  print("reset game\n");
  score = 0;
  level = 1;
  health = maxHealth;
  player.powerupTimerCurr = player.powerupTimerMax;
  player2.powerupTimerCurr = player2.powerupTimerMax;

  for(Enemy enemy : enemies){
    enemy.enabled = false;
  }
  for(Bullet bullet : bullets){
    bullet.enabled = false;
  }
}

void SpawnEnemies(){

  // if (level % 3 == 0){
  //   powerup.RandNum();
  // }



  for(int i = 0; i < 6; i++){
    float r = 30;
    float x = width + (i * (r + 50));
    float angle = i * 3;

    enemies.add(new Enemy(level, angle, new PVector(x, 0, 0), r));
  }
}

void DrawText(float size, float x, float y, String s){
  PFont f;
  f = createFont("Arial", size, true);
  textFont(f, size);
  //fill(0, 0, 0);

  fill(0);
  for(int i = -1; i < 2; i++){
  //  for(int y = -1; y < 2; y++){
  //    text("LIKE THIS!", 20+x,20+y);
  //  }
      text(s, x+i,y);
      text(s, x,y+i);
  }
  fill(255);
  text(s, x,y);

  //text(s, x, y);
}
