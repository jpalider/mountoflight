import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;


public class MoLGame extends QGame implements MoLConstants, MoLImages, QConstants {

	MoLMap map;

	int width;
	int height;
	int hwidth;
	int hheight;
	QImageManager im;
	
	QView view;
	
	int keyState;
	
	int test_image_list[] = {
			TILE_GRASS_1,
			TILE_GRASS_2,
			TILE_GRASS_3,
			TILE_ROAD_1_DUR,
			TILE_ROCK_1,
			TILE_ROCK_2,
			TILE_ROAD_1_LR,
			WORLD_MAP,
			WORLD_MAP_HUT,
			WORLD_MAP_LAKE,
			WORLD_MAP_FOREST,
			WORLD_MAP_MOUNTAINS,
			WORLD_MAP_MARKET,
			WORLD_MAP_TEMPLE,
			WORLD_MAP_CASTLE,
			IMG_CROSS_BLACK,
			IMG_CROSS_RED,
			BUILDING_TEMPLE,
			IMG_COCCINELLA,
			IMG_QBME,
			MONK_WDL, MONK_WDM, MONK_WDR,
			MONK_WUL, MONK_WUM, MONK_WUR,
			MONK_WLR, MONK_WLM, MONK_WLL,
			MONK_WRL, MONK_WRM, MONK_WRR,
			FRAME_CORNER_UL,
			FRAME_CORNER_UR,
			FRAME_CORNER_BL,
			FRAME_CORNER_BR,
			FRAME_BORDER_U,
			FRAME_BORDER_B,
			FRAME_BORDER_L,
			FRAME_BORDER_R,
			TILE_BOX_BG,
			BUILDING_TOWER,
			OBJECT_TREE_1,
			WARRIOR_WUM, WARRIOR_WUL, WARRIOR_WUR,
			WARRIOR_WDM, WARRIOR_WDL, WARRIOR_WDR,
			WARRIOR_WLM, WARRIOR_WLL, WARRIOR_WLR,
			WARRIOR_WRM, WARRIOR_WRL, WARRIOR_WRR,
	};
	
	// excellent example of Code Driven Development... ugh!
	int[][] test_animations_descriptor = {
			{ MONK_WUM, MONK_WUL, MONK_WUM, MONK_WUR, }, // MONK_WALK_UP
			{ MONK_WDM, MONK_WDL, MONK_WDM, MONK_WDR, }, // MONK_WALK_DOWN
			{ MONK_WLM, MONK_WLL, MONK_WLM, MONK_WLR, }, // MONK_WALK_LEFT
			{ MONK_WRM, MONK_WRL, MONK_WRM, MONK_WRR, }, // MONK_WALK_RIGHT
			{ WARRIOR_WUM, WARRIOR_WUL, WARRIOR_WUM, WARRIOR_WUR, }, // WARRIOR_WALK_UP
			{ WARRIOR_WDM, WARRIOR_WDL, WARRIOR_WDM, WARRIOR_WDR, }, // WARRIOR_WALK_DOWN
			{ WARRIOR_WLM, WARRIOR_WLL, WARRIOR_WLM, WARRIOR_WLR, }, // WARRIOR_WALK_LEFT
			{ WARRIOR_WRM, WARRIOR_WRL, WARRIOR_WRM, WARRIOR_WRR, }, // WARRIOR_WALK_RIGHT
			{ WARRIOR_SDL, WARRIOR_SDL, WARRIOR_SDR, WARRIOR_SDD, }, // WARRIOR_STAND_DOWN
			{ WARRIOR_SUL, WARRIOR_SUL, WARRIOR_SUR, WARRIOR_SUU, }, // WARRIOR_STAND_UP
	};

//	public void loadAnimations(int[][] test_animations_descriptor) {
//		test_animations = new Image[test_animations_descriptor.length][];
//		for (int i = 0; i < test_animations.length; i++) {
//			test_animations[i] = new Image[test_animations_descriptor[i].length];
//			for (int j = 0; j < test_animations[i].length; j++) {
//				//test_animations[i][j] = im.getImage(test_animations_descriptor[i][j]);				
//			}
//		}
//	}
		
	
	/**************************************************************************
	 * 
	 *  GAME STUFF
	 * 
	 **************************************************************************/
	
