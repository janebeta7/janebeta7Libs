/*
  Colors janebeta7Libs library for processing
  2010 (c) copyright
  
  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General
  Public License along with this library; if not, write to the
  Free Software Foundation, Inc., 59 Temple Place, Suite 330,
  Boston, MA  02111-1307  USA
  
  DESCRIPTION:
 */
package janebeta7;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.*;
import processing.core.PApplet;

/**
 * 
 * 
 * @author Alba G.Corral
 * 
 *         TO DO LIST:
 * 
 *         -crear un array de arrays para que el processPalette solo se ejecute
 *         cuando se cargue la nueva paleta
 * 
 *         -add api colour lovers
 *         - a–adir un metodo publico que me devuelva el ArrayList con todas las rutas de las paletas
 */

public class Colors {

	PApplet myParent;
	private int MAX_PALETTES = 100;

	private int numColors = 0;
	private int numPalettes = 0;
	private boolean isOrdenados = false;
	private boolean isApplet = false;
	private Color[] imageColors = new Color[MAX_PALETTES];
	private int paletaActiva = 0;
	private int colorActivo = 0;
	private String[] listado;
	private static String pathurl;
	private Assets oAssets;
	private ArrayList<String> thisList ;
	public final String VERSION = "0.1.7";
	public Osc osc;
	private boolean isOsc = false;

	/**
	 * Constructor
	 * 
	 * @example Colors
	 * @param theParent
	 */
	public Colors(PApplet theParent) {
		myParent = theParent;
		listado = new String[MAX_PALETTES];
		oAssets = new Assets();
		thisList = new  ArrayList<String>( ) ;
		System.out.println("Colors >  janebeta7Libs " + VERSION);
		
	}
	public void osc(int port){
		isOsc = true;
		osc = new Osc(port);
	}
	/**
	 * take the colors
	 * 
	 * @return void
	 */
	void processPalette(String fn) {
		
		System.out.println("Colors > get color :" + fn);
		BufferedImage img = null;
		try {
			if (isApplet)
				img = ImageIO.read(new File(fn));
			else
			{
				
				img = ImageIO.read(new File((fn)));
			}
			
			//introducimos paleta al listado ArrayList
			
		} catch (IOException e) {
			System.out.println("* Colors>  no image found:" + (fn));
		}

		int Width = img.getWidth(null);
		int Height = img.getHeight(null);
		numColors = 0;
		for (int x = 0; x < Width; x++) {

			for (int y = 0; y < Height; y++) {
				int c = img.getRGB(x, y);
				int red = (c & 0x00ff0000) >> 16;
				int green = (c & 0x0000ff00) >> 8;
				int blue = c & 0x000000ff;
				// and the Java Color is ...
				Color color = new Color(red, green, blue);
				// System.out.println ("Colores >> -----color: "+color);
				boolean exists = false;

				for (int n = 0; n < numColors; n++) {

					if (color.getRGB() == imageColors[n].getRGB()) {
						exists = true;
						break;
					}
				}
				if (!exists) {
					// add color to pal
					if (numColors < MAX_PALETTES) {
						imageColors[numColors] = color;
						// System.out.println ("color "+numColors+" "+color);
						numColors++;
					}
				}
			}
		}
		System.out.println("Colors> > -----numColors: " + numColors);
	}

	// aniade una imagen al set de Colores
	public void addPaleta(String _path) {
		System.out.println("CURRENT DIR: " + System.getProperty("user.dir"));
		System.out.println("SYSTEM DIR : " + System.getProperty("user.home")); // ex.
																				// c:\windows
																				// on
																				// Win9x
		boolean exists = (new File(_path)).exists();
		if (exists) {
			// File or directory exists
			System.out.println("Colors > addPaleta_applet: " + _path
					+ " numPalettes:" + numPalettes);
			listado[numPalettes] = _path;
			isApplet = true;
			processPalette(listado[paletaActiva]);
			
				thisList.add(listado[paletaActiva]);
		
			numPalettes++;
		} else {
			// File or directory does not exist
			System.out.println("Colors > addPaleta_applet: " + _path
					+ "NO EXISTE");
		}

		// System.out.println("*******Colors > addPaleta_applet: total paletas:"+numPalettes);
	}

	
	// añade las imagenes de un directorio al set de Colores
	public void addFolder(String _path) {
		pathurl = oAssets.dataPath(_path);
		System.out.println("Colors >  dataPath" + pathurl);
		listado = oAssets.readFolder(_path);
		numPalettes = listado.length;
		
		for (int i = 0; i < numPalettes; i++) {
			thisList.add(listado[i]);
		}
		isApplet = false;
		System.out.println("* Colors > numPalettes:" + numPalettes);
		processPalette(listado[paletaActiva]);
		
	}

