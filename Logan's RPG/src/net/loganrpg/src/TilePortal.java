package net.loganrpg.src;

public class TilePortal extends Tile
{
	
	private int dest;
	private Area destination;
	private int tx;
	private int ty;

	public TilePortal(int dest, int x, int y)
	{
		super(Tex.portal);
		this.dest = dest;
		tx = x;
		ty = y;
		isSpecial = true;
	}
	
	public void checkNotify()
	{
		destination = Area.getFromID(dest);
		Session.changeArea(destination, tx, ty);
	}

}
