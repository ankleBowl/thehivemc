package net.rypixel.hiveHide;

import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class HidePlayer extends HivePlayer {

	HidePlayer(Player mcPlayer, Plugin plugin) {
		super(mcPlayer, plugin);
		// TODO Auto-generated constructor stub
	}
	
	public Material block;
	public boolean isHunter;
	public Vector lastCoords;
	public int lastMoved;
	public boolean solid;
	public FallingBlock blockEntity;
	public int attackCooldown;
}
