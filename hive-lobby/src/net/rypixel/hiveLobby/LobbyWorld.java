package net.rypixel.hiveLobby;

import java.util.HashMap;

import org.bukkit.World;
import org.bukkit.entity.Player;

public class LobbyWorld {
	
	World world;
	
	public HashMap<Player, HivePlayer> playerMap = new HashMap<Player, HivePlayer>();
	
	LobbyWorld(World world) {
		this.world = world;
	}
	
	
}
