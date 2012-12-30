import janebeta7.*;

Colors paletas;

float a = 0.0;
float s = 0.0;
void setup(){
  size (500,500);
  background(0);
  paletas = new Colors(this);
  smooth();
  paletas.osc(5555);

}

void draw(){
  a = a + 0.04;
  s = cos(a)*2;
  //paletas.setPalette();
  fill(paletas.getColor());
  ellipse(random(width),random(height),10,10);
  pushMatrix();
  translate(width/2, height/2);
  scale(s); 

  fill(paletas.getColor());
  ellipse(0,0,100,100);
  popMatrix();
}










