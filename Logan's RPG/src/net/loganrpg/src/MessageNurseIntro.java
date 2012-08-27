package net.loganrpg.src;

public class MessageNurseIntro extends Message
{

	public MessageNurseIntro(Session s)
	{
		super(s);
		texts = new String[3];
		texts[0] = "Welcome to Logan's RPG!";
		texts[1] = "Good, it looks like you are fully awake now.";
		texts[2] = "Well, first things first, can you remember your name? Talk to me when ¬ you can!";
	}

}
