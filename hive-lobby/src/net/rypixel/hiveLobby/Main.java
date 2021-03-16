package net.rypixel.hiveLobby;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import me.vagdedes.mysql.basic.Config;
import me.vagdedes.mysql.database.MySQL;
import me.vagdedes.mysql.database.SQL;

public class Main extends JavaPlugin implements Listener {

	public static HashMap<Player, HivePlayer> playerMap = new HashMap<Player, HivePlayer>();
	
	public LobbyWorld[] lobbies = new LobbyWorld[10];
	
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		Constants.init();
		initWorlds();
		initSQL();
	}
	
	public void onDisable() {
		MySQL.disconnect();
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
		} else {
			MySQL.update("Insert into playerInfo values (\"" + event.getPlayer().getUniqueId().toString() + "\", \"\", \"None\", 0, 0, \"\", \"lobby\");");
			hp.mcPlayer.sendMessage(ChatColor.GOLD + "Welcome to your first time on The Rive server! Use the compass to select a game, and have fun!");
		}
		
		for (int i = 0; i < lobbies.length; i++) {
			if (lobbies[i].playerList.size() < 20) {
				hp.mcPlayer.teleport(new Vector(0.5, 22, 0.5).toLocation(lobbies[i].world));
				lobbies[i].playerList.add(hp);
				hp.serverId = i;
				hp.currentMap = "lobby";
				hp.currentWorld = "lobby";
				i = 1000;
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
	
	public void initSQL() {
		Config.create();
		Config.setHost("66.85.144.162");
		Config.setUser("server_326179");
		Config.setPassword("RFO1mx4AEB03CTGVr*Ppz");
		Config.setDatabase("server_326179_a13bfe4c");
		Config.setPort("3306");
		Config.setSSL(true);
		MySQL.connect();
		MySQL.update("CREATE TABLE IF NOT EXISTS playerInfo(UUID varchar(64) PRIMARY KEY, friends varchar(740), playerRank varchar(16), tokens int, luckyCrates int, cosmetics varchar(9999), lobby varchar(32));");
	}
	
	public void initWorlds() {
		for (int i = 0; i < lobbies.length; i++) {
			lobbies[i] = new LobbyWorld(Functions.createNewWorld(Bukkit.getWorld("world"), String.valueOf(i)), this);
			lobbies[i].init();
		}
	}
}
