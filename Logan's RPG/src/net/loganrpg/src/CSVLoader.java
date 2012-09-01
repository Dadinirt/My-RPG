package net.loganrpg.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CSVLoader
{
	
	public static Area loadAreaCSV(File f) throws FileNotFoundException, IOException
	{
		
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String inputtype = reader.readLine();
		if(inputtype.startsWith("<AREA>"))
		{
			
			int levelwidth;
			int levelheight;
			int levelid;
			int viewdistance;
			String levelparams = reader.readLine();
			
			levelwidth = Integer.parseInt(levelparams.split(",")[0].split("<LEVELWIDTH>")[1]);
			levelheight = Integer.parseInt(levelparams.split(",")[1].split("<LEVELHEIGHT>")[1]);
			levelid = Integer.parseInt(levelparams.split(",")[2].split("<LEVELID>")[1]);
			viewdistance = Integer.parseInt(levelparams.split(",")[3].split("<VIEWDISTANCE>")[1]);
			
			Area created = new Area(levelid, levelwidth, levelheight);
			
			if(reader.readLine().startsWith("<DRAW>"))
			{
				for(int l = 0; l < levelheight; l++)
				{
					
					String line = reader.readLine();
					
					for(int c = 0; c < levelwidth; c++)
					{
						switch(line.split(",")[c].toUpperCase().toCharArray()[0])
						{
						case 'G':
							
							if(line.split(",")[c].toCharArray().length == 1)
							{
								created.tiles[c][l] = Tile.grass;
							}
							else
							{
								for(int i = 1; i < line.split(",")[c].split(":").length; i++)
								{
									
									if(line.split(",")[c].split(":")[i] == "r")
									{
										created.tiles[l][c] = Tile.grass;
									}
									if(line.split(",")[c].split(":")[i] == "y")
									{
										created.tiles[l][c] = Tile.grass;
									}
									
								}
								
							}
							break;
							
						case 'P':
							
							int targetid = Integer.parseInt(line.split(",")[c].split(":")[2]);
							int portalstyle = Integer.parseInt(line.split(",")[c].split(":")[1]);
							int targetx = Integer.parseInt(line.split(",")[c].split(":")[3]);
							int targety = Integer.parseInt(line.split(",")[c].split(":")[4]);
							
							switch(portalstyle)
							{
							case 1:
								created.tiles[c][l] = new TilePortal(targetid, targetx, targety);
								break;
							}
							
							break;
							
						case 'T':
							
							created.tiles[c][l] = Tile.tiledfloor;
							break;
							
						}
						
					}
					
				}
				
				String line;
				while((line = reader.readLine()) != null)
				{
					if(line.startsWith("<NPC>"))
					{
						created.putNPC(Integer.parseInt(line.split(",")[1]), Integer.parseInt(line.split(",")[2]), NPC.getFromID(Integer.parseInt(line.split(",")[3])).setDirects(line.split(",")[4]));
					}
				}
				
			}
			
			Area.areas.add(created);
			return created;
			
		}
		
		return null;
	}
	
	public static NPC loadNPCCSV(File f) throws FileNotFoundException, IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String typecheck = reader.readLine();
		if(typecheck.startsWith("<NPC>"))
		{
			int id;
			String name;
			NPCType type = NPCType.DEFAULT_BOY;
			int health;
			int team;
			SkillLevel skill = SkillLevel.NOVICE;
			int spell1;
			int spell2;
			int spell3;
			int spell4;
			int awareness;
			
			id = Integer.parseInt(reader.readLine().split(",")[1]);
			String inputtype = reader.readLine().split(",")[1];
			if(inputtype == "DEFAULT_BOY")
			{
				type = NPCType.DEFAULT_BOY;
			}
			name = reader.readLine().split(",")[1];
			health = Integer.parseInt(reader.readLine().split(",")[1]);
			team = Integer.parseInt(reader.readLine().split(",")[1]);
			String inputskill = reader.readLine().split(",")[1];
			if(inputskill == "NOVICE")
			{
				skill = SkillLevel.NOVICE;
			}
			spell1 = Integer.parseInt(reader.readLine().split(",")[1]);
			spell2 = Integer.parseInt(reader.readLine().split(",")[1]);
			spell3 = Integer.parseInt(reader.readLine().split(",")[1]);
			spell4 = Integer.parseInt(reader.readLine().split(",")[1]);
			awareness = Integer.parseInt(reader.readLine().split(",")[1]);

			NPC created = new NPC(Core.currentsession, name).setID(id).setType(type).setHealth(health).setTeam(team).setSkill(skill).setAwareness(awareness);
			
			NPC.npcs.add(created);
			return created;
		}
		
		return null;
	}

}
