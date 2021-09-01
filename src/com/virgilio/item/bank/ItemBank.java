package com.virgilio.item.bank;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.virgilio.item.bank.commands.BankCommand;
import com.virgilio.item.bank.handler.BankDataHandler;
import com.virgilio.item.bank.handler.PlayerBankHandler;
import com.virgilio.item.bank.utils.User;

public class ItemBank extends JavaPlugin {

	private BankDataHandler data;

	@Override
	public void onEnable() {
		saveDefaultConfig();
		if (!new File(getDataFolder() + "/players").exists()) new File(getDataFolder() + "/players").mkdirs();
		data = new BankDataHandler(this);
		getServer().getPluginManager().registerEvents(new PlayerBankHandler(this), this);
		getCommand("bank").setExecutor(new BankCommand(this));
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.hasPermission("item.bank.user")) {
				data.loadUserData(player);
			}
		}
	}

	@Override
	public void onDisable() {
		for (User user : data.getUsers()) {
			data.unloadUserData(user);
		}
	}

	public BankDataHandler getData() {
		return data;
	}

	public void createFile(File file) {
		file.getParentFile().mkdirs();
		try {
			file.createNewFile();
		} catch (IOException e) {
		}
	}

}
