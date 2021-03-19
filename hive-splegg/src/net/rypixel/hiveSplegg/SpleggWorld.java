package net.rypixel.hiveSplegg;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_8_R1.WorldGenLargeFeatureStart;
import net.rypixel.hiveLobby.Functions;
import net.rypixel.hiveSplegg.HivePlayer;

public class SpleggWorld {

	public Plugin plugin;
	public World world;
	public int id;
	
	public ArrayList<HivePlayer> players = new ArrayList<HivePlayer>();
	
	SpleggWorld(Plugin plugin, int id) {
		this.plugin = plugin;
		this.id = id;
	}
	
	public void init() {
		world = Functions.createNewWorld(Bukkit.getWorld("world"), String.valueOf(id));
	}
	
	public void stop() {
		
		for (HivePlayer hp : players) {
			boolean sent = false;
			for (SpleggWorld world : Main.worlds) {
				if (!sent && world != this) {
					if (world.players.size() < 10) {
						world.players.add(hp);
						hp.mcPlayer.teleport(new Vector(0, 100, 0).toLocation(world.world));
						hp.serverId = world.id;
						sent = true;
					}
				}
			}
			
			if (!sent) {
				int id = Functions.getLowestWorldID(Main.worlds);
				SpleggWorld w = new SpleggWorld(plugin, id);
				w.init();
				Main.worlds.add(w);
				
				w.players.add(hp);
				hp.mcPlayer.teleport(new Vector(0, 100, 0).toLocation(w.world));
				hp.serverId = w.id;
				sent = true;
			}
		}
		
		Main.worlds.remove(this);
	}
	
	public void chat(String message) {
		for (HivePlayer hp : players) {
			hp.mcPlayer.sendMessage(message);
		}
	}
}
