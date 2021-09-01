package com.virgilio.item.bank.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.virgilio.item.bank.ItemBank;
import com.virgilio.item.bank.Util;
import com.virgilio.item.bank.utils.User;

public class BankCommand implements CommandExecutor {

	private ItemBank plugin;

	public BankCommand(ItemBank plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		Player player = (Player) sender;
		if (!player.hasPermission("item.bank.user")) return false;
		if (args.length != 1) {
			if (!plugin.getConfig().getString("messages.usage").isEmpty())
				player.sendMessage(Util.color(plugin.getConfig().getString("messages.usage")));
			return false;
		}
		int vaultIndex = Integer.parseInt(args[0]) - 1;
		int vaults = Util.getVaults(player, plugin.getConfig().getInt("max-banks", 20));
		if (vaultIndex > vaults || vaultIndex < 0) {
			if (!plugin.getConfig().getString("messages.banks").isEmpty())
				player.sendMessage(Util.color(plugin.getConfig().getString("messages.banks")).replace("%banks%", vaults + 1 + ""));
			return false;
		}
		if (plugin.getData().getUser(player.getUniqueId()) == null) plugin.getData().loadUserData(player);
		User user = plugin.getData().getUser(player.getUniqueId());
		user.setOpenBank(vaultIndex);
		player.openInventory(plugin.getData().getUser(player.getUniqueId()).getBank(vaultIndex));
		if (!plugin.getConfig().getString("messages.open").isEmpty())
			player.sendMessage(Util.color(plugin.getConfig().getString("messages.open")).replace("%index%", vaultIndex + 1 + ""));
		return true;
	}

}
