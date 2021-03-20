package net.rypixel.hiveLobby;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Functions {
	
	public static <T> ArrayList<T> ArrayToListConversion(T array[])   
	{   
		ArrayList<T> list = new ArrayList<>();   
			for (T t : array)   
			{   
				list.add(t);   
			}   
		return list;   
	}
	
	public static String ListToCSV(ArrayList<String> list) {
		String output = "";
		for (String v : list) {
			if (output == "") {
				output = v;
			} else {
				output += v;
			}
		}
		return output;
	}
	
	public static World createNewWorld(World world, String id) {
	    File worldDir = world.getWorldFolder();
	    String newName = world.getName() + "_" + id;
	    try {
			FileUtils.copyDirectory(worldDir, new File(worldDir.getParent(), newName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    File newWorldFolder = FileUtils.getFile(worldDir.getParent(), newName);
	    File uid = FileUtils.getFile(newWorldFolder, "uid.dat");
	    try {
			FileUtils.forceDelete(uid);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    //WorldCreator creator = new WorldCreator(newName);
		World newWorld = Bukkit.getServer().createWorld(new WorldCreator(newName));
	    return newWorld;
	}
	
	public static boolean checkForUUID(String UUID) {
		boolean inServer = false;
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getUniqueId().toString().contains(UUID)) {
				inServer = true;
			}
		}
		return inServer;
	}
	
	public static void sendToServer(Player player, String serverName, Plugin plugin) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
		out.writeUTF("Connect");
		out.writeUTF(serverName);
		} catch (IOException eee) {
		Bukkit.getLogger().info("You'll never see me!");
		}
		player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
	}
	
	public static void sendCustomData(Player player, String serverName, Plugin plugin) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		
		try {
			out.writeUTF("Forward"); // So BungeeCord knows to forward it
			out.writeUTF("ALL");
			out.writeUTF("MyChannel"); // The channel name to check if this your data
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
		DataOutputStream msgout = new DataOutputStream(msgbytes);
		try {
			msgout.writeUTF("Some kind of data here"); // You can do anything you want with msgout
			msgout.writeShort(123);
		} catch (IOException exception){
			exception.printStackTrace();
		}

		try {
			out.writeShort(msgbytes.toByteArray().length);
			out.write(msgbytes.toByteArray());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
	}
}
