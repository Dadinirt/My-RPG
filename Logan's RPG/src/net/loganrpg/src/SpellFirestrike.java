package net.loganrpg.src;

public class SpellFirestrike extends Spell
{

	public SpellFirestrike(Session p, Object c, int x, int y, Direction d)
	{
		super(p, c, x, y, d, 0.1f, SpellType.FIRE);
	}

}
