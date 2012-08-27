package net.loganrpg.src;

public class TilePortal extends Tile
{
	
	private Area destination;
	private int tx;
	private int ty;

	public TilePortal(Area dest, int x, int y)
	{
		super(Tex.portal);
		destination = dest;
		tx = x;
		ty = y;
		isSpecial = true;
	}
	
	public void checkNotify()
	{
		Session.changeArea(destination, tx, ty);
	}

}