	// inicia una paleta de colores aleatoria del listado.
	public void setPalette() {
		paletaActiva = random(numPalettes, 0);
		String rutaRandom = listado[paletaActiva];
		System.out.println("*******Colors > random paleta: " + rutaRandom);
		processPalette(listado[paletaActiva]);
	}

	// inicia una paleta de colores numero num del listado.
	public void setPalette(int num) {
		paletaActiva = num;
		System.out.println("Colors >  paleta" + listado[num]);
		processPalette(listado[num]);
	}

	// inicia una paleta de colores numero num del listado.
	public int getCount() {
		return numPalettes;
	}

	// funcion que devuelve un color aleatorio de la paleta cargada
	// dinamicamente
	public int getColor() {
		if (!isOsc)
		return imageColors[random(numColors, 0)].getRGB();
		else
		return osc.getOsc();
	}

	public int getColor(int n) {
		int index = n;
		if (n > getLength())
			index = n;

		return imageColors[index].getRGB();
	}

	public int getOrderColor() {
		Color devolvemos = imageColors[colorActivo];
		colorActivo++; // aumentamos
		if (colorActivo >= numColors)
			colorActivo = 0;
		return devolvemos.getRGB();
	}

	/* devuelve el numero de colores de la paleta activa */
	public int getLength() {
		return numColors;
	}

	public void setOrdenados() {
		isOrdenados = !isOrdenados;
	}

	private static int random(int max, int min) {
		return (int) (Math.random() * (max - min)) + min;
	}

	/**
	 * Blend two colors.
	 * 
	 * @param color1
	 *            First color to blend.
	 * @param color2
	 *            Second color to blend.
	 * @param ratio
	 *            Blend ratio. 0.5 will give even blend, 1.0 will return color1,
	 *            0.0 will return color2 and so on.
	 * @return Blended color.
	 */
	public static Color blend(Color color1, Color color2, double ratio) {
		float r = (float) ratio;
		float ir = (float) 1.0 - r;

		float rgb1[] = new float[3];
		float rgb2[] = new float[3];

		color1.getColorComponents(rgb1);
		color2.getColorComponents(rgb2);

		Color color = new Color(rgb1[0] * r + rgb2[0] * ir, rgb1[1] * r
				+ rgb2[1] * ir, rgb1[2] * r + rgb2[2] * ir);

		return color;
	}

	/**
	 * Make an even blend between two colors.
	 * 
	 * @param color1
	 *            First color to blend.
	 * @param color2
	 *            Second color to blend.
	 * @return Blended color.
	 */
	public static Color blend(Color color1, Color color2) {
		return ColorUtil.blend(color1, color2, 0.5);
	}

	/**
	 * Make a color darker.
	 * 
	 * @param color
	 *            Color to make darker.
	 * @param fraction
	 *            Darkness fraction.
	 * @return Darker color.
	 */
	public static Color darker(Color color, double fraction) {
		int red = (int) Math.round(color.getRed() * (1.0 - fraction));
		int green = (int) Math.round(color.getGreen() * (1.0 - fraction));
		int blue = (int) Math.round(color.getBlue() * (1.0 - fraction));

		if (red < 0)
			red = 0;
		else if (red > 255)
			red = 255;
		if (green < 0)
			green = 0;
		else if (green > 255)
			green = 255;
		if (blue < 0)
			blue = 0;
		else if (blue > 255)
			blue = 255;

		int alpha = color.getAlpha();

		return new Color(red, green, blue, alpha);
	}

	/**
	 * Make a color lighter.
	 * 
	 * @param color
	 *            Color to make lighter.
	 * @param fraction
	 *            Darkness fraction.
	 * @return Lighter color.
	 */
	public static Color lighter(Color color, double fraction) {
		int red = (int) Math.round(color.getRed() * (1.0 + fraction));
		int green = (int) Math.round(color.getGreen() * (1.0 + fraction));
		int blue = (int) Math.round(color.getBlue() * (1.0 + fraction));

		if (red < 0)
			red = 0;
		else if (red > 255)
			red = 255;
		if (green < 0)
			green = 0;
		else if (green > 255)
			green = 255;
		if (blue < 0)
			blue = 0;
		else if (blue > 255)
			blue = 255;

		int alpha = color.getAlpha();

		return new Color(red, green, blue, alpha);
	}

