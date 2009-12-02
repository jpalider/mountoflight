import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;


public class QView {
	
	private Image img;			// current data to draw
	private Graphics g;			// img's Graphics object
	
//	private int x;				// these describe current
//	private int y;				// clip region which will
//	private int w;				// be drawn on Graphics'
//	private int h;				//
	
	private int width;			// initial size
	private int height;			//
	
	public QView(int w, int h) {
//		this.x = 0;
//		this.y = 0;
//		this.w = width = w;
//		this.h = height = h;
		width = w;
		height = h;		
		try {
			img = Image.createImage(w, h);			
			g = img.getGraphics();
		} catch (Exception e) {
			QUtils.debug("QView.QView() failed.");
		}
	}
	
	public void drawQImage(QImage qimg, int x, int y) {
		g.drawImage(qimg.getImage(), x, y, 0);
	}
	
	public void drawQImage(QImage qimg, int x, int y, int anchor) {
		g.drawImage(qimg.getImage(), x, y, anchor);
	}
	
	public void drawQImageCentered(QImage qimg, int x, int y) {
		g.drawImage(qimg.getImage(), x, y, Graphics.VCENTER|Graphics.HCENTER);
	}
	
//	public void drawRawCentered(QImage qimg, int x, int y) {
//		g.drawImage(qimg.getImage(), x, y, Graphics.VCENTER|Graphics.HCENTER);
//	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setClip(int x, int y, int width, int height) {
		g.setClip(x, y, width, height);
	}
	
	public void resetClip() {
		g.setClip(0,0,width,height);
	}
	
	public void fillRect(int x, int y, int w, int h, int c) {
		g.setColor(c);
		g.fillRect(x, y, w, h);
	}
	
	public void drawRect(int x, int y, int width, int height, int c ) {
		g.setColor(c);
		g.drawRect(x, y, width, height); 
	}
	
	public void drawLine(int x1, int y1, int x2, int y2, int c) {
		g.setColor(c);
		g.drawLine(x1, y1, x2, y2);
	}
	
	/**
	 * Fills current clip region with desired color
	 * @param color
	 */
	public void fill(int color) {
		fillRect(0, 0, width, height, color);
	}
	
	
	public void draw(int xpos, int ypos) {
		g.setClip(xpos, ypos, width, height);
		g.drawImage(img, xpos, ypos, 0);
	}
	
	public Image getImage() { 
		return img;
	}
}
