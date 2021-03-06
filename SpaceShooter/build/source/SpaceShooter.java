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

public void setup(){
  

  PVector pos1 = new PVector(10, 100, 0);
  PVector vel1 = new PVector(7, 7, 7);
  PVector a1 = new PVector(0, 0, 0);
  PVector colStroke1 = new PVector(random(255), random(255), random(255));
  PVector colFill1 = new PVector(random(255), random(255), random(255));
  float r1 = 15;
  float health1 = 10;
  PVector size1 = new PVector(20, 15);
  Weapon weapon1 = new Weapon(1, 0.3f);

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
    Weapon weapon2 = new Weapon(1, 0.3f);

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
  //powerUps.add(new PU_MoveFaster());
  powerUps.add(new PU_RandomWeapon());
  //powerUps.add(new PU_Shield());
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
          if(!player.shield)
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
            if(!player2.shield)
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
            if(!player.shield)
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
              if(!player2.shield)
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

public void ResetGame(){
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

public void SpawnEnemies(){

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
  boolean player2 = false;

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
    fill(255);
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
      ((PowerUp)other).activate(bullet.player2);
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
    this.currHealth = this.health = baseHealth + level/10;

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

    // PFont f;
    // f = createFont("Arial", 16, true);
    // textFont(f, 16);
    // fill(0, 0, 0);
    //
    // String s = ""+(int)this.currHealth;
    // text(s, pos.x, pos.y);
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

  PVector bulletCol = new PVector(0, 0, 255);

  public FastWeapon(){
    fireRate = 0.01f;
    damage = 1;
  }

  public FastWeapon(float damage, float fireRate){

    super(damage, fireRate);

    print("Instantiated new fast weapon\n");
  }

  public void Shoot(Player p, boolean player2){
    float rBullet = 5;
    PVector posBullet = new PVector(p.pos.x + p.size.x, p.pos.y + p.size.y/2 - p.r/2, p.pos.z);
    PVector velBullet = new PVector(10, 10, 10);
    PVector aBullet = new PVector(0, 0, 0);
    PVector colStrokeBullet = new PVector(0, 0, 0);
    PVector colFillBullet = new PVector(0, 255, 0);
    float healthBullet = 1;

    Bullet bullet = new Bullet(posBullet, velBullet, aBullet, colStrokeBullet, colFillBullet, rBullet, healthBullet, true, p.weapon.damage);
    bullet.player2 = player2;
    bullets.add(bullet);
    posBullet = new PVector(p.pos.x + p.size.x, p.pos.y + p.size.y/2 - p.r/2 + 15, p.pos.z);
    bullet = new Bullet(posBullet, velBullet, aBullet, colStrokeBullet, colFillBullet, rBullet, healthBullet, true, p.weapon.damage);
    bullet.player2 = player2;
    bullets.add(bullet);
    posBullet = new PVector(p.pos.x + p.size.x, p.pos.y + p.size.y/2 - p.r/2 - 15, p.pos.z);
    bullet = new Bullet(posBullet, velBullet, aBullet, colStrokeBullet, colFillBullet, rBullet, healthBullet, true, p.weapon.damage);
    bullet.player2 = player2;
    bullets.add(bullet);
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
PImage highscoreMultiplayImage;
PImage submitImage;


ButtonRect mainMenuButton;
ButtonRect highscoreMultiplayButton;
ButtonRect submitButton;
ButtonRect textAreaA;
ButtonRect textAreaB;

String nameA = "";
String nameB = "";

float topBoundary = 100;
float bottomBoundary = 400;
float highscoreAnim = bottomBoundary;
float scoreMargin = 50;

public void DrawHighscore(){
  background(155, 155, 155);

  float xOffset = 200;
  mainMenuImage = loadImage("Resources/MenuButton.png");
  image(mainMenuImage, xOffset + width/2 - mainMenuImage.width/2, 700);
  mainMenuButton = new ButtonRect(xOffset + width/2 - mainMenuImage.width/2, xOffset+width/2 + mainMenuImage.width/2, 700, 700+mainMenuImage.height);

  if(gameState == GameState.Highscore){
    xOffset = -200;
    if(!multiplaying)
      highscoreMultiplayImage = loadImage("Resources/ButtonMultiplay.png");
    else
    highscoreMultiplayImage = loadImage("Resources/SoloplayButton.png");
    image(highscoreMultiplayImage, xOffset + width/2 - highscoreMultiplayImage.width/2, 700);
    highscoreMultiplayButton = new ButtonRect(xOffset + width/2 - highscoreMultiplayImage.width/2, xOffset+width/2 + highscoreMultiplayImage.width/2, 700, 700+highscoreMultiplayImage.height);
  }

  if(gameState == gameState.GameOver){
    xOffset = 450;
    submitImage = loadImage("Resources/SubmitButton.png");
    image(submitImage, xOffset + width/2 - submitImage.width/2, 567, 100, 50);
    submitButton = new ButtonRect(xOffset + width/2 - submitImage.width/2, xOffset+width/2 + submitImage.width/2, 567, 567+submitImage.height);

    float tXA = 315;
    float tXB = 300;
    float tYA = 565;
    float tYB = 50;
    if(inputTextA)
      fill(155, 155, 155);
    else
      fill(255, 255, 255);
    rect(tXA, tYA, tXB, tYB);
    textAreaA = new ButtonRect(tXA, tXA+tXB, tYA, tYA+tYB);
    DrawText(32, tXA, tYA+25+(textAscent() + textDescent())/4, nameA);

    if(inputTextB)
      fill(155, 155, 155);
    else
      fill(255, 255, 255);
    tXA *= 2.1f;
    rect(tXA, tYA, tXB, tYB);
    textAreaB = new ButtonRect(tXA, tXA+tXB, tYA, tYA+tYB);
    DrawText(32, tXA, tYA+25+(textAscent() + textDescent())/4, nameB);
  }

  //print(nameA + ":" + nameB + ":" + inputTextA + ":" + inputTextB + "\n");

  String s = "";
  if(!multiplaying)
    s = "Solo Highscores";
  else
    s = "2P Highscores";
  DrawText(32, width/2 - 100, 30, s);

  float textHeight = textAscent() + textDescent();

  boolean drewAnything = false;
  String[][] sa = loadHighscoreB();

  float stringHeight = sa.length * textHeight;

  for(int i = 0; i < sa.length; i++){
    if(topBoundary + i * textHeight + highscoreAnim > topBoundary+25 && i * textHeight + highscoreAnim < bottomBoundary){
        drewAnything = true;

        if(!multiplaying){
          DrawText(32, -150 + width/2 - 100, topBoundary + i * textHeight + highscoreAnim, sa[i][0]);
          DrawText(32, 160 + width/2 - 100, topBoundary + i * textHeight + highscoreAnim, sa[i][1]);
        }
        else{
          DrawText(32, -235+width/2 - 100, topBoundary + i * textHeight + highscoreAnim, sa[i][0]);
          DrawText(32, width/2 - 100, topBoundary + i * textHeight + highscoreAnim, sa[i][1]);
          DrawText(32, 235+width/2 - 100, topBoundary + i * textHeight + highscoreAnim, sa[i][2]);
        }
    }
  }

  line(0, topBoundary, width, topBoundary);
  line(0, bottomBoundary+100, width, bottomBoundary+100);

  if(highscoreAnim == -stringHeight)
    highscoreAnim = bottomBoundary;
  else
    highscoreAnim--;
}

public void saveHighscore(){
  if(!multiplaying){
    String[] highscores = loadStrings("data/highscores.txt");
    String[] tmp = new String[highscores.length + 1];
    for(int i = 0; i < highscores.length; i++){

      tmp[i] = highscores[i];
    }
    tmp[tmp.length - 1] = nameA+" "+(int)score;

    saveStrings(dataPath("highscores.txt"), tmp);
  }
  else{
    String[] highscores = loadStrings("data/highscores2.txt");

    String[] tmp = new String[highscores.length + 1];
    for(int i = 0; i < highscores.length; i++){

      tmp[i] = highscores[i];
    }
    tmp[tmp.length - 1] = nameA+" "+nameB+" "+(int)score;

    saveStrings(dataPath("highscores2.txt"), tmp);
  }

}

public String[] loadHighscore(){
  if(!multiplaying){
      return loadStrings("data/highscores.txt");
  }
  else{
    return loadStrings("data/highscores2.txt");
  }
}

public String[][] loadHighscoreB(){
  if(!multiplaying){
    String[] highscoresOld = loadStrings("data/highscores.txt");

    //nameA, nameB, score for every entry
    String[][] highscoresNew = new String[highscoresOld.length][2];
    for(int i = 0; i < highscoresOld.length; i++){
      //add here
      String[] tmp = highscoresOld[i].split(" ");
      highscoresNew[i] = new String[2];
      for(int j = 0; j < 2; j++){
        highscoresNew[i][j] = tmp[j];
      }
    }

      return highscoresNew;
  }
  else{
    String[] highscoresOld = loadStrings("data/highscores2.txt");

    //nameA, nameB, score for every entry
    String[][] highscoresNew = new String[highscoresOld.length][3];
    for(int i = 0; i < highscoresOld.length; i++){
      //add here
      String[] tmp = highscoresOld[i].split(" ");
      highscoresNew[i] = new String[3];
      for(int j = 0; j < 3; j++){
        highscoresNew[i][j] = tmp[j];
      }
    }

      return highscoresNew;
  }

}
boolean moveLeft;
boolean moveRight;
boolean moveUp;
boolean moveDown;
boolean moveLeftP2;
boolean moveRightP2;
boolean moveUpP2;
boolean moveDownP2;
boolean space;
boolean spaceP2;
boolean pressedEscape;
boolean releasedEscape;
boolean pressedM;
boolean releasedM;

boolean inputTextA = false;
boolean inputTextB = false;

public void keyPressed()
{
	if(inputTextA){
		//print("Starting input process\n");
		if(java.lang.Character.isLetter(key)){
			//print("Character was a letter\n");
			if(nameA.length() < 11)
				nameA += key;
		}
		else{
			//print("Character was not a ltter\n");
			if((int)key == 8){
				if(nameA.length() > 0){
					String tmp = nameA.substring(0, nameA.length()-1);
					nameA = tmp;
				}
			}
			else if((int)key == 9){
				inputTextA = false;
				inputTextB = true;
			}
			//print((int)key + "\n");
		}
	}
	else if(inputTextB){
		if(java.lang.Character.isLetter(key)){
			if(nameB.length() < 11)
				nameB += key;
		}
		else{
			if((int)key == 8){
				if(nameB.length() > 0){
					String tmp = nameB.substring(0, nameB.length()-1);
					nameB = tmp;
				}
			}
			else if((int)key == 9){
				inputTextA = true;
				inputTextB = false;
			}
			//print((int)key + "\n");
		}
	}
	if((int)key == 10){
		saveHighscore();
		gameState = GameState.Highscore;
	}

	//println(keyCode);

	//keyCodes

	if(key == CODED)
	{
		if(keyCode == RIGHT)
		{
			moveRightP2 = true;
		}
		else if(keyCode == LEFT)
		{
			moveLeftP2 = true;
		}
		if(keyCode == UP)
		{
			moveUpP2 = true;
		}
		else if(keyCode == DOWN)
		{
			moveDownP2 = true;
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
	if(key == '<')
	{
		spaceP2 = true;
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
			moveRightP2 = false;
		}
		else if(keyCode == LEFT)
		{
			moveLeftP2 = false;
		}
		if(keyCode == UP)
		{
			moveUpP2 = false;
		}
		else if(keyCode == DOWN)
		{
			moveDownP2 = false;
		}
		else if(keyCode == ENTER || keyCode == RETURN)
		{
			inputTextA = false;
			inputTextB = false;
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
	if(key == '<')
	{
		spaceP2 = false;
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
			multiplaying = false;
	  }
		else if(multiplayButton.Clicked(mouseX, mouseY)){
			multiplaying = true;
	    gameState = GameState.Playing;
	  }
	  else if(highscoreButton.Clicked(mouseX, mouseY)){
			highscoreAnim = bottomBoundary;
	    gameState = GameState.Highscore;
	  }
		else if(exitButton.Clicked(mouseX, mouseY)){
	    exit();
	  }
	}
	else if(gameState == GameState.GameOver){
		if(mainMenuButton.Clicked(mouseX, mouseY)){
			ResetGame();
	    gameState = GameState.MainMenu;
	  }
		if(submitButton.Clicked(mouseX, mouseY)){
			saveHighscore();
			gameState = GameState.Highscore;
		}
		if(textAreaA.Clicked(mouseX, mouseY)){
			//print("Clicked text area A \n");
			inputTextA = true;
			inputTextB = false;
		}
		if(textAreaB.Clicked(mouseX, mouseY)){
			//print("Clicked text area B \n");
			inputTextA = false;
			inputTextB = true;
		}
	}
	else if(gameState == GameState.Highscore){
		if(mainMenuButton.Clicked(mouseX, mouseY)){
			ResetGame();
	    gameState = GameState.MainMenu;
	  }
		if(highscoreMultiplayButton.Clicked(mouseX, mouseY)){
			multiplaying = !multiplaying;
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
PImage multiplayImage;
PImage highscoreImage;
PImage exitImage;

ButtonRect playButton;
ButtonRect multiplayButton;
ButtonRect highscoreButton;
ButtonRect exitButton;

public void DrawMainMenu(){
  background(155, 155, 155);

  String s = "Space Shooter";
  DrawText(32, width/2 - 100, 30, s);

  playImage = loadImage("Resources/PlayButton.png");
  image(playImage, width/2 - playImage.width/2, 150);
  playButton = new ButtonRect(width/2 - playImage.width/2, width/2 + playImage.width/2, 150, 150+playImage.height);

  multiplayImage = loadImage("Resources/ButtonMultiplay.png");
  image(multiplayImage, width/2 - multiplayImage.width/2, 350);
  multiplayButton = new ButtonRect(width/2 - multiplayImage.width/2, width/2 + multiplayImage.width/2, 350, 350+multiplayImage.height);

  highscoreImage = loadImage("Resources/HighscoreButton.png");
  image(highscoreImage, width/2 - highscoreImage.width/2, 550);
  highscoreButton = new ButtonRect(width/2 - highscoreImage.width/2, width/2 + highscoreImage.width/2, 550, 550+highscoreImage.height);

  exitImage = loadImage("Resources/ExitButton.png");
  image(exitImage, width/2 - exitImage.width/2, 750);
  exitButton = new ButtonRect(width/2 - exitImage.width/2, width/2 + exitImage.width/2, 750, 750+exitImage.height);
}
class PU_FasterBullets extends PowerUp{
  public void spray(boolean isPlayer2){
    if(!isPlayer2)
      player.weapon.fireRate = 0.03f;
    else
      player2.weapon.fireRate = 0.03f;
  }
  public void activate(boolean player2) {
    super.activate(player2);

    spray(player2);
  }
  public void deactivate(boolean isPlayer2){
    super.deactivate(isPlayer2);

    if(!isPlayer2)
      player.weapon.fireRate = 0.3f;
    else
      player2.weapon.fireRate = 0.3f;
  }

  public void Message(){
    DrawText(32, width/2, height/2, "You Shpot Faster");
  }
}
class PU_MoveFaster extends PowerUp{

  PVector bulletCol = new PVector(66, 66, 255);

  float oldSpeed;
  float newSpeed;
  public PU_MoveFaster(){
    super();
    oldSpeed = player.vel.y;
    newSpeed = player.vel.y * 2;
  }

public void activate(boolean isPlayer2){
super.activate(isPlayer2);

if(!isPlayer2){
  player.weapon.fireRate = 0.03f;
  player.vel.y = newSpeed;
}
else{
  player2.weapon.fireRate = 0.3f;
  player2.vel.y = newSpeed;
}

}

public void deactivate(boolean isPlayer2){
  super.deactivate(isPlayer2);

  if(!isPlayer2){
    player.weapon.fireRate = 0.3f;
    player.vel.y = oldSpeed;
  }
  else{
    player2.weapon.fireRate = 0.3f;
    player2.vel.y = oldSpeed;
  }
}

public void Message(){
  DrawText(32, width/2, height/2, "You Move Faster");
}
}
class PU_RandomWeapon extends PowerUp{

  ArrayList<Weapon> weapons;

  public PU_RandomWeapon(){
    super();

    weapons = new ArrayList<Weapon>();
    weapons.add(new FastWeapon());
    weapons.add(new StrongWeapon());

    colFill = new PVector(50, 255, 50);
  }

  public void activate(boolean isPlayer2){
    super.activate(isPlayer2);
    //print("Enjoy your new weapon \n");
    //player.weapon = new FastWeapon(1, 0.01);
    if(!isPlayer2)
      player.weapon = weapons.get((int)random(weapons.size()));
    else
      player2.weapon = weapons.get((int)random(weapons.size()));

    //player.weapon.fireRate = 0.01;
  }

  public void deactivate(boolean isPlayer2){
    //test
    super.deactivate(isPlayer2);
    //print("Deactivated power up");

    if(!isPlayer2)
      player.weapon = new Weapon();
    else
      player2.weapon = new Weapon();
    //player.weapon.fireRate = 0.3;
    //RandNum();
  }

  public void Message(){
    DrawText(32, width/2, height/2, "You HAve A New Weapon: ");
  }
}
class PU_Shield extends PowerUp{

  float healthinc;


  public PU_Shield(){
    super();

  }

public void activate(boolean isPlayer2){
 println ("testactive");
super.activate(isPlayer2);

if(!isPlayer2){
  player.shield = true;
}
else{
  player2.shield = true;
}

}

public void deactivate(boolean isPlayer2){
  super.deactivate(isPlayer2);

  if(!isPlayer2){
    player.shield = false;
  }
  else{
    player2.shield = false;
  }
}

public void Message(){
  DrawText(32, width/2, height/2, "You take no damage");
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

  public void receivePowerup(){
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

  public void Shoot(boolean player2){
    weapon.Shoot(this, player2);
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

  boolean hasGeneratedGoal = false;

  float puTimerMax = 5;
  float puTimerCurr;
  float messageTimerMax = 1;
  float messageTimerCurr = messageTimerMax + 1;

  boolean keepDrawing = false;
  float disableTimerMax = 1.5f;
  float diableTimerCurr = 0;

  public PowerUp() {
    numbers = new int [4];
    numbers[0] = 4;
    numbers[1] = 6;
    numbers[2] = 8;
    numbers[3] = 10;
    pos = new PVector();
    colStroke = new PVector(20, 255, 20);
    colFill = new PVector(20, 255, 20);
    pos.x= random(width);
    pos.y= random(height);
    RandNum();
  }
  public void RandNum() {
    // if(puTimerCurr >= puTimerMax){
    //   rand = (int)random(numbers.length);
    //   scoreupdate = (int)score + numbers[rand];
    //   println (scoreupdate);
    //
    //   hasGeneratedGoal = true;
    // }
  }

  public void keepDrawing(){
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

  public void update () {
    RandNum();

    puTimerCurr += (float)1/60;
    if(puTimerCurr >= puTimerMax){
      if(!hasGeneratedGoal){
        time = millis();
        hasGeneratedGoal = true;

        pos.x= random(width);
        pos.y= random(height);
      }

      ellips();
    }

    messageTimerCurr += (float)1/60;
    if(messageTimerCurr < messageTimerMax){
      enabled = true;
    }
  }

  //draws the ellipse
  public void ellips() {
    //print(millis()+":"+(time+3000)+"\n");
    if (millis() < time + 3000) {
      float test = 1f -  ((float)millis() - time)/3000;
      //print(test + "\n");
      r = test * sizemod;
      fill(colFill.x, colFill.y, colFill.z);
      ellipse (pos.x, pos.y, r, r);
      // trufalse = false;
    }
    else{
      hasGeneratedGoal = false;
      puTimerCurr = 0;
    }
    //else if(!hasGeneratedGoal)
  }
  public void activate(boolean isPlayer2){
  //test
    print("Base activate function\n");

    messageTimerCurr = 0;
    if(!isPlayer2)
      player.receivePowerup();
    else
      player2.receivePowerup();
  }

  public void deactivate(boolean isEnemy){
  //test
    print("Base deactivate function\n");
    //time = millis();
    hasGeneratedGoal = false;
    puTimerCurr = 0;
    rand = (int)random(numbers.length);
    puTimerMax = numbers[rand];
    print(puTimerMax + "\n");
  }

  public void Message(){

  }
}
class StrongWeapon extends Weapon{
  float damage;
  float fireRate;

  PVector bulletCol = new PVector(0, 0, 255);

  public StrongWeapon(){
    fireRate = 0.3f;
    damage = 4;
  }

  public StrongWeapon(float damage, float fireRate){

    super(damage, fireRate);

    fireRate = 0.3f;
    damage = 4;

    print("Instantiated new strong weapon\n");
  }

  public void Shoot(Player p, boolean player2){
    float rBullet = 5;
    PVector posBullet = new PVector(p.pos.x + p.size.x, p.pos.y + p.size.y/2 - p.r/2, p.pos.z);
    PVector velBullet = new PVector(10, 10, 10);
    PVector aBullet = new PVector(0, 0, 0);
    PVector colStrokeBullet = new PVector(0, 0, 0);
    PVector colFillBullet = new PVector(0, 255, 0);
    float healthBullet = 1;

    Bullet bullet = new Bullet(posBullet, velBullet, aBullet, colStrokeBullet, colFillBullet, rBullet, healthBullet, true, p.weapon.damage);
    bullet.player2 = player2;
    bullets.add(bullet);
    posBullet = new PVector(p.pos.x + p.size.x, p.pos.y + p.size.y/2 - p.r/2 + 15, p.pos.z);
    bullet = new Bullet(posBullet, velBullet, aBullet, colStrokeBullet, colFillBullet, rBullet, healthBullet, true, p.weapon.damage);
    bullet.player2 = player2;
    bullets.add(bullet);
    posBullet = new PVector(p.pos.x + p.size.x, p.pos.y + p.size.y/2 - p.r/2 - 15, p.pos.z);
    bullet = new Bullet(posBullet, velBullet, aBullet, colStrokeBullet, colFillBullet, rBullet, healthBullet, true, p.weapon.damage);
    bullet.player2 = player2;
    bullets.add(bullet);
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

  public void Shoot(Player p, boolean player2){
    float rBullet = 5;
    PVector posBullet = new PVector(p.pos.x + p.size.x, p.pos.y + p.size.y/2 - p.r/2, p.pos.z);
    PVector velBullet = new PVector(10, 10, 10);
    PVector aBullet = new PVector(0, 0, 0);
    PVector colStrokeBullet = new PVector(0, 0, 0);
    PVector colFillBullet = new PVector(0, 255, 0);
    float healthBullet = 1;

    Bullet bullet = new Bullet(posBullet, velBullet, aBullet, colStrokeBullet, colFillBullet, rBullet, healthBullet, true, p.weapon.damage);
    bullet.player2 = player2;
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
