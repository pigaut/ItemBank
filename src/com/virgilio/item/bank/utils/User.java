package com.virgilio.item.bank.utils;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class User {

	private UUID playerId;
	private List<Inventory> banks;
	private int openBank = -1;

	public User(UUID playerId, List<Inventory> banks) {
		this.playerId = playerId;
		this.banks = banks;
	}

	public UUID getPlayerId() {
		return playerId;
	}
	
	public Player getPlayer() {
		if (Bukkit.getOfflinePlayer(playerId).isOnline()) return Bukkit.getPlayer(playerId);
		return null;
	}
	
	public void setBank(Inventory bank, Integer index) {
		banks.set(index, bank);
	}
	
	public void setBanks(List<Inventory> banks) {
		this.banks = banks;
	}
	
	public List<Inventory> getBanks() {
		return banks;
	}
	
	public Inventory getBank(int vaultIndex) {
		return banks.get(vaultIndex);
	}
	
	public boolean hasBankOpened() {
		return openBank != -1;
	}
	
	public int getOpenBank() {
		return openBank;
	}
	
	public void setOpenBank(int index) {
		openBank = index;
	}
}
