class PU_Health extends PowerUp{

  float healthinc;
  
  
  public PU_Health(){
    super();
   healthinc = 5;
   health = health + healthinc;
  }
 
 

 

void activate(boolean isPlayer2){
 println ("testactive");
super.activate(isPlayer2);

if(!isPlayer2){
  health = health + healthinc;
}
else{
 health = health + healthinc;
}

}

public void deactivate(boolean isPlayer2){
  super.deactivate(isPlayer2);

  if(!isPlayer2){
   health = health ;
  }
  else{
   health = health ;
  }
}

void Message(){
  DrawText(32, width/2, height/2, "5 Health points");
}
}
