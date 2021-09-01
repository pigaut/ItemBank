package com.virgilio.item.bank;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import net.md_5.bungee.api.ChatColor;

public class Util {

	public static String color(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	public static boolean isStrInt(String string) {
		try {
			Integer.parseInt(string);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public static void saveFile(YamlConfiguration config, File file) {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static int getVaults(Player player, int maxVaults) {
		if (player.isOp()) return maxVaults - 1;
		for (PermissionAttachmentInfo perm : player.getEffectivePermissions()) {
			if (perm.getPermission().startsWith("item.bank.vaults.")) {
				String[] split = perm.getPermission().split(".");
				if (Integer.parseInt(split[3]) > maxVaults) return maxVaults - 1;
				return Integer.parseInt(split[3]) - 1;
			}
		}
		return 1;
	}
	
	public static int getRows(Player player, int defaultRows) {
		if (player.isOp()) return defaultRows;
		for (PermissionAttachmentInfo perm : player.getEffectivePermissions()) {
			if (perm.getPermission().startsWith("item.bank.vaults.")) {
				String[] split = perm.getPermission().split(".");
				if (Integer.parseInt(split[3]) > 6) return defaultRows;
				return Integer.parseInt(split[3]);
			}
		}
		return 0;
	}
}
