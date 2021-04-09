package net.rypixel.hiveLobby;

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

	public static HashMap<Player, LobbyPlayer> playerMap = new HashMap<Player, LobbyPlayer>();
	
	public static HashMap<String, Integer> serverToSend = new HashMap<String, Integer>();
	
	public static BukkitTask requestRunnable;
	
	public LobbyWorld[] lobbies = new LobbyWorld[10];
	
	public Plugin plugin = this;
	public BungeeListener bl;
	
	FileConfiguration config = this.getConfig();
	
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		Constants.init();
		initWorlds();
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
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			LobbyPlayer player = playerMap.get((Player) sender);
			if (label.equalsIgnoreCase("transfer")) {
				Functions.sendToServer(player.mcPlayer, args[0], this);
			}
		}
        return false;
    }

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		LobbyPlayer hp = new LobbyPlayer(event.getPlayer(), this);
		hp.mcPlayer.getInventory().clear();
		hp.mcPlayer.getInventory().setItem(8, Constants.lobbySelector);
		hp.mcPlayer.getInventory().setItem(0, Constants.gameSelector);
		hp.mcPlayer.setGameMode(GameMode.ADVENTURE);
		playerMap.put(event.getPlayer(), hp);
		if (SQL.exists("UUID", event.getPlayer().getUniqueId().toString(), "playerInfo")) {
			//hp.friends = SQL.get("friends", "UUID", "=", event.getPlayer().getUniqueId().toString(), "playerInfo").toString();
			hp.playerRank = SQL.get("playerRank", "UUID", "=", event.getPlayer().getUniqueId().toString(), "playerInfo").toString();
			hp.tokens = Integer.parseInt(SQL.get("tokens", "UUID", "=", event.getPlayer().getUniqueId().toString(), "playerInfo").toString());
			hp.luckyCrates = Integer.parseInt(SQL.get("luckyCrates", "UUID", "=", event.getPlayer().getUniqueId().toString(), "playerInfo").toString());
			hp.ownedCosmetics = SQL.get("cosmetics", "UUID", "=", event.getPlayer().getUniqueId().toString(), "playerInfo").toString();
			
			MySQL.update("UPDATE playerInfo SET lobby=\"Lobby\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
			MySQL.update("UPDATE playerInfo SET playerName=\"" + hp.mcPlayer.getDisplayName() + "\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
		} else {
			MySQL.update("Insert into playerInfo values (\"" + event.getPlayer().getUniqueId().toString() + "\", \"\", \"Regular Member\", 0, 0, \"\", \"Lobby\",\"" + hp.mcPlayer.getDisplayName() + "\", \"\", \"\");");
			hp.mcPlayer.sendMessage(ChatColor.GOLD + "Welcome to your first time on The Rive server! Use the compass to select a game, and have fun!");
			hp.playerRank = "Regular Member";
		}
		hp.scoreboard = ScoreHelper.createScore(hp.mcPlayer);
		Scoreboard s = hp.mcPlayer.getScoreboard();
		if (hp.playerRank.equalsIgnoreCase("Regular Member")) {
			//hp.mcPlayer.setPlayerListName(ChatColor.BLUE + hp.mcPlayer.getDisplayName());
			//s.registerNewTeam("Regular Member").;
		}
		
		event.setJoinMessage(null);
		
		for (int i = 0; i < lobbies.length; i++) {
			if (lobbies[i].playerList.size() < 20) {
				lobbies[i].playerList.add(hp);
				lobbies[i].welcomePlayer(hp);
				hp.serverId = i;
				hp.currentMap = "Lobby";
				hp.currentWorld = "Lobby";
				i = 1000;
			}
		}
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		LobbyPlayer hp = playerMap.get(event.getPlayer());
		
		if (hp.inParkour) {
			hp.cancelParkour();
		}
		
		MySQL.update("UPDATE playerInfo SET lobby=\"Offline\" WHERE UUID=\"" + hp.mcPlayer.getUniqueId().toString()+ "\"");
		playerMap.remove(event.getPlayer());
		ScoreHelper.removeScore(event.getPlayer());
		
		event.setQuitMessage(null);
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
				case COMPASS:
					event.getPlayer().openInventory(Constants.gameSelector());
				default:
					break;
				}
			}
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		LobbyPlayer hp = playerMap.get(event.getWhoClicked());
		if (event.getInventory().getTitle().equalsIgnoreCase("Hub Selector")) {
			if (event.getCurrentItem() != null) {
				String strId = event.getCurrentItem().getItemMeta().getDisplayName();
				int serverId = Integer.parseInt(strId) - 1;
				hp.mcPlayer.teleport(new Vector(0.5, 110, 0.5).toLocation(lobbies[serverId].world));
				lobbies[hp.serverId].playerList.remove(hp);
				lobbies[serverId].playerList.add(hp);
				hp.serverId = serverId;
			}
		} else if (event.getInventory().getTitle().equalsIgnoreCase("The Hive Games")) {
			if (event.getCurrentItem() != null) {
				switch (event.getCurrentItem().getType()) {
				case COMPASS:
					hp.mcPlayer.teleport(new Vector(0.5, 22, 0.5).toLocation(lobbies[hp.serverId].world));
					break;
				case SLIME_BALL:
					hp.mcPlayer.teleport(new Vector(-60.5, 18, 60.5).toLocation(lobbies[hp.serverId].world));
					break;
				case EGG:
					Functions.sendToServer(hp.mcPlayer, "splegg", this);
					break;
				case RECORD_4:
					Functions.sendToServer(hp.mcPlayer, "blockparty", this);
					break;
				case GOLD_BOOTS:
					Functions.sendToServer(hp.mcPlayer, "gravity", this);
					break;
				default:
					break;
				}
			}
		}
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onChatSend(PlayerChatEvent event) {
		LobbyPlayer hp = playerMap.get(event.getPlayer());
		event.setCancelled(true);
		lobbies[hp.serverId].chat(ChatColor.BLUE + hp.mcPlayer.getDisplayName() + ChatColor.DARK_GRAY + " >> " + event.getMessage());
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
		MySQL.update("CREATE TABLE IF NOT EXISTS playerInfo(UUID varchar(64) PRIMARY KEY, friends varchar(740), playerRank varchar(16), tokens int, luckyCrates int, cosmetics varchar(9999), lobby varchar(32), playerName varchar(20), requests varchar(9999), partyOwner varchar(64));");
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
