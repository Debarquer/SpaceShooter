ArrayList<Enemy> enemies;
ArrayList<Bullet> bullets;
Player player;

float health = 10;
float score = 0;
float scoreIncrement = 10;

void setup(){
  size(1400, 900);

  PVector pos = new PVector(10, 100, 0);
  PVector vel = new PVector(5, 5, 5);
  PVector a = new PVector(0, 0, 0);
  PVector colStroke = new PVector(random(255), random(255), random(255));
  PVector colFill = new PVector(random(255), random(255), random(255));
  float r = 15;
  float health = 10;
  PVector size = new PVector(20, 15);

  player = new Player(pos, vel, a, colStroke, colFill, r, health, size);

  enemies = new ArrayList<Enemy>();
  SpawnEnemies();

  bullets = new ArrayList<Bullet>();
}

void draw(){
  background(0, 255, 255);

  PFont f;
  f = createFont("Arial", 32, true);
  textFont(f, 32);
  fill(0, 0, 0);

  String s = "Health: "+(int)health + " Score: "+(int)score;
  text(s, 30, 30);

  if(enemies.size() <= 0){
    SpawnEnemies();
  }

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
            bullets.get(i).enabled = false;
            enemies.get(j).enabled = false;
            score += scoreIncrement;
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
            exit();
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

    float r = 5;
    PVector pos = new PVector(player.pos.x + player.size.x, player.pos.y + player.size.y/2 - r/2, player.pos.z);
    PVector vel = new PVector(10, 10, 10);
    PVector a = new PVector(0, 0, 0);
    PVector colStroke = new PVector(0, 255, 0);
    PVector colFill = new PVector(0, 0, 0);
    float health = 1;

    Bullet bullet = new Bullet(pos, vel, a, colStroke, colFill, r, health, true);
    bullets.add(bullet);
  }
}

void SpawnEnemies(){
  for(int i = 0; i < 6; i++){
    float r = 30;
    float margin = 5;
    float x = width + (i * (r + 50));
    //float x = width/2;
    PVector pos = new PVector(x, 0, 0);
    PVector vel = new PVector(2, 2, 2);
    PVector a = new PVector(0, 0, 0);
    PVector colStroke = new PVector(155, 155, 155);
    PVector colFill = new PVector(255, 0, 0);
    float health = 10;
    float angle = i * 3;

    Enemy enemy = new Enemy(pos, vel, a, colStroke, colFill, r, health, angle);
    enemies.add(enemy);
  }
}
