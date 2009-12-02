/************************************
 * 
 * MIDlet extension.
 * 
 * @author Jakub Palider
 * 
 */

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.lcdui.Display;

public class MoL extends javax.microedition.midlet.MIDlet {
	
	static public MoLCanvas canvas;
	static public MoL midlet;
	boolean inited = false;
	QImageManager iManager;
	
	
	public MoL() {
		midlet = this;
		canvas = new MoLCanvas(this);
		canvas.setFullScreenMode(true);
		iManager = new QImageManager();
		iManager.readImagePool();
	}
	
	public void startApp() {
		Display.getDisplay(this).setCurrent(canvas);
		if (inited == false) {
			inited = true;
			new Thread(canvas).start();
		}
	}
	
	public void pauseApp() {
		
	}
	
	protected void destroyApp(boolean d) 
	//throws MIDletStateChangeException
	{
		canvas = null;
		System.out.println("Destroying application...");
	}

}
