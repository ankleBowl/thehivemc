package net.rypixel.hiveLobby;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import me.vagdedes.mysql.database.MySQL;
import me.vagdedes.mysql.database.SQL;
import net.md_5.bungee.api.ChatColor;

public class HivePlayer {

	public Player mcPlayer;
	public String currentWorld = "lobby";
	public String currentMap = "lobby";
	public String playerRank = "";
	public int serverId = 0;
	public ScoreHelper scoreboard;
	public boolean switchingServers;
	public boolean hidePlayers;
	public Plugin plugin;
	
	public int tokens = 0;
	public int luckyCrates = 0;
	public String ownedCosmetics = "";
	
	HivePlayer(Player mcPlayer, Plugin plugin) {
		this.mcPlayer = mcPlayer;
		this.plugin = plugin;
	}

	public String getUUID() {
		return mcPlayer.getUniqueId().toString();
	}
}
