package com.hepolite.coreutil.hunger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.hepolite.api.attribute.Attribute;
import com.hepolite.api.attribute.AttributeDatabase;
import com.hepolite.api.attribute.AttributeType;
import com.hepolite.api.event.HandlerCore;
import com.hepolite.api.event.events.PlayerHungerChange;
import com.hepolite.api.event.events.PlayerSaturationChange;
import com.hepolite.api.units.Time;
import com.hepolite.api.user.IUser;
import com.hepolite.api.user.UserFactory;
import com.hepolite.coreutil.CoreUtilPlugin;

public class HungerHandler extends HandlerCore
{
	public final FoodRegistry foodRegistry;
	private final HungerRegistry hungerRegistry;
	private final GroupRegistry groupRegistry;

	public HungerHandler(final JavaPlugin plugin)
	{
		super(plugin);
		foodRegistry = new FoodRegistry(plugin);
		hungerRegistry = new HungerRegistry(plugin);
		groupRegistry = new GroupRegistry(plugin);
	}

	@Override
	public void onTick(final int tick)
	{
		if (tick % Time.TICKS_PER_SECOND == 0)
		{
			for (final Player player : Bukkit.getOnlinePlayers())
				consumeHunger(UserFactory.fromPlayer(player));
		}

		if (tick % Time.TICKS_PER_HOUR == 0)
			hungerRegistry.saveData();
	}
	@Override
	public void onReload()
	{
		foodRegistry.loadFoods();
		groupRegistry.loadGroups();
	}
	@Override
	public void onDisable()
	{
		hungerRegistry.saveData();
	}

	// ...

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onPlayerJoin(final PlayerJoinEvent event)
	{
		final IUser user = UserFactory.fromPlayer(event.getPlayer());
		if (!hungerRegistry.hasHungerData(user))
			resetHunger(user);
		setUserGroup(user, hungerRegistry.getHungerData(user).group);
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onPlayerRespawn(final PlayerRespawnEvent event)
	{
		resetHunger(UserFactory.fromPlayer(event.getPlayer()));
	}

	private void consumeHunger(final IUser user)
	{
		final HungerData data = getHungerData(user);
		final GroupData group = groupRegistry.getGroupData(data.group);

		float targetConsumption = 0.0f;
		switch (CoreUtilPlugin.getMovementHandler().getMovementType(user.getPlayer().get()))
		{
		case FLOATING:
			targetConsumption = group.consumptionFloating;
			break;
		case FLYING:
			targetConsumption = group.consumptionFlying;
			break;
		case GLIDING:
			targetConsumption = group.consumptionGliding;
			break;
		case HOVERING:
			targetConsumption = group.consumptionHovering;
			break;
		case RUNNING:
			targetConsumption = group.consumptionRunning;
			break;
		case SNEAKING:
			targetConsumption = group.consumptionSneaking;
			break;
		case STANDING:
			targetConsumption = group.consumptionStanding;
			break;
		case SWIMMING:
			targetConsumption = group.consumptionSwimming;
			break;
		case WALKING:
			targetConsumption = group.consumptionWalking;
			break;
		default:
			CoreUtilPlugin.WARN("[HungerHandler] Illegal movement type");
		}

		data.consumption += group.consumptionChange * (targetConsumption - data.consumption);
		changeSaturation(user, -data.consumption);
	}
	private void resetHunger(final IUser user)
	{
		final HungerData data = hungerRegistry.getHungerData(user);
		final GroupData group = groupRegistry.getGroupData(data.group);

		data.hunger = data.saturation = group.hungerMax;
		data.consumption = 0.0f;
	}

	// ...

	/**
	 * Retrieves the hunger group the given user is associated with
	 * 
	 * @param user The user to look up
	 * @return The hunger group ID the user is associated with
	 */
	public String getUserGroup(final IUser user)
	{
		return hungerRegistry.getHungerData(user).group;
	}
	/**
	 * Assigns the hunger group the given user is associated with
	 * 
	 * @param user The user to look up
	 * @param group The group the user should be associated with
	 */
	public void setUserGroup(final IUser user, final String group)
	{
		final GroupData groupData = groupRegistry.getGroupData(group);
		final Attribute maxHunger = AttributeDatabase.get(user, AttributeType.HUNGER_MAX);

		hungerRegistry.getHungerData(user).group = group;
		maxHunger.setBaseValue(groupData.hungerMax).setMinValue(0.0f);
	}

	/**
	 * Changes the given user's hunger level by the given amount. This will not consume saturation
	 * first; if the final hunger value is less than the current saturation value, some saturation
	 * points will be lost.
	 * 
	 * @param user The user to change the hunger for
	 * @param amount The amount of hunger points to add to the user's account
	 */
	public void changeHunger(final IUser user, final float amount)
	{
		final Attribute maxHunger = AttributeDatabase.get(user, AttributeType.HUNGER_MAX);
		final HungerData data = hungerRegistry.getHungerData(user);

		final PlayerHungerChange event = post(new PlayerHungerChange(user, data.hunger, data.hunger + amount));
		if (event.isCancelled())
			return;

		data.hunger = Math.max(0.0f, Math.min(maxHunger.getValue(), event.getNewHunger()));
		data.saturation = Math.min(data.hunger, data.saturation);
	}
	/**
	 * Changes the given user's saturation level by the given amount. If the saturation change leads
	 * to a saturation value greater than the hunger value, the excess saturation points are lost.
	 * If the saturation is about to become negative, the hunger value will be drained instead.
	 * 
	 * @param user The user to change the saturation for
	 * @param amount The amount of saturation points to add to the user's account
	 */
	public void changeSaturation(final IUser user, final float amount)
	{
		final HungerData data = hungerRegistry.getHungerData(user);

		final PlayerSaturationChange event = post(
				new PlayerSaturationChange(user, data.saturation, data.saturation + amount));
		if (event.isCancelled())
			return;

		// If the user has enough saturation to sustain a saturation loss, simply update. Otherwise,
		// drain hunger
		if (event.getNewSaturation() >= 0.0f)
			data.saturation = Math.max(0.0f, Math.min(data.saturation, event.getNewSaturation()));
		else
		{
			data.saturation = 0.0f;
			changeHunger(user, event.getNewSaturation());
		}
	}

	// ...

	/**
	 * Retrieves the raw hunger data for the given user. Consider the returned object as immutable;
	 * use {@link HungerHandler} itself to modify any of the fields in the hunger data.
	 * 
	 * @param user The user to look up
	 * @return The hunger data associated with the given user
	 */
	public HungerData getHungerData(final IUser user)
	{
		return hungerRegistry.getHungerData(user);
	}
}
