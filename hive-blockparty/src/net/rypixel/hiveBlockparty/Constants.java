package net.rypixel.hiveBlockparty;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
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
	
	public static String[] blockpartyText;
	public static int[] roundSpeed;
	
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
		
		blockpartyText = new String[10];
		blockpartyText[0] = "B";
		blockpartyText[1] = "l";
		blockpartyText[2] = "o";
		blockpartyText[3] = "c";
		blockpartyText[4] = "k";
		blockpartyText[5] = "P";
		blockpartyText[6] = "a";
		blockpartyText[7] = "r";
		blockpartyText[8] = "t";
		blockpartyText[9] = "y";
		
		roundSpeed = new int[21];
		roundSpeed[0] = 120;
		roundSpeed[1] = 110;
		roundSpeed[2] = 110;
		roundSpeed[3] = 100;
		roundSpeed[4] = 100;
		roundSpeed[5] = 90;
		roundSpeed[6] = 90;
		roundSpeed[7] = 80;
		roundSpeed[8] = 80;
		roundSpeed[9] = 70;
		roundSpeed[10] = 70;
		roundSpeed[11] = 60;
		roundSpeed[12] = 60;
		roundSpeed[13] = 50;
		roundSpeed[14] = 50;
		roundSpeed[15] = 40;
		roundSpeed[16] = 40;
		roundSpeed[17] = 30;
		roundSpeed[18] = 30;
		roundSpeed[19] = 20;
		roundSpeed[20] = 20;
	}
	
	public static Inventory playerSelector(ArrayList<BlockpartyPlayer> players) {
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
	
	public static ArrayList<DyeColor> colors() {
		ArrayList<DyeColor> colors = new ArrayList<DyeColor>();
		colors.add(DyeColor.BLACK);
		colors.add(DyeColor.BLUE);
		colors.add(DyeColor.BROWN);
		colors.add(DyeColor.CYAN);
		colors.add(DyeColor.GRAY);
		colors.add(DyeColor.GREEN);
		colors.add(DyeColor.LIGHT_BLUE);
		colors.add(DyeColor.LIME);
		colors.add(DyeColor.MAGENTA);
		colors.add(DyeColor.ORANGE);
		colors.add(DyeColor.PINK);
		colors.add(DyeColor.PURPLE);
		colors.add(DyeColor.RED);
		colors.add(DyeColor.SILVER);
		colors.add(DyeColor.WHITE);
		colors.add(DyeColor.YELLOW);
		return colors;
	}
}
