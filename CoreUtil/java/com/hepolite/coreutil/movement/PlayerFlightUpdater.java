package com.hepolite.coreutil.movement;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.hepolite.api.config.IConfig;
import com.hepolite.api.config.Property;
import com.hepolite.api.event.events.PlayerAllowFlightEvent;
import com.hepolite.api.user.IUser;
import com.hepolite.api.user.UserFactory;

public final class PlayerFlightUpdater
{
	private final Property PERMISSION_ADMIN_FLIGHT = new Property("permission.flight.admin");
	private final Property PERMISSION_BASIC_FLIGHT = new Property("permission.flight.basic");

	private final IConfig config;

	public PlayerFlightUpdater(final IConfig config)
	{
		this.config = config;
	}

	public void onTick()
	{
		for (final Player player : Bukkit.getOnlinePlayers())
		{
			if (hasAdminFlight(player))
				player.setAllowFlight(true);
			else
				handleBasicFlight(player);
		}
	}

	/**
	 * Checks if the given player is allowed to fly as an admin
	 * 
	 * @param player The player to check
	 * @return True iff the player is allowed to fly like an admin
	 */
	private boolean hasAdminFlight(final Player player)
	{
		final IUser user = UserFactory.fromPlayer(player);
		boolean permission = user.hasPermission(config.getString(PERMISSION_ADMIN_FLIGHT));
		permission |= player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR;
		return permission;
	}
	private void handleBasicFlight(final Player player)
	{
		final IUser user = UserFactory.fromPlayer(player);
		final boolean permission = user.hasPermission(config.getString(PERMISSION_BASIC_FLIGHT));

		final PlayerAllowFlightEvent event = new PlayerAllowFlightEvent(player, permission);
		Bukkit.getServer().getPluginManager().callEvent(event);
		if (event.allowFlight())
			player.setAllowFlight(true);
		else
		{
			player.setAllowFlight(false);
			player.setFlying(false);
		}
	}
}
