package net.loganrpg.src;

public class NPCtestnpc extends NPC
{

	public NPCtestnpc(Session s, String name)
	{
		super(s, name);
	}
	
	public void interact()
	{
		Core.currentsession.startTalking(Message.testmsg);
	}

}
