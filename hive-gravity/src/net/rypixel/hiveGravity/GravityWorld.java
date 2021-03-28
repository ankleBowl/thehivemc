package net.rypixel.hiveGravity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
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

import net.rypixel.hiveGravity.Functions;

public class GravityWorld {

	public Plugin plugin;
	public int id;
	
	public boolean inGame;
	public boolean starting;
	public boolean canVote;
	public int countdown;
	public int gameClock;
	public int timeRemaining;
	public BukkitTask timer;
	public int highestLevel;
	public int difficultyPicked;
	
	public World[] worlds;
	
	public String[] maps;
	public String[][] difficulties;
	
	public ArrayList<GravityPlayer> players = new ArrayList<GravityPlayer>();
	public ArrayList<String> rankings = new ArrayList<String>();
	public ArrayList<GravityPlayer> finished = new ArrayList<GravityPlayer>();
	public HashMap<GravityPlayer, Integer> finishTimes = new HashMap<GravityPlayer, Integer>();
	public HashMap<GravityPlayer, Integer> votes = new HashMap<GravityPlayer, Integer>();
	
	public World world;
	
	GravityWorld(Plugin plugin, int id) {
		this.plugin = plugin;
		this.id = id;
	}
	
	public void init() {
		world = Functions.createNewWorld(Bukkit.getWorld("gravitymap1"), String.valueOf(id));
		countdown = 1200;
		canVote = true;
		
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
		}
		
