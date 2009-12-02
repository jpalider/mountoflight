import java.util.Random;

import javax.microedition.lcdui.Graphics;


/**
 * This class represents map in game. Map is divided into layers:
 * 1st layer is a background, created with tile images
 * 2nd layer keeps information of static objects
 * 3rd layer is a list of "living" objects
 * @author mumin
 *
 */
public class MoLMap implements Config {

	int mapWidth;
	int mapHeight;
	
	// no matrix
	int[] layer_1;		// static background
	QObject[] layer_2;	// living objects
	QObject[] layer_3;	// characters
	
	// will be set before each draw
	QObject[] visibleObjects;
	
	
	int tileWidth;
	int tileHeight;
	
	int hTiles;
	int vTiles;
	
	// Map position pointer
	int mx;
	int my;
	
	QView view;
	
	// Map position of center of the view
	// int vmx;
	// int vmy;
	
	MoLGame game;

	public MoLMap(/*MoLGame game,*/ QView view) {

		this.view = view;
		mapWidth = 10*CONFIG_TILE_WIDTH; // too much bootstrapping!
		mapHeight = 10*CONFIG_TILE_HEIGHT;
		tileWidth = CONFIG_TILE_WIDTH; 
		tileHeight = CONFIG_TILE_HEIGHT;
		hTiles =  mapWidth / tileWidth; // too much bootstrapping!
		vTiles =  mapHeight/ tileHeight;
		
		// for test, TODO: reading config from file or Configurator object
		//mx = view.getWidth()>>1;
		//my = view.getHeight()>>1;
	}
	
	public void drawLayer1() {
		
		int cw = view.getWidth();
		int ch = view.getHeight();		
		int xUpperLeftCanvasCorner = mx-(cw>>1);
		int yUpperLeftCanvasCorner = my-(ch>>1);
		int firstTile = findTile(xUpperLeftCanvasCorner, yUpperLeftCanvasCorner);
		int xTile = firstTile % vTiles;
		int yTile = firstTile / hTiles;
		int xOffset = xUpperLeftCanvasCorner - xTile*tileWidth;
		int yOffset = yUpperLeftCanvasCorner - yTile*tileHeight;
		int lastHorizontalTile = findTile(xUpperLeftCanvasCorner+cw, yUpperLeftCanvasCorner);
		int lastVerticalTile = findTile(xUpperLeftCanvasCorner, yUpperLeftCanvasCorner+ch);
		// view.fill(0xFFFFFF);
		int x = 0;
		int y = 0;
		int t = firstTile;
		
		for (int yt = firstTile; yt <= lastVerticalTile; yt+=hTiles) {
			for (int xt=0; xt<=lastHorizontalTile-firstTile; xt++) {
				t=yt+xt;
				// can be sped up by: "if (layer_1[t] != layer_1[t-1]) currImg = getImg(l_1[t])
				view.drawQImage(MoL.midlet.iManager.getImage(layer_1[t]), x-xOffset, y-yOffset);
				t++;
				x+=tileWidth;	
			}
			y+=tileHeight;
			x=0;
		}
	}
	
	public void drawLayer2(Graphics g, int mx, int my) {
		view.fillRect(17,17,17,17,0x44AACC);
	}
	
	public void drawLayer3(Graphics g) {
		view.fillRect(27,27,5,5,0xAA44CC);
	}
	
	int findTile(int x, int y) {
		return hTiles*(y/tileHeight) + (x/tileWidth);
	}
	
	void setLayers(int[] layer1data, QObject[] layer2data, QObject[] layer3data) {
			layer_1 = layer1data;
			layer_2 = layer2data;
			layer_3 = layer3data;
			
			visibleObjects = new QObject[layer_2.length + layer_3.length];
			int total = 0;
			for (int i = 0; i < layer2data.length; i++) {
				visibleObjects[total++] = layer2data[i];
			}
			for (int i = 0; i < layer3data.length; i++) {
				visibleObjects[total++] = layer3data[i];
			}
	}
	
	public void draw() {
		drawLayer1();
		
//		int mwh = mapWidth>>1;
//		int mhh = mapHeight>>1;
		int vwh = view.getWidth()>>1;
		int vhh = view.getHeight()>>1;
		for (int i = 0; i < visibleObjects.length; i++) {
			view.drawQImageCentered(visibleObjects[i].getQImage(), 
					vwh + visibleObjects[i].getX() - mx, 
					vhh + visibleObjects[i].getY() - my);
		}
		//if rain
		drawRain(3, 5, 50);
		//if almost dead
		// drawBlackBird
	}
	
	public void validate() {
		int hh = (view.getHeight()>>1);
		int wh = (view.getWidth()>>1);
		if (mx-wh<0 ) mx = wh;
		if (mx-wh+view.getWidth()>=mapWidth) mx = mapWidth - wh - 1 - view.getWidth()%2;
		if (my-hh<0 ) my = hh;
		if (my-hh+view.getHeight()>=mapHeight) my = mapHeight - (view.getHeight()>>1) - 1 - view.getHeight()%2;
	}
	
	int currentSort;
	QObject objSort;
	public void sortLayers() {
		for (int i = currentSort; i < visibleObjects.length-1; i++) {
			if (visibleObjects[i].getCY() + visibleObjects[i].getY() > visibleObjects[i+1].getCY() + visibleObjects[i+1].getY()) {
				objSort = visibleObjects[i];
				visibleObjects[i] = visibleObjects[i+1];
				visibleObjects[i+1] = objSort;
			}			
		}
		currentSort++;
		if (currentSort<=visibleObjects.length-1)
			currentSort = 0;
		// TODO: 
		// now I believe that if there is few objects

	}
	
	
	public void collideLayers() {
		// for all characters 
		for (int i = 0; i < layer_3.length; i++) {
			// check against each (temporarily) living object
			for (int j = 0; j < layer_2.length; j++) {
				if (layer_3[i].collides(layer_2[j])) {
					((MoLCharacter)layer_3[i]).respondToCollision(((MoLGameObject)layer_2[j]).getCollisionResponse());
				}
			}
		}
	}
	
	/**
	 * To simplify code the rain will fall right downwards ;-)
	 * Parameter ratio instead of an angel.
	 * Length in the y axis. 
	 * Intensity means how many drops are there on the screen.
	 */	
	public void drawRain(int ratio, int length, int intensity) {
		int color = 0x777777;
		int x1, y1, x2, y2;
		int base = (int)(QUtils.getTime() % 1000);
		int xval = 0;
		int yval = 0;
		Random generator = new Random(27+base%4);
		for (int i = 0; i <intensity; i++) {
			xval = generator.nextInt()+(base/4+1);
			yval = generator.nextInt()+(base/4+1);
			x1 = xval%view.getWidth()-ratio;
			y1 = yval%view.getHeight()-length;
			x2 = x1+length/ratio;
			y2 = y1+length;
			view.drawLine(x1, y1, x2, y2, color);			
		}
	}

}
