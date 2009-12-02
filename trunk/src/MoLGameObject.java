/**
 * This is a class representing various game objects. With this class I give up
 * OO apraoch - this class will behave accordingly to its type set on creation
 * time.
 * @author mumin
 *
 */
public class MoLGameObject extends QObject implements MoLConstants {
	
	private int type;
	
	// requirements for carrying
	private int size;
	private int weight;
	
	// requirements for use
	private int strength;
	private int mind;
	private int dexterity;
	private int skills;
	
	private int collisionResponse;
	
	// MoLCharacter owner;			// owner==null means that it's lying somewhere ? 
	
	public MoLGameObject(int id, QImage img, int x, int y, int cx, int cy, int width, int height, int ax, int ay, int aradius, boolean living, int collisionType) {
		super(id, img, x, y, cx, cy, width, height, ax, ay, aradius, living, collisionType);
		collisionResponse = OBJECT_RESPONSE_BOUNCE_OFF;
	}

	public void act() {
		// act on its owner
	}
	
	public void use(MoLCharacter character) {
		// this is bad idea... or maybe not? :-)
	}
	
	public void unuse(MoLCharacter character) {
		// act on its owner
	}
	
	public int checkRequirements(MoLCharacter ch) {
		
		if (ch.getStrength() <= strength)			return OBJECT_ACT_ERR_REQ_STRENGTH;
		else if (ch.getDexterity() <= dexterity)	return OBJECT_ACT_ERR_REQ_DEXTERITY;
		else if (ch.getMind() <= mind)				return OBJECT_ACT_ERR_REQ_MIND;
		else if ((ch.getSkills()&skills) == 0) 		return OBJECT_ACT_ERR_REQ_SKILL;
		else if	( type==OBJECT_WEAPON_ATTACK && ch.getWeapon()!=null)	return OBJECT_ACT_ERR_ROOM;
		else if	( type==OBJECT_WEAPON_DEFEND && ch.getShield()!=null)	return OBJECT_ACT_ERR_ROOM;
		else if	( type==OBJECT_CLOTHING && ch.getArmour()!=null)		return OBJECT_ACT_ERR_ROOM;
		
		return OBJECT_ACT_NO_ERR; // I know Java gurus avoid C-style returns but who cares ;-) 
	}
	
	public int getCollisionResponse(MoLGameObject obj) {
		
// collision check done by MoLMap
// this could check the object id or something		
//		if ( super.collides(obj)) {
//			if (super.action(obj)) {
//				obj.act(); // which act, this.act() or the obj.act()?
//			}
//			else {
//				return OBJECT_RESPONSE_BOUNCE_OFF;
//			}
//		}
		return collisionResponse;
		//		return OBJECT_RESPONSE_NONE;
	}
	
	public int getCollisionResponse() {
		return collisionResponse;
	}
}
