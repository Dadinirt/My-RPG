package net.loganrpg.src;

public class NPCBeginningNurse extends NPC
{

	public NPCBeginningNurse(Session s)
	{
		super(s, "NURSE");
	}
	
	public void interact()
	{
		if(Core.currentsession.story == Story.M1_2)
		{
			Core.currentsession.startTalking(Message.nurseintro);
			Core.currentsession.story = Story.M1_2_1;
			return;
		}
		
		if(Core.currentsession.story == Story.M1_2_1)
		{
			Core.currentsession.getsetPlayerName();
			Core.currentsession.story = Story.M1_3;
			return;
		}
		
		if(Core.currentsession.story == Story.M1_3)
		{
			Core.currentsession.startTalking(new MessageNurseExit(parent));
			this.setDirection(Direction.RIGHT);
			this.scheduleMove(6, 9, false);
			return;
		}

	}

}
