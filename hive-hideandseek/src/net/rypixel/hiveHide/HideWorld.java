package net.rypixel.hiveHide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
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

public class HideWorld {

	public Plugin plugin;
	public int id;
	
	public boolean inGame;
	public boolean loadingGame;
	public boolean canVote = true;
	public boolean starting = false;
	public BukkitTask timer;
	public int countDown = 0;
	public int timeRemaining = 0;
	
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
		hp.mcPlayer.setGameMode(GameMode.ADVENTURE);
		hp.mcPlayer.removePotionEffect(PotionEffectType.INVISIBILITY);
		
		int[] tempVotes = tallyVotes();
		
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.YELLOW + " Vote for a map! " + ChatColor.GRAY + "Use " + ChatColor.WHITE + "/v #" + ChatColor.WHITE + " or click.");
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.GRAY + " 1. " + ChatColor.GOLD + randomMaps[0].replace('_', ' ') + ChatColor.GRAY + " [" + ChatColor.WHITE + tempVotes[0] + ChatColor.GRAY + " Votes]");
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.GRAY + " 2. " + ChatColor.GOLD + randomMaps[1].replace('_', ' ') + ChatColor.GRAY + " [" + ChatColor.WHITE + tempVotes[1] + ChatColor.GRAY + " Votes]");
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.GRAY + " 3. " + ChatColor.GOLD + randomMaps[2].replace('_', ' ') + ChatColor.GRAY + " [" + ChatColor.WHITE + tempVotes[2] + ChatColor.GRAY + " Votes]");
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.GRAY + " 4. " + ChatColor.GOLD + randomMaps[3].replace('_', ' ') + ChatColor.GRAY + " [" + ChatColor.WHITE + tempVotes[3] + ChatColor.GRAY + " Votes]");
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.GRAY + " 5. " + ChatColor.GOLD + randomMaps[4].replace('_', ' ') + ChatColor.GRAY + " [" + ChatColor.WHITE + tempVotes[4] + ChatColor.GRAY + " Votes]");
	
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
		timeRemaining = 19200;
		
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
				Random r = new Random();
				for (HidePlayer hp : players) {
					if (hp.block == null) {
						hp.block = Constants.blocksToHide.get(mapName)[r.nextInt(5)];
					}
				}
				
				Bukkit.createWorld(new WorldCreator(mapName));
				gameWorld = Functions.createNewWorld(Bukkit.getWorld(mapName), String.valueOf(id));
				Bukkit.unloadWorld(Bukkit.getWorld(mapName), false);
				
				
				ArrayList<HidePlayer> playersTemp = new ArrayList<HidePlayer>();
				for (HidePlayer p : players) {
					playersTemp.add(p);
				}
				
				int seekerCount = players.size() / 4;
				if (seekerCount == 0) {
					seekerCount = 1;
				}
				
				for (int i = 0; i < seekerCount; i++) {
					int randomNumber = r.nextInt(playersTemp.size());
					playersTemp.get(randomNumber).isHunter = true;
					playersTemp.remove(randomNumber);
				}
				
				for (HidePlayer hp : players) {
					hp.solid = false;
					hp.lastLoc = new Vector(0, 0, 0);
					if (!hp.isHunter) {
						hp.blockEntity = gameWorld.spawnFallingBlock(new Vector(0, 0, 0).toLocation(gameWorld), hp.block, (byte) 0);
						hp.mcPlayer.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 10000, 100, false));
						Inventory playerInv = hp.mcPlayer.getInventory();
						playerInv.setItem(0, new ItemStack(Material.WOOD_SWORD, 1));
						hp.mcPlayer.teleport(new Vector(0.5, 101, 0.5).toLocation(gameWorld));
					} else {
						Inventory playerInv = hp.mcPlayer.getInventory();
						ItemStack[] armorContents = new ItemStack[4];
						armorContents[0] = new ItemStack(Material.IRON_HELMET, 1);
						armorContents[0] = new ItemStack(Material.IRON_CHESTPLATE, 1);
						armorContents[0] = new ItemStack(Material.IRON_LEGGINGS, 1);
						armorContents[0] = new ItemStack(Material.IRON_BOOTS, 1);
						playerInv.setItem(0, new ItemStack(Material.DIAMOND_SWORD, 1));
					}
				}
				inGame = true;
			}
		}.runTaskLater(plugin, 200L);
		
		new BukkitRunnable() {
			public void run() {
				blockTransform();
			}
		}.runTaskLater(plugin, 300L);
	}
	
	public void onPlayerLeave(HidePlayer hp) {
		players.remove(hp);
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
			lore.add(ChatColor.AQUA + "??? Click to Select");
			meta.setLore(lore);
			item.setItemMeta(meta);
			select.setItem(i, item);
		}
		return select;
	}
	
	
	public void onInteract(PlayerInteractEvent event) {
		HidePlayer hp = Main.playerMap.get(event.getPlayer());
		if (inGame) {
			if (event.getAction() == Action.LEFT_CLICK_BLOCK && hp.isHunter) {
				if (hp.attackCooldown >= 10) {
					hp.attackCooldown -= 10;
					Location location = hp.mcPlayer.getTargetBlock((HashSet<Byte>) null, 5).getLocation();
					for (HidePlayer hider : players) {
						if (!hider.isHunter) {
							if (location.getBlock() == hider.placedBlock) {
								hp.lastMoved = 0;
							}
						}
					}
				} else {
					hp.mcPlayer.sendMessage(ChatColor.RED + "Enter the chill zone!");
				}
			}
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
	
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		HidePlayer seeker = Main.playerMap.get(event.getDamager());
		HidePlayer hider = Main.playerMap.get(event.getEntity());
		if (!inGame) {
			event.setCancelled(true);
		} else if (inGame) {
			if (hider.mcPlayer.getHealth() - event.getFinalDamage() < 0) {
				hider.isHunter = true;
				hider.mcPlayer.teleport(new Vector(0.5, 101, 0.5).toLocation(gameWorld));
				
				Inventory playerInv = hider.mcPlayer.getInventory();
				ItemStack[] armorContents = new ItemStack[4];
				armorContents[0] = new ItemStack(Material.IRON_HELMET, 1);
				armorContents[0] = new ItemStack(Material.IRON_CHESTPLATE, 1);
				armorContents[0] = new ItemStack(Material.IRON_LEGGINGS, 1);
				armorContents[0] = new ItemStack(Material.IRON_BOOTS, 1);
				playerInv.setItem(0, new ItemStack(Material.DIAMOND_SWORD, 1));
			}
		}
	}
	
	public void onPlayerMove(PlayerMoveEvent event) {
		HidePlayer hp = Main.playerMap.get(event.getPlayer());
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
				
				for (HidePlayer hp : players) {
					hp.mcPlayer.setFoodLevel(20);
					hp.mcPlayer.setSaturation(20);
				}
				
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
							cancel();
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
				}
			}
		}.runTaskTimer(plugin, 0L, 1L);
	}
	
	public void blockTransform() {
		timer = new BukkitRunnable() {
			public void run() {
				for (HidePlayer hp : players) {
					if (!hp.isHunter) {
						hp.lastMoved++;
						//Bukkit.broadcastMessage(hp.mcPlayer.getDisplayName() + " " + String.valueOf(hp.lastMoved));
						
						Vector loc = new Vector(hp.mcPlayer.getLocation().getBlockX(), hp.mcPlayer.getLocation().getBlockY(), hp.mcPlayer.getLocation().getBlockZ());
						if (hp.lastLoc.distance(loc) > 0.5) {
							hp.lastMoved = 0;
						}
						
						hp.lastLoc = loc;
						
						if (hp.lastMoved > 100 && !hp.solid) {
							hp.placedBlock = hp.mcPlayer.getLocation().getBlock();
							//hp.mcPlayer.setGameMode(GameMode.SPECTATOR);
							hp.placedBlock.setType(hp.block);
							hp.mcPlayer.sendBlockChange(hp.placedBlock.getLocation(), Material.AIR, (byte) 0);
							Vector location = loc;
							location = location.add(new Vector(0.5, 0, 0.5));
							hp.solid = true;
						}
						
						if (hp.lastMoved < 100 && hp.solid) {
							hp.solid = false;
							hp.placedBlock.setType(Material.AIR);
						}
						
						if (!hp.solid) {
							hp.blockEntity.teleport(hp.mcPlayer);
						}
						
						if (hp.lastMoved > 20) {
							if (hp.lastMoved > 80) {
								TitleAPI.sendTitle(hp.mcPlayer, 20, 20, 20, ChatColor.GREEN + ">>>>" + ChatColor.GRAY + ">");
							} else if (hp.lastMoved > 60) {
								TitleAPI.sendTitle(hp.mcPlayer, 20, 20, 20, ChatColor.GREEN + ">>>" + ChatColor.GRAY + ">>");
							} else if (hp.lastMoved > 40) {
								TitleAPI.sendTitle(hp.mcPlayer, 20, 20, 20, ChatColor.GREEN + ">>" + ChatColor.GRAY + ">>>");
							} else if (hp.lastMoved > 20) {
								TitleAPI.sendTitle(hp.mcPlayer, 20, 20, 20, ChatColor.GREEN + ">" + ChatColor.GRAY + ">>>>");
							}
						}
					} else {
						hp.attackCooldown++;
					}
				}
				
				int hiderCount = 0;
				for (HidePlayer hp : players) {
					if (!hp.isHunter) {
						hiderCount++;
					}
				}
				if (hiderCount == 0) {
					gameEnding();
				}
				
				String time = "";
				if (timeRemaining < 600) {
					time = DurationFormatUtils.formatDuration(30000 - (timeRemaining * 50), "mm:ss");
				} else {
					time = DurationFormatUtils.formatDuration(960000 - (timeRemaining * 50), "mm:ss");
				}
				int playersAlive = 0;
				int playersDead = 0;
				for (HidePlayer hp : players) {
					if (hp.isHunter) {
						playersDead++;
					} else {
						playersAlive++;
					}
				}
				for (HidePlayer hp : players) {
					hp.scoreboard.setTitle(ChatColor.AQUA + "Hide" + ChatColor.GREEN + "And" + ChatColor.YELLOW + "Seek");
					hp.scoreboard.setSlot(15, "");
					if (timeRemaining < 600) {
						hp.scoreboard.setSlot(14, ChatColor.GREEN + "Warmup Left");
					} else {
						hp.scoreboard.setSlot(14, ChatColor.GREEN + "Time Remaining");
					}
					hp.scoreboard.setSlot(13, time);
					hp.scoreboard.setSlot(12, "");
					hp.scoreboard.setSlot(11, ChatColor.AQUA + "Players Alive");
					hp.scoreboard.setSlot(10, String.valueOf(playersAlive) + ChatColor.GREEN + " Hiders");
					hp.scoreboard.setSlot(9, String.valueOf(playersDead) + ChatColor.GREEN + " Seekers"); 
					hp.scoreboard.setSlot(8, "");
					hp.scoreboard.setSlot(7, ChatColor.GREEN + "My Round Stats");
					hp.scoreboard.setSlot(6, "N/A" + ChatColor.GRAY + " Kills");
					hp.scoreboard.setSlot(5, "N/A" + ChatColor.GRAY + " Points");
					hp.scoreboard.setSlot(4, "");
					hp.scoreboard.setSlot(3, ChatColor.GREEN + "Taunts Available");
					hp.scoreboard.setSlot(2, ChatColor.GRAY + "-------------");
					hp.scoreboard.setSlot(1, ChatColor.GOLD + "play." + ChatColor.YELLOW + "HiveMC" + ChatColor.GOLD + ".com");
				}
				if (timeRemaining == 600) {
					chat(chatPrefix() + ChatColor.RED + " Ready or not, here they come!");
					for (HidePlayer hp : players) {
						hp.mcPlayer.playSound(hp.mcPlayer.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 1);
					}
					for (HidePlayer hp : players) {
						if (hp.isHunter) {
							hp.mcPlayer.teleport(new Vector(0.5, 101, 0.5).toLocation(gameWorld));
						}
					}
				}
				if (timeRemaining == 580) {
					chat(chatPrefix() + ChatColor.YELLOW + " Starting in " + ChatColor.WHITE + "1");
					for (HidePlayer hp : players) {
						hp.mcPlayer.playSound(hp.mcPlayer.getLocation(), Sound.NOTE_PIANO, 1, 1);
					}
				}
				if (timeRemaining == 560) {
					chat(chatPrefix() + ChatColor.YELLOW + " Starting in " + ChatColor.WHITE + "2");
					for (HidePlayer hp : players) {
						hp.mcPlayer.playSound(hp.mcPlayer.getLocation(), Sound.NOTE_PIANO, 1, 1);
					}
				}
				if (timeRemaining == 540) {
					chat(chatPrefix() + ChatColor.YELLOW + " Starting in " + ChatColor.WHITE + "3");
					for (HidePlayer hp : players) {
						hp.mcPlayer.playSound(hp.mcPlayer.getLocation(), Sound.NOTE_PIANO, 1, 1);
					}
				}
			}
		}.runTaskTimer(plugin, 0L, 2L);
	}
	
	public void gameEnding() {
		
	}
	
	public String chatPrefix() {
		return ChatColor.GRAY + "|" + ChatColor.AQUA + " Hide"  + ChatColor.GREEN + "And" + ChatColor.YELLOW + "Seek " + ChatColor.GRAY + "|";
	}
}
