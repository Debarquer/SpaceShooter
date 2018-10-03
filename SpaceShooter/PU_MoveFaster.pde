class PU_MoveFaster extends PowerUp{

void activate(){

player.vel.y = (player.vel.y) *2;

}

void Message(){

DrawText(32, width/2, height/2, "You Move Faster");

}
}
