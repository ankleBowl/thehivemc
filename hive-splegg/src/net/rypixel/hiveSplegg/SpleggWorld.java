package net.rypixel.hiveSplegg;

import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import net.rypixel.hiveSplegg.HivePlayer;

public class SpleggWorld {

	public Plugin plugin;
	public World world;
	public int id;
	
	public ArrayList<HivePlayer> players = new ArrayList<HivePlayer>();
	
	SpleggWorld(Plugin plugin, int id) {
		this.world = world;
		this.plugin = plugin;
		this.id = id;
	}
	
	public void init() {
		
	}
	
	public void stop() {
		
	}
	
	public void chat(String message) {
		for (HivePlayer hp : players) {
			hp.mcPlayer.sendMessage(message);
		}
	}
}
