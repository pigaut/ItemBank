package com.virgilio.item.bank.handler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import com.virgilio.item.bank.ItemBank;
import com.virgilio.item.bank.Util;
import com.virgilio.item.bank.utils.User;

public class PlayerBankHandler implements Listener {

	private ItemBank plugin;

	public PlayerBankHandler(ItemBank plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if (!event.getPlayer().hasPermission("item.bank.user")) return;
			plugin.getData().loadUserData(event.getPlayer());
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if (!event.getPlayer().hasPermission("item.bank.user")) return;
		plugin.getData().unloadUserData(event.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		if (!event.getPlayer().hasPermission("item.bank.user")) return;
		User user = plugin.getData().getUser(event.getPlayer().getUniqueId());
		if (user == null) return;
		if(!user.hasBankOpened()) return;
		user.setBank(event.getInventory(), user.getOpenBank());
		if (!plugin.getConfig().getString("messages.close").isEmpty())
			event.getPlayer().sendMessage(Util.color(plugin.getConfig().getString("messages.close")).replace("%index%", user.getOpenBank() + 1 + ""));
		user.setOpenBank(-1);
	}
}
