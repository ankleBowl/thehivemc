package net.rypixel.hiveBlockparty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

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

	public static ItemStack playersVisible;
	public static ItemStack playersInvisible;
	
	public static ItemStack again;
	public static ItemStack players;
	
	public static ItemStack jump;
	public static ItemStack pearl;
	public static ItemStack rain;
	
	public static String[] blockpartyText;
	public static int[] roundSpeed;
	
	public static HashMap<DyeColor, String> colorToName = new HashMap<DyeColor, String>();
	public static HashMap<DyeColor, ChatColor> colorToChat = new HashMap<DyeColor, ChatColor>();
	public static HashMap<Integer, String> intToSong = new HashMap<Integer, String>();
	
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
		
		jump = new ItemStack(Material.FEATHER, 1);
		meta = jump.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Jump Boost");
		jump.setItemMeta(meta);
		
		jump = new ItemStack(Material.ENDER_PEARL, 1);
		meta = jump.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Ender Pearl");
		jump.setItemMeta(meta);
		
		rain = new ItemStack(Material.MAGMA_CREAM, 1);
		meta = rain.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Color Rain");
		rain.setItemMeta(meta);
		
		playersVisible = new ItemStack(Material.INK_SACK, 1, DyeColor.LIME.getData());
		meta = playersVisible.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Players Visible");
		playersVisible.setItemMeta(meta);
		
		playersInvisible = new ItemStack(Material.INK_SACK, 1, DyeColor.GRAY.getData());
		meta = playersInvisible.getItemMeta();
		meta.setDisplayName(ChatColor.GRAY + "Players Invisible");
		playersInvisible.setItemMeta(meta);
		
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
		
		colorToName.put(DyeColor.BLACK, "Black");
		colorToName.put(DyeColor.BLUE, "Blue");
		colorToName.put(DyeColor.BROWN, "Brown");
		colorToName.put(DyeColor.CYAN, "Cyan");
		colorToName.put(DyeColor.GRAY, "Gray");
		colorToName.put(DyeColor.GREEN, "Green");
		colorToName.put(DyeColor.LIGHT_BLUE, "Light Blue");
		colorToName.put(DyeColor.LIME, "Lime");
		colorToName.put(DyeColor.MAGENTA, "Magenta");
		colorToName.put(DyeColor.ORANGE, "Orange");
		colorToName.put(DyeColor.PINK, "Pink");
		colorToName.put(DyeColor.PURPLE, "Purple");
		colorToName.put(DyeColor.RED, "Red");
		colorToName.put(DyeColor.SILVER, "Light Gray");
		colorToName.put(DyeColor.WHITE, "White");
		colorToName.put(DyeColor.YELLOW, "Yellow");
		
		colorToChat.put(DyeColor.BLACK, ChatColor.BLACK);
		colorToChat.put(DyeColor.BLUE, ChatColor.BLUE);
		colorToChat.put(DyeColor.BROWN, ChatColor.GOLD);
		colorToChat.put(DyeColor.CYAN, ChatColor.DARK_AQUA);
		colorToChat.put(DyeColor.GRAY, ChatColor.DARK_GRAY);
		colorToChat.put(DyeColor.GREEN, ChatColor.DARK_GREEN);
		colorToChat.put(DyeColor.LIGHT_BLUE, ChatColor.AQUA);
		colorToChat.put(DyeColor.LIME, ChatColor.GREEN);
		colorToChat.put(DyeColor.MAGENTA, ChatColor.LIGHT_PURPLE);
		colorToChat.put(DyeColor.ORANGE, ChatColor.GOLD);
		colorToChat.put(DyeColor.PINK, ChatColor.LIGHT_PURPLE);
		colorToChat.put(DyeColor.PURPLE, ChatColor.DARK_PURPLE);
		colorToChat.put(DyeColor.RED, ChatColor.RED);
		colorToChat.put(DyeColor.SILVER, ChatColor.GRAY);
		colorToChat.put(DyeColor.WHITE, ChatColor.WHITE);
		colorToChat.put(DyeColor.YELLOW, ChatColor.YELLOW);
		
		intToSong.put(10, "makeacake.mp3");
		intToSong.put(11, "revenge.mp3");
		intToSong.put(12, "dontmineatnight.mp3");
		
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
	
	public static ItemStack blockpartyBlock(DyeColor color) {
		ItemStack item = new ItemStack(Material.STAINED_CLAY, 1, color.getData());
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.BOLD + colorToName.get(color));
		item.setItemMeta(meta);
		return item;
	}
	
	public static Inventory pickSong() {
		Inventory inv = Bukkit.createInventory(null, 54, "Vote for a Song");
		
		ItemStack item = null;
		ItemStack border = null;
		ItemMeta meta = null;
		ArrayList<String> lore = new ArrayList<String>();;
		Random random = new Random();
		
		border = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.MAGENTA.getData());
		meta = border.getItemMeta();
		meta.setDisplayName("");
		border.setItemMeta(meta);
		
		for (int i = 0; i < 10; i++) {
			inv.setItem(i, border);
		}
		
		item = new ItemStack(Material.STAINED_CLAY, 1, colors().get(random.nextInt(16)).getData());
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "Make A Cake");
//		lore.clear();
//		lore.add("");
//		lore.add(ChatColor.GRAY + "Read the basics of");
//		lore.add(ChatColor.GRAY + "our rules and how");
//		lore.add(ChatColor.GRAY + "to play Shuffle!");
//		lore.add("");
//		lore.add(ChatColor.AQUA + "► Right-click when held");
//		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(10, item);
		
		item = new ItemStack(Material.STAINED_CLAY, 1, colors().get(random.nextInt(16)).getData());
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "Revenge");
		item.setItemMeta(meta);
		inv.setItem(11, item);
		
		item = new ItemStack(Material.STAINED_CLAY, 1, colors().get(random.nextInt(16)).getData());
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "Don't Mine At Night");
		item.setItemMeta(meta);
		inv.setItem(12, item);
		
		//Next is how do i craft this again
		
		inv.setItem(17, border);
		inv.setItem(18, border);
		
		inv.setItem(26, border);
		inv.setItem(27, border);
		
		inv.setItem(35, border);
		inv.setItem(36, border);
		
		for (int i = 44; i < 54; i++) {
			inv.setItem(i, border);
		}
		
		return inv;
	}
}
