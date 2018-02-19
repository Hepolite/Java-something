package com.hepolite.coreutil.hunger;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.hepolite.api.attribute.Attribute;
import com.hepolite.api.attribute.AttributeDatabase;
import com.hepolite.api.attribute.AttributeType;
import com.hepolite.api.damage.Damage;
import com.hepolite.api.damage.DamageAPI;
import com.hepolite.api.damage.DamageType;
import com.hepolite.api.damage.Heal;
import com.hepolite.api.damage.HealType;
import com.hepolite.api.event.HandlerCore;
import com.hepolite.api.event.events.PlayerHungerChange;
import com.hepolite.api.event.events.PlayerSaturationChange;
import com.hepolite.api.units.Time;
import com.hepolite.api.user.IUser;
import com.hepolite.api.user.UserFactory;
import com.hepolite.coreutil.CoreUtilPlugin;

public final class HungerHandler extends HandlerCore
{
	private final FoodRegistry foodRegistry;
	private final HungerRegistry hungerRegistry;
	private final GroupRegistry groupRegistry;

	private boolean ignoreDamageEvent = false;
	private boolean ignoreHealingEvent = false;

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
		for (final Player player : Bukkit.getOnlinePlayers())
		{
			if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)
				continue;

			final IUser user = UserFactory.fromPlayer(player);
			final HungerData data = getHungerData(user);
			final GroupData group = groupRegistry.getGroupData(data.group);

			if (tick % Time.TICKS_PER_SECOND == 0)
				consumeHunger(user, data, group);
			if (group.healingEnable && tick % group.healingFrequency.asTicks() == 0)
				handleHealing(user, data, group);
			if (group.starvationEnable && tick % group.starvationFrequency.asTicks() == 0)
				handleStarvation(user, data, group);
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
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerTakeDamage(final EntityDamageEvent event)
	{
		if (ignoreDamageEvent || event.getCause() != DamageCause.STARVATION)
			return;
		event.setCancelled(true);
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerHealDamage(final EntityRegainHealthEvent event)
	{
		if (ignoreHealingEvent || event.getRegainReason() != RegainReason.SATIATED)
			return;
		event.setCancelled(true);
	}

	// ...

	private void resetHunger(final IUser user)
	{
		final HungerData data = hungerRegistry.getHungerData(user);
		final GroupData group = groupRegistry.getGroupData(data.group);

		data.hunger = data.saturation = group.hungerMax;
		data.consumption = 0.0f;
	}
	private void updateHunger(final IUser user)
	{
		final Optional<Player> player = user.getPlayer();
		if (!player.isPresent())
			return;
		final Attribute maxHunger = AttributeDatabase.get(user, AttributeType.HUNGER_MAX);
		final HungerData data = hungerRegistry.getHungerData(user);

		player.get().setFoodLevel(Math.round(20.0f * data.hunger / maxHunger.getValue()));
		player.get().setSaturation(20.0f * data.saturation / maxHunger.getValue());
	}

	private void consumeHunger(final IUser user, final HungerData data, final GroupData group)
	{
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
	private void handleStarvation(final IUser user, final HungerData data, final GroupData group)
	{
		if (data.hunger > 0.0f)
			return;
		final Player player = user.getPlayer().get();
		ignoreDamageEvent = true;
		DamageAPI.damage(player, new Damage(DamageType.HUNGER, group.starvationDamage));
		ignoreDamageEvent = false;
	}
	private void handleHealing(final IUser user, final HungerData data, final GroupData group)
	{
		if (data.hunger <= group.healingStart)
			return;
		final Player player = user.getPlayer().get();
		ignoreHealingEvent = true;
		if (DamageAPI.heal(player, player, new Heal(HealType.SATIATED_REGEN, group.healingAmount)))
			changeSaturation(user, -group.healingCost);
		ignoreHealingEvent = false;
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
		updateHunger(user);
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
			data.saturation = Math.max(0.0f, Math.min(data.hunger, event.getNewSaturation()));
		else
		{
			data.saturation = 0.0f;
			changeHunger(user, event.getNewSaturation());
		}
		updateHunger(user);
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

	// ...

	/**
	 * Attempts to retrieve the food data associated with the given item, under the given group. The
	 * the food is not a valid food under the give group, the food will be checked against the
	 * default group. If the food has a custom name, the custom name will be prioritized, then the
	 * same search will be performed on the item type.
	 * 
	 * @param item The item to look up
	 * @param group The group to search under
	 * @return The food data associated with the item if any
	 */
	public Optional<FoodData> getFoodData(final ItemStack item, final String group)
	{
		return foodRegistry.getFoodData(item, group);
	}
	/**
	 * Attempts to retrieve the food data associated with the given item, under the given group. The
	 * the food is not a valid food under the give group, the food will be checked against the
	 * default group.
	 * 
	 * @param item The item to look up
	 * @param group The group to search under
	 * @return The food data associated with the item if any
	 */
	public Optional<FoodData> getFoodData(final String item, final String group)
	{
		return foodRegistry.getFoodData(item, group);
	}
}
