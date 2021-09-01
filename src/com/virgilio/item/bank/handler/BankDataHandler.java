package com.virgilio.item.bank.handler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.virgilio.item.bank.ItemBank;
import com.virgilio.item.bank.Util;
import com.virgilio.item.bank.utils.User;

public class BankDataHandler {

	private ItemBank plugin;
	private String dataFolder;

	private Set<User> users;

	public BankDataHandler(ItemBank plugin) {
		this.plugin = plugin;
		this.users = new HashSet<User>();
		this.dataFolder = plugin.getDataFolder() + "";
		initializeAutoSaveTask();
	}

	private void initializeAutoSaveTask() {
		Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
			@Override
			public void run() {
				if (!users.isEmpty()) {
					for (User user : users) {
						File file = new File(dataFolder + "/players", user.getPlayerId().toString() + ".yml");
						YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
						int index = 0;
						for (Inventory bank : user.getBanks()) {
							if (bank.firstEmpty() == 0) continue;
							config.set(index + "", bank.getContents());
							index++;
						}
						try {
							config.save(file);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}, plugin.getConfig().getLong("auto-save", 3600), plugin.getConfig().getLong("auto-save", 3600));
	}

	public void loadUserData(Player player) {
		File file = new File(dataFolder + "/players", player.getUniqueId().toString() + ".yml");
		if (!file.exists()) plugin.createFile(file);
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		List<Inventory> banks = new ArrayList<Inventory>();
		for (int i = 0; i <= Util.getVaults(player, plugin.getConfig().getInt("max-banks", 20)); i++) {
			if (config.isSet(i + "")) {
				@SuppressWarnings("unchecked")
				ItemStack[] contents = ((List<ItemStack>) config.get(i + "")).toArray(new ItemStack[0]);
				Inventory bank = Bukkit.createInventory(null, Util.getRows(player, plugin.getConfig().getInt("default-rows", 3)) * 9, Util.color(plugin.getConfig().getString("title")).replace("%player%", player.getName()).replace("%bank%", i + 1 + ""));
				bank.setContents(contents);
				banks.add(bank);
			} else {
				banks.add(Bukkit.createInventory(null, Util.getRows(player, plugin.getConfig().getInt("default-rows", 3)) * 9, Util.color(plugin.getConfig().getString("title")).replace("%player%", player.getName()).replace("%bank%", i + 1 + "")));
			}
		}
		addUser(new User(player.getUniqueId(), banks));
	}

	public void unloadUserData(UUID playerId) {
		unloadUserData(getUser(playerId));
	}

	public void unloadUserData(User user) {
		if (user == null) return;
		File file = new File(plugin.getDataFolder() + "/players", user.getPlayerId().toString() + ".yml");
		if (!file.exists()) plugin.createFile(file);
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		int index = 0;
		for (Inventory bank : getUser(user.getPlayerId()).getBanks()) {
			if (bank.firstEmpty() == 0) continue;
			config.set(index + "", bank.getContents());
			index++; 
		} 
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		users.remove(user);
	}

	public Set<User> getUsers() {
		return users;
	}

	public User getUser(UUID playerId) {
		for (User user : users) {
			if (user.getPlayerId().equals(playerId)) return user;
		}
		return null;
	}

	public void removeUser(User user) {
		if (users.contains(user)) users.remove(user);
	}

	public void addUser(User user) {
		users.add(user);
	}
}
