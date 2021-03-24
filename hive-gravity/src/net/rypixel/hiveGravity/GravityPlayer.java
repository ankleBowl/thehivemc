package net.rypixel.hiveGravity;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class GravityPlayer extends HivePlayer {

	GravityPlayer(Player mcPlayer, Plugin plugin) {
		super(mcPlayer, plugin);
		// TODO Auto-generated constructor stub
	}

	public int level;
	public boolean finished;
	
	public int flyTime;
	
}
