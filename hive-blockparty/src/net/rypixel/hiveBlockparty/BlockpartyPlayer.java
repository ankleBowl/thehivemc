package net.rypixel.hiveBlockparty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
	
	public void hideAllPlayers(HashMap<Player, BlockpartyPlayer> map) {
		for (Map.Entry<Player, BlockpartyPlayer> entry : map.entrySet()) {
			mcPlayer.hidePlayer(entry.getKey());
		}
	}
	
	public void showPlayersInWorld(ArrayList<BlockpartyPlayer> list) {
		for (BlockpartyPlayer hp : list) {
			mcPlayer.hidePlayer(hp.mcPlayer);
		}
	}

	public void hideAllPlayers(ArrayList<BlockpartyPlayer> players) {
		for (BlockpartyPlayer hp : players) {
			mcPlayer.hidePlayer(hp.mcPlayer);
		}
	}
}
