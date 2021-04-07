package net.rypixel.hiveHide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.connorlinfoot.titleapi.TitleAPI;

public class HideWorld {

	public Plugin plugin;
	public int id;
	
	public boolean inGame;
	public boolean loadingGame;
	public boolean canVote = true;
	public boolean starting = false;
	public BukkitTask timer;
	public int countDown = 0;
	
	public ArrayList<HidePlayer> players = new ArrayList<HidePlayer>();
	public HashMap<HidePlayer, Integer> votes = new HashMap<HidePlayer, Integer>();
	
	public String[] randomMaps = new String[5];
	public String mapName = "";
	
	public World world;
	
	public World gameWorld;
	
	HideWorld(Plugin plugin, int id) {
		this.plugin = plugin;
		this.id = id;
	}
	
	public void init() {
		world = Functions.createNewWorld(Bukkit.getWorld("hideandseekmap"), String.valueOf(id));
		countDown = 20 * 20;
		pickMaps();
		update();
	}
	
	public void welcomePlayer(HidePlayer hp) {
		players.add(hp);
		Functions.showAllPlayers(players);
		hp.mcPlayer.teleport(new Vector(-79.5, 90, 61.5).toLocation(world));
		
		int[] tempVotes = tallyVotes();
		
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.YELLOW + " Vote for a map! " + ChatColor.GRAY + "Use " + ChatColor.WHITE + "/v #" + ChatColor.WHITE + " or click.");
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.GRAY + " 1. " + ChatColor.GOLD + randomMaps[0] + ChatColor.GRAY + " [" + ChatColor.WHITE + tempVotes[0] + ChatColor.GRAY + " Votes]");
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.GRAY + " 2. " + ChatColor.GOLD + randomMaps[1] + ChatColor.GRAY + " [" + ChatColor.WHITE + tempVotes[1] + ChatColor.GRAY + " Votes]");
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.GRAY + " 3. " + ChatColor.GOLD + randomMaps[2] + ChatColor.GRAY + " [" + ChatColor.WHITE + tempVotes[2] + ChatColor.GRAY + " Votes]");
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.GRAY + " 4. " + ChatColor.GOLD + randomMaps[3] + ChatColor.GRAY + " [" + ChatColor.WHITE + tempVotes[3] + ChatColor.GRAY + " Votes]");
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.GRAY + " 5. " + ChatColor.GOLD + randomMaps[4] + ChatColor.GRAY + " [" + ChatColor.WHITE + tempVotes[4] + ChatColor.GRAY + " Votes]");
	
		Inventory inv = hp.mcPlayer.getInventory();
		inv.clear();
		inv.setItem(0, Constants.rules);
		inv.setItem(1, Constants.vote);
		inv.setItem(4, Constants.locker);
		inv.setItem(8, Constants.hub);
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
		loadingGame = true;
		
		canVote = false;
		int[] votes = tallyVotes();
		int highestN = 0;
		int index = 0;
		for (int i = 0; i < 5; i++) {
			if (votes[i] > highestN) {
				index = i;
				highestN = votes[i];
			}
		}
		mapName = randomMaps[index];
		
		Inventory blockSelectUI = blockSelectUI();
		for (HidePlayer hp : players) {
			TitleAPI.sendTitle(hp.mcPlayer, 20, 20, 20, ChatColor.GREEN + "" + ChatColor.BOLD + "Choose Block");
			TitleAPI.sendSubtitle(hp.mcPlayer, 20, 20, 20, ChatColor.DARK_AQUA + "Choose your block!");
			hp.mcPlayer.openInventory(blockSelectUI);
		}
		
		new BukkitRunnable() {
			public void run() {
				inGame = true;
				Random r = new Random();
				for (HidePlayer hp : players) {
					if (hp.block == null) {
						hp.block = Constants.blocksToHide.get(mapName)[r.nextInt(5)];
					}
				}
				
				Bukkit.createWorld(new WorldCreator(mapName));
				gameWorld = Functions.createNewWorld(Bukkit.getWorld(mapName), String.valueOf(id));
				Bukkit.unloadWorld(Bukkit.getWorld(mapName), false);
				
				
				ArrayList<HidePlayer> playersTemp = players;
				for (int i = 0; i < players.size() / 4; i++) {
					int randomNumber = r.nextInt(playersTemp.size());
					playersTemp.get(randomNumber).isHunter = true;
					playersTemp.remove(randomNumber);
				}
				
				for (HidePlayer hp : players) {
					//teleport the player
				}
			}
		}.runTaskLater(plugin, 200L);
		
	}
	
	public void onPlayerLeave(HidePlayer hp) {
		
	}
	
	public Inventory blockSelectUI() {
		Material[] blockList = Constants.blocksToHide.get(mapName);
		Inventory select = Bukkit.createInventory(null, 9, "Pick A Block To Hide As");
		for (int i = 0; i < 5; i++) {
			ItemStack item = new ItemStack(blockList[i], 1);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.YELLOW + Functions.fixCapitalization(blockList[i].toString()));
			ArrayList<String> lore = new ArrayList<String>();
			lore.add("");
			lore.add(ChatColor.GRAY + "Will this " + Functions.fixCapitalization(blockList[i].toString()) + " to");
			lore.add(ChatColor.GRAY + "a good choice?");
			lore.add("");
			lore.add(ChatColor.AQUA + "" + ChatColor.BOLD + "Block Level");
			lore.add(ChatColor.GRAY + "N/A");
			lore.add("");
			lore.add(ChatColor.AQUA + "► Click to Select");
			meta.setLore(lore);
			item.setItemMeta(meta);
			select.setItem(i, item);
		}
		return select;
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
		event.setCancelled(true);
		HidePlayer hp = Main.playerMap.get(event.getWhoClicked());
		if (inGame) {
			
		} else if (starting) {
			if (event.getCurrentItem() != null) {
				boolean validItem = false;
				for (Material m : Constants.blocksToHide.get(mapName)) {
					if (event.getCurrentItem().getType() == m) {
						validItem = true;
					}
				}
				if (validItem == true) {
					hp.block = event.getCurrentItem().getType();
				}
				hp.mcPlayer.closeInventory();
			}
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
	
	public void onEntityDamage(EntityDamageEvent event) {
		if (!inGame) {
			event.setCancelled(true);
		}
	}
	
	public void onPlayerMove(PlayerMoveEvent event) {
		HidePlayer hp = Main.playerMap.get(event.getPlayer());
		Vector loc = new Vector(hp.mcPlayer.getLocation().getBlockX(), hp.mcPlayer.getLocation().getBlockY(), hp.mcPlayer.getLocation().getBlockZ());
		if (hp.lastCoords != loc) {
			if (hp.solid = true) {
				hp.lastCoords.toLocation(gameWorld).getBlock().setType(Material.AIR);
				hp.solid = false;
			}
			hp.lastCoords = loc;
			hp.lastMoved = 0;
		}
	}
	
	public void chat(String message) {
		for (HidePlayer hp : players) {
			hp.mcPlayer.sendMessage(message);
		}
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
	
	public void update() {
		timer = new BukkitRunnable() {
			public void run() {
				if (!starting && !inGame) {
					if (players.size() > 5 && !starting) {
						starting = true;
					} else {
						for (HidePlayer hp : players) {
							TitleAPI.sendTitle(hp.mcPlayer, 0, 20, 0, "");
							TitleAPI.sendSubtitle(hp.mcPlayer, 0, 20, 0, ChatColor.YELLOW + String.valueOf(6 - players.size()) + " players needed to start...");
						}
					}
					
					if (starting) {
						countDown--;
						
						if (countDown > 0) {
							for (HidePlayer hp : players) {
								TitleAPI.sendTitle(hp.mcPlayer, 0, 20, 0, "");
								TitleAPI.sendSubtitle(hp.mcPlayer, 0, 20, 0, ChatColor.GREEN + "Starting game in " + String.valueOf(countDown / 20));
							}
						}
						
						if (countDown == 0) {
							initGame();
						}
						
						if (countDown == 300) {
							canVote = false;
							int[] votes = tallyVotes();
							int highestN = 0;
							int index = 0;
							for (int i = 0; i < 5; i++) {
								if (votes[i] < highestN) {
									index = i;
									highestN = votes[i];
								}
							}
							chat(chatPrefix() + ChatColor.DARK_AQUA + " Voting has ended!" + ChatColor.AQUA + " The map " + randomMaps[index] + ChatColor.AQUA + "has won!");
							mapName = randomMaps[index];
						}
					}
				} else if (inGame) {
					for (HidePlayer hp : players) {
						hp.lastMoved++;
						if (hp.lastMoved == 100) {
							hp.solid = true;
							hp.lastCoords.toLocation(gameWorld).getBlock().setType(hp.block);
						}
					}
				}
			}
		}.runTaskTimer(plugin, 0L, 1L);
	}
	
	public String chatPrefix() {
		return ChatColor.GRAY + "|" + ChatColor.AQUA + " Hide"  + ChatColor.GREEN + "And" + ChatColor.YELLOW + "Seek " + ChatColor.GRAY + "|";
	}
}
