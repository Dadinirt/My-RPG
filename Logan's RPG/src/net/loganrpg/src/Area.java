package net.loganrpg.src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Area
{

	/**
	 * Array of tiles in the area
	 */
	protected Tile[][] tiles;
	
	/**
	 * The width and height of the area
	 */
	private final int width;
	private final int height;
	
	/**
	 * ID of the level, used when constructing CSV portals
	 */
	public final int ID;
	
	/**
	 * Arraylist of all NPCs in the area
	 */
	protected ArrayList<NPC> NPCs = new ArrayList<NPC>();
	private ArrayList<NPC> rmvNPCs = new ArrayList<NPC>();
	
	/**
	 * Arraylist of all the spells currently active
	 */
	protected ArrayList<Spell> spells = new ArrayList<Spell>();
	private ArrayList<Spell> rmvspells = new ArrayList<Spell>();
	
	/**
	 * Constructor for a CSV loaded area
	 */
	public Area(int id, int width, int height)
	{
		this.width = width;
		this.height = height;
		tiles = new Tile[width][height];
		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				tiles[x][y] = Tile.def;
			}
		}
		this.ID = id;
	}

	/**
	 * Constructor for an area
	 * 
	 * @param width
	 *            - the width of the area in tiles
	 * @param height
	 *            - the height of the area in tiles
	 * @note Will set every tile to the void texture by default, then attempt to
	 *       build the area by calling buildArea()
	 */
	public Area(Session s, int width, int height)
	{
		ID = -1;
		this.width = width;
		this.height = height;
		tiles = new Tile[width][height];

		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				tiles[x][y] = Tile.def;
			}
		}

//		buildArea();
	}
	
	/**
	 * Updates the area for things which require it ASAP
	 */
	public void update()
	{
		//Update all NPCs
		for(int i = 0; i < NPCs.size(); i++)
		{
			NPCs.get(i).update();
			if(NPCs.get(i).isDead())
			{
				rmvNPCs.add(NPCs.get(i));
			}
		}
		
		//Update all spells
		for(int i = 0; i < spells.size(); i++)
		{
			spells.get(i).update();
		}
		
		NPCs.removeAll(rmvNPCs);
		spells.removeAll(rmvspells);
		rmvspells.clear();
		rmvNPCs.clear();
	}
	
	/**
	 * Ticks the area for things which require ticking
	 */
	public void tick()
	{
		//Tick all NPCs
		for(int i = 0; i < NPCs.size(); i++)
		{
			NPCs.get(i).tick();
		}
		
		//Tick all spells
		for(int i = 0; i < spells.size(); i++)
		{
			spells.get(i).tick();
		}
	}

	/**
	 * Builds the area. Must be defined in the specific area class.
	 */
//	protected abstract void buildArea();
	
	/**
	 * Checks which tiles are occupied and marks them as so
	 */
	protected void checkOccupied()
	{
		//Reset all tiles to non occupied
		for(int x = 0; x < this.getWidth(); x++)
		{
			for(int y = 0; y < this.getHeight(); y++)
			{
				tiles[x][y].setOccupied(false);
			}
		}
//		tiles[Core.currentsession.player.getDeltaX()][Core.currentsession.player.getDeltaY()].setOccupied(true);
		//Go through all NPCs and see what space they are on
		for(int i = 0; i < NPCs.size(); i++)
		{
			tiles[NPCs.get(i).getDeltaX()][NPCs.get(i).getDeltaY()].setOccupied();
		}
	}
	
	/**
	 * Gets the width of the area
	 * @return - The width of the area in tiles
	 */
	public int getWidth()
	{
		return width;
	}
	
	/**
	 * Gets the height of the area
	 * @return - The height of the area in tiles
	 */
	public int getHeight()
	{
		return height;
	}
	
	/**
	 * Sets a tile as occupied
	 * @param dx - X co-ordinate of the tile to be made occupied
	 * @param dy - Y co-ordinate of the tile to be made occupied
	 * @param as - Tile is occupied: True/False?
	 */
	public void setTileOccupied(int dx, int dy, boolean as)
	{
		tiles[dx][dy].setOccupied(as);
	}
	
	/**
	 * Gets an NPC by name
	 */
	public NPC getNPCByName(String name)
	{
		for(int i = 0; i < NPCs.size(); i++)
		{
			if(NPCs.get(i).getName() == name)
			{
				return NPCs.get(i);
			}
		}
		
		return null;
	}
	
	/**
	 * Adds a spell to the area
	 */
	public void addSpell(Spell s)
	{
		spells.add(s);
	}
	
	/**
	 * Mark a spell for removal
	 */
	public void removeSpell(Spell s)
	{
		rmvspells.add(s);
	}
	
	/**
	 * Mark an NPC for removal
	 */
	public void removeNPC(NPC n)
	{
		rmvNPCs.add(n);
	}
	
	/**
	 * Puts NPCs, returns self for constructing convenience
	 */
	protected Area putNPC(int x, int y, NPC toput)
	{
		toput.setDeltas(x, y);
		NPCs.add(toput);
		return this;
	}
	
	/**
	 * Gets an area from ID
	 */
	public static Area getFromID(int id)
	{
		for(int i = 0; i < areas.size(); i++)
		{
			if(areas.get(i).ID == id)
			{
				return areas.get(i);
			}
		}
		
		System.out.println("Invalid ID!");
		return null;
	}

	/**
	 * ArrayList of areas, to get from ID
	 */
	public static ArrayList<Area> areas = new ArrayList<Area>();
	
	/**
	 * List of all areas
	 */
	public static Area testbox;
	public static Area begininghospital;

	static
	{
//		testbox = new AreaTestbox(parent, 10, 10).putNPC(5, 5, NPC.testnpc.setDirect(Direction.DOWN)).putNPC(2, 2, NPC.testenemy);
		try
		{
			testbox = CSVLoader.loadAreaCSV(new File("res/csv/RPGLevel test.csv"));
			begininghospital = CSVLoader.loadAreaCSV(new File("res/csv/beginning hospital.csv"));
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
