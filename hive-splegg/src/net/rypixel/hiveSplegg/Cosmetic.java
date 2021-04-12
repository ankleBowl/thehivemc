package net.rypixel.hiveSplegg;

import java.util.Map;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class Cosmetic {
	public int slotNumber;
	public int price;
	public Material page;
	public String name;
	Map<String, Object> item;
	EntityType entity;
	Effect trail;
	String win;
	
	
	Cosmetic(int price, String name, Map<String, Object> item) {
		this.price = price;
		this.name = name;
		this.item = item;
	}
	
	public void setTrail(Effect t) {
		this.trail = t;
	}
	
	public void setEntity(EntityType e) {
		this.entity = e;
	}
	
	public void setWin(String w) {
		this.win = w;
	}
	
	public Map<String, Object> getItem() {
		return item;
	}
}
