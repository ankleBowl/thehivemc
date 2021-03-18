
package net.rypixel.hiveLobby;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

public class BungeeListener implements PluginMessageListener {

	Plugin plugin;
	
	BungeeListener(Plugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		if (subchannel.equals("SomeSubChannel")) {
		
		}
	}
	
	public void init() {
		Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
	    Bukkit.getServer().getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", (PluginMessageListener) this);
	}

}
