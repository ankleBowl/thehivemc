package net.rypixel.hiveGravity;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
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
	
	public static HashMap<String, Vector> spawnLocations = new HashMap<String, Vector>();
	
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
		
		spawnLocations.put("Glitch", new Vector(37.5, 250, -47.5));
		spawnLocations.put("Aquamarine", new Vector(1.5, 244, 13.5));
		spawnLocations.put("Presents", new Vector(-10.5, 250, 3.5));
		spawnLocations.put("Arcane", new Vector(-0.5, 241, 17.5));
		spawnLocations.put("Magic", new Vector(3.5, 246, 5.5));
		spawnLocations.put("Deep_Sea", new Vector(0.5, 225, 8.5));
		spawnLocations.put("CPU", new Vector(1.5, 242, 12.5));
		spawnLocations.put("DNA", new Vector(-5.5, 228, -0.5));
		spawnLocations.put("Speares", new Vector(-10.5, 244, -2.5));
		spawnLocations.put("Shackled", new Vector(-44.5, 246, -29.5));
		spawnLocations.put("Blocks", new Vector(6, 241, 22.5));
		spawnLocations.put("Nightmare", new Vector(9.5, 249.1, 30.5));
		spawnLocations.put("Ore", new Vector(0.5, 235, 1.5));
		spawnLocations.put("Tron", new Vector(2.5, 189, 7.5));
		spawnLocations.put("Cars", new Vector(10.5, 250, 3.5));
		spawnLocations.put("Narnia", new Vector(-3.5, 229.5, 13.5));
		spawnLocations.put("Pipe", new Vector(4.5, 249, -0.5));
	}
	
	public static Inventory playerSelector(ArrayList<GravityPlayer> players) {
		Inventory inv = Bukkit.createInventory(null, 18);
		for (int i = 0; i < 18; i++) {
			if (i < players.size()) {
				ItemStack skull = new ItemStack(Material.SKULL, 1);
				SkullMeta meta = (SkullMeta) skull.getItemMeta();
				meta.setDisplayName(ChatColor.BLUE + players.get(i).mcPlayer.getDisplayName());
				meta.setOwner(players.get(i).mcPlayer.getUniqueId().toString());
				skull.setItemMeta(meta);
			}
		}
		return inv;
	}
	
	public static ArrayList<String> easyMaps() {
		ArrayList<String> easyMaps = new ArrayList<String>();
		
		easyMaps.add("Cars");
		easyMaps.add("Tron");
		easyMaps.add("Speares");
		easyMaps.add("Shackled");
		easyMaps.add("Narnia");
		
		return easyMaps;
	}
	
	public static ArrayList<String> mediumMaps() {
		ArrayList<String> easyMaps = new ArrayList<String>();
		
		easyMaps.add("Pipe");
		easyMaps.add("Ore");
		easyMaps.add("Nightmare");
		easyMaps.add("Magic");
		easyMaps.add("Glitch");
		easyMaps.add("Deep_Sea");
		//easyMaps.add("CPU");
		easyMaps.add("Arcane");
		
		return easyMaps;
	}
	
	public static ArrayList<String> hardMaps() {
		ArrayList<String> easyMaps = new ArrayList<String>();
		
		easyMaps.add("Presents");
		easyMaps.add("DNA");
		easyMaps.add("Blocks");
		easyMaps.add("Aquamarine");
		
		easyMaps.add("CPU");
		
		return easyMaps;
	}
}
