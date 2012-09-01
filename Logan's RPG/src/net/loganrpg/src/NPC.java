package net.loganrpg.src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.opengl.Texture;

public class NPC
{
	
	/**
	 * ArrayList of NPCs
	 */
	public static ArrayList<NPC> npcs = new ArrayList<NPC>();
	
	/**
	 * List of all NPCs
	 */
	public static final NPC testnpc;
	public static final NPC testenemy;
	public static NPC beginningnurse;
	
	/**
	 * ID of the NPC
	 */
	protected int ID;
	
	/**
	 * Session NPC is part of
	 */
	protected static Session parent;

	/**
	 * The name of the NPC
	 * Should always be uppercase for gameplay purposes
	 */
	private String NPCName;
	
	/**
	 * Type of NPC this is
	 */
	private NPCType type = NPCType.DEFAULT_BOY;
	
	/**
	 * Spells the NPC has
	 */
	private Spell spell1;
	
	/**
	 * Team NPC is on. Will actively attack anyone other than on their team or team 0
	 */
	private int team = 0;
	
	/**
	 * The awareness radius of the NPC. Will attack anything that enters it if on different team
	 */
	private int awareness = 5;
	
	/**
	 * The skill level of the NPC
	 */
	private SkillLevel skill = SkillLevel.NOVICE;
	private int recastrate = 30;
	private int recastin = recastrate;
	
	/**
	 * Is the NPC dead?
	 */
	private boolean isDead = false;
	
	/**
	 * Number of times this NPC has been ticked
	 */
	private int ticks = 0;
	
	/**
	 * Delta co-ordinates of the NPC and D for distance
	 */
	private float deltaX = 0;
	private float deltaY = 0;
	private float deltaD = 0;
	
	/**
	 * The health of the NPC
	 * -1 if NPC is invulnerable
	 */
	private int NPCHealth = 10;
	private int NPCMaxHealth = 10;
	
	/**
	 * Magic points of the NPC
	 */
	private int NPCMagic = 100;
	
	/**
	 * Direction NPC is facing
	 */
	private Direction direction = Direction.UP;
	
	/**
	 * Current type of movement for the NPC
	 */
	private Movetype movetype = Movetype.WALK;
	
	/**
	 * Walk and run speeds of the NPC
	 */
	private float walkspeed = 0.05f;
	private float runspeed = 0.09f;
	
	/**
	 * Is the NPC currently moving?
	 */
	private boolean isMoving = false;
	
	/**
	 * Variables used to calculate attacks
	 */
	private boolean isAttackingPlayer = false;
	private NPC target;
	private boolean isAttacking = false;
	
	/**
	 * Has the NPC been scheduled a move and what are the destinations?
	 */
	private boolean moveRequested = false;
	private boolean invert;
	private int targetx;
	private int targety;
	
	/**
	 * Does the NPC move or stand still
	 */
	private boolean doesWander = false;
	
	public NPC(Session s, String name)
	{
		parent = Core.currentsession;
		NPCName = name;
		switch(skill)
		{
		case NOVICE:
			recastrate = 30;
		case AVERAGE:
			recastrate = 20;
		case SKILLED:
			recastrate = 10;
		case HEROIC:
			recastrate = 5;
		}
	}
	
