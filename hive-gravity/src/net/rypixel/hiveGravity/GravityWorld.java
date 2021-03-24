package net.rypixel.hiveGravity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.connorlinfoot.titleapi.TitleAPI;

import net.rypixel.hiveGravity.Functions;

public class GravityWorld {

	public Plugin plugin;
	public int id;
	
	public boolean inGame;
	public boolean starting;
	public int countdown;
	public int gameClock;
	public BukkitTask timer;
	
	public World[] worlds;
	
	public String[] maps;
	public String[][] difficulties;
	
	public ArrayList<HivePlayer> players = new ArrayList<HivePlayer>();
	public HashMap<HivePlayer, String> finishTimes = new HashMap<HivePlayer, String>();
	
	public World world;
	
	GravityWorld(Plugin plugin, int id) {
		this.plugin = plugin;
		this.id = id;
	}
	
	public void init() {
		world = Functions.createNewWorld(Bukkit.getWorld("gravitymap1"), String.valueOf(id));
		countdown = 1200;
		
		worlds = new World[5];
		
		difficulties = new String[5][];
		difficulties[0] = new String[5];
		difficulties[1] = new String[5];
		difficulties[2] = new String[5];
		difficulties[3] = new String[5];
		difficulties[4] = new String[5];
		
		maps = new String[5];
		
		chooseDifficulties();
		
		update();
	}
	
	public void chooseDifficulties() {
		Random rnd = new Random();
		for (int a = 0; a < 5; a++) {
			for (int i = 0; i < 5; i++) {
				int n = rnd.nextInt(3);
				switch (n) {
				case 0:
					difficulties[a][i] = "Easy";
					break;
				case 1:
					difficulties[a][i] = "Normal";
					break;
				case 2:
					difficulties[a][i] = "Hard";
					break;
				}
			}
		}
	}
	
	public void chooseMaps(int index) {
		Random rnd = new Random();
		ArrayList<String> easyMaps = Constants.easyMaps();
		ArrayList<String> normalMaps = Constants.mediumMaps();
		ArrayList<String> hardMaps = Constants.hardMaps();
		for (int i = 0; i < 5; i++) {
			if (difficulties[index][i] == "Easy") {
				int r = rnd.nextInt(easyMaps.size());
				maps[i] = easyMaps.get(r);
				easyMaps.remove(r);
			} else if (difficulties[index][i] == "Normal") {
				int r = rnd.nextInt(normalMaps.size());
				maps[i] = normalMaps.get(r);
				normalMaps.remove(r);
			} else if (difficulties[index][i] == "Hard") {
				int r = rnd.nextInt(hardMaps.size());
				maps[i] = hardMaps.get(r);
				hardMaps.remove(r);
			}
			
			World temp = Bukkit.createWorld(new WorldCreator(maps[i]));
			worlds[i] = Functions.createNewWorld(Bukkit.getWorld(maps[i]), String.valueOf(id));
			Bukkit.unloadWorld(temp, false);
		}
	}
	
	public void update() {
		timer = new BukkitRunnable() {
			public void run() {
				if (!inGame) {
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
							cancel();
						}
					}
				}
			}
		}.runTaskTimer(plugin, 0L, 1L);
	}
	
	public void welcomePlayer(HivePlayer hp) {
		
	}
	
	public void initGame() {
		inGame = true;
		chooseMaps(0);
	}
	
	public void onPlayerLeave(HivePlayer hp) {
		
	}
	
	public void onInteract(PlayerInteractEvent event) {
		
	}
	
	public void onInventoryClick(InventoryClickEvent event) {
		
	}
	
	public void chat(String messgae) {
		
	}
}
