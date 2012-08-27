package net.loganrpg.src;

import java.util.Scanner;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

public class Session {
	
	/**
	 * Variables used to count tick speed
	 */
	private static final int tickspeed = 100;
	private static long prevtime = 0;
	
	/**
	 * Font used in this session for all text
	 */
	private static UnicodeFont font;
	
	/**
	 * Current story position
	 */
	public static Story story = Story.M1_2;
	
	/**
	 * Booleans used to stop repeat key presses
	 */
	private boolean KEY_X = false;
	private boolean KEY_S = false;
	
	/**
	 * The current area the player is in
	 */
	protected static Area currentarea;
	
	/**
	 * The current player in the session
	 */
	protected static Player player;
	
	/**
	 * Is there currently a conversation happening?
	 */
	protected static boolean isTalking = false;
	
	/**
	 * The current message that is playing
	 */
	protected static Message currentdialogue;

	public Session()
	{
		initializeFonts();
		currentarea = Area.beginhospital;
		player = new Player(this, "ALEX", Gender.MALE);
		player.setDeltas(5, 5);
		new Message(this);
	}
	
	/**
	 * Called once and initializes the font used for any text
	 */
	@SuppressWarnings("unchecked")
	private void initializeFonts()
	{
		java.awt.Font awtFont = new java.awt.Font("Arial", java.awt.Font.BOLD, 16);
		font = new UnicodeFont(awtFont);
		font.getEffects().add(new ColorEffect(java.awt.Color.BLACK));
		font.addAsciiGlyphs();
		try
		{
			font.loadGlyphs();
		}
		catch(SlickException e)
		{
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Method to update elements which need it asap and to draw to the screen
	 */
	public void update()
	{
		long curtime = System.currentTimeMillis();
		if(curtime > prevtime + tickspeed)
		{
			tick();
			prevtime = System.currentTimeMillis();
		}
		
		pollInput();
		
		draw();
		
		currentarea.update();
		
		player.updatePlayer();
		
		checkOccupied();
		
//		System.out.println(currentarea.NPCs.get(0).getHealth());
	}
	
	/**
	 * Method which ticks game elements which require it
	 */
	public void tick()
	{
		player.tick();
//		pollInput();
		currentarea.tick();
	}
	
	/**
	 * Method to poll input for anything in the game session that needs it
	 */
	private void pollInput()
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_UP) && !player.isMoving() && !isTalking && player.getDirection() == Direction.UP)
		{
			if(!Keyboard.isKeyDown(Keyboard.KEY_Z))
			{
				player.tryMove(Direction.UP, Movetype.WALK);
			}
			else
			{
				player.tryMove(Direction.UP, Movetype.RUN);
			}
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_UP) && !player.isMoving() && !isTalking)
		{
			player.setDirection(Direction.UP);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT) && !player.isMoving() && !isTalking && player.getDirection() == Direction.LEFT)
		{
			if(!Keyboard.isKeyDown(Keyboard.KEY_Z))
			{
				player.tryMove(Direction.LEFT, Movetype.WALK);
			}
			else
			{
				player.tryMove(Direction.LEFT, Movetype.RUN);
			}
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_LEFT) && !player.isMoving() && !isTalking)
		{
			player.setDirection(Direction.LEFT);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && !player.isMoving() && !isTalking && player.getDirection() == Direction.RIGHT)
		{
			if(!Keyboard.isKeyDown(Keyboard.KEY_Z))
			{
				player.tryMove(Direction.RIGHT, Movetype.WALK);
			}
			else
			{
				player.tryMove(Direction.RIGHT, Movetype.RUN);
			}
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && !player.isMoving() && !isTalking)
		{
			player.setDirection(Direction.RIGHT);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN) && !player.isMoving() && !isTalking && player.getDirection() == Direction.DOWN)
		{
			if(!Keyboard.isKeyDown(Keyboard.KEY_Z))
			{
				player.tryMove(Direction.DOWN, Movetype.WALK);
			}
			else
			{
				player.tryMove(Direction.DOWN, Movetype.RUN);
			}
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_DOWN) && !player.isMoving() && !isTalking)
		{
			player.setDirection(Direction.DOWN);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_X))
		{
			if(!KEY_X)
			{
				if(isTalking)
				{
					Sounds.playSound(Sounds.select);
					currentdialogue.advance();
				}
				else
				{
					switch(player.getDirection())
					{
					case UP:
						if(getOccupier(player.getDeltaX(), player.getDeltaY()-1) instanceof NPC)
						{
							currentarea.NPCs.get(getOccupierListPosition(player.getDeltaX(), player.getDeltaY()-1)).interact();
							currentarea.NPCs.get(getOccupierListPosition(player.getDeltaX(), player.getDeltaY()-1)).setDirection(Direction.DOWN);
							Sounds.playSound(Sounds.select);
						}
						break;
					case DOWN:
						if(getOccupier(player.getDeltaX(), player.getDeltaY()+1) instanceof NPC)
						{
							currentarea.NPCs.get(getOccupierListPosition(player.getDeltaX(), player.getDeltaY()+1)).interact();
							currentarea.NPCs.get(getOccupierListPosition(player.getDeltaX(), player.getDeltaY()+1)).setDirection(Direction.UP);
							Sounds.playSound(Sounds.select);
						}
						break;
					case LEFT:
						if(getOccupier(player.getDeltaX()-1, player.getDeltaY()) instanceof NPC)
						{
							currentarea.NPCs.get(getOccupierListPosition(player.getDeltaX() - 1, player.getDeltaY())).interact();
							currentarea.NPCs.get(getOccupierListPosition(player.getDeltaX() - 1, player.getDeltaY())).setDirection(Direction.RIGHT);
							Sounds.playSound(Sounds.select);
						}
						break;
					case RIGHT:
						if(getOccupier(player.getDeltaX()+1, player.getDeltaY()) instanceof NPC)
						{
							currentarea.NPCs.get(getOccupierListPosition(player.getDeltaX() + 1, player.getDeltaY())).interact();
							currentarea.NPCs.get(getOccupierListPosition(player.getDeltaX() + 1, player.getDeltaY())).setDirection(Direction.LEFT);
							Sounds.playSound(Sounds.select);
						}
						break;
					}
				}
			}
			KEY_X = true;
		}
		else
		{
			KEY_X = false;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			if(!KEY_S)
			{
				player.castSpell(1);
			}
			KEY_S = true;
		}
		else
		{
			KEY_S = false;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_P))
		{
			currentarea.NPCs.get(0).attackplayer();
		}
	}
	
	/**
	 * Sets the name of the player
	 */
	public static void getsetPlayerName()
	{
		Scanner in = new Scanner(System.in);
		player.setName(in.next());
	}
	
	/**
	 * Gets the occupier of a tile
	 */
	public Object getOccupier(int x, int y)
	{
		if(player.getDeltaX() == x && player.getDeltaY() == y)
		{
			return player;
		}
		for(int i = 0; i < currentarea.NPCs.size(); i++)
		{
			if(currentarea.NPCs.get(i).getDeltaX() == x && currentarea.NPCs.get(i).getDeltaY() == y)
			{
				return currentarea.NPCs.get(i);
			}
		}
		return null;
	}
	public int getOccupierListPosition(int x, int y)
	{
		for(int i = 0; i < currentarea.NPCs.size(); i++)
		{
			if(currentarea.NPCs.get(i).getDeltaX() == x && currentarea.NPCs.get(i).getDeltaY() == y)
			{
				return i;
			}
		}
		
		return 0;
	}
	
	/**
	 * Sets an NPC after being modified by any other class
	 */
	public void setNPC(NPC n)
	{
		
	}
	
	/**
	 * Method which will start the player talking
	 */
	public static void startTalking(Message m)
	{
		currentdialogue = m;
		isTalking = true;
	}
	
	/**
	 * Method which finishes any dialogue
	 */
	public void finishTalking()
	{
		isTalking = false;
		currentdialogue.finishDialogue();
	}
	
	/**
	 * Method which checks which tiles are occupied and sets them to occupied
	 */
	public void checkOccupied()
	{
		currentarea.checkOccupied();
		this.currentarea.setTileOccupied(player.getDeltaX(), player.getDeltaY(), true);
	}
	
	/**
	 * Method to change area to a different one
	 */
	public static void changeArea(Area newa, int xs, int ys)
	{
		currentarea = newa;
		player.setDeltas(xs, ys);
	}
	
	/**
	 * Method to draw all graphics to the screen
	 */
	private void draw()
	{
		// First we will draw all the standard tiles in the area
		for(int x = 0; x < currentarea.getWidth(); x++)
		{
			for(int y = 0; y < currentarea.getHeight(); y++)
			{
				GL11.glPushMatrix();
				GL11.glLoadIdentity();
				GL11.glScalef(Core.SCALE, Core.SCALE, 1);
				GL11.glTranslatef((x - player.getAbsX()) * 16 + (Core.WIDTH/(Core.SCALE*16) * 8), (y - player.getAbsY()) * 16 + (Core.HEIGHT/(16*Core.SCALE) * 8), 0);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
				
				currentarea.tiles[x][y].getTileTexture().bind();
				
				GL11.glBegin(GL11.GL_QUADS);
				
				GL11.glTexCoord2f(0, 0);
				GL11.glVertex2f(0.0f, 0.0f);
				GL11.glTexCoord2f(0, 1);
				GL11.glVertex2f(0.0f, 16.0f);
				GL11.glTexCoord2f(1, 1);
				GL11.glVertex2f(16.0f, 16.0f);
				GL11.glTexCoord2f(1, 0);
				GL11.glVertex2f(16.0f, 0.0f);
				
				GL11.glEnd();
				GL11.glPopMatrix();
			}
		}
		
		// Next, draw the player on top of those tiles in the centre of the screen
		GL11.glLoadIdentity();
		GL11.glScalef(Core.SCALE, Core.SCALE, 1);
		GL11.glTranslatef(Core.WIDTH/(Core.SCALE*16) * 8, Core.HEIGHT/(16*Core.SCALE) * 8, 0);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		player.getTexture().bind();
		
		GL11.glBegin(GL11.GL_QUADS);
		
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2f(0.0f, 0.0f);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex2f(0.0f, 16.0f);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex2f(16.0f, 16.0f);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex2f(16.0f, 0.0f);
		
		GL11.glEnd();
		
		// Draw NPCs
		for(int i = 0 ; i < currentarea.NPCs.size(); i++)
		{
			GL11.glLoadIdentity();
			GL11.glScalef(Core.SCALE, Core.SCALE, 1);
			GL11.glTranslatef((currentarea.NPCs.get(i).getAbsX() - player.getAbsX()) * 16 + (Core.WIDTH/(Core.SCALE*16) * 8), (currentarea.NPCs.get(i).getAbsY() - player.getAbsY()) * 16 + (Core.HEIGHT/(16*Core.SCALE) * 8), 0);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			
			currentarea.NPCs.get(i).getTexture().bind();
			
			GL11.glBegin(GL11.GL_QUADS);
			
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2f(0.0f, 0.0f);
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex2f(0.0f, 16.0f);
			GL11.glTexCoord2f(1, 1);
			GL11.glVertex2f(16.0f, 16.0f);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex2f(16.0f, 0.0f);
			
			GL11.glEnd();
		}
		
		// Draw any spells
		for(int i = 0; i < currentarea.spells.size(); i++)
		{
			GL11.glLoadIdentity();
			GL11.glScalef(Core.SCALE, Core.SCALE, 1);
			GL11.glTranslatef((currentarea.spells.get(i).getAbsX() - player.getAbsX()) * 16 + (Core.WIDTH/(Core.SCALE*16) * 8), (currentarea.spells.get(i).getAbsY() - player.getAbsY()) * 16 + (Core.HEIGHT/(16*Core.SCALE) * 8), 0);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			
			currentarea.spells.get(i).getTexture().bind();
			
			GL11.glBegin(GL11.GL_QUADS);
			
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2f(0.0f, 0.0f);
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex2f(0.0f, 16.0f);
			GL11.glTexCoord2f(1, 1);
			GL11.glVertex2f(16.0f, 16.0f);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex2f(16.0f, 0.0f);
			
			GL11.glEnd();
		}
		
		// Draw the text frame and text if we are talking
		if(isTalking)
		{
			GL11.glLoadIdentity();
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			
			Tex.textframe.bind();
			
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			
			GL11.glBegin(GL11.GL_QUADS);
			
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2f(0.0f, Core.HEIGHT - (Core.WIDTH/4) + 16*Core.SCALE);
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex2f(0.0f, Core.HEIGHT);
			GL11.glTexCoord2f(1, 1);
			GL11.glVertex2f(Core.WIDTH, Core.HEIGHT);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex2f(Core.WIDTH, Core.HEIGHT - (Core.WIDTH/4) + 16*Core.SCALE);
			
			GL11.glEnd();
			
			try
			{
				font.drawString(32, Core.HEIGHT - (Core.WIDTH/4) + 16*Core.SCALE + 32, currentdialogue.getText().split("¬")[0]);
				font.drawString(32, Core.HEIGHT - (Core.WIDTH/4) + 16*Core.SCALE + 64, currentdialogue.getText().split("¬")[1]);
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
			}
		}
	}
	
}
