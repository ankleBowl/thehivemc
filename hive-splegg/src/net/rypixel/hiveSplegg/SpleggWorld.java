package net.rypixel.hiveSplegg;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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
	
	public ArrayList<HivePlayer> players = new ArrayList<HivePlayer>();
	
	public HashMap<HivePlayer, Integer> votes = new HashMap<HivePlayer, Integer>();
	
	SpleggWorld(Plugin plugin, int id) {
		this.plugin = plugin;
		this.id = id;
	}
	
	public void init() {
		world = Functions.createNewWorld(Bukkit.getWorld("world"), String.valueOf(id));
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
	}
	
	public void chat(String message) {
		for (HivePlayer hp : players) {
			hp.mcPlayer.sendMessage(message);
		}
	}
	
	public void onInteract(PlayerInteractEvent e) {
		
	}
	
	public void onInventoryClick(InventoryClickEvent e) {
		
	}
}
