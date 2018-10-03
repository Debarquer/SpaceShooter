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
