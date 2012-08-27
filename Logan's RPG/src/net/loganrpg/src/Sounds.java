package net.loganrpg.src;

import java.io.IOException;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Sounds
{

	public static final Audio select;
	
	static
	{
		select = loadAudio("menu_select");
	}
	
	private static Audio loadAudio(String key)
	{
		try
		{
			return AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/wav/" + key + ".wav"));
		}
		catch(IOException e)
		{
		}
		System.out.println("AUDIO LOAD FAIL FOR: " + key);
		return null;
	}
	
	public static void playSound(Audio a)
	{
		a.playAsSoundEffect(1, 1, false);
	}
	
}