	/**
	 * Return the hex name of a specified color.
	 * 
	 * @param color
	 *            Color to get hex name of.
	 * @return Hex name of color: "rrggbb".
	 */
	public static String getHexName(Color color) {
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();

		String rHex = Integer.toString(r, 16);
		String gHex = Integer.toString(g, 16);
		String bHex = Integer.toString(b, 16);

		return (rHex.length() == 2 ? "" + rHex : "0" + rHex)
				+ (gHex.length() == 2 ? "" + gHex : "0" + gHex)
				+ (bHex.length() == 2 ? "" + bHex : "0" + bHex);
	}

	/**
	 * Return the "distance" between two colors. The rgb entries are taken to be
	 * coordinates in a 3D space [0.0-1.0], and this method returnes the
	 * distance between the coordinates for the first and second color.
	 * 
	 * @param r1
	 *            , g1, b1 First color.
	 * @param r2
	 *            , g2, b2 Second color.
	 * @return Distance bwetween colors.
	 */
	public static double colorDistance(double r1, double g1, double b1,
			double r2, double g2, double b2) {
		double a = r2 - r1;
		double b = g2 - g1;
		double c = b2 - b1;

		return Math.sqrt(a * a + b * b + c * c);
	}

	/**
	 * Return the "distance" between two colors.
	 * 
	 * @param color1
	 *            First color [r,g,b].
	 * @param color2
	 *            Second color [r,g,b].
	 * @return Distance bwetween colors.
	 */
	public static double colorDistance(double[] color1, double[] color2) {
		return ColorUtil.colorDistance(color1[0], color1[1], color1[2],
				color2[0], color2[1], color2[2]);
	}

	/**
	 * Return the "distance" between two colors.
	 * 
	 * @param color1
	 *            First color.
	 * @param color2
	 *            Second color.
	 * @return Distance between colors.
	 */
	public static double colorDistance(Color color1, Color color2) {
		float rgb1[] = new float[3];
		float rgb2[] = new float[3];

		color1.getColorComponents(rgb1);
		color2.getColorComponents(rgb2);

		return ColorUtil.colorDistance(rgb1[0], rgb1[1], rgb1[2], rgb2[0],
				rgb2[1], rgb2[2]);
	}

	/**
	 * Check if a color is more dark than light. Useful if an entity of this
	 * color is to be labeled: Use white label on a "dark" color and black label
	 * on a "light" color.
	 * 
	 * @param r
	 *            ,g,b Color to check.
	 * @return True if this is a "dark" color, false otherwise.
	 */
	public static boolean isDark(double r, double g, double b) {
		// Measure distance to white and black respectively
		double dWhite = ColorUtil.colorDistance(r, g, b, 1.0, 1.0, 1.0);
		double dBlack = ColorUtil.colorDistance(r, g, b, 0.0, 0.0, 0.0);

		return dBlack < dWhite;
	}

	/**
	 * Check if a color is more dark than light. Useful if an entity of this
	 * color is to be labeled: Use white label on a "dark" color and black label
	 * on a "light" color.
	 * 
	 * @param color
	 *            Color to check.
	 * @return True if this is a "dark" color, false otherwise.
	 */
	public static boolean isDark(Color color) {
		float r = color.getRed() / 255.0f;
		float g = color.getGreen() / 255.0f;
		float b = color.getBlue() / 255.0f;

		return isDark(r, g, b);
	}

	
	/* devuelve en un ArrayList todas los archivos que componen las paletas de colores
	 * esto nos sirve por ejemplo para llamarlo desde Processing y poder hacer un display de los mismos
	 * por pantalla
	 * valorar si pasar el Listado de String o el Listado de PImage (casi mejor esto)
	 * */
	public ArrayList<String> getList(){
		
		return thisList;
		
		
	}
	/* devuelve el numero de paletas*/
	public int getNum(){
		
			
		return thisList.size();
	}
	/* devuelve un objeto Imagen segun el numero de id del arrayList, si no existe devuelve Null o si no
	 * se puede leer o hay algun problema
	 * 
	 */
	public BufferedImage getImage(int numero ){
		String fn = listado[numero];
		try {
			BufferedImage img;
			if (isApplet)
				img = ImageIO.read(new File(fn));
			else
			{
				
				img = ImageIO.read(new File((fn)));
			}
			return img;
		} catch (IOException e) {
			System.out.println("* Colors - getImage>  no image found:" + (fn));
			return null;
		}
	}
	
}
