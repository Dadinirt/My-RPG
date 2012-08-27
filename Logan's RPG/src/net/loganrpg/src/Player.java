package net.loganrpg.src;

import org.newdawn.slick.opengl.Texture;

public class Player {
	
	/**
	 * A reference back to the session the player is part of
	 */
	Session parent;

	/**
	 * Name of the player
	 */
	private String playername = "DEFAULT";
	
	/**
	 * Gender of the player
	 */
	private Gender playergender;
	
	/**
	 * Spells the player currently has
	 */
	public Spell spell1 = new SpellFirestrike(parent, this, getDeltaX(), getDeltaY(), getDirection());
	
	/**
	 * Flip textures or not?
	 */
	private boolean fliptex = false;
	
	/**
	 * Player Stats
	 */
	private int health = 100;
	
	/**
	 * Count of ticks
	 */
	private float ticks = 0;
	
	/**
	 * Is the player moving?
	 */
	private boolean isMoving = false;
	
	/**
	 * Walk and run speeds of player per update
	 */
	private float walkspeed = 0.05f;
	private float runspeed = 0.1f;
	
	/**
	 * Direction the player is facing
	 */
	private Direction direction = Direction.UP;
	
	/**
	 * Type of movement the character will make
	 */
	private Movetype movetype = Movetype.RUN;
	
	/**
	 * Distance walked so far - used to stop movement at 1
	 */
	private float deltaD = 0;
	
	/**
	 * Delta co-ordinates of the player
	 */
	private float deltaX = 0;
	private float deltaY = 0;
	
	public Player(Session ses, String name, Gender gend)
	{
		playername = name;
		playergender = gend;
		parent = ses;
	}
	
	/**
	 * Called to update the player
	 */
	public void updatePlayer()
	{
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
	}
	
	/**
	 * Called to tick the player
	 */
	public void tick()
	{
		ticks++;
		if(fliptex)
		{
			fliptex = false;
		}
		else
		{
			fliptex = true;
		}
	}
	
	/**
	 * Returns the name of the player
	 */
	public String getName()
	{
		return playername;
	}
	
	/**
	 * Returns the gender of the player
	 */
	public Gender getGender()
	{
		return playergender;
	}
	
	/**
	 * Returns the Delta X co-ordinate of the player
	 */
	public int getDeltaX()
	{
		return Math.round(deltaX);
	}
	
	/**
	 * Returns the Delta Y co-ordinate of the player
	 */
	public int getDeltaY()
	{
		return Math.round(deltaY);
	}
	
	/**
	 * Returns the absolute X co-ordinate of the player (don't think this is ever used)
	 */
	public float getAbsX()
	{
		return deltaX;
	}
	
	/**
	 * Returns the absolute Y co-ordinate of the player (don't think this is ever used)
	 */
	public float getAbsY()
	{
		return deltaY;
	}
	
	/**
	 * Called to try and initiate a move by the player
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
			
			if(!Session.currentarea.tiles[this.getDeltaX()][this.getDeltaY()-1].isPassable())
			{
				this.movetype = Movetype.FAIL;
				return;
			}
			
			this.movetype = m;
			this.isMoving = true;
			Session.currentarea.tiles[this.getDeltaX()][this.getDeltaY()].setOccupied(false);
			
		}
		
		if(d == Direction.DOWN)
		{
			
			if(this.getDeltaY() == Session.currentarea.getHeight() - 1)
			{
				this.movetype = Movetype.FAIL;
				return;
			}
			
			try
			{
				if(!Session.currentarea.tiles[this.getDeltaX()][this.getDeltaY()+1].isPassable())
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
			Session.currentarea.tiles[this.getDeltaX()][this.getDeltaY()].setOccupied(false);
			
		}
		
		if(d == Direction.LEFT)
		{
			
			if(this.getDeltaX() == 0)
			{
				this.movetype = Movetype.FAIL;
				return;
			}
			
			if(!Session.currentarea.tiles[this.getDeltaX()-1][this.getDeltaY()].isPassable())
			{
				this.movetype = Movetype.FAIL;
				return;
			}
			
			this.movetype = m;
			this.isMoving = true;
			Session.currentarea.tiles[this.getDeltaX()][this.getDeltaY()].setOccupied(false);
			
		}
		
		if(d == Direction.RIGHT)
		{
			
			if(this.getDeltaX() == Session.currentarea.getWidth())
			{
				this.movetype = Movetype.FAIL;
				return;
			}
			
			try
			{
				if(!Session.currentarea.tiles[this.getDeltaX()+1][this.getDeltaY()].isPassable())
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
			Session.currentarea.tiles[this.getDeltaX()][this.getDeltaY()].setOccupied(false);
			
		}
	}
	
	/**
	 * Returns the direction the player is facing
	 */
	public Direction getDirection()
	{
		return direction;
	}
	
	/**
	 * Sets the direction of the player
	 */
	public void setDirection(Direction d)
	{
		direction = d;
	}
	
	/**
	 * Returns true if the player is moving
	 */
	public boolean isMoving()
	{
		return isMoving;
	}
	
	/**
	 * Returns the texture to use to draw the player
	 */
	public Texture getTexture()
	{
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
	
	/**
	 * Fires a spell by number
	 */
	public void castSpell(int index)
	{
		switch(index)
		{
		case 1:
			if(this.spell1 instanceof SpellFirestrike)
			{
				Session.currentarea.addSpell(new SpellFirestrike(parent, this, getDeltaX(), getDeltaY(), getDirection()));
			}
		}
	}
	
	/**
	 * Called when the player finishes moving
	 */
	public void finishMove()
	{
		parent.checkOccupied();
		deltaD = 0;
		this.isMoving = false;
		if(Session.currentarea.tiles[this.getDeltaX()][this.getDeltaY()].isSpecial())
		{
			Session.currentarea.tiles[this.getDeltaX()][this.getDeltaY()].checkNotify();
		}
	}
	
	/**
	 * Returns the health of the player
	 */
	public int getHealth()
	{
		return health;
	}
	
	/**
	 * Hurts the player's health
	 * @param amount - the amount to hurt the player
	 */
	public void hurtHealth(int amount)
	{
		health -= amount;
	}
	
	/**
	 * Heals the player's health
	 * @param amount - the amount to heal the player
	 */
	public void healHealth(int amount)
	{
		health += amount;
	}
	
	/**
	 * Sets the player deltas as specified
	 */
	public void setDeltas(int x, int y)
	{
		this.deltaX = x;
		this.deltaY = y;
	}
	
	/**
	 * Gets the player as an NPC, used for NPC's AI
	 */
	public NPC getAsNPC()
	{
		return new NPC(null, "PLAYER").setDeltas(this.getDeltaX(), this.getDeltaY()).setDirect(getDirection()).setTeam(1).setHealth(getHealth());
	}
	
	/**
	 * Sets the player name
	 */
	public void setName(String name)
	{
		playername = name;
	}
	
}
