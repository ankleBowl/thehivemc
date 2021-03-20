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
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.connorlinfoot.titleapi.TitleAPI;

import net.minecraft.server.v1_8_R1.WorldGenLargeFeatureStart;
import net.rypixel.hiveSplegg.Functions;
import net.rypixel.hiveSplegg.SpleggPlayer;

public class SpleggWorld {

	public Plugin plugin;
	public World world;
	public World gameWorld;
	public int id;
	
	public boolean gameStarting;
	public int countdown = 1200;
	
	public int gameTimer;
	public boolean inGame;
	public BukkitTask loop;
	
	public boolean canVote;
	
	public Integer[] maps = new Integer[5];
	
	public ArrayList<SpleggPlayer> players = new ArrayList<SpleggPlayer>();
	
	public HashMap<SpleggPlayer, Integer> votes = new HashMap<SpleggPlayer, Integer>();
	public HashMap<Egg, SpleggPlayer> eggMap = new HashMap<Egg, SpleggPlayer>();
	
	SpleggWorld(Plugin plugin, int id) {
		this.plugin = plugin;
		this.id = id;
	}
	
	public void init() {
		world = Functions.createNewWorld(Bukkit.getWorld("world"), String.valueOf(id));
		selectMaps();
		update();
	}
	
	public void stop() {
		
		for (SpleggPlayer hp : players) {
			boolean sent = false;
			for (SpleggWorld world : Main.worlds) {
				if (!sent && world != this) {
					if (world.players.size() < 10) {
						world.welcomePlayer(hp);
						sent = true;
					}
				}
			}
			
			if (!sent) {
				int id = Functions.getLowestWorldID(Main.worlds);
				SpleggWorld w = new SpleggWorld(plugin, id);
				w.init();
				Main.worlds.add(w);
				
				w.welcomePlayer(hp);
				sent = true;
			}
		}
		
		loop.cancel();
		
		File folder = world.getWorldFolder();
		Bukkit.unloadWorld(world, false);
		try {
			FileUtils.deleteDirectory(folder);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		folder = gameWorld.getWorldFolder();
		Bukkit.unloadWorld(gameWorld, false);
		try {
			FileUtils.deleteDirectory(folder);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Main.worlds.remove(this);
	}
	
	public void update() {
		loop = new BukkitRunnable() {
			public void run() {
				if (!inGame) {
					if (players.size() > 5 && !gameStarting) {
						gameStarting = true;
					}
					
					if (gameStarting) {
						countdown--;
						if (countdown == 0) {
							initGame();
						}
						if (canVote && countdown < 300) {
							canVote = false;
						}
					}
				} else {
					gameTimer++;
					//Check for deaths
					for (SpleggPlayer hp : players) {
						hp.mcPlayer.setFoodLevel(20);
						hp.mcPlayer.setSaturation(20);
						
						if (hp.mcPlayer.getLocation().getBlockY() < 60 && hp.alive) {
							chat(chatPrefix() + ChatColor.BLUE + " " + hp.mcPlayer.getDisplayName() + ChatColor.GRAY + " has fallen to their " + ChatColor.RED + "DEATH!");
							hp.alive = false;
							hp.mcPlayer.teleport(new Vector(0, 100, 0).toLocation(gameWorld));
							hp.mcPlayer.setGameMode(GameMode.SPECTATOR);
							TitleAPI.sendTitle(hp.mcPlayer, 20, 20, 20, ChatColor.RED + "YOU DIED!");
							hp.mcPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 1, false, true));
						}
					}
					
					
					//Check if game is over
					int alivePlayers = 0;
					for (SpleggPlayer hp : players) {
						if (hp.alive) {
							alivePlayers++;
						}
					}
					if (alivePlayers < 2) {
						for (SpleggPlayer hp : players) {
							TitleAPI.sendTitle(hp.mcPlayer, 20, 20, 20, ChatColor.RED + "Game. OVER!");
						}
						new BukkitRunnable() {
							public void run() {
								stop();
						    }
						}.runTaskLater(plugin, 200L);
					}
				}
		    }
		}.runTaskTimer(plugin, 0L, 1L);
	}
	
	public void initGame() {
		inGame = true;
		gameWorld = Functions.createNewWorld(Bukkit.getWorld("spleggmap1"), String.valueOf(id)); //Replace "spleggmap1" with the voted map later
		for (SpleggPlayer hp : players) {
			hp.mcPlayer.teleport(new Vector(0, 100, 0).toLocation(gameWorld));
			hp.mcPlayer.getInventory().clear();
			hp.mcPlayer.setFoodLevel(20);
			hp.mcPlayer.setSaturation(20);
			hp.alive = true;
		}
		new BukkitRunnable() {
			public void run() {
				for (SpleggPlayer hp : players) {
					hp.mcPlayer.getInventory().setItem(0, Constants.spleggGun);
					
				}
		    }
		}.runTaskLater(plugin, 100L);
	}
	
