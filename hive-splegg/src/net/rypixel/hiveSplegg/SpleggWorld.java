package net.rypixel.hiveSplegg;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import com.connorlinfoot.titleapi.TitleAPI;

import me.vagdedes.mysql.database.MySQL;

public class SpleggWorld {

	public Plugin plugin;
	public World world;
	public World gameWorld;
	public int id;
	
	public boolean gameStarting;
	public int countdown = 1200;
	
	public int gameTimer;
	public boolean inGame;
	public boolean gameOver = false;
	public BukkitTask loop;
	
	public boolean canVote = true;
	
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
		countdown = 1200;
		selectMaps();
		update();
	}
	
	public void stop() {
		
		for (SpleggPlayer hp : players) {
			
			hp.mcPlayer.setFlying(false);
			hp.mcPlayer.setLevel(0);
			
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
					for (SpleggPlayer hp : players) {
						hp.scoreboard.setTitle(ChatColor.AQUA + "Splegg");
						hp.scoreboard.setSlot(14, "");
						hp.scoreboard.setSlot(13, ChatColor.GREEN + "Tokens");
						hp.scoreboard.setSlot(12, ChatColor.GRAY + String.valueOf(hp.tokens));
						hp.scoreboard.setSlot(11, "");
						hp.scoreboard.setSlot(10, ChatColor.YELLOW + "Your Stats");
						hp.scoreboard.setSlot(9, ChatColor.DARK_AQUA + "Points: " + ChatColor.AQUA + String.valueOf(hp.points));
						hp.scoreboard.setSlot(8, ChatColor.DARK_AQUA + "Played: " + ChatColor.AQUA + String.valueOf(hp.played));
						hp.scoreboard.setSlot(7, ChatColor.DARK_AQUA + "Wins: " + ChatColor.AQUA + String.valueOf(hp.wins));
						hp.scoreboard.setSlot(6, ChatColor.DARK_AQUA + "Deaths: " + ChatColor.AQUA + String.valueOf(hp.deaths));
						hp.scoreboard.setSlot(5, ChatColor.DARK_AQUA + "Eggs Fired: " + ChatColor.AQUA + String.valueOf(hp.eggsFired));
						hp.scoreboard.setSlot(4, ChatColor.DARK_AQUA + "Blocks Broken: " + ChatColor.AQUA + String.valueOf(hp.eggsLanded));
						hp.scoreboard.setSlot(3, "");
						hp.scoreboard.setSlot(2, ChatColor.DARK_GRAY + "----------------");
						hp.scoreboard.setSlot(1, ChatColor.GREEN + "play." + ChatColor.AQUA + "dejavumc" + ChatColor.AQUA + ".net");
					}
					if (players.size() > 5 && !gameStarting) {
						gameStarting = true;
					} else {
						for (SpleggPlayer hp : players) {
							TitleAPI.sendTitle(hp.mcPlayer, 0, 20, 0, "");
							TitleAPI.sendSubtitle(hp.mcPlayer, 0, 20, 0, ChatColor.YELLOW + String.valueOf(6 - players.size()) + " players needed to start...");
						}
					}
					
					if (gameStarting) {
						countdown--;
						
						for (SpleggPlayer hp : players) {
							TitleAPI.sendTitle(hp.mcPlayer, 0, 20, 0, "");
							TitleAPI.sendSubtitle(hp.mcPlayer, 0, 20, 0, ChatColor.GREEN + "Starting game in " + String.valueOf(countdown / 20));
						}
						
						if (countdown == 0) {
							initGame();
						}
						if (canVote && countdown < 300) {
							canVote = false;
							for (SpleggPlayer hp : players) {
								hp.mcPlayer.getInventory().remove(Material.DIAMOND);
							}
							int[] votes = tallyVotes();
							int highestNumber = 0;
							Random random = new Random();
							int mapNumber = random.nextInt(5);
							int n = 0;
							for (int i : votes) {
								if (i > highestNumber) {
									highestNumber = i;
									mapNumber = n;
								}
								n++;
							}
							String[] mapList = Constants.mapList;
							chat(chatPrefix() + ChatColor.DARK_AQUA + " Voting has ended!" + ChatColor.AQUA + " The map " + ChatColor.WHITE + mapList[maps[mapNumber]].replaceAll("_", " ") + ChatColor.AQUA + " has won!");
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
							gameWorld.spawnEntity(hp.mcPlayer.getLocation(), EntityType.LIGHTNING);
							hp.mcPlayer.teleport(new Vector(0, 100, 0).toLocation(gameWorld));
							hp.mcPlayer.setGameMode(GameMode.SURVIVAL);
							hp.mcPlayer.setAllowFlight(true);
							hp.mcPlayer.setFlying(true);
							hp.mcPlayer.getInventory().remove(Material.IRON_SPADE);
							TitleAPI.sendTitle(hp.mcPlayer, 20, 20, 20, ChatColor.RED + "YOU DIED!");
							hp.mcPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 1, false, true));
							hp.deaths++;
							hp.mcPlayer.getInventory().clear();
							hp.mcPlayer.getInventory().setItem(7, Constants.again);
							hp.mcPlayer.getInventory().setItem(0, Constants.players);
							hp.mcPlayer.getInventory().setItem(8, Constants.hub);
							
							for (SpleggPlayer hp1 : players) {
								if (hp1.alive) {
									hp1.mcPlayer.hidePlayer(hp.mcPlayer);
								}
								hp.mcPlayer.showPlayer(hp1.mcPlayer);
							}
						}
					}
					
					//Check if game is over
					int alivePlayers = 0;
					SpleggPlayer winner = null;
					for (SpleggPlayer hp : players) {
						if (hp.alive) {
							alivePlayers++;
							winner = hp;
						}
					}
					
					for (SpleggPlayer hp : players) {
						
						String time = DurationFormatUtils.formatDuration(600000 - (gameTimer * 50), "mm:ss");
						int seconds = gameTimer / 20;
					     
						hp.mcPlayer.setLevel(600 - seconds);
						
						hp.scoreboard.setTitle(ChatColor.AQUA + "Splegg " + ChatColor.DARK_GRAY + ">>"  + ChatColor.GRAY + time);
						hp.scoreboard.setSlot(13, "");
						hp.scoreboard.setSlot(12, ChatColor.RED + "Players");
						hp.scoreboard.setSlot(11, String.valueOf(alivePlayers));
						hp.scoreboard.setSlot(10, "");
						hp.scoreboard.setSlot(9, ChatColor.YELLOW + "Your Stats");
						hp.scoreboard.setSlot(8, ChatColor.GRAY + "Eggs Fired: " + ChatColor.WHITE + String.valueOf(hp.eggsFiredTemp));
						hp.scoreboard.setSlot(7, ChatColor.GRAY + "Blocks Destroyed: " + ChatColor.WHITE + String.valueOf(hp.eggsLandedTemp));
						hp.scoreboard.setSlot(6, ChatColor.GRAY + "Powerups: " + ChatColor.WHITE + "N/A");
						hp.scoreboard.setSlot(5, "");
						hp.scoreboard.setSlot(4, ChatColor.RED + "No Powerups!");
						hp.scoreboard.setSlot(3, "");
						hp.scoreboard.setSlot(2, ChatColor.DARK_GRAY + "----------------");
						hp.scoreboard.setSlot(1, ChatColor.GREEN + "play." + ChatColor.AQUA + "dejavumc" + ChatColor.GREEN + ".com");
					}
					
					if (gameTimer % 600 == 0) {
						Random rnd = new Random();
						int rndNum = rnd.nextInt(players.size());
						SpleggPlayer hp = players.get(rndNum);
						Vector playerLoc = hp.mcPlayer.getLocation().toVector();
						Block b = gameWorld.getBlockAt(new Vector(playerLoc.getBlockX() + rnd.nextInt(10) - 5, playerLoc.getBlockY() + rnd.nextInt(5), playerLoc.getBlockZ() + rnd.nextInt(10) - 5).toLocation(gameWorld));
						b.setType(Material.ENDER_CHEST);
						chat(chatPrefix() + ChatColor.AQUA + " A powerup has spawned!");
					}
					
					if (alivePlayers < 2 && !gameOver) {
						gameOver = true;
						winner.wins++;
						for (SpleggPlayer hp : players) {
							for (SpleggPlayer hp1 : players) {
								hp.mcPlayer.showPlayer(hp1.mcPlayer);
							}
							TitleAPI.sendTitle(hp.mcPlayer, 20, 20, 20, ChatColor.RED + "Game. OVER!");
							hp.played++;
							MySQL.update("UPDATE splegg SET played=\"" + String.valueOf(hp.played) + "\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
							MySQL.update("UPDATE splegg SET wins=\"" + String.valueOf(hp.wins) + "\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
							MySQL.update("UPDATE splegg SET deaths=\"" + String.valueOf(hp.deaths) + "\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
							MySQL.update("UPDATE splegg SET eggsFired=\"" + String.valueOf(hp.eggsFired) + "\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
							MySQL.update("UPDATE splegg SET blocksbroken=\"" + String.valueOf(hp.eggsLanded) + "\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
						}
						String winnerName = winner.mcPlayer.getDisplayName();
						new BukkitRunnable() {
							public void run() {
								SpleggPlayer mostShot = players.get(0);
								SpleggPlayer mostBroken = players.get(0);
								
								for (SpleggPlayer hp : players) {
									if (hp.eggsFiredTemp > mostShot.eggsFiredTemp) {
										mostShot = hp;
									}
									if (hp.eggsLandedTemp > mostBroken.eggsLandedTemp) {
										mostBroken = hp;
									}
								}
								
								for (SpleggPlayer hp : players) {
									hp.mcPlayer.sendMessage("");
									hp.mcPlayer.sendMessage(ChatColor.RED + "Game. OVER! " + ChatColor.BLUE + winnerName + ChatColor.GRAY + " won the game!");
									hp.mcPlayer.sendMessage("");
									hp.mcPlayer.sendMessage(ChatColor.BLUE + mostBroken.mcPlayer.getDisplayName() + ChatColor.DARK_GREEN + " destroyed the most blocks! " + ChatColor.GREEN + "(" + String.valueOf(mostBroken.eggsLandedTemp) + " blocks)");
									hp.mcPlayer.sendMessage(ChatColor.BLUE + mostShot.mcPlayer.getDisplayName() + ChatColor.GOLD + " shot the most eggs! " + ChatColor.YELLOW + "(" + String.valueOf(mostShot.eggsFiredTemp) + " eggs)");
									hp.mcPlayer.sendMessage("");
									hp.mcPlayer.sendMessage(ChatColor.AQUA + "[Tweet Game]    " + ChatColor.GREEN + "[New Game]");
									hp.mcPlayer.sendMessage("");
								}
						    }
						}.runTaskLater(plugin, 100L);
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
		
		int[] votes = tallyVotes();
		int highestNumber = 0;
		Random random = new Random();
		int mapNumber = random.nextInt(5);
		int n = 0;
		for (int i : votes) {
			if (i > highestNumber) {
				highestNumber = i;
				mapNumber = n;
			}
			n++;
		}
		String[] mapList = Constants.mapList;
		String mapName = mapList[maps[mapNumber]];
		Bukkit.createWorld(new WorldCreator(mapName));
		gameWorld = Functions.createNewWorld(Bukkit.getWorld(mapName), String.valueOf(id)); //Replace "spleggmap1" with the voted map later
		Bukkit.unloadWorld(Bukkit.getWorld(mapName), false);
		
		int tpIndex = 0;
		for (SpleggPlayer hp : players) {
			if (Constants.spawnLocations.containsKey(mapName)) {
				if (tpIndex < Constants.spawnLocations.get(mapName).length) {
					hp.mcPlayer.teleport(Constants.spawnLocations.get(mapName)[tpIndex].toLocation(gameWorld));
				} else {
					tpIndex = 0;
					hp.mcPlayer.teleport(Constants.spawnLocations.get(mapName)[tpIndex].toLocation(gameWorld));
				} 
			} else {
				hp.mcPlayer.teleport(new Vector(0, 100, 0).toLocation(gameWorld));
			}
			hp.mcPlayer.getInventory().clear();
			hp.mcPlayer.setFoodLevel(20);
			hp.mcPlayer.setSaturation(20);
			hp.alive = true;
			hp.played++;
			hp.eggsFiredTemp = 0;
			hp.eggsLandedTemp = 0;
			tpIndex++;
		}
		
		for (SpleggPlayer hp : players) {
			TitleAPI.sendTitle(hp.mcPlayer, 0, 21, 0, ChatColor.GOLD + "⑤");
			TitleAPI.sendSubtitle(hp.mcPlayer, 0, 21, 0, ChatColor.GRAY + "until start");
			hp.mcPlayer.setLevel(5);
		}
		
		new BukkitRunnable() {
			public void run() {
				for (SpleggPlayer hp : players) {
					TitleAPI.sendTitle(hp.mcPlayer, 0, 21, 0, ChatColor.GOLD + "④");
					TitleAPI.sendSubtitle(hp.mcPlayer, 0, 21, 0, ChatColor.GRAY + "until start");
					hp.mcPlayer.setLevel(4);
				}
		    }
		}.runTaskLater(plugin, 20L);
		
		new BukkitRunnable() {
			public void run() {
				for (SpleggPlayer hp : players) {
					TitleAPI.sendTitle(hp.mcPlayer, 0, 21, 0, ChatColor.GOLD + "③");
					TitleAPI.sendSubtitle(hp.mcPlayer, 0, 21, 0, ChatColor.GRAY + "until start");
					hp.mcPlayer.setLevel(3);
				}
		    }
		}.runTaskLater(plugin, 40L);
		
		new BukkitRunnable() {
			public void run() {
				for (SpleggPlayer hp : players) {
					TitleAPI.sendTitle(hp.mcPlayer, 0, 21, 0, ChatColor.GOLD + "②");
					TitleAPI.sendSubtitle(hp.mcPlayer, 0, 21, 0, ChatColor.GRAY + "until start");
					hp.mcPlayer.setLevel(2);
				}
		    }
		}.runTaskLater(plugin, 60L);
		
		new BukkitRunnable() {
			public void run() {
				for (SpleggPlayer hp : players) {
					TitleAPI.sendTitle(hp.mcPlayer, 0, 21, 0, ChatColor.GOLD + "①");
					TitleAPI.sendSubtitle(hp.mcPlayer, 0, 21, 0, ChatColor.GRAY + "until start");
					hp.mcPlayer.setLevel(1);
				}
		    }
		}.runTaskLater(plugin, 80L);
		
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
		hp.mcPlayer.setGameMode(GameMode.SURVIVAL);
		hp.mcPlayer.setAllowFlight(false);
		
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
	
		for (SpleggPlayer hp1 : players) {
			for (SpleggPlayer hp2 : players) {
				if (hp1 != hp2) {
					hp1.mcPlayer.showPlayer(hp2.mcPlayer);
				}
			}
		}
	}
	
	public void selectMaps() {		
		ArrayList<Integer> usedNumbers = new ArrayList<Integer>();
		Random random = new Random();
		int i = 0;
		while (i < 5) {
			int randomInt = random.nextInt(Constants.mapList.length);
			
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
						hp.mcPlayer.openInventory(Constants.shopGuns(hp));
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
						if (hp.currentPowerup == "shotgun") {
							Egg egg1 = hp.mcPlayer.launchProjectile(Egg.class);
							Vector direction1 = hp.mcPlayer.getLocation().getDirection();
							egg1.setVelocity(direction.multiply(1.5).add(new Vector(-1, 0, 0)));
							eggMap.put(egg1, hp);
							
							Egg egg2 = hp.mcPlayer.launchProjectile(Egg.class);
							Vector direction2 = hp.mcPlayer.getLocation().getDirection();
							egg2.setVelocity(direction.multiply(1.5).add(new Vector(1, 0, 0)));
							eggMap.put(egg2, hp);
						} else if (hp.currentPowerup == "rapid") {
							new BukkitRunnable() {
								public void run() {
									Egg egg1 = hp.mcPlayer.launchProjectile(Egg.class);
									Vector direction1 = hp.mcPlayer.getLocation().getDirection();
									egg1.setVelocity(direction.multiply(1.5));
									eggMap.put(egg1, hp);
								}
							}.runTaskLater(plugin, 2L);
						}
						hp.eggsFired++;
						hp.eggsFiredTemp++;
						hp.mcPlayer.playSound(hp.mcPlayer.getLocation(), Sound.GHAST_FIREBALL, 1, 1);
						break;
					case COMPASS:
						hp.mcPlayer.openInventory(Constants.playerSelector(players));
						break;
					case MINECART:
						players.remove(hp);
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
						break;
					case SLIME_BALL:
						Functions.sendToServer(hp.mcPlayer, "lobby0", plugin);
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
			switch (e.getCurrentItem().getType()) {
			case SKULL:
				SkullMeta meta = (SkullMeta) e.getCurrentItem().getItemMeta();
				String playerUUID = meta.getOwner();
				Location teleportLoc = Bukkit.getPlayer(UUID.fromString(playerUUID)).getLocation();
				hp.mcPlayer.teleport(teleportLoc);
				break;
			default:
				break;
			}
		} else {
			if (!e.getInventory().contains(Material.MAP)) {
				if (e.getSlot() != 0 && e.getSlot() != 9 && e.getSlot() != 18 && e.getSlot() != 27) {
					switch (e.getInventory().getContents()[53].getType()) {
					case GOLD_SPADE:
						CosmeticShop.gunShop(e, hp);
						break;
					case EGG:
						//CosmeticShop.soundShop(e, hp);
					default:
						break;
					}
				} else {
					switch (e.getCurrentItem().getType()) {
					case GOLD_SPADE:
						hp.mcPlayer.openInventory(Constants.shopGuns(hp));
						break;
//					case EGG:
//						hp.mcPlayer.openInventory(Constants.shopSounds(hp));
//						break;
					}
				}
			} else {
				switch (e.getCurrentItem().getType()) {
				case MAP:
					if (canVote) {
						int mapNumber = e.getSlot() / 9;
						votes.put(hp, mapNumber);
						hp.mcPlayer.openInventory(voteInv());
					} else {
						hp.mcPlayer.sendMessage(ChatColor.RED + "You can no longer vote!");
					}
					break;
				default:
					break;
				}
			}
		}
		e.setCancelled(true);
	}
	
	public void onProjectileHit(ProjectileHitEvent e) {
		if (e.getEntityType() == EntityType.EGG) {
			SpleggPlayer hp = eggMap.get((Egg) e.getEntity());
//			Location eggLoc = e.getEntity().getLocation();
//			Vector eggDirection = e.getEntity().getVelocity();
//			Block b = eggLoc.add(eggDirection.normalize()).getBlock();
			BlockIterator iterator = new BlockIterator(e.getEntity().getWorld(), e.getEntity().getLocation().toVector(), e.getEntity().getVelocity().normalize(), 0.0D, 4);
			Block b = null;
			while (iterator.hasNext()) {
				b = iterator.next();
				 
				if (b.getType() != Material.AIR) {
					break;
				}
			}
			if (b.getType() == Material.TNT) {
				b.getLocation().getWorld().createExplosion(b.getLocation(), 2);
			}
			if (b.getType() == Material.ENDER_CHEST) {
				Random rnd = new Random();
				int rndNum = rnd.nextInt(9);
				switch (rndNum) {
				case 0:
					hp.mcPlayer.sendMessage(ChatColor.AQUA + "You have triggered a sheep bomb!");
					Location loc = b.getLocation();
					new BukkitRunnable() {
						public void run() {
							loc.getWorld().createExplosion(loc, 5);
						}
					}.runTaskLater(plugin, 100L);
					break;
				case 1:
					hp.mcPlayer.sendMessage(ChatColor.AQUA + "You have unlocked a shotgun for the next 10 seconds!");
					hp.currentPowerup = "shotgun";
					new BukkitRunnable() {
						public void run() {
							hp.currentPowerup = "";
						}
					}.runTaskLater(plugin, 200L);
					break;
				case 2:
					hp.mcPlayer.sendMessage(ChatColor.AQUA + "You have recieved two pieces of TNT!");
					hp.mcPlayer.getInventory().addItem(new ItemStack(Material.TNT, 2));
					break;
				case 3:
					hp.mcPlayer.sendMessage(ChatColor.AQUA + "You will shoot faster for the next 5 seconds!");
					hp.currentPowerup = "rapid";
					break;
				case 4:
					hp.mcPlayer.sendMessage(ChatColor.AQUA + "Your shots are now explosive for 3 seconds!");
					hp.currentPowerup = "boom";
					new BukkitRunnable() {
						public void run() {
							hp.currentPowerup = "";
						}
					}.runTaskLater(plugin, 60L);
					break;
				case 5:
					hp.mcPlayer.sendMessage(ChatColor.AQUA + "You can now jump higher for 15 seconds!");
					hp.mcPlayer.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 300, 2), false);
					break;
				case 6:
					hp.mcPlayer.sendMessage(ChatColor.AQUA + "You are now invisible for 15 seconds!");
					hp.mcPlayer.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 300, 2), false);
					break;
				case 7:
					hp.mcPlayer.sendMessage(ChatColor.AQUA + "You can now run faster for 15 seconds!");
					hp.mcPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 300, 2), false);
					break;
				case 8:
					hp.mcPlayer.sendMessage(ChatColor.AQUA + "Everyone now has a pumpkin head for 3 seconds!");
					for (SpleggPlayer hp1 : players) {
						if (hp1 != hp) {
							ItemStack[] armor = new ItemStack[4];
							armor[0] = new ItemStack(Material.PUMPKIN, 1);
							armor[1] = new ItemStack(Material.PUMPKIN, 1);
							armor[2] = new ItemStack(Material.PUMPKIN, 1);
							armor[3] = new ItemStack(Material.PUMPKIN, 1);
							hp1.mcPlayer.getInventory().setArmorContents(armor);
						}
					}
					new BukkitRunnable() {
						public void run() {
							for (SpleggPlayer hp1 : players) {
								hp1.mcPlayer.getInventory().setArmorContents(null);
							}
						}
					}.runTaskLater(plugin, 60L);
					break;
				}
			}
            b.setType(Material.AIR);
            if (hp.currentPowerup == "boom") {
            	b.getLocation().getWorld().createExplosion(b.getLocation(), 5);
            }
			if (new Vector(0, 100, 0).toLocation(gameWorld).distance(b.getLocation()) < 50) {
				hp.eggsLanded++;
				hp.eggsLandedTemp++;
				for (SpleggPlayer hp1 : players) {
					hp.mcPlayer.playSound(hp1.mcPlayer.getLocation(), Sound.ITEM_PICKUP, 1, 1);
				}
			}
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
	
	public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlock().getType() == Material.TNT) {
            event.getBlock().setType(Material.AIR);
            TNTPrimed tnt = gameWorld.spawn(event.getBlock().getLocation(), TNTPrimed.class);
        }
	}
	
	public String chatPrefix() {
		return ChatColor.DARK_GRAY + "|" + ChatColor.AQUA + " Splegg " + ChatColor.DARK_GRAY + "|";
	}
}
