package net.rypixel.hiveBlockparty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
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
import org.bukkit.event.entity.EntitySpawnEvent;
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

	public static HashMap<Player, BlockpartyPlayer> playerMap = new HashMap<Player, BlockpartyPlayer>();

	public static ArrayList<BlockpartyWorld> worlds = new ArrayList<BlockpartyWorld>();
	
	public Plugin plugin = this;
	public BungeeListener bl;
	
	public boolean pvp = false; //REMOVE THIS LATER
	
	FileConfiguration config = this.getConfig();
	
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		Constants.init();
		loadConfig();
		loadWorlds();
		initSQL();
		bl = new BungeeListener(this);
		bl.init();
	}
	
	public void onDisable() {
		for (Map.Entry<Player, BlockpartyPlayer> set : playerMap.entrySet()) {
			set.getValue().mcPlayer.sendMessage(ChatColor.RED + "The server you were on is shutting down");
			Functions.sendToServer(set.getValue().mcPlayer, "lobby0", plugin);
		}
		for (BlockpartyWorld w : worlds) {
			w.stop();
		}
		MySQL.disconnect();
	}
	
	public void loadConfig() {
		config.addDefault("bungeeName", "");
		config.addDefault("sqlpasword", "");
		config.options().copyDefaults(true);
		saveConfig();
	}
	
	public void loadWorlds() {
		Bukkit.createWorld(new WorldCreator("bpmap1"));
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			BlockpartyPlayer hp = playerMap.get((Player) sender);
			if (label.equalsIgnoreCase("start")) {
				BlockpartyWorld w = Functions.getWorldByID(worlds, hp.serverId);
				w.initGame();
			}
		}
        return false;
    }

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		BlockpartyPlayer hp = new BlockpartyPlayer(event.getPlayer(), this);

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
		
		if (SQL.exists("UUID", event.getPlayer().getUniqueId().toString(), "blockparty")) {
			hp.points = Integer.parseInt(SQL.get("points", "UUID", "=", event.getPlayer().getUniqueId().toString(), "blockparty").toString());
			hp.playedGames = Integer.parseInt(SQL.get("played", "UUID", "=", event.getPlayer().getUniqueId().toString(), "blockparty").toString());
			hp.wonGames = Integer.parseInt(SQL.get("wins", "UUID", "=", event.getPlayer().getUniqueId().toString(), "blockparty").toString());
			hp.winstreak = Integer.parseInt(SQL.get("winstreak", "UUID", "=", event.getPlayer().getUniqueId().toString(), "blockparty").toString());
			hp.hardcorePoints = Integer.parseInt(SQL.get("hardcorepoints", "UUID", "=", event.getPlayer().getUniqueId().toString(), "blockparty").toString());
			hp.placings = Integer.parseInt(SQL.get("placings", "UUID", "=", event.getPlayer().getUniqueId().toString(), "blockparty").toString());
			hp.blockpartyCosmetics = SQL.get("cosmetics", "UUID", "=", event.getPlayer().getUniqueId().toString(), "blockparty").toString();
			hp.ownedBlockpartyCosmetics = Functions.ArrayToListConversion(hp.blockpartyCosmetics.split(","));
			String[] activeCosmetics = SQL.get("activeCosmetics", "UUID", "=", event.getPlayer().getUniqueId().toString(), "blockparty").toString().split(",");
			hp.activeBling = activeCosmetics[0];
			hp.activeJoin = activeCosmetics[1];
			hp.activeSound = activeCosmetics[2];
			hp.activeTrail = activeCosmetics[3];
			MySQL.update("UPDATE playerInfo SET lobby=\"BlockParty\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
		} else {
			MySQL.update("Insert into blockparty values (\"" + event.getPlayer().getUniqueId().toString() + "\", 0, 0, 0, 0, 0, 0, 0, \"\", \"null,null,null,null\");");
		}
		
		hp.scoreboard = ScoreHelper.createScore(hp.mcPlayer);
		Scoreboard s = hp.mcPlayer.getScoreboard();
		
		if (hp.playerRank.equalsIgnoreCase("Regular Member")) {
			//hp.mcPlayer.setPlayerListName(ChatColor.BLUE + hp.mcPlayer.getDisplayName());
			//s.registerNewTeam("Regular Member").;
		}
		
		event.setJoinMessage(null);
		
		boolean sent = false;
		
		hp.hideAllPlayers(playerMap);
		
		for (BlockpartyWorld world : worlds) {
			if (!sent) {
				if (world.players.size() < 20 && !world.inGame) {
					world.welcomePlayer(hp);
					sent = true;
				}
			}
		}
		
		if (!sent) {
			int id = Functions.getLowestWorldID(worlds);
			BlockpartyWorld w = new BlockpartyWorld(plugin, id);
			w.init();
			worlds.add(w);
			
			w.welcomePlayer(hp);
			sent = true;
		}
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		BlockpartyPlayer hp = playerMap.get(event.getPlayer());
		
		BlockpartyWorld world = Functions.getWorldByID(worlds, hp.serverId);
		world.onPlayerLeave(event.getPlayer());
		
		MySQL.update("UPDATE playerInfo SET lobby=\"Offline\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
		MySQL.update("UPDATE playerInfo SET tokens=\"" + hp.tokens + "\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
		
//		MySQL.update("UPDATE blockparty SET played=\"" + String.valueOf(hp.playedGames) + "\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
//		MySQL.update("UPDATE blockparty SET wins=\"" + String.valueOf(hp.wonGames) + "\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
//		MySQL.update("UPDATE blockparty SET winstreak=\"" + String.valueOf(hp.winstreak) + "\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
//		MySQL.update("UPDATE blockparty SET hardcorepoints=\"" + String.valueOf(hp.hardcorePoints) + "\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
//		MySQL.update("UPDATE blockparty SET hardcorewins=\"" + String.valueOf(hp.hardcoreWins) + "\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
//		MySQL.update("UPDATE blockparty SET placings=\"" + String.valueOf(hp.placings) + "\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
//		MySQL.update("UPDATE blockparty SET cosmetics=\"" + Functions.ListToCSV(hp.ownedBlockpartyCosmetics) + "\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
//		MySQL.update("UPDATE blockparty SET activeCosmetics=\"" + hp.activeBling + "," + hp.activeJoin + "," + hp.activeSound + "," + hp.activeTrail + "\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
		
		MySQL.update("UPDATE blockparty SET played=\"" + String.valueOf(hp.playedGames) + "\","
				+ "wins=\"" + String.valueOf(hp.wonGames) + "\","
				+ "winstreak=\"" + String.valueOf(hp.winstreak) + "\","
				+ "hardcorepoints=\"" + String.valueOf(hp.hardcorePoints) + "\","
				+ "hardcorewins=\"" + String.valueOf(hp.hardcoreWins) + "\","
				+ "placings=\"" + String.valueOf(hp.placings) + "\","
				+ "cosmetics=\"" + Functions.ListToCSV(hp.ownedBlockpartyCosmetics) + "\","
				+ "activeCosmetics=\"" + hp.activeBling + "," + hp.activeJoin + "," + hp.activeSound + "," + hp.activeTrail + "\""
				+ "WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
		//TODO FIX THIS LATER
		
		
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
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		BlockpartyPlayer hp = playerMap.get(event.getPlayer());
		BlockpartyWorld world = Functions.getWorldByID(worlds, hp.serverId);
		world.onInteract(event);
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		BlockpartyPlayer hp = playerMap.get(event.getWhoClicked());
		BlockpartyWorld world = Functions.getWorldByID(worlds, hp.serverId);
		world.onInventoryClick(event);
	}
	
	@EventHandler
	public void onChatSend(PlayerChatEvent event) {
		BlockpartyPlayer hp = playerMap.get(event.getPlayer());
		event.setCancelled(true);
		Functions.getWorldByID(worlds, hp.serverId).chat(ChatColor.BLUE + hp.mcPlayer.getDisplayName() + ChatColor.DARK_GRAY + " >> " + event.getMessage());
	}
	
	@EventHandler
	public void onHit(ProjectileHitEvent event) {
		Functions.getWorldByWorld(worlds, event.getEntity().getLocation().getWorld()).onHit(event);
	}
	
	@EventHandler
	public void onPlayerThrow(PlayerDropItemEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onMobSpawn(EntitySpawnEvent event) {
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
		MySQL.update("CREATE TABLE IF NOT EXISTS blockparty(UUID varchar(64) PRIMARY KEY, points int, played int, wins int, winstreak int, hardcorepoints int, hardcorewins int, placings int, cosmetics varchar(9999), activeCosmetics varchar(128));");
	}
}
