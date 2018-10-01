import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class SpaceShooter extends PApplet {

ArrayList<Enemy> enemies;
ArrayList<Bullet> bullets;
Player player;

float health = 10;
float score = 0;
float scoreIncrement = 10;
float level = 1;

public void setup(){
  

  PVector pos = new PVector(10, 100, 0);
  PVector vel = new PVector(5, 5, 5);
  PVector a = new PVector(0, 0, 0);
  PVector colStroke = new PVector(random(255), random(255), random(255));
  PVector colFill = new PVector(random(255), random(255), random(255));
  float r = 15;
  float health = 10;
  PVector size = new PVector(20, 15);

  Weapon weapon = new Weapon(1);
  player = new Player(pos, vel, a, colStroke, colFill, r, health, size, weapon);

  enemies = new ArrayList<Enemy>();
  SpawnEnemies();

  bullets = new ArrayList<Bullet>();
}

public void draw(){
  background(0, 255, 255);

  PFont f;
  f = createFont("Arial", 32, true);
  textFont(f, 32);
  fill(0, 0, 0);

  String s = "Health: "+(int)health + " Score: "+(int)score + " Level: " + (int)level;
  text(s, 30, 30);

  if(enemies.size() <= 0){
    level++;
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
            //exit();
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

    Bullet bullet = new Bullet(pos, vel, a, colStroke, colFill, r, health, true, player.weapon.damage);
    bullets.add(bullet);
  }
}

public void SpawnEnemies(){
  for(int i = 0; i < 6; i++){
    float r = 30;
    float margin = 5;
    float x = width + (i * (r + 50));
    //float x = width/2;
    PVector pos = new PVector(x, 0, 0);
    PVector vel = new PVector(2, 2, 2);
    PVector a = new PVector(0, 0, 0);
    PVector colStroke = new PVector(0, 255, 0);
    PVector colFill = new PVector(255, 0, 0);
    float health = 1 + 2*(level-1);
    float angle = i * 3;

    Enemy enemy = new Enemy(pos, vel, a, colStroke, colFill, r, health, angle);
    enemies.add(enemy);
  }
}
class Bullet extends GameObject{

  boolean playerBullet;
  float damage;

  public Bullet(){
    super();
  }

  public Bullet(PVector pos, PVector vel, PVector a, PVector colStroke, PVector colFill, float r, float health, boolean playerBullet, float damage){
    super(pos, vel, a, colStroke, colFill, r, health);

    this.playerBullet = playerBullet;

    this.damage = damage;
  }

  public void Update(){
    super.Update();

    if(pos.x > width){
      enabled = false;
    }

    ellipseMode(RADIUS);
    ellipse(pos.x, pos.y, r, r);
  }

  public void Move(){
    super.Move();

    pos.x += vel.x;
  }
}
public boolean BulletEnemyCollision(Bullet bullet, Enemy enemy){
  // if(bullet.pos.x + bullet.r > enemy.pos.x && bullet.pos.x < enemy.pos.x + enemy.r && bullet.pos.y + bullet.r > enemy.pos.y && bullet.pos.y < enemy.pos.y + enemy.r){
  //   return true;
  // }

  return dist(bullet.pos.x, bullet.pos.y, enemy.pos.x, enemy.pos.y) < bullet.r + enemy.r;
}

public boolean BulletPlayerCollision(GameObject bullet, Player player){
  return dist(bullet.pos.x, bullet.pos.y, player.pos.x, player.pos.y) < bullet.r + player.size.x;
}
class Enemy extends GameObject{

  float angle, amplitude;
  boolean sine = true;

  float shootTimerMax = 1;
  float shootTimerCurr = 0;
  boolean canFire = true;

  public Enemy(){
    super();
  }

  public Enemy(PVector pos, PVector vel, PVector a, PVector colStroke, PVector colFill, float r, float health, float angle){
    super(pos, vel, a, colStroke, colFill, r, health);

    this.angle = angle;
    amplitude = 100;
  }

  public void Update(){
    super.Update();

    if(!canFire){
      //print(shootTimerCurr + "\n");
      shootTimerCurr += (float)1/60;
      if(shootTimerCurr >= shootTimerMax){
        shootTimerCurr = 0;
        canFire = true;
      }
    }
    else{
      Shoot();
      canFire = false;
    }

    angle += (float)1/60;

    if(pos.y > height){
      enabled = false;
    }
    if(pos.x < -r){
      sine = !sine;
      score -= scoreIncrement;
      pos.x = width;
    }

    if(sine)
      pos.y = height/2 + sin(angle) * amplitude;
    else
      pos.y = height/2 + cos(angle) * amplitude;

    fill(colFill.x, colFill.y, colFill.z);
    ellipseMode(RADIUS);
    ellipse(pos.x, pos.y, r, r);

    PFont f;
    f = createFont("Arial", 16, true);
    textFont(f, 16);
    fill(0, 0, 0);

    String s = ""+(int)this.currHealth;
    text(s, pos.x, pos.y);
  }

  public void Shoot(){
    float rBullet = 5;
    PVector posBullet = new PVector(pos.x - this.r, pos.y - this.r/2 + rBullet/2, pos.z);
    PVector velBullet = new PVector(-10, -10, -10);
    PVector aBullet = new PVector(0, 0, 0);
    PVector colStrokeBullet = new PVector(0, 0, 0);
    PVector colFillBullet = new PVector(255, 0, 0);
    float healthBullet = 1;

    Bullet bullet = new Bullet(posBullet, velBullet, aBullet, colStrokeBullet, colFillBullet, rBullet, healthBullet, false, 1);
    bullets.add(bullet);
  }

  public void Move(){
    super.Move();

    pos.x -= vel.x;
    amplitude = 400;

    if(sine)
      pos.y = height/2 + sin(angle) * amplitude;
    else
      pos.y = height/2 + cos(angle) * amplitude;
  }

  public boolean TakeDamage(float damage){
    currHealth -= damage;
    if(currHealth <= 0){
      return true;
    }
    else{
      colFill = new PVector(currHealth/this.health * 255, (1/(currHealth/this.health)) * 255, 0);
      return false;
    }
  }
}
abstract class GameObject{
  PVector pos, vel, a, colStroke, colFill;
  float r, health, currHealth;
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
    this.currHealth = this.health = health;

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
boolean moveLeft;
boolean moveRight;
boolean moveUp;
boolean moveDown;
boolean space;

public void keyPressed()
{

	//println(keyCode);

	//keyCodes

	if(key == CODED)
	{
		if(keyCode == RIGHT)
		{
			moveRight = true;
		}
		else if(keyCode == LEFT)
		{
			moveLeft = true;
		}
		if(keyCode == UP)
		{
			moveUp = true;
		}
		else if(keyCode == DOWN)
		{
			moveDown = true;
		}
	}

	//letters

	if(key == 'd' || key == 'D')
	{
		moveRight = true;
	}
	else if(key == 'a' || key == 'A')
	{
		moveLeft = true;
	}
	if(key == 'w' || key == 'W')
	{
		moveUp = true;
	}
	if(key == 's' || key == 'S')
	{
		moveDown = true;
	}

	if(key == ' ')
	{
		space = true;
	}
}

public void keyReleased()
{

	//keyCodes

	if(key == CODED)
	{
		if(keyCode == RIGHT)
		{
			moveRight = false;
		}
		else if(keyCode == LEFT)
		{
			moveLeft = false;
		}
		if(keyCode == UP)
		{
			moveUp = false;
		}
		else if(keyCode == DOWN)
		{
			moveDown = false;
		}
	}

	//letters

	if(key == 'd' || key == 'D')
	{
		moveRight = false;
	}
	else if(key == 'a' || key == 'A')
	{
		moveLeft = false;
	}
	if(key == 'w' || key == 'W')
	{
		moveUp = false;
	}
	if(key == 's' || key == 'S')
	{
		moveDown = false;
	}

	if(key == ' ')
	{
		space = false;
	}
}

public float getAxisRaw(String axis)
{
	if(axis == "Horizontal")
	{
		if(moveLeft)
		{
			return -1;
		}
		if(moveRight)
		{
			return 1;
		}
	}

	if(axis == "Vertical")
	{
		if(moveUp)
		{
			return -1;
		}
		if(moveDown)
		{
			return 1;
		}
	}

	return 0;
}
class Player extends GameObject{
  PVector size;

  float shootTimerMax = 0.5f;
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
class Weapon{
  float damage;

  public Weapon(){

  }

  public Weapon(float damage){
    this.damage = damage;
  }
}
  public void settings() {  size(1400, 900); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "SpaceShooter" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
