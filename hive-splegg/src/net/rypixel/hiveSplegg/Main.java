package net.rypixel.hiveSplegg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import me.vagdedes.mysql.basic.Config;
import me.vagdedes.mysql.database.MySQL;
import me.vagdedes.mysql.database.SQL;

public class Main extends JavaPlugin implements Listener {

	public static HashMap<Player, SpleggPlayer> playerMap = new HashMap<Player, SpleggPlayer>();
	
	public static BukkitTask requestRunnable;
	
	public static ArrayList<SpleggWorld> worlds = new ArrayList<SpleggWorld>();
	
	public Plugin plugin = this;
	public BungeeListener bl;
	
	FileConfiguration config = this.getConfig();
	
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		Constants.init();
		//initWorlds();
		loadWorlds();
		initSQL();
		loadConfig();
		bl = new BungeeListener(this);
		bl.init();
	}
	
	public void onDisable() {
		requestRunnable.cancel();
		Bukkit.getConsoleSender().sendMessage("/kick @a");
		MySQL.disconnect();
	}
	
	public void loadConfig() {
		config.addDefault("bungeeName", "");
		config.options().copyDefaults(true);
		saveConfig();
	}
	
	public void loadWorlds() {
		Bukkit.createWorld(new WorldCreator("spleggmap1"));
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			SpleggPlayer hp = playerMap.get((Player) sender);
			if (label.equalsIgnoreCase("close")) {
				SpleggWorld w = Functions.getWorldByID(worlds, hp.serverId);
				w.stop();
			}
			if (label.equalsIgnoreCase("start")) {
				SpleggWorld w = Functions.getWorldByID(worlds, hp.serverId);
				w.initGame();
			}
		}
        return false;
    }

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		SpleggPlayer hp = new SpleggPlayer(event.getPlayer(), this);

		playerMap.put(event.getPlayer(), hp);
		if (SQL.exists("UUID", event.getPlayer().getUniqueId().toString(), "playerInfo")) {
			//hp.friends = SQL.get("friends", "UUID", "=", event.getPlayer().getUniqueId().toString(), "playerInfo").toString();
			hp.playerRank = SQL.get("playerRank", "UUID", "=", event.getPlayer().getUniqueId().toString(), "playerInfo").toString();
			hp.tokens = Integer.parseInt(SQL.get("tokens", "UUID", "=", event.getPlayer().getUniqueId().toString(), "playerInfo").toString());
			hp.luckyCrates = Integer.parseInt(SQL.get("luckyCrates", "UUID", "=", event.getPlayer().getUniqueId().toString(), "playerInfo").toString());
			hp.ownedCosmetics = SQL.get("cosmetics", "UUID", "=", event.getPlayer().getUniqueId().toString(), "playerInfo").toString();

			MySQL.update("UPDATE playerInfo SET playerName=\"" + hp.mcPlayer.getDisplayName() + "\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
		} else {
			hp.mcPlayer.kickPlayer("Log in to The Rive Server directly!");
		}
		
		if (SQL.exists("UUID", event.getPlayer().getUniqueId().toString(), "splegg")) {
			hp.points = Integer.parseInt(SQL.get("points", "UUID", "=", event.getPlayer().getUniqueId().toString(), "splegg").toString());
			hp.played = Integer.parseInt(SQL.get("played", "UUID", "=", event.getPlayer().getUniqueId().toString(), "splegg").toString());
			hp.wins = Integer.parseInt(SQL.get("wins", "UUID", "=", event.getPlayer().getUniqueId().toString(), "splegg").toString());
			hp.deaths = Integer.parseInt(SQL.get("deaths", "UUID", "=", event.getPlayer().getUniqueId().toString(), "splegg").toString());
			hp.eggsFired = Integer.parseInt(SQL.get("eggsFired", "UUID", "=", event.getPlayer().getUniqueId().toString(), "splegg").toString());
			hp.eggsLanded = Integer.parseInt(SQL.get("blocksbroken", "UUID", "=", event.getPlayer().getUniqueId().toString(), "splegg").toString());
			
			MySQL.update("UPDATE playerInfo SET lobby=\"Splegg\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
		} else {
			MySQL.update("Insert into splegg values (\"" + event.getPlayer().getUniqueId().toString() + "\", 0, 0, 0, 0, 0, 0, 0);");
		}
		
		hp.scoreboard = ScoreHelper.createScore(hp.mcPlayer);
		Scoreboard s = hp.mcPlayer.getScoreboard();
		if (hp.playerRank.equalsIgnoreCase("Regular Member")) {
			//hp.mcPlayer.setPlayerListName(ChatColor.BLUE + hp.mcPlayer.getDisplayName());
			//s.registerNewTeam("Regular Member").;
		}
		
		event.setJoinMessage(null);
		
		boolean sent = false;
		
		for (SpleggWorld world : worlds) {
			if (!sent) {
				if (world.players.size() < 20 && !world.inGame) {
					world.welcomePlayer(hp);
					sent = true;
				}
			}
		}
		
		if (!sent) {
			int id = Functions.getLowestWorldID(worlds);
			SpleggWorld w = new SpleggWorld(plugin, id);
			w.init();
			worlds.add(w);
			
			w.welcomePlayer(hp);
			sent = true;
		}
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		SpleggPlayer hp = playerMap.get(event.getPlayer());
		
		SpleggWorld world = Functions.getWorldByID(worlds, hp.serverId);
		world.onPlayerLeave(event);
		
		MySQL.update("UPDATE playerInfo SET lobby=\"Offline\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
		playerMap.remove(event.getPlayer());
		ScoreHelper.removeScore(event.getPlayer());
		
		event.setQuitMessage(null);
	}
	
	@EventHandler 
	public void onMobSpawn(CreatureSpawnEvent event) {
		if (event.getSpawnReason() == SpawnReason.EGG) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		SpleggPlayer hp = playerMap.get(event.getPlayer());
		SpleggWorld world = Functions.getWorldByID(worlds, hp.serverId);
		world.onInteract(event);
	}
	
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		SpleggWorld w = null;
		for (SpleggWorld world : worlds) {
			if (world.gameWorld == event.getEntity().getLocation().getWorld()) {
				w = world;
			}
		}
		w.onProjectileHit(event);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		SpleggPlayer hp = playerMap.get(event.getWhoClicked());
		SpleggWorld world = Functions.getWorldByID(worlds, hp.serverId);
		world.onInventoryClick(event);
	}
	
	@EventHandler
	public void onChatSend(PlayerChatEvent event) {
		SpleggPlayer hp = playerMap.get(event.getPlayer());
		event.setCancelled(true);
		Functions.getWorldByID(worlds, hp.serverId).chat(ChatColor.BLUE + hp.mcPlayer.getDisplayName() + ChatColor.DARK_GRAY + " >> " + event.getMessage());
	}
	
	@EventHandler
	public void onPlayerThrow(PlayerDropItemEvent event) {
		event.setCancelled(true);
	}
	
	public void initSQL() {
		Config.create();
		Config.setHost("66.85.144.162");
		Config.setUser("server_326179");
		Config.setPassword("RFO1mx4AEB03CTGVr*Ppz");
		Config.setDatabase("server_326179_a13bfe4c");
		Config.setPort("3306");
		Config.setSSL(true);
		MySQL.connect();
		MySQL.update("CREATE TABLE IF NOT EXISTS splegg(UUID varchar(64) PRIMARY KEY, tokens int, points int, played int, wins int, deaths int, eggsFired int, blocksbroken int);");
	}
}
