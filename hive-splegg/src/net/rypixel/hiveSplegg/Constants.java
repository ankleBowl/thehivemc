package net.rypixel.hiveSplegg;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class Constants {

	public static ItemStack rules;
	public static ItemStack vote;
	public static ItemStack locker;
	public static ItemStack hub;
	
	public static ItemStack again;
	public static ItemStack players;
	
	public static ItemStack spleggGun;
	
	public static String[] mapList;
	
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
		mapList[0] = "Splatter";
		mapList[1] = "Petal_Power";
		mapList[2] = "Ring_Ring";
		mapList[3] = "Balance";
		mapList[4] = "Starmie";
		mapList[5] = "Palette";
		mapList[6] = "Cell";
		mapList[7] = "Burst";
		mapList[8] = "Pokemon";
		mapList[9] = "Mistic";
		mapList[10] = "Golf";
		mapList[11] = "Tree_Island";
		mapList[12] = "Saturn";
		mapList[13] = "Not_Mushroom_Here";
		mapList[14] = "Tree_Fiddy";
		mapList[15] = "Time_Travel";
		mapList[16] = "Chocolate";
		mapList[17] = "Making_It_Up";
		mapList[18] = "Inferno";
		mapList[19] = "Repercussions";
		mapList[20] = "Catch_em_All";
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
