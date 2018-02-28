package com.hepolite.race.ui;

import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Gui
{
	private final UUID owner;
	private final Inventory capabilities;

	public Gui(final Player owner)
	{
		this.owner = owner.getUniqueId();
		this.capabilities = Bukkit.createInventory(owner, 54);
	}

	/**
	 * Attempts to display the capabilities inventory to the owner of the gui
	 */
	public void openCapabilities()
	{
		buildCapabilitiesInvntory();
		getOwner().ifPresent(owner -> owner.openInventory(capabilities));
	}

	// ...

	private Optional<Player> getOwner()
	{
		return Optional.ofNullable(Bukkit.getPlayer(owner));
	}

	private void buildCapabilitiesInvntory()
	{
		capabilities.clear();
	}
}
