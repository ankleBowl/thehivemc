package net.rypixel.hiveLobby;

import org.bukkit.entity.Player;

public class HivePlayer {

	public Player mcPlayer;
	public String currentWorld = "";
	public String currentMap = "";
	public String playerRank = "";
	public int serverId = 0;
	
	public String friends = ""; //Comma seperated UUIDs
	public int tokens = 0;
	public int luckyCrates = 0;
	public String ownedCosmetics = "";
	
	public boolean playersVisible;
	
	public void addFriend() {
		
	}
	
}
