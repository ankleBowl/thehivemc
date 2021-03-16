
package net.rypixel.hiveLobby;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;

public class Constants {

	public static Vector[] launchpads1 = new Vector[8];
	public static Vector[] launchpads2 = new Vector[8];
	public static Vector[] launchpads3 = new Vector[10];
	
	public static ItemStack lobbySelector;
	
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
		
		lobbySelector = new ItemStack(Material.BOOK, 1);
		ItemMeta lobbySelectorMeta = lobbySelector.getItemMeta();
		lobbySelectorMeta.setDisplayName(ChatColor.BOLD + "" + ChatColor.YELLOW + "Hub" + ChatColor.AQUA + " Selector");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(" ");
		lore.add(ChatColor.GRAY + "Opens up a menu that");
		lore.add(ChatColor.GRAY + "allows you to switch between");
		lore.add(ChatColor.GRAY + "Hive hubs!");
		lore.add(ChatColor.AQUA + "► Hold and right-click");
		lobbySelectorMeta.setLore(lore);
		lobbySelector.setItemMeta(lobbySelectorMeta);
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
	
}
