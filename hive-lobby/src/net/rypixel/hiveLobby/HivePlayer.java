package net.rypixel.hiveLobby;

import java.util.ArrayList;

import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;

import me.vagdedes.mysql.database.MySQL;

public class HivePlayer {

	public Player mcPlayer;
	public String currentWorld = "lobby";
	public String currentMap = "lobby";
	public String playerRank = "";
	public int serverId = 0;
	
	public String friends = ""; //Comma seperated UUIDs
	public int tokens = 0;
	public int luckyCrates = 0;
	public String ownedCosmetics = "";
	
	public boolean playersVisible;
	
	HivePlayer(Player mcPlayer) {
		this.mcPlayer = mcPlayer;
	}
	
	public String addFriend(String UUID) {
		ArrayList<String> friendList = Functions.ArrayToListConversion(friends.split(","));
		boolean alreadyAdded = false;
		for (String p : friendList) {
			if (p == UUID) {
				alreadyAdded = true;
			}
		}
		if (!alreadyAdded) {
			if (friends == "") {
				friends = UUID;
			} else {
				friends += "," + UUID;
			}
			return "success";
		} else {
			return "This person has already been added!";
		}
	}
	
	public String removeFriend(String UUID) {
		//String[] friendList = ;
		ArrayList<String> friendList = Functions.ArrayToListConversion(friends.split(","));
		boolean added = false;
		for (String p : friendList) {
			if (p == UUID) {
				added = true;
			}
		}
		if (added) {
			friendList.remove(UUID);
			friends = Functions.ListToCSV(friendList);
			return "success";
		} else {
			return "This person is not on your friends list!";
		}
	}
	
	public String getUUID() {
		return mcPlayer.getUniqueId().toString();
	}
}