	public MoLGame() {
		
		super();

		width = MoL.canvas.width;
		height = MoL.canvas.height;
		hwidth = width>>1;
		hheight = height>>1;
		im = MoL.midlet.iManager;
		im.loadImages(test_image_list);
	
		view = new QView(width,height);
		
		gameStartTime = getRealTime();
		initState(STATE_WORLD_MAP);
		setState(STATE_WORLD_MAP);

	}

	public void processDrawing() {

		switch (state) {
		
			case STATE_SPLASH_SCREEN:
				drawSplashScreen();
				break;
				
			case STATE_WORLD_MAP:
				drawWorldMap();
				break;
				
			case STATE_LOCATION_TEMPLE:
				try {
					drawLocationTemple();
				} catch (RuntimeException e) {
					e.printStackTrace();
				}
				
				break;
				
			case STATE_MINI_GAME_1:	
				drawMiniGame1();
				break;

			case STATE_MINI_GAME_2:	
				drawMiniGame2();
				break;

			default:
				QUtils.debug("ERR: Game reached undefined state in processDrawing()!");
			
		}
	}
	
	public void draw(Graphics g) {
		//view.draw(g);
		g.drawImage(view.getImage(), 0, 0, 0);
	}
	
	public void processLogic(int keyState) {

		this.keyState = keyState;
		
		if (isPressed(GAME_KEY_LSK)) {
			MoL.midlet.destroyApp(true);
			return;
		}

		switch (state) {
		
			case STATE_SPLASH_SCREEN:
				processSplashScreen();
				break;
				
			case STATE_WORLD_MAP:
				processWorldMap();
				break;
				
			case STATE_LOCATION_TEMPLE:
				processLocationTemple();
				break;
				
			case STATE_MINI_GAME_1:	
				processMiniGame1();
				break;

			case STATE_MINI_GAME_2:	
				processMiniGame2();
				break;
			default:
				QUtils.debug("ERR: Game reached undefined state in processGame()!");
		}
		// if ( Config.CONFIG_CALL_GC ) System.gc();
	}
	
