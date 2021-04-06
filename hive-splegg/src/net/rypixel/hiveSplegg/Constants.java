package net.rypixel.hiveSplegg;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.Vector;

public class Constants {

	public static ItemStack rules;
	public static ItemStack vote;
	public static ItemStack locker;
	public static ItemStack hub;
	
	public static ItemStack again;
	public static ItemStack players;
	
	public static ItemStack spleggGun;
	
	public static String[] mapList;
	
	public static HashMap<String, Vector[]> spawnLocations = new HashMap<String, Vector[]>();
	
	public static void init() {
		ItemMeta meta = null;
		ArrayList<String> lore = new ArrayList<String>();;
		
		rules = new ItemStack(Material.WRITTEN_BOOK, 1);
		meta = rules.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "Rules" + ChatColor.GRAY + " & " + ChatColor.YELLOW + "Info");
		lore.clear();
		lore.add("");
		lore.add(ChatColor.GRAY + "Read the basics of");
		lore.add(ChatColor.GRAY + "our rules and how");
		lore.add(ChatColor.GRAY + "to play Shuffle!");
		lore.add("");
		lore.add(ChatColor.AQUA + "► Right-click when held");
		meta.setLore(lore);
		rules.setItemMeta(meta);
		
		vote = new ItemStack(Material.DIAMOND, 1);
		meta = vote.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "View Vote Menu");
		lore.clear();
		lore.add("");
		lore.add(ChatColor.GRAY + "Use this item to");
		lore.add(ChatColor.GRAY + "vote for something");
		lore.add("");
		lore.add(ChatColor.AQUA + "► Right-click when held");
		meta.setLore(lore);
		vote.setItemMeta(meta);
		
		locker = new ItemStack(Material.YELLOW_FLOWER, 1);
		meta = locker.getItemMeta();
		meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Your Locker");
		lore.clear();
		lore.add("");
		lore.add(ChatColor.GRAY + "Allows you to view");
		lore.add(ChatColor.GRAY + "and select unlocks.");
		lore.add("");
		lore.add(ChatColor.AQUA + "► Right-click when held");
		meta.setLore(lore);
		locker.setItemMeta(meta);
		
		hub = new ItemStack(Material.SLIME_BALL, 1);
		meta = hub.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Back To " + ChatColor.YELLOW + "Hub");
		lore.clear();
		lore.add("");
		lore.add(ChatColor.GRAY + "Returns you to");
		lore.add(ChatColor.GRAY + "the main Hive hub!");
		lore.add("");
		lore.add(ChatColor.AQUA + "► Right-click when held");
		meta.setLore(lore);
		hub.setItemMeta(meta);
		
		spleggGun = new ItemStack(Material.IRON_SPADE, 1);
		meta = spleggGun.getItemMeta();
		meta.setDisplayName(ChatColor.GRAY + "Splegg Gun");
		lore.clear();
		lore.add("");
		lore.add(ChatColor.GRAY + "Shoot eggs");
		lore.add(ChatColor.GRAY + "from this");
		lore.add(ChatColor.GRAY + "fancy gun!");
		lore.add("");
		lore.add(ChatColor.AQUA + "► Shoot to fire");
		meta.setLore(lore);
		spleggGun.setItemMeta(meta);
		
		players = new ItemStack(Material.COMPASS, 1);
		meta = players.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "Player Selector");
		lore.clear();
		lore.add("");
		lore.add(ChatColor.GRAY + "Opens a menu that");
		lore.add(ChatColor.GRAY + "allows you to teleport to");
		lore.add(ChatColor.GRAY + "participating players!");
		lore.add("");
		lore.add(ChatColor.AQUA + "► Right-click when held");
		meta.setLore(lore);
		players.setItemMeta(meta);
		
		again = new ItemStack(Material.MINECART, 1);
		meta = again.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Play Again");
		lore.clear();
		lore.add("");
		lore.add(ChatColor.GRAY + "Sends you into the");
		lore.add(ChatColor.GRAY + "bext available game!");
		lore.add("");
		lore.add(ChatColor.AQUA + "► Right-click when held");
		meta.setLore(lore);
		again.setItemMeta(meta);
		
		mapList = new String[21];
		mapList[0] = "Autumn_Leaves";
		mapList[1] = "Bloom";
		mapList[2] = "Busy_Bees";
		mapList[3] = "Cell";
		mapList[4] = "Gemmy Gears";
		mapList[5] = "Making It Up";
		mapList[6] = "Oversplegg";
		mapList[7] = "Palette";
		mapList[8] = "PinkMe";
		mapList[9] = "Pokemon";
		mapList[10] = "Saturn";
		mapList[11] = "Splatter";
		mapList[12] = "Storm";
		mapList[13] = "Tree_Fiddy";
		mapList[14] = "Tree_Island";
		
		Vector[] spawnLocs = null;
		
		spawnLocs = new Vector[16];
		spawnLocs[0] = new Vector(-20.5, 92, 17.5);
		spawnLocs[1] = new Vector(-20.5, 92, -22.5);
		spawnLocs[2] = new Vector(19.5, 92, -22.5);
		spawnLocs[3] = new Vector(19.5, 92, 17.5);
		spawnLocs[4] = new Vector(-0.5, 92, 20.5);
		spawnLocs[5] = new Vector(-23.5, 92, -2.5);
		spawnLocs[6] = new Vector(-0.5, 92, -25.5);
		spawnLocs[7] = new Vector(22.5, 92, -2.5);
		spawnLocs[8] = new Vector(21, 92, 6);
		spawnLocs[9] = new Vector(8, 92, 20);
		spawnLocs[10] = new Vector(-9, 92, 20);
		spawnLocs[11] = new Vector(-23, 92, 6);
		spawnLocs[12] = new Vector(-23, 92, -11);
		spawnLocs[13] = new Vector(-9, 92, -25);
		spawnLocs[14] = new Vector(8, 92, -25);
		spawnLocs[15] = new Vector(22, 92, -11);
		spawnLocations.put("Palette", spawnLocs);
		
		spawnLocs = new Vector[14];
		spawnLocs[0] = new Vector(-1, 88, -25);
		spawnLocs[1] = new Vector(-1, 88, 19);
		spawnLocs[2] = new Vector(-23.5, 88, 0.5);
		spawnLocs[3] = new Vector(21.5, 88, -5.5);
		spawnLocs[4] = new Vector(-16.5, 88, 13.5);
		spawnLocs[5] = new Vector(14.5, 88, -18.5);
		spawnLocs[6] = new Vector(14.5, 88, 12.5);
		spawnLocs[7] = new Vector(-17.5, 88, -16.5);
		spawnLocs[8] = new Vector(-11.5, 88, -21.5);
		spawnLocs[9] = new Vector(8.5, 89, 17.5);
		spawnLocs[10] = new Vector(-9.5, 88, 17.5);
		spawnLocs[11] = new Vector(7.5, 88, -22.5);
		spawnLocs[12] = new Vector(-21.5, 88, -9.5);
		spawnLocs[13] = new Vector(19.5, 88, 4.5);
		spawnLocations.put("Autumn_Leaves", spawnLocs);
		
		spawnLocs = new Vector[16];
		spawnLocs[0] = new Vector(0.5, 99, 10.5);
		spawnLocs[1] = new Vector(0.5, 99, -14.5);
		spawnLocs[2] = new Vector(13.5, 99, -2.5);
		spawnLocs[3] = new Vector(-12.5, 99, -2.5);
		spawnLocs[4] = new Vector(8.5, 99, -10.5);
		spawnLocs[5] = new Vector(-8.5, 99, 6.5);
		spawnLocs[6] = new Vector(-8.5, 99, -11.5);
		spawnLocs[7] = new Vector(9.5, 100, 6.5);
		spawnLocs[8] = new Vector(5.5, 99, 8.5);
		spawnLocs[9] = new Vector(-4.5, 99, 8.5);
		spawnLocs[10] = new Vector(-11.5, 99, 2.5);
		spawnLocs[11] = new Vector(-11.5, 99, -6.5);
		spawnLocs[12] = new Vector(-3.5, 99, -13.5);
		spawnLocs[13] = new Vector(4.5, 99, -13.5);
		spawnLocs[14] = new Vector(12.5, 99, -6.5);
		spawnLocs[15] = new Vector(12.5, 99, 1.5);
		spawnLocs[0] = new Vector();
		spawnLocations.put("Splatter", spawnLocs);
		
		spawnLocs = new Vector[20];
		spawnLocs[0] = new Vector(-2.5, 93, -20.5);
		spawnLocs[1] = new Vector(-2.5, 93, -19.5);
		spawnLocs[2] = new Vector(-22.5, 93, -0.5);
		spawnLocs[3] = new Vector(17.5, 93, -0.5);
		spawnLocs[4] = new Vector(11.5, 93, 13.5);
		spawnLocs[5] = new Vector(-16.5, 93, -14.5);
		spawnLocs[6] = new Vector(11.5, 93, -14.5);
		spawnLocs[7] = new Vector(-16.5, 93, 13.5);
		spawnLocs[8] = new Vector(5.5, 93, -19.5);
		spawnLocs[9] = new Vector(-21.5, 93, 7.5);
		spawnLocs[10] = new Vector(-10.5, 93, 18.5);
		spawnLocs[11] = new Vector(4.5, 93, 18.5);
		spawnLocs[12] = new Vector(16.5, 93, 7.5);
		spawnLocs[13] = new Vector(16.5, 93, -8.5);
		spawnLocs[14] = new Vector(-10.5, 93, -19.5);
		spawnLocs[15] = new Vector(-21.5, 93, -8.5);
		spawnLocations.put("Busy_Bees", spawnLocs);
		
		spawnLocs = new Vector[16];
		spawnLocs[0] = new Vector();
		spawnLocations.put("Splatter", spawnLocs);
		
		spawnLocs = new Vector[16];
		spawnLocs[0] = new Vector();
		spawnLocations.put("Splatter", spawnLocs);
		
		spawnLocs = new Vector[16];
		spawnLocs[0] = new Vector();
		spawnLocations.put("Splatter", spawnLocs);
		
		spawnLocs = new Vector[16];
		spawnLocs[0] = new Vector();
		spawnLocations.put("Splatter", spawnLocs);
		
		
