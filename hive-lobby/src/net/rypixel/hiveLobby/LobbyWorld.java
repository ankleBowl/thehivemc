package net.rypixel.hiveLobby;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.connorlinfoot.titleapi.*;

public class LobbyWorld {
	
	public World world;
	public Plugin plugin;

	public ArrayList<LobbyPlayer> playerList = new ArrayList<LobbyPlayer>();
	
	LobbyWorld(World world, Plugin plugin) {
		this.world = world;
		this.plugin = plugin;
	}
	
	public void init() {
		update();
		//spawnWither();
	}
	
	public void spawnWither() {
		Entity e = world.spawnEntity(new Vector(0, 0, 0).toLocation(world), EntityType.WITHER);
		Wither w = (Wither) e;
		w.setCustomNameVisible(true);
		
		w.setCustomName(ChatColor.YELLOW + "" + ChatColor.BOLD + "play.HiveMC.com");
	}
	
	public void update() {
		new BukkitRunnable() {
		    public void run() {
		        //Iterate through all players
		    	for (LobbyPlayer player : playerList) {
		    		//Give player hunger
		    		player.mcPlayer.setFoodLevel(20);
		    		
		    		//Check for launchpad locations
		    		for (Vector vec : Constants.launchpads1) {
		    			Location loc = vec.toLocation(world);
		    			if (player.mcPlayer.getLocation().distance(loc) < 1) {
		    				Vector dic = player.mcPlayer.getLocation().getDirection();
		    				Vector velocity = new Vector(dic.getX() * 10, 1.5, dic.getY() * 10);
		    				player.mcPlayer.setVelocity(velocity);
		    			}
		    		}
		    		for (Vector vec : Constants.launchpads2) {
		    			Location loc = vec.toLocation(world);
		    			if (player.mcPlayer.getLocation().distance(loc) < 1) {
		    				Vector dic = player.mcPlayer.getLocation().getDirection();
		    				Vector velocity = new Vector(dic.getX() * 10, 1.5, dic.getY() * 10);
		    				player.mcPlayer.setVelocity(velocity);
		    			}
		    		}
		    		for (Vector vec : Constants.launchpads3) {
		    			Location loc = vec.toLocation(world);
		    			if (player.mcPlayer.getLocation().distance(loc) < 1) {
		    				Vector dic = player.mcPlayer.getLocation().getDirection();
		    				Vector velocity = new Vector(dic.getX() * 10, 1.5, dic.getY() * 10);
		    				player.mcPlayer.setVelocity(velocity);
		    			}
		    		}
		    		
		    		if (player.mcPlayer.getLocation().distance(Constants.parkourLocations[0].toLocation(world)) < 1 && player.inParkour == false) {
	    				player.startParkour();
	    			}
		    		
		    		if (player.inParkour) {
		    			if (player.mcPlayer.getLocation().distance(Constants.parkourLocations[player.parkourCheckpoint + 1].toLocation(world)) < 1) {
		    				player.checkpointParkour();
		    				if (player.parkourCheckpoint == 3) {
		    					player.completeParkour();
		    				}
		    			}
		    		}
		    		
		    		player.scoreboard.setTitle(ChatColor.BOLD + "" + ChatColor.GOLD + "The" + ChatColor.YELLOW + "Hive");
		    		player.scoreboard.setSlot(15, "");
		    		player.scoreboard.setSlot(14, ChatColor.BOLD + "" + ChatColor.RED + "Rank");
		    		player.scoreboard.setSlot(13, ChatColor.BLUE + player.playerRank);
		    		player.scoreboard.setSlot(12, "");
		    		player.scoreboard.setSlot(11, ChatColor.BOLD + "" + ChatColor.GREEN + "Tokens");
		    		player.scoreboard.setSlot(10, ChatColor.GRAY + String.valueOf(player.tokens));
		    		player.scoreboard.setSlot(9, "");
		    		player.scoreboard.setSlot(8, ChatColor.BOLD + "" + ChatColor.AQUA + "LuckyCrates");
		    		player.scoreboard.setSlot(7, ChatColor.GRAY + String.valueOf(player.tokens));
		    		player.scoreboard.setSlot(6, "");
		    		player.scoreboard.setSlot(5, ChatColor.BOLD + "" + ChatColor.WHITE + "Server");
		    		player.scoreboard.setSlot(4, ChatColor.GRAY + "Regular Hub " + String.valueOf(player.serverId + 1));
		    		player.scoreboard.setSlot(3, "");
		    		player.scoreboard.setSlot(2, ChatColor.DARK_GRAY + "----------------");
		    		player.scoreboard.setSlot(1, ChatColor.GOLD + "play." + ChatColor.YELLOW + "HiveMC" + ChatColor.GOLD + ".com");
		    		
		    		
		    		
		    	}
		    }
		}.runTaskTimer(plugin, 0L, 2L);
	}
	
	public void welcomePlayer(LobbyPlayer hp) {
		hp.mcPlayer.teleport(new Vector(0.5, 110, 0.5).toLocation(world));
		TitleAPI.sendTitle(hp.mcPlayer, 20, 20, 20, ChatColor.GREEN + "play" + ChatColor.AQUA + " HiveMC " + ChatColor.GREEN + "com");
	}
	
	public void chat(String message) {
		for (LobbyPlayer hp : playerList) {
			hp.mcPlayer.sendMessage(message);
		}
	}
}
