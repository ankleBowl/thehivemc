package net.rypixel.hiveHide;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class HideWorld {

	public Plugin plugin;
	public int id;
	
	public boolean inGame;
	
	public ArrayList<HidePlayer> players = new ArrayList<HidePlayer>();
	
	public String[] randomMaps = new String[5]
	
	public World world;
	
	public World gameWorld;
	
	HideWorld(Plugin plugin, int id) {
		this.plugin = plugin;
		this.id = id;
	}
	
	public void init() {
		world = Functions.createNewWorld(Bukkit.getWorld("hideandseekmap"), String.valueOf(id));
	}
	
	public void welcomePlayer(HidePlayer hp) {
		players.add(hp);
		Functions.showAllPlayers(players);
		hp.mcPlayer.teleport(new Vector(-79.5, 90, 61.5).toLocation(world));
	}
	
	public void pickMaps() {
		
	}
	
	public void initGame() {
		
	}
	
	public void onPlayerLeave(HidePlayer hp) {
		
	}
	
	public void onInteract(PlayerInteractEvent event) {
		
	}
	
	public void onInventoryClick(InventoryClickEvent event) {
		
	}
	
	public void chat(String messgae) {
		
	}
}