//		mapList[0] = "Splatter";
//		mapList[1] = "Petal_Power";
//		mapList[2] = "Ring_Ring";
//		mapList[3] = "Balance";
//		mapList[4] = "Starmie";
//		mapList[5] = "Palette";
//		mapList[6] = "Cell";
//		mapList[7] = "Burst";
//		mapList[8] = "Pokemon";
//		mapList[9] = "Mistic";
//		mapList[10] = "Golf";
//		mapList[11] = "Tree_Island";
//		mapList[12] = "Saturn";
//		mapList[13] = "Not_Mushroom_Here";
//		mapList[14] = "Tree_Fiddy";
//		mapList[15] = "Time_Travel";
//		mapList[16] = "Chocolate";
//		mapList[17] = "Making_It_Up";
//		mapList[18] = "Inferno";
//		mapList[19] = "Repercussions";
//		mapList[20] = "Catch_em_All";
	}
	
	public static Inventory playerSelector(ArrayList<SpleggPlayer> players) {
		Inventory inv = Bukkit.createInventory(null, 18);
		for (int i = 0; i < 18; i++) {
			if (i < players.size()) {
				ItemStack skull = new ItemStack(Material.SKULL, 1);
				SkullMeta meta = (SkullMeta) skull.getItemMeta();
				meta.setDisplayName(ChatColor.BLUE + players.get(i).mcPlayer.getDisplayName());
				meta.setOwner(players.get(i).mcPlayer.getUniqueId().toString());
			}
		}
		return inv;
	}
}
