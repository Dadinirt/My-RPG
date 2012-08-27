package net.loganrpg.src;

import org.newdawn.slick.opengl.Texture;

public class Tile {

	/**
	 * Texture of the tile
	 */
	private Texture tiletex;
	
	/**
	 * Has the tile got something on it?
	 */
	private boolean isOccupied = false;
	
	/**
	 * Can the tile be passed?
	 */
	private boolean isPassable = true;
	
	/**
	 * Does this tile need regular ticking?
	 */
	private boolean needsTick = false;
	
	/**
	 * Is this tile special (will something happen if a player stands on it)
	 */
	protected boolean isSpecial = false;
	
	/**
	 * Constructor - must set a texture for the tile
	 * @param texture
	 */
	public Tile(Texture texture)
	{
		tiletex = texture;
	}
	
	/**
	 * Ticks the tile if a regular tick is needed
	 */
	protected void tick()
	{
	}
	
	/**
	 * Checks conditions when tile is notified for specials
	 * Useful for doors - check direction player is facing though
	 */
	protected void checkNotify()
	{
	}
	
	/**
	 * Returns true if the tile is occupied
	 */
	protected boolean isOccupied()
	{
		return isOccupied;
	}
	
	/**
	 * Marks the tile as occupied
	 */
	protected void setOccupied()
	{
		isOccupied = true;
	}
	protected void setOccupied(boolean o)
	{
		isOccupied = o;
	}
	
	/**
	 * Returns true if the tile is passable
	 */
	protected boolean isPassable()
	{
		if(!isPassable)
		{
			return isPassable;
		}
		else if(this.isOccupied)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	/**
	 * Returns true if the tile needs regular ticks
	 */
	protected boolean requiresTick()
	{
		return needsTick;
	}
	/**
	 * Returns true if the tile is special and should be notified
	 */
	protected boolean isSpecial()
	{
		return isSpecial;
	}
	
	/**
	 * Returns the texture of the tile (used for drawing)
	 */
	protected Texture getTileTexture()
	{
		return tiletex;
	}

	/**
	 * List of all tiles used for building areas.
	 */
	public static final Tile def;
	public static final Tile grass;
	public static final Tile tiledfloor;
	
	static
	{
		grass = new TileGrass(Tex.grass);
		def = new TileDefault(Tex.def);
		tiledfloor = new TileTiledfloor(Tex.tiledfloor);
	}
	
}