	public void welcomePlayer(SpleggPlayer hp) {
		players.add(hp);
		hp.mcPlayer.teleport(new Vector(0, 10, 0).toLocation(world));
		hp.serverId = id;
		hp.mcPlayer.setGameMode(GameMode.ADVENTURE);
		
		Inventory inv = hp.mcPlayer.getInventory();
		inv.setItem(0, Constants.rules);
		inv.setItem(1, Constants.vote);
		inv.setItem(4, Constants.locker);
		inv.setItem(8, Constants.hub);
		
		String[] mapList = Constants.mapList;
		int[] voteArray = tallyVotes();
		
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.YELLOW + " Vote for a map! " + ChatColor.GRAY + "Use " + ChatColor.WHITE + "/v #" + ChatColor.WHITE + " or click.");
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.GRAY + " 1. " + ChatColor.GOLD + mapList[maps[0]].replace('_', ' ') + ChatColor.GRAY + " [" + ChatColor.WHITE + String.valueOf(voteArray[0]) + ChatColor.GRAY + " Votes]");
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.GRAY + " 2. " + ChatColor.GOLD + mapList[maps[1]].replace('_', ' ') + ChatColor.GRAY + " [" + ChatColor.WHITE + String.valueOf(voteArray[1]) + ChatColor.GRAY + " Votes]");
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.GRAY + " 3. " + ChatColor.GOLD + mapList[maps[2]].replace('_', ' ') + ChatColor.GRAY + " [" + ChatColor.WHITE + String.valueOf(voteArray[2]) + ChatColor.GRAY + " Votes]");
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.GRAY + " 4. " + ChatColor.GOLD + mapList[maps[3]].replace('_', ' ') + ChatColor.GRAY + " [" + ChatColor.WHITE + String.valueOf(voteArray[3]) + ChatColor.GRAY + " Votes]");
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.GRAY + " 5. " + ChatColor.GOLD + mapList[maps[4]].replace('_', ' ') + ChatColor.GRAY + " [" + ChatColor.WHITE + String.valueOf(voteArray[4]) + ChatColor.GRAY + " Votes]");
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
				maps[i] = randomInt;
				i++;
				usedNumbers.add(randomInt);
			}
		}
	}
	
	public void chat(String message) {
		for (SpleggPlayer hp : players) {
			hp.mcPlayer.sendMessage(message);
		}
	}
	
	public void onInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
			SpleggPlayer hp = Main.playerMap.get(e.getPlayer());
			if (e.getItem() != null) {
				if (!inGame) {
					switch (e.getItem().getType()) {
					case WRITTEN_BOOK:
						break;
					case DIAMOND:
						hp.mcPlayer.openInventory(voteInv());
						break;
					case YELLOW_FLOWER:
						break;
					case SLIME_BALL:
						Functions.sendToServer(hp.mcPlayer, "lobby0", plugin);
						break;
					default:
						break;
					}
				} else {
					switch (e.getItem().getType()) {
					case IRON_SPADE:
						Egg egg = hp.mcPlayer.launchProjectile(Egg.class);
						Vector direction = hp.mcPlayer.getLocation().getDirection();
						egg.setVelocity(direction.multiply(1.5));
						eggMap.put(egg, hp);
						hp.eggsFired++;
						break;
					default:
						break;
					}
				}
			}
		}
		e.setCancelled(true);
	}
	
	public void onInventoryClick(InventoryClickEvent e) {
		SpleggPlayer hp = Main.playerMap.get(e.getWhoClicked());
		if (inGame) {
			
		} else {
			switch (e.getCurrentItem().getType()) {
			case MAP:
				int mapNumber = e.getSlot() / 9;
				votes.put(hp, mapNumber);
				hp.mcPlayer.openInventory(voteInv());
				break;
			default:
				break;
			}
		}
		e.setCancelled(true);
	}
	
	public void onProjectileHit(ProjectileHitEvent e) {
		if (e.getEntityType() == EntityType.EGG) {
			Location eggLoc = e.getEntity().getLocation();
			Location forward = eggLoc.add(e.getEntity().getVelocity());
			forward.getBlock().setType(Material.AIR);
			eggMap.get((Egg) e.getEntity()).eggsLanded++;
		}
	}
	
	public int[] tallyVotes() {
		int[] voteTotal = new int[5];
		voteTotal[0] = 0;
		voteTotal[1] = 0;
		voteTotal[2] = 0;
		voteTotal[3] = 0;
		voteTotal[4] = 0;
		for (Map.Entry<SpleggPlayer, Integer> set : votes.entrySet()) {
			voteTotal[set.getValue()]++;
		}
		return voteTotal;
	}
	
	public Inventory voteInv() {
		Inventory inv = Bukkit.createInventory(null, 45, "Vote for an Option");
		
		String[] mapList = Constants.mapList;
		int[] voteArray = tallyVotes();
		
		for (int i = 0; i < 5; i++) {
			ItemStack map = new ItemStack(Material.MAP, 1);
			ItemMeta meta = map.getItemMeta();
			meta.setDisplayName(mapList[maps[i]].replaceAll("_", " "));
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
	
	public void onPlayerLeave(PlayerQuitEvent event) {
		SpleggPlayer hp = Main.playerMap.get(event.getPlayer());
		votes.remove(hp);
		players.remove(hp);
	}
	
	public String chatPrefix() {
		return ChatColor.DARK_GRAY + "|" + ChatColor.AQUA + " Splegg " + ChatColor.DARK_GRAY + "|";
	}
}
