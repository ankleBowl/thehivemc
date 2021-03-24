package net.rypixel.hiveGravity;

import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class GravityWorld {

	public Plugin plugin;
	public int id;
	
	public boolean inGame;
	
	public ArrayList<HivePlayer> players = new ArrayList<HivePlayer>();
	
	public World world;
	
	GravityWorld(Plugin plugin, int id) {
		this.plugin = plugin;
		this.id = id;
	}
	
	public void init() {
		
	}
	
	public void welcomePlayer(HivePlayer hp) {
		
	}
	
	public void initGame() {
		
	}
	
	public void onPlayerLeave(HivePlayer hp) {
		
	}
	
	public void onInteract(PlayerInteractEvent event) {
		
	}
	
	public void onInventoryClick(InventoryClickEvent event) {
		
	}
	
	public void chat(String messgae) {
		
	}
}
