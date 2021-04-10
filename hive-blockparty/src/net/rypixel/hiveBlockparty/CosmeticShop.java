package net.rypixel.hiveBlockparty;

import org.bukkit.event.inventory.InventoryClickEvent;

public class CosmeticShop {
	public static void blingShop(InventoryClickEvent event, BlockpartyPlayer hp) {
		String id = event.getInventory().getContents()[53].getType().toString() + String.valueOf(event.getSlot());
		Cosmetic c = Constants.cosmetics.get(id);
		if (c != null) {
			if (hp.ownedBlockpartyCosmetics.contains(c.name)) {
				if (hp.activeBling == id) {
					hp.activeBling = "";
				} else {
					hp.activeBling = id;
				}
			} else {
				if (hp.tokens > c.price) {
					hp.tokens -= c.price;
					hp.ownedBlockpartyCosmetics.add(c.name);
					hp.mcPlayer.openInventory(Constants.shopBling(hp));
				}
			}
		}
	}
}
