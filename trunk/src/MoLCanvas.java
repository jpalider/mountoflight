import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class MoLCanvas extends Canvas implements QConstants, Runnable, Config {
	
	MoL midlet = null;
	MoLGame game = null;
	
	/***********************************
	 * Canvas-related state variables
	 */

	
	int keyState; // after processing
	int keysPressed = 0;
	int keysReleased = 0;
	int keyToProcess = 0;

	
	static public boolean running = true;
	
	int width, height;
	//Image gBuffer = null; 
	
	
	public MoLCanvas(MoL midlet) {
		this.midlet = midlet;
		if ( false ) {
			// get it from RMS if previously set, otherwise let the canvas set it
			// TODO: an option in game to set the canvas size
		} else {
			setFullScreenMode(true);
			width = getWidth();
			height = getHeight();
		}
	}

	
	protected void paint(Graphics g) {
		
		if (game==null) return;
		
		//		if (CONFIG_DOUBLE_BUFFER) {
		//			//g.drawImage(gBuffer, 0, 0, Graphics.TOP|Graphics.LEFT);
		//		} 
		game.draw(g);
		
	}
	
	public void run() {
		
		init();		
		
		while(running) {
			/* INPUT */
			//processInput();

			/* LOGIC */
			processLogic();
		
			/* PRESENTATION */
			processDrawing();


			/* DOUBLE BUFFER */
			//			if (CONFIG_DOUBLE_BUFFER) {				
			//				game.drawGame(gBuffer.getGraphics());
			//			}

			/* PRESENTATION */
			repaint();
			serviceRepaints();
			
			/* ACHIEVING DESIRED FRAME RATE */
			adjustFrameRate();
		}
		
	}
	
	public void keyPressed(int key) {
//		int currKey = translateKey(key);
//		if ( keysPressed&currKey )
		keyState |= translateKey(key);
	}

	public void keyReleased(int key) {
		keyState &= ~translateKey(key);
	}
	
	public int translateKey(int key) {
		
		int translated = 0;
		
		if (key == 0)
			return 0;
		
		int action = 0;
		try {
			action = getGameAction(key);
		} catch (Exception e) {
			QUtils.debug("exception cautch: processReleased - key=" + key);
		}
		
		switch (action) {		
			case UP:
				translated = ( 1 << GAME_KEY_UP );
				break;
			case DOWN:
				translated = ( 1 << GAME_KEY_DOWN );				
				break;
			case LEFT:
				translated = ( 1 << GAME_KEY_LEFT );
				break;
			case RIGHT:
				translated = ( 1 << GAME_KEY_RIGHT );
				break;
			case FIRE:
				translated = ( 1 << GAME_KEY_FIRE );
				break;
			default:
				switch(key) {
					case KEY_NUM0:						
						break;
					case KEY_NUM1:
						break;
					case KEY_NUM2:
						translated = ( 1 << GAME_KEY_UP );
						break;
					case KEY_NUM3:
						break;
					case KEY_NUM4:
						translated = ( 1 << GAME_KEY_LEFT );
						break;
					case KEY_NUM5:
						break;
					case KEY_NUM6:
						translated = ( 1 << GAME_KEY_RIGHT );
						break;
					case KEY_NUM7:
						break;
					case KEY_NUM8:
						translated = ( 1 << GAME_KEY_DOWN );
						break;
					case KEY_NUM9:
						break;
					case KEY_POUND:
						break;
					case KEY_STAR:
						break;
					case -6:
						translated = ( 1 << GAME_KEY_LSK );
						break;	
					default:
						QUtils.debug("MoLCanvas.translateKey() : Unknown key=" + key);
				}
				
		}
		
		return translated;

	}

/*
	public void processPressed() {
		
		//int key = ( keyPressedDirty != 0 ? keyPressedDirty : keyPressed );		
		int key = keyPressed;
		
		if (key == 0)
			return;
		
		int action = 0;
		try {
			action = getGameAction(key);
		} catch (Exception e) {
			System.out.println("exception cautch: processPressed - key=" + key);
		}
		switch (action) {		
			case UP:
				keyState |=  (1 << GAME_KEY_UP);
				break;
			case DOWN:
				keyState |=  (1 << GAME_KEY_DOWN);
				break;
			case LEFT:
				keyState |=  (1 << GAME_KEY_LEFT);
				break;
			case RIGHT:
				keyState |=  (1 << GAME_KEY_RIGHT);
				break;
			case FIRE:
				keyState |=  (1 << GAME_KEY_FIRE);
				break;
			default:
				switch(key) {
					case KEY_NUM0:						
						break;
					case KEY_NUM1:
						break;
					case KEY_NUM2:
						keyState |=  (1 << GAME_KEY_UP);
						break;
					case KEY_NUM3:
						break;
					case KEY_NUM4:
						keyState |=  (1 << GAME_KEY_LEFT);
						break;
					case KEY_NUM5:
						break;
					case KEY_NUM6:
						keyState |=  (1 << GAME_KEY_RIGHT);
						break;
					case KEY_NUM7:
						break;
					case KEY_NUM8:
						keyState |=  (1 << GAME_KEY_DOWN);
						break;
					case KEY_NUM9:
						break;
					case KEY_POUND:
						break;
					case KEY_STAR:
						break;
					default:
						QUtils.debug("TestMECanvas.processInput() : Unknown key");
				}
				
		}
		
		keyPressed = 0;
	}
	
	
	public void processReleased() {
		
		int key = keyReleased;
		//int key = ( keyReleasedDirty != 0 ? keyReleasedDirty : keyReleased );		
		if (key == 0)
			return;
		int action = 0;
		try {
			action = getGameAction(key);
		} catch (Exception e) {
			System.out.println("exception cautch: processReleased - key=" + key);
		}
		
		switch (action) {		
			case UP:
				keyState &= ~ ( 1 << GAME_KEY_UP );
				break;
			case DOWN:
				keyState &= ~ ( 1 << GAME_KEY_DOWN );				
				break;
			case LEFT:
				keyState &= ~ ( 1 << GAME_KEY_LEFT );
				break;
			case RIGHT:
				keyState &= ~ ( 1 << GAME_KEY_RIGHT );
				break;
			case FIRE:
				keyState &= ~ ( 1 << GAME_KEY_FIRE );
				break;
			default:
				switch(key) {
					case KEY_NUM0:						
						break;
					case KEY_NUM1:
						break;
					case KEY_NUM2:
						keyState &= ~ ( 1 << GAME_KEY_UP );
						break;
					case KEY_NUM3:
						break;
					case KEY_NUM4:
						keyState &= ~ ( 1 << GAME_KEY_LEFT );
						break;
					case KEY_NUM5:
						break;
					case KEY_NUM6:
						keyState &= ~ ( 1 << GAME_KEY_RIGHT );
						break;
					case KEY_NUM7:
						break;
					case KEY_NUM8:
						keyState &= ~ ( 1 << GAME_KEY_DOWN );
						break;
					case KEY_NUM9:
						break;
					case KEY_POUND:
						break;
					case KEY_STAR:
						break;
					default:
						QUtils.debug("TestMECanvas.processInput() : Unknown key=" + key);
				}
				
		}
		keyReleased = 0;

	}
*/
	
	boolean isPressed(int key) {
		if ( (keyState & (1<<key)) != 0 )
			return true;
		else
			return false;
	}

	public void processLogic() {
		game.update();
		game.processLogic(keyState);
	}
	
	public void processDrawing() {
		game.processDrawing();
	}
	
	
	public void init() {
		try {
			game = new MoLGame();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	private long lastFrameTime = QUtils.getTime();
	
	public void adjustFrameRate() {
		if (QUtils.getTime()-lastFrameTime < 1000/TARGET_FPS) {			
			QUtils.msleep( 1000/TARGET_FPS - (QUtils.getTime()-lastFrameTime) );
		} else {
			lastFrameTime = QUtils.getTime();
		}
	}

}
