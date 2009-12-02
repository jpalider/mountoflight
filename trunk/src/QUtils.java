import javax.microedition.lcdui.Graphics;

/**
 * It is a class with a set of various useful functions.
 * 
 * @author Jakub Palider, 14 Apr 2008
 * @version 1.0
 * 
 * TODO: implement drawing for MIDP 1.0
 * 
 */

public class QUtils {
	
// Maybe in the future...	
//	public void drawProgressBar(Graphics g, Image imgBG, Image imgFG, int x, int y, int w, int h,  int border) {
//		
//	}
	
	/**
	 * Function drawing a progress bar. The progress is written from left to right, from bottom upwards.
	 * @param g the Graphics object to be used for rendering the bar
	 * @param horizontal Tells the function whether to draw a horizontal or a vertical bar. True for horizontal.
	 * @param progress current progress
	 * @param colorBG color of a bar when progress is 0% 
	 * @param colorFG color of a bar when progress is 100% 
	 * @param x the x coordinate of the bar to be drawn
	 * @param y the x coordinate of the bar to be drawn
	 * @param width width of a bar
	 * @param height height of a bar 
	 * @param frameWidth
	 * @param frameColor
	 */

	static public void drawProgressBar(Graphics g, int progress, boolean horizontal, int colorBG, int colorFG, int x, int y, int width, int height, int frameWidth, int frameColor) {

		if (width==0 || height==0 || g==null || progress<0 || (frameWidth<<1)>=x || (frameWidth<<1)>=y)
			return;		
		if (progress > 100)
			progress = 100;
		int clipX = g.getClipX();
		int clipY = g.getClipY();
		int clipW = g.getClipWidth();
		int clipH = g.getClipWidth();
		int color = g.getColor();
		
		g.setClip(x, y, width, height);
		if (frameWidth != 0) {
			g.setColor(frameColor);
			g.fillRect(x, y, width, height);
		}
		g.setColor(colorBG);
		g.fillRect(x+frameWidth, y+frameWidth, width-(frameWidth<<1), height-(frameWidth<<1));	
		g.setColor(colorFG);
		if (horizontal) {
			g.fillRect(x+frameWidth, y+frameWidth, width*progress/100-(frameWidth<<1), height-(frameWidth<<1));
		} else {
			g.fillRect(x+frameWidth, y+frameWidth + height - height*progress/100, width-(frameWidth<<1), height*progress/100-(frameWidth<<1));
		}
		
		g.setClip(clipX, clipY, clipW, clipH);
		g.setColor(color);
	}	

	
	/**
	 * Causes the currently executing thread to sleep for the specified number of milliseconds.
	 * @param millis the length of time to sleep in milliseconds.
	 */
	static public void msleep(long millis){
		try{ Thread.sleep(millis); }catch(Exception e){}
	}
	
	/**
	 * Causes the currently executing thread to sleep for the specified number of seconds.
	 * @param seconds the length of time to sleep in seconds.
	 */
	static public void ssleep(long seconds){
		try{ Thread.sleep(seconds * 1000); }catch(Exception e){}
	}
	
	/**
	 * Returns the current time in milliseconds.
	 * @return the difference, measured in milliseconds, between the current time and midnight, January 1, 1970 UTC.
	 */
	static public long getTime() {
		return System.currentTimeMillis();
	}

	static public void debug(String s) {
		System.out.println(s);
	}
	
	static public void drawFrame(QView view, QImageManager im, int x, int y, int width, int height, int ul, int ur, int bl, int br, int u, int b, int l, int r, int bg, int color) {
		
		int cx = x;
		int cy = y;
		int wh = width>>1;
		int hh = height>>1;
		int bs = im.getImage(ul).getWidth();
		int i = 0;
		/*
		 * Frame width/height shall not be less than borders' size 
		 */
		view.drawQImage(im.getImage(ul), cx-wh-bs, cy-hh-bs);
		view.drawQImage(im.getImage(ur), cx+wh, cy-hh-bs);
		view.drawQImage(im.getImage(bl), cx-wh-bs, cy+hh);
		view.drawQImage(im.getImage(br), cx+wh, cy+hh);
		//top
		view.setClip(cx-wh, cy-hh-bs, width, bs);
		i = 0;
		while(i*im.getImage(u).getWidth() < width) {
			view.drawQImage(im.getImage(u), cx-wh+i*im.getImage(u).getWidth(), cy-hh-bs);
			i++;
		}
		//bottom
		view.setClip(cx-wh, cy+hh, width, bs);
		i = 0;
		while(i*im.getImage(b).getWidth() < width) {
			view.drawQImage(im.getImage(b), cx-wh+i*im.getImage(b).getWidth(), cy+hh);
			i++;
		}
		//left
		view.setClip(cx-wh-bs, cy-hh, bs, height);
		i=0;
		while(i*im.getImage(l).getHeight() < height) {
			view.drawQImage(im.getImage(l), cx-wh-bs, cy-hh+i*im.getImage(l).getHeight());
			i++;
		}
		//right
		view.setClip(cx+wh, cy-hh, bs, height);
		i=0;
		while(i*im.getImage(r).getHeight() < height) {
			view.drawQImage(im.getImage(r), cx+wh, cy-hh+i*im.getImage(r).getHeight());
			i++;
		}
		//fill
		view.setClip(cx-wh, cy-hh, width, height);
		i = 0;
		if ( bg != -1 ) {
			while(i*im.getImage(bg).getHeight() < height) {
				int j = 0;
				while(j*im.getImage(bg).getWidth() < width) {
					view.drawQImage(im.getImage(bg), cx-wh+j*im.getImage(bg).getWidth(), cy-hh+i*im.getImage(bg).getHeight());
					j++;
				}
				i++;
			}
		} else {
			view.fillRect(cx-wh, cy-hh, width, height, color);
		}

		view.resetClip();
		
	}
		
}
