
public interface MoLConstants extends MoLImages {

	
//	static final int STATE_MENU_MAIN = 1;
//	static final int STATE_MENU_ABOUT = 2;
//	static final int STATE_MENU_HELP = 3;
//	static final int STATE_MENU_EXIT = 4;
//	
//	static final int STATE_MENU_GAME_START = 4;
//	
//	static final int ACTION_SET_STATE_MENU_MAIN = 1;
//	static final int ACTION_SET_STATE_MENU_ABOUT = 2;
//	static final int ACTION_SET_STATE_MENU_HELP = 3;
//	static final int ACTION_SET_STATE_MENU_EXIT = 4;
//	
//	static final int ACTION_SET_STATE_GAME_START = 1;
	
	static final int OBJECT_EXTRA					= 0x1 << 0;
	static final int OBJECT_WEAPON_ATTACK			= 0x1 << 1;
	static final int OBJECT_WEAPON_DEFEND			= 0x1 << 2;
	static final int OBJECT_CLOTHING				= 0x1 << 3;
	static final int OBJECT_FOOD					= 0x1 << 4;
	static final int OBJECT_BUILDING				= 0x1 << 5;
	
	static final int OBJECT_LOCATED_GROUND			= 1;
	static final int OBJECT_LOCATED_EQUIPMENT		= 2;
	static final int OBJECT_LOCATED_HEAD			= 3;
	static final int OBJECT_LOCATED_BODY			= 4;
	static final int OBJECT_LOCATED_HAND_L			= 5;
	static final int OBJECT_LOCATED_HAND_R			= 6;
	
	static final int OBJECT_ACT_NO_ERR				= 0;
	static final int OBJECT_ACT_ERR_ROOM			= 1;
	static final int OBJECT_ACT_ERR_WEIGHT			= 2;
	static final int OBJECT_ACT_ERR_REQ_STRENGTH	= 4;
	static final int OBJECT_ACT_ERR_REQ_DEXTERITY	= 5;
	static final int OBJECT_ACT_ERR_REQ_MIND		= 6;
	static final int OBJECT_ACT_ERR_REQ_SKILL		= 7;
	
	static final int OBJECT_RESPONSE_NONE			= 0x1<<0;
	static final int OBJECT_RESPONSE_BOUNCE_OFF		= 0x1<<1;
	
	
	static final int NO_ID							= 0;
	
	// current scheme is:
	// U
	// D
	// L
	// R
	static final int WALK_UP						= 0;
	static final int WALK_DOWN						= 1;
	static final int WALK_LEFT						= 2;
	static final int WALK_RIGHT						= 3;
	
	static final int STAND_UP						= 10;
	static final int STAND_DOWN						= 11;
	static final int STAND_LEFT						= 12;
	static final int STAND_RIGHT					= 13;

	static final int MONK_WALK_UP					= 0;
	static final int MONK_WALK_DOWN					= 1;
	static final int MONK_WALK_LEFT					= 2;
	static final int MONK_WALK_RIGHT				= 3;
	static final int MONK_STAND_UP					= 4;
	static final int MONK_STAND_DOWN				= 5;
	static final int MONK_STAND_LEFT				= 6;
	static final int MONK_STAND_RIGHT				= 7;
	static final int WARRIOR_WALK_UP				= 8;
	static final int WARRIOR_WALK_DOWN				= 9;
	static final int WARRIOR_WALK_LEFT				= 10;
	static final int WARRIOR_WALK_RIGHT				= 11;
	static final int WARRIOR_STAND_UP				= 12;
	static final int WARRIOR_STAND_DOWN				= 13;
	static final int WARRIOR_STAND_LEFT				= 14;
	static final int WARRIOR_STAND_RIGHT			= 15;
	
	static final int STATE_TEST						= 0;
	static final int STATE_SPLASH_SCREEN			= 1;
	static final int STATE_WORLD_MAP				= 2;
	static final int STATE_MAIN_MENU				= 3;
	static final int STATE_LOCATION					= 100;
	static final int STATE_LOCATION_TEMPLE			= 101;
	static final int STATE_MINI_GAME				= 200;
	static final int STATE_MINI_GAME_1				= 201;
	static final int STATE_MINI_GAME_2				= 202;
	
	
	static final int EQUIPMENT_INITIAL_SIZE			= 3;
	
	static final int CURSOR_SPEED					= 3;
	

	
	static final  int [] layer_1_temple = new int[] {
		TILE_GRASS_1, TILE_GRASS_2, TILE_GRASS_1, TILE_GRASS_2, TILE_GRASS_1,
		TILE_GRASS_1, TILE_GRASS_1, TILE_GRASS_1, TILE_GRASS_1, TILE_GRASS_1,
										
		TILE_GRASS_1, TILE_GRASS_1, TILE_GRASS_1, TILE_GRASS_2, TILE_GRASS_2,
		TILE_GRASS_1, TILE_GRASS_1, TILE_GRASS_1, TILE_GRASS_2, TILE_GRASS_1,
										
		TILE_GRASS_1, TILE_GRASS_1, TILE_GRASS_2, TILE_GRASS_1, TILE_GRASS_2,
		TILE_GRASS_1, TILE_GRASS_1, TILE_GRASS_2, TILE_GRASS_2, TILE_GRASS_1,
										
		TILE_GRASS_2, TILE_GRASS_1, TILE_GRASS_1, TILE_GRASS_2, TILE_GRASS_2,
		TILE_GRASS_1, TILE_GRASS_1, TILE_GRASS_1, TILE_GRASS_2, TILE_GRASS_1,
										
		TILE_GRASS_2, TILE_GRASS_1, TILE_GRASS_1, TILE_GRASS_2, TILE_GRASS_2,
		TILE_GRASS_1, TILE_GRASS_1, TILE_GRASS_1, TILE_GRASS_2, TILE_GRASS_1,
										
		TILE_GRASS_2, TILE_GRASS_1, TILE_GRASS_1, TILE_GRASS_2, TILE_GRASS_1,
		TILE_GRASS_1, TILE_GRASS_1, TILE_GRASS_1, TILE_GRASS_2, TILE_GRASS_1,
										
		TILE_GRASS_2, TILE_GRASS_1, TILE_GRASS_1, TILE_GRASS_2, TILE_GRASS_2,
		TILE_GRASS_1, TILE_GRASS_1, TILE_GRASS_1, TILE_GRASS_2, TILE_GRASS_1,
										
		TILE_GRASS_2, TILE_GRASS_1, TILE_GRASS_1, TILE_GRASS_1, TILE_GRASS_2,
		TILE_GRASS_1, TILE_GRASS_1, TILE_GRASS_1, TILE_GRASS_2, TILE_GRASS_1,
										
		TILE_GRASS_2, TILE_GRASS_1, TILE_GRASS_1, TILE_GRASS_2, TILE_GRASS_2,
		TILE_GRASS_1, TILE_GRASS_1, TILE_GRASS_1, TILE_GRASS_1, TILE_GRASS_1,
							
		TILE_GRASS_2, TILE_GRASS_1, TILE_GRASS_1, TILE_GRASS_2, TILE_GRASS_1,
		TILE_GRASS_1, TILE_GRASS_1, TILE_GRASS_1, TILE_GRASS_2, TILE_GRASS_1,
	};
	
}
