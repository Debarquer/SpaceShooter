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