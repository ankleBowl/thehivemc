package net.rypixel.hiveBlockparty;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

public class Cosmetic {
	public int slotNumber;
	public int price;
	public Material page;
	public String name;
	Map<String, Object> item;
	Sound sound;
	
	
	Cosmetic(int slotNumber, int price, Material page, String name, Map<String, Object> item) {
		this.slotNumber = slotNumber;
		this.price = price;
		this.page = page;
		this.name = name;
		this.item = item;
	}
	
	public void setSound(Sound s) {
		this.sound = s;
	}
	
	public Map<String, Object> getItem() {
		return item;
	}
}