	/**
	 * Updates the NPC
	 */
	public void update()
	{
		if(moveRequested && !isMoving)
		{
			if(invert)
			{
				if(this.getDeltaX() > targetx)
				{
					tryMove(Direction.LEFT, Movetype.WALK);
				}
				else if(this.getDeltaX() < targetx)
				{
					tryMove(Direction.RIGHT, Movetype.WALK);
				}
				
				if(this.getDeltaY() > targety)
				{
					tryMove(Direction.UP, Movetype.WALK);
				}
				else if(this.getDeltaY() < targety)
				{
					tryMove(Direction.DOWN, Movetype.WALK);
				}
			}
			else
			{
				if(this.getDeltaY() > targety)
				{
					tryMove(Direction.UP, Movetype.WALK);
				}
				else if(this.getDeltaY() < targety)
				{
					tryMove(Direction.DOWN, Movetype.WALK);
				}
				
				if(this.getDeltaX() > targetx)
				{
					tryMove(Direction.LEFT, Movetype.WALK);
				}
				else if(this.getDeltaX() < targetx)
				{
					tryMove(Direction.RIGHT, Movetype.WALK);
				}
			}
			
			if(this.getDeltaX() == targetx && this.getDeltaY() == targety && !isMoving)
			{
				this.isMoving = false;
				this.moveRequested = false;
			}
		}
		
		if(this.isMoving())
		{
			if(movetype == Movetype.WALK)
			{
				switch(direction)
				{
				case UP:
					deltaY -= walkspeed;
					break;
				case DOWN:
					deltaY += walkspeed;
					break;
				case LEFT:
					deltaX -= walkspeed;
					break;
				case RIGHT:
					deltaX += walkspeed;
					break;
				}
				
				deltaD += walkspeed;
				
				if(deltaD >= 1)
				{
					this.finishMove();
				}
			}
			if(movetype == Movetype.RUN)
			{
				switch(direction)
				{
				case UP:
					deltaY -= runspeed;
					break;
				case DOWN:
					deltaY += runspeed;
					break;
				case LEFT:
					deltaX -= runspeed;
					break;
				case RIGHT:
					deltaX += runspeed;
					break;
				}
				
				deltaD += runspeed;
				
				if(deltaD >= 1)
				{
					this.finishMove();
				}
			}
		}
		if(this.NPCHealth <= 0)
		{
			this.isDead = true;
		}
	}
	
	/**
	 * Ticks the NPC
	 */
	public void tick()
	{
		ticks++;
		if(isAttacking)
		{
			if(isAttackingPlayer)
			{
				target = Session.player.getAsNPC();
				thinkAttack();
			}
			else
			{
				thinkAttack();
				checkTargetState();
			}
		}
		recastin--;
	}
	
	/**
	 * Called to finish a move by the NPC
	 */
	private void finishMove()
	{
		Core.currentsession.checkOccupied();
		deltaD = 0;
		this.isMoving = false;
		if(Core.currentsession.currentarea.tiles[this.getDeltaX()][this.getDeltaY()].isSpecial())
		{
			Core.currentsession.currentarea.tiles[this.getDeltaX()][this.getDeltaY()].checkNotify();
		}
	}
	
	/**
	 * Gets the NPC to attack a target
	 */
	public void attack(NPC target)
	{
		this.target = target;
		isAttackingPlayer = false;
		isAttacking = true;
	}
	public void attackplayer()
	{
		isAttackingPlayer = true;
		isAttacking = true;
	}
	
