package net.rypixel.hiveLobby;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

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
}
