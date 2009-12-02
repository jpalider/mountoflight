import java.io.IOException;
import java.io.InputStreamReader;

import javax.microedition.lcdui.Image;

/**
 * This class helps to manage images. User defines sets of images which are to
 * be available upon request. All images are identified by descriptor. 
 * @author mumin
 *
 */
public class QImageManager  {
	
	QImage[] images;
	int maxImages;

	//inforamtion neccesary to get all images, position(x,y), size(w,h) and location(fn)
	int[] x;
	int[] y;
	int[] w;
	int[] h;
	String[] fn;
	
	String currentfn="";
	Image tmpimg;

	static final String descFilename = "images.dsc";
	

	public QImageManager() {		
		maxImages = readImagePool();
		images = new QImage[maxImages];
	}
	
	void tick() {
		// check every tick n++ image activity, if not in use -> remove and free memory
		// to be implemented
	}
	
	boolean loadImage(int imgDesc) {

		if (currentfn.compareTo(fn[imgDesc])!=0) {
			//if (currentfn != fn[imgDesc]) {	
			// references compared!
			// ???
			currentfn = fn[imgDesc];
			try {
				tmpimg = Image.createImage("/"+currentfn+".png");
			} catch (Exception e) {
			}
		}
		// images[imgDesc].img =  Image.createImage(tmpimg,x[imgDesc],y[imgDesc],w[imgDesc],h[imgDesc],javax.microedition.lcdui.game.Sprite.TRANS_NONE);
		images[imgDesc] = new QImage( Image.createImage(tmpimg,x[imgDesc],y[imgDesc],w[imgDesc],h[imgDesc],javax.microedition.lcdui.game.Sprite.TRANS_NONE) );
		
		return true;
	}

	boolean loadImages(int[] imageList) {
		for (int i = 0; i < imageList.length; i++) {
			if (loadImage(imageList[i]) == false)
				return false;
		}
		return true;
	}

	
	/**
	 * Function allows to access image by its descriptor.
	 * @param desc Image descriptor to access.
	 * @return Image or null if no image can be found corresponding to desc.
	 */
	QImage getImage(int desc) {
		if (images[desc] != null) {
			return images[desc];
		} else {
			// if ( loadImage(desc) )
			// 	return images[desc];
			// else
			return null;
		}
	}
	
	/**
	 * This function reads a file to create a pool of all images available in
	 * application with corresponding location - file, coordinates and size.
	 * 
	 * The structure of entries is as following:
	 * DESCIPTION, filename, x, y, width, height,
	 * All head lines beginning with # are skipped.
	 * Description has no impact on anything, is for user only.
	 * 
	 * TODO: tool for copying DESCRIPTION into java interface.
	 */
	int readImagePool() {
		
		int fileSize = 0;
		InputStreamReader insr = null;
		
		//--- learn size of a file to allocate buffer of correct size
		try {
			insr = new InputStreamReader(getClass().getResourceAsStream(descFilename));
			while(insr.read() != -1) {
				fileSize++;
			}
			insr.close();
		} catch (IOException e) {
			QUtils.debug("ImageManager.readImageList()" + e.toString());
		}
		
		//--- read whole file content
		char[] cbuf = new char[fileSize+1];
		try {
			insr = new InputStreamReader(getClass().getResourceAsStream(descFilename));
			insr.read(cbuf);
			insr.close();
		} catch (IOException e) {
			QUtils.debug("ImageManager.readImageList()" + e.toString());
		}
		String str = new String(cbuf);
		cbuf = null;

		
		//--- all head (head only) comments are removed...
		str = str.substring(str.indexOf('\n',str.lastIndexOf('#'))+1);
		
		int linesCount = 0;
		int tint = 0;
		while((tint = str.indexOf('\n',tint+1))!=-1) {
			linesCount++;
		}
		if (str.length() - str.lastIndexOf('\n') > 5) {  // 6 commas only, solves empty line problem
			linesCount++;
		}
		
		x = new int [linesCount];
		y = new int [linesCount];
		w = new int [linesCount];
		h = new int [linesCount];
		fn= new String [linesCount];
		
		tint = 0;
		int n = 0;
		while(n < linesCount) {
			tint = str.indexOf(',',tint)+1;
			fn[n] = str.substring(tint, str.indexOf(',',tint));
			tint += fn[n].length()+1;
			x[n] = Integer.parseInt( str.substring(tint,str.indexOf(',',tint)) );
			tint = str.indexOf(',',tint)+1;
			y[n] = Integer.parseInt( str.substring(tint,str.indexOf(',',tint)) );
			tint = str.indexOf(',',tint)+1;
			w[n] = Integer.parseInt( str.substring(tint,str.indexOf(',',tint)) );
			tint = str.indexOf(',',tint)+1;
			h[n] = Integer.parseInt( str.substring(tint,str.indexOf(',',tint)) );
			tint = str.indexOf(',',tint)+1;
			n++;
		}
		return linesCount;
	}
	
}
