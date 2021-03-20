
package net.rypixel.hiveLobby;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;

public class Constants {

	public static Vector[] launchpads1 = new Vector[8];
	public static Vector[] launchpads2 = new Vector[8];
	public static Vector[] launchpads3 = new Vector[10];
	
	public static Vector[] parkourLocations = new Vector[4];
	
	public static Team noRank;
	
	public static ItemStack lobbySelector;
	public static ItemStack gameSelector;
	
	public static void init() {
		launchpads1[0] = new Vector(-20.5, 21, 0.5);
		launchpads1[1] = new Vector(-14.5, 21, -14.5);
		launchpads1[2] = new Vector(0.5, 21, -19.5);
		launchpads1[3] = new Vector(15.5, 21, -14.5);
		launchpads1[4] = new Vector(21.5, 21, 0.5);
		launchpads1[5] = new Vector(15.5, 21, 15.5);
		launchpads1[6] = new Vector(0.5, 21, 20.5);
		launchpads1[7] = new Vector(-14.5, 21, 15.5);
		
		launchpads2[0] = new Vector(-45.5, 19, 0.5);
		launchpads2[1] = new Vector(-39.5, 19, -22.5);
		launchpads2[2] = new Vector(0.5, 19, -45.5);
		launchpads2[3] = new Vector(40.5, 19, -22.5);
		launchpads2[4] = new Vector(46.5, 19, 0.5);
		launchpads2[5] = new Vector(40.5, 19, 23.5);
		launchpads2[6] = new Vector(0.5, 19, 46.5);
		launchpads2[7] = new Vector(-39.5, 19, 23.5);
		
		launchpads3[0] = new Vector(0.5, 18, 68.5);
		launchpads3[1] = new Vector(-40.5, 18, 68.5);
		launchpads3[2] = new Vector(-67.5, 18, 41.5);
		launchpads3[3] = new Vector(-67.5, 18, 0.5);
		launchpads3[4] = new Vector(-40.5, 18, -67.5);
		launchpads3[5] = new Vector(0.5, 18, -67.5);
		launchpads3[6] = new Vector(41.5, 18, -67.5);
		launchpads3[7] = new Vector(68.5, 18, -40.5);
		launchpads3[8] = new Vector(68.5, 18, 0.5);
		launchpads3[9] = new Vector(68.5, 18, 41.5);
		
		parkourLocations[0] = new Vector(-57.5, 20, 63.5);
		parkourLocations[1] = new Vector(-37.5, 62, 40.5);
		parkourLocations[2] = new Vector(-21.5, 76, -28.5);
		parkourLocations[3] = new Vector(33.5, 81, 15.5);
		
		lobbySelector = new ItemStack(Material.BOOK, 1);
		ItemMeta lobbySelectorMeta = lobbySelector.getItemMeta();
		lobbySelectorMeta.setDisplayName(ChatColor.BOLD + "" + ChatColor.YELLOW + "Hub" + ChatColor.AQUA + " Selector");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(" ");
		lore.add(ChatColor.GRAY + "Opens up a menu that");
		lore.add(ChatColor.GRAY + "allows you to switch between");
		lore.add(ChatColor.GRAY + "Hive hubs!");
		lore.add(" ");
		lore.add(ChatColor.AQUA + "► Hold and right-click");
		lobbySelectorMeta.setLore(lore);
		lobbySelector.setItemMeta(lobbySelectorMeta);
		
		gameSelector = new ItemStack(Material.COMPASS, 1);
		ItemMeta gameSelectorMeta = lobbySelector.getItemMeta();
		gameSelectorMeta.setDisplayName(ChatColor.BOLD + "" + ChatColor.YELLOW + "Select" + ChatColor.AQUA + " Game");
		lore.clear();
		lore.add(" ");
		lore.add(ChatColor.GRAY + "Opens up a selection menu");
		lore.add(ChatColor.GRAY + "that allows you to teleport");
		lore.add(ChatColor.GRAY + "to game rooms!");
		lore.add(" ");
		lore.add(ChatColor.AQUA + "► Hold and right-click");
		gameSelectorMeta.setLore(lore);
		gameSelector.setItemMeta(gameSelectorMeta);
	}
	
	public static Inventory lobbySelector(int slotNumber) {
		Inventory inv = Bukkit.createInventory(null, 54, "Hub Selector");
		int i = 10;
		for (int c = 0; c < slotNumber; c++) {
			if (c == 7) {
				i += 2;
			}
			if (c == 14) {
				i += 2;
			}
			if (c == 21) {
				i += 2;
			}
			ItemStack iron = new ItemStack(Material.IRON_BLOCK, 1);
			ItemMeta ironMeta = iron.getItemMeta();
			ironMeta.setDisplayName(String.valueOf(c + 1));
			iron.setItemMeta(ironMeta);
			inv.setItem(i + c, iron);
		}
		return inv;
	}
	
	public static Inventory gameSelector() {
		Inventory inv = Bukkit.createInventory(null, 45, "The Hive Games");
		ArrayList<String> lore = new ArrayList<String>();
		ItemStack item = null;
		ItemMeta itemMeta = null;
		
		item = new ItemStack(Material.COMPASS, 1);
		itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(ChatColor.BOLD + "" + ChatColor.GRAY + "Back to Spawn");
		lore.clear();
		lore.add(" ");
		lore.add(ChatColor.GRAY + "Stuck or lost? Use me to travel");
		lore.add(ChatColor.GRAY + "back to the center of the hub!");
		lore.add(" ");
		lore.add(ChatColor.AQUA + "► Left-click to Teleport");
		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);
		inv.setItem(22, item);
		
		item = new ItemStack(Material.SLIME_BALL, 1);
		itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(ChatColor.BOLD + "" + ChatColor.RED + "Parkour");
		lore.clear();
		lore.add(" ");
		lore.add(ChatColor.GRAY + "Think you can take on the challenge");
		lore.add(ChatColor.GRAY + "of the hub parkour?");
		lore.add(" ");
		lore.add(ChatColor.AQUA + "► Left-click to Teleport");
		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);
		inv.setItem(23, item);
		
		item = new ItemStack(Material.EGG, 1);
		itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(ChatColor.BOLD + "" + ChatColor.AQUA + "Splegg");
		lore.clear();
		lore.add(ChatColor.DARK_GRAY + "PvP, Solo, Parkour");
		lore.add(" ");
		lore.add(ChatColor.GRAY + "Use your egg blaster to shoot");
		lore.add(ChatColor.GRAY + "block destroying eggs to make");
		lore.add(ChatColor.GRAY + "other players fall to their death");
		lore.add(ChatColor.GRAY + "Last player alive wins!");
		lore.add(" ");
		lore.add(ChatColor.GREEN + "► Left-click to Queue");
		lore.add(ChatColor.AQUA + "► Right-click to view Servers");
		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);
		inv.setItem(23, item);
		
		return inv;
	}
	
}
