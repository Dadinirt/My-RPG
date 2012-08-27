package net.loganrpg.src;

import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class Core {
	
	/**
	 * Game window dimensions
	 */
	public static final int WIDTH = 720;
	public static final int HEIGHT = 480;
	
	/**
	 * Global scale of the tiles
	 */
	public static final float SCALE = 2.5f; 
	
	public static enum Gamestate
	{
		MENU, INSESSION
	}
	
	static Gamestate state = Gamestate.INSESSION;
	
	public static Session currentsession;

	static void start()
	{
		try
		{
			System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL", "true");
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setTitle("Logan's RPG");
			Display.create();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error initializing Display.");
		}
		
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glLoadIdentity();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		
		currentsession = new Session();
		
		while(!Display.isCloseRequested())
		{
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			
			if(state == Gamestate.INSESSION)
			{
				currentsession.update();
			}
			
			Display.update();
			Display.sync(60);
		}
		
		AL.destroy();
	}
	
	public static void main(String[] args)
	{
		start();
	}
	
}
