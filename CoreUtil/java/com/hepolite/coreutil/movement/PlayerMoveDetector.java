package com.hepolite.coreutil.movement;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import com.hepolite.api.event.IListener;
import com.hepolite.api.units.Time;

public final class PlayerMoveDetector implements IListener
{
	private final Map<UUID, Location> locations = new HashMap<>();
	private final Map<UUID, Location> prevLocations = new HashMap<>();

	/**
	 * When a player joins, the location has to be tracked
	 */
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onEvent(final PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();
		locations.put(player.getUniqueId(), player.getLocation());
	}

	public void onTick(final int tick)
	{
		if (tick % Time.TICKS_PER_SECOND == 0)
		{
			prevLocations.clear();
			prevLocations.putAll(locations);
			locations.clear();
			for (final Player player : Bukkit.getOnlinePlayers())
				locations.put(player.getUniqueId(), player.getLocation());
		}
	}

	/**
	 * Checks whether the player has changed their position within the previous second
	 * 
	 * @param player The player to check
	 * @return True iff the player has moved to a different block
	 */
	public boolean isPlayerMoving(final Player player)
	{
		final UUID uuid = player.getUniqueId();
		final Location old = prevLocations.get(uuid);
		final Location current = locations.get(uuid);
		if (old == null || current == null)
			return false;
		return !(old.getWorld().equals(current.getWorld()) && old.getBlockX() == current.getBlockX()
				&& old.getBlockY() == current.getBlockY() && old.getBlockZ() == current.getBlockZ());
	}
	/**
	 * Checks what type of motion the player is currently performing
	 * 
	 * @param player The player to check
	 * @return The movement type of the player
	 */
	public MovementType getMovementType(final Player player)
	{
		if (player.isFlying())
		{
			if (isPlayerMoving(player))
				return MovementType.FLYING;
			else
				return MovementType.HOVERING;
		}
		else if (player.isGliding())
			return MovementType.GLIDING;
		else if (player.getLocation().getBlock().isLiquid())
		{
			if (isPlayerMoving(player))
				return MovementType.SWIMMING;
			else
				return MovementType.FLOATING;
		}
		else if (player.isSprinting())
			return MovementType.RUNNING;
		else if (player.isSneaking())
			return MovementType.SNEAKING;
		else if (isPlayerMoving(player))
			return MovementType.WALKING;
		else
			return MovementType.STANDING;
	}
}
