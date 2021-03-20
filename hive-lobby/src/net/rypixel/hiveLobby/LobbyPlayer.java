package net.rypixel.hiveLobby;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class LobbyPlayer extends HivePlayer {

	public boolean inParkour;
	public BukkitTask parkourRunnable;
	public int parkourTime;
	public int parkourCheckpoint;
	public Vector[] parkourCheckpoints;
	
	LobbyPlayer(Player mcPlayer, Plugin plugin) {
		super(mcPlayer, plugin);
		// TODO Auto-generated constructor stub
	}
	
	public void startParkour() {
		
		mcPlayer.sendMessage("");
		mcPlayer.sendMessage("");
		mcPlayer.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Parkour Started!");
		mcPlayer.sendMessage("");
		mcPlayer.sendMessage(ChatColor.YELLOW + "You can return to checkpoints by using");
		mcPlayer.sendMessage(ChatColor.WHITE + "/parkour checkpoint " + ChatColor.YELLOW + "or" + ChatColor.WHITE + " /parkour start");
		mcPlayer.sendMessage("");
		
		inParkour = true;
		parkourRunnable = new BukkitRunnable() {
		    public void run() {
		        parkourTime++;
		    }
		}.runTaskTimer(plugin, 0L, 1L);
	}
	
	public void cancelParkour() {
		parkourRunnable.cancel();
		parkourTime = 0;
		inParkour = false;
	}
	
	public void completeParkour() {
		parkourRunnable.cancel();
		parkourTime = 0;
		inParkour = false;
	}
	
	public void checkpointParkour() {
		parkourCheckpoint++;
		mcPlayer.sendMessage("You have reached checkpoint " + String.valueOf(parkourCheckpoint));
	}

}
