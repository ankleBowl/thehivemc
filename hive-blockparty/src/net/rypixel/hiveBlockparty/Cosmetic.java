package net.rypixel.hiveBlockparty;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Cosmetic {
	public int slotNumber;
	public int price;
	public Material page;
	public String name;
	ItemStack item;
	
	Cosmetic(int slotNumber, int price, Material page, String name, ItemStack item) {
		this.slotNumber = slotNumber;
		this.price = price;
		this.page = page;
		this.name = name;
		this.item = item;
	}
	
	public ItemStack getItem() {
		return item;
	}
}
