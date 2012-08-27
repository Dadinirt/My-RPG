package net.loganrpg.src;

public class MessageTestmsg extends Message
{

	public MessageTestmsg(Session cs)
	{
		super(cs);
		this.texts = new String[4];
		texts[0] = "This is a test message";
		texts[1] = "You should be able to scroll through with X";
		texts[2] = "This is the end of the message.";
		texts[3] = "This is a really really long text message thing to see when exactly we need to start ¬ splitting messages and stuff.";
	}
	
}
