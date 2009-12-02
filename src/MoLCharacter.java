import java.util.Vector;

public class MoLCharacter extends QObject implements MoLConstants {
		
	private int strength;						// in fight, carrying objects,
	private int mind;							// in fight, using various objects,
	private int dexterity;						// in fight
	private int endurance;						// when moving & fighting
	private int resistance;						// to hits
	private int life;							// 
	private int speed;							// maximum speed in px/frame !! there are no frames in this game
	private int recovery;						// gaining endurance points
	private int healing;						// healing speed
	private int skills;							// extension
	
	private MoLGameObject armour;				//
	private MoLGameObject weapon;				//
	private MoLGameObject shield;				//
	private MoLGameObject additional;			//
	
	private int direction;

												// it is where all the stuff is kept 
	private Vector equipment = new Vector(EQUIPMENT_INITIAL_SIZE);
	
	public int getDirection() {
		return direction;
	}


	public void setDirection(int direction) {
		this.direction = direction;
	}


	public void setMind(int mind) {
		this.mind = mind;
	}


	public void setDexterity(int dexterity) {
		this.dexterity = dexterity;
	}


	public void setResistance(int resistance) {
		this.resistance = resistance;
	}


	public void setLife(int life) {
		this.life = life;
	}


	public void setSpeed(int speed) {
		this.speed = speed;
	}


	public void setRecovery(int recovery) {
		this.recovery = recovery;
	}


	public void setHealing(int healing) {
		this.healing = healing;
	}


	public void setSkills(int skills) {
		this.skills = skills;
	}


	public void setShield(MoLGameObject shield) {
		this.shield = shield;
	}


	public void setEquipment(Vector equipment) {
		this.equipment = equipment;
	}


	public MoLCharacter(int id, QImage img, int x, int y, int cx, int cy, int width, int height,  int ax, int ay, int aradius, boolean living, int collisionType) {
		super(id, img, x, y, cx, cy, width, height, ax, ay, aradius, living, collisionType);
		direction = STAND_DOWN;
	}
	
	
	public boolean use(MoLGameObject obj) {
		if ( obj.checkRequirements(this) != OBJECT_ACT_NO_ERR) {
			// pass some information to the user
			return false;
		}
		obj.use(this);
		return true;
	}
	
	public void unuse(MoLGameObject obj) {
		obj.unuse(this);
	}

	public int getEndurance() {
		return endurance;
	}

	public void setEndurance(int endurance) {
		this.endurance = endurance;
	}

	public int getStrength() {
		return strength;
	}

	public int getMind() {
		return mind;
	}

	public int getDexterity() {
		return dexterity;
	}

	public int getResistance() {
		return resistance;
	}

	public int getLife() {
		return life;
	}

	public int getSpeed() {
		return speed;
	}

	public int getRecovery() {
		return recovery;
	}

	public int getHealing() {
		return healing;
	}

	public int getSkills() {
		return skills;
	}

	public MoLGameObject getArmour() {
		return armour;
	}

	public MoLGameObject getWeapon() {
		return weapon;
	}

	public MoLGameObject getShield() {
		return shield;
	}

	public MoLGameObject getAdditional() {
		return additional;
	}

	public Vector getEquipment() {
		return equipment;
	}
	
	public void moveLeft() {
		if (direction != WALK_LEFT) {
			setDirection(WALK_LEFT);	
			setCurrentAnimation(WALK_LEFT);
		}
		setX( getX() - speed );
	}
	public void moveRight() {
		if (direction != WALK_RIGHT) {
			setDirection(WALK_RIGHT);
			setCurrentAnimation(WALK_RIGHT);			
		}
		setX( getX() + speed );
	}
	public void moveUp() {
		if (direction != WALK_UP) {
			setDirection(WALK_UP);
			setCurrentAnimation(WALK_UP);
		}
		setY( getY() - speed );
	}
	public void moveDown() {
		if (direction != WALK_DOWN) {
			setDirection(WALK_DOWN);
			setCurrentAnimation(WALK_DOWN);
		}
		setY( getY() + speed );
		// setY( getY() + speed  - MoLMap.speed );
		// endurance -= MoLMap.surface;
	}
	
	public void respondToCollision(int collisionResponse) {
		switch (collisionResponse) {
			case OBJECT_RESPONSE_BOUNCE_OFF:
				setCoordinates(this.getPX(), this.getPy());
				break;

			default:
				break;
		}
	}

}
