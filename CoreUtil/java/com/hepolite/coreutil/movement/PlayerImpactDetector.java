package com.hepolite.coreutil.movement;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import com.hepolite.api.config.IConfig;
import com.hepolite.api.config.Property;
import com.hepolite.api.event.IListener;
import com.hepolite.api.event.events.PlayerImpactGroundEvent;

public final class PlayerImpactDetector implements IListener
{
	private final Property DRAG_FRACTION = new Property("fall.water.dragFraction");
	private final Property DRAG_FIXED = new Property("fall.water.dragFixed");

	private final Map<UUID, Float> fallDistances = new HashMap<>();
	private final IConfig config;

	public PlayerImpactDetector(final IConfig config)
	{
		this.config = config;
	}

	/**
	 * When a player joins, the fall distance has to be tracked
	 */
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onEvent(final PlayerJoinEvent event)
	{
		fallDistances.put(event.getPlayer().getUniqueId(), 0.0f);
	}

	public void onTick()
	{
		for (final Player player : Bukkit.getOnlinePlayers())
		{
			final float playerDistance = player.getFallDistance();
			final float actualDistance = getFallDistance(player);
			setFallDistance(player, playerDistance);

			if (actualDistance > 0.0f && playerDistance <= 0.0f)
				handleImpact(player, actualDistance);
		}
	}

	/**
	 * Retrieves the distance the player has fallen in total
	 * 
	 * @param player The player to check
	 * @return The distance the player has fallen
	 */
	public float getFallDistance(final Player player)
	{
		return fallDistances.get(player.getUniqueId());
	}
	/**
	 * Sets the total distance the player has fallen in total
	 * 
	 * @param player The player to check
	 * @param distance The distance the player is to have fallen
	 */
	public void setFallDistance(final Player player, final float distance)
	{
		fallDistances.put(player.getUniqueId(), distance);
	}

	/**
	 * Handles the case where the player's fall distance is not matching the actual distance.
	 * Typically this means the player has hit something, but also may mean the player is in a
	 * liquid.
	 * 
	 * @param player The player to handle
	 * @param actualDistance The actual distance the player has fallen
	 */
	private void handleImpact(final Player player, final float actualDistance)
	{
		final Block block = player.getLocation().getBlock();
		if (block.isLiquid())
			handleLiquidImpact(player, actualDistance);
		else
			handleSolidImpact(player, actualDistance);
	}
	private void handleLiquidImpact(final Player player, final float actualDistance)
	{
		// Calculate the reduced fall distance due to fluids
		final float fraction = config.getFloat(DRAG_FRACTION);
		final float fixed = config.getFloat(DRAG_FIXED);
		final float reducedDistance = Math.max(0.0f, (1.0f - fraction) * actualDistance - fixed);

		setFallDistance(player, reducedDistance);
		handleSolidImpact(player, reducedDistance);
	}
	private void handleSolidImpact(final Player player, final float actualDistance)
	{
		Block block = player.getLocation().getBlock();
		if (block.getType() == Material.AIR || block.isLiquid())
			block = block.getRelative(BlockFace.DOWN);
		if (block.getType() == Material.AIR || block.isLiquid())
			return;

		final Material type = block.getType();
		if (type != Material.LADDER && type != Material.VINE && !player.isFlying())
			Bukkit.getServer().getPluginManager().callEvent(new PlayerImpactGroundEvent(player, actualDistance));
		setFallDistance(player, 0.0f);
	}
}
