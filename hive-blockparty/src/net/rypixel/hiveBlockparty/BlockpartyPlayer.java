package net.rypixel.hiveBlockparty;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class BlockpartyPlayer extends HivePlayer {

	BlockpartyPlayer(Player mcPlayer, Plugin plugin) {
		super(mcPlayer, plugin);
		// TODO Auto-generated constructor stub
	}

	public boolean isDead;
	public int tempPoints;
	public int points;
	public int playedGames;
	public int wonGames;
	public int winstreak;
	public int hardcorePoints;
	public int hardcoreWins;
	public int placings;
	
	public String powerUp;
	
	public String blockpartyCosmetics;
}
