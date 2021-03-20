package net.rypixel.hiveSplegg;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SpleggPlayer extends HivePlayer {

	public boolean alive = true;
	
	public int eggsFired;
	public int eggsLanded;
	public int points;
	public int played;
	public int wins;
	public int deaths;
	
	SpleggPlayer(Player mcPlayer, Plugin plugin) {
		super(mcPlayer, plugin);
		// TODO Auto-generated constructor stub
	}

}

