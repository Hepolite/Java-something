package com.hepolite.coreutil.movement;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import com.hepolite.api.attribute.Attribute;
import com.hepolite.api.attribute.AttributeDatabase;
import com.hepolite.api.attribute.AttributeType;
import com.hepolite.api.config.IConfig;
import com.hepolite.api.config.Property;
import com.hepolite.api.event.IListener;
import com.hepolite.api.user.IUser;
import com.hepolite.api.user.UserFactory;

public final class PlayerSpeedUpdater implements IListener
{
	private final Property SPEED_FLY = new Property("movement.speed.fly");
	private final Property SPEED_WALK = new Property("movement.speed.walk");

	private final IConfig config;

	public PlayerSpeedUpdater(final IConfig config)
	{
		this.config = config;
	}

	/**
	 * When a player joins, ensure that the movement attributes exists
	 */
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onEvent(final PlayerJoinEvent event)
	{
		final IUser user = UserFactory.fromPlayer(event.getPlayer());
		final Attribute speedFly = AttributeDatabase.get(user, AttributeType.SPEED_FLY);
		final Attribute speedWalk = AttributeDatabase.get(user, AttributeType.SPEED_WALK);

		speedFly.setBaseValue(config.getFloat(SPEED_FLY)).setMinValue(-1.0f).setMaxValue(1.0f);
		speedWalk.setBaseValue(config.getFloat(SPEED_WALK)).setMinValue(-1.0f).setMaxValue(1.0f);
	}

	public void onTick()
	{
		for (final Player player : Bukkit.getOnlinePlayers())
		{
			final IUser user = UserFactory.fromPlayer(player);
			player.setFlySpeed(AttributeDatabase.get(user, AttributeType.SPEED_FLY).getValue());
			player.setWalkSpeed(AttributeDatabase.get(user, AttributeType.SPEED_WALK).getValue());
		}
	}
}
