package net.rypixel.hiveSplegg;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
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
	
	public static HashMap<String, Cosmetic> guns = new HashMap<String, Cosmetic>();
	public static HashMap<String, Cosmetic> entities = new HashMap<String, Cosmetic>();
	public static HashMap<String, Cosmetic> trails = new HashMap<String, Cosmetic>();
	public static HashMap<String, Cosmetic> wins = new HashMap<String, Cosmetic>();
	
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
		
		mapList = new String[15];
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
		
		ItemStack item = null;
		
		item = new ItemStack(Material.STICK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "Wand");
		item.setItemMeta(meta);
		guns.put("GOLD_SPADE12", new Cosmetic(100, "Wand", item.serialize()));
		
		
		item = new ItemStack(Material.STICK, 1);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Creeper");
		item.setItemMeta(meta);
		Cosmetic c = new Cosmetic(100, "Creeper", item.serialize());
		c.setEntity(EntityType.CREEPER);
		entities.put("EGG12", c);
		
		
		item = new ItemStack(Material.FLINT_AND_STEEL, 1);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Fire");
		item.setItemMeta(meta);
		c = new Cosmetic(100, "Fire", item.serialize());
		c.setTrail(Effect.LAVADRIP);
		trails.put("ARROW12", c);
		
		
		item = new ItemStack(Material.BEACON, 1);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "Jump Boost");
		item.setItemMeta(meta);
		c = new Cosmetic(100, "Jump", item.serialize());
		wins.put("NETHER_STAR12", c);
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
	
	public static Inventory shopSidebar() {
		Inventory inv = Bukkit.createInventory(null, 54, "Unlock Selector");
		
		ItemStack item = null;
		ItemMeta meta = null;
		ArrayList<String> lore = new ArrayList<String>();
		
		item = new ItemStack(Material.GOLD_SPADE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Splegg Guns");
		lore.clear();
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(0, item);
		
		item = new ItemStack(Material.EGG, 1);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Explosive Entities");
		item.setItemMeta(meta);
		inv.setItem(9, item);
		
		item = new ItemStack(Material.ARROW, 1);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Projectile Trails");
		item.setItemMeta(meta);
		inv.setItem(18, item);
		
		item = new ItemStack(Material.NETHER_STAR, 1);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Win Celebrations");
		item.setItemMeta(meta);
		inv.setItem(27, item);
		
		return inv;
	}
	
	public static Inventory shopGuns(SpleggPlayer hp) {
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
		
		item = ItemStack.deserialize(guns.get("GOLD_SPADE12").getItem());
		if (!hp.spleggCosmetics.contains("Wand")) {
			item.setType(Material.INK_SACK);
		}
		inv.setItem(12, item);
//		
//		item = ItemStack.deserialize(bling.get("GOLD_BOOTS13").getItem());
//		if (!hp.ownedBlockpartyCosmetics.contains("Dirt")) {
//			item.setType(Material.INK_SACK);
//		}
//		inv.setItem(13, item);

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
		
		item = new ItemStack(Material.GOLD_SPADE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Bling");
		lore.clear();
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(53, item);
		
		return inv;
	}
	
	public static Inventory shopEntities(SpleggPlayer hp) {
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
		
		inv.setItem(1, borderBlack);
		inv.setItem(2, borderRed);
		inv.setItem(3, borderRed);
		inv.setItem(4, borderRed);
		inv.setItem(5, borderRed);
		inv.setItem(6, borderRed);
		inv.setItem(7, borderRed);
		inv.setItem(8, borderRed);


		inv.setItem(10, borderRed);
		inv.setItem(11, borderRed);
		
		item = ItemStack.deserialize(entities.get("EGG12").getItem());
		if (!hp.spleggCosmetics.contains("Creeper")) {
			item.setType(Material.INK_SACK);
		}
		inv.setItem(12, item);
		
//		item = ItemStack.deserialize(bling.get("GOLD_BOOTS13").getItem());
//		if (!hp.ownedBlockpartyCosmetics.contains("Dirt")) {
//			item.setType(Material.INK_SACK);
//		}
//		inv.setItem(13, item);
		
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
		
		item = new ItemStack(Material.EGG, 1);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Explosive Entities");
		lore.clear();
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(53, item);
		
		return inv;
	}
	
	public static Inventory shopTrails(SpleggPlayer hp) {
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
		
		inv.setItem(1, borderBlack);
		inv.setItem(2, borderRed);
		inv.setItem(3, borderRed);
		inv.setItem(4, borderRed);
		inv.setItem(5, borderRed);
		inv.setItem(6, borderRed);
		inv.setItem(7, borderRed);
		inv.setItem(8, borderRed);


		inv.setItem(10, borderBlack);
		inv.setItem(11, borderRed);
		
		item = ItemStack.deserialize(trails.get("ARROW12").getItem());
		if (!hp.spleggCosmetics.contains("Fire")) {
			item.setType(Material.INK_SACK);
		}
		inv.setItem(12, item);
		
//		item = ItemStack.deserialize(bling.get("GOLD_BOOTS13").getItem());
//		if (!hp.ownedBlockpartyCosmetics.contains("Dirt")) {
//			item.setType(Material.INK_SACK);
//		}
//		inv.setItem(13, item);
		
		inv.setItem(17, borderRed);
		inv.setItem(19, borderRed);
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
		
		item = new ItemStack(Material.ARROW, 1);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Trails");
		lore.clear();
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(53, item);
		
		return inv;
	}
	
	public static Inventory shopWins(SpleggPlayer hp) {
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
		
		inv.setItem(1, borderBlack);
		inv.setItem(2, borderRed);
		inv.setItem(3, borderRed);
		inv.setItem(4, borderRed);
		inv.setItem(5, borderRed);
		inv.setItem(6, borderRed);
		inv.setItem(7, borderRed);
		inv.setItem(8, borderRed);


		inv.setItem(10, borderBlack);
		inv.setItem(11, borderRed);
		
		item = ItemStack.deserialize(wins.get("NETHER_STAR12").getItem());
		if (!hp.spleggCosmetics.contains("Jump Boost")) {
			item.setType(Material.INK_SACK);
		}
		inv.setItem(12, item);
		
//		item = ItemStack.deserialize(bling.get("GOLD_BOOTS13").getItem());
//		if (!hp.ownedBlockpartyCosmetics.contains("Dirt")) {
//			item.setType(Material.INK_SACK);
//		}
//		inv.setItem(13, item);
		
		inv.setItem(17, borderRed);
		inv.setItem(19, borderBlack);
		inv.setItem(20, borderRed);
		
		inv.setItem(26, borderRed);
		inv.setItem(28, borderRed);
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
		
		item = new ItemStack(Material.NETHER_STAR, 1);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Win Celebrations");
		lore.clear();
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(53, item);
		
		return inv;
	}
}
