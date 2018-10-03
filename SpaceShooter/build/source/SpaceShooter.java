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

enum GameState {MainMenu, Highscore, Paused, Playing, GameOver};
GameState gameState = GameState.MainMenu;

ArrayList<Enemy> enemies;
ArrayList<Bullet> bullets;
Player player;
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

public void setup(){
  

  PVector pos = new PVector(10, 100, 0);
  PVector vel = new PVector(7, 7, 7);
  PVector a = new PVector(0, 0, 0);
  PVector colStroke = new PVector(random(255), random(255), random(255));
  PVector colFill = new PVector(random(255), random(255), random(255));
  float r = 15;
  float health = 10;
  PVector size = new PVector(20, 15);

  //damage, fireRate
  Weapon weapon = new Weapon(1, 0.3f);
  player = new Player(pos, vel, a, colStroke, colFill, r, health, size, weapon);
  powerup = new PU_FasterBullets();
  enemies = new ArrayList<Enemy>();
  SpawnEnemies();

  bullets = new ArrayList<Bullet>();

  stars = new Stars();

  BGAImage = loadImage("Resources/BGA.png");
  BGBImage = loadImage("Resources/BGB.png");
  BGCImage = loadImage("Resources/BGC.png");
  BGDImage = loadImage("Resources/BGD.png");
  BGEImage = loadImage("Resources/BGE.png");
}

public void draw(){
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
   powerup.update();
    String s = "Health: "+(int)health + " Score: "+(int)score + " Level: " + (int)level;
    DrawText(32, 30, 30, s);

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

            gameState = GameState.GameOver;
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
  else if(gameState == GameState.GameOver){
    saveHighscore();

    gameState = GameState.Highscore;
    DrawHighscore();
    ResetGame();
  }
}

public void ResetGame(){
  score = 0;
  level = 0;
  health = maxHealth;
  for(Enemy enemy : enemies){
    enemy.enabled = false;
  }
}

public void SpawnEnemies(){

  if (level % 3 == 0){
    powerup.RandNum();
  }
  for(int i = 0; i < 6; i++){
    float r = 30;
    float x = width + (i * (r + 50));
    float angle = i * 3;

    enemies.add(new Enemy(level, angle, new PVector(x, 0, 0), r));
  }
}

