
public class QAnimation {

	static final int ANIMATION_NEVER_TIMEOUT = -99999;
	
	private QImage frames[];
	public int currentFrame;
	private int framesTimeThreshold[];
	private int framesXPosThreshold[];
	private int framesYPosThreshold[];

	/**
	 * Creates an animation for an object
	 * @param object This is needed to obtain objets's coordinates as the animation
	 * remembers its object's previous position - needed to decide whether to 
	 * change current frame
	 * @param frames A set o QImage frames used in this animation 
	 * @param framesTimeThreshold The animation changes current frame when timeout
	 * reaches current frame duration
	 * @param framesXPosThreshold The animation changes current frame when object's
	 * x coordinate go beyond a given limit
	 * @param framesYPosThreshold The animation changes current frame when object's
	 * y coordinate go beyond a given limit
	 */
	public QAnimation(QImage[] frames, int[] framesTimeThreshold,
						int[] framesXPosThreshold, int[] framesYPosThreshold) {
		this.frames = frames;
		this.framesTimeThreshold = framesTimeThreshold;
		this.framesXPosThreshold = framesXPosThreshold;
		this.framesYPosThreshold = framesYPosThreshold;
		currentFrame = 0;
		
		// check if all parameters are fine, e.g. arrays lengths are equal
	}
	
//	public void setOwner(QObject object) {
//		this.object = object;
//	}

	public QImage getCurentFrame() {
		return frames[currentFrame];
	}

	public void update(QObject object) {
		//System.out.println(currentFrame);
		boolean updateFrame = false;
		
		if (object.getadX() >= framesXPosThreshold[currentFrame] ) {
			updateFrame = true;
		} else if ( object.getadY() >= framesYPosThreshold[currentFrame] ) {
			updateFrame = true;
		} else if ( object.getadT() >= framesTimeThreshold[currentFrame] ) {			
			updateFrame = true;
		}
		
		if (framesTimeThreshold[currentFrame] == ANIMATION_NEVER_TIMEOUT) {
			// updateFrame = false;	
			// not really
		}
		
		if ( updateFrame ) {
			object.updatePrevAnimData();
			currentFrame++;			
			if (currentFrame >= frames.length) {
				currentFrame = 0;
			}
		}
	}
	
	public long getNeverExpire() {
		return ANIMATION_NEVER_TIMEOUT;
	}
	
}
