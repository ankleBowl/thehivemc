package net.rypixel.hiveLobby;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
		hp.mcPlayer.setGameMode(GameMode.CREATIVE);
		playerMap.put(event.getPlayer(), hp);
		if (SQL.exists("UUID", event.getPlayer().getUniqueId().toString(), "playerInfo")) {
			//player.mysteryDust = Integer.parseInt(SQL.get("mysteryDust", "UUID", "=", event.getPlayer().getUniqueId().toString(), "playerInfo").toString());
		} else {
			MySQL.update("Insert into playerInfo values (\"" + event.getPlayer().getUniqueId().toString() + "\", \"\", \"None\", 0, 0, \"\");");
			hp.mcPlayer.sendMessage(ChatColor.GOLD + "Welcome to your first time on The Rive server! Use the compass to select a game, and have fun!");
		}
		
		for (int i = 0; i < lobbies.length; i++) {
			if (lobbies[i].playerList.size() < 20) {
				hp.mcPlayer.teleport(new Vector(0, 100, 0).toLocation(lobbies[i].world));
				lobbies[i].playerList.add(hp);
				i = 1000;
			}
		}
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
		MySQL.update("CREATE TABLE IF NOT EXISTS playerInfo(UUID varchar(64) PRIMARY KEY, friends varchar(740), playerRank varchar(16), tokens int, luckyCrates int, cosmetics varchar(9999));");
	}
	
	public void initWorlds() {
		for (int i = 0; i < lobbies.length; i++) {
			lobbies[i] = new LobbyWorld(Functions.createNewWorld(Bukkit.getWorld("world"), String.valueOf(i)), this);
			lobbies[i].init();
		}
	}
}
