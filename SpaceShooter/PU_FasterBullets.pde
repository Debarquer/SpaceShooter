class PU_FasterBullets extends PowerUp{

public void spray(){


player.weapon.fireRate = 0.03;




}
void activate() {
  super.activate();

  spray();

}
void deactivate(){

player.weapon.fireRate = 0.3;



}




}
