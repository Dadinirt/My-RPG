package net.loganrpg.src;

public class MessageNurseExit extends Message
{

	public MessageNurseExit(Session s)
	{
		super(s);
		texts = new String[1];
		texts[0] = "Ah so your name is " + Session.player.getName() +"? Well " + Session.player.getName() + ", you are free to go and explore the world!";
	}

}
