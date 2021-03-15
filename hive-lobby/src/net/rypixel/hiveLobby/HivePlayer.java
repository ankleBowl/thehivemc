package net.rypixel.hiveLobby;

import java.util.ArrayList;
import java.util.List;

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
	
	public void addFriend(String UUID) {
		if (friends == "") {
			friends = UUID;
		} else {
			friends += "," + UUID;
		}
	}
	
	public void removeFriend(String UUID) {
		//String[] friendList = ;
		ArrayList<String> friendList = Functions.ArrayToListConversion(friends.split(","));
		friendList.remove(UUID);
		friends = Functions.ListToCSV(friendList);
	}
	
	public String getUUID() {
		return mcPlayer.getUniqueId().toString();
	}
}
