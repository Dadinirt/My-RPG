package net.loganrpg.src;

public class Message
{

	/**
	 * List of all messages
	 */
	public static final Message testmsg;
	public static final Message nurseintro;
	public static final Message nurseexit;
	
	/**
	 * Array storing all the text screens in a section of dialogue
	 */
	protected String[] texts;
	
	/**
	 * The current index the text is up to
	 */
	private int currentIndex = 0;
	
	/**
	 * Parent session the message is being spoken in
	 */
	protected static Session cs;
	
	public Message(Session s)
	{
		/**
		 * Define message text in seperate classes
		 */
		cs = s;
	}
	
	/**
	 * Called upon completion of the dialogue
	 */
	protected void finishDialogue()
	{
		currentIndex = 0;
	}
	
	/**
	 * Returns the current line of text to print
	 */
	protected String getText()
	{
		try
		{
			return texts[currentIndex];
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			Core.currentsession.finishTalking();
		}
		return "";
	}
	
	/**
	 * Advances the message
	 */
	public void advance()
	{
		setCurrentIndex(getCurrentIndex() + 1);
	}
	
	/**
	 * Gets the total number of texts in the dialogue
	 */
	public int getLength()
	{
		return texts.length;
	}

	public int getCurrentIndex()
	{
		return currentIndex;
	}

	private void setCurrentIndex(int currentIndex)
	{
		this.currentIndex = currentIndex;
	}
	
	/**
	 * Defines all messages
	 */
	static
	{
		testmsg = new MessageTestmsg(cs);
		nurseintro = new MessageNurseIntro(cs);
		nurseexit = new MessageNurseExit(cs);
	}
	
}