	/**
	 * The intelligence algorithms for the NPC when attacking
	 */
	private void thinkAttack()
	{
		boolean decisionfinal = false;
		int spellselected = 0;
		Direction neededdirection = this.getDirection();
		boolean usehealing = true;
		// Can the NPC fire a spell that will hit it's target?
		for(int i = 1; i == 1; i++)
		{
			switch(i)
			{
			case 1:
				if(spell1.getCost() < this.getMagic() && spell1.getDamage() < 0)
				{
					spellselected = 1;
					usehealing = false;
				}
				break;
			}
		}
		
		if(!usehealing && !isMoving())
		{
			if(this.getDeltaX() == target.getDeltaX() && this.getDeltaY() > target.getDeltaY())
			{
				neededdirection = Direction.UP;
				decisionfinal = true;
				this.setDirection(neededdirection);
				castSpell(spellselected);
			}
			else if(this.getDeltaX() == target.getDeltaX() && this.getDeltaY() < target.getDeltaY())
			{
				neededdirection = Direction.DOWN;
				decisionfinal = true;
				this.setDirection(neededdirection);
				castSpell(spellselected);
			}
			if(this.getDeltaX() > target.getDeltaX() && this.getDeltaY() == target.getDeltaY())
			{
				neededdirection = Direction.LEFT;
				decisionfinal = true;
				this.setDirection(neededdirection);
				castSpell(spellselected);
			}
			else if(this.getDeltaX() > target.getDeltaX())
			{
				if(this.getDeltaY() > target.getDeltaY())
				{
					if((this.getDeltaY() - target.getDeltaY()) < (this.getDeltaX() - target.getDeltaX()))
					{
						if(this.getDeltaX() > target.getDeltaX())
						{
							tryMove(Direction.LEFT, Movetype.WALK);
						}
						else
						{
							tryMove(Direction.RIGHT, Movetype.WALK);
						}
					}
					else
					{
						if(this.getDeltaY() > target.getDeltaY())
						{
							tryMove(Direction.UP, Movetype.WALK);
						}
						else
						{
							tryMove(Direction.DOWN, Movetype.WALK);
						}
					}
				}
				else
				{
					if((target.getDeltaY() - this.getDeltaY()) < (this.getDeltaX() - target.getDeltaX()))
					{
						if(this.getDeltaX() > target.getDeltaX())
						{
							tryMove(Direction.LEFT, Movetype.WALK);
						}
						else
						{
							tryMove(Direction.RIGHT, Movetype.WALK);
						}
					}
					else
					{
						if(this.getDeltaY() > target.getDeltaY())
						{
							tryMove(Direction.UP, Movetype.WALK);
						}
						else
						{
							tryMove(Direction.DOWN, Movetype.WALK);
						}
					}
				}
				
				return;
			}
			if(this.getDeltaX() < target.getDeltaX() && this.getDeltaY() == target.getDeltaY())
			{
				neededdirection = Direction.RIGHT;
				decisionfinal = true;
				this.setDirection(neededdirection);
				castSpell(spellselected);
			}
			else if(this.getDeltaX() < target.getDeltaX())
			{
				if(this.getDeltaY() > target.getDeltaY())
				{
					if((this.getDeltaY() - target.getDeltaY()) > (this.getDeltaX() - target.getDeltaX()))
					{
						if(this.getDeltaX() > target.getDeltaX())
						{
							tryMove(Direction.LEFT, Movetype.WALK);
						}
						else
						{
							tryMove(Direction.RIGHT, Movetype.WALK);
						}
					}
					else
					{
						if(this.getDeltaY() > target.getDeltaY())
						{
							tryMove(Direction.UP, Movetype.WALK);
						}
						else
						{
							tryMove(Direction.DOWN, Movetype.WALK);
						}
					}
				}
				else
				{
					if((target.getDeltaY() - this.getDeltaY()) < (this.getDeltaX() - target.getDeltaX()))
					{
						if(this.getDeltaX() > target.getDeltaX())
						{
							tryMove(Direction.LEFT, Movetype.WALK);
						}
						else
						{
							tryMove(Direction.RIGHT, Movetype.WALK);
						}
					}
					else
					{
						if(this.getDeltaY() > target.getDeltaY())
						{
							tryMove(Direction.UP, Movetype.WALK);
						}
						else
						{
							tryMove(Direction.DOWN, Movetype.WALK);
						}
					}
					return;
				}
			}
			
			return;
		}
		else if(!isMoving)
		{
			for(int i = 1; i == 1; i++)
			{
				switch(i)
				{
				case 1:
					if(spell1.getCost() < this.getMagic() && spell1.getDamage() > 0)
					{
						spellselected = 1;
					}
					break;
				}
			}
			
			if(spellselected != 0)
			{
				castSpell(spellselected);
			}
		}
	}
	
	/**
	 * Fires a spell by number
	 */
	public void castSpell(int index)
	{
		if(recastin < 0)
		{
			switch(index)
			{
			case 1:
				if(this.spell1 instanceof SpellFirestrike)
				{
					Core.currentsession.currentarea.addSpell(new SpellFirestrike(Core.currentsession, this, getDeltaX(), getDeltaY(), getDirection()));
				}
			}
			
			recastin = recastrate;
		}
	}
	
	/**
	 * Checks the target state to see if it is dead and therefor to finish attacking
	 */
	private void checkTargetState()
	{
		try
		{
			if(target.getHealth() == 0)
			{
				isAttacking = false;
			}
		}
		catch(NullPointerException e)
		{
			isAttacking = false;
		}
	}
	
	/**
	 * Gets the NPC's name
	 */
	public String getName()
	{
		return NPCName;
	}
	
	/**
	 * Gets the NPC's health
	 * @return - The NPC's health
	 */
	public int getHealth()
	{
		return NPCHealth;
	}
	
	/**
	 * Returns NPC's magic points remaining
	 */
	public int getMagic()
	{
		return NPCMagic;
	}
	
	/**
	 * Methods to hurt and heal health stat
	 * @param amount - Amount to modify health
	 */
	public void hurtHealth(int amount)
	{
		NPCHealth -= amount;
	}
	public void healHealth(int amount)
	{
		NPCHealth += amount;
	}
	public void modifyHealth(int amount)
	{
		NPCHealth += amount;
	}
	
