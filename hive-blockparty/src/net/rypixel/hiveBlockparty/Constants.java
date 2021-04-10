package net.rypixel.hiveBlockparty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.mcjukebox.plugin.bukkit.api.ResourceType;
import net.mcjukebox.plugin.bukkit.api.models.Media;

public class Constants {

	public static ItemStack rules;
	public static ItemStack vote;
	public static ItemStack locker;
	public static ItemStack settings;
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
	public static HashMap<Integer, Media> intToSong = new HashMap<Integer, Media>();
	
	public static HashMap<String, Cosmetic> cosmetics = new HashMap<String, Cosmetic>();
	
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
		
		settings = new ItemStack(Material.REDSTONE_COMPARATOR, 1);
		meta = settings.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "Your Settings");
		lore.clear();
		lore.add("");
		lore.add(ChatColor.GRAY + "Allows you to view");
		lore.add(ChatColor.GRAY + "and change your BP");
		lore.add(ChatColor.GRAY + "settings.");
		lore.add("");
		lore.add(ChatColor.AQUA + "► Right-click when held");
		meta.setLore(lore);
		settings.setItemMeta(meta);
		
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
		
		intToSong.put(10, new Media(ResourceType.MUSIC, "https://raw.githubusercontent.com/ankleBowl/thehivemc/main/blockpartysongs/makeacake.ogg")); //make a cake
		intToSong.put(11, new Media(ResourceType.MUSIC, "https://raw.githubusercontent.com/ankleBowl/thehivemc/main/blockpartysongs/revenge.ogg")); //revenge
		intToSong.put(12, new Media(ResourceType.MUSIC, "https://raw.githubusercontent.com/ankleBowl/thehivemc/main/blockpartysongs/dontmineatnight.ogg")); //dont mine at night
		
		ItemStack item = null;
		
		item = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		LeatherArmorMeta meta1 = (LeatherArmorMeta) item.getItemMeta();
		meta1.setColor(Color.GREEN);
		meta1.setDisplayName(ChatColor.AQUA + "Cactus");
		item.setItemMeta(meta1);
		cosmetics.put("GOLD_BOOTS12", new Cosmetic(12, 100, Material.GOLD_BOOTS, "Cactus", item));
		
		item = new ItemStack(Material.DIRT, 1);
		meta = item.getItemMeta();
		meta1.setDisplayName(ChatColor.AQUA + "Dirt");
		item.setItemMeta(meta1);
		cosmetics.put("GOLD_BOOTS13", new Cosmetic(13, 200, Material.GOLD_BOOTS, "Dirt", item));
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
//		lore.add(ChatColor.GRAY + "to play Shuffle!");g
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
	
	public static Inventory settings(BlockpartyPlayer hp) {
		Inventory inv = Bukkit.createInventory(null, 27, "Your Settings");
		
		ItemStack item = null;
		ItemStack border = null;
		ItemMeta meta = null;
		ArrayList<String> lore = new ArrayList<String>();
		
		border = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData());
		meta = border.getItemMeta();
		meta.setDisplayName("");
		border.setItemMeta(meta);
		
		for (int i = 0; i < 10; i++) {
			inv.setItem(i, border);
		}
		
		if (hp.hardcoreMode) {
			item = new ItemStack(Material.INK_SACK, 1, DyeColor.LIME.getData());
		} else {
			item = new ItemStack(Material.INK_SACK, 1, DyeColor.SILVER.getData());
		}
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Hardcore Mode");
		lore.clear();
		lore.add("");
		lore.add(ChatColor.GRAY + "Hardcore is a new way to");
		lore.add(ChatColor.GRAY + "play BlockParty!");
		lore.add(ChatColor.GRAY + "Here's what's different:");
		lore.add("");
		lore.add(ChatColor.GRAY + " - NO Blocks in your inventory");
		lore.add(ChatColor.GRAY + " - NO BossBar message");
		lore.add(ChatColor.GRAY + " - Invisible players in round");
		lore.add(ChatColor.GRAY + " - Mismatched color messages");
		lore.add("");
		lore.add(ChatColor.GRAY + "Current Selection");
		if (hp.hardcoreMode) {
			lore.add(ChatColor.GREEN + "Enabled");
		} else {
			lore.add(ChatColor.RED + "Disabled");
		}
		lore.add("");
		lore.add(ChatColor.AQUA + "► Click to Cycle");
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(12, item);
		
		for (int i = 17; i < 27; i++) {
			inv.setItem(i, border);
		}
		
		return inv;
	}
	
	public static Inventory shopSidebar() {
		Inventory inv = Bukkit.createInventory(null, 54, "Unlock Selector");
		
		ItemStack item = null;
		ItemMeta meta = null;
		ArrayList<String> lore = new ArrayList<String>();
		
		item = new ItemStack(Material.GOLD_BOOTS, 1);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Bling");
		lore.clear();
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(0, item);
		
		item = new ItemStack(Material.JUKEBOX, 1);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Death Sounds");
		item.setItemMeta(meta);
		inv.setItem(9, item);
		
		item = new ItemStack(Material.PAPER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Join Message");
		item.setItemMeta(meta);
		inv.setItem(18, item);
		
		item = new ItemStack(Material.ENDER_PEARL, 1);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Trails");
		item.setItemMeta(meta);
		inv.setItem(27, item);
		
		return inv;
	}
	
	public static Inventory shopBling(BlockpartyPlayer hp) {
		Inventory inv = shopSidebar();
		
		ItemStack item = null;
		ItemStack borderRed = null;
		ItemStack borderBlack = null;
		ItemStack notOwned = null;
		Material originalType = null;
		
		ItemMeta meta = null;
		ArrayList<String> lore = new ArrayList<String>();
	
		borderRed = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.RED.getData());
		meta = borderRed.getItemMeta();
		meta.setDisplayName("");
		borderRed.setItemMeta(meta);
		
		borderBlack = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.BLACK.getData());
		meta = borderBlack.getItemMeta();
		meta.setDisplayName("");
		borderBlack.setItemMeta(meta);
		
		notOwned = new ItemStack(Material.INK_SACK, 1, DyeColor.SILVER.getData());
		meta = borderBlack.getItemMeta();
		meta.setDisplayName("");
		borderBlack.setItemMeta(meta);
		
		inv.setItem(1, borderRed);
		inv.setItem(2, borderRed);
		inv.setItem(3, borderRed);
		inv.setItem(4, borderRed);
		inv.setItem(5, borderRed);
		inv.setItem(6, borderRed);
		inv.setItem(7, borderRed);
		inv.setItem(8, borderRed);
		
		item = cosmetics.get("GOLD_BOOTS12").getItem();
		if (!hp.ownedBlockpartyCosmetics.contains("Cactus")) {
			item.setType(Material.INK_SACK);
		}
		inv.setItem(12, item);
		
		item = cosmetics.get("GOLD_BOOTS13").getItem();
		if (!hp.ownedBlockpartyCosmetics.contains("Dirt")) {
			item.setType(Material.INK_SACK);
		}
		inv.setItem(13, item);
		
		
		inv.setItem(10, borderBlack);
		inv.setItem(11, borderRed);
		
		inv.setItem(17, borderRed);
		inv.setItem(19, borderBlack);
		inv.setItem(20, borderRed);
		
		inv.setItem(26, borderRed);
		inv.setItem(28, borderBlack);
		inv.setItem(29, borderRed);
		
		inv.setItem(35, borderRed);
		inv.setItem(37, borderBlack);
		inv.setItem(38, borderRed);
		
		inv.setItem(44, borderRed);
		inv.setItem(46, borderBlack);
		inv.setItem(47, borderRed);
		inv.setItem(48, borderRed);
		inv.setItem(49, borderRed);
		inv.setItem(50, borderRed);
		inv.setItem(51, borderRed);
		inv.setItem(52, borderRed);
		
		item = new ItemStack(Material.GOLD_BOOTS, 1);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Bling");
		lore.clear();
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(53, item);
		
		return inv;
	}
}
