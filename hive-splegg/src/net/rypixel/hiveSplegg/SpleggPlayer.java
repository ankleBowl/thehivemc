package net.rypixel.hiveSplegg;

import java.util.ArrayList;

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
	
	public String activeGun;
	public String activeMob;
	public String activeTrails;
	public String activeCelebration;
	
	public ArrayList<String> spleggCosmetics;
	
	public String currentPowerup;
	
	public int eggsFiredTemp;
	public int eggsLandedTemp;
	
	SpleggPlayer(Player mcPlayer, Plugin plugin) {
		super(mcPlayer, plugin);
		// TODO Auto-generated constructor stub
	}

}

