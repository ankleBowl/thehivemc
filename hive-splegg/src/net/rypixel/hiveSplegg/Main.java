package net.rypixel.hiveSplegg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
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

	public static HashMap<Player, HivePlayer> playerMap = new HashMap<Player, HivePlayer>();
	
	public static BukkitTask requestRunnable;
	
	public static ArrayList<SpleggWorld> worlds = new ArrayList<SpleggWorld>();
	
	public Plugin plugin = this;
	public BungeeListener bl;
	
	FileConfiguration config = this.getConfig();
	
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		//Constants.init();
		//initWorlds();
		initSQL();
		loadConfig();
		requests();
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
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			HivePlayer hp = playerMap.get((Player) sender);
			if (label.equalsIgnoreCase("close")) {
				SpleggWorld w = Functions.getWorldByID(worlds, hp.serverId);
				w.stop();
			}
		}
        return false;
    }

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		HivePlayer hp = new HivePlayer(event.getPlayer());
//		hp.mcPlayer.getInventory().clear();
//		hp.mcPlayer.getInventory().setItem(8, Constants.lobbySelector);
//		hp.mcPlayer.getInventory().setItem(0, Constants.gameSelector);
//		hp.mcPlayer.setGameMode(GameMode.ADVENTURE);
		playerMap.put(event.getPlayer(), hp);
		if (SQL.exists("UUID", event.getPlayer().getUniqueId().toString(), "playerInfo")) {
			hp.friends = SQL.get("friends", "UUID", "=", event.getPlayer().getUniqueId().toString(), "playerInfo").toString();
			hp.playerRank = SQL.get("playerRank", "UUID", "=", event.getPlayer().getUniqueId().toString(), "playerInfo").toString();
			hp.tokens = Integer.parseInt(SQL.get("tokens", "UUID", "=", event.getPlayer().getUniqueId().toString(), "playerInfo").toString());
			hp.luckyCrates = Integer.parseInt(SQL.get("luckyCrates", "UUID", "=", event.getPlayer().getUniqueId().toString(), "playerInfo").toString());
			hp.ownedCosmetics = SQL.get("cosmetics", "UUID", "=", event.getPlayer().getUniqueId().toString(), "playerInfo").toString();
			
			hp.partyOwner = SQL.get("partyOwner", "UUID", "=", event.getPlayer().getUniqueId().toString(), "playerInfo").toString();
			if (hp.partyOwner.toCharArray().length > 16) {
				hp.inParty = true;
				hp.refreshParty();
			}
			
			MySQL.update("UPDATE playerInfo SET lobby=\"Lobby\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
			MySQL.update("UPDATE playerInfo SET playerName=\"" + hp.mcPlayer.getDisplayName() + "\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
		} else {
			hp.mcPlayer.kickPlayer("Log in to The Rive Server directly!");
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
				if (world.players.size() < 10) {
					world.players.add(hp);
					hp.mcPlayer.teleport(new Vector(0, 100, 0).toLocation(world.world));
					hp.serverId = world.id;
					sent = true;
				}
			}
		}
		
		if (!sent) {
			int id = Functions.getLowestWorldID(worlds);
			SpleggWorld w = new SpleggWorld(plugin, id);
			w.init();
			worlds.add(w);
			
			w.players.add(hp);
			hp.mcPlayer.teleport(new Vector(0, 100, 0).toLocation(w.world));
			hp.serverId = w.id;
			sent = true;
		}
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		HivePlayer hp = playerMap.get(event.getPlayer());
		MySQL.update("UPDATE playerInfo SET lobby=\"Offline\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
		playerMap.remove(event.getPlayer());
		ScoreHelper.removeScore(event.getPlayer());
		
		event.setQuitMessage(null);
		
		if (!hp.switchingServers) {
			if (hp.isPartyOwner) {
				hp.disbandParty();
			} else if (hp.inParty) {
				hp.leaveParty();
			}
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {

		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		HivePlayer hp = playerMap.get(event.getWhoClicked());
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onChatSend(PlayerChatEvent event) {
		HivePlayer hp = playerMap.get(event.getPlayer());
		event.setCancelled(true);
		Functions.getWorldByID(worlds, hp.serverId).chat(ChatColor.BLUE + hp.mcPlayer.getDisplayName() + ChatColor.DARK_GRAY + " >> " + event.getMessage());
	}
	
	public void requests() {
		requestRunnable = new BukkitRunnable() {
		    public void run() {
		    	try {
			    	String toUUID = SQL.get("UUID", "requests", "!=", "", "playerInfo").toString(); //gets people who have requests
			    	if (toUUID != null && Functions.checkForUUID(toUUID)) { //checks to see if they are on the server
				    	String request = SQL.get("requests", "UUID", "=", toUUID, "playerInfo").toString();
				    	HivePlayer hp = playerMap.get(Bukkit.getPlayer(UUID.fromString(toUUID)));
				    	String[] requestArr = request.split(":");
				    	if (requestArr[0].equals("friend")) {
					    	hp.requests = request;
					    	hp.mcPlayer.sendMessage("You got a friend request from " + requestArr[2] + "!");
					    	MySQL.update("UPDATE playerInfo SET requests=\"\" WHERE UUID=\"" + toUUID + "\"");
				    	} else if (requestArr[0].equals("friendback")) {
				    		String name = SQL.get("playerName", "UUID", "=", requestArr[1], "playerInfo").toString();
				    		hp.mcPlayer.sendMessage(name + " accepted your friend request!");
				    		hp.forceAddFriend(requestArr[1]);
				    		MySQL.update("UPDATE playerInfo SET requests=\"\" WHERE UUID=\"" + toUUID + "\"");
				    	} else if (requestArr[0].equals("removefriend")) {
				    		hp.removeFriend(requestArr[1]);
				    	} else if (requestArr[0].equals("partyinvite")) {
				    		hp.requests = request;
				    		hp.mcPlayer.sendMessage(ChatColor.DARK_GRAY + "■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
				    		hp.mcPlayer.sendMessage(ChatColor.DARK_GRAY + "〡" + ChatColor.AQUA + "Party" + ChatColor.DARK_GRAY + "〡" + ChatColor.BLUE + requestArr[2] + ChatColor.AQUA + " wants you to join their party!");
				    		hp.mcPlayer.sendMessage(ChatColor.DARK_GRAY + "〡" + ChatColor.AQUA + "Party" + ChatColor.DARK_GRAY + "〡" + ChatColor.GREEN + "" + ChatColor.BOLD + "Click here to accept.");
				    		hp.mcPlayer.sendMessage(ChatColor.DARK_GRAY + "■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
					    	//hp.mcPlayer.sendMessage("You got a party invite from " + requestArr[2] + "!");
					    	MySQL.update("UPDATE playerInfo SET requests=\"\" WHERE UUID=\"" + toUUID + "\"");
					    	new BukkitRunnable(){
					    	    public void run(){
					    	        if (hp.requests.split(",")[0].equals("partyinvite")) {
					    	        	hp.requests = "";
					    	        }
					    	    }
					    	}.runTaskLater(plugin, 600L);
				    	} else if (requestArr[0].equals("partyjoined")) {
				    		//hp.mcPlayer.sendMessage(requestArr[2] + "accepted your party request!");
				    		hp.mcPlayer.sendMessage(ChatColor.DARK_GRAY + "〡" + ChatColor.AQUA + "Party" + ChatColor.DARK_GRAY + "〡" + ChatColor.GREEN + "✚ " + ChatColor.WHITE + requestArr[2] + ChatColor.GREEN + " joined your party");
				    		MySQL.update("UPDATE playerInfo SET requests=\"\" WHERE UUID=\"" + toUUID + "\"");
				    	} else if (requestArr[0].equals("leaveparty")) {
				    		hp.mcPlayer.sendMessage("The party has been disbanded!");
				    		MySQL.update("UPDATE playerInfo SET requests=\"\" WHERE UUID=\"" + toUUID + "\"");
				    		hp.leaveParty(); //I know this sometimes fails because the SQL entry is deleted before this is triggered
				    	} else if (requestArr[0].equals("warp")) {
				    		hp.switchingServers = true;
				    		Functions.sendToServer(hp.mcPlayer, requestArr[1], plugin);
				    		MySQL.update("UPDATE playerInfo SET requests=\"toserver:" + requestArr[2] + "\" WHERE UUID=\"" + toUUID + "\"");
				    	} else if (requestArr[0].equals("toserver")) {
							String strId = requestArr[1];
							int serverId = Integer.parseInt(strId);
							hp.mcPlayer.teleport(new Vector(0.5, 22, 0.5).toLocation(Functions.getWorldByID(worlds, serverId).world));
							Functions.getWorldByID(worlds, serverId).players.remove(hp);
							Functions.getWorldByID(worlds, serverId).players.add(hp);
							playerMap.get(hp).serverId = serverId;
							MySQL.update("UPDATE playerInfo SET requests=\"\" WHERE UUID=\"" + toUUID + "\"");
				    	}
			    	}
		    	} catch(NullPointerException e) {
		    		//Yes i know this is a bad way of doing this
		    	}
		    }
		}.runTaskTimer(this, 0L, 10L);
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
		MySQL.update("CREATE TABLE IF NOT EXISTS playerInfo(UUID varchar(64) PRIMARY KEY, friends varchar(740), playerRank varchar(16), tokens int, luckyCrates int, cosmetics varchar(9999), lobby varchar(32), playerName varchar(20), requests varchar(9999), partyOwner varchar(64));");
		MySQL.update("DROP TABLE parties");
		MySQL.update("CREATE TABLE IF NOT EXISTS parties(owner varchar(64) PRIMARY KEY, members varchar(999))");
	}
}
