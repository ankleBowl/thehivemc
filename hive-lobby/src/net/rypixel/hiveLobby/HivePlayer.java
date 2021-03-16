package net.rypixel.hiveLobby;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;

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
	
	public String acceptFriend(String UUID) {
		if (requests != "") {
			String[] request = requests.split(":");
			if (request[0].equalsIgnoreCase("friend")) {
				if (request[1].contains(UUID)) {
					if (friends.toCharArray().length < 16) {
						friends = UUID;
					} else {
						friends += "," + UUID;
					}
					MySQL.update("UPDATE playerInfo SET friends=\"" + friends + "\" WHERE UUID=\"" + mcPlayer.getUniqueId().toString() + "\"");
					String uuidFrom = SQL.get("UUID", "playerName", "=", request[2], "playerInfo").toString();
					MySQL.update("UPDATE playerInfo SET requests=\"" + "friendback:" + mcPlayer.getUniqueId().toString() + "\" WHERE UUID=\"" + uuidFrom + "\"");
				} else {
					mcPlayer.sendMessage(ChatColor.RED + "You do not have an incoming friend request from this person!");
				}
			} else {
				mcPlayer.sendMessage(ChatColor.RED + "You do not have an incoming friend request!");
			}
		}
		return "";
	}
	
	public void forceAddFriend(String UUID) {
		if (friends.toCharArray().length < 16) {
			friends = UUID;
		} else {
			friends += "," + UUID;
		}
		MySQL.update("UPDATE playerInfo SET friends=\"" + friends + "\" WHERE UUID=\"" + mcPlayer.getUniqueId().toString() + "\"");
	}
	
	public String removeFriend(String UUID) {
		ArrayList<String> friendList = Functions.ArrayToListConversion(friends.split(","));
		boolean added = false;
		for (String p : friendList) {
			if (p.contains(UUID)) {
				added = true;
			}
		}
		if (added) {
			friendList.remove(UUID);
			friends = Functions.ListToCSV(friendList);
			MySQL.update("UPDATE playerInfo SET friends=\"" + friends + "\" WHERE UUID=\"" + mcPlayer.getUniqueId().toString() + "\"");
			MySQL.update("UPDATE playerInfo SET requests=\"" + "removefriend:" + mcPlayer.getUniqueId().toString() + "\" WHERE UUID=\"" + UUID + "\"");
			return "success";
		} else {
			return "This person is not on your friends list!";
		}
	}
	
	public String requestFriend(String UUID) {
		MySQL.update("UPDATE playerInfo SET requests=\"" + "friend:" + mcPlayer.getUniqueId().toString() + ":" + mcPlayer.getDisplayName() + "\" WHERE UUID=\"" + UUID + "\"");
		return "";
	}
	
	public String getUUID() {
		return mcPlayer.getUniqueId().toString();
	}
	
	public void createParty() {
		if (!inParty) {
			MySQL.update("Insert into parties values (\"" + mcPlayer.getUniqueId().toString() + "\", \"\");");
			inParty = true;
			isPartyOwner = true;
		}
	}
	
	public void joinParty(String uuid) {
		if (!inParty) {
			String[] request = requests.split(":");
			if (request[0].equalsIgnoreCase("partyinvite")) {
				if (uuid.equalsIgnoreCase(request[1])) {
					inParty = true;
					partyMembers = SQL.get("members", "owner", "=", uuid, "parties").toString();
					if (partyMembers.toCharArray().length < 16) {
						partyMembers = mcPlayer.getUniqueId().toString();
					} else {
						partyMembers += "," + mcPlayer.getUniqueId().toString();
					}
					partyOwner = uuid;
					MySQL.update("UPDATE parties SET members=\"" + partyMembers + "\" WHERE owner=\"" + uuid + "\"");
					MySQL.update("UPDATE playerInfo SET requests=\"" + "partyjoined:" + mcPlayer.getUniqueId().toString() + ":" + mcPlayer.getDisplayName() + "\" WHERE UUID=\"" + uuid + "\"");
				}
			}
		}
	}
	
	public void inviteToParty(String uuid) {
		MySQL.update("UPDATE playerInfo SET requests=\"" + "partyinvite:" + mcPlayer.getUniqueId().toString() + ":" + mcPlayer.getDisplayName() + "\" WHERE UUID=\"" + uuid + "\"");
	}
	
	public void leaveParty() {
		if (!isPartyOwner) {
			partyMembers = SQL.get("members", "owner", "=", partyOwner, "parties").toString();
			ArrayList<String> partyList = Functions.ArrayToListConversion(partyMembers.split(","));
			partyList.remove(mcPlayer.getUniqueId().toString());
			partyMembers = Functions.ListToCSV(partyList);
			MySQL.update("UPDATE parties SET members=\"" + partyMembers + "\" WHERE UUID=\"" + partyOwner + "\"");
			
			inParty = false;
			isPartyOwner = false;
			partyOwner = "";
			partyMembers = "";
		}
	}
	
	public void disbandParty() {
		if (isPartyOwner) {
			
		}
	}
}
