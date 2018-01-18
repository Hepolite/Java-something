package com.hepolite.coreutil.movement;

import org.bukkit.entity.Player;

import com.hepolite.api.config.ConfigFactory;
import com.hepolite.api.config.IConfig;
import com.hepolite.api.event.HandlerCore;
import com.hepolite.api.plugin.PluginCore;

/**
 * Implements utility functions for player movement, player movement type detection and more
 */
public final class MovementHandler extends HandlerCore
{
	private final IConfig config;

	private final PlayerSpeedUpdater playerSpeedUpdater;
	private final PlayerFlightUpdater playerFlightUpdater;
	private final PlayerImpactDetector playerImpactDetector;
	private final PlayerJumpDetector playerJumpDetector;
	private final PlayerMoveDetector playerMoveDetector;

	public MovementHandler(final PluginCore plugin)
	{
		super(plugin);
		config = ConfigFactory.create(plugin, "Movement");

		playerFlightUpdater = new PlayerFlightUpdater(config);
		playerSpeedUpdater = register(new PlayerSpeedUpdater(config));
		playerImpactDetector = register(new PlayerImpactDetector(config));
		playerJumpDetector = register(new PlayerJumpDetector());
		playerMoveDetector = register(new PlayerMoveDetector());
	}

	@Override
	public void onTick(final int tick)
	{
		playerFlightUpdater.onTick();
		playerSpeedUpdater.onTick();
		playerJumpDetector.onTick();
		playerMoveDetector.onTick(tick);
		playerImpactDetector.onTick();
	}

	// ...

	/**
	 * Checks whether the player has changed their position within the previous second
	 * 
	 * @param player The player to check
	 * @return True iff the player has moved to a different block
	 */
	public boolean isPlayerMoving(final Player player)
	{
		return playerMoveDetector.isPlayerMoving(player);
	}
	/**
	 * Checks what type of motion the player is currently performing
	 * 
	 * @param player The player to check
	 * @return The movement type of the player
	 */
	public MovementType getMovementType(final Player player)
	{
		return playerMoveDetector.getMovementType(player);
	}

	/**
	 * Retrieves the distance the player has fallen in total
	 * 
	 * @param player The player to check
	 * @return The distance the player has fallen
	 */
	public float getFallDistance(final Player player)
	{
		return playerImpactDetector.getFallDistance(player);
	}
	/**
	 * Sets the total distance the player has fallen in total
	 * 
	 * @param player The player to check
	 * @param distance The distance the player is to have fallen
	 */
	public void setFallDistance(final Player player, final float distance)
	{
		playerImpactDetector.setFallDistance(player, distance);
	}
}
