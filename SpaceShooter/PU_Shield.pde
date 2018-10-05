class PU_Shield extends PowerUp{

  float healthinc;
  
  
  public PU_Shield(){
    super();

  }
 
 

 

void activate(boolean isPlayer2){
 println ("testactive");
super.activate(isPlayer2);

if(!isPlayer2){

}
else{
 
}

}

public void deactivate(boolean isPlayer2){
  super.deactivate(isPlayer2);

  if(!isPlayer2){
 
  }
  else{

  }
}

void Message(){
  DrawText(32, width/2, height/2, "You take no damage");
}
}
