import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;


public class QImage {

	Image img;

	public int getHeight() {
		return img.getHeight();
	}
	
	public int getWidth() {
		return img.getWidth();
	}
	
	Image getImage() {
		return img;
	}
	
	public QImage(Image img) {
		this.img = img;
	}
}
