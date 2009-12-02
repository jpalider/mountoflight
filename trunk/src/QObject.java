import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class QObject implements QConstants{

	private int id;							// needs for identifying some objects, default 0
	private int x, y;						// location in the space of the center of the object
	private int cx, cy;						// collision center of an object relative to location!!
	private int radius; 					// for collision detection
	private int radiuspow;					// radius^2
	private int width;
	private int height;
	private int ax, ay;						// center of active area
	private int aradiuspow;					// for action detection
	private boolean living;					// if other object can move it / act on it / respond to collision
	private QImage img;						// default object image, should suffice for most objects
											// image size may imply collision area
	private QImage altimg;					// image to be displayed when asked
	private int collision;					// type of collision this object prefers, depends on 
											// radius this can be explicitly overridden by setting
											// collision parameter in detection method
	private boolean animate;				// don't animate by default 
	private Hashtable animations;			//
	private QAnimation currentAnimation;	//
	private int px;							// previously updated coordinates
	private int py;							//
	private long pt;						//
	private int af;							// animation frame?

	
	public QObject(int id, QImage img, int x, int y, int cx, int cy, int width, int height, int ax, int ay, int aradius, boolean living, int collisionType) {

		this.id = id;
		this.img = img;
		this.x = x;
		this.y = y;
		this.cx = cx;
		this.cy = cy;
		this.px = x;
		this.py = y;
		this.living = living;
		this.id = id;
		this.aradiuspow = aradius*aradius;
		animations = new Hashtable();
		collision = collisionType;
		switch (collision) {
			case COLLISION_CIRCLE:
				if ( img!=null && (width==0 || height==0) ) {
					this.radius = (img.getWidth()>>1) + (img.getHeight()>>1);
				} else {
					this.radius = width>>1 + height>>1;
				}
				radiuspow = radius*radius;
				break;
			case COLLISION_RECTANGLE:
				if ( img!=null && (width==0 || height==0) ) {
					this.width = img.getWidth();
					this.height = img.getHeight();
				} else {
					this.width = width;
					this.height = height;
				}
				break;
			case COLLISION_TRANSPARENT:
				this.width = width;
				this.height = height;
				break;
			default:
				
		}

	}

	
	public void setCoordinates(int x, int y) {
		setX(x);
		setY(y);
	}
	
	public void dX(int dx) {
		setX(this.x + dx);
	}
	
	public void dY(int dy) {
		setY(this.y + dy);
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getID() {
		return id;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getCX() {
		return cx;
	}
	
	public int getCY() {
		return cy;
	}

	public int getPX() {
		return px;
	}


	public void setPY(int px) {
		this.px = px;
	}


	public int getPy() {
		return py;
	}


	public void setPy(int py) {
		this.py = py;
	}


	public int getadX() {
		return  Math.abs(x-px);
	}
	
	public int getadY() {
		return Math.abs(y-py);
	}
	
	public long getadT() {
		return Math.abs(QGame.getRealTime()-pt);
	}
	
	public void updatePrevAnimData() {
		this.px = x;
		this.py = y;
		this.pt = QGame.getRealTime();
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getRadius() {
		return radius;
	}
	
	public int getRadiusPow() {
		return radiuspow;
	}
	
	public int getCollisionType() {
		return collision;
	}
	
	public boolean collides(QObject obj) {
		
		int collisionType = COLLISION_CIRCLE;
		
		if (collision == obj.getCollisionType()) // the same type of shapes,
			collisionType = collision;			// either COLLISION_RECTANGLE or COLLISION_CIRCLE
		else if (collision == COLLISION_CIRCLE && obj.getCollisionType()==COLLISION_RECTANGLE) 
			collisionType = COLLISION_CIRCLE_AGAINST_RECTANGLE;
		else if (collision == COLLISION_RECTANGLE && obj.getCollisionType()==COLLISION_CIRCLE) 
			collisionType = COLLISION_RECTANGLE_AGAINST_CIRCLE;
		else if (collision == COLLISION_TRANSPARENT || obj.getCollisionType()==COLLISION_TRANSPARENT)
			collisionType = COLLISION_TRANSPARENT;
		
	
		return collides(obj, collisionType);
	}
	
	// checks if this object collides with another, various types of checking is provided
	private boolean collides(QObject obj, int collisionType) {
		int ox = obj.getX() + obj.getCX();
		int oy = obj.getY() + obj.getCY();
		int ohw = obj.getWidth()>>1;
		int ohh = obj.getHeight()>>1;
		int or2 = obj.getRadiusPow();
		int hh = getHeight()>>1;
		int hw = getWidth()>>1;
		int cx = this.x + this.cx;
		int cy = this.y + this.cy;
		
		switch (collisionType) {
			case COLLISION_TRANSPARENT:
				return false;
		
			case COLLISION_RECTANGLE:
				// four corners of this object checked against object rectangle 
				//
				// +----------+
				// |          | 
				// +----------+  +----+
				//               |    |
				//               +----+
				//
				if ( cx-hw>=ox-ohw && cx-hw<=ox+ohw && cy-hh>=oy-ohh && cy-hh<=oy+ohh || 	// upper left
					 cx+hw>=ox-ohw && cx+hw<=ox+ohw && cy-hh>=oy-ohh && cy-hh<=oy+ohh ||	// upper right
					 cx-hw>=ox-ohw && cx-hw<=ox+ohw && cy+hh>=oy-ohh && cy+hh<=oy+ohh || 	// bottom left
					 cx+hw>=ox-ohw && cx+hw<=ox+ohw && cy+hh>=oy-ohh && cy+hh<=oy+ohh )		// bottom right
					return true;
				
			case COLLISION_CIRCLE:
				// this circle against object circle
				//   ___
				//  / . \
				//  \___/ ___
				//       / . \
				//       \___/
				//
				
				if ( (cx-ox)*(cx-ox) + (cy-oy)*(cy-oy) < or2+radiuspow)
					return true;
				
				
			case COLLISION_CIRCLE_AGAINST_RECTANGLE:
				// four corners of object rectangle against this circle
				//   ___                 ___
				//  / . \ ul            / . \ ur
				//  \___/ +-----------+ \___/
				//        |     o     |
				//        |    inside |
				//   ___  +-----------+  ___
				//  / . \               / . \
				//  \___/bl             \___/br
				//

				if( (ox-ohw-cx)*(ox-ohw-cx) + (oy-ohh-cy)*(oy-ohh-cy) <= radiuspow ||	// upper left
					(ox+ohw-cx)*(ox+ohw-cx) + (oy-ohh-cy)*(oy-ohh-cy) <= radiuspow ||	// upper right
					(ox-ohw-cx)*(ox-ohw-cx) + (oy+ohh-cy)*(oy+ohh-cy) <= radiuspow ||	// bottom left
					(ox+ohw-cx)*(ox+ohw-cx) + (oy+ohh-cy)*(oy+ohh-cy) <= radiuspow ||	// bottom right
					cx>=ox-ohw && cx<=ox+ohw && cy>=oy-ohh && cy<=oy+ohh )				// inside rectangle
				{
					return true;
				}
				
			case COLLISION_RECTANGLE_AGAINST_CIRCLE:
				// there is no difference between checking a circle against
				// rectangle and rectangle against circle in real world ;-)
				// but I would have to switch objects to do that so I've 
				// decided to have another collision case.
				//
				//     +-------+
				//     |   o   |
				//     +-------+   ___ 
				//                / . \
				//                \___/
				
				if( (cx-hw-ox)*(cx-hw-ox) + (cy-hh-oy)*(cy-hh-oy) <= or2 ||	// upper left
					(cx+hw-ox)*(cx+hw-ox) + (cy-hh-oy)*(cy-hh-oy) <= or2 ||	// upper right
					(cx-hw-ox)*(cx-hw-ox) + (cy+hh-oy)*(cy+hh-oy) <= or2 ||	// bottom left
					(cx+hw-ox)*(cx+hw-ox) + (cy+hh-oy)*(cy+hh-oy) <= or2 ||	// bottom right
					ox>=cx-hw && ox<=cx+hw && oy>=cy-hh && oy<=cy+hh )		// inside rectangle
					return true;
		}
		return false;
	}
	
	public boolean action(QObject obj) {
		// circle detection
		int ox = obj.getCX();
		int oy = obj.getCY();
		int or2 = obj.getRadiusPow();
		if (or2 == 0 || aradiuspow == 0)
			return false;
		if ( (ax-ox)*(ax-ox) + (ay-oy)*(ay-oy) < or2+aradiuspow)
			return true;
		return false;
	}
	
	public void act() {
		
	}
	
/*	This would be more OO elegant but I don't like it */
	// The object knows where to draw itself  - does it?
	public void draw(QView v) { //, int mx, int my) {
		if (img != null) {
			v.drawQImageCentered(img, x, y);
		} else {
			QUtils.debug("Nothing to draw");
		}
	}
	
	// The caller decides where to draw this object. Due to canvas <-> map coordinates mismatch
	public void draw(Graphics g, int mx, int my) {
		if (img != null) {
			//g.drawImage(img, cx-(img.getWidth()>>1), cy-(img.getHeight()>>1), 0);
			//g.drawImage(img, cx, cy, Graphics.VCENTER|Graphics.HCENTER);
		} else {
			QUtils.debug("Nothing to draw");
		}
		
	}

	public QImage getQImage() {
		return img;
	}
	
	public QImage getAltQImage() {
		return altimg;
	}
	
	public void tick() {
		if (animate) {
			currentAnimation.update(this);
			img = currentAnimation.getCurentFrame();
		}
	}
	
	/**
	 * Adds an animation to the object
	 * @param animationIdentifier
	 * @param animation
	 * @return
	 */
	public boolean addAnimation(int animationIdentifier, QAnimation animation) {
		try {
			animations.put(new Integer(animationIdentifier),animation);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/**
	 * This method should be used when occurs and object's state change
	 * that results in different animation. 
	 * @param animation Desired animation.
	 */
	public void setCurrentAnimation(QAnimation animation) {
		currentAnimation = animation;
		currentAnimation.currentFrame = 0;
	}
	
	/**
	 * A method that sets an animation by its descriptor.
	 * @param animationIdentifier
	 */
	public void setCurrentAnimation(int animationIdentifier) {
		setCurrentAnimation( (QAnimation)animations.get(new Integer(animationIdentifier)) );
	}
	
	/**
	 * Method to force an animation to be performed by the object.
	 * @param animation Desired animation.
	 */
	public void setAnimation(QAnimation animation) {
		setCurrentAnimation(animation);
	}
	
	public void enableAnimation() {
		animate = true;
	}
	
	public void disableAnimation() {
		animate = false;
	}

}
