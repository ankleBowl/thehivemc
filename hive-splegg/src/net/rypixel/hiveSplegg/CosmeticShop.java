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
}
