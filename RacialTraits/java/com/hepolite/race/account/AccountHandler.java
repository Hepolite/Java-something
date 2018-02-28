package com.hepolite.race.account;

import java.util.Optional;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.hepolite.api.config.Config;
import com.hepolite.api.config.IConfig;
import com.hepolite.api.config.Property;
import com.hepolite.api.event.HandlerCore;
import com.hepolite.api.units.Time;

public final class AccountHandler extends HandlerCore
{
	private final AccountMap accounts;
	private final IConfig accountConfig;

	public AccountHandler(final JavaPlugin plugin)
	{
		super(plugin);
		accountConfig = new Config(plugin.getDataFolder(), "accounts");
		accounts = accountConfig.getValue(new Property("accounts"), new AccountMap());
	}

	@Override
	public void onTick(final int tick)
	{
		if (tick % Time.TICKS_PER_HOUR == 0)
			saveData();
	}
	@Override
	public void onDisable()
	{
		saveData();
	}

	// ...

	/**
	 * Stores all account data associated with all players to the config file
	 */
	private void saveData()
	{
		accountConfig.set(new Property("accounts"), accounts);
		accountConfig.saveToDisk();
	}

	// ...

	/**
	 * Retrieves the currently active account associated with the given player, creating it if it
	 * does not exist
	 * 
	 * @param player The player to look up
	 * @return The active account of the player if it exists
	 */
	public Account getAccount(final Player player)
	{
		final UUID uuid = player.getUniqueId();
		if (!accounts.containsKey(uuid))
			accounts.put(uuid, new Account());
		return accounts.get(uuid);
	}
	/**
	 * Retrieves the currently active account data of the given id that is associated with the given
	 * player
	 * 
	 * @param player The player to look up
	 * @return The account data if it exists
	 */
	public Optional<Account.Data> getAccountData(final Player player)
	{
		return getAccount(player).getData();
	}
	/**
	 * Retrieves the account data of the given id that is associated with the given player
	 * 
	 * @param player The player to look up
	 * @param id The id of the account data to grab
	 * @return The account data if it exists
	 */
	public Optional<Account.Data> getAccountData(final Player player, final int id)
	{
		return getAccount(player).getData(id);
	}
}
