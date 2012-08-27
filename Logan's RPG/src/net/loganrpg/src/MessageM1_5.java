package net.loganrpg.src;

public class MessageM1_5 extends Message
{

	public MessageM1_5(Session s)
	{
		super(s);
		texts = new String[7];
		
		texts[0] = "JONNY: " + Session.player.getName() + ", have you seen this?! I don't know what happened but ¬ I've never seen one of these aliens dead before!";
		texts[1] = "JOE: Wait no... It's moving! RUN!";
		texts[2] = "JOE: You two alright?";
		texts[3] = "JONNY: *COUGH* Yes I'm fine.";
		texts[4] = "JOE: That explosion changed me... It's like I have some... hidden power...";
		texts[5] = "JONNY: WAIT! LOOK OUT! ALIENS to the east!";
		texts[6] = "HINT: Press 'S' to use your spell!";
	}
	
	public void advance()
	{
		super.advance();
		if(this.getCurrentIndex() == 2)
		{
			Session.player.spell1 = Spell.firestrike;
		}
		
		if(this.getCurrentIndex() == 6)
		{
			Session.currentarea.putNPC(Session.player.getDeltaX() + 10, Session.player.getDeltaY(), new NPCtestnpc(Core.currentsession, "ALIEN1").setHealth(50));
			Session.currentarea.getNPCByName("JOE").attack(Session.currentarea.getNPCByName("ALIEN1"));
		}
	}

}
