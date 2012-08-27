package net.loganrpg.src;

public class AreaTestbox extends Area
{

	public AreaTestbox(Session s, int width, int height)
	{
		super(s, width, height);
	}

	protected void buildArea()
	{
		for(int x = 0; x < this.getWidth(); x++)
		{
			for(int y = 0; y < this.getHeight(); y++)
			{
				tiles[x][y] = new TileGrass(Tex.grass);
			}
		}
	}

}
