package net.loganrpg.src;

public class AreaBeginninghospital extends Area
{

	public AreaBeginninghospital(Session s, int width, int height)
	{
		super(s, width, height);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void buildArea()
	{
		for(int x = 0; x < getWidth(); x++)
		{
			for(int y = 0; y < getHeight(); y++)
			{
				tiles[x][y] = new TileTiledfloor(Tex.tiledfloor);
			}
		}
		
		tiles[5][9] = new TilePortal(Area.testbox, 1, 1);
	}

}