public void DrawText(float size, float x, float y, String s){
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
class Stars{
  ArrayList<Star> stars;

  public Stars(){
    stars = new ArrayList<Star>();
    for(int i = 0; i < 100; i++){
        stars.add(new Star(random(255), random(255), random(255), random(width), random(height)));
    }
  }

  public void UpdateStars(){
    for(Star star : stars){
      star.Update();
    }
  }
}

class Star
{
  PVector starColor;
  PVector pos;
  float numStars = 100;

  public Star(float x, float y, float z, float a, float b)
  {
  this.starColor = new PVector(x, y, z);
  this.pos = new PVector(a, b);
  }

  public void Update()
  {
    for(int i = 0; i <= numStars; i++)
    {
      strokeWeight(2);
      fill(starColor.x, starColor.y, starColor.z);
      point(pos.x, pos.y);

    }
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

    //fill(colFill.x, colFill.y, colFill.z);
    fill(player.weapon.bulletCol.x, player.weapon.bulletCol.y, player.weapon.bulletCol.z);
    stroke(colStroke.x,colStroke.y, colStroke.z);

    ellipseMode(RADIUS);
    ellipse(pos.x, pos.y, r, r);
  }

  public void Move(){
    super.Move();

    pos.x += vel.x;
  }
}
public boolean BulletEnemyCollision(Bullet bullet, GameObject other){

  if(dist(bullet.pos.x, bullet.pos.y, other.pos.x, other.pos.y) < bullet.r + other.r){
    //print("Collision\n");
    if(other instanceof PowerUp){
      //print("With PowerUp\n");
      ((PowerUp)other).activate();
    }

    return true;
  }
  else{
    return false;
  }
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

  float baseSpeed = 5;
  float baseHealth = 1;

  public Enemy(){
    super();
  }

  public Enemy(float level, float angle, PVector pos, float r){
    this.pos = new PVector();
    this.pos.x = pos.x;
    this.pos.y = pos.y;
    this.pos.z = pos.z;

    this.vel = new PVector();
    this.vel.x = baseSpeed + level/10;
    this.vel.y = baseSpeed + level/10;
    this.vel.z = baseSpeed + level/10;

    this.a = new PVector();
    this.a.x = a.x;
    this.a.y = a.y;
    this.a.z = a.z;

    this.colStroke = new PVector();
    this.colStroke.x = 0;
    this.colStroke.y = 0;
    this.colStroke.z = 0;

    this.colFill = new PVector();
    this.colFill.x = 255;
    this.colFill.y = 0;
    this.colFill.z = 0;

    this.r = r;
    this.currHealth = this.health = baseHealth /*+ level*/;

    enabled = true;

    this.angle = angle;
    amplitude = 100;
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
      //sine = !sine;
      score -= scoreIncrement;
      if(score <= 0)
        score = 0;
      //pos.x = width;
      enabled = false;
    }

    if(sine)
      pos.y = height/2 + sin(angle) * amplitude;
    else
      pos.y = height/2 + cos(angle) * amplitude;

    fill(colFill.x, colFill.y, colFill.z);
    ellipseMode(RADIUS);
    ellipse(pos.x, pos.y, r, r);
    //ellipse(pos.x, pos.y, currHealth/this.health*r, currHealth/this.health*r);

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
class FastWeapon extends Weapon{
  float damage;
  float fireRate;

  PVector bulletCol = new PVector(0, 255, 255);

  public FastWeapon(){
    fireRate = 0.01f;
    damage = 1;
  }

  public FastWeapon(float damage, float fireRate){

    print("Instantiated new fast weapon\n");

    this.damage = damage;
    this.fireRate = fireRate;
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
    //stroke(colStroke.x, colStroke.y,colStroke.z);
    //fill(colFill.x, colFill.y, colFill.z);
  }

  public void Move(){

  }

  public void Shoot(){

  }
}

PImage mainMenuImage;

ButtonRect mainMenuButton;

float topBoundary = 100;
float bottomBoundary = 400;
float highscoreAnim = bottomBoundary;
float scoreMargin = 50;

public void DrawHighscore(){
  background(155, 155, 155);

  mainMenuImage = loadImage("Resources/MenuButton.png");
  image(mainMenuImage, width/2 - mainMenuImage.width/2, 600);
  mainMenuButton = new ButtonRect(width/2 - mainMenuImage.width/2, width/2 + mainMenuImage.width/2, 600, 600+mainMenuImage.height);

  String s = "Highscore";
  DrawText(32, width/2 - 100, 30, s);

  boolean drewAnything = false;
  String[] sa = loadHighscore();
  for(int i = 0; i < sa.length; i++){
    if(topBoundary + i * scoreMargin + highscoreAnim > topBoundary+25 && i * scoreMargin + highscoreAnim < bottomBoundary){
        drewAnything = true;
        DrawText(32, width/2 - 100, topBoundary + i * scoreMargin + highscoreAnim, sa[i]);
    }
  }

  line(0, topBoundary, width, topBoundary);
  line(0, bottomBoundary+100, width, bottomBoundary+100);

  if(highscoreAnim == -bottomBoundary-100)
    highscoreAnim = bottomBoundary;
  else
    highscoreAnim--;
}

public void saveHighscore(){
  String[] highscores = loadStrings("data/highscores.txt");
  String[] tmp = new String[highscores.length + 1];
  for(int i = 0; i < highscores.length; i++){

    tmp[i] = highscores[i];
  }
  tmp[tmp.length - 1] = ""+(int)score;

  saveStrings(dataPath("highscores.txt"), tmp);
}

public String[] loadHighscore(){
  // String[] highscores = loadStrings("data/highscores.txt");
  //
  // String s = "";
  // for(String string : highscores){
  //   s += string + "\n";
  // }
  // return s;

  return loadStrings("data/highscores.txt");
}
boolean moveLeft;
boolean moveRight;
boolean moveUp;
boolean moveDown;
boolean space;
boolean pressedEscape;
boolean releasedEscape;
boolean pressedM;
boolean releasedM;

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

	if(key == 'p' || key == 'P')
	{
		pressedEscape = true;
	}
	if(key == 'm' || key == 'M')
	{
		pressedM = true;
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

	if(key == 'p' || key == 'P')
	{
		releasedEscape = true;
	}
	if(key == 'm' || key == 'M')
	{
		releasedM = true;
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

public void mouseReleased(){
  //print(buttonA.Clicked(mouseX, mouseY));

	if(gameState == GameState.MainMenu){
		if(playButton.Clicked(mouseX, mouseY)){
	    gameState = GameState.Playing;
	  }
	  else if(highscoreButton.Clicked(mouseX, mouseY)){
	    gameState = GameState.Highscore;
	  }
		else if(exitButton.Clicked(mouseX, mouseY)){
	    exit();
	  }
	}
	else if(gameState == GameState.Highscore){
		if(mainMenuButton.Clicked(mouseX, mouseY)){
	    gameState = GameState.MainMenu;
	  }
	}
}
class ButtonRect{
  float minX, maxX, minY, maxY;

  public ButtonRect(float minX, float maxX, float minY, float maxY){
    this.minX = minX;
    this.maxX = maxX;
    this.minY = minY;
    this.maxY = maxY;
  }

  public boolean Clicked(float x, float y){
    return x > minX && x < maxX && y > minY && y < maxY;
  }
}

PImage playImage;
PImage highscoreImage;
PImage exitImage;

ButtonRect playButton;
ButtonRect highscoreButton;
ButtonRect exitButton;

public void DrawMainMenu(){
  background(155, 155, 155);

  String s = "Space Shooter";
  DrawText(32, width/2 - 100, 30, s);

  playImage = loadImage("Resources/PlayButton.png");
  image(playImage, width/2 - playImage.width/2, 200);
  playButton = new ButtonRect(width/2 - playImage.width/2, width/2 + playImage.width/2, 200, 200+playImage.height);

  highscoreImage = loadImage("Resources/HighscoreButton.png");
  image(highscoreImage, width/2 - highscoreImage.width/2, 400);
  highscoreButton = new ButtonRect(width/2 - highscoreImage.width/2, width/2 + highscoreImage.width/2, 400, 400+highscoreImage.height);

  exitImage = loadImage("Resources/ExitButton.png");
  image(exitImage, width/2 - exitImage.width/2, 600);
  exitButton = new ButtonRect(width/2 - exitImage.width/2, width/2 + exitImage.width/2, 600, 600+exitImage.height);
}
class PU_FasterBullets extends PowerUp{

public void spray(){

player.weapon.fireRate = 0.0009f;




}
public void activate() {
spray();

}



}

class PU_RandomWeapon extends PowerUp{

  public PU_RandomWeapon(){
    super();

    colFill = new PVector(50, 255, 50);
  }

  public void activate(){

    //print("Enjoy your new weapon \n");
    player.weapon = new FastWeapon(1, 0.01f);
    player.receivePowerup();

    player.weapon.fireRate = 0.01f;
  }

  public void deactivate(){
  //test
    //print("Deactivated power up");

    //player.weapon = new Weapon();
    player.weapon.fireRate = 0.3f;
    //RandNum();
  }
}
class Player extends GameObject{
  PVector size;

  float shootTimerMax = 0.5f;
  float shootTimerCurr = 0;
  boolean canFire = true;

  Weapon weapon;

  float powerupTimerMax = 3;
  float powerupTimerCurr;

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

    print("Timer: " + powerupTimerCurr + "\n");
    powerupTimerCurr+=(float)1/60;
    if(powerupTimerCurr >= powerupTimerMax){
      powerupTimerCurr = 0;
      powerup.deactivate();
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

  public void receivePowerup(){
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

  public void Shoot(){
    float rBullet = 5;
    PVector posBullet = new PVector(player.pos.x + player.size.x, player.pos.y + player.size.y/2 - player.r/2, player.pos.z);
    PVector velBullet = new PVector(10, 10, 10);
    PVector aBullet = new PVector(0, 0, 0);
    PVector colStrokeBullet = new PVector(0, 0, 0);
    PVector colFillBullet = new PVector(0, 255, 0);
    float healthBullet = 1;

    Bullet bullet = new Bullet(posBullet, velBullet, aBullet, colStrokeBullet, colFillBullet, rBullet, healthBullet, true, player.weapon.damage);
    bullets.add(bullet);
  }
}
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
  public void RandNum() {
    rand = (int)random(numbers.length);
    scoreupdate = (int)score + numbers[rand];
    println (scoreupdate);
  }

  public void update () {
    if(enabled){
      if (trufalse == true) {

        ellips();
      }
    }
    if (score == scoreupdate) {
      enabled = true;
      trufalse = true;
      time = millis();
    }
  }
  public void ellips() {

    if (millis() < time + 3000) {
      float test = 1f -  ((float)millis() - time)/3000;
        r = test * sizemod;
      fill(colFill.x, colFill.y, colFill.z);
      ellipse (pos.x, pos.y, r, r);
      // trufalse = false;
    }
    else{
      enabled = false;
    }

  }
  public void activate(){
  //test
    //print("Activated power up");

    player.receivePowerup();

    RandNum();
  }

  public void deactivate(){
  //test
    print("Activated power up");

    RandNum();
  }
}
class Weapon{
  float damage;
  float fireRate;

  PVector bulletCol = new PVector(255, 255, 0);

  public Weapon(){
    damage = 1;
    fireRate = 0.3f;
  }

  public Weapon(float damage, float fireRate){
    this.damage = damage;
    this.fireRate = fireRate;
  }

  public void Shoot(){
    float rBullet = 5;
    PVector posBullet = new PVector(player.pos.x + player.size.x, player.pos.y + player.size.y/2 - player.r/2, player.pos.z);
    PVector velBullet = new PVector(10, 10, 10);
    PVector aBullet = new PVector(0, 0, 0);
    PVector colStrokeBullet = new PVector(0, 0, 0);
    PVector colFillBullet = new PVector(0, 255, 0);
    float healthBullet = 1;

    Bullet bullet = new Bullet(posBullet, velBullet, aBullet, colStrokeBullet, colFillBullet, rBullet, healthBullet, true, player.weapon.damage);
    bullets.add(bullet);
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
