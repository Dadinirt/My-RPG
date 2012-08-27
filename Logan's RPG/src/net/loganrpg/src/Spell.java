package net.loganrpg.src;

import org.newdawn.slick.opengl.Texture;

public class Spell
{
	
	/**
	 * List of spells
	 * Only really used by NPCs
	 */
	public static final Spell firestrike;

	/**
	 * Session Spell is part of
	 */
	private Session parent;
	
	/**
	 * Number of times the spell has been ticked
	 */
	private int ticks = 0;
	
	/**
	 * Delta co-ordinates of the spell
	 */
	private float deltaX;
	private float deltaY;
	private float deltaD;
	
	/**
	 * Person who cast the spell
	 */
	private Object caster;
	
	/**
	 * Type of spell this is, used to get textures
	 */
	private SpellType spelltype;
	
	/**
	 * Direction spell is facing
	 */
	private Direction direction = Direction.UP;
	
	/**
	 * Speed spell moves
	 */
	private float spellSpeed = 0.1f;
	
	/**
	 * Damage the spell does
	 */
	private float damage = -10f;
	
	/**
	 * Amount of magic points the spell costs to cast
	 */
	private int spellCost = 20;
	
	/**
	 * Main spell constructor
	 */
	public Spell(Session parent, Object caster, int x, int y, Direction d, float speed, SpellType type)
	{
		this.parent = parent;
		this.caster = caster;
		deltaX = x;
		deltaY = y;
		direction = d;
		spellSpeed = speed;
		spelltype = type;
	}
	
	/**
	 * Updates the spell (mainly position)
	 */
	public void update()
	{
		switch (direction)
		{
		case UP:
			this.deltaY -= spellSpeed;
			break;
		case DOWN:
			this.deltaY += spellSpeed;
			break;
		case LEFT:
			this.deltaX -= spellSpeed;
			break;
		case RIGHT:
			this.deltaX += spellSpeed;
			break;
		}
		
		this.deltaD += spellSpeed;
		
		try{
			if(parent.currentarea.tiles[this.getDeltaX()][this.getDeltaY()].isOccupied())
			{
				if(parent.getOccupier(getDeltaX(), getDeltaY()) instanceof NPC && parent.getOccupier(getDeltaX(), getDeltaY()) != caster)
				{
					parent.currentarea.NPCs.get(parent.getOccupierListPosition(getDeltaX(), getDeltaY())).modifyHealth((int) this.damage);
					parent.currentarea.removeSpell(this);
				}
				
				if(parent.getOccupier(getDeltaX(), getDeltaY()) instanceof Player && parent.getOccupier(getDeltaX(), getDeltaY()) != caster)
				{
					Session.player.healHealth((int) this.damage);
					parent.currentarea.removeSpell(this);
				}
			}
			if(parent.currentarea.tiles[this.getDeltaX()][this.getDeltaY()].isPassable())
			{
				
			}
		}
		catch(NullPointerException e)
		{
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			parent.currentarea.removeSpell(this);
		}
	}
	
	/**
	 * Ticks the spell
	 */
	public void tick()
	{
		ticks++;
	}
	
	/**
	 * Returns the texture for the spell based on it's type
	 */
	public Texture getTexture()
	{
		switch(spelltype)
		{
		case FIRE:
			switch(direction)
			{
			case UP:
				return Tex.firespellu;
			case DOWN:
				return Tex.firespelld;
			case LEFT:
				return Tex.firespelll;
			case RIGHT:
				return Tex.firespellr;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns x and y co-ordiantes of the spell
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
	 * Returns the direction of the spell
	 */
	public Direction getDirection()
	{
		return direction;
	}
	
	/**
	 * Returns amount of magic points used up by casting
	 */
	public int getCost()
	{
		return spellCost;
	}
	
	/**
	 * Returns damage of the spell
	 */
	public float getDamage()
	{
		return damage;
	}
	
	/**
	 * Sets the deltas of the spell
	 * @return 
	 */
	public Spell setDeltas(int x, int y, Direction d)
	{
		deltaX = x;
		deltaY = y;
		direction = d;
		return this;
	}
	
	static
	{
		firestrike = new SpellFirestrike(null, null, 0, 0, null);
	}
	
}
