package net.rypixel.hiveBlockparty;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
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
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;

import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.session.ClipboardHolder;

public class BlockpartyWorld {

	public int id;
	public World world;
	public Plugin plugin;
	
	public BukkitTask timer;
	
	public boolean inGame;
	public boolean starting;
	public int countdown;
	
	public int titleTimer;
	
	public int level;
	
	public ArrayList<HivePlayer> players = new ArrayList<HivePlayer>();
	
	public HashMap<DyeColor, DyeColor> colorMap = new HashMap<DyeColor, DyeColor>();
	public ArrayList<DyeColor> colorsUsed = new ArrayList<DyeColor>();
	public DyeColor colorToRemove;
	
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
						}
					}
				}
			}
		}.runTaskTimer(plugin, 0L, 1L);
	}
	
	public void initGame() {
		for (HivePlayer hp : players) {
			hp.mcPlayer.teleport(new Vector(0, 1, 0).toLocation(world));
		}
		inGame = true;
		titleTimer = 10;
		
		opening();
		cycle(Constants.roundSpeed[level], 50);
	}
	
	public void cycle(int runTime, int emptyTime) {		
		//Set new floor
		loadFloor();
		//get the block they will need to run to
		for (HivePlayer hp : players) {
			//check if dead
		}

		new BukkitRunnable() {
			public void run() {
				Random random = new Random();
				colorToRemove = colorsUsed.get(random.nextInt(colorsUsed.size()));
				//countdown for players to run
			}
		}.runTaskLater(plugin, 60);
		
		new BukkitRunnable() {
			public void run() {
				//remove the floor
				removeFloor(colorToRemove);

			}
		}.runTaskLater(plugin, runTime + 60);
		
		new BukkitRunnable() {
			public void run() {
				//when the players wait and the floor is empty
				if (level < 21) {
					level++;
					cycle(Constants.roundSpeed[level], 50);
					
				} else {
					//game is over (tie)
				}
			}
		}.runTaskLater(plugin, runTime + emptyTime + 60);
	}

	
	public void opening() {
		
		new BukkitRunnable() {
			public void run() {
				titleTimer--;
				String message = generateTitle(titleTimer);
				for (HivePlayer hp : players) {
					TitleAPI.sendTitle(hp.mcPlayer, 0, 20, 20, message);
					//hp.mcPlayer.sendMessage(message);
				}
				if (titleTimer < -9) {
					cancel();
				}
			}
		}.runTaskTimer(plugin, 0L, 1L);
	}
	
	public String generateTitle(int spacing) {
		if (spacing > -1) {
			String string = "";
			for (int i = 0; i < 10; i++) {
				string += Constants.blockpartyText[i];
				for (int n = 0; n < spacing; n++) {
					string += " ";
				}
			}
			return ChatColor.YELLOW + string;
		} else {
			int number = Math.abs(spacing);
			String string = "";
			for (int i = 0; i < 10; i++) {
				if (i != number - 1) {
					string += ChatColor.YELLOW + Constants.blockpartyText[i];
				} else {
					string += ChatColor.GOLD + Constants.blockpartyText[i];
				}
			}
			return string;
		}
	}
	
	public void removeFloor(DyeColor color) {
		for (int x = 0; x < 48; x++) {
			for (int z = 0; z < 48; z++) {
				Block c = world.getBlockAt(x - 32, 0, z - 16);
				if (c.getData() == colorToRemove.getData()) {
					c.setType(Material.AIR);
				}
			}
		}
	}
	
	public void loadFloor() {
		updateHashmap();
		
		Random random = new Random();
		int map = random.nextInt(9);
		colorsUsed.clear();
		
		for (int x = 0; x < 48; x++) {
			for (int z = 0; z < 48; z++) {
				Block b = world.getBlockAt(new Vector(x + 100, map, z).toLocation(world));
				DyeColor color = DyeColor.getByData(b.getData());
				Block c = world.getBlockAt(x - 32, 0, z - 16);
				c.setType(Material.STAINED_CLAY);
				c.setData(colorMap.get(color).getData());
				if (!colorsUsed.contains(colorMap.get(color))) {
					colorsUsed.add(colorMap.get(color));
				}
			}
		}
	}
	
	public void updateHashmap() {
		Random random = new Random();
		ArrayList<DyeColor> colorsToTranslate = Constants.colors();
		ArrayList<DyeColor> colorsToPut = Constants.colors();
		while (colorsToTranslate.size() > 0) {
			int colorInt = random.nextInt(colorsToTranslate.size());
			int colorPutInt = random.nextInt(colorsToTranslate.size());
			colorMap.put(colorsToTranslate.get(colorInt), colorsToPut.get(colorPutInt));
			colorsToTranslate.remove(colorInt);
			colorsToPut.remove(colorPutInt);
		}
	}
}
