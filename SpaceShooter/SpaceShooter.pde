enum GameState {MainMenu, Highscore, Paused, Playing};
GameState gameState = GameState.MainMenu;

ArrayList<Enemy> enemies;
ArrayList<Bullet> bullets;
Player player;
PowerUp powerup;
Stars stars;

float health = 10;
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

void setup(){
  size(1400, 900);

  PVector pos = new PVector(10, 100, 0);
  PVector vel = new PVector(7, 7, 7);
  PVector a = new PVector(0, 0, 0);
  PVector colStroke = new PVector(random(255), random(255), random(255));
  PVector colFill = new PVector(random(255), random(255), random(255));
  float r = 15;
  float health = 10;
  PVector size = new PVector(20, 15);

  Weapon weapon = new Weapon(1, 0.3);
  player = new Player(pos, vel, a, colStroke, colFill, r, health, size, weapon);

  enemies = new ArrayList<Enemy>();
  SpawnEnemies();

  bullets = new ArrayList<Bullet>();
  powerup = new PowerUp();

  stars = new Stars();

  BGAImage = loadImage("Resources/BGA.png");
  BGBImage = loadImage("Resources/BGB.png");
  BGCImage = loadImage("Resources/BGC.png");
  BGDImage = loadImage("Resources/BGD.png");
  BGEImage = loadImage("Resources/BGE.png");
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
    powerup.update();
    player.Move();
    player.Update();

    for(int i = 0; i < enemies.size(); i++){
      if(enemies.get(i).enabled){
        enemies.get(i).Move();

        if(BulletPlayerCollision(enemies.get(i), player)){
          enemies.get(i).enabled = false;
          health--;
          if(health <= 0){
            health = 0;
            print("YOU LOSE!\n");
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
                bullets.get(i).enabled = false;
                enemies.get(j).enabled = false;
                score += scoreIncrement;
              }
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

              saveHighscore();


              gameState = GameState.MainMenu;
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

      player.Shoot();
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

    if(pressedM && releasedM){
      gameState = GameState.Playing;
      pressedM = false;
      releasedM = false;
    }
  }
}

void SpawnEnemies(){
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
  fill(0, 0, 0);

  text(s, x, y);
}
