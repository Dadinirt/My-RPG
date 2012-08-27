package net.loganrpg.src;

public class MessageS1_3_J extends Message
{

	public MessageS1_3_J(Session s)
	{
		super(s);
		texts = new String[4];
		texts[0] = "JONNY: Good, " + Session.player.getName() + ", you're awake. You remember us, ¬ JONNY and JOE right?";
		texts[1] = "JOE: The situation has got a lot worse since your accident. This is now the ¬ only village completely free of alien infestation.";
		texts[2] = "JONNY: And we are the only people left who have any chance of getting out.  We ¬ need to leave now, while there is still hope!";
		texts[3] = "JOE: We'll see you by the road to the north, quick!";
	}

}
