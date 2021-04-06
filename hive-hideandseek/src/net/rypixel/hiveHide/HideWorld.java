package net.rypixel.hiveHide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class HideWorld {

	public Plugin plugin;
	public int id;
	
	public boolean inGame;
	public boolean canVote = true;
	
	public ArrayList<HidePlayer> players = new ArrayList<HidePlayer>();
	public HashMap<HidePlayer, Integer> votes = new HashMap<HidePlayer, Integer>();
	
	public String[] randomMaps = new String[5];
	
	public World world;
	
	public World gameWorld;
	
	HideWorld(Plugin plugin, int id) {
		this.plugin = plugin;
		this.id = id;
	}
	
	public void init() {
		world = Functions.createNewWorld(Bukkit.getWorld("hideandseekmap"), String.valueOf(id));
	}
	
	public void welcomePlayer(HidePlayer hp) {
		players.add(hp);
		Functions.showAllPlayers(players);
		hp.mcPlayer.teleport(new Vector(-79.5, 90, 61.5).toLocation(world));
		
		int[] tempVotes = tallyVotes();
		
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.YELLOW + " Vote for a map! " + ChatColor.GRAY + "Use " + ChatColor.WHITE + "/v #" + ChatColor.WHITE + " or click.");
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.GRAY + " 1. " + ChatColor.GOLD + randomMaps[0] + ChatColor.GRAY + " [" + ChatColor.WHITE + tempVotes[0] + ChatColor.GRAY + " Votes]");
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.GRAY + " 2. " + ChatColor.GOLD + randomMaps[0] + ChatColor.GRAY + " [" + ChatColor.WHITE + tempVotes[1] + ChatColor.GRAY + " Votes]");
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.GRAY + " 3. " + ChatColor.GOLD + randomMaps[0] + ChatColor.GRAY + " [" + ChatColor.WHITE + tempVotes[2] + ChatColor.GRAY + " Votes]");
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.GRAY + " 4. " + ChatColor.GOLD + randomMaps[0] + ChatColor.GRAY + " [" + ChatColor.WHITE + tempVotes[3] + ChatColor.GRAY + " Votes]");
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.GRAY + " 5. " + ChatColor.GOLD + randomMaps[0] + ChatColor.GRAY + " [" + ChatColor.WHITE + tempVotes[4] + ChatColor.GRAY + " Votes]");
	}
	
	public int[] tallyVotes() {
		int[] tempVotes = new int[5];
		for (HidePlayer hp : players) {
			if (votes.containsKey(hp)) {
				tempVotes[votes.get(hp)]++;
			}
		}
		return tempVotes;
	}
	
	public void pickMaps() {
		ArrayList<String> maps = Constants.mapList;
		randomMaps = new String[5];
		for (int i = 0; i < 5; i++) {
			Random r = new Random();
			int randomNum = r.nextInt(maps.size());
			randomMaps[i] = maps.get(randomNum);
			maps.remove(randomNum);
		}
	}
	
	public void initGame() {

	}
	
	public void onPlayerLeave(HidePlayer hp) {
		
	}
	
	public void onInteract(PlayerInteractEvent event) {
		HidePlayer hp = Main.playerMap.get(event.getPlayer());
		if (inGame) {
			
		} else {
			if (event.getItem() != null) {
				switch (event.getItem().getType()) {
				case DIAMOND:
					hp.mcPlayer.openInventory(voteInv());
					break;
				case SLIME_BALL:
					Functions.sendToServer(hp.mcPlayer, "lobby0", plugin);
					break;
				}
			}
		}
	}
	
	public void onInventoryClick(InventoryClickEvent event) {
		HidePlayer hp = Main.playerMap.get(event.getWhoClicked());
		if (inGame) {
			
		} else {
			if (event.getCurrentItem() != null) {
				switch (event.getCurrentItem().getType()) {
				case MAP:
					if (canVote) {
						int mapNumber = event.getSlot() / 9;
						votes.put(hp, mapNumber);
						hp.mcPlayer.openInventory(voteInv());
					} else {
						hp.mcPlayer.sendMessage(ChatColor.RED + "You can no longer vote!");
					}
					break;
				}
			}
		}
	}
	
	public void chat(String messgae) {
		
	}
	
	public Inventory voteInv() {
		Inventory inv = Bukkit.createInventory(null, 45, "Vote for an Option");
		
		ArrayList<String> mapList = Constants.mapList;
		int[] voteArray = tallyVotes();
		
		for (int i = 0; i < 5; i++) {
			ItemStack map = new ItemStack(Material.MAP, 1);
			ItemMeta meta = map.getItemMeta();
			meta.setDisplayName(randomMaps[i].replaceAll("_", " "));
			map.setItemMeta(meta);
			inv.setItem(i * 9, map);
			if (votes.size() > 0) {
				ItemStack emptyGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData());
				ItemStack greenGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GREEN.getData());
				double percentage = (double) voteArray[i] / votes.size();
				int slot = 0;
				for (double n = 0; n < 1; n += 0.125) {
					slot++;
					if (percentage > n) {
						inv.setItem(i * 9 + slot, greenGlass);
					} else {
						inv.setItem(i * 9 + slot, emptyGlass);
					}
				}
			}
		}
		return inv;
	}
	
	public String chatPrefix() {
		return ChatColor.GRAY + "|" + ChatColor.AQUA + " Hide"  + ChatColor.GREEN + "And" + ChatColor.YELLOW + "Seek " + ChatColor.GRAY + "|";
	}
}
