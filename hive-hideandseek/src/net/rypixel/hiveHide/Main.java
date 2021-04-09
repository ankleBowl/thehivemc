package net.rypixel.hiveHide;

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
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
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

	public static HashMap<Player, HidePlayer> playerMap = new HashMap<Player, HidePlayer>();
	
	public static BukkitTask requestRunnable;
	
	public static ArrayList<HideWorld> worlds = new ArrayList<HideWorld>();
	
	public Plugin plugin = this;
	public BungeeListener bl;
	
	FileConfiguration config = this.getConfig();
	
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		Constants.init();
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
		config.addDefault("sqlpassword", "");
		config.options().copyDefaults(true);
		saveConfig();
	}
	
	public void loadWorlds() {
		Bukkit.createWorld(new WorldCreator("hideandseekmap"));
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			HidePlayer hp = playerMap.get((Player) sender);
			if (label.equalsIgnoreCase("close")) {
				HideWorld w = Functions.getWorldByID(worlds, hp.serverId);
				//w.stop();
			}
			if (label.equalsIgnoreCase("start")) {
				HideWorld w = Functions.getWorldByID(worlds, hp.serverId);
				w.initGame();
			}
		}
        return false;
    }

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		HidePlayer hp = new HidePlayer(event.getPlayer(), this);

		playerMap.put(event.getPlayer(), hp);
		if (SQL.exists("UUID", event.getPlayer().getUniqueId().toString(), "playerInfo")) {
			//hp.friends = SQL.get("friends", "UUID", "=", event.getPlayer().getUniqueId().toString(), "playerInfo").toString();
			hp.playerRank = SQL.get("playerRank", "UUID", "=", event.getPlayer().getUniqueId().toString(), "playerInfo").toString();
			hp.tokens = Integer.parseInt(SQL.get("tokens", "UUID", "=", event.getPlayer().getUniqueId().toString(), "playerInfo").toString());
			hp.luckyCrates = Integer.parseInt(SQL.get("luckyCrates", "UUID", "=", event.getPlayer().getUniqueId().toString(), "playerInfo").toString());
			hp.ownedCosmetics = SQL.get("cosmetics", "UUID", "=", event.getPlayer().getUniqueId().toString(), "playerInfo").toString();
		} else {
			hp.mcPlayer.kickPlayer("Log in to The Rive Server directly!");
		}
		
