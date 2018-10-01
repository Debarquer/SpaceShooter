public boolean BulletEnemyCollision(Bullet bullet, Enemy enemy){
  // if(bullet.pos.x + bullet.r > enemy.pos.x && bullet.pos.x < enemy.pos.x + enemy.r && bullet.pos.y + bullet.r > enemy.pos.y && bullet.pos.y < enemy.pos.y + enemy.r){
  //   return true;
  // }

  return dist(bullet.pos.x, bullet.pos.y, enemy.pos.x, enemy.pos.y) < bullet.r + enemy.r;
}

public boolean BulletPlayerCollision(GameObject bullet, Player player){
  return dist(bullet.pos.x, bullet.pos.y, player.pos.x, player.pos.y) < bullet.r + player.size.x;
}
