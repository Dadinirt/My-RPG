package net.loganrpg.src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Tex
{

	public static final Texture def;
	public static final Texture grass;
	public static final Texture phpu;
	public static final Texture phpd;
	public static final Texture phpr;
	public static final Texture phpl;
	public static final Texture portal;
	public static final Texture textframe;
	
	public static final Texture defaultboyup;
	public static final Texture defaultboydown;
	public static final Texture defaultboyleft;
	public static final Texture defaultboyright;
	public static final Texture defaultboymoveup;
	public static final Texture defaultboymovedown;
	public static final Texture defaultboymoveleft;
	public static final Texture defaultboymoveright;
	
	public static final Texture firespellu;
	public static final Texture firespelld;
	public static final Texture firespelll;
	public static final Texture firespellr;
	
	public static final Texture tiledfloor;

	public Tex()
	{
	}

	static
	{
		grass = loadTexture("tex/grass");
		def = loadTexture("tex/void");
		phpu = loadTexture("tex/phpu");
		phpd = loadTexture("tex/phpd");
		phpr = loadTexture("tex/phpr");
		phpl = loadTexture("tex/phpl");
		portal = loadTexture("tex/portal");
		textframe = loadTexture("tex/textframe");
		
		defaultboyup = loadTexture("tex/defaultboyup");
		defaultboydown = loadTexture("tex/defaultboydown");
		defaultboyleft = loadTexture("tex/defaultboyleft");
		defaultboyright = loadTexture("tex/defaultboyright");
		defaultboymoveleft = loadTexture("tex/defaultboymoveleft");
		defaultboymoveright = loadTexture("tex/defaultboymoveright");
		defaultboymoveup = loadTexture("tex/defaultboymoveup");
		defaultboymovedown = loadTexture("tex/defaultboymovedown");
		
		firespellu = loadTexture("tex/firespellu");
		firespelll = loadTexture("tex/firespelll");
		firespellr = loadTexture("tex/firespellr");
		firespelld = loadTexture("tex/firespelld");
		
		tiledfloor = loadTexture("tex/tiledfloor");
	}

	private static Texture loadTexture(String key)
	{
		try
		{
			return TextureLoader.getTexture("PNG", new FileInputStream(new File("res/" + key + ".png")));
		}
		catch (FileNotFoundException e)
		{
			System.out.println("FILE NOT FOUND!");
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

}