		World temp = Bukkit.createWorld(new WorldCreator(maps[0]));
		worlds[0] = Functions.createNewWorld(Bukkit.getWorld(maps[0]), String.valueOf(id));
		Bukkit.unloadWorld(temp, false);
	}
	
	public void update() {
		timer = new BukkitRunnable() {
			public void run() {
				if (!inGame) {
					if (players.size() > 5 && !starting) {
						starting = true;
					} else {
						for (GravityPlayer hp : players) {
							TitleAPI.sendTitle(hp.mcPlayer, 0, 20, 0, "");
							TitleAPI.sendSubtitle(hp.mcPlayer, 0, 20, 0, ChatColor.YELLOW + String.valueOf(6 - players.size()) + " players needed to start...");
						}
					}
					
					if (starting) {
						countdown--;
						
						for (GravityPlayer hp : players) {
							TitleAPI.sendTitle(hp.mcPlayer, 0, 20, 0, "");
							TitleAPI.sendSubtitle(hp.mcPlayer, 0, 20, 0, ChatColor.GREEN + "Starting game in " + String.valueOf(countdown / 20));
						}
						
						if (countdown == 0) {
							initGame();
							cancel();
						}
						
						if (countdown == 300) {
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
							difficultyPicked = index;
							chat(chatPrefix() + ChatColor.DARK_AQUA + " Voting has ended!" + ChatColor.AQUA + " The map " + generateVoteMessage(difficulties[index]) + ChatColor.AQUA + "has won!");
						}
					}
				} else {
					gameClock++;
					timeRemaining++;
					
					rankPlayers();
					for (GravityPlayer hp : players) {	
						String time = DurationFormatUtils.formatDuration(600000 - (timeRemaining * 50), "mm:ss");
						int seconds = gameClock / 20;
					     
						hp.mcPlayer.setLevel(hp.mcPlayer.getLocation().getBlockY());
						
						hp.scoreboard.setTitle(ChatColor.AQUA + "Gra" + ChatColor.GREEN + "vi" + ChatColor.YELLOW + "ty");
						hp.scoreboard.setSlot(13, "");
						hp.scoreboard.setSlot(12, ChatColor.YELLOW + "Time Left");
						hp.scoreboard.setSlot(11, time);
						hp.scoreboard.setSlot(10, "");
						hp.scoreboard.setSlot(9, ChatColor.AQUA + "Ranking");
						hp.scoreboard.setSlot(8, ChatColor.GREEN + "#1 " + ChatColor.BLUE + rankings.get(0));
						hp.scoreboard.setSlot(7, ChatColor.GREEN + "#2 " + ChatColor.BLUE + rankings.get(1));
						hp.scoreboard.setSlot(6, ChatColor.GREEN + "#3 " + ChatColor.BLUE + rankings.get(2));
						hp.scoreboard.setSlot(5, ChatColor.GREEN + "#4 " + ChatColor.BLUE + rankings.get(3));
						hp.scoreboard.setSlot(4, ChatColor.GREEN + "#5 " + ChatColor.BLUE + rankings.get(4));
						hp.scoreboard.setSlot(3, "");
						hp.scoreboard.setSlot(2, ChatColor.DARK_GRAY + "----------------");
						hp.scoreboard.setSlot(1, ChatColor.GOLD + "play." + ChatColor.YELLOW + "HiveMC" + ChatColor.GOLD + ".com");
						
						hp.mapTime++;
						hp.mcPlayer.setFoodLevel(20);
						hp.mcPlayer.setSaturation(20);
						
						if (hp.mcPlayer.getLocation().getBlock().getType() == Material.PORTAL) {
							onPortalUsed(hp);
						}
					}
					
					if (timeRemaining == 600000) {
						gameEnding();
					}
				}
			}
		}.runTaskTimer(plugin, 0L, 1L);
	}
	
	public void rankPlayers() {
		rankings.clear();
		for (GravityPlayer hp : finished) {	
			String time = DurationFormatUtils.formatDuration(finishTimes.get(hp) * 50, "mm:ss");
			rankings.add(hp.mcPlayer.getDisplayName() + ChatColor.WHITE + " " + time);
		}
		for (GravityPlayer hp : players) {	
			if (hp.level == 4 && !hp.finished) {
				rankings.add(hp.mcPlayer.getDisplayName());
			}
		}
		for (GravityPlayer hp : players) {	
			if (hp.level == 3 && !hp.finished) {
				rankings.add(hp.mcPlayer.getDisplayName());
			}
		}
		for (GravityPlayer hp : players) {	
			if (hp.level == 2 && !hp.finished) {
				rankings.add(hp.mcPlayer.getDisplayName());
			}
		}
		for (GravityPlayer hp : players) {	
			if (hp.level == 1 && !hp.finished) {
				rankings.add(hp.mcPlayer.getDisplayName());
			}
		}
		for (GravityPlayer hp : players) {	
			if (hp.level == 0 && !hp.finished) {
				rankings.add(hp.mcPlayer.getDisplayName());
			}
		}
		for (int i = 0; i < 4; i++) {
			rankings.add("N/A");
		}
	}
	
	public void welcomePlayer(GravityPlayer hp) {
		hp.mcPlayer.teleport(new Vector(0.5, 172, 0.5).toLocation(world));
		hp.serverId = id;
		hp.mcPlayer.setAllowFlight(false);
		hp.mcPlayer.removePotionEffect(PotionEffectType.INVISIBILITY);
		hp.mcPlayer.setGameMode(GameMode.ADVENTURE);
		
		chat(ChatColor.BLUE + hp.mcPlayer.getDisplayName() + ChatColor.GRAY + " wants to fall!");
		
		int[] votes = tallyVotes();
		
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.YELLOW + " Vote for a map! " + ChatColor.GRAY + "Use " + ChatColor.WHITE + "/v #" + ChatColor.WHITE + " or click.");
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.GRAY + " 1. " + generateVoteMessage(difficulties[0]) + ChatColor.GRAY + " [" + ChatColor.WHITE + votes[0] + ChatColor.GRAY + " Votes]");
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.GRAY + " 2. " + generateVoteMessage(difficulties[1]) + ChatColor.GRAY + " [" + ChatColor.WHITE + votes[1] + ChatColor.GRAY + " Votes]");
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.GRAY + " 3. " + generateVoteMessage(difficulties[2]) + ChatColor.GRAY + " [" + ChatColor.WHITE + votes[2] + ChatColor.GRAY + " Votes]");
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.GRAY + " 4. " + generateVoteMessage(difficulties[3]) + ChatColor.GRAY + " [" + ChatColor.WHITE + votes[3] + ChatColor.GRAY + " Votes]");
		hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.GRAY + " 5. " + generateVoteMessage(difficulties[4]) + ChatColor.GRAY + " [" + ChatColor.WHITE + votes[4] + ChatColor.GRAY + " Votes]");
		
		players.add(hp);
	}
	
	public void initGame() {
		chooseMaps(difficultyPicked);
		inGame = true;
		for (GravityPlayer hp : players) {
			hp.level = 0;
			hp.finished = false;
			hp.mcPlayer.teleport(Constants.spawnLocations.get(maps[0]).toLocation(worlds[0]));
		}
		
		Constants.spawnLocations.get(maps[0]).toBlockVector().add(new Vector(1, 0, 0)).toLocation(worlds[0]).getBlock().setType(Material.BARRIER);
		Constants.spawnLocations.get(maps[0]).toBlockVector().add(new Vector(-1, 0, 0)).toLocation(worlds[0]).getBlock().setType(Material.BARRIER);
		Constants.spawnLocations.get(maps[0]).toBlockVector().add(new Vector(0, 0, 1)).toLocation(worlds[0]).getBlock().setType(Material.BARRIER);
		Constants.spawnLocations.get(maps[0]).toBlockVector().add(new Vector(0, 0, -1)).toLocation(worlds[0]).getBlock().setType(Material.BARRIER);
		Constants.spawnLocations.get(maps[0]).toBlockVector().add(new Vector(0, 2, 0)).toLocation(worlds[0]).getBlock().setType(Material.BARRIER);
		
		for (GravityPlayer hp : players) {
			TitleAPI.sendTitle(hp.mcPlayer, 0, 21, 0, ChatColor.RED + "⑤");
			TitleAPI.sendSubtitle(hp.mcPlayer, 0, 21, 0, ChatColor.GRAY + "until start");
			hp.mcPlayer.setLevel(5);
		}
		
		new BukkitRunnable() {
			public void run() {
				for (GravityPlayer hp : players) {
					TitleAPI.sendTitle(hp.mcPlayer, 0, 21, 0, ChatColor.RED + "④");
					TitleAPI.sendSubtitle(hp.mcPlayer, 0, 21, 0, ChatColor.GRAY + "until start");
					hp.mcPlayer.setLevel(4);
				}
		    }
		}.runTaskLater(plugin, 20L);
		
		new BukkitRunnable() {
			public void run() {
				for (GravityPlayer hp : players) {
					TitleAPI.sendTitle(hp.mcPlayer, 0, 21, 0, ChatColor.GOLD + "③");
					TitleAPI.sendSubtitle(hp.mcPlayer, 0, 21, 0, ChatColor.GRAY + "until start");
					hp.mcPlayer.setLevel(3);
				}
		    }
		}.runTaskLater(plugin, 40L);
		
		new BukkitRunnable() {
			public void run() {
				for (GravityPlayer hp : players) {
					TitleAPI.sendTitle(hp.mcPlayer, 0, 21, 0, ChatColor.GOLD + "②");
					TitleAPI.sendSubtitle(hp.mcPlayer, 0, 21, 0, ChatColor.GRAY + "until start");
					hp.mcPlayer.setLevel(2);
				}
		    }
		}.runTaskLater(plugin, 60L);
		
		new BukkitRunnable() {
			public void run() {
				for (GravityPlayer hp : players) {
					TitleAPI.sendTitle(hp.mcPlayer, 0, 21, 0, ChatColor.YELLOW + "①");
					TitleAPI.sendSubtitle(hp.mcPlayer, 0, 21, 0, ChatColor.GRAY + "until start");
					hp.mcPlayer.setLevel(1);
				}
		    }
		}.runTaskLater(plugin, 80L);
		
		new BukkitRunnable() {
			public void run() {
				for (GravityPlayer hp : players) {
					TitleAPI.sendTitle(hp.mcPlayer, 0, 21, 0, "Stage 1");
					TitleAPI.sendSubtitle(hp.mcPlayer, 0, 21, 0, maps[0].replace('_', ' ') + ChatColor.DARK_GRAY + " - " + difficultyText(0));
				}
				chat("");
				chat(chatPrefix() + ChatColor.GRAY + " Map " + ChatColor.DARK_GRAY + ">>" + ChatColor.AQUA + " " + maps[0]);
				chat(chatPrefix() + ChatColor.GRAY + " Creator " + ChatColor.DARK_GRAY + ">>" + ChatColor.AQUA + " " + "N/A");
				chat(chatPrefix() + ChatColor.GRAY + " Difficulty " + ChatColor.DARK_GRAY + ">>" + " " + difficultyText(0));
				chat(chatPrefix() + ChatColor.GRAY + " Best Time " + ChatColor.DARK_GRAY + ">>" + ChatColor.AQUA + " " + "N/A");
				chat("");
				
				Constants.spawnLocations.get(maps[0]).toBlockVector().add(new Vector(1, 0, 0)).toLocation(worlds[0]).getBlock().setType(Material.AIR);
				Constants.spawnLocations.get(maps[0]).toBlockVector().add(new Vector(-1, 0, 0)).toLocation(worlds[0]).getBlock().setType(Material.AIR);
				Constants.spawnLocations.get(maps[0]).toBlockVector().add(new Vector(0, 0, 1)).toLocation(worlds[0]).getBlock().setType(Material.AIR);
				Constants.spawnLocations.get(maps[0]).toBlockVector().add(new Vector(0, 0, -1)).toLocation(worlds[0]).getBlock().setType(Material.AIR);
				Constants.spawnLocations.get(maps[0]).toBlockVector().add(new Vector(0, 2, 0)).toLocation(worlds[0]).getBlock().setType(Material.AIR);
			}
		}.runTaskLater(plugin, 100L);
	}
	
	public void onPlayerLeave(GravityPlayer hp) {
		players.remove(hp);
	}
	
	public void onInteract(PlayerInteractEvent event) {
		GravityPlayer hp = Main.playerMap.get(event.getPlayer());
		if (!inGame) {
			if (event.getItem() != null) {
				switch (event.getItem().getType()) {
				case DIAMOND:
					voteInv();
					break;
				case SLIME_BALL:
					Functions.sendToServer(hp.mcPlayer, "lobby0", plugin);
					break;
				default:
					break;
				}
			}
		} else {
			if (event.getItem() != null) {
				switch (event.getItem().getType()) {
				case SLIME_BALL:
					Functions.sendToServer(hp.mcPlayer, "lobby0", plugin);
					break;
				default:
					break;
				}
			}
		}
	}
	
	public void onInventoryClick(InventoryClickEvent event) {
		GravityPlayer hp = Main.playerMap.get(event.getWhoClicked());
		if (event.getCurrentItem() != null) {
			if (inGame) {
				
			} else {
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
				default:
					break;
				}
			}
		}
	}
	
	public void onEntityDamage(EntityDamageEvent event) {
		event.setCancelled(true);
		if (event.getEntity() instanceof Player) {
			GravityPlayer hp = Main.playerMap.get(event.getEntity());
			if (event.getCause() == DamageCause.FALL && event.getFinalDamage() > 10) {
				hp.mcPlayer.teleport(Constants.spawnLocations.get(maps[hp.level]).toLocation(hp.mcPlayer.getWorld()));
				hp.fails++;
			}
		}
	}
	
	public void onPortalUsed(GravityPlayer hp) {
		hp.level++;
		if (hp.level > highestLevel && hp.level < 5) {
			World temp = Bukkit.createWorld(new WorldCreator(maps[hp.level]));
			worlds[hp.level] = Functions.createNewWorld(Bukkit.getWorld(maps[hp.level]), String.valueOf(id));
			Bukkit.unloadWorld(temp, false);
			highestLevel++;
		}
		
		if (hp.level < 5) {
			hp.mcPlayer.teleport(Constants.spawnLocations.get(maps[hp.level]).toLocation(worlds[hp.level]));
			if (!hp.finished) {
				TitleAPI.sendTitle(hp.mcPlayer, 20, 20, 20, "Stage " + String.valueOf(hp.level + 1));
				TitleAPI.sendSubtitle(hp.mcPlayer, 20, 20, 20, maps[hp.level].replace('_', ' ') + ChatColor.DARK_GRAY + " - " + difficultyText(hp.level));
				
				hp.mcPlayer.sendMessage("");
				hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.GRAY + " Map " + ChatColor.DARK_GRAY + ">>" + ChatColor.AQUA + " " + maps[hp.level]);
				hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.GRAY + " Creator " + ChatColor.DARK_GRAY + ">>" + ChatColor.AQUA + " " + " N/A");
				hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.GRAY + " Difficulty " + ChatColor.DARK_GRAY + ">>" + " " + difficultyText(hp.level));
				hp.mcPlayer.sendMessage(chatPrefix() + ChatColor.GRAY + " Best Time " + ChatColor.DARK_GRAY + ">>" + ChatColor.AQUA + " " + "N/A");
				hp.mcPlayer.sendMessage("");
				
				chat(chatPrefix() + ChatColor.BLUE + " " + hp.mcPlayer.getDisplayName() + ChatColor.GREEN + " finished " + ChatColor.AQUA + "Stage " + String.valueOf(hp.level) + ChatColor.GREEN + " in " + ChatColor.LIGHT_PURPLE + String.valueOf(hp.mapTime / 20) + " seconds");
				
				hp.mapTime = 0;
			}
		} else {
			hp.mcPlayer.teleport(Constants.spawnLocations.get(maps[0]).toLocation(worlds[0]));
			hp.level = 0;
			if (!hp.finished) {
				hp.finished = true;
				hp.mcPlayer.setAllowFlight(true);
				hp.mcPlayer.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999, 1, false, true));
				finishTimes.put(hp, gameClock);
				if (timeRemaining < 48000) {
					timeRemaining = 48000;
				}
				if (finished.size() == 0) {
					chat(chatPrefix() + " " + ChatColor.BLUE + hp.mcPlayer.getDisplayName() + ChatColor.GREEN + " just finished 1st! The game will end in 2 minutes");
				} else if (finished.size() == 1) {
					chat(chatPrefix() + " " + ChatColor.BLUE + hp.mcPlayer.getDisplayName() + ChatColor.GREEN + " just finished 2nd!");
				} else if (finished.size() == 2) {
					chat(chatPrefix() + " " + ChatColor.BLUE + hp.mcPlayer.getDisplayName() + ChatColor.GREEN + " just finished 3rd!");
				} else {
					chat(chatPrefix() + " " + ChatColor.BLUE + hp.mcPlayer.getDisplayName() + ChatColor.GREEN + " just finished " + String.valueOf(finished.size()) + "th!");
				}
				finished.add(hp);
				hp.mcPlayer.sendMessage(ChatColor.GREEN + "You finished! " + ChatColor.GRAY + "You are now spectating...");
				
				if (finished.size() == players.size()) {
					gameEnding();
				}
			}
		}
	}
	
	public void onPlayerMove(PlayerMoveEvent event) {
//		if (gameClock < 100 && inGame) {
//			event.setCancelled(true);
//		}
	}
	
	public void chat(String message) {
		for (GravityPlayer hp : players) {
			hp.mcPlayer.sendMessage(message);
		}
	}
	
	public int[] tallyVotes() {
		int[] voteTotal = new int[5];
		voteTotal[0] = 0;
		voteTotal[1] = 0;
		voteTotal[2] = 0;
		voteTotal[3] = 0;
		voteTotal[4] = 0;
		for (Map.Entry<GravityPlayer, Integer> set : votes.entrySet()) {
			voteTotal[set.getValue()]++;
		}
		return voteTotal;
	}
	
	public Inventory voteInv() {
		Inventory inv = Bukkit.createInventory(null, 45, "Vote for an Option");
	
		int[] voteArray = tallyVotes();
		
		for (int i = 0; i < 5; i++) {
			ItemStack map = new ItemStack(Material.MAP, 1);
			ItemMeta meta = map.getItemMeta();
			meta.setDisplayName(String.valueOf(i));
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
	
	public String generateVoteMessage(String[] difficulties) {
		String message = "";
		String[] numbers = new String[5];
		numbers[0] = "①";
		numbers[1] = "②";
		numbers[2] = "③";
		numbers[3] = "④";
		numbers[4] = "⑤";
		int index = 0;
		for (String s : difficulties) {
			switch (s) {
			case "Easy":
				message += ChatColor.GREEN + numbers[index];
				break;
			case "Normal":
				message += ChatColor.YELLOW + numbers[index];
				break;
			case "Hard":
				message += ChatColor.RED + numbers[index];
				break;
			}
			index++;
		}
		return message;
	}
	
	public String difficultyText(int roundIndex) {
		String message = "";
		switch (difficulties[difficultyPicked][roundIndex]) {
		case "Easy":
			message += ChatColor.GREEN + "Easy";
			break;
		case "Normal":
			message += ChatColor.YELLOW + "Medium";
			break;
		case "Hard":
			message += ChatColor.RED + "Hard";
			break;
		}
		return message;
	}
	
	public void gameEnding() {
		for (GravityPlayer hp : players) {
			hp.mcPlayer.teleport(Constants.spawnLocations.get(maps[0]).toLocation(worlds[0]));
			TitleAPI.sendTitle(hp.mcPlayer, 20, 20, 20, ChatColor.RED + "Game. OVER!");
			TitleAPI.sendSubtitle(hp.mcPlayer, 20, 20, 20, ChatColor.BLUE + hp.mcPlayer.getDisplayName() + ChatColor.GRAY + " won the game");
		}
		new BukkitRunnable() {
			public void run() {
				for (GravityPlayer hp : players) {
					hp.mcPlayer.teleport(new Vector(0.5, 172, 0.5).toLocation(world));
				}
		    }
		}.runTaskLater(plugin, 60L);
		
		new BukkitRunnable() {
			public void run() {
				for (GravityPlayer hp : players) {
					hp.mcPlayer.sendMessage("");
					hp.mcPlayer.sendMessage(ChatColor.RED + "Game. OVER! " + ChatColor.BLUE + finished.get(0).mcPlayer.getDisplayName() + ChatColor.GRAY + " won the game!");
					hp.mcPlayer.sendMessage("");
					hp.mcPlayer.sendMessage(ChatColor.GRAY + "You played " + getMatchPlayedStr(hp) + ChatColor.GRAY + " stages and failed " + ChatColor.RED + String.valueOf(hp.fails) + ChatColor.GRAY + " times.");
					if (hp.finished) {
						if (finished.indexOf(hp) == 0) {
							hp.mcPlayer.sendMessage(ChatColor.GRAY + "You placed " + ChatColor.YELLOW + String.valueOf(finished.indexOf(hp)) + "st" + ChatColor.GRAY + " with a time of " + "time");
						} else if (finished.indexOf(hp) == 1) {
							hp.mcPlayer.sendMessage(ChatColor.GRAY + "You placed " + ChatColor.YELLOW + String.valueOf(finished.indexOf(hp)) + "nd" + ChatColor.GRAY + " with a time of " + "time");
						} else if (finished.indexOf(hp) == 2) {
							hp.mcPlayer.sendMessage(ChatColor.GRAY + "You placed " + ChatColor.YELLOW + String.valueOf(finished.indexOf(hp)) + "rd" + ChatColor.GRAY + " with a time of " + "time");
						} else {
							hp.mcPlayer.sendMessage(ChatColor.GRAY + "You placed " + ChatColor.YELLOW + String.valueOf(finished.indexOf(hp)) + "th" + ChatColor.GRAY + " with a time of " + "time");
						}
					}
				}
		    }
		}.runTaskLater(plugin, 100L);
	}
	
	public String getMatchPlayedStr(GravityPlayer hp) {
		String string = "";
		for (int i = 0; i < 5; i++) {
			ChatColor color = null;
			if (hp.level >= i) {
				color = ChatColor.GREEN;
			} else {
				color = ChatColor.RED;
			}
			
			switch (i) {
			case 0:
				string += color + "①";
				break;
			case 1:
				string += color + "②";
				break;
			case 2:
				string += color + "③";
				break;
			case 3:
				string += color + "④";
				break;
			case 4:
				string += color + "⑤";
				break;
			}
		}
		return string;
	}
	
	public String chatPrefix() {
		return ChatColor.DARK_GRAY + "| " + ChatColor.AQUA + "Gra" + ChatColor.GREEN + "vi" + ChatColor.YELLOW + "ty" + ChatColor.DARK_GRAY + " |";
	}
}
