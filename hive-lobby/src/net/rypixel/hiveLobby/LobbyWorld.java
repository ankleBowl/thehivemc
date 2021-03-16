package net.rypixel.hiveLobby;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class LobbyWorld {
	
	public World world;
	public Plugin plugin;

	public ArrayList<HivePlayer> playerList = new ArrayList<HivePlayer>();
	
	LobbyWorld(World world, Plugin plugin) {
		this.world = world;
		this.plugin = plugin;
	}
	
	public void init() {
		update();
	}
	
	public void update() {
		new BukkitRunnable() {
		    public void run() {
		        //Iterate through all players
		    	for (HivePlayer player : playerList) {
		    		//Check for launchpad locations
		    		for (Vector vec : Constants.launchpads1) {
		    			Location loc = vec.toLocation(world);
		    			if (player.mcPlayer.getLocation().distance(loc) < 0.5) {
		    				player.mcPlayer.setVelocity(player.mcPlayer.getVelocity().add(new Vector(0, 2, 0)).multiply(5));
		    			}
		    		}
		    		for (Vector vec : Constants.launchpads2) {
		    			Location loc = vec.toLocation(world);
		    			if (player.mcPlayer.getLocation().distance(loc) < 0.5) {
		    				player.mcPlayer.setVelocity(player.mcPlayer.getVelocity().add(new Vector(0, 2, 0)).multiply(5));
		    			}
		    		}
		    		for (Vector vec : Constants.launchpads3) {
		    			Location loc = vec.toLocation(world);
		    			if (player.mcPlayer.getLocation().distance(loc) < 0.5) {
		    				player.mcPlayer.setVelocity(player.mcPlayer.getVelocity().add(new Vector(0, 2, 0)).multiply(5));
		    			}
		    		}
		    	}
		    }
		}.runTaskTimer(plugin, 0L, 2L);
	}
	
	
}
