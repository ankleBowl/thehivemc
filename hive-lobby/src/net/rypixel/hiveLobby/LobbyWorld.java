package net.rypixel.hiveLobby;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.World;
import org.bukkit.entity.Player;

public class LobbyWorld {
	
	public World world;

	public ArrayList<HivePlayer> playerList = new ArrayList<HivePlayer>();
	
	LobbyWorld(World world) {
		this.world = world;
	}
	
	
}
