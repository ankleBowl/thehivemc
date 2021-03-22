package net.rypixel.hiveBlockparty;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.connorlinfoot.titleapi.TitleAPI;

public class BlockpartyWorld {

	public int id;
	public World world;
	public Plugin plugin;
	
	public BukkitTask timer;
	
	public boolean inGame;
	public boolean starting;
	public int countdown;
	
	public int level;
	
	public ArrayList<HivePlayer> players = new ArrayList<HivePlayer>();
	
	BlockpartyWorld(Plugin plugin, int id) {
		this.plugin = plugin;
		this.id = id;
	}
	
	public void init() {
		world = Functions.createNewWorld(Bukkit.getWorld("bpmap1"), String.valueOf(id));
		update();
	}

	public void onPlayerLeave(PlayerQuitEvent event) {
		
	}
	
	public void onInteract(PlayerInteractEvent event) {

	}

	public void onInventoryClick(InventoryClickEvent event) {

	}
	
	public void welcomePlayer(HivePlayer hp) {
		players.add(hp);
		hp.mcPlayer.teleport(new Vector(34.5, 3, 8).toLocation(world));
		hp.serverId = id;
		hp.mcPlayer.setGameMode(GameMode.ADVENTURE);
	}
	
	public void chat(String message) {
		for (HivePlayer hp : players) {
			hp.mcPlayer.sendMessage(message);
		}
	}
	
	public void update() {
		timer = new BukkitRunnable() {
			public void run() {
				if (players.size() > 5 && !starting) {
					starting = true;
				} else {
					for (HivePlayer hp : players) {
						TitleAPI.sendTitle(hp.mcPlayer, 0, 20, 0, "");
						TitleAPI.sendSubtitle(hp.mcPlayer, 0, 20, 0, ChatColor.YELLOW + String.valueOf(6 - players.size()) + " players needed to start...");
					}
				}
				
				if (starting) {
					countdown--;
					
					for (HivePlayer hp : players) {
						TitleAPI.sendTitle(hp.mcPlayer, 0, 20, 0, "");
						TitleAPI.sendSubtitle(hp.mcPlayer, 0, 20, 0, ChatColor.GREEN + "Starting game in " + String.valueOf(countdown / 20));
					}
					
					if (countdown == 0) {
						initGame();
					}
				}
			}
		}.runTaskTimer(plugin, 0L, 1L);
	}
	
	public void initGame() {
		for (HivePlayer hp : players) {
			hp.mcPlayer.teleport(new Vector(0, 1, 0).toLocation(world));
		}
		cycle(100, 50);
	}
	
	public void cycle(int runTime, int emptyTime) {
		for (HivePlayer hp : players) {
			//check if dead
		}
		
		new BukkitRunnable() {
			public void run() {
				//countdown for players to run
			}
		}.runTaskLater(plugin, runTime);
		
		new BukkitRunnable() {
			public void run() {
				//remove the floor
				if (level < 22) {
					cycle(100, 50);
				} else {
					//game is over (tie)
				}
			}
		}.runTaskLater(plugin, runTime);
	}
	
	public void opening() {
		
	}
}