	/**
	 * Returns the co-ordinates of the NPC
	 */
	public int getDeltaX()
	{
		return Math.round(deltaX);
	}
	public int getDeltaY()
	{
		return Math.round(deltaY);
	}
	
	public float getAbsX()
	{
		return deltaX;
	}
	public float getAbsY()
	{
		return deltaY;
	}
	
	/**
	 * Schedules a move for the NPC to make to certain co-ordinates.
	 * The third parameter is TRUE to move X then Y, FALSE for Y then X
	 */
	public void scheduleMove(int targetx, int targety, boolean invert)
	{
		this.invert = invert;
		this.targetx = targetx;
		this.targety = targety;
		this.moveRequested = true;
	}
	
	/**
	 * Method called to try to move the NPC
	 */
	public void tryMove(Direction d, Movetype m)
	{
		this.direction = d;
		
		if(d == Direction.UP)
		{
			
			if(this.getDeltaY() == 0)
			{
				this.movetype = Movetype.FAIL;
				return;
			}
			
			if(!Core.currentsession.currentarea.tiles[this.getDeltaX()][this.getDeltaY()-1].isPassable() || Core.currentsession.currentarea.tiles[this.getDeltaX()][this.getDeltaY()-1].isOccupied())
			{
				this.movetype = Movetype.FAIL;
				return;
			}
			
			this.movetype = m;
			this.isMoving = true;
			Core.currentsession.currentarea.tiles[this.getDeltaX()][this.getDeltaY()].setOccupied(false);
			
		}
		
		if(d == Direction.DOWN)
		{
			
			if(this.getDeltaY() == Core.currentsession.currentarea.getHeight() - 1)
			{
				this.movetype = Movetype.FAIL;
				return;
			}
			
			try
			{
				if(!Core.currentsession.currentarea.tiles[this.getDeltaX()][this.getDeltaY()+1].isPassable() || Core.currentsession.currentarea.tiles[this.getDeltaX()][this.getDeltaY()+1].isOccupied())
				{
					this.movetype = Movetype.FAIL;
					return;
				}
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				System.out.println("Caught.");
				this.movetype = Movetype.FAIL;
				return;
			}
			
			this.movetype = m;
			this.isMoving = true;
			Core.currentsession.currentarea.tiles[this.getDeltaX()][this.getDeltaY()].setOccupied(false);
			
		}
		
		if(d == Direction.LEFT)
		{
			
			if(this.getDeltaX() == 0)
			{
				this.movetype = Movetype.FAIL;
				return;
			}
			
			if(!Core.currentsession.currentarea.tiles[this.getDeltaX()-1][this.getDeltaY()].isPassable() || Core.currentsession.currentarea.tiles[this.getDeltaX()-1][this.getDeltaY()].isOccupied())
			{
				this.movetype = Movetype.FAIL;
				return;
			}
			
			this.movetype = m;
			this.isMoving = true;
			Core.currentsession.currentarea.tiles[this.getDeltaX()][this.getDeltaY()].setOccupied(false);
			
		}
		
		if(d == Direction.RIGHT)
		{
			
			if(this.getDeltaX() == Core.currentsession.currentarea.getWidth())
			{
				this.movetype = Movetype.FAIL;
				return;
			}
			
			try
			{
				if(!Core.currentsession.currentarea.tiles[this.getDeltaX()+1][this.getDeltaY()].isPassable() || Core.currentsession.currentarea.tiles[this.getDeltaX()+1][this.getDeltaY()].isOccupied())
				{
					this.movetype = Movetype.FAIL;
					return;
				}
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				this.movetype = Movetype.FAIL;
				return;
			}
			
			this.movetype = m;
			this.isMoving = true;
			Core.currentsession.currentarea.tiles[this.getDeltaX()][this.getDeltaY()].setOccupied(false);
			
		}
	}
	
	/**
	 * Method called when NPC is interacted with (Usually initiates dialogue)
	 */
	public void interact()
	{
		
	}
	
