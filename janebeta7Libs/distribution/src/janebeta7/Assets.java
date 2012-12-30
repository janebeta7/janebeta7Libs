/*
  Assets utils janebeta7Libs
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
  Esta clase sirve para gestionar todo lo referente con archivos assets utilizados en nuestros
  sketches
 */
package janebeta7;

import java.io.File;
import java.io.FilenameFilter;

public class Assets {
	/**
	 * Constructor
	 * 
	 * @example Colors
	 * @param theParent
	 */
	public Assets() {

	}

	public String[] readFolder(String path) {
		System.out.println("* Assets > path: " + dataPath(path));
		File dir = new File(dataPath(path)+"/");
		String[] children = dir.list(new PicFilter());
		if (children == null) {
			// Either dir does not exist or is not a directory
		} else {
			for (int i = 0; i < children.length; i++) {
				// Get filename of file or directory
				System.out.println("* Assets >  asset[" + i + "] >> "
						+ children[i]);
				children[i] =  dataPath(path)+"/" +children[i];
				
				
			}
		}

		// The list of files can also be retrieved as File objects
		// File[] files = dir.listFiles();
		return children;
		
	}

	/**
	 * Return a full path to an item in the data folder.
	 * <p>
	 * In this method, the data path is defined not as the applet's actual data
	 * path, but a folder titled "data" in the sketch's working directory. This
	 * is because in an application, the "data" folder is exported as part of
	 * the jar file, and it's not as though you're gonna write into the jar file
	 * itself. If you need to get things out of the jar file, you should use
	 * openStream().
	 */
	public String dataPath(String where) {
		String sketchPath = System.getProperty("user.dir");

		// isAbsolute() could throw an access exception, but so will writing
		// to the local disk using the sketch path, so this is safe here.
		if (new File(where).isAbsolute())
			return where;

		return sketchPath + File.separator + "data" + File.separator + where;
	}

	/**
	 * Prepend the sketch folder path to the filename (or path) that is passed
	 * in. External libraries should use this function to save to the sketch
	 * folder.
	 * <p/>
	 * Note that when running as an applet inside a web browser, the sketchPath
	 * will be set to null, because security restrictions prevent applets from
	 * accessing that information.
	 * <p/>
	 * This will also cause an error if the sketch is not inited properly,
	 * meaning that init() was never called on the PApplet when hosted my some
	 * other main() or by other code. For proper use of init(), see the examples
	 * in the main description text for PApplet.
	 */
	public static String sketchPath(String where) {
		String sketchPath = System.getProperty("user.dir");
		if (sketchPath == null) {
			throw new RuntimeException("The applet was not inited properly, "
					+ "or security restrictions prevented "
					+ "it from determining its path.");
		}
		// isAbsolute() could throw an access exception, but so will writing
		// to the local disk using the sketch path, so this is safe here.
		// for 0120, added a try/catch anyways.
		try {
			if (new File(where).isAbsolute())
				return where;
		} catch (Exception e) {
		}

		return sketchPath + File.separator + where;
	}

	public boolean accept(File dir, String name) {
		if (new File(dir, name).isDirectory()) {
			return false;
		}
		name = name.toLowerCase();
		return (name.endsWith(".png") || name.endsWith(".jpg") || name
				.endsWith(".gif"));
	}
/* return true if a file Exist, false if a file not exist*/
	public boolean isFile(String _url) {
		File file = new File(dataPath(_url));
		//System.out.println("* Assets > isFile: " + dataPath(_url));
		boolean exists = file.exists();
		if (!exists)
			return false;
		else
			return true;

	}
}

class PicFilter implements FilenameFilter {

	/**
	 * Select only *.Pic files.
	 * 
	 * @param dir
	 *            the directory in which the file was found.
	 * 
	 * @param name
	 *            the name of the file
	 * 
	 * @return true if and only if the name should be included in the file list;
	 *         false otherwise.
	 */
	public boolean accept(File dir, String name) {
		if (new File(dir, name).isDirectory()) {
			return false;
		}
		name = name.toLowerCase();
		return (name.endsWith(".png") || name.endsWith(".jpg") || name
				.endsWith(".gif"));
	}

}
