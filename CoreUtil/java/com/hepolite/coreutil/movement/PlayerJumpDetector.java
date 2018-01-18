package com.hepolite.coreutil.movement;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import com.hepolite.api.event.IListener;
import com.hepolite.api.event.events.PlayerJumpEvent;

public final class PlayerJumpDetector implements IListener
{
	private final Map<UUID, Integer> jumpStatistics = new HashMap<>();

	/**
	 * When a player joins, the jump counter has to be tracked
	 */
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onEvent(final PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();
		jumpStatistics.put(player.getUniqueId(), player.getStatistic(Statistic.JUMP));
	}

	public void onTick()
	{
		for (final Player player : Bukkit.getOnlinePlayers())
		{
			if (updateStatistics(player) && isValidBlock(player))
				Bukkit.getServer().getPluginManager().callEvent(new PlayerJumpEvent(player));
		}
	}

	/**
	 * Updates the cached jump statistic if needed
	 * 
	 * @param player The player to update for
	 * @return True iff the cache had to be updated
	 */
	private boolean updateStatistics(final Player player)
	{
		final int jumps = player.getStatistic(Statistic.JUMP);
		if (jumps == jumpStatistics.get(player.getUniqueId()))
			return false;
		jumpStatistics.put(player.getUniqueId(), jumps);
		return true;
	}
	/**
	 * Checks if the player is in a valid jump block
	 * 
	 * @param player
	 * @return True iff the player is not in a ladder, vine, web, or fluids
	 */
	private boolean isValidBlock(final Player player)
	{
		final Block block = player.getLocation().getBlock();
		if (block.isLiquid())
			return false;
		switch (block.getType())
		{
		case LADDER:
		case VINE:
		case WEB:
			return false;
		default:
			return true;
		}
	}
}