	/**
	 * Returns the texture used to draw the NPC based on type
	 */
	public Texture getTexture()
	{
		switch(type)
		{
		case DEFAULT_BOY:
			switch(direction)
			{
			case UP:
				if(isMoving)
				{
					if(ticks % 4 == 2 || ticks % 4 == 0)
					{
						return Tex.defaultboyup;
					}
					else
					{
						return Tex.defaultboymoveup;
					}
				}
				else
				{
					return Tex.defaultboyup;
				}
			case DOWN:
				if(isMoving)
				{
					if(ticks % 4 == 2 || ticks % 4 == 0)
					{
						return Tex.defaultboydown;
					}
					else
					{
						return Tex.defaultboymovedown;
					}
				}
				else
				{
					return Tex.defaultboydown;
				}
			case LEFT:
				if(isMoving)
				{
					if(ticks % 4 == 2 || ticks % 4 == 0)
					{
						return Tex.defaultboyleft;
					}
					else
					{
						return Tex.defaultboymoveleft;
					}
				}
				else
				{
					return Tex.defaultboyleft;
				}
			case RIGHT:
				if(isMoving)
				{
					if(ticks % 4 == 2 || ticks % 4 == 0)
					{
						return Tex.defaultboyright;
					}
					else
					{
						return Tex.defaultboymoveright;
					}
				}
				else
				{
					return Tex.defaultboyright;
				}
			}
			
			return null;
		}
		
		return null;
	}
	
	/**
	 * Returns true if the NPC is dead
	 */
	public boolean isDead()
	{
		return this.isDead;
	}
	
	/**
	 * Returns the direction of the NPC
	 */
	public Direction getDirection()
	{
		return direction;
	}
	
	/**
	 * Sets the direction of the NPC
	 */
	public void setDirection(Direction d)
	{
		direction = d;
	}
	
	/**
	 * Returns true if the NPC is moving
	 */
	public boolean isMoving()
	{
		return isMoving;
	}
	
	/**
	 * Returns NPC team number
	 */
	public int getTeam()
	{
		return team;
	}
	
	/**
	 * Returns NPC awareness
	 */
	public int getAwareness()
	{
		return awareness;
	}
	
	/**
	 * Returns the skill level of the NPC
	 */
	public SkillLevel getSkill()
	{
		return skill;
	}
	
	/**
	 * Methods used when constructing NPCs in static{}
	 */
	protected NPC setHealth(int h)
	{
		NPCHealth = h;
		NPCMaxHealth = h;
		return this;
	}

	protected NPC setDeltas(int dX, int dY)
	{
		deltaX = dX;
		deltaY = dY;
		return this;
	}
	
	protected NPC setType(NPCType type)
	{
		this.type = type;
		return this;
	}
	
	protected NPC setDirect(Direction d)
	{
		this.direction = d;
		return this;
	}
	
	protected NPC setDirects(String s)
	{
		switch(s.toCharArray()[0])
		{
		case 'U':
			this.direction = Direction.UP;
			break;
		case 'D':
			this.direction = Direction.DOWN;
			break;
		case 'L':
			this.direction = Direction.LEFT;
			break;
		case 'R':
			this.direction = Direction.RIGHT;
			break;
		}
		return this;
	}
	
	protected NPC setTeam(int t)
	{
		team = t;
		return this;
	}
	
	protected NPC setID(int id)
	{
		this.ID = id;
		return this;
	}
	
	protected NPC setAwareness(int a)
	{
		awareness = a;
		return this;
	}
	
	protected NPC setSkill(SkillLevel s)
	{
		skill = s;
		return this;
	}
	
	protected NPC setSpell(int index, Spell toset)
	{
		switch(index)
		{
		case 1:
			spell1 = toset;
			break;
		}
		return this;
	}
	
	/**
	 * Gets an NPC by ID
	 */
	public static NPC getFromID(int id)
	{
		for(int i = 0; i < npcs.size(); i++)
		{
			if(npcs.get(i).ID == id)
			{
				return npcs.get(i);
			}
		}
		
		System.out.println("Invalid NPC ID!");
		return null;
	}
	
	static
	{
		testnpc = new NPCtestnpc(parent, "TEST").setHealth(50).setTeam(2).setSkill(SkillLevel.NOVICE).setSpell(1, Spell.firestrike);
		testenemy = new NPCtestnpc(parent, "ENEMY").setHealth(30).setTeam(1).setSkill(SkillLevel.NOVICE).setSpell(1, Spell.firestrike);
		beginningnurse = new NPCBeginningNurse(parent);
		try
		{
			beginningnurse = CSVLoader.loadNPCCSV(new File("res/csv/nurse.csv"));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
}