//		if (SQL.exists("UUID", event.getPlayer().getUniqueId().toString(), "blockparty")) {
//			hp.points = Integer.parseInt(SQL.get("points", "UUID", "=", event.getPlayer().getUniqueId().toString(), "blockparty").toString());
//			hp.playedGames = Integer.parseInt(SQL.get("played", "UUID", "=", event.getPlayer().getUniqueId().toString(), "blockparty").toString());
//			hp.wonGames = Integer.parseInt(SQL.get("wins", "UUID", "=", event.getPlayer().getUniqueId().toString(), "blockparty").toString());
//			hp.winstreak = Integer.parseInt(SQL.get("winstreak", "UUID", "=", event.getPlayer().getUniqueId().toString(), "blockparty").toString());
//			hp.hardcorePoints = Integer.parseInt(SQL.get("hardcorepoints", "UUID", "=", event.getPlayer().getUniqueId().toString(), "blockparty").toString());
//			hp.placings = Integer.parseInt(SQL.get("placings", "UUID", "=", event.getPlayer().getUniqueId().toString(), "blockparty").toString());
//			hp.blockpartyCosmetics = SQL.get("cosmetics", "UUID", "=", event.getPlayer().getUniqueId().toString(), "blockparty").toString();
//			
//			MySQL.update("UPDATE playerInfo SET lobby=\"BlockParty\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
//		} else {
//			MySQL.update("Insert into blockparty values (\"" + event.getPlayer().getUniqueId().toString() + "\", 0, 0, 0, 0, 0, 0, 0, \"\");");
//		}
		
		hp.scoreboard = ScoreHelper.createScore(hp.mcPlayer);
		
		event.setJoinMessage(null);
		
		boolean sent = false;
		
		for (HideWorld world : worlds) {
			if (!sent) {
				if (world.players.size() < 20 && !world.loadingGame) {
					world.welcomePlayer(hp);
					sent = true;
				}
			}
		}
		
		if (!sent) {
			int id = Functions.getLowestWorldID(worlds);
			HideWorld w = new HideWorld(plugin, id);
			w.init();
			worlds.add(w);
			
			w.welcomePlayer(hp);
			sent = true;
		}
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		HidePlayer hp = playerMap.get(event.getPlayer());
		
		HideWorld world = Functions.getWorldByID(worlds, hp.serverId);
		world.onPlayerLeave(hp);
		
		MySQL.update("UPDATE playerInfo SET lobby=\"Offline\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
		
//		MySQL.update("UPDATE blockparty SET played=\"" + String.valueOf(hp.playedGames) + "\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
//		MySQL.update("UPDATE blockparty SET wins=\"" + String.valueOf(hp.wonGames) + "\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
//		MySQL.update("UPDATE blockparty SET winstreak=\"" + String.valueOf(hp.winstreak) + "\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
//		MySQL.update("UPDATE blockparty SET hardcorepoints=\"" + String.valueOf(hp.hardcorePoints) + "\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
//		MySQL.update("UPDATE blockparty SET hardcorewins=\"" + String.valueOf(hp.hardcoreWins) + "\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
//		MySQL.update("UPDATE blockparty SET placings=\"" + String.valueOf(hp.placings) + "\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
//		MySQL.update("UPDATE blockparty SET cosmetics=\"" + hp.blockpartyCosmetics + "\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
		
		
		playerMap.remove(event.getPlayer());
		ScoreHelper.removeScore(event.getPlayer());
		
		event.setQuitMessage(null);
	}
	
	@EventHandler 
	public void onMobSpawn(CreatureSpawnEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			HivePlayer hp = Main.playerMap.get(event.getEntity());
			Functions.getWorldByID(worlds, hp.serverId).onEntityDamage(event);
		}
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player) {
			HivePlayer hp = Main.playerMap.get(event.getEntity());
			Functions.getWorldByID(worlds, hp.serverId).onEntityDamageByEntity(event);
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		HidePlayer hp = playerMap.get(event.getPlayer());
		HideWorld world = Functions.getWorldByID(worlds, hp.serverId);
		world.onInteract(event);
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		HidePlayer hp = playerMap.get(event.getWhoClicked());
		HideWorld world = Functions.getWorldByID(worlds, hp.serverId);
		world.onInventoryClick(event);
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		HidePlayer hp = playerMap.get(event.getPlayer());
		Functions.getWorldByID(worlds, hp.serverId).onPlayerMove(event);
	}
	
	@EventHandler
	public void onChatSend(PlayerChatEvent event) {
		HidePlayer hp = playerMap.get(event.getPlayer());
		event.setCancelled(true);
		Functions.getWorldByID(worlds, hp.serverId).chat(ChatColor.BLUE + hp.mcPlayer.getDisplayName() + ChatColor.DARK_GRAY + " >> " + event.getMessage());
	}
	
	@EventHandler
	public void onPlayerThrow(PlayerDropItemEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onMobSpawn(EntitySpawnEvent event) {
		event.setCancelled(true);
	}
	
//	@EventHandler
//	public void usePortal(PlayerPortalEvent event) {
//		event.setCancelled(true);
//		HidePlayer hp = playerMap.get(event.getPlayer());
//		Functions.getWorldByID(worlds, hp.serverId).onPortalUsed(event);
//	}
	
	@EventHandler
	public void onBlockUpdate(BlockPhysicsEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onEntityChangeBlockEvent(EntityChangeBlockEvent event) {
		event.setCancelled(true);
	}
	
	public void initSQL() {
		Config.create();
		Config.setHost("66.85.144.162");
		Config.setUser("server_326179");
		Config.setPassword(config.getString("sqlpassword"));
		Config.setDatabase("server_326179_a13bfe4c");
		Config.setPort("3306");
		Config.setSSL(true);
		MySQL.connect();
		MySQL.update("CREATE TABLE IF NOT EXISTS blockparty(UUID varchar(64) PRIMARY KEY, points int, played int, wins int, winstreak int, hardcorepoints int, hardcorewins int, placings int, cosmetics varchar(9999));");
	}
}
