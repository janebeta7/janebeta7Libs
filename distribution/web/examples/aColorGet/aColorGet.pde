import janebeta7.*;



Colors paletas;
float a = 0.0;
float s = 0.0;
void setup() {
  size (800, 500);
  background(0);
  paletas = new Colors(this);
  smooth();
  paletas.addFolder(dataPath("set/"));
  // println("<<<<getNum >>>>:"+paletas.getNum()); //numero de paletas
  drawPaletas();
}
void drawPaletas() {
  int y = 10;
  int x  =0;
  int w = 50;
  int h = 50;
  int modY = 9;
  int spacew = 20;
  int spaceh = 20;
  int strokeH = 5;
  ArrayList listaPaletas = paletas.getList();
  for (int i=0;i<paletas.getNum() ;i++) { //reccorremos el arrayList de las paletas

    println("******:"+listaPaletas.get(i));
    PImage img = loadImage(listaPaletas.get(i).toString());
    fill(255);
    if ( i % modY ==0) { 
      y = y+h+spaceh; 
      x = 0;
    }
    imageMode(CENTER);
    rectMode(CENTER);
    x = x + w+spacew;
    rect(x, y, w+strokeH, h+strokeH);
    image(img, x, y, w, h);
    
  }
}
void draw() {
}
void mousePressed()
{
  paletas.setPalette();
}







