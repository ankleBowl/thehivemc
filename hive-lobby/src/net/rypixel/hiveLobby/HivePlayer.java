package net.rypixel.hiveLobby;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.vagdedes.mysql.database.MySQL;
import me.vagdedes.mysql.database.SQL;
import net.md_5.bungee.api.ChatColor;

public class HivePlayer {

	public Player mcPlayer;
	public String currentWorld = "lobby";
	public String currentMap = "lobby";
	public String playerRank = "";
	public String requests = "";
	public int serverId = 0;
	public ScoreHelper scoreboard;
	public boolean switchingServers;
	
	public boolean inParty;
	public boolean isPartyOwner;
	public String partyMembers;
	public String partyOwner;
	
	public String friends = ""; //Comma seperated UUIDs
	public int tokens = 0;
	public int luckyCrates = 0;
	public String ownedCosmetics = "";
	
	public boolean playersVisible;
	
	HivePlayer(Player mcPlayer) {
		this.mcPlayer = mcPlayer;
	}
	
	public String getUUID() {
		return mcPlayer.getUniqueId().toString();
	}
}
