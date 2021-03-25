package net.rypixel.hiveGravity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;
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

import net.rypixel.hiveGravity.Functions;

public class GravityWorld {

	public Plugin plugin;
	public int id;
	
	public boolean inGame;
	public boolean starting;
	public boolean canVote;
	public int countdown;
	public int gameClock;
	public BukkitTask timer;
	public int highestLevel;
	public int difficultyPicked;
	
	public World[] worlds;
	
	public String[] maps;
	public String[][] difficulties;
	
	public ArrayList<GravityPlayer> players = new ArrayList<GravityPlayer>();
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

				}
			}
		}.runTaskTimer(plugin, 0L, 1L);
	}
	
	public void welcomePlayer(GravityPlayer hp) {
		hp.mcPlayer.teleport(new Vector(0.5, 172, 0.5).toLocation(world));
		hp.serverId = id;
		hp.mcPlayer.setAllowFlight(true);
		hp.mcPlayer.removePotionEffect(PotionEffectType.INVISIBILITY);
		
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
			hp.mcPlayer.teleport(new Vector(1.5, 242, 12.5).toLocation(worlds[0]));
		}
		
		for (GravityPlayer hp : players) {
			TitleAPI.sendTitle(hp.mcPlayer, 0, 21, 0, ChatColor.GOLD + "⑤");
			TitleAPI.sendSubtitle(hp.mcPlayer, 0, 21, 0, ChatColor.GRAY + "until start");
			hp.mcPlayer.setLevel(5);
		}
		
		new BukkitRunnable() {
			public void run() {
				for (GravityPlayer hp : players) {
					TitleAPI.sendTitle(hp.mcPlayer, 0, 21, 0, ChatColor.GOLD + "④");
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
					TitleAPI.sendTitle(hp.mcPlayer, 0, 21, 0, ChatColor.GOLD + "①");
					TitleAPI.sendSubtitle(hp.mcPlayer, 0, 21, 0, ChatColor.GRAY + "until start");
					hp.mcPlayer.setLevel(1);
				}
		    }
		}.runTaskLater(plugin, 80L);
	}
	
	public void onPlayerLeave(GravityPlayer hp) {
		
	}
	
	public void onInteract(PlayerInteractEvent event) {
		
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
			if (event.getCause() == DamageCause.FALL) {
				hp.mcPlayer.teleport(Constants.spawnLocations.get(maps[hp.level]).toLocation(worlds[hp.level]));
			}
		}
	}
	
	public void onPortalUsed(PlayerPortalEvent event) {
		GravityPlayer hp = Main.playerMap.get(event.getPlayer());
		hp.level++;
		if (hp.level > highestLevel && hp.level < 5) {
			World temp = Bukkit.createWorld(new WorldCreator(maps[hp.level]));
			worlds[hp.level] = Functions.createNewWorld(Bukkit.getWorld(maps[hp.level]), String.valueOf(id));
			Bukkit.unloadWorld(temp, false);
			highestLevel++;
		}
		
		if (hp.level < 5) {
			hp.mcPlayer.teleport(Constants.spawnLocations.get(maps[hp.level]).toLocation(worlds[hp.level]));
		} else {
			hp.mcPlayer.teleport(Constants.spawnLocations.get(maps[hp.level]).toLocation(worlds[hp.level]));
			hp.level = 0;
			hp.finished = true;
			hp.mcPlayer.setAllowFlight(true);
			hp.mcPlayer.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999, 1, false, true));
			finishTimes.put(hp, gameClock);
			
		}
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
	
	public String chatPrefix() {
		return ChatColor.DARK_GRAY + "| " + ChatColor.AQUA + "Gra" + ChatColor.GREEN + "vi" + ChatColor.YELLOW + "ty" + ChatColor.DARK_GRAY + " |";
	}
}
