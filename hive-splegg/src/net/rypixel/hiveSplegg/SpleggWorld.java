package net.rypixel.hiveSplegg;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_8_R1.WorldGenLargeFeatureStart;
import net.rypixel.hiveSplegg.Functions;
import net.rypixel.hiveSplegg.HivePlayer;

public class SpleggWorld {

	public Plugin plugin;
	public World world;
	public World gameWorld;
	public int id;
	
	public boolean gameStarting;
	public int countdown = 1200;
	
	public boolean inGame;
	
	public boolean canVote;
	
	public Integer[] maps = new Integer[5];
	
	public ArrayList<HivePlayer> players = new ArrayList<HivePlayer>();
	
	public HashMap<HivePlayer, Integer> votes = new HashMap<HivePlayer, Integer>();
	
	SpleggWorld(Plugin plugin, int id) {
		this.plugin = plugin;
		this.id = id;
	}
	
	public void init() {
		world = Functions.createNewWorld(Bukkit.getWorld("world"), String.valueOf(id));
		selectMaps();
	}
	
	public void stop() {
		
		for (HivePlayer hp : players) {
			boolean sent = false;
			for (SpleggWorld world : Main.worlds) {
				if (!sent && world != this) {
					if (world.players.size() < 10) {
						world.players.add(hp);
						hp.mcPlayer.teleport(new Vector(0, 100, 0).toLocation(world.world));
						hp.serverId = world.id;
						sent = true;
					}
				}
			}
			
			if (!sent) {
				int id = Functions.getLowestWorldID(Main.worlds);
				SpleggWorld w = new SpleggWorld(plugin, id);
				w.init();
				Main.worlds.add(w);
				
				w.players.add(hp);
				hp.mcPlayer.teleport(new Vector(0, 100, 0).toLocation(w.world));
				hp.serverId = w.id;
				sent = true;
			}
		}
		
		File folder = world.getWorldFolder();
		Bukkit.unloadWorld(world, false);
		try {
			FileUtils.deleteDirectory(folder);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Main.worlds.remove(this);
	}
	
	public void update() {
		new BukkitRunnable() {
			public void run() {
				if (!inGame) {
					if (players.size() > 5 && !gameStarting) {
						gameStarting = true;
					}
					
					if (gameStarting) {
						countdown--;
						if (countdown == 0) {
							inGame = true;
							//Game Prep
						}
						if (canVote && countdown < 300) {
							canVote = false;
						}
					}
				} else {
					
				}
		    }
		}.runTaskTimer(plugin, 0L, 1L);
	}
	
	public void initGame() {
		
	}
	
	public void welcomePlayer(HivePlayer hp) {
		players.add(hp);
		hp.mcPlayer.teleport(new Vector(0, 100, 0).toLocation(world));
		hp.serverId = id;
		
		Inventory inv = hp.mcPlayer.getInventory();
		inv.setItem(0, Constants.rules);
		inv.setItem(1, Constants.vote);
		inv.setItem(4, Constants.locker);
		inv.setItem(8, Constants.hub);
		
		String[] mapList = Constants.mapList;
		int[] voteArray = tallyVotes();
		
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.YELLOW + " Vote for a map! " + ChatColor.GRAY + "Use " + ChatColor.WHITE + "/v #" + ChatColor.WHITE + " or click.");
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.GRAY + "1. " + ChatColor.GOLD + mapList[maps[0]].replace('_', ' ') + ChatColor.GRAY + " [" + ChatColor.WHITE + String.valueOf(voteArray[0]) + ChatColor.GRAY + " Votes]");
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.GRAY + "1. " + ChatColor.GOLD + mapList[maps[1]].replace('_', ' ') + ChatColor.GRAY + " [" + ChatColor.WHITE + String.valueOf(voteArray[1]) + ChatColor.GRAY + " Votes]");
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.GRAY + "1. " + ChatColor.GOLD + mapList[maps[2]].replace('_', ' ') + ChatColor.GRAY + " [" + ChatColor.WHITE + String.valueOf(voteArray[2]) + ChatColor.GRAY + " Votes]");
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.GRAY + "1. " + ChatColor.GOLD + mapList[maps[3]].replace('_', ' ') + ChatColor.GRAY + " [" + ChatColor.WHITE + String.valueOf(voteArray[3]) + ChatColor.GRAY + " Votes]");
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.GRAY + "1. " + ChatColor.GOLD + mapList[maps[4]].replace('_', ' ') + ChatColor.GRAY + " [" + ChatColor.WHITE + String.valueOf(voteArray[4]) + ChatColor.GRAY + " Votes]");
	}
	
	public void selectMaps() {
		ArrayList<Integer> usedNumbers = new ArrayList<Integer>();
		Random random = new Random();
		int i = 0;
		while (i < 5) {
			int randomInt = random.nextInt(21);
			
			boolean used = false;
			for (Integer n : usedNumbers) {
				if (n == randomInt) {
					used = true;
				}
			}
			
			if (!used) {
				i++;
				usedNumbers.add(randomInt);
			}
		}
	}
	
	public void chat(String message) {
		for (HivePlayer hp : players) {
			hp.mcPlayer.sendMessage(message);
		}
	}
	
	public void onInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
			if (e.getItem() != null) {
				if (!inGame) {
					switch (e.getItem().getType()) {
					case WRITTEN_BOOK:
						break;
					case DIAMOND:
						break;
					case YELLOW_FLOWER:
						break;
					case SLIME_BALL:
						break;
					default:
						break;
					}
				} else {
					switch (e.getItem().getType()) {
					case IRON_SPADE:
						break;
					default:
						break;
					}
				}
			}
		}
	}
	
	public void onInventoryClick(InventoryClickEvent e) {
		
	}
	
	public int[] tallyVotes() {
		int[] voteTotal = new int[5];
		for (Map.Entry<HivePlayer, Integer> set : votes.entrySet()) {
			voteTotal[set.getValue()]++;
		}
		return voteTotal;
	}
	
	public String chatPrefix() {
		return ChatColor.DARK_GRAY + "|" + ChatColor.AQUA + " Splegg " + ChatColor.DARK_GRAY + "|";
	}
}
