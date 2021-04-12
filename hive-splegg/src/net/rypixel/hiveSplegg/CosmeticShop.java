package net.rypixel.hiveSplegg;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;

public class CosmeticShop {
	public static void gunShop(InventoryClickEvent event, SpleggPlayer hp) {
		String id = event.getInventory().getContents()[53].getType().toString() + String.valueOf(event.getSlot());
		Cosmetic c = Constants.guns.get(id);
		Bukkit.broadcastMessage(id);
		if (c != null) {
			if (hp.spleggCosmetics.contains(c.name)) {
				if (hp.activeGun == id) {
					hp.activeGun = "null";
				} else {
					hp.activeGun = id;
				}
			} else {
				if (hp.tokens > c.price) {
					hp.tokens -= c.price;
					hp.spleggCosmetics.add(c.name);
					hp.mcPlayer.openInventory(Constants.shopGuns(hp));
				}
			}
		}
	}
	
	public static void entityShop(InventoryClickEvent event, SpleggPlayer hp) {
		String id = event.getInventory().getContents()[53].getType().toString() + String.valueOf(event.getSlot());
		Cosmetic c = Constants.entities.get(id);
		Bukkit.broadcastMessage(id);
		if (c != null) {
			if (hp.spleggCosmetics.contains(c.name)) {
				if (hp.activeMob == id) {
					hp.activeMob = "null";
				} else {
					hp.activeMob = id;
				}
			} else {
				if (hp.tokens > c.price) {
					hp.tokens -= c.price;
					hp.spleggCosmetics.add(c.name);
					hp.mcPlayer.openInventory(Constants.shopEntities(hp));
				}
			}
		}
	}
	
	public static void trailShop(InventoryClickEvent event, SpleggPlayer hp) {
		String id = event.getInventory().getContents()[53].getType().toString() + String.valueOf(event.getSlot());
		Cosmetic c = Constants.trails.get(id);
		Bukkit.broadcastMessage(id);
		if (c != null) {
			if (hp.spleggCosmetics.contains(c.name)) {
				if (hp.activeTrails == id) {
					hp.activeTrails = "null";
				} else {
					hp.activeTrails = id;
				}
			} else {
				if (hp.tokens > c.price) {
					hp.tokens -= c.price;
					hp.spleggCosmetics.add(c.name);
					hp.mcPlayer.openInventory(Constants.shopTrails(hp));
				}
			}
		}
	}
	
	public static void winShop(InventoryClickEvent event, SpleggPlayer hp) {
		String id = event.getInventory().getContents()[53].getType().toString() + String.valueOf(event.getSlot());
		Cosmetic c = Constants.wins.get(id);
		Bukkit.broadcastMessage(id);
		if (c != null) {
			if (hp.spleggCosmetics.contains(c.name)) {
				if (hp.activeCelebration == id) {
					hp.activeCelebration = "null";
				} else {
					hp.activeCelebration = id;
				}
			} else {
				if (hp.tokens > c.price) {
					hp.tokens -= c.price;
					hp.spleggCosmetics.add(c.name);
					hp.mcPlayer.openInventory(Constants.shopTrails(hp));
				}
			}
		}
	}
}