	private boolean isPressed( int key) {
		if ((keyState & (1<<key)) != 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setState(int newState) {
		state = newState;
	}
	
	public void clearWholeScreen(int colour) {
		view.fill(0x0);
	}
	
	public void initState(int targetState) {
		switch (targetState) {
			case STATE_SPLASH_SCREEN:
				initSplashScreen();
				break;
			case STATE_WORLD_MAP:
				initWorldMap();
				break;
			case STATE_LOCATION_TEMPLE:
				initLocationTemple();
				break;				
			case STATE_MINI_GAME_1:	
				initMiniGame1();
				break;
			case STATE_MINI_GAME_2:	
				initMiniGame2();
				break;	
			default:
				break;
		}
	}
	
	public void drawFrame(int x, int y, int width, int height) {
		QUtils.drawFrame(view, im, x, y, width, height, FRAME_CORNER_UL, FRAME_CORNER_UR, FRAME_CORNER_BL, FRAME_CORNER_BR, FRAME_BORDER_U, FRAME_BORDER_B, FRAME_BORDER_L, FRAME_BORDER_R, -1 /*TILE_BOX_BG*/, 0x0);
		
	}


	/**************************************************************************
	 * 
	 *  SPLASH SCREEN
	 * 
	 *************************************************************************/	
	
	QObject coccinella;
	int coccinellaProgressResolution;
	int coccinellaProgress;
	int coccinellaXPos;
	boolean coccinellaStopped;
	int[] qbmeImage;
	int qbmeAlpha;
	int qbmeY;
	int qbmeX;
	int qbmeYResolution;
	int qbmeAlphaResolution;
	boolean qbmeVanish;
	
	
	public void initSplashScreen() {
		coccinellaProgressResolution = 7;
		coccinellaProgress = -coccinellaProgressResolution;
		coccinellaXPos = width*4/5;
		coccinella = new QObject(MoLImages.IMG_COCCINELLA, im.getImage(MoLImages.IMG_COCCINELLA), coccinellaXPos, 0, 0, 0, im.getImage(MoLImages.IMG_COCCINELLA).getWidth(), 0, 0, 0, 0, false, COLLISION_TRANSPARENT);
		coccinella.setCoordinates(coccinella.getX(), height-coccinellaProgress);
		coccinellaStopped = false;

		qbmeY = -im.getImage(IMG_QBME).getHeight();
		qbmeX = (coccinellaXPos>>1)-(im.getImage(IMG_QBME).getWidth()>>1);
		
		qbmeYResolution = 3;
		qbmeAlphaResolution = 8;
		qbmeAlpha = 255;
		qbmeVanish = false;
		qbmeImage = new int[im.getImage(IMG_QBME).getWidth() * im.getImage(IMG_QBME).getHeight()];
		im.getImage(IMG_QBME).getImage().getRGB(	qbmeImage,
													0,
													im.getImage(IMG_QBME).getWidth(),
													0,
													0,
													im.getImage(IMG_QBME).getWidth(),
													im.getImage(IMG_QBME).getHeight());
		
		lastMeasureTime = getRealTime();
	}
	
	public void processSplashScreen() {
		
		// stop crawling for qbme image "animation"	
		if ( (height>>1) <= coccinellaProgress && qbmeVanish == false) {
			coccinellaStopped = true;
			qbmeVanish = true;
		}

		// crawl to the (almost) middle of the screen
		if ( coccinellaStopped == false || qbmeVanish == false ) {
			// every 100ms do:
			if (getRealTime()-lastMeasureTime > 100) {
				coccinellaProgress+=coccinellaProgressResolution;
				coccinella.setCoordinates(coccinella.getX(), height-coccinellaProgress);
				lastMeasureTime = getRealTime();
			}
		} else { //
			if ( qbmeY+(im.getImage(IMG_QBME).getHeight()>>1) <= height>>1) {
				qbmeY += qbmeYResolution;
			} else if (qbmeAlpha >= qbmeAlphaResolution) {					
				qbmeAlpha -= qbmeAlphaResolution;
				for (int i = 0; i < qbmeImage.length; i++) {
					//qbmeImage[i] = (qbmeAlpha<<24) | ( qbmeImage[i] & 0x00FFFFFF );
					qbmeImage[i] -= (qbmeAlphaResolution<<24);
				}
			} else {				
				for (int i = 0; i < qbmeImage.length; i++) {
					qbmeImage[i] &= 0xFF000000;
				}
				qbmeVanish = true;
				coccinellaStopped = false;
			}
		}			
	
		if (coccinellaProgress>height+coccinellaProgressResolution) {
			initState(STATE_WORLD_MAP);
			setState(STATE_WORLD_MAP);
		}		
	}
	
	
	public void drawSplashScreen() {
		view.fill(0x0);
		// footprints
		int sign=1;
		for (int i = 0; i < coccinellaProgress; i+=coccinellaProgressResolution) {
			sign = -sign;
			view.fillRect(coccinellaXPos - sign*(coccinella.getWidth()>>2), height-i, 1, 1, 0xFFFFFF);
		}
		// This call crashes app on my ke970
		//		g.drawRGB(	qbmeImage, 
		//					0,
		//					im.getImage(IMG_QBME).getWidth(),
		//					qbmeX,
		//					qbmeY,
		//					im.getImage(IMG_QBME).getWidth(),
		//					im.getImage(IMG_QBME).getHeight(),
		//					true); 
		// proposed workaround is:
		QImage tmpImage = new QImage(Image.createRGBImage(
												qbmeImage,
												im.getImage(IMG_QBME).getWidth(),
												im.getImage(IMG_QBME).getHeight(),
								                true)
								);
		view.drawQImage(tmpImage, qbmeX, qbmeY);
		coccinella.draw(view);
	}
	
	public void releaseSplashScreen() {
		coccinella = null;
		qbmeImage = null;
	}
	
	/**************************************************************************
	 * 
	 *  WORLD MAP
	 * 
	 **************************************************************************/

	QObject[] worldMapLocations;
	QObject	cross, crossBlack, crossRed;
	private int xCross, yCross;
	
	
	public void initWorldMap() {
		int mwh = im.getImage(MoLImages.WORLD_MAP).getWidth()>>1;
		int mhh = im.getImage(MoLImages.WORLD_MAP).getHeight()>>1;
		xCross = hwidth;
		yCross = hheight;
		int tx = hwidth;
		int ty = hheight;

		int mounx = mwh * 3/8;
		int mouny = mhh * 3/4;
		int lakex = mwh * 3/8;
		int lakey = -mhh * 4/8;
		int hutx = mwh * 3/8;
		int huty = mhh * 3/8;;
		int tempx = -mwh * 3/8;
		int tempy = -mhh * 2/8;
		int markx = -mwh * 1/2;
		int marky = mhh * 1/2;
		
		worldMapLocations = new QObject[] {
								new QObject(MoLImages.WORLD_MAP_MOUNTAINS,	im.getImage(MoLImages.WORLD_MAP_MOUNTAINS),	tx+mounx, ty+mouny, 0, 0, 0, 0, 0, 0, 0, false, COLLISION_RECTANGLE),
								new QObject(MoLImages.WORLD_MAP_LAKE,		im.getImage(MoLImages.WORLD_MAP_LAKE),		tx+lakex, ty+lakey, 0, 0, 0, 0, 0, 0, 0, false, COLLISION_RECTANGLE),
								new QObject(MoLImages.WORLD_MAP_HUT,		im.getImage(MoLImages.WORLD_MAP_HUT),		tx+hutx,  ty+huty,  0, 0, 0, 0, 0, 0, 0, false, COLLISION_RECTANGLE),
								new QObject(MoLImages.WORLD_MAP_TEMPLE,		im.getImage(MoLImages.WORLD_MAP_TEMPLE),	tx+tempx, ty+tempy, 0, 0, 0, 0, 0, 0, 0, false, COLLISION_RECTANGLE),
								new QObject(MoLImages.WORLD_MAP_MARKET,		im.getImage(MoLImages.WORLD_MAP_MARKET),	tx+markx, ty+marky, 0, 0, 0, 0, 0, 0, 0, false, COLLISION_RECTANGLE),
										};
		
		crossBlack = new QObject(MoLImages.IMG_CROSS_BLACK, im.getImage(MoLImages.IMG_CROSS_BLACK), xCross, yCross, 0, 0, 0, 0, 0, 0, 0, false, COLLISION_RECTANGLE);
		crossRed = new QObject(MoLImages.IMG_CROSS_RED, im.getImage(MoLImages.IMG_CROSS_RED), xCross, yCross, 0, 0, 0, 0, 0, 0, 0, false, COLLISION_RECTANGLE);
		cross = crossBlack;
	}
	
	public void processWorldMap() {
		
		// navigate the cursor
		if (isPressed(GAME_KEY_UP)) {
			cross.dY(-CURSOR_SPEED);
		}
		if (isPressed(GAME_KEY_DOWN)) {
			cross.dY(CURSOR_SPEED);
		}
		if (isPressed(GAME_KEY_LEFT)) {
			cross.dX(-CURSOR_SPEED);
		}
		if (isPressed(GAME_KEY_RIGHT)) {
			cross.dX(CURSOR_SPEED);
		} 

		// check if the cursor hovers any location, if none id is left unchanged 
		int id = -1;
		for (int i = 0; i < worldMapLocations.length; i++) {
			if ( cross.collides(worldMapLocations[i]) ) {
				id = worldMapLocations[i].getID();
				break;
			}				
		}
		
		if (id != -1) {
			if (cross.getID() != MoLImages.IMG_CROSS_RED) {
				cross = crossRed;
				cross.setX(crossBlack.getX());
				cross.setY(crossBlack.getY());
			}
		} else {
			if (cross.getID() != MoLImages.IMG_CROSS_BLACK) {
				cross = crossBlack;
				cross.setX(crossRed.getX());
				cross.setY(crossRed.getY());
			}
		}

		// don't let the cursor go beyond map's borders
		int crossx = cross.getX();
		int crossy = cross.getY();
		int imghw = im.getImage(WORLD_MAP).getWidth()>>1;
		int imghh = im.getImage(WORLD_MAP).getHeight()>>1;
		int chw = cross.getWidth()>>1; 
		int chh = cross.getHeight()>>1;

		if (crossx > hwidth + imghw - chw)  cross.setX(hwidth + imghw - chw);
		if (crossx < hwidth - imghw + chw)  cross.setX(hwidth - imghw + chw);
		if (crossy > hheight + imghh - chh) cross.setY(hheight + imghh - chh);
		if (crossy < hheight - imghh + chh) cross.setY(hheight - imghh + chh);

		if (isPressed(GAME_KEY_FIRE)) {
			switch(id) {
				case MoLImages.WORLD_MAP_MOUNTAINS:
					initState(STATE_SPLASH_SCREEN);
					setState(STATE_SPLASH_SCREEN);
					break;
				case MoLImages.WORLD_MAP_TEMPLE:
					initState(STATE_LOCATION_TEMPLE);
					setState(STATE_LOCATION_TEMPLE);
					break;
				case MoLImages.WORLD_MAP_LAKE:
					initState(STATE_MINI_GAME_1);
					setState(STATE_MINI_GAME_1);
					break;
				case MoLImages.WORLD_MAP_HUT:
					initState(STATE_MINI_GAME_2);
					setState(STATE_MINI_GAME_2);
					break;
				default:
					break;
			}
		}
	}
	
	public void drawWorldMap() {
		view.fillRect(0, 0, width, height, 0x0);
		view.drawQImageCentered(im.getImage(MoLImages.WORLD_MAP), width>>1, height>>1);
		// TODO: draw __available__ places
		for (int i = 0; i < worldMapLocations.length; i++) {
			worldMapLocations[i].draw(view);
		}
		cross.draw(view);
	}
	
	public void releaseWorldMap() {
		worldMapLocations = null;
		crossBlack = null;
		crossRed = null;
		cross = null;
	}

	/**************************************************************************
	 * 
	 *  LOCATION TEMPLE
	 * 
	 **************************************************************************/

	MoLMap location;
	MoLCharacter monk;
	MoLGameObject temple;
	MoLGameObject tower;
	MoLGameObject tree_1;
	MoLGameObject tree_2;
	MoLGameObject tree_3;
	MoLGameObject tree_4;
	MoLGameObject tree_5;
	
	public void initLocationTemple() {
		try {	
			monk = new MoLCharacter(MONK_WDM,im.getImage(MONK_WDM), 200, 130, 0, 0, 10, 10, 0, 0, 0, true, COLLISION_RECTANGLE);
			QImage[][] tmpImages = new QImage[4][4];
			                                    
			for (int i = 0; i < tmpImages.length; i++) {
				for (int j = 0; j < tmpImages[i].length; j++) {
					tmpImages[i][j] = im.getImage(test_animations_descriptor[i][j]);
				}
			}

			int[] tmpTimeT = {20000,20000,20000,20000,};
			int[] tmpXT = {5,6,5,6,};
			int[] tmpYT = {5,6,5,6,};

			monk.addAnimation(WALK_UP,		new QAnimation(tmpImages[0], tmpTimeT, tmpXT, tmpYT));
			monk.addAnimation(WALK_DOWN,	new QAnimation(tmpImages[1], tmpTimeT, tmpXT, tmpYT));
			monk.addAnimation(WALK_LEFT,	new QAnimation(tmpImages[2], tmpTimeT, tmpXT, tmpYT));
			monk.addAnimation(WALK_RIGHT,	new QAnimation(tmpImages[3], tmpTimeT, tmpXT, tmpYT));
			monk.setCurrentAnimation(MONK_WALK_DOWN);
			monk.setSpeed(2);
			monk.enableAnimation();
			monk.setCoordinates(xCross, yCross);
			
			int treeh = 3;
			int treew = 3;
			int treey = 55;
			int treex = -8;
			location = new MoLMap(view);
			temple = new MoLGameObject(NO_ID, im.getImage(BUILDING_TEMPLE), 120, 120, 0, 14, 100, 35, 0, 0, 0, true,COLLISION_RECTANGLE);
			tower = new MoLGameObject(NO_ID, im.getImage(BUILDING_TOWER), 330, 400, 0, 100, 90, 90, 0, 0, 0, true,COLLISION_CIRCLE);
			tree_1 = new MoLGameObject(NO_ID, im.getImage(OBJECT_TREE_1), 250, 120, treex, treey, treew, treeh, treex, treey, 0, true,COLLISION_RECTANGLE);
			tree_2 = new MoLGameObject(NO_ID, im.getImage(OBJECT_TREE_1), 220, 380, treex, treey, treew, treeh, treex, treey, 0, true,COLLISION_RECTANGLE);
			tree_3 = new MoLGameObject(NO_ID, im.getImage(OBJECT_TREE_1), 190, 560, treex, treey, treew, treeh, treex, treey, 0, true,COLLISION_RECTANGLE);
			tree_4 = new MoLGameObject(NO_ID, im.getImage(OBJECT_TREE_1), 340, 600, treex, treey, treew, treeh, treex, treey, 0, true,COLLISION_RECTANGLE);
			tree_5 = new MoLGameObject(NO_ID, im.getImage(OBJECT_TREE_1), 240, 480, treex, treey, treew, treeh, treex, treey, 0, true,COLLISION_RECTANGLE);
//			MoLGameObject(int id, QImage img, int x, int y, int cx, int cy, int width, int height, int ax, int ay, int aradius, boolean living, int collisionType)
			
			int lo = 0;
			QObject[] layer_2 = new MoLGameObject[7];	// living objects
			QObject[] layer_3 = new MoLCharacter[1];	// characters
			
			layer_2[lo++] = temple;
			layer_2[lo++] = tower;
			layer_2[lo++] = tree_1;
			layer_2[lo++] = tree_2;
			layer_2[lo++] = tree_3;
			layer_2[lo++] = tree_4;
			layer_2[lo++] = tree_5;
			
			layer_3[0] = monk;
			
			location.setLayers(layer_1_temple, layer_2, layer_3);

		}catch (Exception e) {
			QUtils.debug("exception caught initLocationTemple() " + e.toString());
		}	

	}
	
	public void processLocationTemple() {
		
		if (isPressed(GAME_KEY_UP)) monk.moveUp();
		if (isPressed(GAME_KEY_DOWN)) monk.moveDown();
		if (isPressed(GAME_KEY_LEFT)) monk.moveLeft();
		if (isPressed(GAME_KEY_RIGHT)) monk.moveRight();
		
		location.mx = monk.getX();
		location.my = monk.getY();		
		
		location.validate();
		location.sortLayers();
		location.collideLayers();
		monk.tick();

	}

	public void drawLocationTemple() {
		location.draw();
	}


	/**************************************************************************
	 * 
	 *  MINI GAME 1 - consolidation
	 * 
	 **************************************************************************/
	int mg1_htiles = 3;
	int mg1_vtiles = 4;
	QImage[] mg1_tiles;
	int[] mg1_tiles_to_display; // index in mg1_tiles[]
	int mg1_tilew, mg1_tileh;
	final int MG1_EMPTY_TILE = -1;
	int mg1_x = 0;
	int mg1_y = 0;
	int mg1_xoffset, mg1_yoffset;
	boolean mg1_solved;
	int mg1_key_press_interval; // in [ms]
	long mg1_key_press_time;
	
	private void initMiniGame1() {
		
		QImage gameImgage = im.getImage(WORLD_MAP);
		
		/*
		 * In case where image dimension is not a multiplication of tile
		 * size we must align it.
		 */
		mg1_tilew = (gameImgage.getWidth()+gameImgage.getWidth()%mg1_htiles)/mg1_htiles;
		mg1_tileh = (gameImgage.getHeight()+gameImgage.getHeight()%mg1_vtiles)/mg1_vtiles;
		
		mg1_tiles = new QImage[mg1_htiles*mg1_vtiles];
		for (int i = 0; i < mg1_tiles.length; i++) {
			int x = i%mg1_htiles;
			int y = i/mg1_htiles;
			Image img=null;
			img = Image.createImage(mg1_tilew, mg1_tileh);
			img.getGraphics().setColor(0x0);
			img.getGraphics().fillRect(0, 0, mg1_tilew, mg1_tileh);
			/* 
			 * drawRegion throws an exception if the region to be copied 
			 * exceeds the bounds of the source image, so in case where 
			 * img_dimension % mg1_tiles != 0 we need to limit the request
			 * to source image boundaries.
			 */
			int adjx=0, adjy=0;
			adjx = (x*mg1_tilew + mg1_tilew) - gameImgage.getWidth();
			adjy = (y*mg1_tileh + mg1_tileh) - gameImgage.getHeight();
			img.getGraphics().drawRegion(gameImgage.getImage(), x*mg1_tilew, y*mg1_tileh, mg1_tilew-adjx, mg1_tileh-adjy, 0, 0, 0, 0);
			mg1_tiles[i] = new QImage(img);
		}
		// scatter the mg1_tiles
		mg1_tiles_to_display = new int [] { 3, 6, 5, 8, 0, 7, 1, 9, 2, 10, 4, MG1_EMPTY_TILE,};
		// for test
		mg1_tiles_to_display = new int [] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, MG1_EMPTY_TILE, 10,};
		mg1_xoffset = (view.getWidth()-gameImgage.getWidth())>>1;
		mg1_yoffset = (view.getHeight()-gameImgage.getHeight())>>1;
		mg1_solved = false;
		mg1_key_press_interval = 200;
		mg1_key_press_time = QUtils.getTime();
	}
	
	private void processMiniGame1() {

		if (checkMiniGame1() && !mg1_solved) {
			mg1_solved = true;
		}
		
		if (mg1_solved) {
			//animate
			mg1_solved = false;
			setState(STATE_WORLD_MAP);
			return;
		} 
		
		if (QUtils.getTime()-mg1_key_press_time < mg1_key_press_interval) {
			return;
		}

		boolean pressed = false;
		if (isPressed(GAME_KEY_UP)) {
			mg1_y -= mg1_tileh;			
			if ( mg1_y < 0 ) {
				mg1_y = (mg1_vtiles-1)*mg1_tileh;
			}
			pressed = true;
		}
		
		if (isPressed(GAME_KEY_DOWN)) {
			mg1_y += mg1_tileh;
			if ( mg1_y > (mg1_vtiles-1)*mg1_tileh) {
				mg1_y = 0;
			}
			pressed = true;
		}
		
		if (isPressed(GAME_KEY_LEFT)) {
			mg1_x -= mg1_tilew;
			if (mg1_x<0) {
				mg1_x = (mg1_htiles-1)*mg1_tilew;
			}
			pressed = true;
		}
		
		if (isPressed(GAME_KEY_RIGHT)) {
			mg1_x += mg1_tilew;
			if ( mg1_x > (mg1_htiles-1)*mg1_tilew) {
				mg1_x = 0;
			}
			pressed = true;
		}
		
		if (isPressed(GAME_KEY_FIRE)) {
			int ct = mg1_htiles*(mg1_y/mg1_tileh) + (mg1_x/mg1_tilew);
			if ( (ct+1 < mg1_tiles_to_display.length) ) {
				if (mg1_tiles_to_display[ct + 1] == MG1_EMPTY_TILE) {
					mg1_tiles_to_display[ct + 1] = mg1_tiles_to_display[ct];
					mg1_tiles_to_display[ct] = MG1_EMPTY_TILE;
				}
			}
			if ( (ct > 0) ) {
				if (mg1_tiles_to_display[ct - 1] == MG1_EMPTY_TILE) {
					mg1_tiles_to_display[ct - 1] = mg1_tiles_to_display[ct];
					mg1_tiles_to_display[ct] = MG1_EMPTY_TILE;
				}
			}
			if (ct-mg1_htiles >= 0) {
				if (mg1_tiles_to_display[ct - mg1_htiles] == MG1_EMPTY_TILE) {
					mg1_tiles_to_display[ct - mg1_htiles] = mg1_tiles_to_display[ct];
					mg1_tiles_to_display[ct] = MG1_EMPTY_TILE;
				}
			}
			if (ct+mg1_htiles < mg1_tiles_to_display.length) {
				if (mg1_tiles_to_display[ct + mg1_htiles] == MG1_EMPTY_TILE) {
					mg1_tiles_to_display[ct + mg1_htiles] = mg1_tiles_to_display[ct];
					mg1_tiles_to_display[ct] = MG1_EMPTY_TILE;
				}
			}
			pressed = true;
		}
		if (pressed) {
			mg1_key_press_time = QUtils.getTime();
		}
	}
	
	private void drawMiniGame1() {
		view.fill(0x0);

		drawFrame(mg1_xoffset + ((mg1_htiles*mg1_tilew)>>1), mg1_yoffset + ((mg1_vtiles*mg1_tileh)>>1), mg1_htiles*mg1_tilew, mg1_vtiles*mg1_tileh);
		for (int i = 0; i < mg1_tiles_to_display.length; i++) {
			int x = i%mg1_htiles;
			int y = i/mg1_htiles;
			if (mg1_tiles_to_display[i] != MG1_EMPTY_TILE) {
				view.drawQImage(mg1_tiles[mg1_tiles_to_display[i]], x*mg1_tilew+mg1_xoffset, y*mg1_tileh+mg1_yoffset);
			}
		}
		view.drawRect(mg1_x+mg1_xoffset, mg1_y+mg1_yoffset, mg1_tilew, mg1_tileh, 0xFFFF00);
	}
	
	private boolean checkMiniGame1() {
		for (int i = 0; i < mg1_tiles_to_display.length-1; i++) {
			if (i != mg1_tiles_to_display[i]) {
				return false;
			}
		}
		return true;
	}
	
	/**************************************************************************
	 * 
	 *  MINI GAME 2 - knight tour
	 * 
	 **************************************************************************/
	int[][] board;
	int xtiles, ytiles;
	int x, y;
	int tx, ty;
	int key_press_interval;
	long key_press_time;
	boolean solved;
	int board_width;
	int board_height;
	
	private void initMiniGame2() {
		ytiles = xtiles = 5;
		tx = x = 0;
		ty = y = 0;
		board = new int[xtiles][ytiles];
		solved = false;
		key_press_interval = 500;
		board_width = width*2/3/xtiles*xtiles; // 2/3 -> mulpiplicity of 5
		board_height = board_width;
		
	}
	
	private void processMiniGame2() {
		if (checkMiniGame2() && !solved) {
			solved = true;
		}
		
		if (solved) {
			solved = false;
			setState(STATE_WORLD_MAP);
			return;
		} 
		
		if (QUtils.getTime()-key_press_time < key_press_interval) {
			return;
		}

		boolean pressed = false;
		if (isPressed(GAME_KEY_UP)) {
			if (ty>0) {
				ty--;
			}
			pressed = true;
		}
		if (isPressed(GAME_KEY_DOWN)) {
			if (ty<ytiles-1) {
				ty++;
			}
			pressed = true;
		}
		if (isPressed(GAME_KEY_LEFT)) {
			if (tx>0) {
				tx--;
			}
			pressed = true;
		}
		if (isPressed(GAME_KEY_RIGHT)) {
			if (tx<xtiles-1) {
				tx++;
			}
			pressed = true;
		}
		
		if (pressed) {
			key_press_time = QUtils.getTime();
		}
	}
	
	private void drawMiniGame2() {
		view.fill(0x0);
		drawFrame(hwidth, hheight, board_width, board_height);
		for (int i = 1; i < xtiles; i++ ) {
			view.drawLine(hwidth-(board_width>>1)+i*(board_width/xtiles), hheight-(board_height>>1), hwidth-(board_width>>1)+i*(board_width/xtiles), hheight+(board_height>>1), 0xFFFF00);
		}
		for (int i = 0; i < ytiles+1; i++ ) {
			view.drawLine(hheight-(board_height>>1)+i*(board_height/ytiles), hwidth-(board_width>>1), hheight-(board_height>>1)+i*(board_height/ytiles), hwidth+(board_width>>1), 0xFFFF00);

		}
		// maybe create tiles?

	}
	
	private boolean checkMiniGame2() {
		return false;
	}

}
