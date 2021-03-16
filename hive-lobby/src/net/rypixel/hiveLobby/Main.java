package net.rypixel.hiveLobby;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import me.vagdedes.mysql.basic.Config;
import me.vagdedes.mysql.database.MySQL;
import me.vagdedes.mysql.database.SQL;

public class Main extends JavaPlugin implements Listener {

	public static HashMap<Player, HivePlayer> playerMap = new HashMap<Player, HivePlayer>();
	
	public static BukkitTask requestRunnable;
	
	public LobbyWorld[] lobbies = new LobbyWorld[10];
	
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		Constants.init();
		initWorlds();
		initSQL();
		requests();
	}
	
	public void onDisable() {
		requestRunnable.cancel();
		Bukkit.getConsoleSender().sendMessage("/kick @a");
		MySQL.disconnect();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			HivePlayer player = playerMap.get((Player) sender);
			if (label.equalsIgnoreCase("friend") || label.equalsIgnoreCase("f")) {
				if (args[0].equalsIgnoreCase("list")) {
					String[] fList = player.friends.split(",");
					for (String uuid : fList) {
						Player f = Bukkit.getPlayer(UUID.fromString(uuid));
						String status = SQL.get("lobby", "UUID", "=", uuid, "playerInfo").toString();
						String name = SQL.get("playerName", "UUID", "=", uuid, "playerInfo").toString();
						player.mcPlayer.sendMessage(ChatColor.GRAY + "✦► " + ChatColor.BLUE + name + ChatColor.DARK_GRAY + " [" + status + "]");
					}
				}
				if (args[0].equalsIgnoreCase("add")) {				
					String uuid = SQL.get("UUID", "playerName", "=", args[1], "playerInfo").toString();
					player.requestFriend(uuid);
				}
				if (args[0].equalsIgnoreCase("accept")) {				
					String uuid = SQL.get("UUID", "playerName", "=", args[1], "playerInfo").toString();
					player.acceptFriend(uuid);
				}
				if (args[0].equalsIgnoreCase("remove")) {				
					String uuid = SQL.get("UUID", "playerName", "=", args[1], "playerInfo").toString();
					player.removeFriend(uuid);
				}
			} else if (label.equalsIgnoreCase("party") || label.equalsIgnoreCase("p")) {
				if (args[0].equalsIgnoreCase("invite")) {
					String uuid = SQL.get("UUID", "playerName", "=", args[1], "playerInfo").toString();
					if (!player.inParty) {
						player.createParty();
						player.inviteToParty(uuid);
					} else if (player.inParty) {
						if (player.isPartyOwner) {
							player.inviteToParty(uuid);
						} else {
							player.mcPlayer.sendMessage(ChatColor.RED + "You are not the party owner!");
						}
					}
				}
				if (args[0].equalsIgnoreCase("join")) {
					String uuid = SQL.get("UUID", "playerName", "=", args[1], "playerInfo").toString();
					player.joinParty(uuid);
				}
				if (args[0].equalsIgnoreCase("leave")) {
					player.leaveParty();
				}
				if (args[0].equalsIgnoreCase("disband")) {
					player.disbandParty();
				}
				if (args[0].equalsIgnoreCase("warp")) {
					player.refreshParty();
					if (player.isPartyOwner && player.inParty) {
						for (String s : player.partyMembers.split(",")) {
							if (Functions.checkForUUID(s)) {
								HivePlayer p = playerMap.get(Bukkit.getPlayer(UUID.fromString(s)));
								int serverId = player.serverId;
								p.mcPlayer.teleport(new Vector(0.5, 22, 0.5).toLocation(lobbies[serverId].world));
								lobbies[p.serverId].playerList.remove(p);
								lobbies[serverId].playerList.add(p);
								p.serverId = serverId;
							} else {
								//Future waterfall || bungee code
							}
						}
					}
				}
			}
		}
        return false;
    }

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		HivePlayer hp = new HivePlayer(event.getPlayer());
		hp.mcPlayer.getInventory().clear();
		hp.mcPlayer.getInventory().setItem(8, Constants.lobbySelector);
		hp.mcPlayer.setGameMode(GameMode.ADVENTURE);
		playerMap.put(event.getPlayer(), hp);
		if (SQL.exists("UUID", event.getPlayer().getUniqueId().toString(), "playerInfo")) {
			hp.friends = SQL.get("friends", "UUID", "=", event.getPlayer().getUniqueId().toString(), "playerInfo").toString();
			hp.playerRank = SQL.get("playerRank", "UUID", "=", event.getPlayer().getUniqueId().toString(), "playerInfo").toString();
			hp.tokens = Integer.parseInt(SQL.get("tokens", "UUID", "=", event.getPlayer().getUniqueId().toString(), "playerInfo").toString());
			hp.luckyCrates = Integer.parseInt(SQL.get("luckyCrates", "UUID", "=", event.getPlayer().getUniqueId().toString(), "playerInfo").toString());
			hp.ownedCosmetics = SQL.get("cosmetics", "UUID", "=", event.getPlayer().getUniqueId().toString(), "playerInfo").toString();
			
			MySQL.update("UPDATE playerInfo SET lobby=\"Lobby\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
			MySQL.update("UPDATE playerInfo SET playerName=\"" + hp.mcPlayer.getDisplayName() + "\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
		} else {
			MySQL.update("Insert into playerInfo values (\"" + event.getPlayer().getUniqueId().toString() + "\", \"\", \"None\", 0, 0, \"\", \"Lobby\",\"" + hp.mcPlayer.getDisplayName() + "\", \"\");");
			hp.mcPlayer.sendMessage(ChatColor.GOLD + "Welcome to your first time on The Rive server! Use the compass to select a game, and have fun!");
		}
		
		for (int i = 0; i < lobbies.length; i++) {
			if (lobbies[i].playerList.size() < 20) {
				hp.mcPlayer.teleport(new Vector(0.5, 22, 0.5).toLocation(lobbies[i].world));
				lobbies[i].playerList.add(hp);
				hp.serverId = i;
				hp.currentMap = "Lobby";
				hp.currentWorld = "Lobby";
				i = 1000;
			}
		}
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		HivePlayer hp = playerMap.get(event.getPlayer());
		MySQL.update("UPDATE playerInfo SET lobby=\"Offline\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
		playerMap.remove(event.getPlayer());
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.getItem() != null) {
				switch (event.getItem().getType()) {
				case BOOK:
					event.getPlayer().openInventory(Constants.lobbySelector(lobbies.length));
					break;
				default:
					break;
				}
			}
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getInventory().getTitle().equalsIgnoreCase("Hub Selector")) {
			if (event.getCurrentItem() != null) {
				String strId = event.getCurrentItem().getItemMeta().getDisplayName();
				int serverId = Integer.parseInt(strId) - 1;
				playerMap.get(event.getWhoClicked()).mcPlayer.teleport(new Vector(0.5, 22, 0.5).toLocation(lobbies[serverId].world));
				lobbies[playerMap.get(event.getWhoClicked()).serverId].playerList.remove(playerMap.get(event.getWhoClicked()));
				lobbies[serverId].playerList.add(playerMap.get(event.getWhoClicked()));
				playerMap.get(event.getWhoClicked()).serverId = serverId;
			}
		}
		event.setCancelled(true);
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
					    	hp.mcPlayer.sendMessage("You got a party invite from " + requestArr[2] + "!");
					    	MySQL.update("UPDATE playerInfo SET requests=\"\" WHERE UUID=\"" + toUUID + "\"");
				    	} else if (requestArr[0].equals("partyjoined")) {
				    		hp.mcPlayer.sendMessage(requestArr[2] + "accepted your party request!");
				    		MySQL.update("UPDATE playerInfo SET requests=\"\" WHERE UUID=\"" + toUUID + "\"");
				    	} else if (requestArr[0].equals("leaveparty")) {
				    		hp.mcPlayer.sendMessage("The party has been disbanded!");
				    		MySQL.update("UPDATE playerInfo SET requests=\"\" WHERE UUID=\"" + toUUID + "\"");
				    		hp.leaveParty(); //I know this sometimes fails because the SQL entry is deleted before this is triggered
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
		MySQL.update("CREATE TABLE IF NOT EXISTS playerInfo(UUID varchar(64) PRIMARY KEY, friends varchar(740), playerRank varchar(16), tokens int, luckyCrates int, cosmetics varchar(9999), lobby varchar(32), playerName varchar(20), requests varchar(9999));");
		MySQL.update("DROP TABLE parties");
		MySQL.update("CREATE TABLE IF NOT EXISTS parties(owner varchar(64) PRIMARY KEY, members varchar(999))");
	}
	
	public void initWorlds() {
		for (int i = 0; i < lobbies.length; i++) {
			lobbies[i] = new LobbyWorld(Functions.createNewWorld(Bukkit.getWorld("world"), String.valueOf(i)), this);
			lobbies[i].init();
		}
	}
}
