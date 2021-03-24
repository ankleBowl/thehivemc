package net.rypixel.hiveBlockparty;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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
	public boolean ending;
	public int countdown;
	
	public int titleTimer;
	
	public int level;
	
	public ArrayList<BlockpartyPlayer> players = new ArrayList<BlockpartyPlayer>();
	
	public HashMap<DyeColor, DyeColor> colorMap = new HashMap<DyeColor, DyeColor>();
	public ArrayList<DyeColor> colorsUsed = new ArrayList<DyeColor>();
	public DyeColor colorToRemove;
	
	BlockpartyWorld(Plugin plugin, int id) {
		this.plugin = plugin;
		this.id = id;
	}
	
	public void init() {
		world = Functions.createNewWorld(Bukkit.getWorld("bpmap1"), String.valueOf(id));
		countdown = 1200;
		update();
	}

	public void onPlayerLeave(Player p) {
		BlockpartyPlayer hp = Main.playerMap.get(p);
		players.remove(hp);
		if (!ending && !starting) {
			chat(chatPrefix() + ChatColor.BLUE + " " + hp.mcPlayer.getDisplayName() + ChatColor.DARK_GRAY + " -> " + ChatColor.RED + "ELIMINATED!");
			world.spawnEntity(hp.mcPlayer.getLocation(), EntityType.LIGHTNING);
		}
	}
	
	public void onInteract(PlayerInteractEvent event) {
		BlockpartyPlayer hp = Main.playerMap.get(event.getPlayer());
		if (inGame) {
			if (event.getItem() != null) {
				switch (event.getItem().getType()) {
				case COMPASS:
					break;
				case MINECART:
					players.remove(hp);
					boolean sent = false;
					for (BlockpartyWorld world : Main.worlds) {
						if (!sent && world != this) {
							if (world.players.size() < 10) {
								world.welcomePlayer(hp);
								sent = true;
							}
						}
					}
					
					if (!sent) {
						int id = Functions.getLowestWorldID(Main.worlds);
						BlockpartyWorld w = new BlockpartyWorld(plugin, id);
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

	}
	
	public void welcomePlayer(BlockpartyPlayer hp) {
		players.add(hp);
		hp.mcPlayer.teleport(new Vector(34.5, 3, 8).toLocation(world));
		hp.serverId = id;
		hp.mcPlayer.setGameMode(GameMode.ADVENTURE);
		hp.mcPlayer.removePotionEffect(PotionEffectType.INVISIBILITY);
		hp.mcPlayer.setAllowFlight(false);
		
		hp.mcPlayer.setFoodLevel(20);
		hp.mcPlayer.setSaturation(20);
		
		Inventory inv = hp.mcPlayer.getInventory();
		inv.clear();
		inv.setItem(0, Constants.rules);
		inv.setItem(1, Constants.vote);
		inv.setItem(4, Constants.locker);
		inv.setItem(8, Constants.hub);
		
		chat(ChatColor.BLUE + hp.mcPlayer.getDisplayName() + ChatColor.GRAY + " is ready to dance!");
	}
	
	public void chat(String message) {
		for (BlockpartyPlayer hp : players) {
			hp.mcPlayer.sendMessage(message);
		}
	}
	
	public void title(String message, String subtitle) {
		for (BlockpartyPlayer hp : players) {
			TitleAPI.sendSubtitle(hp.mcPlayer, 20, 20, 20, subtitle);
			TitleAPI.sendTitle(hp.mcPlayer, 20, 20, 20, message);
		}
	}
	
	public void update() {
		timer = new BukkitRunnable() {
			public void run() {
				if (!inGame) {
					
					for (BlockpartyPlayer hp : players) {
						hp.scoreboard.setTitle(ChatColor.GOLD + "Hive" + ChatColor.YELLOW + "MC");
						hp.scoreboard.setSlot(15, "");
						hp.scoreboard.setSlot(14, ChatColor.GREEN + "Tokens");
						hp.scoreboard.setSlot(13, ChatColor.GRAY + String.valueOf(hp.tokens));
						hp.scoreboard.setSlot(12, "");
						hp.scoreboard.setSlot(11, ChatColor.YELLOW + "Your Stats");
						hp.scoreboard.setSlot(10, ChatColor.DARK_AQUA + "Points: " + ChatColor.AQUA + String.valueOf(hp.points));
						hp.scoreboard.setSlot(9, ChatColor.DARK_AQUA + "Games Played: " + ChatColor.AQUA + String.valueOf(hp.playedGames));
						hp.scoreboard.setSlot(8, ChatColor.DARK_AQUA + "Victories: " + ChatColor.AQUA + String.valueOf(hp.wonGames));
						hp.scoreboard.setSlot(7, ChatColor.DARK_AQUA + "Win Streak: " + ChatColor.AQUA + String.valueOf(hp.winstreak));
						hp.scoreboard.setSlot(6, ChatColor.DARK_AQUA + "Hardcore Points: " + ChatColor.AQUA + String.valueOf(hp.hardcorePoints));
						hp.scoreboard.setSlot(5, ChatColor.DARK_AQUA + "Hardcore Victories: " + ChatColor.AQUA + String.valueOf(hp.hardcoreWins));
						hp.scoreboard.setSlot(4, ChatColor.DARK_AQUA + "Placings: " + ChatColor.AQUA + String.valueOf(hp.placings));
						hp.scoreboard.setSlot(3, "");
						hp.scoreboard.setSlot(2, ChatColor.DARK_GRAY + "----------------");
						hp.scoreboard.setSlot(1, ChatColor.GOLD + "play." + ChatColor.YELLOW + "HiveMC" + ChatColor.GOLD + ".com");
						
					}
					
					if (players.size() > 5 && !starting) {
						starting = true;
					} else {
						for (BlockpartyPlayer hp : players) {
							TitleAPI.sendTitle(hp.mcPlayer, 0, 20, 0, "");
							TitleAPI.sendSubtitle(hp.mcPlayer, 0, 20, 0, ChatColor.YELLOW + String.valueOf(6 - players.size()) + " players needed to start...");
						}
					}
					
					if (starting) {
						countdown--;
						
						for (BlockpartyPlayer hp : players) {
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
	
	public void initGame() {
		for (BlockpartyPlayer hp : players) {
			hp.mcPlayer.teleport(new Vector(0, 1, 0).toLocation(world));
			hp.isDead = false;
			hp.mcPlayer.getInventory().clear();
			hp.playedGames++;
		}
		inGame = true;
		titleTimer = 10;
		
		opening();
		cycle(Constants.roundSpeed[level], 50);
	}
	
	public void cycle(int runTime, int emptyTime) {		
		
		//Set new floor
		loadFloor();
		
		ArrayList<BlockpartyPlayer> dead = new ArrayList<BlockpartyPlayer>();
		for (BlockpartyPlayer hp : players) {
			hp.mcPlayer.setFoodLevel(20);
			hp.mcPlayer.setSaturation(20);
			hp.mcPlayer.getInventory().setItem(4, null);
			
			TitleAPI.sendSubtitle(hp.mcPlayer, 0, 61, 0, ChatColor.AQUA + "Waiting...");
			TitleAPI.sendTitle(hp.mcPlayer, 0, 61, 0, "");
			
			if (!hp.isDead && hp.mcPlayer.getLocation().getY() < 0) {
				hp.isDead = true;
				hp.mcPlayer.teleport(new Vector(0, 10, 0).toLocation(world));
				hp.mcPlayer.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999, 1, false, true));
				hp.mcPlayer.setAllowFlight(true);
				hp.mcPlayer.setFlying(true);
				TitleAPI.sendTitle(hp.mcPlayer, 20, 20, 20, ChatColor.RED + "YOU DIED!");
				hp.mcPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 1, false, true));
				hp.mcPlayer.getInventory().setItem(7, Constants.again);
				hp.mcPlayer.getInventory().setItem(0, Constants.players);
				hp.mcPlayer.getInventory().setItem(8, Constants.hub);
				chat(chatPrefix() + ChatColor.BLUE + " " + hp.mcPlayer.getDisplayName() + ChatColor.DARK_GRAY + " -> " + ChatColor.RED + "ELIMINATED!");
				world.spawnEntity(hp.mcPlayer.getLocation(), EntityType.LIGHTNING);
				hp.winstreak = 0;
				dead.add(hp);
			}
		}
		
		if (level != 0) {
			chat(chatPrefix() + ChatColor.GREEN + " ✚" + ChatColor.AQUA + " 1 point");
		}

		ArrayList<BlockpartyPlayer> possibleWinner = new ArrayList<BlockpartyPlayer>();
		for (BlockpartyPlayer hp : players) {
			if (!hp.isDead) {
				possibleWinner.add(hp);
				if (level != 0) {
					hp.tempPoints++;
					hp.points++;
				}
			}
		}
		
		for (BlockpartyPlayer hp : players) {
			hp.scoreboard.setTitle(ChatColor.YELLOW + "BlockParty");
			hp.scoreboard.setSlot(14, "");
			hp.scoreboard.setSlot(13, ChatColor.RED + "Dancers Left");
			hp.scoreboard.setSlot(12, String.valueOf(possibleWinner.size()) + ChatColor.GRAY + " Dancers");
			hp.scoreboard.setSlot(11, "");
			hp.scoreboard.setSlot(10, ChatColor.AQUA + "Game Info");
			hp.scoreboard.setSlot(9, ChatColor.GRAY + "Round: " + ChatColor.WHITE + String.valueOf(level + 1));
			hp.scoreboard.setSlot(8, ChatColor.GRAY + "Round Speed: " + ChatColor.WHITE + String.valueOf((double) Constants.roundSpeed[level] / 20) + "s");
			hp.scoreboard.setSlot(7, "");
			hp.scoreboard.setSlot(6, ChatColor.GREEN + "Your Stats");
			hp.scoreboard.setSlot(5, ChatColor.GRAY + "Points: " + ChatColor.WHITE + String.valueOf(hp.tempPoints));
			hp.scoreboard.setSlot(4, ChatColor.GRAY + "Powerups: " + ChatColor.WHITE + "N/A");
			hp.scoreboard.setSlot(3, "");
			hp.scoreboard.setSlot(2, ChatColor.DARK_GRAY + "----------------");
			hp.scoreboard.setSlot(1, ChatColor.GOLD + "play." + ChatColor.YELLOW + "HiveMC" + ChatColor.GOLD + ".com");
		}
		if (possibleWinner.size() < 3) {
			for (BlockpartyPlayer hp : dead) {
				hp.placings++;
			}
		}
		
		if (possibleWinner.size() < 2) {
			if (possibleWinner.size() == 0) {
				gameEnding(dead);
			} else {
				gameEnding(possibleWinner);
			}
		} else {
			new BukkitRunnable() {
				public void run() {
					Random random = new Random();
					colorToRemove = colorsUsed.get(random.nextInt(colorsUsed.size()));
					for (BlockpartyPlayer hp : players) {
						if (!hp.isDead) {
							hp.mcPlayer.getInventory().setItem(4, Constants.blockpartyBlock(colorToRemove));
							countDown(runTime);
						}
					}
				}
			}.runTaskLater(plugin, 60);
			
			new BukkitRunnable() {
				public void run() {
					//remove the floor
					removeFloor(colorToRemove);
					for (BlockpartyPlayer hp : players) {
						TitleAPI.sendSubtitle(hp.mcPlayer, 0, 51, 0, ChatColor.RED + "✖ " + ChatColor.WHITE + "STOP" + ChatColor.RED + " ✖");
						TitleAPI.sendTitle(hp.mcPlayer, 0, 51, 0, "");
					}
				}
			}.runTaskLater(plugin, runTime + 60);
			
			new BukkitRunnable() {
				public void run() {
					//when the players wait and the floor is empty
					if (level < 20) {
						level++;
						cycle(Constants.roundSpeed[level], 50);
					} else {
						stop();
					}
				}
			}.runTaskLater(plugin, runTime + emptyTime + 60);
		}
	}
	
	public void countDown(int ticks) {
		new BukkitRunnable() {
			public void run() {
				if (ticks > 9) {
					String message = Constants.colorToChat.get(colorToRemove) + "";
					for (int i = 0; i < ticks / 10; i++) {
						message += "■";
					}
					message += " " + Constants.colorToName.get(colorToRemove) + " ";
					for (int i = 0; i < ticks / 10; i++) {
						message += "■";
					}
					for (BlockpartyPlayer hp : players) {
						TitleAPI.sendSubtitle(hp.mcPlayer, 0, 11, 0, message);
						TitleAPI.sendTitle(hp.mcPlayer, 0, 11, 0, "");
					}
					countDown(ticks - 10);
				}
			}
		}.runTaskLater(plugin, 10);
	}
	
	public void gameEnding(ArrayList<BlockpartyPlayer> winners) {
		ending = true;
		if (winners.size() > 2) {
			title(ChatColor.BLUE + "Multiple players have won!", "");
		} else if (winners.size() == 1) {
			title(ChatColor.BLUE + winners.get(0).mcPlayer.getDisplayName() + " has won!", "");
		} else {
			title(ChatColor.BLUE + winners.get(0).mcPlayer.getDisplayName() + " and " +  winners.get(1).mcPlayer.getDisplayName() + " have won!", "");
		}
		
		for (BlockpartyPlayer hp : winners ) {
			hp.wonGames++;
			hp.winstreak++;
		}
		
		for (BlockpartyPlayer hp : players) {
			hp.mcPlayer.teleport(new Vector(-61.5, 7, 6.5).toLocation(world));
			
			Inventory inv = hp.mcPlayer.getInventory();
			inv.clear();
			inv.setItem(7, Constants.again);
			inv.setItem(0, Constants.players);
			inv.setItem(8, Constants.hub);
			
			hp.mcPlayer.setGameMode(GameMode.ADVENTURE);
			hp.mcPlayer.removePotionEffect(PotionEffectType.INVISIBILITY);
			hp.mcPlayer.setAllowFlight(false);
			
			hp.mcPlayer.setFoodLevel(20);
			hp.mcPlayer.setSaturation(20);
		}
		
		new BukkitRunnable() {
			public void run() {
				stop();
			}
		}.runTaskLater(plugin, 200L);
	}
	
	public void opening() {
		new BukkitRunnable() {
			public void run() {
				titleTimer--;
				String message = generateTitle(titleTimer);
				for (BlockpartyPlayer hp : players) {
					TitleAPI.sendTitle(hp.mcPlayer, 0, 20, 20, message);
					TitleAPI.sendSubtitle(hp.mcPlayer, 0, 20, 20, "Time to DANCE!");
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
				if (c.getData() != colorToRemove.getData()) {
					c.setType(Material.AIR);
				}
			}
		}
	}
	
	public void loadFloor() {
		int map = 0;
		updateHashmap();
		if (level != 0) {
			Random random = new Random();
			map = random.nextInt(9) + 1;
		}
		
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
		if (level != 0) {
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
		} else {
			ArrayList<DyeColor> colors = Constants.colors();
			for (DyeColor c : colors) {
				colorMap.put(c, c);
			}
		}
	}
		
	public void stop() {
		for (BlockpartyPlayer hp : players) {
			hp.mcPlayer.setLevel(0);
			
			boolean sent = false;
			for (BlockpartyWorld world : Main.worlds) {
				if (!sent && world != this) {
					if (world.players.size() < 10) {
						world.welcomePlayer(hp);
						sent = true;
					}
				}
			}
			
			if (!sent) {
				int id = Functions.getLowestWorldID(Main.worlds);
				BlockpartyWorld w = new BlockpartyWorld(plugin, id);
				w.init();
				Main.worlds.add(w);
				
				w.welcomePlayer(hp);
				sent = true;
			}
		}
		
		level = 22;
		
		File folder = world.getWorldFolder();
		Bukkit.unloadWorld(world, false);
		try {
			FileUtils.deleteDirectory(folder);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Main.worlds.remove(this);
	}
	
	public String chatPrefix() {
		return ChatColor.DARK_GRAY + "| " + ChatColor.AQUA + "B" + ChatColor.GREEN + "l" + ChatColor.YELLOW + "o" + ChatColor.GOLD + "c" + ChatColor.RED + "k" + ChatColor.AQUA + "Party" + ChatColor.DARK_GRAY + " |";
	}
}
